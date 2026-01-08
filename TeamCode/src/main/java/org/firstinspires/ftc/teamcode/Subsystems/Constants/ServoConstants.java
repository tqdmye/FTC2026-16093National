package org.firstinspires.ftc.teamcode.Subsystems.Constants;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;

public enum ServoConstants {

    SHOOTER_TURRET_SLOW(1),
    SHOOTER_TURRET_MID(0.72),
    SHOOTER_TURRET_LONG(0.59  ),

    PRELIMIT_ON(0.1),
    PRELIMIT_OFF(0.4),

    ;



    public final double value;

    ServoConstants(double pos) {
        value = pos;
    }
    public void setToServo(@NonNull Servo servo){
        servo.setPosition(this.value);
    }
}