package org.firstinspires.ftc.teamcode.Subsystems.Constants;

public enum ShooterConstants {
    SHOOTER_SLOW_VELOCITY(1120),
    SHOOTER_MID_VELOCITY(1260),
    SHOOTER_FAST_VELOCITY(1440),
    SHOOTER_IDLE_VELOCITY(800),

    SHOOTER_P(40),
    SHOOTER_I(0),
    SHOOTER_D(17),
    SHOOTER_F(16),

    SHOOTER_TURRET_SLOW(0.79),
    SHOOTER_TURRET_MID(0.45),
    SHOOTER_TURRET_LONG(0.29),

    PRELIMIT_ON(0.65),
    PRELIMIT_OFF(1),

    PRELIMIT_DNT_SHOOT(0.68),
    PRELIMIT_SHOOT(1),

    AUTO_SHOOTER_SLOW_VELOCITY(1120),
    AUTO_SHOOTER_MID_VELOCITY(1180),
    AUTO_SHOOTER_FAST_VELOCITY(1480),
    AUTO_SHOOTER_TURRET_SLOW(0.79),
    AUTO_SHOOTER_TURRET_MID(0.42),
    AUTO_SHOOTER_TURRET_LONG(0.32),
    ;

    public final double value;

    ShooterConstants(double value) {
        this.value = value;
    }
}