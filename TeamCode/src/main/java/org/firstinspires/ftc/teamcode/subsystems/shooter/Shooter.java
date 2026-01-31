package org.firstinspires.ftc.teamcode.Subsystems.shooter;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.RepeatCommand;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Subsystems.Constants.ShooterConstants;


public class Shooter {
    public DcMotorEx shooterUp, shooterDown;
    public  Servo shooterAngleServo;
    private double targetVelocity = 9999;
    public boolean isAsTargetVelocity = false;


    public Shooter(HardwareMap hardwareMap) {
        this.shooterUp = hardwareMap.get(DcMotorEx.class, "shooterUp");
        this.shooterDown = hardwareMap.get(DcMotorEx.class, "shooterDown");
        this.shooterAngleServo = hardwareMap.get(Servo.class,"shooterAngle");

        shooterUp.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterDown.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterAngleServo.setDirection(Servo.Direction.FORWARD);

        shooterUp.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        shooterDown.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        shooterUp.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterDown.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterUp.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterDown.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        shooterDown.setVelocityPIDFCoefficients(ShooterConstants.SHOOTER_P.value, ShooterConstants.SHOOTER_I.value, ShooterConstants.SHOOTER_D.value, ShooterConstants.SHOOTER_F.value);
        shooterUp.setVelocityPIDFCoefficients(ShooterConstants.SHOOTER_P.value, ShooterConstants.SHOOTER_I.value, ShooterConstants.SHOOTER_D.value, ShooterConstants.SHOOTER_F.value);
    }

    public void accelerate_mid(){
        targetVelocity = ShooterConstants.SHOOTER_FAST_VELOCITY.value;
        shooterUp.setVelocity(ShooterConstants.SHOOTER_MID_VELOCITY.value);
        shooterDown.setVelocity(ShooterConstants.SHOOTER_MID_VELOCITY.value);
//        if(targetVelocity-shooterLeft.getVelocity()>150){
//            shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_MID.value+0.07);
//        }
//        else{
            shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_MID.value);
//        }
    }
    public void accelerate_slow(){
        shooterUp.setVelocity(ShooterConstants.SHOOTER_SLOW_VELOCITY.value);
        shooterDown.setVelocity(ShooterConstants.SHOOTER_SLOW_VELOCITY.value);
        shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_SLOW.value);
        targetVelocity = ShooterConstants.SHOOTER_SLOW_VELOCITY.value;
    }
    public void accelerate_fast(){
        targetVelocity = ShooterConstants.SHOOTER_FAST_VELOCITY.value;
        shooterUp.setVelocity(ShooterConstants.SHOOTER_FAST_VELOCITY.value);
        shooterDown.setVelocity(ShooterConstants.SHOOTER_FAST_VELOCITY.value);
        if(targetVelocity-shooterUp.getVelocity()>160){
            shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_LONG.value+0.14);
        }
        else if(targetVelocity-shooterUp.getVelocity()>120){
            shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_LONG.value+0.8);
        }
        else{
            shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_LONG.value);
        }
    }

    public void accelerate_idle(){
        shooterUp.setVelocity(ShooterConstants.SHOOTER_IDLE_VELOCITY.value);
        shooterDown.setVelocity(ShooterConstants.SHOOTER_IDLE_VELOCITY.value);
        shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_SLOW.value);
    }

    public void emergency(){
        shooterUp.setPower(-1);
        shooterDown.setPower(-1);
    }

    public void stopAccelerate(){
        shooterUp.setPower(0);
        shooterDown.setPower(0);
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
        return shooterDown.getVelocity();
    }

    public void checkVelocity(){
        isAsTargetVelocity = Math.abs(shooterUp.getVelocity() - targetVelocity) <= 60;
    }

    public RepeatCommand repeatedShootMid(){
        return new RepeatCommand(
                new InstantCommand(this::accelerate_mid)
        );
    }
}