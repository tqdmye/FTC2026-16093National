//package org.firstinspires.ftc.teamcode.commands;
//
//import com.arcrobotics.ftclib.command.CommandBase;
//import com.pedropathing.follower.Follower;
//import com.pedropathing.geometry.Pose;
//
//import org.firstinspires.ftc.teamcode.Subsystems.shooter.Shootzone;
//import org.firstinspires.ftc.teamcode.Subsystems.shooter.ShootZoneConstants;
//import org.firstinspires.ftc.teamcode.Subsystems.shooter.Shooter;
//
//import java.util.function.BooleanSupplier;
//
//public class PedroShootAutoAdjustCommand extends CommandBase {
//
//    private final Shooter shooter;
//    private final Follower follower;
//    private final BooleanSupplier isAutoShoot;
//    private final BooleanSupplier isLimitOn;
//
//
//    public PedroShootAutoAdjustCommand(
//            Shooter shooter,
//            Follower follower,
//            BooleanSupplier isAutoShoot, BooleanSupplier isLimitOn
//    ) {
//        this.shooter = shooter;
//        this.follower = follower;
//        this.isAutoShoot = isAutoShoot;
//        this.isLimitOn = isLimitOn;
//
//    }
//
//    @Override
//    public void execute() {
//
//        if (!isAutoShoot.getAsBoolean()) {
//            return;
//        }
//
//        follower.update();
//        Pose pose = follower.getPose();
//        if (pose == null) return;
//
//        double x = pose.getX();
//        double y = pose.getY();
//
//        Shootzone zone = ShootZoneConstants.getZone(x, y);
//        if (zone == Shootzone.INVALID) return;
//
//        if( !isLimitOn.getAsBoolean()){
//            shooter.applyZone(zone);
//        }
//        else {
//            shooter.accelerate_idle();
//        }
//    }
//
//    @Override
//    public boolean isFinished() {
//        return false;
//    }
//}
