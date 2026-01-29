package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Vision;
import pedroPathing.Constants;

@Config
@TeleOp(name = "Test Vision PID", group = "test")
public class TestVisionPID extends LinearOpMode {

    private Follower follower;
    private Vision vision;

    @Override
    public void runOpMode() throws InterruptedException {
        Telemetry telemetry_M = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(0, 0, 0));
        vision = new Vision(telemetry_M, hardwareMap);

        telemetry_M.addLine("READY - Press Play");
        telemetry_M.update();

        waitForStart();
        follower.startTeleOpDrive(true);

        while (opModeIsActive()) {
            follower.update();

            // 直接调用类方法，逻辑统一
            double turnPower = 0;

            if (gamepad1.a) {
                turnPower = vision.getPIDAutoAlignTurn(telemetry_M);
                follower.setTeleOpDrive(0, 0, turnPower);
            } else {
                follower.setTeleOpDrive(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x,
                        -gamepad1.right_stick_x
                );
                vision.resetPID(); // 关键：松开按键即重置，不影响下次检测
            }

            telemetry_M.addData("Vision Found", vision.targetFound);
            telemetry_M.addData("Current Power", turnPower);
            telemetry_M.update();
        }
    }
}