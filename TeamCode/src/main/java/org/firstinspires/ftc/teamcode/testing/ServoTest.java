
package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "servo test", group = "tests")
@Config
public class ServoTest extends LinearOpMode {

    private final Telemetry telemetry_M = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    public static boolean read_only = false;
    public static boolean reverse = false;
    public static double servo_pos = 1;

    public static String servo_name0 = "intakeRight";
    public static String servo_name1 = "intakeLeft";

    @Override
    public void runOpMode() {

        Servo servo0 = hardwareMap.get(Servo.class, servo_name0);
        Servo servo1 = hardwareMap.get(Servo.class, servo_name1);

        if (reverse) {
            servo0.setDirection(Servo.Direction.REVERSE);
        }

        waitForStart();

        while (opModeIsActive()) {
            if (!read_only) servo0.setPosition(servo_pos);
            telemetry_M.addData(servo_name0, servo0.getPosition());
            telemetry_M.addData(servo_name1, servo1.getPosition());
            telemetry_M.update();
        }
    }
}

