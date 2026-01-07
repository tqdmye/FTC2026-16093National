package org.firstinspires.ftc.teamcode.testing;//package org.firstinspires.ftc.teamcode.testing;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorEx;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.Servo;
//
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//
//@TeleOp(name = "LEDtest", group = "test")
//@Config
//public class LEDTest extends LinearOpMode {
//
//    private final Telemetry telemetry_M =
//            new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
//
//    public static double led_position = 1;
//
//    public static String led_name_0 = "indicatorLight";
//
//
//    @Override
//    public void runOpMode() {
//        Servo LED = hardwareMap.get(Servo.class, led_name_0);
//
//        waitForStart();
//
//        while (opModeIsActive()) {
//            LED.setPosition(led_position);
//
//            telemetry_M.addData("velocity_0", LED.getPosition());
//            telemetry_M.update();
//        }
//    }
//}
////0.333
////0.279