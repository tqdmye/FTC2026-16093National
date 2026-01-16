package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Vector2d;
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
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.commands.PedroShootAutoAdjustCommand;
import org.firstinspires.ftc.teamcode.commands.PreLimitCommand;
import org.firstinspires.ftc.teamcode.commands.TeleOpDriveCommand;
import org.firstinspires.ftc.teamcode.subsystems.Led;
import org.firstinspires.ftc.teamcode.subsystems.ballstorage.BallStorage;
import org.firstinspires.ftc.teamcode.subsystems.driving.NewMecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.intakepreshoot.IntakePreshooter;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;
import org.firstinspires.ftc.teamcode.utils.ButtonEx;

import pedroPathing.Constants;

/*
roadrunner drivecommand
auto adjust shooter, using pedropathing
 */
@TeleOp(group = "0-competition", name = "TeleOp Solo Test 2")
public class TeleOpSoloTest2 extends CommandOpModeEx {
    GamepadEx gamepadEx1, gamepadEx2;
    NewMecanumDrive driveCore;
    PreLimitCommand preLimitCommand;

    PedroShootAutoAdjustCommand pedroAutoShootAdjustCommand;

    Shooter shooter;
    IntakePreshooter intake;
    Led led;

    BallStorage ballStorage;

    Follower follower;

    private boolean isFieldCentric=false;

    public boolean isLimitOn = true;

    public boolean isShooting = false;

    public boolean isVelocityDetecting = false;

    public boolean isAutoShoot = true;


    @Override
    public void initialize() {

        CommandScheduler.getInstance().cancelAll();
        this.telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        gamepadEx1 = new GamepadEx(gamepad1);
        gamepadEx2 = new GamepadEx(gamepad2);

        driveCore = new NewMecanumDrive(hardwareMap);
        ballStorage = new BallStorage(hardwareMap);

        driveCore.init();
        TeleOpDriveCommand driveCommand = new TeleOpDriveCommand(driveCore,
                ()->gamepadEx1.getLeftX(),
                ()->gamepadEx1.getLeftY(),
                ()->gamepadEx1.getRightX(),
                ()->(gamepadEx1.getButton(GamepadKeys.Button.START) && !gamepad1.touchpad),
                ()->(gamepadEx1.getButton(GamepadKeys.Button.RIGHT_BUMPER)),
                ()->(isFieldCentric));

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



        driveCore.resetHeading();
//        driveCore.yawHeading += 90; //如果specimen自动接solo手动就把这行去掉
//        driveCore.yawHeading %= 360;    //如果specimen自动接solo手动就把这行去掉
        driveCore.resetOdo();

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(0, 0, 0));

        driveCore.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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
        shooter.accelerate_slow();
    }

    @Override
    public void functionalButtons() {

        //leftBumper -- intake
        //rightTrigger -- Shooter
        //leftTrigger -- preShooter
        //a -- preShooter & intake 反转

        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.DPAD_UP))
                .whenPressed(new InstantCommand(()->isAutoShoot=!isAutoShoot));

        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.BACK))
                .whenPressed(new InstantCommand(()->isFieldCentric=!isFieldCentric));

        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.A))
                .whenPressed(new InstantCommand(()->isLimitOn=!isLimitOn));

        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.LEFT_BUMPER))
                .whenPressed(new ParallelCommandGroup(
                        new InstantCommand(()->intake.intake())       )
                )
                .whenReleased(new ParallelCommandGroup(
                        new InstantCommand(()->intake.init())        )
                );

        new ButtonEx(() ->
                gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.5
                        && !isLimitOn && !isAutoShoot)
                .whenPressed(new SequentialCommandGroup(
                        new InstantCommand(()->isVelocityDetecting= ! isVelocityDetecting),
                        new InstantCommand(() -> shooter.accelerate_mid())))

                .whenReleased(
                        new SequentialCommandGroup(
                                new WaitCommand(150),
                                new InstantCommand(()->isVelocityDetecting= ! isVelocityDetecting),
                                new InstantCommand(() -> shooter.accelerate_slow())
                        )
                );


        new ButtonEx(()->gamepadEx1.getButton(GamepadKeys.Button.Y)
                && !isLimitOn   && !isAutoShoot)
                .whenPressed(new SequentialCommandGroup(
                        new InstantCommand(()->isVelocityDetecting= true),
                        new InstantCommand(()->intake.setPowerScale(0.85)),
                        new InstantCommand(()->shooter.setPowerScale(0.85)),
                        new InstantCommand(() -> shooter.accelerate_fast())
                ))
                .whenReleased(
                        new SequentialCommandGroup(
                                new WaitCommand(150),
                                new InstantCommand(()->isVelocityDetecting=false),
                                new InstantCommand(()->intake.setPowerScale(1.0)),
                                new InstantCommand(()->shooter.setPowerScale(1.0)),
                                new InstantCommand(() -> shooter.accelerate_slow())
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
    }

    @Override
    public void run(){
        driveCore.updateOdo();
        driveCore.update();
        ballStorage.update();
        telemetry.addData("color", ballStorage.getColorQueue());
        if (Math.abs(shooter.shooterRight.getVelocity()-1320)<= 40){
            shooter.isAsTargetVelocity = true;
        } else {
            shooter.isAsTargetVelocity = false;
        }
        CommandScheduler.getInstance().run();
        Vector2d robotVel = driveCore.getRobotLinearVelocity();
        if(isAutoShoot)telemetry.addLine("AutoShoot");
        else telemetry.addLine("Not Auto Shoot");
        telemetry.addData("Pedro Pose", follower.getPose());
        telemetry.addData("shooter velocity", shooter.shooterRight.getVelocity());
        telemetry.addData("Robot vx (in/s)", robotVel.getX());
        telemetry.addData("Robot vy (in/s)", robotVel.getY());
        telemetry.addData("Robot speed", Math.hypot(robotVel.getX(), robotVel.getY()));
        telemetry.addData("LF vel", driveCore.leftFront.getVelocity());
        telemetry.addData("RF vel", driveCore.rightFront.getVelocity());
        telemetry.addData("LR vel", driveCore.leftRear.getVelocity());
        telemetry.addData("RR vel", driveCore.rightRear.getVelocity());
        if(isFieldCentric) telemetry.addLine("Field Centric");
        else telemetry.addLine("Robot Centric");
        if(isLimitOn) telemetry.addLine("Limit On");
        else telemetry.addLine("Limit Off");
        if(isVelocityDetecting) telemetry.addLine("is velocity detesting" );
        else telemetry.addLine("not detecting");
        if(shooter.isAsTargetVelocity) telemetry.addLine("is At target velocity");
        else telemetry.addLine("not at target vel");
        telemetry.addData("vel diff", Math.abs(shooter.shooterRight.getVelocity()-1320));
        telemetry.update();
    }
}