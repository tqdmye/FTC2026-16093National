package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

@TeleOp(name = "VisionTest", group = "Test")
public class VisionTest extends OpMode {

    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTag;

    @Override
    public void init() {
        aprilTag = new AprilTagProcessor.Builder().build();

        visionPortal = new VisionPortal.Builder()
                .addProcessor(aprilTag)
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .build();

        telemetry.addLine("Vision initialized");
        telemetry.update();
    }

    @Override
    public void loop() {
        AprilTagDetection desiredTag = null;

        for (AprilTagDetection detection : aprilTag.getDetections()) {
            // 如果只关心某一个 ID，可以在这里筛选
            // if (detection.id == 1)
            desiredTag = detection;
            break;
        }

        if (desiredTag == null || desiredTag.ftcPose == null) {
            telemetry.addLine("No AprilTag detected");
            telemetry.update();
            return;
        }

        telemetry.addData("Forward (cm)", desiredTag.ftcPose.y);
        telemetry.addData("Strafe (cm)", desiredTag.ftcPose.x);
        telemetry.addData("Yaw (deg)", desiredTag.ftcPose.yaw);
        telemetry.update();
    }

    @Override
    public void stop() {
        if (visionPortal != null) {
            visionPortal.close();
        }
    }
}
