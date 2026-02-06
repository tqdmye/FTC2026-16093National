package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.Subsystems.IntakePreShooterFSM;
import org.firstinspires.ftc.teamcode.Subsystems.shooter.ShooterFSM;
import org.firstinspires.ftc.teamcode.commands.autos.AccelerateAutoCommand;
import org.firstinspires.ftc.teamcode.commands.autos.IntakeAutoCommand;


public class AutoCommand {

    private final ShooterFSM shooter;
    private final IntakePreShooterFSM intake;
    private final AccelerateAutoCommand accel;

    public AutoCommand(
            ShooterFSM shooter,
            IntakePreShooterFSM intake,
            AccelerateAutoCommand accel
    ) {
        this.shooter = shooter;
        this.intake = intake;
        this.accel = accel;
    }


    public Command accelIdle() {
        return new InstantCommand(
                () -> accel.setState(AccelerateAutoCommand.AccelState.IDLE)
        );
    }

    public Command accelSlow() {
        return new InstantCommand(
                () -> accel.setState(AccelerateAutoCommand.AccelState.SLOW)
        );
    }

    public Command accelMid() {
        return new InstantCommand(
                () -> accel.setState(AccelerateAutoCommand.AccelState.MID)
        );
    }
    public Command accelFast() {
        return new InstantCommand(
                () -> accel.setState(AccelerateAutoCommand.AccelState.FAST)
        );
    }

    public Command accelFastPreload() {
        return new InstantCommand(
                () -> accel.setState(AccelerateAutoCommand.AccelState.FASTPRELOAD)
        );
    }

    public Command intakeAuto(IntakeAutoCommand intakeAutoCommand){
            return new InstantCommand(
                    ()-> intakeAutoCommand.setState(IntakeAutoCommand.IntakeState.INTAKE));
    }

    public Command shootFarPreload() {
        return new SequentialCommandGroup(
                new WaitCommand(2100),
                new InstantCommand(intake::shoot),
                new WaitCommand(500),
                new InstantCommand(intake::dntShoot)
        );
    }

    public Command shootFar() {
        return new SequentialCommandGroup(
                new InstantCommand(intake::shoot),
                new WaitCommand(500),
                new InstantCommand(intake::dntShoot)
        );
    }

    public Command shootMid() {
        return new SequentialCommandGroup(
                new WaitCommand(20),
                new InstantCommand(intake::shoot),
                new WaitCommand(500),
                new InstantCommand(intake::dntShoot)
        );
    }

    public Command shootSlow() {
        return new SequentialCommandGroup(
                new WaitCommand(100),
                new InstantCommand(intake::shoot),
                new WaitCommand(500),
                new InstantCommand(intake::dntShoot)
        );
    }

    public Command stopAll() {
        return new ParallelCommandGroup(
                new InstantCommand(intake::init),
                accelIdle()
        );
    }
}