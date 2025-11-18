package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;


import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "motor test2")
@Config
public class motorTest2 extends LinearOpMode {

    private final Telemetry telemetry_M = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    public static boolean read_only = true;
    public static boolean reverse = false;
    public static double motor_pow = 0.1;

    public static String motor_name1 = "ShooterMotor" ;
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

                telemetry_M.addData("leftFront", motor0.getPower());

                telemetry_M.update();
            } else if (read_only) {
                motor0.setPower(motor_pow);
                telemetry.addData("Position",motor0.getCurrentPosition());
                telemetry.update();
            }
        }
    }
}