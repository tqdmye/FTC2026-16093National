package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.RepeatCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.BallStorage;
import org.firstinspires.ftc.teamcode.Subsystems.IntakePreshooter;
import org.firstinspires.ftc.teamcode.Subsystems.Led;
import org.firstinspires.ftc.teamcode.Subsystems.Vision;
import org.firstinspires.ftc.teamcode.Subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.commands.PedroShootAutoAdjustCommand;
import org.firstinspires.ftc.teamcode.commands.PreLimitCommand;
import org.firstinspires.ftc.teamcode.commands.TeleOpDriveCommandVision;
import org.firstinspires.ftc.teamcode.utils.ButtonEx;

import pedroPathing.Constants;

/*
roadrunner drivecommand
auto adjust shooter, using pedropathing
 */
@TeleOp(group = "0-competition", name = "TeleOp Solo Vision")
public class TeleOpSoloVision extends CommandOpModeEx {
    GamepadEx gamepadEx1, gamepadEx2;
//    NewMecanumDrive driveCore;
    Follower follower;

    Shooter shooter;
    IntakePreshooter intake;
    Led led;
    BallStorage ballStorage;
    Vision vision;

    PreLimitCommand preLimitCommand;
    PedroShootAutoAdjustCommand pedroAutoShootAdjustCommand;

    private boolean isFieldCentric=false;
    // 视觉自瞄是否正在接管底盘（避免和摇杆命令抢写电机功率导致抖动）
    public boolean isVisionDriving = false;
    private boolean visionArrivedNotified = false;

    public boolean isLimitOn = true;

    public boolean isShooting = false;

    public boolean isVelocityDetecting = false;

    public boolean isAutoShoot = false;
    public boolean isShootFar = false;


    @Override
    public void initialize() {

        CommandScheduler.getInstance().cancelAll();
        this.telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        gamepadEx1 = new GamepadEx(gamepad1);
        gamepadEx2 = new GamepadEx(gamepad2);

//        driveCore = new NewMecanumDrive(hardwareMap);
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(0, 0, 0));
        ballStorage = new BallStorage(hardwareMap);
        vision = new Vision(telemetry, hardwareMap);

//        driveCore.init();
        TeleOpDriveCommandVision driveCommandVision = new TeleOpDriveCommandVision(
                follower,
                vision,
                ()->gamepadEx1.getLeftX(),
                ()->gamepadEx1.getLeftY(),
                ()->gamepadEx1.getRightX(),
                ()->(gamepadEx1.getButton(GamepadKeys.Button.RIGHT_BUMPER)),
                ()->(isVisionDriving),
                telemetry);

        intake = new IntakePreshooter(hardwareMap);
//        frontArm.setLED(false);
        shooter = new Shooter(hardwareMap);

        led = new Led(hardwareMap);
        preLimitCommand = new PreLimitCommand(shooter,
                intake,
                led,
                ballStorage,
                ()->(isVelocityDetecting),
                ()->(isLimitOn),
                ()->(isShooting));


//        driveCore.resetHeading();
//        driveCore.yawHeading += 90; //如果specimen自动接solo手动就把这行去掉
//        driveCore.yawHeading %= 360;    //如果specimen自动接solo手动就把这行去掉
//        driveCore.resetOdo();



        CommandScheduler.getInstance().schedule(driveCommandVision);
        CommandScheduler.getInstance().schedule(preLimitCommand);
        CommandScheduler.getInstance().schedule(
                new PedroShootAutoAdjustCommand(
                        shooter,
                        follower,
                        () -> isAutoShoot,
                        () -> isLimitOn
                )
        );




        //timers
        new ButtonEx(()->getRuntime()>30).whenPressed(()->gamepad1.rumble(500));
        new ButtonEx(()->getRuntime()>60).whenPressed(()->gamepad1.rumble(500));
        new ButtonEx(()->getRuntime()>110).whenPressed(()->gamepad1.rumble(1000));
    }

    @Override
    public void onStart() {
        resetRuntime();
        follower.startTeleOpDrive(true);
        shooter.accelerate_idle();
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

        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.BACK))
                .whenPressed(new InstantCommand(()->isFieldCentric=!isFieldCentric));

        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.A))
                .toggleWhenPressed(
                        new InstantCommand(()->shooter.accelerate_slow()),
                        new InstantCommand(()->shooter.accelerate_idle()));

        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.LEFT_BUMPER))
                .whenPressed(new ParallelCommandGroup(
                        new InstantCommand(()->intake.intake())       )
                )
                .whenReleased(new ParallelCommandGroup(
                        new InstantCommand(()->intake.init())        )
                );

        new ButtonEx(() ->
                gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.5
                        && !isAutoShoot)
                .whenPressed(new SequentialCommandGroup(
                        new InstantCommand(()->isVelocityDetecting= ! isVelocityDetecting),
                        new InstantCommand(() -> shooter.accelerate_mid())))

                .whenReleased(
                        new SequentialCommandGroup(
                                new WaitCommand(150),
                                new InstantCommand(()->isVelocityDetecting= ! isVelocityDetecting),
                                new InstantCommand(() -> shooter.accelerate_idle())
                        )
                );


        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.Y)
                && !isAutoShoot)
                .toggleWhenPressed(
                        new ParallelCommandGroup(
                                new InstantCommand(()->isVelocityDetecting= true),
                                new InstantCommand(()->intake.setPowerScale(0.75)),
                                new InstantCommand(()->isShootFar = true),
                                new RepeatCommand(new InstantCommand(()->shooter.accelerate_fast()))
                        ),
                        new SequentialCommandGroup(
                                new WaitCommand(150),
                                new InstantCommand(()->isVelocityDetecting=false),
                                new InstantCommand(()->intake.setPowerScale(1.0)),
                                new InstantCommand(() -> shooter.accelerate_idle()),
                                new InstantCommand(()->isShootFar = false)
                        )
                );

        new ButtonEx(()->gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)>0.5)
                .whenPressed(new InstantCommand(()->isShooting = true))
                .whenReleased(new InstantCommand(()->isShooting = false));

        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.B))
                .whenPressed(new ParallelCommandGroup(
                        new InstantCommand(()->intake.outtake())))
                .whenReleased(new ParallelCommandGroup(
                        new InstantCommand(()->intake.init())));

        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.DPAD_DOWN))
                .whenPressed(new InstantCommand(()->shooter.emergency()))
                .whenReleased(new InstantCommand(()->shooter.stopAccelerate()));

        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.DPAD_UP))
                .whenPressed(new InstantCommand(()->shooter.stopAccelerate()));

//        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.DPAD_LEFT))
//                .whenPressed(vision.turnToAprilTag(follower));
    }

    @Override
    public void run(){
        follower.update();
        ballStorage.update();
        shooter.checkVelocity();
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