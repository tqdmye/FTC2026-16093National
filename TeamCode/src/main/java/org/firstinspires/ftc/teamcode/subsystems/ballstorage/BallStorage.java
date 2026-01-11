package org.firstinspires.ftc.teamcode.subsystems.ballstorage;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BallStorage {
    public Rev2mDistanceSensor intakeDistanceSensor;
    public RevColorSensorV3 intakeColorSensor;
    private final List<Float> hues = new ArrayList<>();

    private final Queue<Integer> colorQueue = new LinkedList<>();

    private double distance = intakeDistanceSensor.getDistance(DistanceUnit.CM);

    public boolean isBallInZone = false;

    private final float[] hsvValues = {0F, 0F, 0F};

    public BallStorage (HardwareMap hardwareMap){
        this.intakeColorSensor = hardwareMap.get(RevColorSensorV3.class,"intakeColorSensor");
        this.intakeDistanceSensor = hardwareMap.get(Rev2mDistanceSensor.class,"intakeDistanceSensor");
    }
    public void update(){
        double distance = intakeDistanceSensor.getDistance(DistanceUnit.CM);

        boolean isBallInZone = (distance <= SensorConstants.INZONE_DISTANCE.value);

        double r = intakeColorSensor.red();
        double g = intakeColorSensor.green();
        double b = intakeColorSensor.blue();

        boolean isGreen  = g > r && g > b && g > 120;
        boolean isPurple = (r + b) / 2 > g && r > 80 && b > 80;



    }
}
