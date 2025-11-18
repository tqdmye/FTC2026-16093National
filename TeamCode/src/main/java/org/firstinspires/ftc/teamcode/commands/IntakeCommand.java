package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import org.firstinspires.ftc.teamcode.subsystems.Intake;

public class IntakeCommand extends CommandBase {

    private final Intake intake;
    private final GamepadEx gamepadEx1;

    public IntakeCommand(Intake intake, GamepadEx gamepadEx1) {
        this.intake = intake;
        this.gamepadEx1 = gamepadEx1;
        addRequirements(intake);
    }

    @Override
    public void execute() {

        if (gamepadEx1.getButton(GamepadKeys.Button.RIGHT_BUMPER)) {
            intake.intakeIn();
        } else if (gamepadEx1.getButton(GamepadKeys.Button.LEFT_BUMPER)) {
            intake.intakeOut();
        } else {
            intake.stop();
        }
    }
}

