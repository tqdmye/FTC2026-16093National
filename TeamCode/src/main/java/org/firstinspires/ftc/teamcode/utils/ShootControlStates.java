package org.firstinspires.ftc.teamcode.utils;

/**
 * Centralized finite state machine for shooting system.
 * This class ONLY represents intent/state, never touches hardware.
 */
public class ShootControlStates {

    /** High-level shooting mode */
    public enum ShootMode {
        IDLE,       // not shooting
        SHOOT,     // driver-controlled shooting
    }

    /** Limit strategy */
    public enum LimitMode {
        ON,
        OFF
    }

    /** Shooter velocity intent */
    public enum VelocityMode {
        IDLE,
        SLOW,
        MID,
        FAST
    }

    public ShootMode shootMode = ShootMode.IDLE;
    public LimitMode limitMode = LimitMode.ON;
    public VelocityMode velocityMode = VelocityMode.IDLE;

}
