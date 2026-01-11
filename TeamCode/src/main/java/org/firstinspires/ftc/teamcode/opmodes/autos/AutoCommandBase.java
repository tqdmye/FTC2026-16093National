package org.firstinspires.ftc.teamcode.opmodes.autos;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.intakepreshoot.IntakePreshooter;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.commands.autos.AccelerateAutoCommand;
import org.firstinspires.ftc.teamcode.commands.autos.IntakeAutoCommand;

import pedroPathing.Constants;


public abstract class AutoCommandBase extends LinearOpMode {
    protected Shooter shooter;

    protected IntakePreshooter intake;

    protected Follower follower;

    protected AutoCommand autoCommand;

    protected AccelerateAutoCommand accCommand;

    protected IntakeAutoCommand intakeAutoCommand;

    public abstract Command runAutoCommand();

    public abstract Pose getStartPose();




    private void initialize() {
        follower = Constants.createFollower(hardwareMap);

        follower.setStartingPose(getStartPose());
        shooter = new Shooter(hardwareMap);

        intake = new IntakePreshooter(hardwareMap);
        accCommand = new AccelerateAutoCommand(shooter);
        intakeAutoCommand = new IntakeAutoCommand(intake);


        this.autoCommand = new AutoCommand(shooter,intake);


    }

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();

        Command toRun = runAutoCommand();//.andThen(autoFinish());

        CommandScheduler.getInstance().schedule(toRun);
        CommandScheduler.getInstance().schedule(accCommand);
        CommandScheduler.getInstance().schedule(intakeAutoCommand);







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