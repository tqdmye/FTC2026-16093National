//package org.firstinspires.ftc.teamcode.subsystems.shooter;
//
//import org.firstinspires.ftc.teamcode.subsystems.ShooterConstants.ServoConstants;
//
//import java.util.EnumMap;
//
//public class ShooterPresetTable {
//
//    private static final EnumMap<Shootzone, ShooterPreset> table =
//            new EnumMap<>(Shootzone.class);
//
//    static {
//        table.put(Shootzone.CLOSE, new ShooterPreset(ShooterConstants.SHOOTER_SLOW_VELOCITY.value, ServoConstants.SHOOTER_TURRET_SLOW.value));
//        table.put(Shootzone.MID,   new ShooterPreset(ShooterConstants.SHOOTER_MID_VELOCITY.value, ServoConstants.SHOOTER_TURRET_MID.value));
//        table.put(Shootzone.FAR,   new ShooterPreset(ShooterConstants.SHOOTER_FAST_VELOCITY.value, ServoConstants.SHOOTER_TURRET_LONG.value));
//    }
//
//    public static ShooterPreset get(Shootzone zone) {
//        return table.get(zone);
//    }
//}
