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
@Autonomous(name = "Auto Red Near")
public class RedNearAuto extends AutoCommandBase {


    private final Pose startPose = new Pose(46.920, -45.665, Math.toRadians(-58));

    private final Pose scorePose = new Pose(20.826, -25.971, Math.toRadians(-53));

    private final Pose scoreMidPose = new Pose(9.089, -15.059,Math.toRadians(-45));

    private final Pose prepare1Pose = new Pose(4.585, -27.999, Math.toRadians(-90));

    private final Pose intake1Pose = new Pose(4.443, -52.023, Math.toRadians(-90));
    private final Pose prepareGatePose = new Pose(-8.759, -47.065, Math.toRadians(-90));
    private final Pose openGatePose = new Pose(-8.675, -53.34, Math.toRadians(-90));

    private final Pose openGateIntakePose = new Pose(-20.337,-57,Math.toRadians(-90));

    private final Pose prepare2Pose = new Pose(-20.352, -27.582, Math.toRadians(-90));
    private final Pose intake2Pose = new Pose(-20.89, -56.530, Math.toRadians(-90));
    private final Pose prepare3Pose = new Pose(-44.041, -28.559, Math.toRadians(-90));

    private final Pose intake3Pose = new Pose(-44.041, -56.630, Math.toRadians(-90));
    private final Pose intakeLoad1 = new Pose(-47.631, -62.190, Math.toRadians(-90));
    private final Pose intakeLoad3 = new Pose(-65.59, -62.19 , Math.toRadians(-90));
    private final Pose parkPose = new Pose(9.439, -27, Math.toRadians(-90));

    private final Pose openGatePose2 = new Pose(72.982, 7.239, Math.toRadians(-1));

    private PathChain scorePreload,
            prepare1, intake1, after1, score1,
            openGate,
            prepare2, intake2, after2, score2,
            prepare3, intake3, after3, score3,
            intakeLoad, prepareMid, scoreMidLoad, park;

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
                .addPath(new BezierLine(prepare1Pose, intake1Pose))
                .setLinearHeadingInterpolation(prepare1Pose.getHeading(), intake1Pose.getHeading())
                .build();



        after1 = follower.pathBuilder()
                .addPath(new BezierLine(intake1Pose,prepare1Pose))
                .setLinearHeadingInterpolation(intake1Pose.getHeading(), prepare1Pose.getHeading())
                .build();

        openGate = follower.pathBuilder()
                .addPath(new BezierLine(prepare2Pose,openGatePose))
                .setLinearHeadingInterpolation(prepare2Pose.getHeading(), openGatePose.getHeading())
                .build();


        score1 = follower.pathBuilder()
                .addPath(new BezierLine(prepare1Pose, scorePose))
                .setLinearHeadingInterpolation(prepare1Pose.getHeading(), scorePose.getHeading())
                .build();

        prepare2 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, prepare2Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), prepare2Pose.getHeading())
                .build();

        intake2 = follower.pathBuilder()
                .addPath(new BezierLine(prepare2Pose, intake2Pose))
                .setLinearHeadingInterpolation(prepare2Pose.getHeading(), intake2Pose.getHeading())
                .build();



        after2 = follower.pathBuilder()
                .addPath(new BezierLine(intake2Pose, prepare2Pose))
                .setLinearHeadingInterpolation(intake2Pose.getHeading(), prepare2Pose.getHeading())
                .build();

        score2 = follower.pathBuilder()
                .addPath(new BezierLine(openGatePose ,scorePose))
                .setLinearHeadingInterpolation(openGatePose.getHeading(), scorePose.getHeading())
                .build();

        prepare3 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose,prepare3Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), prepare3Pose.getHeading())
                .build();

        intake3 = follower.pathBuilder()
                .addPath(new BezierLine(prepare3Pose, intake3Pose))
                .setLinearHeadingInterpolation(prepare3Pose.getHeading(), intake3Pose.getHeading())
                .build();



        after3 = follower.pathBuilder()
                .addPath(new BezierLine(intake3Pose, prepare3Pose))
                .setLinearHeadingInterpolation(intake3Pose.getHeading(), prepare3Pose.getHeading())
                .build();

        score3 = follower.pathBuilder()
                .addPath(new BezierLine(prepare3Pose, scorePose))
                .setLinearHeadingInterpolation(prepare3Pose.getHeading(), scorePose.getHeading())
                .build();

        prepareMid = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, intakeLoad1))
                .setLinearHeadingInterpolation(scorePose.getHeading(), intakeLoad1.getHeading())
                .build();

        intakeLoad = follower.pathBuilder()
                .addPath(new BezierLine(intakeLoad1, intakeLoad3))
                .setLinearHeadingInterpolation(intakeLoad1.getHeading(), intakeLoad3.getHeading())
                .build();

        scoreMidLoad = follower.pathBuilder()
                .addPath(new BezierLine(intakeLoad3,scoreMidPose))
                .setLinearHeadingInterpolation(intakeLoad3.getHeading(), scoreMidPose.getHeading())
                .build();


        park = follower.pathBuilder()
                .addPath(new BezierLine(scoreMidPose, parkPose))
                .setLinearHeadingInterpolation(scoreMidPose.getHeading(), parkPose.getHeading())
                .build();


        return new SequentialCommandGroup(
                autoCommand.accelSlow(accCommand),
                autoCommand.limitOn(),
                autoCommand.intakeAuto(intakeAutoCommand),
                new driveAutoCommand(follower,scorePreload),
                autoCommand.shootPreload(),

                new driveAutoCommand(follower,prepare1),
                new InstantCommand(()->follower.setMaxPower(0.7)),
                new driveAutoCommand(follower,intake1),
                new InstantCommand(()->follower.setMaxPower(1)),
                new driveAutoCommand(follower,after1),
                new driveAutoCommand(follower,score1),
                autoCommand.shoot(),

                new driveAutoCommand(follower,prepare2),
                new InstantCommand(()->follower.setMaxPower(0.7)),
                new driveAutoCommand(follower,intake2),
                new InstantCommand(()->follower.setMaxPower(1)),
                new driveAutoCommand(follower,after2),
                new driveAutoCommand(follower,openGate),
                autoCommand.accelMid(accCommand),
                openGateCommand(),
                new driveAutoCommand(follower,score2),
                autoCommand.shootMid(),

                new driveAutoCommand(follower,prepare3),
                new InstantCommand(()->follower.setMaxPower(0.7)),
                new driveAutoCommand(follower,intake3),
                new InstantCommand(()->follower.setMaxPower(1)),
                new driveAutoCommand(follower,after3),
                new driveAutoCommand(follower,score3),
                autoCommand.shootMid(),

                new driveAutoCommand(follower,prepareMid),
                new driveAutoCommand(follower,intakeLoad),
                new driveAutoCommand(follower,scoreMidLoad),
                autoCommand.shootMid(),

                new driveAutoCommand(follower,park),
                autoCommand.stopAll()

        );



    }

    @Override
    public Pose getStartPose() {
        return startPose;
    }


}

