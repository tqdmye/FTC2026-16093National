package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Shooter")
public class Shooter extends LinearOpMode {



    private Servo intakeLeft;
    private Servo intakeRight;


    private Servo tail;
    @Override
    public void runOpMode() throws InterruptedException {


        DcMotorEx shooterMotorR = hardwareMap.get(DcMotorEx.class,"shooterMotorR");
        DcMotorEx backRightMotor = hardwareMap.get(DcMotorEx.class,"rightBackMotor");
        DcMotorEx frontLeftMotor = hardwareMap.get(DcMotorEx.class,"leftFrontMotor");
        DcMotorEx backLeftMotor = hardwareMap.get(DcMotorEx.class,"leftBackMotor");
        DcMotorEx frontRightMotor = hardwareMap.get(DcMotorEx.class,"rightFrontMotor");

        DcMotorEx shooterMotorL = hardwareMap.get(DcMotorEx.class,"shooterMotorL");


        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class,"intakeMotor");

        DcMotorEx benzMotor = hardwareMap.get(DcMotorEx.class,"benzMotor");


        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);

        double motorInput = 1.0;

        intakeLeft = hardwareMap.get(Servo.class,"intakeLeft");
        intakeRight = hardwareMap.get(Servo.class,"intakeRight");


        tail = hardwareMap.get(Servo.class,"tail");

        intakeRight.setDirection(Servo.Direction.REVERSE);
        intakeLeft.setDirection(Servo.Direction.FORWARD);

        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        benzMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        benzMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        benzMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        waitForStart();


        while(opModeIsActive()) {
            int targetPosition = 165;
            benzMotor.setTargetPosition(targetPosition);
            benzMotor.setPower(0.2);



            while (benzMotor.isBusy() && opModeIsActive()) {

                telemetry.addData("Target Position", targetPosition);
                telemetry.addData("Current Position", benzMotor.getCurrentPosition());
                telemetry.update();

            }

            benzMotor.setPower(0);

            intakeMotor.setPower(motorInput);
            shooterMotorR.setVelocity(-800);
            shooterMotorL.setVelocity(-800);
            tail.setPosition(1);
            intakeLeft.setPosition(1);
            intakeRight.setPosition(1);


            }
            if (gamepad1.left_bumper) {
                tail.setPosition(0.3);
            }

            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x * 1.1;
            double rx = -gamepad1.right_stick_x;

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

