package org.firstinspires.ftc.teamcode.Subsystems.shooter;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Subsystems.Constants.ShooterConstants;

public class ShooterFSM {

    public enum State {
        IDLE,
        FAST,
        MID,
        SLOW,
        READY,
        ACCELERATING
    }
    public State state = State.IDLE;

    public DcMotorEx shooterLeft, shooterRight;
    public Servo shooterAngleServo;

    private double targetVelocity = 0.0;


    public ShooterFSM(HardwareMap hardwareMap) {

        shooterLeft = hardwareMap.get(DcMotorEx.class, "shooterLeft");
        shooterRight = hardwareMap.get(DcMotorEx.class, "shooterRight");
        shooterAngleServo = hardwareMap.get(Servo.class, "shooterAngle");

        shooterLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterAngleServo.setDirection(Servo.Direction.FORWARD);

        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        shooterLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        shooterLeft.setVelocityPIDFCoefficients(
                ShooterConstants.SHOOTER_P.value,
                ShooterConstants.SHOOTER_I.value,
                ShooterConstants.SHOOTER_D.value,
                ShooterConstants.SHOOTER_F.value
        );

        shooterRight.setVelocityPIDFCoefficients(
                ShooterConstants.SHOOTER_P.value,
                ShooterConstants.SHOOTER_I.value,
                ShooterConstants.SHOOTER_D.value,
                ShooterConstants.SHOOTER_F.value
        );
    }

    public void accelerate_slow() {
        shooterLeft.setVelocity(ShooterConstants.SHOOTER_SLOW_VELOCITY.value);
        shooterRight.setVelocity(ShooterConstants.SHOOTER_SLOW_VELOCITY.value);
        shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_SLOW.value);
    }

    public void accelerate_mid() {
        targetVelocity = ShooterConstants.SHOOTER_MID_VELOCITY.value;
        shooterLeft.setVelocity(ShooterConstants.SHOOTER_MID_VELOCITY.value);
        shooterRight.setVelocity(ShooterConstants.SHOOTER_MID_VELOCITY.value);
        if(targetVelocity-shooterLeft.getVelocity()>150){
            shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_MID.value+0.07);
        }
        else{
            shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_MID.value);
        }
    }

    public void accelerate_fast() {
        targetVelocity = ShooterConstants.SHOOTER_FAST_VELOCITY.value;
        shooterLeft.setVelocity(ShooterConstants.SHOOTER_FAST_VELOCITY.value);
        shooterRight.setVelocity(ShooterConstants.SHOOTER_FAST_VELOCITY.value);
        if(targetVelocity-shooterLeft.getVelocity()>250){
            shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_LONG.value+0.15);
        }
        else if(targetVelocity-shooterLeft.getVelocity()>150){
            shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_LONG.value+0.1);
        }
        else{
            shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_LONG.value);
        }
    }

    public void accelerate_idle() {
        shooterLeft.setVelocity(ShooterConstants.SHOOTER_IDLE_VELOCITY.value);
        shooterRight.setVelocity(ShooterConstants.SHOOTER_IDLE_VELOCITY.value);
        shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_SLOW.value);
        state = State.IDLE;
    }

    public void emergency() {
        shooterLeft.setPower(-1);
        shooterRight.setPower(-1);
    }

    public void stopAccelerate() {
        shooterLeft.setPower(0);
        shooterRight.setPower(0);
    }



    public boolean isAsVelocity() {
        return Math.abs(targetVelocity - shooterLeft.getVelocity()) < 100;
    }
}
