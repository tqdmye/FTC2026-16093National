package org.firstinspires.ftc.teamcode.opmodes;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;

import pedroPathing.Constants;

public abstract class AutoCommandBase extends LinearOpMode {

    protected Intake intake;
    protected Shooter shooter;


    protected Follower follower;

    public abstract Command runAutoCommand();

    public abstract Pose getStartPose();

    private void initialize() {
        follower = Constants.createFollower(hardwareMap);

        follower.setStartingPose(getStartPose());


        intake = new Intake(hardwareMap);

    }

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();

        Command toRun = runAutoCommand();//.andThen(autoFinish());

        CommandScheduler.getInstance().schedule(toRun);



        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            periodic();
        }

        onAutoStopped();
        CommandScheduler.getInstance().reset();
        TelemetryPacket packet = new TelemetryPacket();
        FtcDashboard.getInstance().sendTelemetryPacket(packet);
    }

    /**
     *  The periodic function to update variables repeatedly。
     */
    public void periodic() {
        CommandScheduler.getInstance().run();
    }

    /**
     *  Executes when auto being stooped, either finished executing or stopped on DriverStation.
     */
    public void onAutoStopped() {}
}