package org.firstinspires.ftc.teamcode.opmodes.autos;


import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commands.autos.driveAutoCommand;
@Config
@Autonomous(name = "Auto Red Far")
public class RedFarAuto extends AutoCommandBase {


    private final Pose startPose = new Pose(1.939, 51.423, Math.toRadians(-23.17));

    private final Pose scorePose = new Pose(1.939, 51.423, Math.toRadians(-23.17));

    private final Pose prepare1Pose = new Pose(4.137, 7.539, Math.toRadians(-90));
    private final Pose intake1Pose1 = new Pose(8.091, 2.056, Math.toRadians(-90));
    private final Pose intake1Pose2 = new Pose(5.565, 0.287, Math.toRadians(-180));
    private final Pose intake1Pose3 = new Pose(-4.086, 0.046, Math.toRadians(-180));
    private final Pose prepare2Pose  = new Pose(31.000, 32.834, Math.toRadians(-90));
    private final Pose intake2Pose1 = new Pose(8.091, 2.056, Math.toRadians(-90));
    private final Pose intake2Pose2 = new Pose(31.000, 23.932, Math.toRadians(-90));
    private final Pose intake2Pose3 = new Pose(31.000, -3, Math.toRadians(-90));
    private final Pose parkPose = new Pose(30.729, 49.009, Math.toRadians(-11.24));

    private PathChain scorePreload,
            prepare1,  after1,
            prepare2, after2,
            prepare3,  after3,
            score1, score2, score3,
            openGate, afterOpenGate,openGate2,
            intake1,intake2,intake3,
            openGateIntake,score4,intakeLoad,scoreMidLoad,prepareMid,park;

    @NonNull
    private Command openGateCommand(){
        return new WaitCommand(700);
    }




    public Command runAutoCommand() {
        follower.setMaxPower(0.8);
        scorePreload = follower.pathBuilder()
                .addPath(new BezierLine(startPose,scorePose))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading())
                .build();

        prepare1 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, prepare1Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), prepare1Pose.getHeading())
                .build();

        intake1 = follower.pathBuilder()
                .addPath(new BezierLine(prepare1Pose, intake1Pose1))
                .setLinearHeadingInterpolation(prepare1Pose.getHeading(), intake1Pose1.getHeading())
                .build();


        score1 = follower.pathBuilder()
                .addPath(new BezierLine(intake1Pose1, scorePose))
                .setLinearHeadingInterpolation(intake1Pose1.getHeading(), scorePose.getHeading())
                .build();

        prepare2 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, prepare1Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), prepare1Pose.getHeading())
                .build();
        intake2 = follower.pathBuilder()
                .addPath(new BezierLine(prepare1Pose, intake1Pose1))
                .setLinearHeadingInterpolation(prepare1Pose.getHeading(), intake2Pose1.getHeading())
                .build();
        score2 = follower.pathBuilder()
                .addPath(new BezierLine(intake1Pose1, scorePose))
                .setLinearHeadingInterpolation(intake1Pose1.getHeading(), scorePose.getHeading())
                .build();

        prepare3 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, prepare1Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), prepare1Pose.getHeading())
                .build();
        intake3 = follower.pathBuilder()
                .addPath(new BezierLine(prepare1Pose, intake1Pose1))
                .setLinearHeadingInterpolation(prepare1Pose.getHeading(), intake1Pose1.getHeading())
                .build();
        score3 = follower.pathBuilder()
                .addPath(new BezierLine(intake1Pose1, scorePose))
                .setLinearHeadingInterpolation(intake1Pose1.getHeading(), scorePose.getHeading())
                .build();





        return new SequentialCommandGroup(
                autoCommand.accelFast(accCommand),
                autoCommand.limitOn(),
                autoCommand.intakeAuto(intakeAutoCommand),
                autoCommand.shootFarPreload(),
                new driveAutoCommand(follower,prepare1),
                new driveAutoCommand(follower,intake1),
                new driveAutoCommand(follower,score1),
                autoCommand.shootFar(),
                new driveAutoCommand(follower,prepare1),
                new driveAutoCommand(follower,intake2),
                new driveAutoCommand(follower,score1),
                autoCommand.shootFar(),
                new driveAutoCommand(follower,prepare1),
                new driveAutoCommand(follower,intake2),
                new driveAutoCommand(follower,score1),
                autoCommand.shootFar(),
                new driveAutoCommand(follower,prepare1),
                new driveAutoCommand(follower,intake2),
                new driveAutoCommand(follower,score1),
                autoCommand.shootFar(),
                new driveAutoCommand(follower,prepare1),
                new driveAutoCommand(follower,intake2),
                new driveAutoCommand(follower,score1),
                autoCommand.shootFar(),
                new driveAutoCommand(follower,prepare1),
                new driveAutoCommand(follower,intake2),
                new driveAutoCommand(follower,score1),
                autoCommand.shootFar(),
                new driveAutoCommand(follower,prepare1),
                new driveAutoCommand(follower,intake2),
                new driveAutoCommand(follower,score1),
                autoCommand.shootFar(),
                new driveAutoCommand(follower,prepare1),
                new driveAutoCommand(follower,intake2),
                new driveAutoCommand(follower,score1),
                autoCommand.shootFar(),
                new driveAutoCommand(follower,prepare1),
                new driveAutoCommand(follower,intake2),
                new driveAutoCommand(follower,score1),
                autoCommand.shootFar(),
                autoCommand.stopAll()

        );
    }

    @Override
    public Pose getStartPose() {
        return startPose;
    }


}

