package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {

    private DcMotorEx intake;

    private Servo preLimit;


    public Intake(@NonNull HardwareMap hardwareMap){
        this.intake = hardwareMap.get(DcMotorEx.class, "intake");
        this.preLimit = hardwareMap.get(Servo.class,"preLimit");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void intake() {
        intake.setPower(1);
    }

    public void outtake(){
        intake.setPower(-0.8);
    }

    public void init(){
        intake.setPower(0.4);
    }

    public void limitOn(){
        preLimit.setPosition(0.15);
    }

    public void limitOff(){
        preLimit.setPosition(0.4);
 
    }
}