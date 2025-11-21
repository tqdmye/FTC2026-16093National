package org.firstinspires.ftc.teamcode.opmodes.autos; // make sure this aligns with class location

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;




import pedroPathing.Constants;

import org.firstinspires.ftc.teamcode.commands.IntakeAutoCommand;
import org.firstinspires.ftc.teamcode.commands.IntakeCommand;
import org.firstinspires.ftc.teamcode.commands.MecanumDriveCommand;
import org.firstinspires.ftc.teamcode.commands.ShooterAutoCommand;
import org.firstinspires.ftc.teamcode.commands.ShooterCommand;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

@Autonomous(name = "AutoShooterTest", group = "Auto")
public class AutoShooterTest extends OpMode {
    private MecanumDrive mecanumDrive;
    private MecanumDriveCommand mecanumDriveCommand;
    private Intake intake;
    private IntakeCommand intakeCommand;
    private Shooter shooter;
    private ShooterCommand ShooterCommand;
    private IntakeAutoCommand intakeAutoCommand;


    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer, shooterTimer;

    private int pathState;
    private final Pose startPose = new Pose(26.069, 122.085, Math.toRadians(142)); // Start Pose of our robot.
    private final Pose scorePose = new Pose(50, 96, Math.toRadians(140));// Scoring Pose of our robot. It is facing the goal at a 135 degree angle.

    private final Pose pickup1Pose1 = new Pose(42.832, 83.5, Math.toRadians(180)); // Highest (First Set) of Artifacts from the Spike Mark.
    private final Pose pickup1Pose2 = new Pose(22.861,83.5,Math.toRadians(180));






    private PathChain motion1,motion2,motion3;
    private ShooterAutoCommand ShooterAutoCommand;

    public void buildPaths() {

        motion1 = follower.pathBuilder()
                .addPath(new BezierLine(startPose,pickup1Pose1))
                .setLinearHeadingInterpolation(startPose.getHeading(), pickup1Pose1.getHeading())
                .setBrakingStrength(0.9)
                .build();
        motion2 = follower.pathBuilder()
                .addPath(new BezierLine(pickup1Pose1, pickup1Pose2))
                .setLinearHeadingInterpolation(pickup1Pose1.getHeading(), pickup1Pose2.getHeading())
                .setBrakingStrength(0.9)
                .build();
        motion3 = follower.pathBuilder()
                .addPath(new BezierLine(pickup1Pose2,scorePose))
                .setLinearHeadingInterpolation(pickup1Pose2.getHeading(),scorePose.getHeading())
                .setBrakingStrength(0.9)
                .build();



    }
    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(motion1);
                setPathState(1);
                break;
            case 1:

            /* You could check for
            - Follower State: "if(!follower.isBusy()) {}"
            - Time: "if(pathTimer.getElapsedTimeSeconds() > 1) {}"
            - Robot Position: "if(follower.getPose().getX() > 36) {}"
            */

                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    if (actionTimer.getElapsedTime() > 0.1) {
                        intake.setDefaultCommand(intakeAutoCommand);


                    }
                    /* Score Preload */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(motion2,true);
                    setPathState(2);
                }
                break;
            case 2:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if(!follower.isBusy()) {
                    /* Grab Sample */



                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(motion3,true);

                    ShooterAutoCommand.schedule();
                    shooterTimer.resetTimer();
                    if (shooterTimer.getElapsedTime() > 15.0) {
                        // 在15秒后停止射击
                        shooter.stopShooter(); // 停止射击
                        ShooterAutoCommand.cancel(); // 取消射击命令
                    }// 重置计时器

                    setPathState(-1);
                }
                break;


        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }
    @Override
    public void loop() {

        follower.update();

        autonomousPathUpdate();

        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();


        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setStartingPose(startPose);

    }

    /** This method is called continuously after Init while waiting for "play". **/
    @Override
    public void init_loop() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        // 初始化机器人子系统
        follower = Constants.createFollower(hardwareMap);
        intake = new Intake(hardwareMap);
        shooter = new Shooter(hardwareMap);

        // 初始化命令
        intakeAutoCommand = new IntakeAutoCommand(intake);
        ShooterAutoCommand = new ShooterAutoCommand(shooter);

        buildPaths();
        follower.setStartingPose(startPose);
    }

    /** This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system **/
    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    /** We do not use this because everything should automatically disable **/
    @Override
    public void stop() {}
}


