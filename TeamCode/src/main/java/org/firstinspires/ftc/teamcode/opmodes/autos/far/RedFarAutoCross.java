package org.firstinspires.ftc.teamcode.opmodes.autos.far;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commands.autos.driveAutoCommand;
import org.firstinspires.ftc.teamcode.opmodes.autos.AutoCommandBase;

@Config
@Autonomous(name = "Auto Red Far Cross")
public class RedFarAutoCross extends AutoCommandBase {

    /* ================= Pose ================= */

    private final Pose startPose = new Pose(1.939, 51.423, Math.toRadians(-23));
    private final Pose scorePoseStraight = new Pose(8, 40.423, Math.toRadians(-22));
    private final Pose scorePoseCross = new Pose(8, 40.423, Math.toRadians(-24));
    private final Pose prepareLoadingPose = new Pose(3, 47, Math.toRadians(-90));
    private final Pose intakeLoadingPose = new Pose(1, 3, Math.toRadians(-90));
    private final Pose prepareCrossPose = new Pose(30, 40, Math.toRadians(-85));
    private final Pose intakeCrossPose = new Pose(30, 3, Math.toRadians(-90));


    private final Pose parkPose = new Pose(1, 15, Math.toRadians(-90));

    /* ================= 参数 ================= */

    public static double AUTO_TOTAL_TIME = 30.0;
    public static double PARK_REMAIN_TIME = 2.5;

    /* ================= Auto ================= */

    @Override
    public Command runAutoCommand() {

        /* ---------- Commands ---------- */

        SequentialCommandGroup preload = new SequentialCommandGroup(
                new InstantCommand(() -> intake.dntShoot()),
                autoCommand.accelFastPreload(),
                autoCommand.intakeAuto(intakeAutoCommand),
                autoCommand.shootFarPreload()
        );

        SequentialCommandGroup scoreFirst = new SequentialCommandGroup(
                autoCommand.accelFast(),

                new driveAutoCommand(follower, buildPath(startPose, prepareLoadingPose)),
                new InstantCommand(() -> follower.setMaxPower(0.9)),
                new driveAutoCommand(follower, buildPath(prepareLoadingPose, intakeLoadingPose), 1800),
                new driveAutoCommand(follower, buildPath(intakeLoadingPose, prepareLoadingPose), 200),
                new driveAutoCommand(follower, buildPath(prepareLoadingPose, intakeLoadingPose), 1800),
                new InstantCommand(() -> follower.setMaxPower(0.75)),
                new driveAutoCommand(follower, buildPath(intakeLoadingPose, scorePoseStraight)),
                autoCommand.shootFar()
        );

        /* ---------- Final Auto ---------- */

        return new SequentialCommandGroup(
                preload,
                scoreFirst,

                // 1. 这里调用方法，生成一个新的 scoreCross 实例并加入队列
                getScoreCrossCommand(),
                getScoreStraightCommand(),
                // 2. 这里再次调用方法，生成又一个新的 scoreCross 实例并加入队列
                getScoreCrossCommand(),

                // 3. ConditionalCommand 内部也会调用 getScoreCrossCommand()，生成它自己专用的实例
                // 这样就避免了同一个 Command 对象被添加多次的问题
                new ConditionalCommand(
                        getScoreCrossCommand(), // 内部生成新实例
                        getIntakeLastCommand(), // 内部生成新实例
                        () -> (AUTO_TOTAL_TIME - getRuntime()) > PARK_REMAIN_TIME
                ),

                autoCommand.stopAll()
        );
    }

    /* ================= Helper Methods for Commands ================= */

    private SequentialCommandGroup getScoreCrossCommand() {
        return new SequentialCommandGroup(
                autoCommand.accelFast(),
                new driveAutoCommand(follower, buildPath(scorePoseStraight, prepareCrossPose)),
                new InstantCommand(() -> follower.setMaxPower(1)),
                new driveAutoCommand(follower, buildPath(prepareCrossPose, intakeCrossPose), 1600),
                new InstantCommand(() -> follower.setMaxPower(0.75)),
                new driveAutoCommand(follower, buildPath(intakeCrossPose, prepareCrossPose)),
                new driveAutoCommand(follower, buildPath(prepareCrossPose, scorePoseCross)),

                autoCommand.shootFar()
        );
    }

    private SequentialCommandGroup getScoreStraightCommand() {
        return new SequentialCommandGroup(

                new driveAutoCommand(follower, buildPath(scorePoseCross, prepareLoadingPose)),
                new InstantCommand(() -> follower.setMaxPower(1)),
                new driveAutoCommand(follower, buildPath(prepareLoadingPose, intakeLoadingPose), 1600),
                new driveAutoCommand(follower, buildPath(intakeLoadingPose, prepareLoadingPose), 200),
                new driveAutoCommand(follower, buildPath(prepareLoadingPose, intakeLoadingPose), 1600),
                new InstantCommand(() -> follower.setMaxPower(0.75)),
                new driveAutoCommand(follower, buildPath(intakeLoadingPose, scorePoseStraight)),
                autoCommand.shootFar()
        );
    }

    private SequentialCommandGroup getIntakeLastCommand() {
        return new SequentialCommandGroup(
                new driveAutoCommand(follower, buildPath(scorePoseStraight, prepareCrossPose)),
                new InstantCommand(() -> follower.setMaxPower(1)),
                new driveAutoCommand(follower, buildPath(prepareCrossPose, intakeCrossPose), 1600),
                new driveAutoCommand(follower, buildPath(intakeCrossPose, prepareCrossPose), 200),
                new driveAutoCommand(follower, buildPath(prepareCrossPose, intakeCrossPose), 1600)
        );
    }

    /* ================= Path Builder Methods ================= */

    private PathChain buildPath(Pose start, Pose end) {
        return follower.pathBuilder()
                .addPath(new BezierLine(start, end))
                .setLinearHeadingInterpolation(start.getHeading(), end.getHeading())
                .build();
    }

    @Override
    public Pose getStartPose() {
        return startPose;
    }
}
