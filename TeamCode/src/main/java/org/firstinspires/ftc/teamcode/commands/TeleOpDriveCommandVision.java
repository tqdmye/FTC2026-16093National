package org.firstinspires.ftc.teamcode.commands;


import com.arcrobotics.ftclib.command.CommandBase;
import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Vision;
import org.firstinspires.ftc.teamcode.Subsystems.driving.NewMecanumDrive;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class TeleOpDriveCommandVision extends CommandBase {
    private final Follower follower;
    private final Vision vision;
    private final DoubleSupplier x;
    private final DoubleSupplier rotate;
    private final DoubleSupplier y;
    private final BooleanSupplier isSlowMode;
    private Telemetry telemetry;

    private double slowModeMultiplier;

    // 当视觉/其它逻辑接管底盘时，用于让本命令“不动手”，避免多处同时写电机功率导致抖动
    private final BooleanSupplier isVisionDriving;

    public TeleOpDriveCommandVision(
            Follower follower,
            Vision vision,
            DoubleSupplier x,
            DoubleSupplier y,
            DoubleSupplier rotate,
            BooleanSupplier isSlowMode,
            BooleanSupplier isVisionDriving,
            Telemetry telemetry) {
        this.follower = follower;
        this.vision = vision;
        this.x = x;
        this.rotate = rotate;
        this.y = y;
        this.isSlowMode = isSlowMode;
        this.isVisionDriving = isVisionDriving;
        this.telemetry = telemetry;
    }

    @Override
    public void execute() {

        double drive = y.getAsDouble();
        double strafe = -x.getAsDouble();
        double turn;

        double multiplier = isSlowMode.getAsBoolean() ? 0.6 : 1.0;

        if (isVisionDriving.getAsBoolean()) {
            // 调用 Vision 类中封装好的 PID
            turn = vision.getPIDAutoAlignTurn(telemetry);

            // 视觉丢失时的安全回退
            if (!vision.targetFound) {
                turn = -rotate.getAsDouble() * 0.6;
            }
        } else {
            // 正常手动模式
            turn = -rotate.getAsDouble();
            vision.resetPID(); // 持续重置，防止切换瞬间产生突跳
        }

        follower.setTeleOpDrive(
                drive * multiplier,
                strafe * multiplier,
                turn * multiplier);
        follower.update();

    }
}
