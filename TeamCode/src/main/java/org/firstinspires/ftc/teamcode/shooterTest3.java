package org.firstinspires.ftc.teamcode;
//shooter电机的闭环控制

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "ShooterTest3")
public class shooterTest3 extends LinearOpMode {




    @Override
    public void runOpMode() throws InterruptedException {
        double targetVelocity = -1200;
        double currentVelocity;



        DcMotorEx shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter");
        DcMotorEx preShooterMotor = hardwareMap.get(DcMotorEx.class, "preShooter");
        DcMotorEx midShooterMotor = hardwareMap.get(DcMotorEx.class, "midShooter");




       // double motorInput = 1.0;


        shooterMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        shooterMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);



        waitForStart();
        
        while (opModeIsActive()) {
            shooterMotor.setVelocity(targetVelocity);
            currentVelocity = shooterMotor.getVelocity();
            telemetry.addData("currentVelocity", shooterMotor.getVelocity());
            telemetry.addData("Difference", Math.abs(currentVelocity-targetVelocity));
            telemetry.update();




            if (Math.abs(currentVelocity-targetVelocity) <= 40) {
                preShooterMotor.setPower(1);
                midShooterMotor.setPower(-1);



            } else {
                preShooterMotor.setPower(0);
                midShooterMotor.setPower(0);
            }







        }
    }}

