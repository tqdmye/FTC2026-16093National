package org.firstinspires.ftc.teamcode.commands;


import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Subsystems.driving.NewMecanumDrive;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class TeleOpDriveCommandVision extends CommandBase {
    private final NewMecanumDrive drive;
    private final DoubleSupplier x;
    private final DoubleSupplier rotate;
    private final DoubleSupplier y;
    private final BooleanSupplier shouldReset;
    private final BooleanSupplier isSlowMode;
    private final BooleanSupplier isFieldCentric;
    // 当视觉/其它逻辑接管底盘时，用于让本命令“不动手”，避免多处同时写电机功率导致抖动
    private final BooleanSupplier isVisionDriving;

    public TeleOpDriveCommandVision(
            NewMecanumDrive drive,
            DoubleSupplier x,
            DoubleSupplier y,
            DoubleSupplier rotate,
            BooleanSupplier shouldReset,
            BooleanSupplier isSlowMode,
            BooleanSupplier isFieldCentric,
            BooleanSupplier isVisionDriving) {
        this.drive = drive;
        this.x = x;
        this.rotate = rotate;
        this.y = y;
        this.shouldReset = shouldReset;
        this.isSlowMode = isSlowMode;
        this.isFieldCentric = isFieldCentric;
        this.isVisionDriving = isVisionDriving;
    }

    @Override
    public void execute() {
        // 视觉在接管驱动时，本命令直接退出，不再改电机功率，避免抖动
        if (isVisionDriving != null && isVisionDriving.getAsBoolean()) {
            return;
        }
        if (shouldReset.getAsBoolean()){
            drive.resetHeading();
            drive.resetOdo();
        }
        if (isFieldCentric.getAsBoolean()) {
            if(isSlowMode.getAsBoolean()){
                drive.setFieldCentric(x.getAsDouble(),y.getAsDouble(),rotate.getAsDouble(), 0.5);
            }
            else{
                drive.setFieldCentric(x.getAsDouble(),y.getAsDouble(),rotate.getAsDouble(), 1);
            }
            drive.update();
            drive.updateOdo();
        } else {
            if(isSlowMode.getAsBoolean()){
                drive.setBotCentric(x.getAsDouble(),y.getAsDouble(),rotate.getAsDouble(), 0.6);
            }
            else{
                drive.setBotCentric(x.getAsDouble(),y.getAsDouble(),rotate.getAsDouble(), 1);
            }
        }
        drive.update();
    }
}
