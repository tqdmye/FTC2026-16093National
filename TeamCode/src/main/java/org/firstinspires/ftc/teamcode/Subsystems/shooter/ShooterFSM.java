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

    public DcMotorEx shooterUp, shooterDown;
    public Servo shooterAngleServo;

    private double targetVelocity = 0.0;


    public ShooterFSM(HardwareMap hardwareMap) {

        shooterUp = hardwareMap.get(DcMotorEx.class, "shooterUp");
        shooterDown = hardwareMap.get(DcMotorEx.class, "shooterDown");
        shooterAngleServo = hardwareMap.get(Servo.class, "shooterAngle");

        shooterUp.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterDown.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterAngleServo.setDirection(Servo.Direction.REVERSE);

        shooterUp.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterDown.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        shooterUp.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterDown.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterUp.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterDown.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        shooterUp.setVelocityPIDFCoefficients(
                ShooterConstants.SHOOTER_P.value,
                ShooterConstants.SHOOTER_I.value,
                ShooterConstants.SHOOTER_D.value,
                ShooterConstants.SHOOTER_F.value
        );

        shooterDown.setVelocityPIDFCoefficients(
                ShooterConstants.SHOOTER_P.value,
                ShooterConstants.SHOOTER_I.value,
                ShooterConstants.SHOOTER_D.value,
                ShooterConstants.SHOOTER_F.value
        );
    }

    public void accelerate_slow() {
        targetVelocity = ShooterConstants.SHOOTER_SLOW_VELOCITY.value;
        shooterUp.setVelocity(ShooterConstants.SHOOTER_SLOW_VELOCITY.value);
        shooterDown.setVelocity(ShooterConstants.SHOOTER_SLOW_VELOCITY.value);
        shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_SLOW.value);
    }

    public void accelerate_mid() {
        targetVelocity = ShooterConstants.SHOOTER_MID_VELOCITY.value;
        shooterUp.setVelocity(ShooterConstants.SHOOTER_MID_VELOCITY.value);
        shooterDown.setVelocity(ShooterConstants.SHOOTER_MID_VELOCITY.value);
        if(targetVelocity-shooterUp.getVelocity()>150){
            shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_MID.value+0.07);
        }
        else{
            shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_MID.value);
        }
    }

    public void accelerate_fast() {
        targetVelocity = ShooterConstants.SHOOTER_FAST_VELOCITY.value;
        shooterUp.setVelocity(ShooterConstants.SHOOTER_FAST_VELOCITY.value);
        shooterDown.setVelocity(ShooterConstants.SHOOTER_FAST_VELOCITY.value);
        if(targetVelocity-shooterUp.getVelocity()>250){
            shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_LONG.value+0.15);
        }
        else if(targetVelocity-shooterUp.getVelocity()>150){
            shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_LONG.value+0.1);
        }
        else{
            shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_LONG.value);
        }
    }

    public void accelerate_idle() {
        shooterUp.setVelocity(ShooterConstants.SHOOTER_IDLE_VELOCITY.value);
        shooterDown.setVelocity(ShooterConstants.SHOOTER_IDLE_VELOCITY.value);
        shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_SLOW.value);
        state = State.IDLE;
    }

    public void emergency() {
        shooterUp.setPower(-1);
        shooterDown.setPower(-1);
    }

    public void stopAccelerate() {
        shooterUp.setPower(0);
        shooterDown.setPower(0);
    }



    public boolean isAsVelocity() {
        return Math.abs(targetVelocity - shooterUp.getVelocity()) < 100;
    }
}
