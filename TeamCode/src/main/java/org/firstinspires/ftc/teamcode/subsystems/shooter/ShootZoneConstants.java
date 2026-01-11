package org.firstinspires.ftc.teamcode.subsystems.shooter;

public class ShootZoneConstants {

    // 如果以后要调，直接改这些数
    public static final double CLOSE_X = 15.0;

    public static final double FAR_X   = -20;




    public static Shootzone getZone(double x, double y) {
        if (x > CLOSE_X) {
            return Shootzone.CLOSE;
        } else if (x < CLOSE_X && x > FAR_X) {
            return Shootzone.MID;
        } else if (x < FAR_X ) {
            return Shootzone.FAR;
        }
        return Shootzone.INVALID;
    }

}
