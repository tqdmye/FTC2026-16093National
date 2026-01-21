package org.firstinspires.ftc.teamcode.Subsystems;//package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Subsystems.Constants.SensorConstants;
import org.firstinspires.ftc.teamcode.Subsystems.driving.NewMecanumDrive;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Vision{
    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTag;
    private AprilTagDetection desiredTag = null;

    public boolean targetFound     = false;    // Set to true when an AprilTag target is detected
    double  strafe          = 0;        // Desired strafe power/speed (-1 to +1)
    double  drive           = 0;        // Desired forward power/speed (-1 to +1) +ve is forward
    double  turn            = 0;        // Desired turning power/speed (-1 to +1) +ve is CounterClockwise

    // “到位”判定阈值（单位：cm / deg）
    public static double RANGE_TOLERANCE_CM = 1.5;
    public static double BEARING_TOLERANCE_DEG = 2.0;
    public static double YAW_TOLERANCE_DEG = 2.5;

    // 到位后锁存，直到操作手松开自瞄（withOutVision）
    private boolean arrived = false;

    public Vision(Telemetry telemetry, final HardwareMap hardwareMap) {
        aprilTag = new AprilTagProcessor.Builder()
                .setOutputUnits(DistanceUnit.CM, AngleUnit.DEGREES)
                .build();

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // eg: Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second
        // Note: Decimation can be changed on-the-fly to adapt during a match.
        aprilTag.setDecimation(2);

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(aprilTag)
                .build();
        setManualExposure(telemetry,6, 250);  // Use low exposure time to reduce motion blur
    }

    public boolean isArrived() {
        return arrived;
    }

    public void driveWithVision(NewMecanumDrive driveCore, Telemetry telemetry, boolean isDriveWithVision) {
        // 每帧都重置，避免“看过一次就一直认为能看到”的粘住问题
        this.drive = 0;
        this.strafe = 0;
        this.turn = 0;
        targetFound = false;
        desiredTag  = null;

        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                targetFound = true;
                desiredTag = detection;
                break;
            }
        }

        // Tell the driver what we see, and what to do.
        if (targetFound) {
            telemetry.addData("\n>","HOLD Left-Bumper to Drive to Target\n");
            telemetry.addData("Found", "ID %d (%s)", desiredTag.id, desiredTag.metadata.name);
            telemetry.addData("Range",  "%5.1f centimeters", desiredTag.ftcPose.range);
            telemetry.addData("Bearing","%3.0f degrees", desiredTag.ftcPose.bearing);
            telemetry.addData("Yaw","%3.0f degrees", desiredTag.ftcPose.yaw);
        } else {
            telemetry.addData("\n>","Drive using joysticks to find valid target\n");
        }

        // 自瞄模式下：没看到 tag 就立刻停下，不再沿着旧数据继续走
        if (isDriveWithVision && !targetFound) {
            arrived = false;
            moveRobotVision(driveCore, 0, 0, 0);
            telemetry.addLine("Vision: LOST TAG -> STOP");
            telemetry.update();
            return;
        }

        // If vision drive is enabled AND we have found a target, drive automatically.
        if (isDriveWithVision && targetFound) {

            // Determine heading and range error so we can use them to control the robot automatically.
            double  rangeError   = (desiredTag.ftcPose.range - SensorConstants.VISION_DESIRED_DISTANCE.value);
            double  headingError = desiredTag.ftcPose.bearing;
            double  yawError        = desiredTag.ftcPose.yaw;

            // 到位判定：到位就停下，并向上层提供 arrived=true
            if (Math.abs(rangeError) < RANGE_TOLERANCE_CM
                    && Math.abs(headingError) < BEARING_TOLERANCE_DEG
                    && Math.abs(yawError) < YAW_TOLERANCE_DEG) {
                arrived = true;
                moveRobotVision(driveCore, 0, 0, 0);
                telemetry.addLine("Vision: ARRIVED");
                telemetry.update();
                return;
            } else {
                arrived = false;
            }

            // Use the speed and turn "gains" to calculate how we want the robot to move.  Clip it to the maximum
            drive = Range.clip(rangeError * SensorConstants.VISION_SPEED_GAIN.value, -SensorConstants.VISION_MAX_AUTO_SPEED.value, SensorConstants.VISION_MAX_AUTO_SPEED.value);
            strafe = Range.clip(-yawError * SensorConstants.VISION_STRAFE_GAIN.value, -SensorConstants.VISION_MAX_AUTO_STRAFE.value, SensorConstants.VISION_MAX_AUTO_STRAFE.value);
            turn  = Range.clip(headingError * SensorConstants.VISION_TURN_GAIN.value, -SensorConstants.VISION_MAX_AUTO_TURN.value, SensorConstants.VISION_MAX_AUTO_TURN.value) ;

            telemetry.addData("Auto","Drive %5.2f, Turn %5.2f", this.drive, turn);
        }
        telemetry.update();

        moveRobotVision(driveCore, drive, strafe, turn);
    }

    public void moveRobotVision(NewMecanumDrive drive, double x, double y, double yaw) {
        // Calculate wheel powers.
        double frontLeftPower    =  x - y - yaw;
        double frontRightPower   =  x + y + yaw;
        double backLeftPower     =  x + y - yaw;
        double backRightPower    =  x - y + yaw;

        // Normalize wheel powers to be less than 1.0
        double max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        max = Math.max(max, Math.abs(backLeftPower));
        max = Math.max(max, Math.abs(backRightPower));

        if (max > 1.0) {
            frontLeftPower /= max;
            frontRightPower /= max;
            backLeftPower /= max;
            backRightPower /= max;
        }

        // Send powers to the wheels.
        drive.leftFront.setPower(frontLeftPower);
        drive.rightFront.setPower(frontRightPower);
        drive.leftRear.setPower(backLeftPower);
        drive.rightRear.setPower(backRightPower);
    }

    private void setManualExposure(Telemetry telemetry, int exposureMS, int gain) {
        // Wait for the camera to be open, then use the controls

        if (visionPortal == null) {
            return;
        }

        // Make sure camera is streaming before we try to set the exposure controls
        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            telemetry.addData("Camera", "Waiting");
            telemetry.update();
            while (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
                sleep(20);
            }
            telemetry.addData("Camera", "Ready");
            telemetry.update();
        }

        ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
        if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
            exposureControl.setMode(ExposureControl.Mode.Manual);
            sleep(50);
        }
        exposureControl.setExposure((long)exposureMS, TimeUnit.MILLISECONDS);
        sleep(20);
        GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
        gainControl.setGain(gain);
        sleep(20);
        telemetry.addData("Camera", "Ready");
        telemetry.update();
    }

    public final void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public final void withOutVision(){
        drive = 0;
        strafe = 0;
        turn = 0;
        targetFound = false;
        desiredTag  = null;
        arrived = false;
    }
}
