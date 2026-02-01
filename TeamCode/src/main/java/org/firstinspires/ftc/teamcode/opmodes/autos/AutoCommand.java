package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.Subsystems.IntakePreshooter;
import org.firstinspires.ftc.teamcode.Subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.commands.autos.AccelerateAutoCommand;
import org.firstinspires.ftc.teamcode.commands.autos.IntakeAutoCommand;
//import com.pedropathing.localization.Pose;
//import com.pedropathing.pathgen.Point;


public class AutoCommand {
    Shooter shooter;
    IntakePreshooter intake;

    public Command limitOff(){
        return new InstantCommand((
        )->intake.limitOff());
    }
    public Command limitOn(){
        return new InstantCommand((
        )->intake.limitOn());
    }



    public Command accelSlow(AccelerateAutoCommand accel) {
        return new InstantCommand(
                () -> accel.setState(AccelerateAutoCommand.AccelState.SLOW)
        );
    }

    public Command accelMid(AccelerateAutoCommand accel) {
        return new InstantCommand(
                () -> accel.setState(AccelerateAutoCommand.AccelState.MID)
        );
    }

    public Command accelFast(AccelerateAutoCommand accel) {
        return new InstantCommand(
                () -> accel.setState(AccelerateAutoCommand.AccelState.FAST)
        );
    }

    public Command intakeAuto(IntakeAutoCommand intakeAutoCommand){
        return new InstantCommand(
                ()-> intakeAutoCommand.setState(IntakeAutoCommand.IntakeState.INTAKE)
        );
    }





    public AutoCommand(Shooter shooter, IntakePreshooter intake) {
        this.shooter = shooter;
        this.intake = intake;
    }

    public Command shootPreload() {
        return new SequentialCommandGroup(
                new WaitCommand(500),
                new InstantCommand(() -> intake.limitOff()),
                new InstantCommand(()->intake.preShooterShoot(1)),
                new WaitCommand(800),
                new InstantCommand(() -> intake.limitOn())
        );
    }

    public Command shoot() {
        return new SequentialCommandGroup(
                new WaitCommand(150),
                new InstantCommand(() -> intake.limitOff()),
                new InstantCommand(()->intake.preShooterShoot(1)),
                new WaitCommand(800),
                new InstantCommand(() -> intake.limitOn())
        );
    }

    public Command shootMid() {
        return new SequentialCommandGroup(

                new WaitCommand(130),
                new InstantCommand(() -> intake.limitOff()),
                new InstantCommand(()->intake.preShooterShoot(1)),
                new WaitCommand(1400),
                new InstantCommand(() -> intake.limitOn())
        );
    }

    public Command shootFar() {
        return new SequentialCommandGroup(
                new WaitCommand(500),
                new InstantCommand(() -> intake.limitOff()),
                new InstantCommand(()->intake.preShooterShoot(0.75)),
                new WaitCommand(1000),
                new InstantCommand(() -> intake.limitOn())
        );
    }
    public Command shootFarPreload() {
        return new SequentialCommandGroup(
                new WaitCommand(3000),
                new InstantCommand(() -> intake.limitOff()),
                new InstantCommand(()->intake.preShooterShoot(0.75)),
                new WaitCommand(1000),
                new InstantCommand(() -> intake.limitOn())
        );
    }

    public Command intake() {
        return new RunCommand(
                () -> intake.intake()
        ) {
            @Override
            public void end(boolean interrupted) {
                intake.init();
            }
        };
    }


    public Command stopAll() {
        return new ParallelCommandGroup(
                new InstantCommand(() -> intake.init()),
                new InstantCommand(() -> shooter.stopAccelerate())
        );
    }

    public Command park() {
        return new ParallelCommandGroup(
                new InstantCommand(() -> intake.init()),
                new InstantCommand(() -> shooter.stopAccelerate()),
                new WaitCommand(99999999)
        );
    }


}