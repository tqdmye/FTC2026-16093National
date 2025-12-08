package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name="colortest")
public class testColor extends LinearOpMode {
    @Override
    public void runOpMode() {

        DcMotorEx frontLeftMotor = hardwareMap.get(DcMotorEx.class,"leftFront");
        DcMotorEx backLeftMotor = hardwareMap.get(DcMotorEx.class,"leftRear");
        DcMotorEx frontRightMotor = hardwareMap.get(DcMotorEx.class,"rightFront");
        DcMotorEx backRightMotor = hardwareMap.get(DcMotorEx.class,"rightRear");
        RevColorSensorV3 revColorSensor = hardwareMap.get(RevColorSensorV3.class, "colorSensor");


        revColorSensor.enableLed(true);

        frontRightMotor.setDirection(DcMotorEx.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorEx.Direction.REVERSE);

        int[] colorRecord = new int[3];
        int colorIndex = 0;
        boolean inZone = false;


        waitForStart();

        while (opModeIsActive()) {
            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double rx = -gamepad1.right_stick_x;


            double a = Math.max(Math.abs(x) + Math.abs(y) + Math.abs(rx), 1);
            frontLeftMotor.setPower((y + x + rx) / a);
            backLeftMotor.setPower((y - x + rx) / a);
            frontRightMotor.setPower((y - x - rx) / a);
            backRightMotor.setPower((y + x - rx) / a);
            double r = revColorSensor.red();
            double g = revColorSensor.green();
            double b = revColorSensor.blue();
            boolean isGreen = g > r && g > b && g > 120;
            boolean isPurple = (r + b) / 2 > g && r > 80 && b > 80;

            double distance = ((DistanceSensor) revColorSensor).getDistance(DistanceUnit.CM);
            if(distance <=2.2 && ! inZone){
                if(colorIndex < 3) {
                    if (isPurple) {
                        colorRecord[colorIndex] = 1;
                    }
                    else if (isGreen) {
                        colorRecord[colorIndex] = 0;
                    } else {

                    }
                    colorIndex++;
                }
                inZone = true;
            }
            if (distance > 2.2){
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
            telemetry.addData("0", colorRecord[0]);
            telemetry.addData("1", colorRecord[1]);
            telemetry.addData("2", colorRecord[2]);
            telemetry.addData("Index", colorIndex);
            telemetry.addData("InZone", inZone);
            telemetry.update();
        }
    }
}