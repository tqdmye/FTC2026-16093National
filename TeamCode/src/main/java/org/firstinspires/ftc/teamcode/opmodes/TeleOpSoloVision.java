package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.BallStorage;
import org.firstinspires.ftc.teamcode.Subsystems.Led;
import org.firstinspires.ftc.teamcode.Subsystems.Vision;
import org.firstinspires.ftc.teamcode.Subsystems.IntakePreShooterFSM;
import org.firstinspires.ftc.teamcode.Subsystems.shooter.ShooterFSM;
import org.firstinspires.ftc.teamcode.commands.RobotFSMCommand;
import org.firstinspires.ftc.teamcode.commands.TeleOpDriveCommandVision;
import org.firstinspires.ftc.teamcode.utils.ButtonEx;

import pedroPathing.Constants;

/*
roadrunner drivecommand
auto adjust shooter, using pedropathing
 */
@TeleOp(group = "0-competition", name = "TeleOp Solo Vision")
public class TeleOpSoloVision extends CommandOpModeEx {
    GamepadEx gamepadEx1;
    Follower follower;
    private IntakePreShooterFSM intakePreShooterFSM;
    private RobotFSMCommand robotFSMCommand;
    private ShooterFSM shooterFSM;
    Led led;
    BallStorage ballStorage;
    Vision vision;

    // 视觉自瞄是否正在接管底盘（避免和摇杆命令抢写电机功率导致抖动）
    public boolean isVisionDriving = false;


    @Override
    public void initialize() {

        CommandScheduler.getInstance().cancelAll();
        telemetry = new MultipleTelemetry(
                telemetry,
                FtcDashboard.getInstance().getTelemetry()
        );

        gamepadEx1 = new GamepadEx(gamepad1);

        /* ---------- Subsystems ---------- */
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(0, 0, 0));
        vision = new Vision(telemetry, hardwareMap);
        shooterFSM = new ShooterFSM(hardwareMap);
        intakePreShooterFSM = new IntakePreShooterFSM(hardwareMap);
        led = new Led(hardwareMap);

        /* ---------- Drive Command ---------- */
        TeleOpDriveCommandVision driveCommandVision = new TeleOpDriveCommandVision(
                follower,
                vision,
                ()->gamepadEx1.getLeftX(),
                ()->gamepadEx1.getLeftY(),
                ()->gamepadEx1.getRightX(),
                ()->(gamepadEx1.getButton(GamepadKeys.Button.RIGHT_BUMPER)),
                ()->(isVisionDriving),
                telemetry);

        robotFSMCommand = new RobotFSMCommand(
                shooterFSM,
                intakePreShooterFSM,
                led,
                () -> gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.5 // fire request
        );

        /* ---------- Schedule ---------- */
        CommandScheduler.getInstance().schedule(driveCommandVision);
        CommandScheduler.getInstance().schedule(robotFSMCommand);

        //timers
        new ButtonEx(()->getRuntime()>30).whenPressed(()->gamepad1.rumble(500));
        new ButtonEx(()->getRuntime()>60).whenPressed(()->gamepad1.rumble(500));
        new ButtonEx(()->getRuntime()>110).whenPressed(()->gamepad1.rumble(1000));
    }

    @Override
    public void onStart() {
        resetRuntime();
        follower.startTeleOpDrive(true);
        shooterFSM.accelerate_idle();
    }

    @Override
    public void functionalButtons() {

        //leftBumper -- intake
        //rightTrigger -- Shooter
        //leftTrigger -- preShooter
        //a -- preShooter & intake 反转

        new ButtonEx(() -> gamepadEx1.getButton(GamepadKeys.Button.X))
                .whenPressed(new InstantCommand(() -> {
                    isVisionDriving = true;
                    gamepad1.rumble(100);
                }))
                .whenReleased(new InstantCommand(() -> {
                    isVisionDriving = false;
                    vision.resetPID();
                }));

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
    public void run(){
        follower.update();
        CommandScheduler.getInstance().run();

        // 实战反馈：如果开启自瞄且误差极小，手柄轻微震动提醒可以发射
        if (isVisionDriving && vision.targetFound) {
            if (Math.abs(vision.getFilteredHeadingError()) < Vision.TOLERANCE) {
                // 每秒轻微震动一次提示对准
                if (getRuntime() % 0.5 < 0.05) {
                    gamepad1.rumble(60);
                }
            }
        }

        telemetry.addData("Vision Mode", isVisionDriving);
        telemetry.addData("Tag Visible", vision.targetFound);
        telemetry.addData("Pose", follower.getPose().toString());
        telemetry.update();
    }
}