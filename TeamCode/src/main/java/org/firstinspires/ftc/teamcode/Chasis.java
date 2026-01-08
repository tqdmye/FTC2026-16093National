//package org.firstinspires.ftc.teamcode;
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorEx;
//
//@TeleOp(name="Chassis")
//public class Chasis extends LinearOpMode {
//    @Override
//    public void runOpMode() throws InterruptedException {
//
//
//        DcMotorEx frontLeftMotor = hardwareMap.get(DcMotorEx.class,"leftFront");
//        DcMotorEx backLeftMotor = hardwareMap.get(DcMotorEx.class,"leftRear");
//        DcMotorEx frontRightMotor = hardwareMap.get(DcMotorEx.class,"rightFront");
//        DcMotorEx backRightMotor = hardwareMap.get(DcMotorEx.class,"rightRear");
//
//
//
//
//
//        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
//
//
//
//        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
//
//
//        waitForStart();
//
//        while (opModeIsActive()){
//
//
//
//
//
//
//
//
//            double y = -gamepad1.left_stick_y;
//            double x = gamepad1.left_stick_x * 1.1;
//            double rx = gamepad1.right_stick_x;
//
//            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
//            double frontLeftPower = (y + x + rx) / denominator;
//            double backLeftPower = (y - x + rx) / denominator;
//            double frontRightPower = (y - x - rx) / denominator;
//            double backRightPower = (y + x - rx) / denominator;
//
//            double powerCoefficient = 1.0;
//
//            frontLeftMotor.setPower(frontLeftPower * powerCoefficient);
//            backLeftMotor.setPower(backLeftPower * powerCoefficient);
//            frontRightMotor.setPower(frontRightPower * powerCoefficient);
//            backRightMotor.setPower(backRightPower * powerCoefficient);
//        }
//    }
//}