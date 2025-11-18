package org.firstinspires.ftc.teamcode.testing;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.Telemetry;


@TeleOp(name = "motor test", group = "test")
@Config
public class MotorTest extends LinearOpMode {

    private final Telemetry telemetry_M = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    public static int encoder_position = 1150;
    public static double max_power = 1;
    public static int Velocity = 1800;
    public static boolean read_only = false;
    public static boolean reverse_0 = false;
    public static boolean reset = true;
    public static boolean set_power_mode = true;
    public static boolean isSetVelocity = false;
    public static boolean otherMotor = false;



    @Override
    public void runOpMode() {
        DcMotorEx frontLeftMotor = hardwareMap.get(DcMotorEx.class, "leftFrontMotor");
        DcMotorEx frontRightMotor = hardwareMap.get(DcMotorEx.class, "rightFrontMotor");
        DcMotorEx backLeftMotor = hardwareMap.get(DcMotorEx.class, "leftBackMotor");
        DcMotorEx backRightMotor = hardwareMap.get(DcMotorEx.class, "rightBackMotor");

        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {
            frontLeftMotor.setPower(0.1);
            frontRightMotor.setPower(0.1);
            backLeftMotor.setPower(0.1);
            backRightMotor.setPower(0.1);


            telemetry_M.addData("left front: ", frontLeftMotor.getVelocity());
            telemetry_M.addData("left back: ", backLeftMotor.getVelocity());
            telemetry_M.addData("right front: ", frontRightMotor.getVelocity());
            telemetry_M.addData("right back: ", backRightMotor.getVelocity());
            telemetry_M.update();
        }
    }
}