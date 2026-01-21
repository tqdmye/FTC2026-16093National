package org.firstinspires.ftc.teamcode.Subsystems.Constants;

public enum SensorConstants {
    INZONE_DISTANCE(2.6),
    GREEN_MIN_H(150),
    GREEN_MAX_H(170),
    PURPLE_MIN_H(205),
    PURPLE_MAX_H(240),

    VISION_DESIRED_DISTANCE(300),
    VISION_SPEED_GAIN(0.7),
    VISION_STRAFE_GAIN(0.3),
    VISION_TURN_GAIN(0.08),
    VISION_MAX_AUTO_SPEED(0.9),
    VISION_MAX_AUTO_STRAFE(0.6),
    VISION_MAX_AUTO_TURN(0.3)

    ;

    public final double value;

    SensorConstants(double value) {
        this.value = value;
    }
}