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
    public static double setP = 33;
    public static double setI = 0;
    public static double setD = 15;
    public static double setF = 15;
    public static double setShooterPower = 1;

    public static volatile double servo_pos = 0.5;
    public static boolean isVelocityMode = true;
    public static boolean isShooterLeft = false;
    public static boolean isShooterMid = false;
    public static boolean isShooterRight = true;
    public static boolean isShooterRightReverse = false;
    public static boolean isShooterLeftReverse = true;
    public static boolean isShooterMidReverse = false;

    public static double setPreShooterPower = 0.7;
    //  public static double shooterMinVelocity = 1400.0;
    public static double shooterVelocity = 1000;

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx shooterLeft = hardwareMap.get(DcMotorEx.class, "shooterLeft");
        DcMotorEx shooterRight = hardwareMap.get(DcMotorEx.class, "shooterRight");
        DcMotorEx shooterMid = hardwareMap.get(DcMotorEx.class, "shooterMid");

        DcMotorEx intake = hardwareMap.get(DcMotorEx.class, "intake");
        Servo shooterTurret = hardwareMap.get(Servo.class,"shooterAngle");


        intake.setDirection(DcMotorSimple.Direction.REVERSE);


        shooterLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        shooterRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        shooterLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMid.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterMid.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if (isPIDControl) {
            shooterLeft.setVelocityPIDFCoefficients(setP, setI, setD, setF);
            shooterRight.setVelocityPIDFCoefficients(setP, setI, setD, setF);
            shooterMid.setVelocityPIDFCoefficients(setP, setI, setD, setF);

        }

        waitForStart();

        while (opModeIsActive()) {
            if(isVelocityMode){
                if(isShooterLeft){
                    shooterLeft.setVelocity(shooterVelocity);
                    telemetry_M.addData("Shooter Left Velocity", shooterLeft.getVelocity());
                    telemetry_M.update();
                } else if (isShooterRight) {
                    shooterRight.setVelocity(shooterVelocity);
                    telemetry_M.addData("Shooter Right Velocity", shooterRight.getVelocity());
                    telemetry_M.update();

                } else if (isShooterMid) {
                    shooterMid.setVelocity(shooterVelocity);
                    telemetry_M.addData("Shooter Mid Velocity", shooterMid.getVelocity());
                    telemetry_M.update();

                }
            }
            if (isShooterMidReverse){
                shooterMid.setDirection(DcMotorEx.Direction.REVERSE);
            } else {
                shooterMid.setDirection(DcMotorSimple.Direction.FORWARD);
            }

            if (isShooterRightReverse){
                shooterRight.setDirection(DcMotorEx.Direction.REVERSE);
            } else {
                shooterRight.setDirection(DcMotorSimple.Direction.FORWARD);
            }

            if (isShooterLeftReverse){
                shooterLeft.setDirection(DcMotorEx.Direction.REVERSE);
            } else {
                shooterLeft.setDirection(DcMotorSimple.Direction.FORWARD);
            }



//      if (frontShooter.getVelocity() > shooterMinVelocity) {
            if(gamepad1.a){

                intake.setPower(1);
            }
            else{

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