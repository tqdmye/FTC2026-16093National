package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "IntakeTest")
public class IntakeTest extends LinearOpMode {



    private DcMotorEx shooterMotorR;
    private DcMotorEx shooterMotorL;
    private DcMotorEx intakeMotor;
    IMU imu;

    @Override
    public void runOpMode() throws InterruptedException {
        double targetVelocity ;
        double currentVelocity;

        DcMotorEx frontLeftMotor = hardwareMap.get(DcMotorEx.class,"leftFront");
        DcMotorEx backLeftMotor = hardwareMap.get(DcMotorEx.class,"leftRear");
        DcMotorEx frontRightMotor = hardwareMap.get(DcMotorEx.class,"rightFront");
        DcMotorEx backRightMotor = hardwareMap.get(DcMotorEx.class,"rightRear");
        DcMotorEx preShooterMotor = hardwareMap.get(DcMotorEx.class,"preshooter");
        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class,"intake");

        Servo spinUp = hardwareMap.get(Servo.class,"spinUp");
        Servo spinDown = hardwareMap.get(Servo.class,"spinDown");

        frontLeftMotor.setDirection(DcMotorEx.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorEx.Direction.REVERSE);

        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);


        waitForStart();

        while(opModeIsActive()) {




            if (gamepad1.right_bumper){
                intakeMotor.setPower(1);
            } else {
                intakeMotor.setPower(0);
            }


            /*
            if ((Math.abs(currentVelocity-targetVelocity) <= 100) && (gamepad1.left_bumper)) {

                preShooterMotor.setPower(1);

            } else {
                preShooterMotor.setPower(0);

            }
            */

            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x * 1.1;
            double rx = gamepad1.right_stick_x;

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
        }
    }
}

