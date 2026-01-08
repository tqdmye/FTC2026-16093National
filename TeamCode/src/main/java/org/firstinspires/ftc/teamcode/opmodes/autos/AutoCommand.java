package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.commands.AccelerateAutoCommand;
import org.firstinspires.ftc.teamcode.commands.IntakeAutoCommand;
//import com.pedropathing.localization.Pose;
//import com.pedropathing.pathgen.Point;


public class AutoCommand {
    Shooter shooter;
    Intake intake;

    public Command limitOff(){
        return new InstantCommand((
        )->intake.limitOff());
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








    public AutoCommand(Shooter shooter, Intake intake) {
        this.shooter = shooter;
        this.intake = intake;
    }
//    public Command accelerateSlow() {
//        return new RunCommand(shooter::accelerate_slow) {
//            @Override
//            public void end(boolean interrupted) {
//                shooter.stopAccelerate();
//            }
//        };
//    }
//
//    public Command accelerateMid() {
//        return new RunCommand(shooter::accelerate_mid) {
//            @Override
//            public void end(boolean interrupted) {
//                shooter.stopAccelerate();
//            }
//        };
//    }


//    public Command accelerateSlow() {
//        return new RunCommand(() -> shooter.accelerate_slow());
//    }
//
//    public Command accelerateMid() {
//        return new RunCommand(() -> shooter.accelerate_mid());
//    }

//    public Command accelerateFast() {
//        return new RunCommand(() -> shooter.accelerate_fast());
//    }
//
//
//    public Command preLimitOn() {
//        return new InstantCommand(() -> intake.limitOff());
//    }

    public Command shootPreload() {
        return new SequentialCommandGroup(
                new WaitCommand(500),
                new InstantCommand(() -> shooter.shoot()),
                new WaitCommand(800),
                new InstantCommand(() -> shooter.init())
        );
    }

    public Command shoot() {
        return new SequentialCommandGroup(
                new WaitCommand(150),
                new InstantCommand(() -> shooter.shoot()),
                new WaitCommand(800),
                new InstantCommand(() -> shooter.init())
        );
    }

    public Command shootMid() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> shooter.servosetpositon_mid_4()),
                new WaitCommand(130),
                new InstantCommand(() -> shooter.shoot()),
                new WaitCommand(1400),
                new InstantCommand(() -> shooter.init())
        );
    }

    public Command shootFar() {
        return new SequentialCommandGroup(
                new WaitCommand(500),
                new InstantCommand(() -> shooter.shoot()),
                new WaitCommand(1000),
                new InstantCommand(() -> shooter.init())
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
                new InstantCommand(() -> shooter.init()),
                new InstantCommand(() -> shooter.stopAccelerate())
        );
    }

    public Command park() {
        return new ParallelCommandGroup(
                new InstantCommand(() -> intake.init()),
                new InstantCommand(() -> shooter.init()),
                new InstantCommand(() -> shooter.stopAccelerate()),
                new WaitCommand(99999999)
        );
    }


}