//package org.firstinspires.ftc.teamcode.commands;
//
//
//import com.arcrobotics.ftclib.command.CommandBase;
//import com.pedropathing.follower.Follower;
//
//import java.util.function.BooleanSupplier;
//import java.util.function.DoubleSupplier;
//
//public class PPTeleOpDriveCommand extends CommandBase {
//    private final Follower follower;
//    private final DoubleSupplier x;
//    private final DoubleSupplier rotate;
//    private final DoubleSupplier y;
//    private final BooleanSupplier shouldReset;
//    private final BooleanSupplier isSlowMode;
//    private final BooleanSupplier isRobotCentric;
//
//    private double slowModeMultiplier;
//
//    public PPTeleOpDriveCommand(
//            Follower follower,
//            DoubleSupplier x,
//            DoubleSupplier y,
//            DoubleSupplier rotate,
//            BooleanSupplier shouldReset,
//            BooleanSupplier isSlowMode,
//            BooleanSupplier isRobotCentric) {
//        this.follower = follower;
//        this.x = x;
//        this.rotate = rotate;
//        this.y = y;
//        this.shouldReset = shouldReset;
//        this.isSlowMode = isSlowMode;
//        this.isRobotCentric = isRobotCentric;
//    }
//
//    @Override
//    public void execute() {
//        if(isSlowMode.getAsBoolean()) slowModeMultiplier = 0.6;
//        else slowModeMultiplier = 1.0;
//
//        follower.setTeleOpDrive(
//                y.getAsDouble() * slowModeMultiplier,
//                x.getAsDouble() * slowModeMultiplier,
//                rotate.getAsDouble() * slowModeMultiplier,
//                isRobotCentric.getAsBoolean());
//        follower.update();
//    }
//}
