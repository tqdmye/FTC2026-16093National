package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Test TeleOp")
public class teleOpTest extends LinearOpMode {

    private DcMotorEx leftFront, leftRear, rightFront, rightRear;
    private DcMotorEx shooter, preShooter, midShooter;

    @Override
    public void runOpMode() {
        leftFront  = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftRear = hardwareMap.get(DcMotorEx.class, "leftRear");
        rightFront  = hardwareMap.get(DcMotorEx.class, "rightFront");
        rightRear = hardwareMap.get(DcMotorEx.class, "rightRear");
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        preShooter = hardwareMap.get(DcMotorEx.class, "preShooter");
        midShooter = hardwareMap.get(DcMotorEx.class, "midShooter");

        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftRear.setDirection(DcMotor.Direction.FORWARD);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightRear.setDirection(DcMotor.Direction.FORWARD);

        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        waitForStart();

        while (opModeIsActive()) {

            double drive  = -gamepad1.left_stick_y;   // 前后
            double strafe = gamepad1.left_stick_x;    // 左右平移
            double turn   = gamepad1.right_stick_x;   // 旋转

            double leftFrontPower  = drive + strafe + turn;
            double rightFrontPower = drive - strafe - turn;
            double leftBackPower   = drive - strafe + turn;
            double rightBackPower  = drive + strafe - turn;

            double max = Math.max(1.0, Math.abs(leftFrontPower));
            max = Math.max(max, Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            leftFrontPower  /= max;
            rightFrontPower /= max;
            leftBackPower   /= max;
            rightBackPower  /= max;

            leftFront.setPower(leftFrontPower);
            rightFront.setPower(rightFrontPower);
            leftRear.setPower(leftBackPower);
            rightRear.setPower(rightBackPower);

            if(gamepad1.right_trigger > 0.5){
                shooter.setPower(0.9);
            }
            else{
                shooter.setPower(0);
            }
            if(gamepad1.left_trigger > 0.5){
                preShooter.setPower(1);
            }
            else if(gamepad1.a){
                preShooter.setPower(-1);
            }
            else{
                preShooter.setPower(0);
            }
            if(gamepad1.left_bumper){
                midShooter.setPower(1);
            }
            else if(gamepad1.a){
                midShooter.setPower(-0.7);
            }
            else{
                midShooter.setPower(0);
            }


//            telemetry.update();
        }
    }
}