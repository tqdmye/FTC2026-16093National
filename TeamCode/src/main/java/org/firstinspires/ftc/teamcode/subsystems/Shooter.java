package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class Shooter extends SubsystemBase {
    private boolean ShooterState ;
    private double targetVelocity = -1200;
    private double currentVelocity;
    public DcMotorEx shooterMotor;
    public DcMotorEx preShooterMotor;

    private final double motorInput = -1.0;

    public Shooter(final HardwareMap hardwareMap) {
        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter");
        preShooterMotor = hardwareMap.get(DcMotorEx.class, "preShooter");
        shooterMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        shooterMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);


    }

    public void startShooter() {
        ShooterState = true;
        shooterMotor.setVelocity(targetVelocity);
        currentVelocity = shooterMotor.getVelocity();
        telemetry.addData("ShooterState:", ShooterState);
        telemetry.addData("currentVelocity", shooterMotor.getVelocity());
        telemetry.addData("Difference", Math.abs(currentVelocity-targetVelocity));
        telemetry.update();

    }

    public void stopShooter() {
        ShooterState = false;
        shooterMotor.setPower(0);
        telemetry.addData("ShooterState:", ShooterState);
        telemetry.update();

    }

    public void startPreShooter() {
        preShooterMotor.setPower(1);

    }
    public void stopPreShooter(){
        preShooterMotor.setPower(0);
    }
}
