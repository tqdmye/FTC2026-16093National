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
@Autonomous(name = "Auto Red Far Cross")
public class RedFarAutoCross extends AutoCommandBase {

    /* ================= Pose ================= */

    private final Pose startPose = new Pose(1.939, 51.423, Math.toRadians(-23));
    private final Pose scorePose = new Pose(1.939, 44.423, Math.toRadians(-27));
    private final Pose preparePose = new Pose(3.5, 43, Math.toRadians(-90));
    private final Pose intakeLoadingPose = new Pose(1, 3, Math.toRadians(-90));
    private final Pose intakeOtherPose = new Pose(1, 4, Math.toRadians(-90));

    private final Pose intakeCrossPose1 = new Pose(21, 3.5, Math.toRadians(-85));
    private final Pose intakeCrossPose2 = new Pose(21, 5.5, Math.toRadians(-85));

    private final Pose parkPose = new Pose(1, 15, Math.toRadians(-90));

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

        PathChain beforeScore = follower.pathBuilder()
                .addPath(new BezierLine(intakeLoadingPose, preparePose))
                .setLinearHeadingInterpolation(intakeLoadingPose.getHeading(), preparePose.getHeading())
                .build();
        PathChain score = follower.pathBuilder()
                .addPath(new BezierLine(preparePose, scorePose))
                .setLinearHeadingInterpolation(preparePose.getHeading(), scorePose.getHeading())
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

        PathChain intakeCross1 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, intakeCrossPose1))
                .setLinearHeadingInterpolation(scorePose.getHeading(), intakeCrossPose1.getHeading())
                .build();

        PathChain intakeCross2 = follower.pathBuilder()
                .addPath(new BezierLine(intakeCrossPose1, intakeCrossPose2))
                .setLinearHeadingInterpolation(intakeCrossPose1.getHeading(), intakeCrossPose2.getHeading())
                .build();
        PathChain intakeCross3 = follower.pathBuilder()
                .addPath(new BezierLine(intakeCrossPose2, intakeCrossPose1))
                .setLinearHeadingInterpolation(intakeCrossPose2.getHeading(), intakeCrossPose1.getHeading())
                .build();

        PathChain scoreCross = follower.pathBuilder()
                .addPath(new BezierLine(intakeCrossPose1, scorePose))
                .setLinearHeadingInterpolation(intakeCrossPose1.getHeading(), scorePose.getHeading())
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
                new driveAutoCommand(follower, beforeScore, 16000),

                new InstantCommand(() -> follower.setMaxPower(0.75)),
                new driveAutoCommand(follower, score),
                autoCommand.shootFar()
        );
        SequentialCommandGroup scoreInfiniteCross = new SequentialCommandGroup(
                new driveAutoCommand(follower, intakeCross1),
                new InstantCommand(() -> follower.setMaxPower(1)),
                new driveAutoCommand(follower, intakeCross2, 1600),
                new driveAutoCommand(follower, intakeCross3, 1600),

                new InstantCommand(() -> follower.setMaxPower(0.75)),
                new driveAutoCommand(follower, scoreCross),
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
                scoreInfiniteCross,
                scoreInfinite,
                scoreInfiniteCross,
                scoreInfinite,
                scoreInfiniteCross,
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
