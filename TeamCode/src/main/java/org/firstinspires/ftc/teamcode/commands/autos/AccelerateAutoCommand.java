package org.firstinspires.ftc.teamcode.commands.autos;

import com.arcrobotics.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.Subsystems.shooter.ShooterFSM;

public class AccelerateAutoCommand extends CommandBase {

    private final ShooterFSM shooter;

    public enum AccelState {
        IDLE,
        SLOW,
        MID,
        FAST
    }

    private AccelState accelState = AccelState.IDLE;

    public AccelerateAutoCommand(ShooterFSM shooter) {
        this.shooter = shooter;
        addRequirements();
    }

    public void setState(AccelState state) {
        this.accelState = state;
        switch (state) {
            case IDLE: shooter.state = ShooterFSM.State.IDLE; break;
            case SLOW: shooter.state = ShooterFSM.State.SLOW; break;
            case MID:  shooter.state = ShooterFSM.State.MID;  break;
            case FAST: shooter.state = ShooterFSM.State.FAST; break;
        }
    }

    @Override
    public void execute() {
        switch (shooter.state) {
            case SLOW:
                shooter.accelerate_slow_auto();
                break;
            case MID:
                shooter.accelerate_mid_auto();
                break;
            case FAST:
                shooter.accelerate_fast_auto();
                break;
            case IDLE:
            default:
                shooter.accelerate_idle();
                break;
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
