package org.firstinspires.ftc.teamcode.Subsystems.Constants;

public enum ShooterConstants {
    SHOOTER_SLOW_VELOCITY(1140),
    SHOOTER_MID_VELOCITY(1300),
    SHOOTER_FAST_VELOCITY(1500),
    SHOOTER_AUTO_SLOW_VELOCITY(950),

    SHOOTER_IDLE_VELOCITY(500),

    SHOOTER_P(39),
    SHOOTER_I(0),
    SHOOTER_D(17),
    SHOOTER_F(17.5),

    SHOOTER_TURRET_SLOW(0.54),
    SHOOTER_TURRET_MID(0.37),
    SHOOTER_TURRET_LONG(0.28),

    PRELIMIT_ON(0.68),
    PRELIMIT_OFF(1),
    ;

    public final double value;

    ShooterConstants(double value) {
        this.value = value;
    }
}