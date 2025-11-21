package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.commands.IntakeCommand;
import org.firstinspires.ftc.teamcode.commands.ShooterCommand;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDrive;
import org.firstinspires.ftc.teamcode.commands.MecanumDriveCommand;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

@TeleOp(name = "TeleOpDrive", group = "Main")
public class TeleOpDrive extends CommandOpMode {

    private MecanumDrive mecanumDrive;
    private MecanumDriveCommand mecanumDriveCommand;
    private Intake intake;
    private IntakeCommand intakeCommand;
    private Shooter shooter;
    private ShooterCommand ShooterCommand;
    private GamepadEx Gamepad1,Gamepad2;

    @Override
    public void initialize() {

        CommandScheduler.getInstance().reset();


        mecanumDrive = new MecanumDrive(telemetry, hardwareMap);
        intake = new Intake(hardwareMap);


        Gamepad1 = new GamepadEx(gamepad1);
        Gamepad2 = new GamepadEx(gamepad2);

        //
        mecanumDriveCommand = new MecanumDriveCommand(mecanumDrive, Gamepad1);
        intakeCommand = new IntakeCommand(intake, Gamepad2);
        ShooterCommand = new ShooterCommand(shooter, Gamepad2);



        mecanumDrive.setDefaultCommand(mecanumDriveCommand);
        intake.setDefaultCommand(intakeCommand);
        shooter.setDefaultCommand(ShooterCommand);


        telemetry.addLine("Initialized TeleOpDrive");
        telemetry.update();
    }
}
