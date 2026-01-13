package org.firstinspires.ftc.teamcode.subsystems.Constants;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;

public enum ServoConstants {

    SHOOTER_TURRET_SLOW(0.51),
    SHOOTER_TURRET_MID(0.47),
    SHOOTER_TURRET_LONG(0.45),

    PRELIMIT_ON(1 ),
    PRELIMIT_OFF(0.67),

    ;



    public final double value;

    ServoConstants(double pos) {
        value = pos;
    }
    public void setToServo(@NonNull Servo servo){
        servo.setPosition(this.value);
    }
}