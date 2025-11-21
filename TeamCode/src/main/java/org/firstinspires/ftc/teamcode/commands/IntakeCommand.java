package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import org.firstinspires.ftc.teamcode.subsystems.Intake;

public class IntakeCommand extends CommandBase {

    private final Intake intake;
    private final GamepadEx gamepadEx2;

    public IntakeCommand(Intake intake, GamepadEx gamepadEx2) {
        this.intake = intake;
        this.gamepadEx2 = gamepadEx2;
        addRequirements(intake);
    }

    @Override
    public void execute() {

        if (gamepadEx2.getButton(GamepadKeys.Button.RIGHT_BUMPER)) {
            intake.intakeIn();
        } else if ((gamepadEx2.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER))>=0.5) {
            intake.intakeOut();
        } else {
            intake.stop();
        }
    }
}

