package org.firstinspires.ftc.teamcode.commands.fsm;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.Subsystems.intakePreShooterFSM;
import org.firstinspires.ftc.teamcode.Subsystems.Led;
import org.firstinspires.ftc.teamcode.Subsystems.shooter.ShooterFSM;

import java.util.function.BooleanSupplier;

public class RobotFSMCommand extends CommandBase {

    private final ShooterFSM shooterFSM;
    private final intakePreShooterFSM intakePreShooterFSM;
    private final Led led;
    private final BooleanSupplier fireRequest;

    public RobotFSMCommand(
            ShooterFSM shooterFSM,
            intakePreShooterFSM intakePreShooterFSM,
            Led led,
            BooleanSupplier fireRequest
    ) {
        this.shooterFSM = shooterFSM;
        this.intakePreShooterFSM = intakePreShooterFSM;
        this.led = led;
        this.fireRequest = fireRequest;
    }

    @Override
    public void execute() {

        if(fireRequest.getAsBoolean() && shooterFSM.state != ShooterFSM.State.IDLE){
            intakePreShooterFSM.shoot();
        }
        else{
            intakePreShooterFSM.dntShoot();
        }
        if (shooterFSM.isAsVelocity()) {
            led.isAtVelocity();
        } else {
            led.setNone();
        }


        switch (shooterFSM.state) {
            case SLOW:
                shooterFSM.accelerate_slow();
                break;
            case MID:
                shooterFSM.accelerate_mid();
                break;
            case FAST:
                shooterFSM.accelerate_fast();
                break;
            case IDLE:
                shooterFSM.accelerate_idle();
                break;
        }
    }
}
