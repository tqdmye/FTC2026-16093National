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

  public static volatile double servo_pos = 0.54;

  @Override
  public void runOpMode() throws InterruptedException {
    DcMotorEx shooterUp = hardwareMap.get(DcMotorEx.class, "shooterUp");
    DcMotorEx shooterDown = hardwareMap.get(DcMotorEx.class, "shooterDown");


    DcMotorEx intake = hardwareMap.get(DcMotorEx.class, "intake");
    DcMotorEx preShooter = hardwareMap.get(DcMotorEx.class, "preShooter");
    Servo shooterAngleServo = hardwareMap.get(Servo.class,"shooterAngle");

    Servo preLimit = hardwareMap.get(Servo.class,"preLimit");

    shooterUp.setDirection(DcMotorSimple.Direction.REVERSE);
    shooterDown.setDirection(DcMotorSimple.Direction.FORWARD);

    intake.setDirection(DcMotorSimple.Direction.REVERSE);
    preShooter.setDirection(DcMotorSimple.Direction.FORWARD);

    shooterAngleServo.setDirection(Servo.Direction.FORWARD);


    shooterUp.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
    shooterDown.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);


    shooterUp.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    shooterUp.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    shooterDown.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    shooterDown.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


    if (isPIDControl) {
      shooterUp.setVelocityPIDFCoefficients(setP, setI, setD, setF);
      shooterDown.setVelocityPIDFCoefficients(setP, setI, setD, setF);

    }

    waitForStart();

    while (opModeIsActive()) {
      if(isPowerMode){
        shooterUp.setPower(setShooterPower);
        shooterDown.setPower(setShooterPower);
      }
      else{
        shooterUp.setVelocity(shooterVelocity);
        shooterDown.setVelocity(shooterVelocity);

        shooterAngleServo.setPosition(servo_pos);

      }

//      if (frontShooter.getVelocity() > shooterMinVelocity) {
      if(gamepad1.a){

        intake.setPower(1);
        preShooter.setPower(setPreShooterPower);
        preLimit.setPosition(1);
      }
      else{

        intake.setPower(0);
        preShooter.setPower(0);
        preLimit.setPosition(0.68);
      }

//      if (frontShooter.getVelocity() < shooterMinVelocity) {
//        //            if(gamepad1.b){\
//        preShooter.setPower(0);
//        blender.setPower(0);
//        intake.setPower(0);
//      }

      telemetry_M.addData("Shooter Down Velocity", shooterDown.getVelocity());
      telemetry_M.addData("Shooter Up Velocity", shooterUp.getVelocity());

      telemetry_M.update();

    }
  }
}