package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Subsystems.IntakePreShooterFSM;
import org.firstinspires.ftc.teamcode.Subsystems.Led;
import org.firstinspires.ftc.teamcode.Subsystems.shooter.ShooterFSM;

import java.util.function.BooleanSupplier;

public class RobotFSMCommand extends CommandBase {

    private final ShooterFSM shooterFSM;
    private final IntakePreShooterFSM intakePreShooterFSM;
    private final Led led;
    private final BooleanSupplier fireRequest;

    public RobotFSMCommand(
            ShooterFSM shooterFSM,
            IntakePreShooterFSM intakePreShooterFSM,
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
            led.isAtVelocity();   // 到速：统一提示色（例如白色/蓝色）
        } else {
            switch (shooterFSM.state) {
                case SLOW:
                    led.setRed();
                    break;
                case MID:
                    led.setYellow();
                    break;
                case FAST:
                    led.setPurple();
                    break;
                case IDLE:
                    led.setNone();
                    break;
            }
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
