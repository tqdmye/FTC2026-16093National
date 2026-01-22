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
        ACCELERATING,
        READY,
        MAKEUP,
        FAST
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

    /* ================= 基础控制 ================= */

    public void accelerate_slow() {
        shooterLeft.setVelocity(ShooterConstants.SHOOTER_SLOW_VELOCITY.value);
        shooterRight.setVelocity(ShooterConstants.SHOOTER_SLOW_VELOCITY.value);
        shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_SLOW.value);
    }

    public void accelerate_mid() {
        targetVelocity = ShooterConstants.SHOOTER_MID_VELOCITY.value;
        shooterLeft.setVelocity(targetVelocity);
        shooterRight.setVelocity(targetVelocity);
        shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_MID.value);
    }

    public void accelerate_fast() {
        targetVelocity = ShooterConstants.SHOOTER_FAST_VELOCITY.value;
        shooterLeft.setVelocity(targetVelocity);
        shooterRight.setVelocity(targetVelocity);
        shooterAngleServo.setPosition(ShooterConstants.SHOOTER_TURRET_LONG.value);
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

    /* ================= Zone 接口 ================= */

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

    /* ================= 状态判断 ================= */

    public void checkVelocity() {
        double velocityError = Math.abs(shooterLeft.getVelocity()-targetVelocity);
        if (velocityError <= 60) {state = State.READY;}
        if (velocityError > 60 && velocityError <= 200) {state = State.MAKEUP;}
        if(velocityError > 200) {state = State.ACCELERATING;}
    }

    public double getVelocity() {
        return shooterRight.getVelocity();
    }
}
