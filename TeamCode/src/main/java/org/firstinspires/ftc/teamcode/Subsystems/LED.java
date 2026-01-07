package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class LED {
    public Servo indicatorLight;


    public LED(HardwareMap hardwareMap) {
        this.indicatorLight = hardwareMap.get(Servo.class, "indicatorLight");
    }

    public void setBlue() {
        indicatorLight.setPosition(0.6);
    }
    public void setRed() {
        indicatorLight.setPosition(0.3);
    }

    public void setWhite() {
        indicatorLight.setPosition(0.8);
    }

    public void setGreen() {
        indicatorLight.setPosition(0.5);
    }


    public void setNone(){
        indicatorLight.setPosition(0);
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