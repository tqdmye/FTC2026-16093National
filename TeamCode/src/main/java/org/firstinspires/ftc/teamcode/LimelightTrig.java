package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;


@TeleOp(name="nancy")
public class LimelightTrig extends LinearOpMode {
    //    private Limelight3A cameraFront;
    private GoBildaPinpointDriver odo;
    private double angle;
    private DcMotorEx cameraMotor;


    @Override
    public void runOpMode() throws InterruptedException {



        odo = hardwareMap.get(GoBildaPinpointDriver.class, "odo");
        cameraMotor = hardwareMap.get(DcMotorEx.class, "cameraMotor");

        odo.setOffsets(100, 90, DistanceUnit.MM);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        Pose2D pose2D = new Pose2D(DistanceUnit.MM, -1420, -1350, AngleUnit.DEGREES, Math.toRadians(0));
        odo.setPosition(pose2D);

        double localx,localy;
        double headingAngle;

        cameraMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        cameraMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        cameraMotor.setTargetPosition(0);
        cameraMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        cameraMotor.setPower(1);


        // Make sure your ID's match your configuration
        DcMotorEx frontLeftMotor = hardwareMap.get(DcMotorEx.class,"leftFrontMotor");
        DcMotorEx backLeftMotor = hardwareMap.get(DcMotorEx.class,"leftBackMotor");
        DcMotorEx frontRightMotor = hardwareMap.get(DcMotorEx.class,"rightFrontMotor");
        DcMotorEx backRightMotor = hardwareMap.get(DcMotorEx.class,"rightBackMotor");


        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            odo.update();
            localx =odo.getPosition().getX(DistanceUnit.MM);
            localy =odo.getPosition().getY(DistanceUnit.MM);
            telemetry.addData("x",localx);
            telemetry.addData("y",localy);
            headingAngle = odo.getPosition().getHeading(DEGREES);
            double angle = Math.atan((-localx)/(-localy));
            angle=Math.toDegrees(angle) + 90;
            telemetry.addData("angle",angle);
            angle = headingAngle+angle;
            cameraMotor.setTargetPosition((int) (angle));
            //cameraMotor.setTargetPosition((int) odo.getPosition().getHeading(RADIANS));
            telemetry.addData("degree", angle);
            telemetry.addData("motor Target Pos",cameraMotor.getTargetPosition());
            telemetry.addData("motor pos", cameraMotor.getCurrentPosition());
            telemetry.update();

            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            double powerCoefficent=1;

            frontLeftMotor.setPower(frontLeftPower*powerCoefficent);
            backLeftMotor.setPower(backLeftPower*powerCoefficent);
            frontRightMotor.setPower(frontRightPower*powerCoefficent);
            backRightMotor.setPower(backRightPower*powerCoefficent);
        }
    }
}