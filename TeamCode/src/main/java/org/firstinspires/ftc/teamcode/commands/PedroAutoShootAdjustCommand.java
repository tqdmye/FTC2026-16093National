package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.Subsystems.Constants.ShootZone;
import org.firstinspires.ftc.teamcode.Subsystems.Constants.ShootZoneConstants;
import org.firstinspires.ftc.teamcode.Subsystems.Constants.ShooterPreset;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;

import java.util.EnumMap;
import java.util.function.BooleanSupplier;

public class PedroAutoShootAdjustCommand extends CommandBase {

    private final Shooter shooter;
    private final Follower follower;
    private final BooleanSupplier enabled;

    private ShootZone lastZone = null;

    private final EnumMap<ShootZone, ShooterPreset> presets =
            new EnumMap<>(ShootZone.class);

    public PedroAutoShootAdjustCommand(
            Shooter shooter,
            Follower follower,
            BooleanSupplier enabled
    ) {
        this.shooter = shooter;
        this.follower = follower;
        this.enabled = enabled;

        presets.put(ShootZone.CLOSE, new ShooterPreset(3200, 0.42));
        presets.put(ShootZone.MID,   new ShooterPreset(3800, 0.48));
        presets.put(ShootZone.FAR,   new ShooterPreset(4400, 0.55));
    }

    @Override
    public void execute() {

        if (!enabled.getAsBoolean()) {
            lastZone = null;
            return;
        }

        follower.update();
        Pose pose = follower.getPose();
        if (pose == null) return;

        double x = pose.getX();
        double y = pose.getY();

        ShootZone zone = ShootZoneConstants.getZone(x, y);
        if (zone == ShootZone.INVALID) return;

        if (zone != lastZone) {
            ShooterPreset preset = presets.get(zone);
            shooter.applyZone(zone);
            lastZone = zone;
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
