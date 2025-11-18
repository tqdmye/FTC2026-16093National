package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class Intake extends SubsystemBase {
    public DcMotorEx intakeMotor;
    public Servo intakeLeft;
    public Servo intakeRight;
    private final double motorInput = 1.0;

    public Intake(final HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
        intakeLeft = hardwareMap.get(Servo.class, "intakeLeft");
        intakeRight = hardwareMap.get(Servo.class, "intakeRight");
        intakeRight.setDirection(Servo.Direction.REVERSE);
        intakeLeft.setDirection(Servo.Direction.FORWARD);

        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void intakeIn() {
        intakeMotor.setPower(motorInput);
        intakeLeft.setPosition(1);
        intakeRight.setPosition(1);
    }

    public void intakeOut() {
        intakeMotor.setPower(-motorInput);
        intakeLeft.setPosition(0);
        intakeRight.setPosition(0);
    }

    public void stop() {
        intakeMotor.setVelocity(0);
        intakeLeft.setPosition(0.5);
        intakeRight.setPosition(0.5);
    }
}
