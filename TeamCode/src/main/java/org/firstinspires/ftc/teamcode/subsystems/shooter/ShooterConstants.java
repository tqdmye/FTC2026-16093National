package org.firstinspires.ftc.teamcode.subsystems.shooter;

public enum ShooterConstants {
    SHOOTER_SLOW_VELOCITY(1180),
    SHOOTER_MID_VELOCITY(1330),
    SHOOTER_FAST_VELOCITY(1720),
    SHOOTER_AUTO_SLOW_VELOCITY(950),

    SHOOTER_IDLE_VELOCITY(0),

    SHOOTER_P(25),
    SHOOTER_I(0),
    SHOOTER_D(0),
    SHOOTER_F(15)
    ;

    public final int value;

    ShooterConstants(int value) {
        this.value = value;
    }
}