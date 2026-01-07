/*
 * Author: Yunkai Li
 * Date: 2025/07/18
 * Intro: A wrapper of pedro pathing follower. Add tolerances to avoid swinging near the end point.
 * Please DO NOT delete this comment! Be respectful to the copyright!!!
 * */

package org.firstinspires.ftc.teamcode.utils;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.pathgen.BezierPoint;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.Arrays;
import java.util.List;

public class FollowerEx extends Follower {

    private double xTolerance, yTolerance, headingTolerance;
    private final List<DcMotorEx> motors;
    private Point endPoint;
    private double endHeading;
    public boolean isFinished = true;

    public FollowerEx(HardwareMap hardwareMap, Class<?> FConstants, Class<?> LConstants) {
        super(hardwareMap, FConstants, LConstants);
        DcMotorEx leftFront, leftRear, rightRear, rightFront;
        leftFront = hardwareMap.get(DcMotorEx.class, FollowerConstants.leftFrontMotorName);
        leftRear = hardwareMap.get(DcMotorEx.class, FollowerConstants.leftRearMotorName);
        rightRear = hardwareMap.get(DcMotorEx.class, FollowerConstants.rightRearMotorName);
        rightFront = hardwareMap.get(DcMotorEx.class, FollowerConstants.rightFrontMotorName);
        this.motors = Arrays.asList(leftFront, leftRear, rightFront, rightRear);//That is not a good solution
        // , but it is the only solution think of.
    }

    public void follow(PathChain pathChain, double xTolerance, double yTolerance, double headingTolerance) {
        isFinished = false;

        super.setMaxPower(1);
        super.followPath(pathChain);
        this.xTolerance = xTolerance;
        this.yTolerance = yTolerance;
        this.headingTolerance = headingTolerance;
        Path lastPath = pathChain.getPath(pathChain.size()-1);
        endHeading = lastPath.getHeadingGoal(pathChain.size()-1);
        endPoint = lastPath.getPoint(lastPath.length());
    }

    @Override
    public void update(){
        super.update();
        if(!isFinished && Math.abs(super.getHeadingError()) < headingTolerance && super.atPoint(endPoint, xTolerance, yTolerance)) {
            isFinished = true;
            breakFollowing();
            holdPoint(new BezierPoint(endPoint), endHeading);
            super.setMaxPower(0.3);
            for (DcMotorEx motor : motors){
                motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);//Stop following and brake when nearing the end point.
            }
        }
    }

    public void forceStop(){
        super.breakFollowing();
        for (DcMotorEx motor : motors){
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);//Stop following and brake when nearing the end point.
        }
    }

    public void setSlowPID(){
        FollowerConstants.translationalPIDFCoefficients.setCoefficients(0.008, 0, 0.00005, 0.002);

        FollowerConstants.headingPIDFCoefficients.setCoefficients(0.04, 0, 0.001, 0.01);
        FollowerConstants.useSecondaryHeadingPID = true;
        FollowerConstants.secondaryHeadingPIDFCoefficients.setCoefficients(0.25, 0, 0, 0.05);

        FollowerConstants.drivePIDFCoefficients.setCoefficients(0.1,0,0.0005,0.06,0.0019);
        FollowerConstants.useSecondaryDrivePID = true;
        FollowerConstants.secondaryDrivePIDFCoefficients.setCoefficients(0.001,0,0.00008,0,0);
    }

    public void setFastPID(){
        FollowerConstants.translationalPIDFCoefficients.setCoefficients(0.08, 0, 0.0005, 0.02);

        FollowerConstants.headingPIDFCoefficients.setCoefficients(0.4, 0, 0.01, 0.1);
        FollowerConstants.useSecondaryHeadingPID = true;
        FollowerConstants.secondaryHeadingPIDFCoefficients.setCoefficients(2.5, 0, 0, 0.5);

        FollowerConstants.drivePIDFCoefficients.setCoefficients(1,0,0.005,0.6,0.019);
        FollowerConstants.useSecondaryDrivePID = true;
        FollowerConstants.secondaryDrivePIDFCoefficients.setCoefficients(0.01,0,0.0008,0,0);
    }

}