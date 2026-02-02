package org.firstinspires.ftc.teamcode.opmodes.autos;

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
@Autonomous(name = "Auto Blue Near")
public class RedNearAuto extends AutoCommandBase {

    /* ================= Pose ================= */

    private final Pose startPose      = new Pose(46.920, 45.665, Math.toRadians(58));
    private final Pose scorePose      = new Pose(20.826, 25.971, Math.toRadians(53));
    private final Pose scoreMidPose   = new Pose(9.089, 15.059, Math.toRadians(45));

    private final Pose prepare1Pose   = new Pose(4.585, 27.999, Math.toRadians(90));
    private final Pose intake1Pose    = new Pose(4.443, 52.023, Math.toRadians(90));

    private final Pose prepare2Pose   = new Pose(-20.352, 27.582, Math.toRadians(90));
    private final Pose intake2Pose    = new Pose(-20.89, 56.530, Math.toRadians(90));

    private final Pose prepare3Pose   = new Pose(-44.041, 28.559, Math.toRadians(90));
    private final Pose intake3Pose    = new Pose(-44.041, 56.630, Math.toRadians(90));

    private final Pose openGatePose   = new Pose(-8.675, 53.34, Math.toRadians(90));

    private final Pose intakeLoad1    = new Pose(-47.631, 62.190, Math.toRadians(90));
    private final Pose intakeLoad3    = new Pose(-65.59,  62.19,  Math.toRadians(90));

    private final Pose parkPose       = new Pose(9.439, 27, Math.toRadians(90));

    /* ================= Paths ================= */

    private PathChain
            scorePreload,

    prepare1, intake1, after1, score1,
            prepare2, intake2, after2, score2,
            prepare3, intake3, after3, score3,

    prepareMid, intakeLoad, scoreMidLoad,
            park;

    /* ================= Small Commands ================= */

    private Command openGateWait() {
        return new WaitCommand(700);
    }

    /* ================= Auto ================= */

    @Override
    public Command runAutoCommand() {

        /* ---------- Paths ---------- */

        scorePreload = path(startPose, scorePose);

        prepare1 = path(scorePose, prepare1Pose);
        intake1  = path(prepare1Pose, intake1Pose);
        after1   = path(intake1Pose, prepare1Pose);
        score1   = path(prepare1Pose, scorePose);

        prepare2 = path(scoreMidPose, prepare2Pose);
        intake2  = path(prepare2Pose, intake2Pose);
        after2   = path(intake2Pose, prepare2Pose);
        score2   = path(openGatePose, scoreMidPose);

        prepare3 = path(scoreMidPose, prepare3Pose);
        intake3  = path(prepare3Pose, intake3Pose);
        after3   = path(intake3Pose, prepare3Pose);
        score3   = path(prepare3Pose, scoreMidPose);

        prepareMid   = path(scoreMidPose, intakeLoad1);
        intakeLoad   = path(intakeLoad1, intakeLoad3);
        scoreMidLoad = path(intakeLoad3, scoreMidPose);

        park = path(scoreMidPose, parkPose);

        /* ---------- Command Groups ---------- */

        SequentialCommandGroup preload = new SequentialCommandGroup(
                new InstantCommand(() -> intake.dntShoot()),

                autoCommand.accelSlow(),

                autoCommand.intakeAuto(intakeAutoCommand),
                new driveAutoCommand(follower, scorePreload),
                autoCommand.shootSlow()
        );

        SequentialCommandGroup cycle1 = new SequentialCommandGroup(
                autoCommand.accelMid(),
                new driveAutoCommand(follower, prepare1),
                new driveAutoCommand(follower, intake1),
                new driveAutoCommand(follower, after1),
                new driveAutoCommand(follower, score1),

                autoCommand.shootMid()
        );

        SequentialCommandGroup cycle2 = new SequentialCommandGroup(
                new driveAutoCommand(follower, prepare2),
                setPower(0.7),
                new driveAutoCommand(follower, intake2),
                setPower(1),
                new driveAutoCommand(follower, after2),
                new driveAutoCommand(follower, path(prepare2Pose, openGatePose)),
                openGateWait(),
                new driveAutoCommand(follower, score2),
                autoCommand.shootMid()
        );

        SequentialCommandGroup cycle3 = new SequentialCommandGroup(
                new driveAutoCommand(follower, prepare3),
                new driveAutoCommand(follower, intake3),
                new driveAutoCommand(follower, after3),
                new driveAutoCommand(follower, score3),

                autoCommand.shootMid()
        );


        SequentialCommandGroup midLoad = new SequentialCommandGroup(
                new driveAutoCommand(follower, prepareMid),
                new driveAutoCommand(follower, intakeLoad),
                new driveAutoCommand(follower, scoreMidLoad),
                autoCommand.shootMid()
        );

        /* ---------- Final Auto ---------- */

        return new SequentialCommandGroup(
                preload,
                cycle1,
                cycle2,
                cycle3,
                midLoad,
                new driveAutoCommand(follower, park),
                autoCommand.stopAll()
        );
    }

    /* ================= Helpers ================= */

    private PathChain path(Pose start, Pose end) {
        return follower.pathBuilder()
                .addPath(new BezierLine(start, end))
                .setLinearHeadingInterpolation(start.getHeading(), end.getHeading())
                .build();
    }

    private InstantCommand setPower(double power) {
        return new InstantCommand(() -> follower.setMaxPower(power));
    }

    private SequentialCommandGroup intakeScoreCycle(
            PathChain prepare,
            PathChain intake,
            PathChain after,
            PathChain score,
            Runnable shootCommand
    ) {
        return new SequentialCommandGroup(
                new driveAutoCommand(follower, prepare),
                setPower(0.7),
                new driveAutoCommand(follower, intake),
                setPower(1),
                new driveAutoCommand(follower, after),
                new driveAutoCommand(follower, score),
                new InstantCommand(shootCommand)
        );
    }

    /* ================= Start Pose ================= */

    @Override
    public Pose getStartPose() {
        return startPose;
    }
}
