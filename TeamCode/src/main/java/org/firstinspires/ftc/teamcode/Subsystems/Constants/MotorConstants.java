package org.firstinspires.ftc.teamcode.Subsystems.Constants;

public enum MotorConstants {
    SHOOTER_SLOW_VELOCITY(1100),
    SHOOTER_MID_VELOCITY(1380),
    SHOOTER_FAST_VELOCITY(1760),
    SHOOTER_AUTO_SLOW_VELOCITY(950),

    SHOOTER_IDLE_VELOCITY(600),

    SHOOTER_P(32),
    SHOOTER_I(0),
    SHOOTER_D(15),
    SHOOTER_F(16)
    ;

    public final int value;

    MotorConstants(int value) {
        this.value = value;
    }
}