package org.firstinspires.ftc.teamcode.Subsystems.Constants;
import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;

public enum ServoConstants {

    SHOOTER_TURRET_SLOW(0.98),
    SHOOTER_TURRET_MID(0.88),
    SHOOTER_TURRET_LONG(0.8),

    PRELIMIT_ON(0.14),
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