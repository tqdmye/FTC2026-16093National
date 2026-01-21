package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Subsystems.BallStorage;
import org.firstinspires.ftc.teamcode.Subsystems.IntakePreshooter;
import org.firstinspires.ftc.teamcode.Subsystems.Led;
import org.firstinspires.ftc.teamcode.Subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.utils.ShootControlStates;
import org.firstinspires.ftc.teamcode.utils.ShootControlStates.LimitMode;
import org.firstinspires.ftc.teamcode.utils.ShootControlStates.ShootMode;
import org.firstinspires.ftc.teamcode.utils.ShootControlStates.VelocityMode;

/**
 * Policy command:
 * Applies deterministic behavior based on ShootControlState.
 */
public class PreLimitCommandRevise extends CommandBase {

    private final Shooter shooter;
    private final IntakePreshooter intake;
    private final Led led;
    private final BallStorage ballStorage;
    private final ShootControlStates state;

    private LimitMode lastLimitMode = LimitMode.ON;

    private boolean ballFullLatched = false;
    private long ballFullTimestamp = 0;

    public PreLimitCommandRevise(
            Shooter shooter,
            IntakePreshooter intake,
            Led led,
            BallStorage ballStorage,
            ShootControlStates state
    ) {
        this.shooter = shooter;
        this.intake = intake;
        this.led = led;
        this.ballStorage = ballStorage;
        this.state = state;
    }

    @Override
    public void execute() {
        handleLimitTransition();
        applyShooterVelocityPolicy();
        applyIntakePolicy();
        applyLedPolicy();
        handleBallFullIndicator();
    }

    /* ================= limit ================= */

    private void handleLimitTransition() {
        if (state.limitMode != lastLimitMode) {
            if (state.limitMode == LimitMode.ON) {
                shooter.accelerate_idle();
            } else {
                shooter.accelerate_slow();
            }
            lastLimitMode = state.limitMode;
        }
    }

    /* ================= shooter ================= */

    private void applyShooterVelocityPolicy() {
        switch (state.velocityMode) {
            case IDLE:
                shooter.accelerate_idle();
                break;
            case SLOW:
                shooter.accelerate_slow();
                break;
            case MID:
                shooter.accelerate_mid();
                break;
            case FAST:
                shooter.accelerate_fast();
                break;
        }
    }

    /* ================= intake ================= */

    private void applyIntakePolicy() {

        if (state.limitMode == LimitMode.ON) {
            intake.limitOn();
            return;
        }

        switch (state.shootMode) {
            case SHOOT:
                intake.limitOff();
                intake.shoot();
                break;

            case IDLE:
                intake.limitOn();
                break;
        }
    }

    /* ================= LED ================= */

    private void applyLedPolicy() {

        if (ballStorage.isFull()) {
            led.setBallFull();
            return;
        }

        if (state.limitMode == LimitMode.ON) {
            led.setNone();
            return;
        }

        if (shooter.isAsTargetVelocity) {
            led.setGreen();
        } else {
            led.setBlue();
        }
    }

    /* ================= ball full ================= */

    private void handleBallFullIndicator() {

        if (ballStorage.isFull() && !ballFullLatched) {
            ballFullLatched = true;
            ballFullTimestamp = System.currentTimeMillis();
        }

        if (ballFullLatched &&
                System.currentTimeMillis() - ballFullTimestamp >= 2000) {
            led.resetBall();
            ballFullLatched = false;
        }
    }
}
