//package org.firstinspires.ftc.teamcode.opmodes;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.acmerobotics.roadrunner.geometry.Vector2d;
//import com.arcrobotics.ftclib.command.CommandScheduler;
//import com.arcrobotics.ftclib.command.InstantCommand;
//import com.arcrobotics.ftclib.command.ParallelCommandGroup;
//import com.arcrobotics.ftclib.command.SequentialCommandGroup;
//import com.arcrobotics.ftclib.command.WaitCommand;
//import com.arcrobotics.ftclib.gamepad.GamepadEx;
//import com.arcrobotics.ftclib.gamepad.GamepadKeys;
//import com.pedropathing.follower.Follower;
//import com.pedropathing.geometry.Pose;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotor;
//
//import org.firstinspires.ftc.teamcode.commands.TeleOpDriveCommand;
//import org.firstinspires.ftc.teamcode.commands.fsm.RobotFSMCommand;
//import org.firstinspires.ftc.teamcode.Subsystems.BallStorage;
//import org.firstinspires.ftc.teamcode.Subsystems.Firer ;
//import org.firstinspires.ftc.teamcode.Subsystems.Led;
//import org.firstinspires.ftc.teamcode.Subsystems.driving.NewMecanumDrive;
//import org.firstinspires.ftc.teamcode.Subsystems.shooter.ShooterFSM;
//import org.firstinspires.ftc.teamcode.utils.ButtonEx;
//
//import pedroPathing.Constants;
//
//@TeleOp(group = "0-competition", name = "TeleOp Solo Revise")
//public class TeleOpSoloRevise extends CommandOpModeEx {
//
//    private GamepadEx gamepadEx1;
//
//    private NewMecanumDrive driveCore;
//    private Follower follower;
//
//    private ShooterFSM shooterFSM;
//    private Firer firer;
//    private Led led;
//    private BallStorage ballStorage;
//
//    private RobotFSMCommand robotFSMCommand;
//
//    private boolean isFieldCentric = false;
//
//    @Override
//    public void initialize() {
//
//        CommandScheduler.getInstance().cancelAll();
//        telemetry = new MultipleTelemetry(
//                telemetry,
//                FtcDashboard.getInstance().getTelemetry()
//        );
//
//        gamepadEx1 = new GamepadEx(gamepad1);
//
//        /* ---------- Subsystems ---------- */
//        driveCore = new NewMecanumDrive(hardwareMap);
//        shooterFSM = new ShooterFSM(hardwareMap);
//        firer = new Firer (hardwareMap);
//        led = new Led(hardwareMap);
//        ballStorage = new BallStorage(hardwareMap);
//
//        driveCore.init();
//        driveCore.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        driveCore.resetHeading();
//        driveCore.resetOdo();
//
//        /* ---------- Drive Command ---------- */
//        TeleOpDriveCommand driveCommand = new TeleOpDriveCommand(
//                driveCore,
//                () -> gamepadEx1.getLeftX(),
//                () -> gamepadEx1.getLeftY(),
//                () -> gamepadEx1.getRightX(),
//                () -> (gamepadEx1.getButton(GamepadKeys.Button.START) && !gamepad1.touchpad),
//                () -> gamepadEx1.getButton(GamepadKeys.Button.RIGHT_BUMPER),
//                () -> isFieldCentric
//        );
//
//        /* ---------- FSM ---------- */
//        robotFSMCommand = new RobotFSMCommand(
//                shooterFSM,
//                firer,
//                led,
//                ballStorage,
//
//                // fire request
//                () -> gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.5,
//
//                // mode toggle
//                () -> gamepadEx1.getButton(GamepadKeys.Button.A)
//        );
//
//        /* ---------- Pedro Auto Shoot ---------- */
//        follower = Constants.createFollower(hardwareMap);
//        follower.setStartingPose(new Pose(0, 0, 0));
//
//
//        /* ---------- Schedule ---------- */
//        CommandScheduler.getInstance().schedule(driveCommand);
//        CommandScheduler.getInstance().schedule(robotFSMCommand);
//
//        /* ---------- Match Timers ---------- */
//        new ButtonEx(() -> getRuntime() > 30).whenPressed(() -> gamepad1.rumble(500));
//        new ButtonEx(() -> getRuntime() > 60).whenPressed(() -> gamepad1.rumble(500));
//        new ButtonEx(() -> getRuntime() > 110).whenPressed(() -> gamepad1.rumble(1000));
//    }
//
//    @Override
//    public void onStart() {
//        resetRuntime();
//        shooterFSM.accelerate_idle();
//    }
//
//    @Override
//    public void functionalButtons() {
//
//
//        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.BACK))
//                .whenPressed(new InstantCommand(()->isFieldCentric=!isFieldCentric));
//
//        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.LEFT_BUMPER))
//                .whenPressed(new ParallelCommandGroup(
//                        new InstantCommand(()->firer.intake())       )
//                )
//                .whenReleased(new ParallelCommandGroup(
//                        new InstantCommand(()->firer.init())        )
//                );
//
//        new ButtonEx(() ->
//                gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.5
//                        && robotFSMCommand.getRobotMode()== RobotFSMCommand.RobotMode.COMBAT)
//                .whenPressed(new SequentialCommandGroup(
//                        new InstantCommand(()->isVelocityDetecting= ! isVelocityDetecting),
//                        new InstantCommand(() -> shooterFSM.accelerate_mid())))
//
//                .whenReleased(
//                        new SequentialCommandGroup(
//                                new WaitCommand(150),
//                                new InstantCommand(()->isVelocityDetecting= ! isVelocityDetecting),
//                                new InstantCommand(() -> shooterFSM.accelerate_idle())
//                        )
//                );
//
//
//        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.Y)
//                && !isLimitOn   && !isAutoShoot)
//                .whenPressed(new SequentialCommandGroup(
//                        new InstantCommand(()->isVelocityDetecting= true),
//                        new InstantCommand(()->firer.setPowerScale(0.75)),
//                        new InstantCommand(() -> shooterFSM.accelerate_fast())
//                ))
//                .whenReleased(
//                        new SequentialCommandGroup(
//                                new WaitCommand(150),
//                                new InstantCommand(()->isVelocityDetecting=false),
//                                new InstantCommand(()->firer.setPowerScale(1.0)),
//                                new InstantCommand(() -> shooterFSM.accelerate_idle()),
//                                new InstantCommand(()->firer.stopPreShooter())
//                        )
//                );
//
//        new ButtonEx(()->gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)>0.5)
//                .whenPressed(new InstantCommand(()->isShooting = true))
//                .whenReleased(new InstantCommand(()->isShooting = false));
//
//        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.B))
//                .whenPressed(new ParallelCommandGroup(
//                        new InstantCommand(()->firer.outtake())))
//                .whenReleased(new ParallelCommandGroup(
//                        new InstantCommand(()->firer.init())));
//
//        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.DPAD_DOWN))
//                .whenPressed(new InstantCommand(()->shooterFSM.emergency()))
//                .whenReleased(new InstantCommand(()->shooterFSM.stopAccelerate()));
//
//        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.DPAD_UP))
//                .whenPressed(new InstantCommand(()->shooterFSM.stopAccelerate()));
//    }
//
//    @Override
//    public void run() {
//
//        follower.update();
//        driveCore.updateOdo();
//        driveCore.update();
//
//        shooterFSM.checkVelocity();
//        ballStorage.update();
//
//        CommandScheduler.getInstance().run();
//
//        Vector2d vel = driveCore.getRobotLinearVelocity();
//
//        telemetry.addData("RobotMode", robotFSMCommand.getRobotMode());
//        telemetry.addData("shooterFSMState", shooterFSM.getState());
//        telemetry.addData("Pedro Pose", follower.getPose());
//        telemetry.addData("Robot Speed", Math.hypot(vel.getX(), vel.getY()));
//        telemetry.update();
//    }
//}
