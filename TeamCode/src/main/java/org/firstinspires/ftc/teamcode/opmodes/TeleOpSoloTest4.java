package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.PPTeleOpDriveCommand;
import org.firstinspires.ftc.teamcode.commands.PedroShootAutoAdjustCommand;
import org.firstinspires.ftc.teamcode.commands.PreLimitCommand;
import org.firstinspires.ftc.teamcode.subsystems.Led;
import org.firstinspires.ftc.teamcode.subsystems.ballstorage.BallStorage;
import org.firstinspires.ftc.teamcode.subsystems.intakepreshoot.IntakePreshooter;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.utils.ButtonEx;

import pedroPathing.Constants;
/*
pedropathing drivecommand
auto adjust shooter, using pedropathing
 */
@TeleOp(group = "0-competition", name = "TeleOp Solo Test 4")
public class TeleOpSoloTest4 extends CommandOpModeEx {
    Follower follower;
    GamepadEx gamepadEx1, gamepadEx2;
    PreLimitCommand preLimitCommand;
    Shooter shooter;
    IntakePreshooter intake;
    Led led;
    BallStorage ballStorage;
    private boolean isRobotCentric=true;
    public boolean isLimitOn = true;
    public boolean isShooting = false;
    public boolean isVelocityDetecting = false;
    public boolean isAutoShoot = false;


    @Override
    public void initialize() {
        CommandScheduler.getInstance().cancelAll();
        this.telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        gamepadEx1 = new GamepadEx(gamepad1);
        gamepadEx2 = new GamepadEx(gamepad2);

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(0, 0, 0));
        follower.update();

        PPTeleOpDriveCommand driveCommand = new PPTeleOpDriveCommand(follower,
                ()->-gamepadEx1.getLeftX(),
                ()->gamepadEx1.getLeftY(),
                ()->-gamepadEx1.getRightX(),
                ()->(gamepadEx1.getButton(GamepadKeys.Button.START) && !gamepad1.touchpad),
                ()->(gamepadEx1.getButton(GamepadKeys.Button.RIGHT_BUMPER)),
                ()->(isRobotCentric));

        intake = new IntakePreshooter(hardwareMap);
//        frontArm.setLED(false);
        shooter = new Shooter(hardwareMap);

        led = new Led(hardwareMap);
        preLimitCommand = new PreLimitCommand(shooter,
                intake,
                led,
                //                ballStorage,
                ()->(isVelocityDetecting),
                ()->(isLimitOn),
                ()->(isShooting));

        CommandScheduler.getInstance().schedule(driveCommand);
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
        follower.startTeleOpDrive();
    }

    @Override
    public void functionalButtons() {

        //leftBumper -- intake
        //rightTrigger -- Shooter
        //leftTrigger -- preShooter
        //a -- preShooter & intake 反转

        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.DPAD_RIGHT))
                .whenPressed(new InstantCommand(()->isAutoShoot=!isAutoShoot));

        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.BACK))
                .whenPressed(new InstantCommand(()->isRobotCentric=!isRobotCentric));

        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.A))
                .whenPressed(new InstantCommand(()->isLimitOn=!isLimitOn));

        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.LEFT_BUMPER))
                .whenPressed(new ParallelCommandGroup(
                        new InstantCommand(()->intake.intake()))
                )
                .whenReleased(new ParallelCommandGroup(
                        new InstantCommand(()->intake.init()))
                );

        new ButtonEx(() ->
                gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.5
                        && !isLimitOn && !isAutoShoot)
                .whenPressed(new SequentialCommandGroup(
                        new InstantCommand(()->isVelocityDetecting = !isVelocityDetecting),
                        new InstantCommand(() -> shooter.accelerate_mid())))
                .whenReleased(
                        new SequentialCommandGroup(
                                new WaitCommand(150),
                                new InstantCommand(()->isVelocityDetecting = !isVelocityDetecting),
                                new InstantCommand(() -> shooter.accelerate_idle())
                        )
                );

        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.Y)
                && !isLimitOn   && !isAutoShoot)
                .whenPressed(new SequentialCommandGroup(
                        new InstantCommand(()->isVelocityDetecting = true),
                        new InstantCommand(()->intake.setPowerScale(0.85)),
                        new InstantCommand(()->shooter.setPowerScale(0.85)),
                        new InstantCommand(() -> shooter.accelerate_fast())
                ))
                .whenReleased(
                        new SequentialCommandGroup(
                                new WaitCommand(150),
                                new InstantCommand(()->isVelocityDetecting = false),
                                new InstantCommand(()->intake.setPowerScale(1.0)),
                                new InstantCommand(()->shooter.setPowerScale(1.0)),
                                new InstantCommand(() -> shooter.accelerate_idle())
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

        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.DPAD_LEFT))
                .whenPressed(new InstantCommand(()->shooter.stopAccelerate()));
    }

    @Override
    public void run(){

        if (Math.abs(shooter.shooterRight.getVelocity()-1320)<= 40){
            shooter.isAsTargetVelocity = true;
        } else {
            shooter.isAsTargetVelocity = false;
        }
        CommandScheduler.getInstance().run();
        if(isAutoShoot)telemetry.addLine("AutoShoot");
        else telemetry.addLine("Not Auto Shoot");
        telemetry.addData("Pedro Pose", follower.getPose());
        telemetry.addData("shooter velocity", shooter.shooterRight.getVelocity());
        if(isRobotCentric) telemetry.addLine("Robot Centric");
        else telemetry.addLine("Field Centric");
        if(isLimitOn) telemetry.addLine("Limit On");
        else telemetry.addLine("Limit Off");
        if(isVelocityDetecting) telemetry.addLine("is velocity detesting" );
        else telemetry.addLine("not detecting");
        if(shooter.isAsTargetVelocity) telemetry.addLine("is At target velocity");
        else telemetry.addLine("not at target vel");
        telemetry.update();
    }
}