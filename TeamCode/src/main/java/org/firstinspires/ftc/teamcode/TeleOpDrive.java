package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.commands.IntakeCommand;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDrive;
import org.firstinspires.ftc.teamcode.commands.MecanumDriveCommand;

@TeleOp(name = "TeleOpDrive", group = "Main")
public class TeleOpDrive extends CommandOpMode {

    private MecanumDrive mecanumDrive;
    private MecanumDriveCommand mecanumDriveCommand;
    private Intake intake;
    private IntakeCommand intakeCommand;
    private GamepadEx driverGamepad;

    @Override
    public void initialize() {

        CommandScheduler.getInstance().reset();


        mecanumDrive = new MecanumDrive(telemetry, hardwareMap);
        intake = new Intake(hardwareMap);


        driverGamepad = new GamepadEx(gamepad1);

        //
        mecanumDriveCommand = new MecanumDriveCommand(mecanumDrive, driverGamepad);
        intakeCommand = new IntakeCommand(intake, driverGamepad);


        mecanumDrive.setDefaultCommand(mecanumDriveCommand);
        intake.setDefaultCommand(intakeCommand);

        telemetry.addLine("Initialized TeleOpDrive");
        telemetry.update();
    }
}
