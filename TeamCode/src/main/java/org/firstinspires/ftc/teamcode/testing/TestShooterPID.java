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
  public static double setP = 30;
  public static double setI = 0;
  public static double setD = 0;
  public static double setF = 15;
  public static double setShooterPower = 1;
  public static boolean isPowerMode = false;
  public static double setPreShooterPower = 1;
//  public static double shooterMinVelocity = 1400.0;
  public static double shooterVelocity = 1440;

  public static volatile double servo_pos = 0.98;

  @Override
  public void runOpMode() throws InterruptedException {
    DcMotorEx shooterDown = hardwareMap.get(DcMotorEx.class, "shooterDown");
    DcMotorEx shooterUp = hardwareMap.get(DcMotorEx.class, "shooterUp");
    DcMotorEx preShooter = hardwareMap.get(DcMotorEx.class, "preShooter");
    DcMotorEx intake = hardwareMap.get(DcMotorEx.class, "intake");
    Servo shooterAngleServo = hardwareMap.get(Servo.class,"shooterAngle");

    Servo preLimit = hardwareMap.get(Servo.class,"preLimit");

    shooterDown.setDirection(DcMotorSimple.Direction.REVERSE);
    shooterUp.setDirection(DcMotorSimple.Direction.FORWARD);
    intake.setDirection(DcMotorSimple.Direction.REVERSE);


    shooterDown.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
    shooterUp.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

    shooterDown.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    shooterDown.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    shooterUp.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    shooterUp.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    if (isPIDControl) {
      shooterDown.setVelocityPIDFCoefficients(setP, setI, setD, setF);
      shooterUp.setVelocityPIDFCoefficients(setP, setI, setD, setF);
    }

    waitForStart();

    while (opModeIsActive()) {
      if(isPowerMode){
        shooterDown.setPower(setShooterPower);
        shooterUp.setPower(setShooterPower);
      }
      else{
        shooterDown.setVelocity(shooterVelocity);
        shooterUp.setVelocity(shooterVelocity);
        shooterAngleServo.setPosition(servo_pos);

      }

//      if (frontShooter.getVelocity() > shooterMinVelocity) {
      if(gamepad1.a){
        preShooter.setPower(setPreShooterPower);
        intake.setPower(1);
        preLimit.setPosition(0.4);
      }
      else{
        preShooter.setPower(0);
        intake.setPower(0);
        preLimit.setPosition(0.4);
      }

//      if (frontShooter.getVelocity() < shooterMinVelocity) {
//        //            if(gamepad1.b){\
//        preShooter.setPower(0);
//        blender.setPower(0);
//        intake.setPower(0);
//      }

      telemetry_M.addData("Shooter Down Velocity", shooterDown.getVelocity());
      telemetry_M.addData("Shooter Up Velocity", shooterDown.getVelocity());

      telemetry_M.addData(
              "PreShooter Velocity", preShooter.getVelocity());
      telemetry_M.update();

    }
  }
}