//package org.firstinspires.ftc.teamcode;
//
//import android.graphics.Color;
//
//import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
//import com.qualcomm.hardware.rev.RevColorSensorV3;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.ColorSensor;
//import com.qualcomm.robotcore.hardware.DistanceSensor;
//
//import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
//import org.firstinspires.ftc.teamcode.Subsystems.Constants.SensorConstants;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Queue;
//
//@TeleOp(name = "Ball Detection")
//public class ballDetection extends LinearOpMode {
//
//
//    private final float[] hsvValues = {0F, 0F, 0F};
//
//
//    private boolean ballDetected = false;
//    private boolean purple = false;
//    private boolean green = false;
//
//    private long ballNum = 0;
//
//    private Queue<Integer> colorQue = new LinkedList<>();
//
//    private List<Float> hues = new ArrayList<>();
//    private ColorSensor colorSensor;
//
//    private DistanceSensor distanceSensor;
//
//
//    private double ballDistance = 2.6;
//
//
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//
//        DistanceSensor distanceSensor = hardwareMap.get(Rev2mDistanceSensor.class, "intakeDistanceSensor");
//        ColorSensor colorSensor = hardwareMap.get(RevColorSensorV3.class, "intakeColorSensor");
//
//
//        waitForStart();
//        while (opModeIsActive()){
//            double dis = distanceSensor.getDistance(DistanceUnit.CM);
//
//
//            int r = colorSensor.red();
//            int g = colorSensor.green();
//            int b = colorSensor.blue();
//
//            // 转 HSV
//            float[] hsv = new float[3];
//            Color.RGBToHSV(r, g, b, hsv);
//            if (dis < ballDistance) {
//                hues.add(hsvValues[0]);
//                if (!ballDetected) ballNum++;
//                ballDetected = true;
//            }
//
//            if ((dis > ballDistance ) && ballDetected) {
//                ballDetected = false;
//
//                Collections.sort(hues);
//
//                float res = hues.get(hues.size() / 2);
//
//
//
//                if (res >= SensorConstants.PURPLE_MIN_H.value && res <= SensorConstants.PURPLE_MAX_H.value) {
//                    colorQue.offer(1);
//                    purple = true;
//                }
//                else if (res >= SensorConstants.GREEN_MIN_H.value && res <= SensorConstants.GREEN_MAX_H.value){
//                    colorQue.offer(0);
//                    green = true;
//                }
//                if (colorQue.size() > 3) colorQue.poll();
//
//                purple = false;
//                green = false;
//
//                hues.clear();
//            }
//            telemetry.addData("r", r);
//            telemetry.addData("g", g);
//            telemetry.addData("b", b);
//            telemetry.addData("dis", dis);
//            telemetry.addData("is purple", purple);
//            telemetry.addData("is green", green);
//            telemetry.addData("color",colorQue);
//            telemetry.update();
//
//        }
//
//
//
//
//
//
//    }
//}