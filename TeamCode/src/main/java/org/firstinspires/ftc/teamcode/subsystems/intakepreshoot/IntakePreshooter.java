package org.firstinspires.ftc.teamcode.subsystems.intakepreshoot;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.Constants.ServoConstants;

public class IntakePreshooter {

    private DcMotorEx intake;

    private Servo preLimit;
    private double powerScale = 1.0;


    public IntakePreshooter(@NonNull HardwareMap hardwareMap){
        this.intake = hardwareMap.get(DcMotorEx.class, "intake");
        this.preLimit = hardwareMap.get(Servo.class,"preLimit");
        intake.setDirection(DcMotorSimple.Direction.FORWARD);
    }
    public void setPowerScale(double scale) {
        powerScale = scale;
    }
    public void intake() {
        intake.setPower(1.0 * powerScale);
    }

    public void outtake(){
        intake.setPower(-0.8);
    }

    public void init(){
        intake.setPower(0.4);
    }


    public void limitOn(){
        preLimit.setPosition(ServoConstants.PRELIMIT_ON.value);
    }

    public void limitOff(){
        preLimit.setPosition(ServoConstants.PRELIMIT_OFF.value);

    }
}