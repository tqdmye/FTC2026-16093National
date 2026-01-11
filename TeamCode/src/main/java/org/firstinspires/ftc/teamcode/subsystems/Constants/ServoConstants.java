package org.firstinspires.ftc.teamcode.subsystems.Constants;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;

public enum ServoConstants {

    SHOOTER_TURRET_SLOW(0.88),
    SHOOTER_TURRET_MID(0.66),
    SHOOTER_TURRET_LONG(0.46  ),

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