//package org.firstinspires.ftc.teamcode.commands;
//
//import com.arcrobotics.ftclib.command.CommandBase;
//import com.arcrobotics.ftclib.command.Subsystem;
//import com.arcrobotics.ftclib.controller.PIDController;
//import com.pedropathing.follower.Follower;
//
//import org.firstinspires.ftc.teamcode.Subsystems.Vision;
//
//public class AutoAimCommand extends CommandBase {
//
//    private final Follower follower;
//    private final Vision vision;
//
//    private final int targetTag = 24;
//
//    // PID 参数（要调）
//    private final PIDController xPID = new PIDController(1.0, 0, 0);
//    private final PIDController yPID = new PIDController(1.0, 0, 0);
//    private final PIDController turnPID = new PIDController(0.02, 0, 0);
//    public boolean isDectected = false;
//
//    public AutoAimCommand(Follower follower, Vision vision) {
//        this.follower = follower;
//        this.vision = vision;
//    }
//
//    @Override
//    public void initialize() {
//        // 防止 schedule 时 follower 还未进入 teleop drive 模式导致内部 Pose 为空
//        follower.startTeleopDrive(true);
//        follower.update();
//    }
//
//    @Override
//    public void execute() {
//        // 再兜底一次：Pedro 内部 pose 未准备好时避免直接进 setTeleOpDrive 引发空指针
//        if (follower.getPose() == null) {
//            follower.update();
//            follower.setTeleOpDrive(0, 0, 0);
//            return;
//        }
//        if (!vision.hasTarget(targetTag)) {
//            follower.setTeleOpDrive(0,0,0);
//            return;
//        }
//        else{
//            isDectected = true;
//        }
//
//        double forwardError = vision.getForwardError(targetTag) - 1.20;
//        double strafeError  = vision.getStrafeError(targetTag);
//        double yawError     = vision.getYawError(targetTag);
//
//        double forward = xPID.calculate(forwardError, 0);
//        double strafe  = yPID.calculate(strafeError, 0);
//        double turn    = turnPID.calculate(yawError, 0);
//
//        follower.setTeleOpDrive(
//                strafe,
//                forward,
//                turn
//        );
//
//    }
//
//    @Override
//    public boolean isFinished() {
//        return Math.abs(vision.getForwardError(targetTag) - 1.20) < 0.03 &&
//                Math.abs(vision.getStrafeError(targetTag)) < 0.03 &&
//                Math.abs(vision.getYawError(targetTag)) < 2.0;
//    }
//
//    @Override
//    public void end(boolean interrupted) {
//        follower.setTeleOpDrive(0,0,0);
//        xPID.reset();
//        yPID.reset();
//        turnPID.reset();
//
//    }
//}
