package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Led {
    public Servo indicatorLight, indicatorLight2;


    public Led(HardwareMap hardwareMap) {
        this.indicatorLight = hardwareMap.get(Servo.class, "indicatorLight");
        this.indicatorLight2 = hardwareMap.get(Servo.class,"indicatorLight2");
    }

    public void setBlue() {
        indicatorLight.setPosition(0.6);
    }

    public void setGreen() {
        indicatorLight.setPosition(0.5);
    }

    public void setNone(){
        indicatorLight.setPosition(0);
    }

    public void setBallFull(){
        indicatorLight2.setPosition(0.5);
    }

    public void resetBall(){
        indicatorLight2.setPosition(0);
    }



    public enum ledColor{

        NULL(0.2),
        RED(0.3),
        YELLOW(0.4),
        GREEN(0.5),
        BLUE(0.6),
        PURPLE(0.7),
        WHITE(0.8);

        ledColor(double colorpose) {
        }
    }



}