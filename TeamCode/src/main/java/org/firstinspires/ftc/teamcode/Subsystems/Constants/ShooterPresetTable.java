package org.firstinspires.ftc.teamcode.Subsystems.Constants;

import java.util.EnumMap;

public class ShooterPresetTable {

    private static final EnumMap<ShootZone, ShooterPreset> table =
            new EnumMap<>(ShootZone.class);

    static {
        table.put(ShootZone.CLOSE, new ShooterPreset(MotorConstants.SHOOTER_SLOW_VELOCITY.value, ServoConstants.SHOOTER_TURRET_SLOW.value));
        table.put(ShootZone.MID,   new ShooterPreset(MotorConstants.SHOOTER_MID_VELOCITY.value, ServoConstants.SHOOTER_TURRET_MID.value));
        table.put(ShootZone.FAR,   new ShooterPreset(MotorConstants.SHOOTER_FAST_VELOCITY.value, ServoConstants.SHOOTER_TURRET_LONG.value));
    }

    public static ShooterPreset get(ShootZone zone) {
        return table.get(zone);
    }
}
