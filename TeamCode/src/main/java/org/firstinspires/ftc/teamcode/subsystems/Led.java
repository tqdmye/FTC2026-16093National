package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Led {
    public Servo velocityIndicator;


    public Led(HardwareMap hardwareMap) {
        this.velocityIndicator = hardwareMap.get(Servo.class, "indicatorLight");
    }

    public void setBlue() {
        velocityIndicator.setPosition(0.6);
    }
    public void setYellow() {
        velocityIndicator.setPosition(0.4);
    }


    public void setGreen() {
        velocityIndicator.setPosition(0.5);
    }
    public void setRed() {
        velocityIndicator.setPosition(0.3);
    }
    public void setPurple() {
        velocityIndicator.setPosition(0.7);
    }



    public void setNone(){
        velocityIndicator.setPosition(0);
    }

    public void isAtVelocity() {
        velocityIndicator.setPosition(0.6);
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