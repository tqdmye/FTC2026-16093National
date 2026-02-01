package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.commands.TeleOpDriveCommand;
import org.firstinspires.ftc.teamcode.commands.RobotFSMCommand;
import org.firstinspires.ftc.teamcode.Subsystems.intakePreShooterFSM ;
import org.firstinspires.ftc.teamcode.Subsystems.Led;
import org.firstinspires.ftc.teamcode.Subsystems.driving.NewMecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystems.shooter.ShooterFSM;
import org.firstinspires.ftc.teamcode.utils.ButtonEx;

@TeleOp(group = "0-competition", name = "TeleOp Solo")
public class TeleOpSolo extends CommandOpModeEx {

    private GamepadEx gamepadEx1;

    private NewMecanumDrive driveCore;

    private ShooterFSM shooterFSM;
    private intakePreShooterFSM intakePreShooterFSM;
    private Led led;
    private RobotFSMCommand robotFSMCommand;


    @Override
    public void initialize() {

        CommandScheduler.getInstance().cancelAll();
        telemetry = new MultipleTelemetry(
                telemetry,
                FtcDashboard.getInstance().getTelemetry()
        );

        gamepadEx1 = new GamepadEx(gamepad1);

        /* ---------- Subsystems ---------- */
        driveCore = new NewMecanumDrive(hardwareMap);
        shooterFSM = new ShooterFSM(hardwareMap);
        intakePreShooterFSM = new intakePreShooterFSM (hardwareMap);
        led = new Led(hardwareMap);

        driveCore.init();
        driveCore.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveCore.resetHeading();
        driveCore.resetOdo();

        /* ---------- Drive Command ---------- */
        TeleOpDriveCommand driveCommand = new TeleOpDriveCommand(
                driveCore,
                () -> gamepadEx1.getLeftX(),
                () -> gamepadEx1.getLeftY(),
                () -> gamepadEx1.getRightX(),
                () -> gamepadEx1.getButton(GamepadKeys.Button.RIGHT_BUMPER)
        );

        robotFSMCommand = new RobotFSMCommand(
                shooterFSM,
                intakePreShooterFSM,
                led,
                () -> gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.5 // fire request
        );

        /* ---------- Schedule ---------- */
        CommandScheduler.getInstance().schedule(driveCommand);
        CommandScheduler.getInstance().schedule(robotFSMCommand);

        /* ---------- Match Timers ---------- */
//        new ButtonEx(() -> getRuntime() > 30).whenPressed(() -> gamepad1.rumble(500));
//        new ButtonEx(() -> getRuntime() > 60).whenPressed(() -> gamepad1.rumble(500));
//        new ButtonEx(() -> getRuntime() > 110).whenPressed(() -> gamepad1.rumble(1000));
    }

    @Override
    public void onStart() {
        resetRuntime();
        shooterFSM.accelerate_idle();
    }

    @Override
    public void functionalButtons() {


        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.LEFT_BUMPER))
                .whenPressed(new ParallelCommandGroup(
                        new InstantCommand(()-> intakePreShooterFSM.intake()))
                )
                .whenReleased(new ParallelCommandGroup(
                        new InstantCommand(()-> intakePreShooterFSM.init())));

        new ButtonEx(() ->
                gamepadEx1.getButton(GamepadKeys.Button.A))
                .whenPressed(new InstantCommand(()->shooterFSM.state = ShooterFSM.State.SLOW));

        new ButtonEx(() ->
                gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.5)
                .whenPressed(new SequentialCommandGroup(
                                new InstantCommand(()->shooterFSM.state = ShooterFSM.State.MID),
                                new InstantCommand(()-> intakePreShooterFSM.setPowerScale(1.0))
                                           )
                )


                .whenReleased(
                        new SequentialCommandGroup(
                                new WaitCommand(150),
                                new InstantCommand(()->shooterFSM.state = ShooterFSM.State.IDLE)
                        )
                );


        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.Y))
                .whenPressed(new ConditionalCommand(
                        new SequentialCommandGroup(
                                new WaitCommand(150),
                                new InstantCommand(()-> intakePreShooterFSM.setPowerScale(1.0)),
                                new InstantCommand(() -> shooterFSM.state = ShooterFSM.State.IDLE),
                                new InstantCommand(()-> intakePreShooterFSM.stopPreShooter())
                        ),
                        new SequentialCommandGroup(
                                new InstantCommand(()-> intakePreShooterFSM.setPowerScale(0.85)),
                                new InstantCommand(() -> shooterFSM.state = ShooterFSM.State.FAST)
                        ),
                        ()->shooterFSM.state != ShooterFSM.State.IDLE)
                );

        new ButtonEx(()->gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)>0.5)
                .whenReleased(new SequentialCommandGroup(
                        new WaitCommand(600),
                        new InstantCommand(()->shooterFSM.state = ShooterFSM.State.IDLE)
                ));

        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.B))
                .whenPressed(new ParallelCommandGroup(
                        new InstantCommand(()-> intakePreShooterFSM.outtake())))
                .whenReleased(new ParallelCommandGroup(
                        new InstantCommand(()-> intakePreShooterFSM.init())));

        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.DPAD_DOWN))
                .whenPressed(new InstantCommand(()->shooterFSM.emergency()))
                .whenReleased(new InstantCommand(()->shooterFSM.stopAccelerate()));

        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.DPAD_UP))
                .whenPressed(new InstantCommand(()->shooterFSM.stopAccelerate()));
    }

    @Override
    public void run() {

        driveCore.updateOdo();
        driveCore.update();

        CommandScheduler.getInstance().run();

        Vector2d vel = driveCore.getRobotLinearVelocity();

        telemetry.addData("shooter Speed", shooterFSM.shooterUp.getVelocity());
        telemetry.update();
    }
}
