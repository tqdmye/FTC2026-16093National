package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "Chassis test", group = "tests")
@Config
public class ChassisTest extends LinearOpMode {

    private final Telemetry telemetry_M =
            new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    public static int encoder_position = 1150;
    public static double max_power = 1;
    public static int shooterVelocity = 1800;
    public static int motorVelocity = 1800;
    public static boolean read_only = false;
    public static boolean reverse_0 = false;
    public static boolean reset = true;
    public static boolean set_power_mode = true;
    public static boolean isSetVelocity = false;
    public static boolean otherMotor = false;
    public static String motor_name_0 = "leftFront";
    public static String motor_name_1 = "leftRear";
    public static  String motor_name_2 = "rightFront";
    public static  String motor_name_3 = "rightRear";


    @Override
    public void runOpMode() {
        DcMotorEx motor0 = hardwareMap.get(DcMotorEx.class, motor_name_0);
        DcMotorEx motor1 = hardwareMap.get(DcMotorEx.class, motor_name_1);
        DcMotorEx motor2 = hardwareMap.get(DcMotorEx.class, motor_name_2);
        DcMotorEx motor3 = hardwareMap.get(DcMotorEx.class, motor_name_3);
        motor0.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motor0.setDirection(DcMotor.Direction.REVERSE);



        motor1.setDirection(DcMotor.Direction.REVERSE);
        waitForStart();

        while (opModeIsActive()) {
            if (set_power_mode) {
                if (read_only) {
                    motor0.setPower(0);
                    motor1.setPower(0);
                    motor2.setPower(0);
                    motor3.setPower(0);

                } else {
                    motor0.setPower(max_power);
                    motor1.setPower(max_power);
                    motor3.setPower(max_power);
                    motor2.setPower(max_power);
                }
                motor0.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motor3.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            }

            telemetry_M.addData("velocity_0", motor0.getVelocity());
            telemetry_M.addData("velocity_1", motor1.getVelocity());
            telemetry_M.addData("velocity_2", motor2.getVelocity());
            telemetry_M.addData("velocity_3", motor3.getVelocity());
            telemetry_M.update();
        }
    }
}
