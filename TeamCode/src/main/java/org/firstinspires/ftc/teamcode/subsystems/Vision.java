package org.firstinspires.ftc.teamcode.Subsystems;

import android.util.Size;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import java.util.List;

@Config
public class Vision {
    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTag;
    public boolean targetFound = false;

    // --- 经过实战优化的参数 ---
    public static double kP = 0.04;      // 0.035 代表 10度偏差产生 0.35 功率，会被限制在 MAX_TURN
    public static double kI = 0.005;     // 极小的 I，只负责微调
    public static double kD = 5;      // 增加 D 项，抵消卡尔曼滤波带来的延迟感

    public static double kI_ZONE = 8.0;  // 只有在误差小于 8 度时才开启积分，防止远距离积累
    public static double kI_LIMIT = 0.05;// iTerm 贡献的最大功率限额

    public static double MIN_TURN = 0.01;
    public static double MAX_TURN = 0.2;
    public static double DEADZONE = 1.5;
    public static double TOLERANCE = 1.5;

    // 卡尔曼滤波参数
    public static double Q = 0.1;
    public static double R = 0.4;
    private double x_est = 0, p_est = 1;

    private double lastError = 0;
    private double integral = 0;

    public Vision(Telemetry telemetry, HardwareMap hardwareMap) {
        aprilTag = new AprilTagProcessor.Builder()
                .setOutputUnits(DistanceUnit.CM, AngleUnit.DEGREES)
                .build();
        aprilTag.setDecimation(1);

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(aprilTag)
                .setCameraResolution(new Size(640, 480))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .build();
    }

    private double kalmanUpdate(double measurement) {
        double p_pred = p_est + Q;
        double K = p_pred / (p_pred + R);
        x_est = x_est + K * (measurement - x_est);
        p_est = (1 - K) * p_pred;
        return x_est;
    }

    public double getPIDAutoAlignTurn(Telemetry telemetry) {
        double error = getFilteredHeadingError();
        if (!targetFound) { resetPID(); return 0; }

        // 1. 死区逻辑
        if (Math.abs(error) < DEADZONE) {
            integral = 0;
            return 0;
        }

        double turnPower = 0;
        // 2. 误差在容差之外才进行计算
        if (Math.abs(error) > TOLERANCE) {
            // P 项
            double pTerm = error * kP;

            // I 项：积分分离逻辑
            if (Math.abs(error) < kI_ZONE) {
                integral += error;
            } else {
                integral = 0; // 远距离不积分
            }
            double iTerm = Range.clip(integral * kI, -kI_LIMIT, kI_LIMIT);

            // D 项
            double dTerm = (error - lastError) * kD;

            turnPower = pTerm + iTerm + dTerm;
            turnPower += Math.signum(turnPower) * MIN_TURN;
            turnPower = Range.clip(turnPower, -MAX_TURN, MAX_TURN);
        } else {
            integral = 0;
        }

        lastError = error;
        telemetry.addData("Filtered Error", "%.2f", error);
        telemetry.addData("iTerm", "%.3f", integral * kI);
        return -turnPower;
    }

    public double getRawHeadingError() {
        List<AprilTagDetection> detections = aprilTag.getDetections();
        targetFound = false;
        if (detections != null) {
            for (AprilTagDetection d : detections) {
                if (d.metadata != null) {
                    targetFound = true;
                    double yTotal = d.ftcPose.y + Math.cos(Math.toRadians(d.ftcPose.yaw)) * 16;
                    double xTotal = d.ftcPose.x + Math.sin(Math.toRadians(d.ftcPose.yaw)) * 16;
                    return Math.toDegrees(Math.atan2(xTotal, yTotal));
                }
            }
        }
        return 0;
    }

    public double getFilteredHeadingError() {
        double raw = getRawHeadingError();
        return targetFound ? kalmanUpdate(raw) : x_est;
    }

    public void resetPID() {
        lastError = 0; integral = 0;
        x_est = 0; p_est = 1;
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