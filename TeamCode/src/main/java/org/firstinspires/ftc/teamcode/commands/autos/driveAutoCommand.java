package org.firstinspires.ftc.teamcode.commands.autos;

import com.arcrobotics.ftclib.command.CommandBase;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.util.ElapsedTime;

public class driveAutoCommand extends CommandBase {
    private Follower follower;
    private PathChain pathChain;
    private double waitTime;
    private ElapsedTime timer;
    private PathChain nextPath;

    public driveAutoCommand(Follower follower, PathChain pathChain) {
        this.follower = follower;
        this.pathChain = pathChain;
        this.waitTime = 30 * 1000;
        this.timer = new ElapsedTime();
        nextPath = null;
    }

    public driveAutoCommand(Follower follower, PathChain pathChain, double waitTime, PathChain nextPath) {
        this.follower = follower;
        this.pathChain = pathChain;
        this.waitTime = waitTime;
        this.timer = new ElapsedTime();
        this.nextPath = nextPath;
    }

    @Override
    public void initialize() {
        timer.reset();
        follower.followPath(pathChain);
    }

    @Override
    public void execute() {
        follower.update();
    }

    @Override
    public void end(boolean interrupted) {
        follower.breakFollowing();
    }

    @Override
    public boolean isFinished() {
        if (timer.milliseconds() >= waitTime && nextPath != null) {
            nextPath = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(follower.getPose(), nextPath.endPose())
                    )
                    .setLinearHeadingInterpolation(follower.getHeading(), nextPath.endPose().getHeading())
                    .build();
        }
        return !follower.isBusy() || timer.milliseconds() >= waitTime;
    }
}