package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.ArrayList;

@TeleOp(name="colortest")
public class testColor extends LinearOpMode {
    @Override
    public void runOpMode() {

        DcMotorEx leftFront = hardwareMap.get(DcMotorEx.class,"leftFront");
        DcMotorEx leftRear = hardwareMap.get(DcMotorEx.class,"leftRear");
        DcMotorEx rightFront = hardwareMap.get(DcMotorEx.class,"rightFront");
        DcMotorEx rightRear = hardwareMap.get(DcMotorEx.class,"rightRear");
        RevColorSensorV3 revColorSensor = hardwareMap.get(RevColorSensorV3.class, "colorSensor");


        revColorSensor.enableLed(true);

        rightFront.setDirection(DcMotorEx.Direction.REVERSE);
        rightRear.setDirection(DcMotorEx.Direction.REVERSE);


        ArrayList<Integer> colorRecord = new ArrayList<>();

        boolean inZone = false;


        waitForStart();

        while (opModeIsActive()) {
            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double rx = -gamepad1.right_stick_x;


            double a = Math.max(Math.abs(x) + Math.abs(y) + Math.abs(rx), 1);
            leftFront.setPower((y + x + rx) / a);
            leftRear.setPower((y - x + rx) / a);
            rightFront.setPower((y - x - rx) / a);
            rightRear.setPower((y + x - rx) / a);
            double r = revColorSensor.red();
            double g = revColorSensor.green();
            double b = revColorSensor.blue();
            boolean isGreen = g > r && g > b && g > 120;
            boolean isPurple = (r + b) / 2 > g && r > 80 && b > 80;

            double distance = ((DistanceSensor) revColorSensor).getDistance(DistanceUnit.CM);
            if (distance <= Constants.ballDetectionConstants.DIS_BOUNDARY && !inZone) {

                if (isPurple) {
                    colorRecord.add(1);   // 紫色
                } else if (isGreen) {
                    colorRecord.add(0);   // 绿色
                }
                inZone = true;
            }
            //2.2cm以内允许检测并记录，紫色记录为1，绿色记录为0
            if (distance > Constants.ballDetectionConstants.DIS_BOUNDARY){
                inZone =  false;
            }

            telemetry.addData("Red", r);
            telemetry.addData("Green", g);
            telemetry.addData("Blue", b);
            telemetry.addLine("----------------------------");
            telemetry.addData("Detected GREEN", isGreen);
            telemetry.addData("Detected PURPLE", isPurple);
            telemetry.addData("Distance", distance);
            telemetry.addLine("--------ColorRecord--------");
            telemetry.addData("ListSize", colorRecord.size());
            telemetry.addData("Record", colorRecord.toString());
            telemetry.addData("InZone", inZone);
            telemetry.addData("InZone", inZone);
            telemetry.update();
        }
    }
}