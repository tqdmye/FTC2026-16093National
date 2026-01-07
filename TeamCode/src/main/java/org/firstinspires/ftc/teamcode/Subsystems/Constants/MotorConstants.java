package org.firstinspires.ftc.teamcode.Subsystems.Constants;

public enum MotorConstants {
    SHOOTER_SLOW_VELOCITY(1080),
    SHOOTER_MID_VELOCITY(1280),
    SHOOTER_FAST_VELOCITY(1500),
    SHOOTER_AUTO_SLOW_VELOCITY(950),

    SHOOTER_IDLE_VELOCITY(600),

    SHOOTER_P(30),
    SHOOTER_I(0),
    SHOOTER_D(0),
    SHOOTER_F(14)
    ;

    public final int value;

    MotorConstants(int value) {
        this.value = value;
    }
}