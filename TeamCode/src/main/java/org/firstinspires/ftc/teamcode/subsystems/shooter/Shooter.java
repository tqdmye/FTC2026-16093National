package org.firstinspires.ftc.teamcode.Subsystems.shooter;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Subsystems.Constants.ShooterConstants;
import org.firstinspires.ftc.teamcode.Subsystems.Constants.ShooterConstants;


public class Shooter {
    public DcMotorEx shooterLeft, shooterRight;
    public  Servo shooterAngleServo;
    private double targetVelocity;
    public boolean isAsTargetVelocity = false;


    public Shooter(HardwareMap hardwareMap) {
        this.shooterLeft = hardwareMap.get(DcMotorEx.class, "shooterLeft");
        this.shooterRight = hardwareMap.get(DcMotorEx.class, "shooterRight");
        this.shooterAngleServo = hardwareMap.get(Servo.class,"shooterAngle");

        shooterLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterAngleServo.setDirection(Servo.Direction.FORWARD);

        shooterLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        shooterRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        shooterLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        shooterRight.setVelocityPIDFCoefficients(ShooterConstants.SHOOTER_P.value, ShooterConstants.SHOOTER_I.value, ShooterConstants.SHOOTER_D.value, ShooterConstants.SHOOTER_F.value);
        shooterLeft.setVelocityPIDFCoefficients(ShooterConstants.SHOOTER_P.value, ShooterConstants.SHOOTER_I.value, ShooterConstants.SHOOTER_D.value, ShooterConstants.SHOOTER_F.value);
    }

    public void accelerate_mid(){
        shooterLeft.setVelocity(ShooterConstants.SHOOTER_MID_VELOCITY.value);
        shooterRight.setVelocity(ShooterConstants.SHOOTER_MID_VELOCITY.value);
        shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_MID.value);
        targetVelocity = ShooterConstants.SHOOTER_MID_VELOCITY.value;
    }
    public void accelerate_slow(){
        shooterLeft.setVelocity(ShooterConstants.SHOOTER_SLOW_VELOCITY.value);
        shooterRight.setVelocity(ShooterConstants.SHOOTER_SLOW_VELOCITY.value);
        shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_SLOW.value);
        targetVelocity = ShooterConstants.SHOOTER_SLOW_VELOCITY.value;
    }
    public void accelerate_fast(){
        targetVelocity = ShooterConstants.SHOOTER_FAST_VELOCITY.value;
        shooterLeft.setVelocity(ShooterConstants.SHOOTER_FAST_VELOCITY.value);
        shooterRight.setVelocity(ShooterConstants.SHOOTER_FAST_VELOCITY.value);
        if(Math.abs(shooterLeft.getVelocity() - targetVelocity)>100){
            shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_LONG.value+0.1);
        }
        else{
            shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_LONG.value);
        }
    }

    public void accelerate_idle(){
        shooterLeft.setVelocity(ShooterConstants.SHOOTER_IDLE_VELOCITY.value);
        shooterRight.setVelocity(ShooterConstants.SHOOTER_IDLE_VELOCITY.value);
        shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_SLOW.value);
    }

    public void emergency(){
        shooterLeft.setPower(-1);
        shooterRight.setPower(-1);
    }

    public void stopAccelerate(){
        shooterLeft.setPower(0);
        shooterRight.setPower(0);
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

    public double getVelocity(){
        return shooterRight.getVelocity();
    }

    public void checkVelocity(){
        isAsTargetVelocity = Math.abs(shooterLeft.getVelocity() - targetVelocity) <= 60;
    }
}