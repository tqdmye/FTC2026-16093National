package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.Constants.ServoConstants;


public class Shooter {
    public DcMotorEx shooterLeft, shooterRight, shooterMid;
    public  Servo shooterAngleServo;


    private double targetVelocity;

    public boolean isAsTargetVelocity;

    private double powerScale = 1.0;



    public Shooter(HardwareMap hardwareMap) {
        this.shooterLeft = hardwareMap.get(DcMotorEx.class, "shooterLeft");
        this.shooterRight = hardwareMap.get(DcMotorEx.class, "shooterRight");
        this.shooterMid = hardwareMap.get(DcMotorEx.class, "shooterMid");
        this.shooterAngleServo = hardwareMap.get(Servo.class,"shooterAngle");

        shooterLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterMid.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterAngleServo.setDirection(Servo.Direction.FORWARD);



        shooterLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        shooterRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        shooterMid.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        shooterLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterMid.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMid.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        shooterMid.setVelocityPIDFCoefficients(ShooterConstants.SHOOTER_P.value, ShooterConstants.SHOOTER_I.value, ShooterConstants.SHOOTER_D.value, ShooterConstants.SHOOTER_F.value);

        shooterRight.setVelocityPIDFCoefficients(ShooterConstants.SHOOTER_P.value, ShooterConstants.SHOOTER_I.value, ShooterConstants.SHOOTER_D.value, ShooterConstants.SHOOTER_F.value);
        shooterLeft.setVelocityPIDFCoefficients(ShooterConstants.SHOOTER_P.value, ShooterConstants.SHOOTER_I.value, ShooterConstants.SHOOTER_D.value, ShooterConstants.SHOOTER_F.value);
    }

    public void setPowerScale(double scale) {
        powerScale = scale;
    }




    public void accelerate_mid(){

        shooterLeft.setVelocity(ShooterConstants.SHOOTER_MID_VELOCITY.value);

        shooterRight.setVelocity(ShooterConstants.SHOOTER_MID_VELOCITY.value);
        shooterMid.setVelocity(ShooterConstants.SHOOTER_MID_VELOCITY.value);
        shooterAngleServo.setPosition(ServoConstants.SHOOTER_TURRET_MID.value);



        targetVelocity = ShooterConstants.SHOOTER_MID_VELOCITY.value;

        if (Math.abs(shooterRight.getVelocity() - 1320)<= 40){
            isAsTargetVelocity = true;
        } else {
            isAsTargetVelocity = false;
        }


    }
    public void accelerate_slow(){
        shooterLeft.setVelocity(ShooterConstants.SHOOTER_SLOW_VELOCITY.value);
        shooterRight.setVelocity(ShooterConstants.SHOOTER_SLOW_VELOCITY.value);
        shooterMid.setVelocity(ShooterConstants.SHOOTER_SLOW_VELOCITY.value);
        shooterAngleServo.setPosition(ServoConstants.SHOOTER_TURRET_SLOW.value);


    }
    public void accelerate_fast(){
        shooterLeft.setVelocity(ShooterConstants.SHOOTER_FAST_VELOCITY.value);
        shooterRight.setVelocity(ShooterConstants.SHOOTER_FAST_VELOCITY.value);
        shooterMid.setVelocity(ShooterConstants.SHOOTER_FAST_VELOCITY.value);
        shooterAngleServo.setPosition(ServoConstants.SHOOTER_TURRET_LONG.value);

        if (Math.abs(shooterLeft.getVelocity() - 1500) <= 40){
            isAsTargetVelocity = true;
        } else {
            isAsTargetVelocity = false;
        }

    }

    public void accelerate_idle(){
        shooterLeft.setVelocity(ShooterConstants.SHOOTER_IDLE_VELOCITY.value);
        shooterRight.setVelocity(ShooterConstants.SHOOTER_IDLE_VELOCITY.value);
        shooterMid.setVelocity(ShooterConstants.SHOOTER_IDLE_VELOCITY.value);

        shooterAngleServo.setPosition(ServoConstants.SHOOTER_TURRET_SLOW.value);

    }



    public void emergency(){
        shooterLeft.setPower(-1);
        shooterRight.setPower(-1);
        shooterMid.setPower(-1);
    }



    public void stopAccelerate(){
        shooterLeft.setPower(0);
        shooterRight.setPower(0);
        shooterMid.setPower(0);
    }
    public void applyZone(Shootzone zone) {
        switch (zone) {
            case CLOSE:
                accelerate_slow();
                break;

            case MID:
                accelerate_mid();
                break;

            case FAR:
                accelerate_fast();
                break;

            default:
                accelerate_idle();
                break;
        }
    }

}