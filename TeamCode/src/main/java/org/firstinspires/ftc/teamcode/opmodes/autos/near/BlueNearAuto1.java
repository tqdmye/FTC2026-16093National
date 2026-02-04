package org.firstinspires.ftc.teamcode.opmodes.autos.near;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commands.autos.driveAutoCommand;
import org.firstinspires.ftc.teamcode.opmodes.autos.AutoCommandBase;

@Config
@Autonomous(name = "Auto Blue Near 1")
public class BlueNearAuto1 extends AutoCommandBase {

    /* ================= Pose ================= */

    private final Pose startPose      = new Pose(58.767, 45.313, Math.toRadians(53));
    private final Pose scorePose      = new Pose(40, 30, Math.toRadians(50));
    private final Pose scoreMidPose   = new Pose(20, 20, Math.toRadians(45));

    private final Pose prepare1Pose   = new Pose(14.5, 25.574, Math.toRadians(90));
    private final Pose intake1Pose    = new Pose(14.5, 51, Math.toRadians(90));

    private final Pose openGatePreparePose   = new Pose(10, 30, Math.toRadians(90));
    private final Pose openGatePose   = new Pose(6, 52, Math.toRadians(90));

    private final Pose prepare2Pose   = new Pose(-9.5, 25.318, Math.toRadians(90));
    private final Pose intake2Pose    = new Pose(-9.5, 56, Math.toRadians(90));

    private final Pose prepare3Pose   = new Pose(-32.5, 25.077, Math.toRadians(90));
    private final Pose intake3Pose    = new Pose(-32.5, 56, Math.toRadians(90));

    private final Pose parkPose       = new Pose(3.187, 40, Math.toRadians(90));


    /* ================= Small Commands ================= */

    private Command openGateWait() {
        return new WaitCommand(1500);
    }

    /* ================= Auto ================= */

    @Override
    public Command runAutoCommand() {

        /* ---------- Paths ---------- */

        PathChain scorePreload = path(startPose, scorePose);

        PathChain prepare1 = path(scorePose, prepare1Pose);
        PathChain intake1 = path(prepare1Pose, intake1Pose);
        PathChain after1 = path(intake1Pose, prepare1Pose);
        PathChain score1 = path(openGatePose, scoreMidPose);

        PathChain prepare2 = path(scoreMidPose, prepare2Pose);
        PathChain intake2 = path(prepare2Pose, intake2Pose);
        PathChain after2 = path(intake2Pose, prepare2Pose);
        PathChain score2 = path(prepare2Pose, scoreMidPose);

        PathChain prepare3 = path(scoreMidPose, prepare3Pose);
        PathChain intake3 = path(prepare3Pose, intake3Pose);
        PathChain after3 = path(intake3Pose, prepare3Pose);
        PathChain score3 = path(prepare3Pose, scoreMidPose);

        PathChain park = path(scoreMidPose, parkPose);

        /* ---------- Command Groups ---------- */

        SequentialCommandGroup preload = new SequentialCommandGroup(
                new InstantCommand(()->follower.setMaxPower(0.7)),
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
                new driveAutoCommand(follower, path(intake1Pose, openGatePreparePose)),
                new driveAutoCommand(follower, path(openGatePreparePose, openGatePose)),
                openGateWait(),
                new driveAutoCommand(follower, score1),
                autoCommand.shootMid()
        );

        SequentialCommandGroup cycle2 = new SequentialCommandGroup(
                new driveAutoCommand(follower, prepare2),
                new driveAutoCommand(follower, intake2),
                new driveAutoCommand(follower, after2),
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

        /* ---------- Final Auto ---------- */

        return new SequentialCommandGroup(
                preload,
                cycle1,
                cycle2,
                cycle3,
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

    private PathChain path1(Pose start, Pose control,Pose end) {
        return follower.pathBuilder()
                .addPath(new BezierCurve(start,control, end))
                .setLinearHeadingInterpolation(start.getHeading(), end.getHeading())
                .build();
    }


    /* ================= Start Pose ================= */

    @Override
    public Pose getStartPose() {
        return startPose;
    }
}
