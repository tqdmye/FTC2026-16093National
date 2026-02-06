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
@Autonomous(name = "Auto Blue Far Cross")
public class BlueFarAutoCross extends AutoCommandBase {

    /* ================= Pose ================= */

    private final Pose startPose = new Pose(1.939, -51.423, Math.toRadians(23));
    private final Pose scorePoseStraight = new Pose(8, -40.423, Math.toRadians(28));
    private final Pose scorePoseStraightLoading = new Pose(8, -40.423, Math.toRadians(26));

    private final Pose scorePoseCross = new Pose(8, -40.423, Math.toRadians(30));
    private final Pose scorePoseCrossThird = new Pose(8, -40.423, Math.toRadians(30));

    private final Pose prepareLoadingPose = new Pose(6, -47, Math.toRadians(90));
    private final Pose intakeLoadingPose = new Pose(1, -5, Math.toRadians(90));
    private final Pose prepareThirdPose = new Pose(30, -40, Math.toRadians(90));
    private final Pose intakeThirdPose = new Pose(30, -9, Math.toRadians(90));
    private final Pose intakeCrossPose1 = new Pose(13, -6, Math.toRadians(45));
    private final Pose intakeCrossPose2 = new Pose(30, -6, Math.toRadians(45));


    private final Pose parkPose = new Pose(1, -15, Math.toRadians(90));

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

        SequentialCommandGroup scoreLoading = new SequentialCommandGroup(
                autoCommand.accelFast(),

                new driveAutoCommand(follower, buildPath(startPose, prepareLoadingPose)),
                new InstantCommand(() -> follower.setMaxPower(0.9)),
                new driveAutoCommand(follower, buildPath(prepareLoadingPose, intakeLoadingPose), 1800),
                new driveAutoCommand(follower, buildPath(intakeLoadingPose, prepareLoadingPose), 200),
                new driveAutoCommand(follower, buildPath(prepareLoadingPose, intakeLoadingPose), 1800),
                new InstantCommand(() -> follower.setMaxPower(0.75)),
                new driveAutoCommand(follower, buildPath(intakeLoadingPose, scorePoseStraightLoading)),
                autoCommand.shootFar()
        );

        SequentialCommandGroup scoreThird = new SequentialCommandGroup(
                new driveAutoCommand(follower, buildPath(scorePoseStraightLoading, prepareThirdPose)),
                new InstantCommand(() -> follower.setMaxPower(0.8)),
                new driveAutoCommand(follower, buildPath(prepareThirdPose, intakeThirdPose), 3000),
                new InstantCommand(()->follower.setMaxPower(0.75)),
                new driveAutoCommand(follower, buildPath(intakeThirdPose, scorePoseCross)),
                autoCommand.shootFar()
        );

        /* ---------- Final Auto ---------- */

        return new SequentialCommandGroup(
                preload,
                scoreLoading,

                scoreThird,

                getScoreStraightCommand(),
                // 2. 这里再次调用方法，生成又一个新的 scoreCross 实例并加入队列
                getScoreCrossCommand(),
                getScoreStraightCommand(),

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
                new InstantCommand(()->follower.setMaxPower(1)),
                new driveAutoCommand(follower, buildPath(scorePoseStraight, intakeCrossPose1), 1600),
                new driveAutoCommand(follower, buildPath(intakeCrossPose1, intakeCrossPose2), 1600),
                new InstantCommand(() -> follower.setMaxPower(0.75)),
                new driveAutoCommand(follower, buildPath(intakeCrossPose2, scorePoseCross)),
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
                new InstantCommand(()->follower.setMaxPower(1)),
                new driveAutoCommand(follower, buildPath(scorePoseStraight, intakeCrossPose1), 1600),
                new driveAutoCommand(follower, buildPath(intakeCrossPose1, intakeCrossPose2), 1600)
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
