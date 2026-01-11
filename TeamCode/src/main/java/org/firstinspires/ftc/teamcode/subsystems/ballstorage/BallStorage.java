package org.firstinspires.ftc.teamcode.subsystems.ballstorage;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.ArrayList;

public class BallStorage {
    public Rev2mDistanceSensor intakeDistanceSensor;
    public RevColorSensorV3 intakeColorSensor;
    public ArrayList<Integer> colorRecord = new ArrayList<>();

    private double distance = intakeDistanceSensor.getDistance(DistanceUnit.CM);

    public boolean isBallInZone = false;

    public BallStorage (HardwareMap hardwareMap){
        this.intakeColorSensor = hardwareMap.get(RevColorSensorV3.class,"intakeColorSensor");
        this.intakeDistanceSensor = hardwareMap.get(Rev2mDistanceSensor.class,"intakeDistanceSensor");
    }
    public void setBallInZone(){

    }

}
