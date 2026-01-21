package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Subsystems.Constants.ServoConstants;

public class IntakePreshooter {

    private final DcMotorEx intake;
    private final DcMotorEx preShooter;

    private final Servo preLimit;
    private double powerScale = 1.0;


    public IntakePreshooter(@NonNull HardwareMap hardwareMap){
        this.intake = hardwareMap.get(DcMotorEx.class, "intake");
        this.preShooter = hardwareMap.get(DcMotorEx.class, "preShooter");
        this.preLimit = hardwareMap.get(Servo.class,"preLimit");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        preShooter.setDirection(DcMotorSimple.Direction.FORWARD);
    }
    public void setPowerScale(double scale) {
        powerScale = scale;
    }
    public void intake() {
        intake.setPower(powerScale);

    }

    public void outtake(){
        intake.setPower(-0.8);
    }

    public void init(){
        intake.setPower(0);
        preShooter.setPower(0);
    }

    public void shoot(){
        preShooter.setPower(powerScale);
    }


    public void limitOn(){
        preLimit.setPosition(ServoConstants.PRELIMIT_ON.value);
    }

    public void limitOff(){
        preLimit.setPosition(ServoConstants.PRELIMIT_OFF.value);

    }
    public void stopPreShooter(){
        preShooter.setPower(0);
    }
}