package org.firstinspires.ftc.teamcode.commands;


import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.ballstorage.BallStorage;
import org.firstinspires.ftc.teamcode.subsystems.intakepreshoot.IntakePreshooter;
import org.firstinspires.ftc.teamcode.subsystems.Led;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;

import java.util.function.BooleanSupplier;

public class PreLimitCommand extends CommandBase {

    private boolean isFullTriggered = false;
    private long fullTimeStamp = 0;
    private final BooleanSupplier isLimitOn;
    private final BooleanSupplier isVelocityDetecting;
    private final BooleanSupplier isShooting;
    private final Shooter shooter;
    private final IntakePreshooter intake;
    private final Led led;
    private final BallStorage ballStorage;
    private boolean lastLimitOn = false;


    public PreLimitCommand(
            Shooter shooter,
            IntakePreshooter intake,
            Led led,
            BallStorage ballStorage,
            BooleanSupplier isVelocityDetecting,
            BooleanSupplier isLimitOn,
            BooleanSupplier isShooting) {
        this.shooter = shooter;
        this.intake = intake;
        this.led = led;
        this.ballStorage = ballStorage;
        this.isLimitOn = isLimitOn;
        this.isVelocityDetecting = isVelocityDetecting;
        this.isShooting = isShooting;
    }

    @Override
    public void execute() {

        boolean limitOn = isLimitOn.getAsBoolean();

        boolean velocityDetecting = isVelocityDetecting.getAsBoolean();

        boolean shooting = isShooting.getAsBoolean();

        if (limitOn != lastLimitOn) {

            if (!limitOn) {

                shooter.accelerate_slow();
            } else {

                shooter.accelerate_idle();
            }

            lastLimitOn = limitOn;
        }


        if (limitOn) {
            intake.limitOn();
            led.setNone();
        } else {

            led.setBlue();

            if (shooting){
                intake.limitOff();
            } else {
                intake.limitOn();
            }

            if (velocityDetecting && shooter.isAsTargetVelocity){

                led.setGreen();  //

            }
        }

        if (ballStorage.isFull() && !isFullTriggered) {
            led.setBallComplete();
            fullTimeStamp = System.currentTimeMillis();
            isFullTriggered = true;
        }
        if (isFullTriggered && System.currentTimeMillis() - fullTimeStamp >= 2000) {

            ballStorage.reset();
            isFullTriggered = false;
        }






    }

}
