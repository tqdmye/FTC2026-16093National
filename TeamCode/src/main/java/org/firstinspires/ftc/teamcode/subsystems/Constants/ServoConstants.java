package org.firstinspires.ftc.teamcode.Subsystems.Constants;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;

public enum ServoConstants {

    SHOOTER_TURRET_SLOW(0.54),
    SHOOTER_TURRET_MID(0.37),
    SHOOTER_TURRET_LONG(0.28),

    PRELIMIT_ON(0.68),
    PRELIMIT_OFF(1),

    ;



    public final double value;

    ServoConstants(double pos) {
        value = pos;
    }
    public void setToServo(@NonNull Servo servo){
        servo.setPosition(this.value);
    }
}