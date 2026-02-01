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
    private final BooleanSupplier isSlowMode;

    public TeleOpDriveCommand(
            NewMecanumDrive drive,
            DoubleSupplier x,
            DoubleSupplier y,
            DoubleSupplier rotate,
            BooleanSupplier isSlowMode) {
        this.drive = drive;
        this.x = x;
        this.rotate = rotate;
        this.y = y;
        this.isSlowMode = isSlowMode;
    }

    @Override
    public void execute() {

        if(isSlowMode.getAsBoolean()){
            drive.setBotCentric(x.getAsDouble(),y.getAsDouble(),rotate.getAsDouble(), 0.6);
        }
        else{
            drive.setBotCentric(x.getAsDouble(),y.getAsDouble(),rotate.getAsDouble(), 1);
        }

        drive.update();
    }
}
