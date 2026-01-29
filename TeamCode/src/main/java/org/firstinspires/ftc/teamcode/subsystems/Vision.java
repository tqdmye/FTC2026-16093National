package org.firstinspires.ftc.teamcode.Subsystems;

import android.util.Size;

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
import org.firstinspires.ftc.teamcode.commands.autos.driveAutoCommand;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.acmerobotics.dashboard.config.Config;

@Config
public class Vision {
    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTag;
    private AprilTagDetection desiredTag = null;

    public boolean targetFound = false;

    // --- PID 实战参数 (根据调试结果修改) ---
    public static double kP = 0.5;
    public static double kI = 0.08;
    public static double kD = 6;
    public static double kI_LIMIT = 0.2; // 积分限幅
    public static double MIN_TURN = 0.01;
    public static double MAX_TURN = 0.2;
    public static double TOLERANCE = 3; // 度

    private double lastError = 0;
    private double integral = 0;
    private double lastFilteredError = 0;
    public static double alpha = 0.1; // 滤波系数

    public Vision(Telemetry telemetry, HardwareMap hardwareMap) {
        aprilTag = new AprilTagProcessor.Builder()
                .setOutputUnits(DistanceUnit.CM, AngleUnit.DEGREES)
                .build();
        aprilTag.setDecimation(1);

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setCameraResolution(new Size(640, 480))
                .addProcessor(aprilTag)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG) // 提升到30FPS
                .enableLiveView(true)
                .build();

        setManualExposure(telemetry, 3, 200);
    }

    /**
     * 实战调用：获取 PID 转向功率
     */
    public double getPIDAutoAlignTurn(Telemetry telemetry) {
        double error = getFilteredHeadingError();

        if (!targetFound) {
            resetPID();
            return 0.0;
        }

        double turnPower = 0;
        if (Math.abs(error) > TOLERANCE) {
            // P
            double pTerm = error * kP;
            // I (含抗饱和限幅)
            integral += error;
            double iTerm = Range.clip(integral * kI, -kI_LIMIT, kI_LIMIT);
            // D
            double dTerm = (error - lastError) * kD;

            turnPower = pTerm + iTerm + dTerm;
            // 摩擦力补偿
            turnPower += Math.signum(turnPower) * MIN_TURN;
            // 输出限幅
            turnPower = Range.clip(turnPower, -MAX_TURN, MAX_TURN);
        } else {
            integral = 0;
        }

        lastError = error;
        telemetry.addData("Vision Error", "%.2f deg", error);
        return -turnPower; // 根据底盘朝向可能需要去负号
    }

    public double getRawHeadingError() {
        List<AprilTagDetection> detections = aprilTag.getDetections();
        targetFound = false;
        if (detections != null) {
            for (AprilTagDetection d : detections) {
                if (d.metadata != null) {
                    targetFound = true;
                    double yTotal = d.ftcPose.y + Math.cos(d.ftcPose.yaw) * 16;
                    double xTotal = d.ftcPose.x + Math.sin(d.ftcPose.yaw) * 16;
                    return Math.toDegrees(Math.atan2(xTotal, yTotal));
                }
            }
        }
        return 0;
    }

    public double getFilteredHeadingError() {
        double rawError = getRawHeadingError();
        if (!targetFound) {
            lastFilteredError = 0;
            return 0;
        }
        double filteredError = alpha * rawError + (1.0 - alpha) * lastFilteredError;
        lastFilteredError = filteredError;
        return filteredError;
    }

    public void resetPID() {
        lastError = 0;
        integral = 0;
        lastFilteredError = 0;
    }

    private void setManualExposure(Telemetry telemetry, int exposureMS, int gainValue) {
        while (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            sleep(20);
        }
        ExposureControl exposure = visionPortal.getCameraControl(ExposureControl.class);
        exposure.setMode(ExposureControl.Mode.Manual);
        exposure.setExposure(exposureMS, TimeUnit.MILLISECONDS);
        GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
        gainControl.setGain(gainValue);
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }

//    public Command turnToAprilTag(Follower follower){
//        double yTotal = desiredTag.ftcPose.y + Math.cos(desiredTag.ftcPose.yaw)*16;
//        double xTotal = desiredTag.ftcPose.x + Math.sin(desiredTag.ftcPose.yaw)*16;
//        double headingTotal = Math.atan2(xTotal, yTotal);  //heading total （rad）
//        PathChain turnToTag = follower.pathBuilder()
//                .addPath(new BezierLine(
//                        follower.getPose(),
//                        new Pose(follower.getPose().getX(), follower.getPose().getY(), follower.getPose().getHeading() + headingTotal)))
//                .setLinearHeadingInterpolation(follower.getHeading(), follower.getHeading() + headingTotal)
//                .build();
//        return new driveAutoCommand(follower, turnToTag);
//    }
}
