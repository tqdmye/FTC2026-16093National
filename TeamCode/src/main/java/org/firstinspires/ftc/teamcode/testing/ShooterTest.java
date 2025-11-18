package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "shooter test", group = "test")
@Config
public class ShooterTest extends LinearOpMode {

    private final Telemetry telemetry_M = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

    public static double Velocity;

    private DcMotorEx shooterMotorR;
    private DcMotorEx shooterMotorL;

    @Override
    public void runOpMode() {
        shooterMotorR = hardwareMap.get(DcMotorEx.class,"shooterMotorR");
        shooterMotorL = hardwareMap.get(DcMotorEx.class,"shooterMotorL");


        waitForStart();

        while (opModeIsActive()) {
            shooterMotorR.setVelocity(100);
            shooterMotorL.setVelocity(100);


            telemetry_M.addData("R Power: ", shooterMotorR.getPower());
            telemetry_M.addData("L Power: ", shooterMotorL.getPower());
            telemetry_M.addData("R Velocity: ", shooterMotorR.getVelocity());
            telemetry_M.addData("L Velocity: ", shooterMotorL.getVelocity());

            telemetry_M.update();
        }
    }
}