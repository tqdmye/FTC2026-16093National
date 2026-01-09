package org.firstinspires.ftc.teamcode.opmodes.autos;


import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commands.autos.driveAutoCommand;
@Config
@Autonomous(name = "Auto Red Near 3+9+3")
public class RedNearAuto extends AutoCommandBase {


    private final Pose startPose = new Pose(51.113, -47.224, Math.toRadians(309));

    private final Pose scorePose = new Pose(26.551, -26.884, Math.toRadians(313));

    private final Pose scoreMidPose = new Pose(11.416, -10.589, Math.toRadians(309));

    private final Pose prepare1Pose = new Pose(12.439, -27.770, Math.toRadians(270));

    private final Pose intake1Pose3 = new Pose(9.439, -53.656, Math.toRadians(270));
    private final Pose prepareGatePose = new Pose(1.635, -48.42, Math.toRadians(270));
    private final Pose openGatePose = new Pose(-5.635, -50.905, Math.toRadians(270));

    private final Pose openGateIntakePose = new Pose(65,4,Math.toRadians(-70));

    private final Pose prepare2Pose = new Pose(-13.4, -29.18, Math.toRadians(270));
    private final Pose intake2Pose1 = new Pose(53.764, 29.378, Math.toRadians(-88));
    private final Pose intake2Pose2 = new Pose(53.764, 20.378, Math.toRadians(-88));
    private final Pose intake2Pose3 = new Pose(-15, -58, Math.toRadians(268));
    private final Pose prepare3Pose = new Pose(-36.52, -28.23, Math.toRadians(270));

    private final Pose intake3Pose3 = new Pose(-36.52, -58, Math.toRadians(270));
    private final Pose intakeLoad1 = new Pose(-42.991, -62, Math.toRadians(180));
    private final Pose intakeLoad3 = new Pose(-62, -62 , Math.toRadians(180));
    private final Pose parkPose = new Pose(9.439, -27, Math.toRadians(270));

    private final Pose openGatePose2 = new Pose(72.982, 7.239, Math.toRadians(-1));

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
                .addPath(new BezierLine(prepare1Pose, intake1Pose3))
                .setLinearHeadingInterpolation(prepare1Pose.getHeading(), intake1Pose3.getHeading())
                .build();



        after1 = follower.pathBuilder()
                .addPath(new BezierLine(intake1Pose3,prepare1Pose))
                .setLinearHeadingInterpolation(intake1Pose3.getHeading(), prepare1Pose.getHeading())
                .build();

        openGate = follower.pathBuilder()
                .addPath(new BezierLine(prepare2Pose,openGatePose))
                .setLinearHeadingInterpolation(prepare2Pose.getHeading(), openGatePose.getHeading())
                .build();

        openGate2 = follower.pathBuilder()
                .addPath(new BezierCurve(prepareGatePose, openGatePose2))
                .setLinearHeadingInterpolation(prepareGatePose.getHeading(), openGatePose2.getHeading())
                .build();

        openGateIntake = follower.pathBuilder()
                .addPath(new BezierCurve(scorePose,openGateIntakePose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), openGateIntakePose.getHeading())
                .build();
        score4 = follower.pathBuilder()
                .addPath(new BezierCurve(openGateIntakePose, scorePose))
                .setLinearHeadingInterpolation(openGateIntakePose.getHeading(), scorePose.getHeading())
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
                .addPath(new BezierLine(prepare2Pose, intake2Pose3))
                .setLinearHeadingInterpolation(prepare2Pose.getHeading(), intake2Pose3.getHeading())
                .build();



        after2 = follower.pathBuilder()
                .addPath(new BezierLine(intake2Pose3, prepare2Pose))
                .setLinearHeadingInterpolation(intake2Pose3.getHeading(), prepare2Pose.getHeading())
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
                .addPath(new BezierLine(prepare3Pose,intake3Pose3))
                .setLinearHeadingInterpolation(prepare3Pose.getHeading(), intake3Pose3.getHeading())
                .build();



        after3 = follower.pathBuilder()
                .addPath(new BezierLine(intake3Pose3, prepare3Pose))
                .setLinearHeadingInterpolation(intake3Pose3.getHeading(), prepare3Pose.getHeading())
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
                autoCommand.limitOff(),
                autoCommand.intakeAuto(intakeAutoCommand),
                new driveAutoCommand(follower,scorePreload),
                autoCommand.shootPreload(),
                new driveAutoCommand(follower,prepare1),
                new driveAutoCommand(follower,intake1),
                new driveAutoCommand(follower,after1),
                new driveAutoCommand(follower,score1),
                autoCommand.shoot(),
                new driveAutoCommand(follower,prepare2),
                new driveAutoCommand(follower,intake2),
                new driveAutoCommand(follower,after2),
                new driveAutoCommand(follower,openGate),
                openGateCommand(),
                new driveAutoCommand(follower,score2),
                autoCommand.shoot(),
                new driveAutoCommand(follower,prepare3),
                new driveAutoCommand(follower,intake3),
                new driveAutoCommand(follower,after3),
                new driveAutoCommand(follower,score3),
                autoCommand.shoot(),
                new driveAutoCommand(follower,prepareMid),
                autoCommand.accelMid(accCommand),
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

