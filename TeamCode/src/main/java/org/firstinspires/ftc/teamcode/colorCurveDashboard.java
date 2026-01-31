//package org.firstinspires.ftc.teamcode;
//
//import android.graphics.Color;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
//import com.qualcomm.hardware.rev.RevColorSensorV3;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//
//import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
//
//@Config
//@TeleOp(name="Color Curve Dashboard", group="Debug")
//public class colorCurveDashboard extends OpMode {
//
//    // green
//    public static double GREEN_MIN_H = 150;
//    public static double GREEN_MAX_H = 170;
//    public static double GREEN_MIN_S = 0.6;
//    public static double GREEN_MIN_V = 0;
//
//    // purple
//    public static double PURPLE_MIN_H = 205;
//    public static double PURPLE_MAX_H = 240;
//    public static double PURPLE_MIN_S = 0.4;
//    public static double PURPLE_MIN_V = 0;
//
//    RevColorSensorV3 color;
//    Rev2mDistanceSensor distanceSensor;
//    FtcDashboard dashboard;
//
//    @Override
//    public void init() {
//        color = hardwareMap.get(RevColorSensorV3.class, "intakeColorSensor");
//        distanceSensor = hardwareMap.get(Rev2mDistanceSensor.class,"intakeDistanceSensor");
//        dashboard = FtcDashboard.getInstance();
//        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());
//    }
//
//    @Override
//    public void loop() {
//
//        // 读取 RGB
//        int r = color.red();
//        int g = color.green();
//        int b = color.blue();
//        double dis = distanceSensor.getDistance(DistanceUnit.CM);
//
//        // 转 HSV
//        float[] hsv = new float[3];
//        Color.RGBToHSV(r, g, b, hsv);
//
//        float H = hsv[0];
//        float S = hsv[1] ;
//        float V = hsv[2] ;
//
//
//
//        boolean isGreen =
//                (H >= GREEN_MIN_H && H <= GREEN_MAX_H &&
//                        S >= GREEN_MIN_S && V >= GREEN_MIN_V);
//
//        boolean isPurple =
//                (H >= PURPLE_MIN_H && H <= PURPLE_MAX_H &&
//                        S >= PURPLE_MIN_S);
//        //紫色的v会不知道为什么会上下乱跳，所以判断里把v删掉了
//
//
//
//        telemetry.addLine("=== HSV Data ===");
//        telemetry.addData("Hue (H)", H);
//        telemetry.addData("Saturation (S%)", S);
//        telemetry.addData("Value (V%)", V);
//
//        telemetry.addLine("\n=== RGB Raw ===");
//        telemetry.addData("R", r);
//        telemetry.addData("G", g);
//        telemetry.addData("B", b);
//
//        telemetry.addLine("\n=== Green Thresholds ===");
//        telemetry.addData("Green H Min", GREEN_MIN_H);
//        telemetry.addData("Green H Max", GREEN_MAX_H);
//        telemetry.addData("Green S Min", GREEN_MIN_S);
//        telemetry.addData("Green V Min", GREEN_MIN_V);
//        telemetry.addData("Detected GREEN?", isGreen);
//
//        telemetry.addLine("\n=== Purple Thresholds ===");
//        telemetry.addData("Purple H Min", PURPLE_MIN_H);
//        telemetry.addData("Purple H Max", PURPLE_MAX_H);
//        telemetry.addData("Purple S Min", PURPLE_MIN_S);
//        telemetry.addData("Purple V Min", PURPLE_MIN_V);
//        telemetry.addData("Detected PURPLE?", isPurple);
//
//        telemetry.update();
//    }
//}
