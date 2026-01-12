package org.firstinspires.ftc.teamcode.subsystems.ballstorage;

import android.graphics.Color;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BallStorage {
    public Rev2mDistanceSensor intakeDistanceSensor;
    public RevColorSensorV3 intakeColorSensor;
    private final List<Float> hues = new ArrayList<>();

    private final Queue<Integer> colorQueue = new LinkedList<>();



    private boolean isBallDetected = false;

    private final float[] hsvValues = {0F, 0F, 0F};

    public BallStorage (HardwareMap hardwareMap){
        this.intakeColorSensor = hardwareMap.get(RevColorSensorV3.class,"intakeColorSensor");
        this.intakeDistanceSensor = hardwareMap.get(Rev2mDistanceSensor.class,"intakeDistanceSensor");
    }
    public void update(){
        double distance = intakeDistanceSensor.getDistance(DistanceUnit.CM);

        boolean isBallInZone = (distance <= SensorConstants.INZONE_DISTANCE.value);

        int r = intakeColorSensor.red();
        int g = intakeColorSensor.green();
        int b = intakeColorSensor.blue();
        Color.RGBToHSV(r, g, b, hsvValues);

        if (isBallInZone) {
            hues.add(hsvValues[0]);
            isBallDetected = true;
        }
        if (!isBallInZone && isBallDetected) {
            isBallDetected = false;

            if (!hues.isEmpty()) {
                Collections.sort(hues);
                float medianHue = hues.get(hues.size() / 2);

                if (medianHue >= SensorConstants.PURPLE_MIN_H.value &&
                        medianHue <= SensorConstants.PURPLE_MAX_H.value) {

                    colorQueue.offer(1);

                } else if (medianHue >= SensorConstants.GREEN_MIN_H.value &&
                        medianHue <= SensorConstants.GREEN_MAX_H.value) {

                    colorQueue.offer(0);
                }

                if (colorQueue.size() > 3) {
                    colorQueue.poll();
                }
            }

            hues.clear();
        }


    }
    public int getBallCount() {
        return colorQueue.size();
    }

    public boolean isFull() {
        return colorQueue.size() >= 3;
    }

    public Queue<Integer> getColorQueue() {
        return colorQueue;
    }
    public void reset() {
        colorQueue.clear();
        hues.clear();
        isBallDetected = false;
    }
}
