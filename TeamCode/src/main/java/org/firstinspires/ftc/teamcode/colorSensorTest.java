package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@TeleOp(name="colorSensorTest")
public class colorSensorTest extends LinearOpMode {

    float hsvValues[] = {0F, 0F, 0F};
    final double SCALE_FACTOR = 255;
    private boolean ballDetected = false;
    private final double boundary = 2.0;

    private Queue<Integer> colorQue = new LinkedList<>();
    List<Float> hues = new ArrayList<>();
    @Override
    public void runOpMode() {


        DcMotorEx frontLeftMotor = hardwareMap.get(DcMotorEx.class,"leftFront");
        DcMotorEx backLeftMotor = hardwareMap.get(DcMotorEx.class,"leftRear");
        DcMotorEx frontRightMotor = hardwareMap.get(DcMotorEx.class,"rightFront");
        DcMotorEx backRightMotor = hardwareMap.get(DcMotorEx.class,"rightRear");
        DistanceSensor distanceSensor = hardwareMap.get(DistanceSensor.class, "colorSensor");
        ColorSensor colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");

        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);
         // Get the distance in cm

        waitForStart();

        while (opModeIsActive()){
            double dis = distanceSensor.getDistance(DistanceUnit.CM);

            double r = colorSensor.red();
            double g = colorSensor.green();
            double b = colorSensor.blue();


            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double rx = -gamepad1.right_stick_x;

            double a = Math.max(Math.abs(x)+ Math.abs(y)+ Math.abs(rx), 1);

            frontLeftMotor.setPower((y + x + rx) / a);
            backLeftMotor.setPower((y-x+rx)/a);
            frontRightMotor.setPower((y-x-rx)/a);
            backRightMotor.setPower((y+x-rx)/a);



            telemetry.addData("distance", dis);
            telemetry.addData("red",r);
            telemetry.addData("green",g);
            telemetry.addData("blue",b);

            telemetry.update();
        }
    }
}