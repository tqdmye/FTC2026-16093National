package org.firstinspires.ftc.teamcode.commands.autos;


import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;


public abstract class autoCommandBase extends LinearOpMode {
    protected Shooter shooter;

    protected Intake intake;

    protected Telemetry telemetryM;
    protected Follower follower;

    public abstract Command runAutoCommand();

    public abstract Pose getStartPose();

    private void initialize() {


        follower.setStartingPose(getStartPose());
        shooter = new Shooter(hardwareMap);
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