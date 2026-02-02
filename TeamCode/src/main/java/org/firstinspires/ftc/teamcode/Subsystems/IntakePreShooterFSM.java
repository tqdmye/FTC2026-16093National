package org.firstinspires.ftc.teamcode.Subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Subsystems.Constants.ShooterConstants;

public class IntakePreShooterFSM {

    public enum State {
        HOLDING,
        FIRING
    }

    public State state = State.HOLDING;

    private final DcMotorEx intake;
    private final DcMotorEx preShooter;

    private final Servo preLimit;
    private double powerScale = 1.0;



    public IntakePreShooterFSM(@NonNull HardwareMap hardwareMap){
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
        preLimit.setPosition(ShooterConstants.PRELIMIT_SHOOT.value);
        state = State.FIRING;
    }


    public void dntShoot(){
        preLimit.setPosition(ShooterConstants.PRELIMIT_DNT_SHOOT.value);
        preShooter.setPower(0);
        state = State.HOLDING;
    }

    public void stopPreShooter(){
        preShooter.setPower(0);
    }
}