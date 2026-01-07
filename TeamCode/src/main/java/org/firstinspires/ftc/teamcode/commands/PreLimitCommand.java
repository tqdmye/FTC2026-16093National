package org.firstinspires.ftc.teamcode.commands;


import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.LED;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;

import java.util.function.BooleanSupplier;

public class PreLimitCommand extends CommandBase {

    private final BooleanSupplier isLimitOn;

    private final BooleanSupplier isVelocityDetecting;

    private final BooleanSupplier isShooting;

    private final Shooter shooter;

    private final Intake intake;

    private final LED led;

    private boolean lastLimitOn = false;


    public PreLimitCommand(
            Shooter shooter,
            Intake intake,
            LED led,
            BooleanSupplier isVelocityDetecting,
            BooleanSupplier isLimitOn,
            BooleanSupplier isShooting) {
        this.shooter = shooter;
        this.intake = intake;
        this.led = led;
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




    }

}
