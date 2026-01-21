package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "Test Shooter PID", group = "test")
@Config
public class TestShooterPID extends LinearOpMode {
  private final Telemetry telemetry_M =
          new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
  public static boolean isPIDControl = true;
  public static double setP = 40;
  public static double setI = 0;
  public static double setD = 17;
  public static double setF = 17.5;
  public static double setShooterPower = 1;
  public static boolean isPowerMode = false;
  public static double setPreShooterPower = 1;
  //  public static double shooterMinVelocity = 1400.0;
  public static double shooterVelocity = 1440;

  public static volatile double servo_pos = 0.35;

  @Override
  public void runOpMode() throws InterruptedException {
    DcMotorEx shooterLeft = hardwareMap.get(DcMotorEx.class, "shooterLeft");
    DcMotorEx shooterRight = hardwareMap.get(DcMotorEx.class, "shooterRight");


    DcMotorEx intake = hardwareMap.get(DcMotorEx.class, "intake");
    DcMotorEx preShooter = hardwareMap.get(DcMotorEx.class, "preShooter");
    Servo shooterAngleServo = hardwareMap.get(Servo.class,"shooterAngle");

    Servo preLimit = hardwareMap.get(Servo.class,"preLimit");

    shooterLeft.setDirection(DcMotorSimple.Direction.FORWARD);
    shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);
    intake.setDirection(DcMotorSimple.Direction.REVERSE);

    shooterAngleServo.setDirection(Servo.Direction.FORWARD);


    shooterLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
    shooterRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);


    shooterLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    shooterRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


    if (isPIDControl) {
      shooterLeft.setVelocityPIDFCoefficients(setP, setI, setD, setF);
      shooterRight.setVelocityPIDFCoefficients(setP, setI, setD, setF);

    }

    waitForStart();

    while (opModeIsActive()) {
      if(isPowerMode){
        shooterLeft.setPower(setShooterPower);
        shooterRight.setPower(setShooterPower);
      }
      else{
        shooterLeft.setVelocity(shooterVelocity);
        shooterRight.setVelocity(shooterVelocity);

        shooterAngleServo.setPosition(servo_pos);

      }

//      if (frontShooter.getVelocity() > shooterMinVelocity) {
      if(gamepad1.a){

        intake.setPower(1);
        preShooter.setPower(setPreShooterPower);
        preLimit.setPosition(0.4);
      }
      else{

        intake.setPower(0);
        preShooter.setPower(0);
        preLimit.setPosition(0.4);
      }

//      if (frontShooter.getVelocity() < shooterMinVelocity) {
//        //            if(gamepad1.b){\
//        preShooter.setPower(0);
//        blender.setPower(0);
//        intake.setPower(0);
//      }

      telemetry_M.addData("Shooter Down Velocity", shooterLeft.getVelocity());
      telemetry_M.addData("Shooter Up Velocity", shooterLeft.getVelocity());

      telemetry_M.update();

    }
  }
}