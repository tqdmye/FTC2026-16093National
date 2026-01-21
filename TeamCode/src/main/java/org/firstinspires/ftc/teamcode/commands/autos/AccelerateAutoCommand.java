package org.firstinspires.ftc.teamcode.commands.autos;

import com.arcrobotics.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.Subsystems.shooter.Shooter;

public class AccelerateAutoCommand extends CommandBase {

    public enum AccelState {
        OFF,
        SLOW,
        MID,
        FAST
    }

    private final Shooter shooter;
    private AccelState state = AccelState.OFF;
    private AccelState lastState = null;

    public AccelerateAutoCommand(Shooter shooter) {
        this.shooter = shooter;

    }

    public void setState(AccelState newState) {
        state = newState;
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
            case SLOW:
                shooter.accelerate_slow();
                break;
            case MID:
                shooter.accelerate_mid();
                break;
            case FAST:
                shooter.accelerate_fast();
                break;
            case OFF:

                break;
        }
    }

    private void onEnter(AccelState state) {
        if (state == AccelState.OFF) {
            shooter.stopAccelerate();
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
