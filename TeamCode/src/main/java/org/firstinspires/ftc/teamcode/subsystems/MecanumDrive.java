package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MecanumDrive extends SubsystemBase {

    private final DcMotorEx frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor;
    private final Telemetry telemetry;

    public MecanumDrive(Telemetry telemetry, final HardwareMap hardwareMap) {
        this.telemetry = telemetry;

        frontLeftMotor = hardwareMap.get(DcMotorEx.class, "leftFrontMotor");
        backLeftMotor = hardwareMap.get(DcMotorEx.class, "leftBackMotor");
        frontRightMotor = hardwareMap.get(DcMotorEx.class, "rightFrontMotor");
        backRightMotor = hardwareMap.get(DcMotorEx.class, "rightBackMotor");

        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void drive(double y, double x, double rx) {
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        double powerCoefficient = 1.0;

        frontLeftMotor.setPower(frontLeftPower * powerCoefficient);
        backLeftMotor.setPower(backLeftPower * powerCoefficient);
        frontRightMotor.setPower(frontRightPower * powerCoefficient);
        backRightMotor.setPower(backRightPower * powerCoefficient);

        telemetry.addData("Drive", "FL: %.2f, FR: %.2f, BL: %.2f, BR: %.2f",
                frontLeftPower, frontRightPower, backLeftPower, backRightPower);
        telemetry.update();
    }

    public void stop() {
        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);
    }
}