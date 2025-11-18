package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDrive;

public class MecanumDriveCommand extends CommandBase {

    private final MecanumDrive drive;
    private final GamepadEx gamepadEx1;

    public MecanumDriveCommand(MecanumDrive drive, GamepadEx gamepadEx1) {
        this.drive = drive;
        this.gamepadEx1 = gamepadEx1;
        addRequirements(drive);
    }

    @Override
    public void execute() {

        double y = gamepadEx1.getLeftY();
        double x = gamepadEx1.getLeftX() * 1.1;
        double rx = gamepadEx1.getRightX();

        drive.drive(y, x, rx);
    }

    @Override
    public void end(boolean interrupted) {
        drive.stop();
    }
}