package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.util.LinkedList;
import java.util.Queue;

@TeleOp(name = "shooter_diffy")
public class Shooter extends LinearOpMode {

    private Limelight3A limelight;

    private static final int WINDOW_SIZE = 6;
    private final Queue<Double> txHistory = new LinkedList<>();
    private Double lastTx = null;

    private boolean aligned = false;

    private final double[] gaussianKernel =
            {0.06136, 0.24477, 0.38774, 0.24477, 0.06136};
    private double previousTxFiltered = 0;

    @Override
    public void runOpMode() {

        DcMotorEx shooterUp = hardwareMap.get(DcMotorEx.class, "shooterUp");
        DcMotorEx shooterDown = hardwareMap.get(DcMotorEx.class, "shooterDown");

        shooterUp.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        shooterDown.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        limelight = hardwareMap.get(Limelight3A.class, "camera1");
        limelight.pipelineSwitch(7);
        limelight.start();

        waitForStart();

        while (opModeIsActive()) {

            /* ==========================
               1. 计算逻辑轴命令
               ========================== */

            double yawCmd = 0.0;    // 云台转向
            double spinCmd = 0.0;   // shooter 转速

            LLResult result = limelight.getLatestResult();

            if (result != null && result.isValid()
                    && !result.getFiducialResults().isEmpty()) {

                LLResultTypes.FiducialResult tag =
                        result.getFiducialResults().get(0);

                double txRaw = tag.getTargetXDegrees();
                lastTx = txRaw;

                if (txHistory.size() >= WINDOW_SIZE) txHistory.poll();
                txHistory.add(txRaw);

                double txFiltered = applyGaussianFilter();
                txFiltered = applyLowPassFilter(txFiltered);

                double deadband = 7.0;
                double kP = 0.01;

                if (Math.abs(txFiltered) > deadband) {
                    yawCmd = kP * txFiltered;
                    yawCmd = clip(yawCmd, -0.15, 0.15);
                    aligned = false;
                } else {
                    yawCmd = 0;
                    aligned = true;
                }

                telemetry.addData("txRaw", txRaw);
                telemetry.addData("txFiltered", txFiltered);

            } else {
                // 丢失目标：按 lastTx 扫描
                aligned = false;
                double searchPower = 0.13;

                if (lastTx != null) {
                    yawCmd = lastTx > 0 ? searchPower : -searchPower;
                } else {
                    yawCmd = searchPower;
                }
            }

            /* ==========================
               2. 对准后开启 shooter
               ========================== */
            if (aligned) {
                spinCmd = 0.6;   // shooter 转速（功率制）
                yawCmd = 0;      // 锁死转向
            }

            /* ==========================
               3. diffy 映射（唯一出口）
               ========================== */
            double upPower   = yawCmd + spinCmd;
            double downPower = yawCmd - spinCmd;

            upPower   = clip(upPower,   -1.0, 1.0);
            downPower = clip(downPower, -1.0, 1.0);

            shooterUp.setPower(upPower);
            shooterDown.setPower(downPower);

            telemetry.addData("yawCmd", yawCmd);
            telemetry.addData("spinCmd", spinCmd);
            telemetry.addData("upPower", upPower);
            telemetry.addData("downPower", downPower);
            telemetry.addData("Aligned", aligned);

            telemetry.update();
            idle();
        }
    }

    /* ==========================
       滤波工具函数
       ========================== */

    private double applyGaussianFilter() {
        if (txHistory.isEmpty()) return 0.0;

        Double[] values = txHistory.toArray(new Double[0]);
        int size = values.length;
        double sum = 0;

        for (int i = 0; i < size; i++) {
            int k = gaussianKernel.length - size + i;
            if (k < 0) k = 0;
            sum += values[i] * gaussianKernel[k];
        }
        return sum;
    }

    private double applyLowPassFilter(double current) {
        double alpha = 0.1;
        double filtered = alpha * current + (1 - alpha) * previousTxFiltered;
        previousTxFiltered = filtered;
        return filtered;
    }

    private double clip(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}
