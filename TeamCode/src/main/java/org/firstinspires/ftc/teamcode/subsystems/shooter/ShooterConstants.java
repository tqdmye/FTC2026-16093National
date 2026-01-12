package org.firstinspires.ftc.teamcode.subsystems.shooter;

public enum ShooterConstants {
    SHOOTER_SLOW_VELOCITY(1080),
    SHOOTER_MID_VELOCITY(1300),
    SHOOTER_FAST_VELOCITY(1560),
    SHOOTER_AUTO_SLOW_VELOCITY(950),

    SHOOTER_IDLE_VELOCITY(800),

    SHOOTER_P(33),
    SHOOTER_I(0),
    SHOOTER_D(15),
    SHOOTER_F(15)
    ;

    public final int value;

    ShooterConstants(int value) {
        this.value = value;
    }
}