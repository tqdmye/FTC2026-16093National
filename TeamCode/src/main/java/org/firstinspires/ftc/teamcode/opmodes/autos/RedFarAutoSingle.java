package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commands.autos.driveAutoCommand;

@Config
@Autonomous(name = "Auto Red Far Single")
public class RedFarAutoSingle extends AutoCommandBase {

    /* ================= Pose ================= */

    private final Pose startPose = new Pose(1.939, 51.423, Math.toRadians(-23));
    private final Pose scorePose = new Pose(1.939, 47.423, Math.toRadians(-30));
    private final Pose preparePose = new Pose(1, 47, Math.toRadians(-90));
    private final Pose intakeLoadingPose = new Pose(1, 1, Math.toRadians(-90));
    private final Pose intakeOtherPose = new Pose(1, 2, Math.toRadians(-90));

    private final Pose parkPose = new Pose(1, -15, Math.toRadians(-90));

    /* ================= 参数 ================= */

    public static double AUTO_TOTAL_TIME = 30.0;
    public static double PARK_REMAIN_TIME = 0;

    /* ================= Auto ================= */

    @Override
    public Command runAutoCommand() {

        /* ---------- Paths ---------- */

        PathChain prepare = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, preparePose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), preparePose.getHeading())
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
                .addPath(new BezierLine(intakeLoadingPose, scorePose))
                .setLinearHeadingInterpolation(intakeLoadingPose.getHeading(), scorePose.getHeading())
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
                .addPath(new BezierLine(scorePose, parkPose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), parkPose.getHeading())
                .build();

        /* ---------- Commands ---------- */

        SequentialCommandGroup preload = new SequentialCommandGroup(
                new InstantCommand(() -> intake.dntShoot()),
                autoCommand.accelFast(),
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

        /* ---------- Time Guard ---------- */

//        Command scoreOrPark = new ConditionalCommand(
//                scoreInfinite,
//                new SequentialCommandGroup(
//                        new InstantCommand(() -> follower.setMaxPower(1)),
//                        new driveAutoCommand(follower, park),
//                        autoCommand.stopAll()
//                ),
//                () -> (AUTO_TOTAL_TIME - getRuntime()) > PARK_REMAIN_TIME
//        );

        /* ---------- Final Auto ---------- */

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

                // 无限循环，但每次都会判断时间
//                scoreOrPark,
//                scoreOrPark,
//                scoreOrPark,
//                scoreOrPark,
//                scoreOrPark,
//                scoreOrPark,
//                scoreOrPark,
//                scoreOrPark,

                autoCommand.stopAll()
        );
    }

    @Override
    public Pose getStartPose() {
        return startPose;
    }
}
