package org.firstinspires.ftc.teamcode.commands;


import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;

import com.arcrobotics.ftclib.command.CommandBase;
import com.pedropathing.follower.Follower;

import java.util.function.BooleanSupplier;

public class PPTeleOpDriveCommand extends CommandBase {
    private final Follower follower;

    private final BooleanSupplier isSlowMode;
    private final BooleanSupplier isFieldCentric;
    private double slowModeMultiplier;

    public PPTeleOpDriveCommand(
            Follower follower,
            BooleanSupplier isSlowMode,
            BooleanSupplier isFieldCentric) {
        this.follower = follower;

        this.isSlowMode = isSlowMode;
//        this.isFieldCentric = isFieldCentric;
        this.isFieldCentric = isFieldCentric;
    }

    @Override
    public void execute() {
        if(isSlowMode.getAsBoolean()) slowModeMultiplier = 0.6;
        else slowModeMultiplier = 1.0;

        if (isFieldCentric.getAsBoolean()) {
            follower.setTeleOpDrive(
                    -gamepad1.left_stick_y * slowModeMultiplier,
                    -gamepad1.left_stick_x * slowModeMultiplier,
                    -gamepad1.right_stick_x * slowModeMultiplier,
                    false);
            follower.update();
        } else {
            follower.setTeleOpDrive(
                    -gamepad1.left_stick_y * slowModeMultiplier,
                    -gamepad1.left_stick_x * slowModeMultiplier,
                    -gamepad1.right_stick_x * slowModeMultiplier,
                    true);
        }
        follower.update();
    }
}
