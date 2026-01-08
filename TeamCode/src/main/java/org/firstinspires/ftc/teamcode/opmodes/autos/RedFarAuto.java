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
@Autonomous(name = "Auto Red Far Test 3+9")
public class RedFarAuto extends AutoCommandBase {


    private final Pose startPose = new Pose(0.1, -16.907, Math.toRadians(0));

    private final Pose scorePose = new Pose(1.939, -12.894, Math.toRadians(330.17));

    private final Pose prepare1Pose = new Pose(-2, -18.524, Math.toRadians(270));
    private final Pose intake1Pose1 = new Pose(-2, -59, Math.toRadians(270));

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
                .setLinearHeadingInterpolation(prepare1Pose.getHeading(), intake1Pose1.getHeading())
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
                autoCommand.intakeAuto(intakeAutoCommand),
                new driveAutoCommand(follower,scorePreload),
                autoCommand.shootFar(),
                new driveAutoCommand(follower,prepare1),
                new driveAutoCommand(follower,intake1),
                new driveAutoCommand(follower,score1),
                autoCommand.shootFar(),
                new driveAutoCommand(follower,prepare2),
                new driveAutoCommand(follower,intake2),
                new driveAutoCommand(follower,score2),
                autoCommand.shootFar(),
                new driveAutoCommand(follower,prepare3),
                new driveAutoCommand(follower,intake3),
                new driveAutoCommand(follower,score3),
                autoCommand.shootFar()







        );



    }

    @Override
    public Pose getStartPose() {
        return startPose;
    }


}

