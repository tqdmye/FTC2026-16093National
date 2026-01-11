package org.firstinspires.ftc.teamcode.subsystems.shooter;

import org.firstinspires.ftc.teamcode.subsystems.Constants.Motorconstants;
import org.firstinspires.ftc.teamcode.subsystems.Constants.Servoconstants;

import java.util.EnumMap;

public class ShooterPresetTable {

    private static final EnumMap<Shootzone, ShooterPreset> table =
            new EnumMap<>(Shootzone.class);

    static {
        table.put(Shootzone.CLOSE, new ShooterPreset(Motorconstants.SHOOTER_SLOW_VELOCITY.value, Servoconstants.SHOOTER_TURRET_SLOW.value));
        table.put(Shootzone.MID,   new ShooterPreset(Motorconstants.SHOOTER_MID_VELOCITY.value, Servoconstants.SHOOTER_TURRET_MID.value));
        table.put(Shootzone.FAR,   new ShooterPreset(Motorconstants.SHOOTER_FAST_VELOCITY.value, Servoconstants.SHOOTER_TURRET_LONG.value));
    }

    public static ShooterPreset get(Shootzone zone) {
        return table.get(zone);
    }
}
