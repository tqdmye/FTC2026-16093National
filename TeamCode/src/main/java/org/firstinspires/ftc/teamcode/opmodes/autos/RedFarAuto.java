package org.firstinspires.ftc.teamcode.opmodes.autos;


import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
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
    private final Pose preparePose = new Pose(4.137, 7.539, Math.toRadians(-90));
    private final Pose intakeLoadingPose = new Pose(8.091, 2.056, Math.toRadians(-90));
    private final Pose intakeOtherPose = new Pose(8.091, 2.056, Math.toRadians(-90));


    public Command runAutoCommand() {
//        follower.setMaxPower(0.8);

        PathChain prepare = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, preparePose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), preparePose.getHeading())
                .build();

        PathChain intakeLoading = follower.pathBuilder()
                .addPath(new BezierLine(preparePose, intakeLoadingPose))
                .setLinearHeadingInterpolation(preparePose.getHeading(), intakeLoadingPose.getHeading())
                .build();


        PathChain score = follower.pathBuilder()
                .addPath(new BezierLine(intakeLoadingPose, scorePose))
                .setLinearHeadingInterpolation(intakeLoadingPose.getHeading(), scorePose.getHeading())
                .build();


        PathChain intakeInfinite = follower.pathBuilder()
                .addPath(new BezierLine(preparePose, intakeLoadingPose))
                .setLinearHeadingInterpolation(preparePose.getHeading(), intakeOtherPose.getHeading())
                .build();


        SequentialCommandGroup preload = new SequentialCommandGroup(
                autoCommand.accelFast(accCommand),
                autoCommand.limitOn(),
                autoCommand.intakeAuto(intakeAutoCommand),
                autoCommand.shootFarPreload()
        );

        SequentialCommandGroup scoreFirst = new SequentialCommandGroup(
                new driveAutoCommand(follower, prepare),
                new InstantCommand(()->follower.setMaxPower(0.6)),
                new driveAutoCommand(follower, intakeLoading),
                new InstantCommand(()->follower.setMaxPower(0.9)),
                new driveAutoCommand(follower, score),
                autoCommand.shootFar()
        );

        SequentialCommandGroup scoreInfinite = new SequentialCommandGroup(
                new driveAutoCommand(follower, prepare),
                new InstantCommand(()->follower.setMaxPower(0.6)),
                new driveAutoCommand(follower, intakeInfinite),
                new InstantCommand(()->follower.setMaxPower(0.9)),
                new driveAutoCommand(follower, score),
                autoCommand.shootFar()
        );


        return new SequentialCommandGroup(
                preload,
                scoreFirst,
                scoreInfinite,
                scoreInfinite,
                scoreInfinite,
                scoreInfinite,
                scoreInfinite,
                scoreInfinite,
                scoreInfinite,
                scoreInfinite,
                scoreInfinite,

                autoCommand.stopAll()

        );
    }

    @Override
    public Pose getStartPose() {
        return startPose;
    }


}

