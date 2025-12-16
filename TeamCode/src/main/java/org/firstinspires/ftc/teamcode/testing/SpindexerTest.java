package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "Spindexer Test", group = "tests")
@Config
public class SpindexerTest extends LinearOpMode {

    private final Telemetry telemetry_M = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    public static boolean read_only = true;
    public static boolean reverse = false;
    public static double motor_pow = 0.1;
    public static boolean reverse0 = false;

    public static boolean reverse1 = false;

    public static String motor_name1 = "intake" ;
    private DcMotorEx motor0 = null;
    private Servo servo0 = null;
    private Servo servo1 = null;
    public static volatile double servo_pos = 0.5;
    public static String servo_name0 = "spinUp";
    public static String servo_name1 = "spinDown";


    @Override
    public void runOpMode() {

        motor0 = hardwareMap.get(DcMotorEx.class, motor_name1);
        servo0 = hardwareMap.get(Servo.class, servo_name0);
        servo1 = hardwareMap.get(Servo.class, servo_name1);

        if (reverse0) {
            servo0.setDirection(Servo.Direction.REVERSE);
        }
        if (reverse1) {
            servo1.setDirection(Servo.Direction.REVERSE);
        }
        if (reverse){
            motor0.setDirection(DcMotorEx.Direction.REVERSE);
        }

        waitForStart();

        while (opModeIsActive()) {
            if (!read_only) {
                motor0.setPower(motor_pow);
                servo0.setPosition(servo_pos);
                servo1.setPosition(servo_pos);

                telemetry_M.addData("power", motor0.getPower());
                telemetry_M.update();
            } else if (read_only) {

                motor0.setPower(motor_pow);
                telemetry.addData("Position",motor0.getCurrentPosition());
                telemetry.update();

            }
        }
    }
}