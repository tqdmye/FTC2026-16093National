package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "benz")
public class benz extends LinearOpMode {



    @Override
    public void runOpMode() throws InterruptedException {



        DcMotorEx benzMotor = hardwareMap.get(DcMotorEx.class, "benzMotor");









        waitForStart();

        // 记录benzMotor的初始角度
        int initialAngle = benzMotor.getCurrentPosition();
        telemetry.addData("initialAngle", initialAngle);
        telemetry.update();


        while (opModeIsActive()) {


            benzMotor.setPower(0.1);

            // 获取IMU角度并计算旋转的角度差值
            int currentAngle = benzMotor.getCurrentPosition();
            telemetry.addData("currentAngle", currentAngle);
            telemetry.update();
            int angleDifference = currentAngle - initialAngle;
            telemetry.addData("Angle Difference", angleDifference);
            telemetry.update();


            // 如果旋转了180°（可以设置一个小的容差值，例如 2°）
            if (Math.abs(angleDifference) >= 180) {
                benzMotor.setPower(0);  // 停止电机
            }


        }
    }
}
