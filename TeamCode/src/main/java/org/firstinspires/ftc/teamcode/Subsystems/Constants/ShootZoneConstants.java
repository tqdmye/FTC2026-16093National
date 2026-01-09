package org.firstinspires.ftc.teamcode.Subsystems.Constants;

public class ShootZoneConstants {

    // 如果以后要调，直接改这些数
    public static final double CLOSE_X = 15.0;
    public static final double MID_X   = -10;
    public static final double FAR_X   = -20;




    public static ShootZone getZone(double x, double y) {
        if (x > CLOSE_X) {
            return ShootZone.CLOSE;
        } else if (x < CLOSE_X && x > FAR_X) {
            return ShootZone.MID;
        } else if (x < FAR_X ) {
            return ShootZone.FAR;
        }
        return ShootZone.INVALID;
    }


//    public static ShootZone getZone(double x, double y) {
//
//        double dx = TARGET_X - x;
//        double dy = TARGET_Y - y;
//        double dist = Math.hypot(dx, dy); // 距离（inch）
//
//        if (dist < 24) {
//            return ShootZone.CLOSE;
//        } else if (dist < 48) {
//            return ShootZone.MID;
//        } else {
//            return ShootZone.FAR;
//        }
//    }

}
