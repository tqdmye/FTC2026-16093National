package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

public class ShooterCommand extends CommandBase {

    private final Shooter shooter;
    private final GamepadEx gamepadEx2;



    public ShooterCommand(Shooter shooter, GamepadEx gamepadEx2) {
        this.shooter = shooter;
        this.gamepadEx2 = gamepadEx2;
        addRequirements(shooter);
    }

    @Override
    public void execute() {
        shooter.startShooter();

        if (gamepadEx2.getButton(GamepadKeys.Button.LEFT_BUMPER)) {
            shooter.startPreShooter();
        } else {
            shooter.stopPreShooter();
        }
    }
}

