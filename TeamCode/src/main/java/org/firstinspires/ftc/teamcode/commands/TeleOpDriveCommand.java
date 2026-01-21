package org.firstinspires.ftc.teamcode.commands;


import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Subsystems.driving.NewMecanumDrive;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class TeleOpDriveCommand extends CommandBase {
    private final NewMecanumDrive drive;
    private final DoubleSupplier x;
    private final DoubleSupplier rotate;
    private final DoubleSupplier y;
    private final BooleanSupplier shouldReset;
    private final BooleanSupplier isSlowMode;
    private final BooleanSupplier isFieldCentric;

    public TeleOpDriveCommand(
            NewMecanumDrive drive,
            DoubleSupplier x,
            DoubleSupplier y,
            DoubleSupplier rotate,
            BooleanSupplier shouldReset,
            BooleanSupplier isSlowMode,
            BooleanSupplier isFieldCentric) {
        this.drive = drive;
        this.x = x;
        this.rotate = rotate;
        this.y = y;
        this.shouldReset = shouldReset;
        this.isSlowMode = isSlowMode;
//        this.isFieldCentric = isFieldCentric;
        this.isFieldCentric = isFieldCentric;
    }

    @Override
    public void execute() {
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
