package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "IntakeTest")
public class IntakeTest extends LinearOpMode {



    private DcMotorEx shooterMotorR;
    private DcMotorEx shooterMotorL;
    private DcMotorEx intakeMotor;

    @Override
    public void runOpMode() throws InterruptedException {
        double targetVelocity = -1200;
        double currentVelocity;

        DcMotorEx frontLeftMotor = hardwareMap.get(DcMotorEx.class,"leftFront");
        DcMotorEx backLeftMotor = hardwareMap.get(DcMotorEx.class,"leftRear");
        DcMotorEx frontRightMotor = hardwareMap.get(DcMotorEx.class,"rightFront");
        DcMotorEx backRightMotor = hardwareMap.get(DcMotorEx.class,"rightRear");
        DcMotorEx shooterMotor = hardwareMap.get(DcMotorEx.class,"shooter");
        DcMotorEx preShooterMotor = hardwareMap.get(DcMotorEx.class,"preShooter");
        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class,"intake");


        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE); // only for this robot (Broken motor)

        shooterMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        shooterMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);





        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);


        waitForStart();

        while(opModeIsActive()) {
            shooterMotor.setVelocity(targetVelocity);
            currentVelocity = shooterMotor.getVelocity();
            telemetry.addData("currentVelocity", shooterMotor.getVelocity());
            telemetry.addData("Difference", Math.abs(currentVelocity-targetVelocity));
            telemetry.update();

            if (gamepad1.right_bumper){
                intakeMotor.setPower(-1);
            } else {
                intakeMotor.setPower(0);
            }

            if ((Math.abs(currentVelocity-targetVelocity) <= 60) && (gamepad1.left_bumper)) {
                preShooterMotor.setPower(1);

            } else {
                preShooterMotor.setPower(0);

            }

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

