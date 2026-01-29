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

    // --- Dashboard 参数 ---
    public static double kP = 0.5;
    public static double kI = 0.1;          // 新增积分项：处理最后对不准的静差
    public static double kD = 6;
    public static double kI_LIMIT = 0.2;    // 积分限幅：防止积分过载导致大幅度甩头

    public static double MIN_TURN = 0.04;    // 最小克服摩擦力功率
    public static double MAX_TURN = 0.25;    // 最大转向速度限制
    public static double TOLERANCE = 6;   // 容差

    private Follower follower;
    private Vision vision;

    // PID 内部变量
    private double lastError = 0;
    private double integral = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        Telemetry telemetry_M = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        // 初始化 PedroPathing
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(0, 0, 0));

        // 初始化视觉
        vision = new Vision(telemetry_M, hardwareMap);

        telemetry_M.addLine("系统就绪 | System Ready");
        telemetry_M.update();

        waitForStart();

        follower.startTeleOpDrive(true);

        while (opModeIsActive()) {
            follower.update();

            double error = vision.getFilteredHeadingError();
            double turnPower = 0;

            if (follower.getPose() == null) continue;

            // 按住 A 键自动对齐
            if (gamepad1.a && vision.targetFound) {
                if (Math.abs(error) > TOLERANCE) {
                    // P 项
                    double pTerm = error * kP;

                    // I 项：累加误差
                    integral += error;
                    // 积分限幅 (Anti-Windup)
                    integral = Math.max(-kI_LIMIT / kI, Math.min(kI_LIMIT / kI, integral));
                    double iTerm = integral * kI;

                    // D 项：误差变化率
                    double dTerm = (error - lastError) * kD;

                    turnPower = pTerm + iTerm + dTerm;

                    // 摩擦力补偿 (Feedforward)
                    turnPower += Math.signum(turnPower) * MIN_TURN;

                    // 最终功率限幅
                    turnPower = Math.max(-MAX_TURN, Math.min(MAX_TURN, turnPower));
                } else {
                    // 进入容差范围，清除积分防止下次启动受影响
                    integral = 0;
                }
                lastError = error;

                // 写入 Pedro 底盘：仅转向
                follower.setTeleOpDrive(0, 0, -turnPower);

            } else {
                // 标准手动驾驶
                follower.setTeleOpDrive(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x,
                        -gamepad1.right_stick_x
                );

                // 重置 PID 状态，防止下次按下 A 时突跳
                lastError = 0;
                integral = 0;
                turnPower = 0;
            }

            // 调试数据
            telemetry_M.addData("Mode", (gamepad1.a ? "AUTO_ALIGN" : "MANUAL"));
            telemetry_M.addData("Target Found", vision.targetFound);
            telemetry_M.addData("Error", error);
            telemetry_M.addData("Integral", integral); // 观察积分是否过大
            telemetry_M.addData("Turn Power Output", turnPower);
            telemetry_M.update();
        }
    }
}