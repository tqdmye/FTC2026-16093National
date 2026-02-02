package org.firstinspires.ftc.teamcode.commands.autos;

import com.arcrobotics.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.Subsystems.IntakePreShooterFSM;

public class IntakeAutoCommand extends CommandBase {

    private final IntakePreShooterFSM intake;
    public enum IntakeState {
        OUTTAKE,
        INIT,
        INTAKE,
        HOLD
    }

    private IntakeState state = IntakeState.INIT;
    private IntakeState lastState = null;

    public IntakeAutoCommand(IntakePreShooterFSM intake) {
        this.intake = intake;

    }


    public void setState(IntakeState newState) {
        this.state = newState;
    }

    @Override
    public void initialize() {
        lastState = null;
    }

    @Override
    public void execute() {
        if (state != lastState) {
            onEnter(state);
            lastState = state;
        }


        switch (state) {
            case INTAKE:
                intake.intake();
                break;
            case HOLD:

                break;
            case OUTTAKE:
            case INIT:

                break;
        }
    }

    private void onEnter(IntakeState state) {
        switch (state) {
            case OUTTAKE:
                intake.outtake();
                break;

            case INIT:
                intake.init();
                break;

            case INTAKE:
                intake.intake();
                break;

            case HOLD:

                break;
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
