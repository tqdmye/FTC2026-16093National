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
    private boolean arrived = false;

    /* ================== Target Definition ================== */

    // 目标点（Tag 坐标系）
    private static final double TARGET_FORWARD_CM = 115.0; // y
    private static final double TARGET_STRAFE_CM  = -11.8; // x

    // Heading 到位阈值
    private static final double HEADING_TOLERANCE_DEG = 0.8;

    // 防止 atan2 数值抖动
    private static final double MIN_DY_CM = 5.0;

    /* ================== Output ================== */

    private double turn = 0.0;

    /* ================== Constructor ================== */

    public Vision(Telemetry telemetry, HardwareMap hardwareMap) {

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

    /**
     * 只根据 AprilTag 的 x / y 计算 Heading
     * drive / strafe 永远为 0
     */
    public void aimHeadingOnly(NewMecanumDrive driveCore,
                               Telemetry telemetry,
                               boolean enableVision) {

        turn = 0.0;
        targetFound = false;
        desiredTag = null;

        /* ---------- 找 Tag ---------- */
        List<AprilTagDetection> detections = aprilTag.getDetections();
        for (AprilTagDetection d : detections) {
            if (d.metadata != null) {
                targetFound = true;
                desiredTag = d;
                break;
            }
        }

        /* ---------- 未启用或丢失 ---------- */
        if (!enableVision || !targetFound) {
            arrived = false;
            stopRobot(driveCore);
            telemetry.addLine("Vision: NO TAG");
            telemetry.update();
            return;
        }

        /* ---------- 当前位置（Tag 坐标系） ---------- */
        double camX = desiredTag.ftcPose.x; // strafe
        double camY = desiredTag.ftcPose.y; // forward

        /* ---------- 指向目标点的向量 ---------- */
        double dx = TARGET_STRAFE_CM  - camX;
        double dy = TARGET_FORWARD_CM - camY;

        /* ---------- 防止 dy 太小 ---------- */
        if (Math.abs(dy) < MIN_DY_CM) {
            dy = Math.copySign(MIN_DY_CM, dy);
        }

        /* ---------- Heading Error ---------- */
        double headingErrorDeg =
                Math.toDegrees(Math.atan2(dx, dy));

        /* ---------- 到位判定 ---------- */
        if (Math.abs(headingErrorDeg) < HEADING_TOLERANCE_DEG) {
            arrived = true;
            stopRobot(driveCore);
            telemetry.addLine("Vision: HEADING ALIGNED");
        } else {
            arrived = false;
        }

        /* ---------- 闭环控制（只转） ---------- */
        turn = Range.clip(
                headingErrorDeg * SensorConstants.VISION_TURN_GAIN.value,
                -SensorConstants.VISION_MAX_AUTO_TURN.value,
                SensorConstants.VISION_MAX_AUTO_TURN.value
        );

        /* ---------- 输出 ---------- */
        moveRobot(driveCore, 0.0, 0.0, turn);

        telemetry.addData("Cam X / Y", "%.1f / %.1f", camX, camY);
        telemetry.addData("dx / dy", "%.1f / %.1f", dx, dy);
        telemetry.addData("Heading Err", "%.2f deg", headingErrorDeg);
        telemetry.update();
    }

    /* ================== Drive ================== */

    private void stopRobot(NewMecanumDrive drive) {
        moveRobot(drive, 0, 0, 0);
    }

    private void moveRobot(NewMecanumDrive drive,
                           double forward,
                           double strafe,
                           double yaw) {

        double fl = forward - strafe - yaw;
        double fr = forward + strafe + yaw;
        double bl = forward + strafe - yaw;
        double br = forward - strafe + yaw;

        double max = Math.max(
                Math.max(Math.abs(fl), Math.abs(fr)),
                Math.max(Math.abs(bl), Math.abs(br))
        );

        if (max > 1.0) {
            fl /= max;
            fr /= max;
            bl /= max;
            br /= max;
        }

        drive.leftFront.setPower(fl);
        drive.rightFront.setPower(fr);
        drive.leftRear.setPower(bl);
        drive.rightRear.setPower(br);
    }

    /* ================== Camera ================== */

    private void setManualExposure(Telemetry telemetry,
                                   int exposureMS,
                                   int gainValue) {

        while (visionPortal.getCameraState()
                != VisionPortal.CameraState.STREAMING) {
            sleep(20);
        }

        ExposureControl exposure =
                visionPortal.getCameraControl(ExposureControl.class);
        exposure.setMode(ExposureControl.Mode.Manual);
        exposure.setExposure(exposureMS, TimeUnit.MILLISECONDS);

        GainControl gainControl =
                visionPortal.getCameraControl(GainControl.class);
        gainControl.setGain(gainValue);
    }


    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}
