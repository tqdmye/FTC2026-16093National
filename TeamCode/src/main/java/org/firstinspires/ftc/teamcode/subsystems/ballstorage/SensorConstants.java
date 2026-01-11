package org.firstinspires.ftc.teamcode.subsystems.ballstorage;

public enum SensorConstants {
    INZONE_DISTANCE(2.6),
    GREEN_MIN_H(150),
    GREEN_MAX_H(170),
    PURPLE_MIN_H(205),
    PURPLE_MAX_H(240)

    ;

    public final double value;

    SensorConstants(double value) {
        this.value = value;
    }
}