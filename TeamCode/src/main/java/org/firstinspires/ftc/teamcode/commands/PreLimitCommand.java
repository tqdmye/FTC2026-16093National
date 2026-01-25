package org.firstinspires.ftc.teamcode.commands;


import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Subsystems.Led;
import org.firstinspires.ftc.teamcode.Subsystems.BallStorage;
import org.firstinspires.ftc.teamcode.Subsystems.IntakePreshooter;
import org.firstinspires.ftc.teamcode.Subsystems.shooter.Shooter;

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

//        if (limitOn != lastLimitOn) {
//
//            if (!limitOn) {
//
//                shooter.accelerate_slow();
//            } else {
//
//                shooter.accelerate_idle();
//            }
//
//            lastLimitOn = limitOn;
//        }

        if (shooting){
            intake.limitOff();
            intake.shoot();
        } else {
            intake.limitOn();
            intake.stopPreShooter();
        }

        if (shooter.isAsTargetVelocity){

            led.setGreen();  //

        }
        else{
            led.setNone();
        }

        if (ballStorage.isFull() && !isFullTriggered) {
            led.setBallFull();
            fullTimeStamp = System.currentTimeMillis();
            isFullTriggered = true;
        }
        if (isFullTriggered && System.currentTimeMillis() - fullTimeStamp >= 2000) {
//            ballStorage.reset();
            led.resetBall();
            isFullTriggered = false;
        }









    }

}
