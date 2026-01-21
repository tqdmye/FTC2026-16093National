package org.firstinspires.ftc.teamcode.commands;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Subsystems.shooter.Shootzone;
import org.firstinspires.ftc.teamcode.Subsystems.shooter.ShootZoneConstants;
import org.firstinspires.ftc.teamcode.Subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.Subsystems.driving.NewMecanumDrive;

import java.util.function.BooleanSupplier;

public class ShootAutoAdjustCommand extends CommandBase {

    private final Shooter shooter;
    private final NewMecanumDrive drive;
    private final BooleanSupplier isAutoShoot;
    private final BooleanSupplier isLimitOn;

    private Shootzone lastZone = null;

    public ShootAutoAdjustCommand(
            Shooter shooter,
            NewMecanumDrive drive,
            BooleanSupplier isAutoShoot, BooleanSupplier isLimitOn
    ) {
        this.shooter = shooter;
        this.drive = drive;
        this.isAutoShoot = isAutoShoot;
        this.isLimitOn = isLimitOn;
    }

    @Override
    public void execute() {

        if (!isAutoShoot.getAsBoolean()) return;

        Pose2d pose = drive.getPoseEstimate();
        Shootzone zone = ShootZoneConstants.getZone(pose.getX(), pose.getY());

        if( !isLimitOn.getAsBoolean()){
            shooter.applyZone(zone);
            lastZone = zone;
        }
        else {
            shooter.accelerate_idle();
        }

    }


    @Override
    public boolean isFinished() {
        return false;
    }
}
