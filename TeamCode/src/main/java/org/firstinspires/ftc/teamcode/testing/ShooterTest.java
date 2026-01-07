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

@TeleOp(name = "ShooterTest", group = "test")
@Config
public class ShooterTest extends LinearOpMode {
    private final Telemetry telemetry_M =
            new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    public static boolean isPIDControl = true;
    public static double setP = 30;
    public static double setI = 0;
    public static double setD = 0;
    public static double setF = 15;
    public static double setShooterPower = 1;

    public static volatile double servo_pos = 0.5;
    public static boolean isVelocityMode = true;
    public static boolean isShooterUp = false;
    public static boolean isShooterDown = true;

    public static double setPreShooterPower = 0.7;
    //  public static double shooterMinVelocity = 1400.0;
    public static double shooterVelocity = 1000;

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx shooterDown = hardwareMap.get(DcMotorEx.class, "shooterDown");
        DcMotorEx shooterUp = hardwareMap.get(DcMotorEx.class, "shooterUp");
        DcMotorEx preShooter = hardwareMap.get(DcMotorEx.class, "preShooter");
        DcMotorEx intake = hardwareMap.get(DcMotorEx.class, "intake");
        Servo shooterTurret = hardwareMap.get(Servo.class,"shooterTurret");

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
            if(isVelocityMode){
                if(isShooterDown){
                    shooterDown.setVelocity(shooterVelocity);
                    telemetry_M.addData("Shooter Down Velocity", shooterDown.getVelocity());
                    telemetry_M.update();
                } else if (isShooterUp) {
                    shooterUp.setVelocity(shooterVelocity);
                    telemetry_M.addData("Shooter Up Velocity", shooterUp.getVelocity());
                    telemetry_M.update();

                }
            }


//      if (frontShooter.getVelocity() > shooterMinVelocity) {
            if(gamepad1.a){
                preShooter.setPower(setPreShooterPower);
                intake.setPower(1);
            }
            else{
                preShooter.setPower(0);
                intake.setPower(0);
            }

//      if (frontShooter.getVelocity() < shooterMinVelocity) {
//        //            if(gamepad1.b){
//        preShooter.setPower(0);
//        blender.setPower(0);
//        intake.setPower(0);
//      }




        }
    }
}