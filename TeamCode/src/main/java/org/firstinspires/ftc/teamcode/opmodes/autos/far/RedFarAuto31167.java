package org.firstinspires.ftc.teamcode.opmodes.autos.far;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commands.autos.driveAutoCommand;
import org.firstinspires.ftc.teamcode.opmodes.autos.AutoCommandBase;

@Config
@Autonomous(name = "Auto Red Far 31167")
public class RedFarAuto31167 extends AutoCommandBase {

    /* ================= Pose ================= */

    private final Pose startPose = new Pose(1.939, 51.423, Math.toRadians(-23));
    private final Pose scorePoseStraight =  new Pose(8, 40.423, Math.toRadians(-25));
    private final Pose scorePoseCross = new Pose(8, 40.423, Math.toRadians(-32));
    private final Pose preparePose = new Pose(4, 47, Math.toRadians(-90));
    private final Pose intakeLoadingPose = new Pose(0.5, 3, Math.toRadians(-90));
    private final Pose intakeOtherPose = new Pose(0.5, 6, Math.toRadians(-90));

    private final Pose parkPose = new Pose(0.5, 15, Math.toRadians(-90));


    private final Pose prepareCrossPose = new Pose(30, 40, Math.toRadians(-85));
    private final Pose intakeCrossPose = new Pose(30, 10, Math.toRadians(-90));

    /* ================= 参数 ================= */

    public static double AUTO_TOTAL_TIME = 30.0;
    public static double PARK_REMAIN_TIME = 0;

    /* ================= Auto ================= */

    @Override
    public Command runAutoCommand() {

        /* ---------- Paths ---------- */

        PathChain prepare = follower.pathBuilder()
                .addPath(new BezierLine(scorePoseCross, preparePose))
                .setLinearHeadingInterpolation(scorePoseCross.getHeading(), preparePose.getHeading())
                .build();

        PathChain intakeLoading1 = follower.pathBuilder()
                .addPath(new BezierLine(preparePose, intakeLoadingPose))
                .setLinearHeadingInterpolation(preparePose.getHeading(), intakeLoadingPose.getHeading())
                .build();

        PathChain intakeLoading2 = follower.pathBuilder()
                .addPath(new BezierLine(intakeLoadingPose, preparePose))
                .setLinearHeadingInterpolation(intakeLoadingPose.getHeading(), preparePose.getHeading())
                .build();

        PathChain intakeLoading3 = follower.pathBuilder()
                .addPath(new BezierLine(preparePose, intakeLoadingPose))
                .setLinearHeadingInterpolation(preparePose.getHeading(), intakeLoadingPose.getHeading())
                .build();

        PathChain score = follower.pathBuilder()
                .addPath(new BezierLine(intakeLoadingPose, scorePoseStraight))
                .setLinearHeadingInterpolation(intakeLoadingPose.getHeading(), scorePoseStraight.getHeading())
                .build();

        PathChain intakeInfinite1 = follower.pathBuilder()
                .addPath(new BezierLine(preparePose, intakeOtherPose))
                .setLinearHeadingInterpolation(preparePose.getHeading(), intakeOtherPose.getHeading())
                .build();

        PathChain intakeInfinite2 = follower.pathBuilder()
                .addPath(new BezierLine(intakeOtherPose, preparePose))
                .setLinearHeadingInterpolation(intakeOtherPose.getHeading(), preparePose.getHeading())
                .build();

        PathChain intakeInfinite3 = follower.pathBuilder()
                .addPath(new BezierLine(preparePose, intakeOtherPose))
                .setLinearHeadingInterpolation(preparePose.getHeading(), intakeOtherPose.getHeading())
                .build();

        PathChain park = follower.pathBuilder()
                .addPath(new BezierLine(scorePoseStraight, parkPose))
                .setLinearHeadingInterpolation(scorePoseStraight.getHeading(), parkPose.getHeading())
                .build();


        PathChain intakeCross1 = follower.pathBuilder()
                .addPath(new BezierLine(startPose, prepareCrossPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), prepareCrossPose.getHeading())
                .build();
        PathChain intakeCross2 = follower.pathBuilder()
                .addPath(new BezierLine(prepareCrossPose, intakeCrossPose))
                .setLinearHeadingInterpolation(prepareCrossPose.getHeading(), intakeCrossPose.getHeading())
                .build();

        PathChain scoreCrossPath = follower.pathBuilder()
                .addPath(new BezierLine(intakeCrossPose, scorePoseCross))
                .setLinearHeadingInterpolation(intakeCrossPose.getHeading(), scorePoseCross.getHeading())
                .build();


        /* ---------- Commands ---------- */

        SequentialCommandGroup preload = new SequentialCommandGroup(
                new InstantCommand(() -> intake.dntShoot()),
                autoCommand.accelFastPreload(),
                autoCommand.intakeAuto(intakeAutoCommand),
                autoCommand.shootFarPreload()
        );

        SequentialCommandGroup scoreFirst = new SequentialCommandGroup(
                new driveAutoCommand(follower, prepare),
                new InstantCommand(() -> follower.setMaxPower(0.9)),
                new driveAutoCommand(follower, intakeLoading1, 1800),
                new driveAutoCommand(follower, intakeLoading2, 200),
                new driveAutoCommand(follower, intakeLoading3, 1800),
                new InstantCommand(() -> follower.setMaxPower(0.75)),
                new driveAutoCommand(follower, score),
                autoCommand.shootFar()
        );

        SequentialCommandGroup scoreInfinite = new SequentialCommandGroup(
                new driveAutoCommand(follower, prepare),
                new InstantCommand(() -> follower.setMaxPower(1)),
                new driveAutoCommand(follower, intakeInfinite1, 1600),
                new driveAutoCommand(follower, intakeInfinite2, 100),
                new driveAutoCommand(follower, intakeInfinite3, 1600),
                new InstantCommand(() -> follower.setMaxPower(0.75)),
                new driveAutoCommand(follower, score),
                autoCommand.shootFar()
        );
        SequentialCommandGroup intakeLast = new SequentialCommandGroup(
                new driveAutoCommand(follower, prepare),
                new InstantCommand(() -> follower.setMaxPower(1)),
                new driveAutoCommand(follower, intakeInfinite1, 1600),
                new driveAutoCommand(follower, intakeInfinite2, 100),
                new driveAutoCommand(follower, intakeInfinite3, 1600)

        );


        SequentialCommandGroup scoreCross = new SequentialCommandGroup(
                new driveAutoCommand(follower, intakeCross1),
                new InstantCommand(() -> follower.setMaxPower(1)),
                new driveAutoCommand(follower, intakeCross2),

                new InstantCommand(() -> follower.setMaxPower(0.7)),
                new driveAutoCommand(follower, scoreCrossPath),
                autoCommand.shootFar()
        );




        return new SequentialCommandGroup(
                preload,
                scoreCross,
                scoreFirst,
                scoreInfinite,
                scoreInfinite,
                scoreInfinite,
                intakeLast,
                autoCommand.stopAll()
        );
    }

    @Override
    public Pose getStartPose() {
        return startPose;
    }
}
