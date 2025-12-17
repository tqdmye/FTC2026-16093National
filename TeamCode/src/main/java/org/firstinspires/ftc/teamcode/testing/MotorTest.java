package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "Motor Test", group = "tests")
@Config
public class MotorTest extends LinearOpMode {

    private final Telemetry telemetry_M = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    public static boolean read_only = false;
    public static boolean reverse = false;
    public static double motor_pow = 0.1;


    public static String motor_name1 = "intake" ;
    private DcMotorEx motor0 = null;




    @Override
    public void runOpMode() {

        motor0 = hardwareMap.get(DcMotorEx.class, motor_name1);



        if (reverse){
            motor0.setDirection(DcMotorEx.Direction.REVERSE);
        }

        waitForStart();

        while (opModeIsActive()) {
            if (!read_only) {
                motor0.setPower(motor_pow);
            }
            telemetry_M.addData("power", motor0.getPower());
            telemetry_M.addData("velocity", motor0.getVelocity());
            telemetry_M.update();

        }
    }
}