package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Subsystems.Constants.MotorConstants;
import org.firstinspires.ftc.teamcode.Subsystems.Constants.ServoConstants;

public class Shooter {
    public DcMotorEx shooterDown, shooterUp, preShooter;
    public  Servo shooterAngleServo;

    private double targetRightPosition;
    private double targetLeftPosition;

    private double currentRightPosition;
    private double currentLeftPosition;

    private double targetVelocity;

    public boolean isAsTargetVelocity;
    public double currentVelocity;



    public Shooter(HardwareMap hardwareMap) {
        this.shooterDown = hardwareMap.get(DcMotorEx.class, "shooterDown");
        this.shooterUp = hardwareMap.get(DcMotorEx.class, "shooterUp");
        this.preShooter = hardwareMap.get(DcMotorEx.class, "preShooter");
        this.shooterAngleServo = hardwareMap.get(Servo.class,"shooterAngle");

//        this.indicatorLight = hardwareMap.get(Servo.class,"indicatorLight");

        shooterDown.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterUp.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterAngleServo.setDirection(Servo.Direction.FORWARD);



        shooterDown.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        shooterUp.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        preShooter.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        shooterDown.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterUp.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterDown.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterUp.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        shooterUp.setVelocityPIDFCoefficients(MotorConstants.SHOOTER_P.value, MotorConstants.SHOOTER_I.value, MotorConstants.SHOOTER_D.value, MotorConstants.SHOOTER_F.value);
        shooterDown.setVelocityPIDFCoefficients(MotorConstants.SHOOTER_P.value, MotorConstants.SHOOTER_I.value, MotorConstants.SHOOTER_D.value, MotorConstants.SHOOTER_F.value);
    }


    public void servosetpositon_mid_4(){
        shooterAngleServo.setPosition(ServoConstants.SHOOTER_TURRET_MID.value);
    }
    public void servosetpositon_mid_3(){
        shooterAngleServo.setPosition(ServoConstants.SHOOTER_TURRET_MID.value+0.02);

    }
    public void servosetpositon_mid_2(){
        shooterAngleServo.setPosition(ServoConstants.SHOOTER_TURRET_MID.value+0.04);
    }
    public void servosetpositon_mid_1(){
        shooterAngleServo.setPosition(ServoConstants.SHOOTER_TURRET_MID.value+0.06);

    }





    public void accelerate_mid(){

        shooterDown.setVelocity(MotorConstants.SHOOTER_MID_VELOCITY.value);
        shooterUp.setVelocity(MotorConstants.SHOOTER_MID_VELOCITY.value);


        targetVelocity = MotorConstants.SHOOTER_MID_VELOCITY.value;

        if (Math.abs(shooterUp.getVelocity() - 1320)<= 40){
            isAsTargetVelocity = true;
        } else {
            isAsTargetVelocity = false;
        }


    }
    public void accelerate_slow(){
        shooterDown.setVelocity(MotorConstants.SHOOTER_SLOW_VELOCITY.value);
        shooterUp.setVelocity(MotorConstants.SHOOTER_SLOW_VELOCITY.value);
        shooterAngleServo.setPosition(ServoConstants.SHOOTER_TURRET_SLOW.value);


    }
    public void accelerate_fast(){
        shooterDown.setVelocity(MotorConstants.SHOOTER_FAST_VELOCITY.value);
        shooterUp.setVelocity(MotorConstants.SHOOTER_FAST_VELOCITY.value);
        shooterAngleServo.setPosition(ServoConstants.SHOOTER_TURRET_LONG.value);

        if (Math.abs(shooterDown.getVelocity() - 1500) <= 40){
            isAsTargetVelocity = true;
        } else {
            isAsTargetVelocity = false;
        }

    }

    public void accelerate_idle(){
        shooterDown.setVelocity(MotorConstants.SHOOTER_IDLE_VELOCITY.value);
        shooterUp.setVelocity(MotorConstants.SHOOTER_IDLE_VELOCITY.value);
        shooterAngleServo.setPosition(ServoConstants.SHOOTER_TURRET_SLOW.value);

    }

    public void shoot(){
        preShooter.setPower(1);
    }


    public void stopShoot(){
        preShooter.setPower(0);
    }

    public void outtake(){
        preShooter.setPower(-0.8);
    }

    public void emergency(){
        shooterDown.setPower(-1);
        shooterUp.setPower(-1);
    }

    public void init(){
        preShooter.setPower(-0.1);
    }


    public void stopAccelerate(){
        shooterDown.setPower(0);
        shooterUp.setPower(0);
    }
}