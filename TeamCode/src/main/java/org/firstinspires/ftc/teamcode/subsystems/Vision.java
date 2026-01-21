package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
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

public class Vision {

    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTag;
    private AprilTagDetection desiredTag = null;

    public boolean targetFound = false;

    double drive = 0;
    double strafe = 0;
    double turn = 0;

    // ======== 目标约束（唯一解）========
    private static final double TARGET_FORWARD_CM = 300.0;
    private static final double TARGET_STRAFE_CM  = -50.0;
    private static final double TARGET_YAW_DEG    = 20.0;

    // 到位阈值
    private static final double YPOS_TOLERANCE_CM = 2.0;
    private static final double XPOS_TOLERANCE_CM = 2.0;
    private static final double YAW_TOLERANCE_DEG = 2.5;

    private boolean arrived = false;

    public Vision(Telemetry telemetry, final HardwareMap hardwareMap) {

        aprilTag = new AprilTagProcessor.Builder()
                .setOutputUnits(DistanceUnit.CM, AngleUnit.DEGREES)
                .build();

        aprilTag.setDecimation(2);

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(aprilTag)
                .build();

        setManualExposure(telemetry, 6, 250);
    }

    public boolean isArrived() {
        return arrived;
    }

    public void driveWithVision(NewMecanumDrive driveCore,
                                Telemetry telemetry,
                                boolean isDriveWithVision) {

        drive = 0;
        strafe = 0;
        turn = 0;
        targetFound = false;
        desiredTag = null;

        List<AprilTagDetection> detections = aprilTag.getDetections();
        for (AprilTagDetection d : detections) {
            if (d.metadata != null) {
                targetFound = true;
                desiredTag = d;
                break;
            }
        }

        if (isDriveWithVision && !targetFound) {
            arrived = false;
            moveRobotVision(driveCore, 0, 0, 0);
            telemetry.addLine("Vision: LOST TAG");
            telemetry.update();
            return;
        }

        if (isDriveWithVision && targetFound) {

            // ======== 唯一解误差（Tag 坐标系）========
            double forwardError = desiredTag.ftcPose.y - TARGET_FORWARD_CM;
            double strafeError  = desiredTag.ftcPose.x - TARGET_STRAFE_CM;
            double yawError     = desiredTag.ftcPose.yaw - TARGET_YAW_DEG;

            // ======== 到位判定 ========
            if (Math.abs(forwardError) < YPOS_TOLERANCE_CM &&
                    Math.abs(strafeError)  < XPOS_TOLERANCE_CM &&
                    Math.abs(yawError)     < YAW_TOLERANCE_DEG) {

                arrived = true;
                moveRobotVision(driveCore, 0, 0, 0);
                telemetry.addLine("Vision: ARRIVED (UNIQUE POSE)");
                telemetry.update();
                return;
            } else {
                arrived = false;
            }

            // ======== 闭环控制 ========
            drive = Range.clip(
                    forwardError * SensorConstants.VISION_SPEED_GAIN.value,
                    -SensorConstants.VISION_MAX_AUTO_SPEED.value,
                    SensorConstants.VISION_MAX_AUTO_SPEED.value);

            strafe = Range.clip(
                    strafeError * SensorConstants.VISION_STRAFE_GAIN.value,
                    -SensorConstants.VISION_MAX_AUTO_STRAFE.value,
                    SensorConstants.VISION_MAX_AUTO_STRAFE.value);

            turn = Range.clip(
                    yawError * SensorConstants.VISION_TURN_GAIN.value,
                    -SensorConstants.VISION_MAX_AUTO_TURN.value,
                    SensorConstants.VISION_MAX_AUTO_TURN.value);

            telemetry.addData("Err F/S/Y",
                    "%.1f / %.1f / %.1f",
                    forwardError, strafeError, yawError);
        }

        telemetry.update();
        moveRobotVision(driveCore, drive, strafe, turn);
    }

    public void moveRobotVision(NewMecanumDrive drive, double x, double y, double yaw) {

        double fl = x - y - yaw;
        double fr = x + y + yaw;
        double bl = x + y - yaw;
        double br = x - y + yaw;

        double max = Math.max(Math.max(Math.abs(fl), Math.abs(fr)),
                Math.max(Math.abs(bl), Math.abs(br)));

        if (max > 1.0) {
            fl /= max; fr /= max; bl /= max; br /= max;
        }

        drive.leftFront.setPower(fl);
        drive.rightFront.setPower(fr);
        drive.leftRear.setPower(bl);
        drive.rightRear.setPower(br);
    }

    private void setManualExposure(Telemetry telemetry, int exposureMS, int gain) {

        while (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            sleep(20);
        }

        ExposureControl exposure = visionPortal.getCameraControl(ExposureControl.class);
        exposure.setMode(ExposureControl.Mode.Manual);
        exposure.setExposure(exposureMS, TimeUnit.MILLISECONDS);

        GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
        gainControl.setGain(gain);
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }

    public void withOutVision() {
        drive = 0;
        strafe = 0;
        turn = 0;
        targetFound = false;
        desiredTag = null;
        arrived = false;
    }
}
