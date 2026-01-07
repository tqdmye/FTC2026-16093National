package org.firstinspires.ftc.teamcode.opmodes;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.Subsystems.driving.NewMecanumDrive;
import org.firstinspires.ftc.teamcode.commands.TeleOpDriveCommand;
import org.firstinspires.ftc.teamcode.utils.ButtonEx;


@TeleOp(group = "0-competition", name = "TeleOp Dual")
public class TeleOpDual extends CommandOpModeEx {
    GamepadEx gamepadEx1, gamepadEx2;
    NewMecanumDrive driveCore;
    Shooter shooter;
    Intake intake;
    private boolean isFieldCentric=false;


    @Override
    public void initialize() {
        CommandScheduler.getInstance().cancelAll();
        this.telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        gamepadEx1 = new GamepadEx(gamepad1);
        gamepadEx2 = new GamepadEx(gamepad2);

        driveCore = new NewMecanumDrive(hardwareMap);
        driveCore.init();
        TeleOpDriveCommand driveCommand = new TeleOpDriveCommand(driveCore,
                ()->gamepadEx1.getLeftX(),
                ()->gamepadEx1.getLeftY(),
                ()->gamepadEx1.getRightX(),
                ()->(gamepadEx1.getButton(GamepadKeys.Button.START) && !gamepad1.touchpad),
                ()->(gamepadEx1.getButton(GamepadKeys.Button.RIGHT_BUMPER)),
                ()->(isFieldCentric));

        intake = new Intake(hardwareMap);
//        frontArm.setLED(false);
        shooter = new Shooter(hardwareMap);


        driveCore.resetHeading();
//        driveCore.yawHeading += 90; //如果specimen自动接solo手动就把这行去掉
//        driveCore.yawHeading %= 360;    //如果specimen自动接solo手动就把这行去掉
        driveCore.resetOdo();
        driveCore.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        CommandScheduler.getInstance().schedule(driveCommand);

        //timers
        new ButtonEx(()->getRuntime()>30).whenPressed(()->gamepad1.rumble(500));
        new ButtonEx(()->getRuntime()>60).whenPressed(()->gamepad1.rumble(500));
        new ButtonEx(()->getRuntime()>110).whenPressed(()->gamepad1.rumble(1000));

    }

    @Override
    public void onStart() {
        resetRuntime();
        shooter.accelerate_slow();
    }

    @Override
    public void functionalButtons() {

        //leftBumper -- intake
        //rightTrigger -- Shooter
        //leftTrigger -- preShooter
        //a -- preShooter & intake 反转

        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.BACK))
                .whenPressed(new InstantCommand(()->isFieldCentric=!isFieldCentric));

        new ButtonEx(()->gamepadEx2.getButton(GamepadKeys.Button.LEFT_BUMPER))
                .whenPressed(new InstantCommand(()->intake.intake()))
                .whenReleased(new InstantCommand(()->intake.init()));

        new ButtonEx(()->gamepadEx2.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)>0.5)
                .whenPressed(new InstantCommand(()->shooter.accelerate_mid()))
                .whenReleased(new InstantCommand(()->shooter.accelerate_slow()));

        new ButtonEx(()->gamepadEx2.getButton(GamepadKeys.Button.Y))
                .whenPressed(new InstantCommand(()->shooter.accelerate_slow()))
                .whenReleased(new InstantCommand(()->shooter.accelerate_slow()));

        new ButtonEx(()->Math.abs(gamepadEx2.getRightY())>0.7)
                .whenPressed(new InstantCommand(()->shooter.accelerate_fast()))
                .whenReleased(new InstantCommand(()->shooter.accelerate_slow()));

        new ButtonEx(()->gamepadEx2.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)>0.5)
                .whenPressed(new InstantCommand(()->shooter.shoot()))
                .whenReleased(new InstantCommand(()->shooter.init()));

        new ButtonEx(()->gamepadEx2.getButton(GamepadKeys.Button.A))
                .whenPressed(new ParallelCommandGroup(
                        new InstantCommand(()->intake.outtake()),
                        new InstantCommand(()->shooter.outtake())))
                .whenReleased(new ParallelCommandGroup(
                        new InstantCommand(()->intake.init()),
                        new InstantCommand(()->shooter.init())));

        new ButtonEx(()->gamepadEx2.getButton(GamepadKeys.Button.DPAD_DOWN))
                .whenPressed(new InstantCommand(()->shooter.emergency()))
                .whenReleased(new InstantCommand(()->shooter.stopAccelerate()));

        new ButtonEx(()->gamepadEx2.getButton(GamepadKeys.Button.DPAD_UP))
                .whenPressed(new InstantCommand(()->shooter.stopAccelerate()));

    }

    @Override
    public void run(){
        CommandScheduler.getInstance().run();

        telemetry.addData("shooter velocity", shooter.shooterDown.getVelocity());
        telemetry.addData("Heading", Math.toDegrees(driveCore.getHeading()));
        if(isFieldCentric) telemetry.addLine("Field Centric");
        else telemetry.addLine("Robot Centric");
        telemetry.update();
        telemetry.update();
    }
}