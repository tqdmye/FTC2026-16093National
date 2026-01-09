package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;


@TeleOp (name = "Test Localizer", group = "Testing")
@Config
public class TestLocalizer extends LinearOpMode {
    Follower follower;
    private final Telemetry telemetry_M = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

    public static double startX = 0;
    public static double startY = 0;
    public static double startHeading = 0;

    public static double targetX = 0;
    public static double targetY = 0;
    public static double targetHeading = 0;


    private Pose2d startPos;
    private boolean isMoving = false;
    private long moveStartTime = 0;
    private static final long MOVE_TIMEOUT_MS = 10000; // 10 second timeout
    @Override
    public void runOpMode(){
        // Initialize Follower


        // Set initial position
        startPos = new Pose2d(startX, startY, Math.toRadians(startHeading));
        follower.setStartingPose(new Pose(startX, startY, Math.toRadians(startHeading)));
        follower.update();

        // Setup telemetry
        telemetry_M.addData("Status", "Initialized");
        telemetry_M.addData("Start Position", "X: %.2f, Y: %.2f, Heading: %.2f",
            startX, startY, startHeading);
        telemetry_M.addData("Target Position", "X: %.2f, Y: %.2f, Heading: %.2f",
            targetX, targetY, targetHeading);
        telemetry_M.addData("Controls", "A: Move to target, B: Stop, X: Reset position");
        telemetry_M.update();

        waitForStart();

        while (opModeIsActive()){
            // Update follower
            follower.update();

            // Get current position
            Pose currentPose = follower.getPose();
            if (currentPose == null) {
                telemetry_M.addData("ERROR", "Current pose is null!");
                telemetry_M.update();
                sleep(20);
                continue;
            }
            double currentX = currentPose.getX();
            double currentY = currentPose.getY();
            double currentHeading = Math.toDegrees(currentPose.getHeading());

            // Handle input controls




            if (gamepad1.x) {
                // Reset to start position
                follower.setStartingPose(new Pose(startX, startY, Math.toRadians(startHeading)));
                follower.update();
                isMoving = false;
                telemetry_M.addData("Action", "Position reset to start");
            }

            // Check if movement is complete
            if (isMoving) {
                if (!follower.isBusy()) {
                    isMoving = false;
                    telemetry_M.addData("Action", "Movement completed");
                } else if (System.currentTimeMillis() - moveStartTime > MOVE_TIMEOUT_MS) {
                    // Timeout - stop movement by creating a path to current position



                    isMoving = false;
                    telemetry_M.addData("Action", "Movement timeout");
                }
            }

            // Calculate error - with null checks

            double errorX = 0, errorY = 0, errorHeading = 0;



            // Display telemetry
            telemetry_M.addData("Current Position", "X: %.3f, Y: %.3f, Heading: %.2f°", currentX, currentY, currentHeading);
            telemetry_M.addData("Target Position", "X: %.3f, Y: %.3f, Heading: %.2f°", targetX, targetY, targetHeading);
            telemetry_M.addData("Error", "X: %.3f, Y: %.3f, Heading: %.2f°", errorX, errorY, errorHeading);
            telemetry_M.addData("Movement Status", isMoving ? "Moving" : "Idle");
            telemetry_M.addData("Follower Busy", follower.isBusy());
            telemetry_M.addData("Move Timeout", "%.1fs", (System.currentTimeMillis() - moveStartTime) / 1000.0);
            telemetry_M.update();

            // Small delay to prevent overwhelming the system
            sleep(20);
        }
    }
}
