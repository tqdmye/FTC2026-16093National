package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="Chasis")
public class Chasis extends LinearOpMode {
    @Override
    public void runOpMode() {


        DcMotorEx frontLeftMotor = hardwareMap.get(DcMotorEx.class,"leftFrontMotor");
        DcMotorEx backLeftMotor = hardwareMap.get(DcMotorEx.class,"leftBackMotor");
        DcMotorEx frontRightMotor = hardwareMap.get(DcMotorEx.class,"rightFrontMotor");
        DcMotorEx backRightMotor = hardwareMap.get(DcMotorEx.class,"rightBackMotor");

        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);


        waitForStart();

        while (opModeIsActive()){
            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double rx = -gamepad1.right_stick_x;

            double a = Math.max(Math.abs(x)+ Math.abs(y)+ Math.abs(rx), 1);

            frontLeftMotor.setPower((y + x + rx) / a);
            backLeftMotor.setPower((y-x+rx)/a);
            frontRightMotor.setPower((y-x-rx)/a);
            backRightMotor.setPower((y+x-rx)/a);
        }
    }
}