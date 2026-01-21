package org.firstinspires.ftc.teamcode.Subsystems;

import android.graphics.Color;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class BallStorage {

    private final RevColorSensorV3 colorSensor;
    private final float[] hsv = new float[3];
    private final ElapsedTime timer = new ElapsedTime();

    private boolean wasInZone = false;     // 上一帧是否检测到球
    private boolean ballConfirmed = false; // 是否满

    private static final double THRESHOLD_MS = 500; // 停留/消失时间阈值

    private static final float GREEN_MIN_H = 150;
    private static final float GREEN_MAX_H = 170;
    private static final float GREEN_MIN_S = 0.6f;
    private static final float GREEN_MIN_V = 0.0f;

    private static final float PURPLE_MIN_H = 205;
    private static final float PURPLE_MAX_H = 240;
    private static final float PURPLE_MIN_S = 0.4f;

    public BallStorage(HardwareMap hardwareMap) {
        colorSensor = hardwareMap.get(RevColorSensorV3.class, "intakeColorSensor");
    }

    public void update() {
        int r = colorSensor.red();
        int g = colorSensor.green();
        int b = colorSensor.blue();

        Color.RGBToHSV(r, g, b, hsv);

        float H = hsv[0];
        float S = hsv[1];
        float V = hsv[2];

        // 判断是否有球：绿色或紫色球
        boolean isGreen = (H >= GREEN_MIN_H && H <= GREEN_MAX_H) &&
                (S >= GREEN_MIN_S && V >= GREEN_MIN_V);
        boolean isPurple = (H >= PURPLE_MIN_H && H <= PURPLE_MAX_H) &&
                (S >= PURPLE_MIN_S);

        boolean isInZone = isGreen || isPurple;

        if (isInZone) {
            if (!wasInZone) {
                // 刚进入检测区，重置计时器
                timer.reset();
            }

            // 如果球停留超过阈值，标记满
            if (!ballConfirmed && timer.milliseconds() >= THRESHOLD_MS) {
                ballConfirmed = true;
            }
        } else {
            if (wasInZone) {
                // 刚离开检测区，重置计时器
                timer.reset();
            }

            // 如果没有球超过阈值时间，标记不满
            if (ballConfirmed && timer.milliseconds() >= THRESHOLD_MS) {
                ballConfirmed = false;
            }
        }

        wasInZone = isInZone;
    }

    /** 是否满：球停留超过阈值且没消失超过阈值 */
    public boolean isFull() {
        return ballConfirmed;
    }

    /** 调试用 */
    public float getHue() { return hsv[0]; }
    public float getSaturation() { return hsv[1]; }
    public float getValue() { return hsv[2]; }
}
