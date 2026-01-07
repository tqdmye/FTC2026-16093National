package org.firstinspires.ftc.teamcode.testing;//package org.firstinspires.ftc.teamcode.testing;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.Servo;
//
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//
//@TeleOp(name = "Servo Dual Test", group = "tests")
//@Config
//public class ServoDualTest extends LinearOpMode {
//
//    private final Telemetry telemetry_M =
//            new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
//    public static boolean read_only = false;
//    public static boolean reverse0 = false;
//
//    public static boolean reverse1 = true;
//    public static volatile double servo_pos = 0.5;
//
//
//    public static String servo_name0 = "shooterRight";
//    public static String servo_name1 = "shooterLeft";
//
//    public static String motor_name0 = "shooter";
//
//
//
//    @Override
//    public void runOpMode() {
//
//        Servo servo0 = hardwareMap.get(Servo.class, servo_name0);
//
//        Servo servo1 = hardwareMap.get(Servo.class, servo_name1);
//
//
//        if (reverse0) {
//            servo0.setDirection(Servo.Direction.REVERSE);
//        }
//
//        if (reverse1) {
//            servo1.setDirection(Servo.Direction.REVERSE);
//        }
//
//
//        waitForStart();
//
//        while (opModeIsActive()) {
//            if (!read_only){
//                servo0.setPosition(servo_pos);
//                servo1.setPosition(servo_pos-0.1);
//            }
//
//            telemetry_M.addData(servo_name0, servo0.getPosition());
//            telemetry_M.addData(servo_name1, servo1.getPosition());
//            telemetry_M.update();
//            idle();
//        }
//    }
//}
