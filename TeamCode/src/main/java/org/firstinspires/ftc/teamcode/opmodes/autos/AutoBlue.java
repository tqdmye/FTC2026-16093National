package org.firstinspires.ftc.teamcode.opmodes.autos;//package org.firstinspires.ftc.teamcode.opmodes.autos;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.arcrobotics.ftclib.command.Command;
//import com.arcrobotics.ftclib.command.CommandScheduler;
//import com.arcrobotics.ftclib.command.InstantCommand;
//import com.arcrobotics.ftclib.command.ParallelRaceGroup;
//import com.arcrobotics.ftclib.command.SequentialCommandGroup;
//import com.arcrobotics.ftclib.command.WaitCommand;
//import com.pedropathing.follower.Follower;
//import com.pedropathing.localization.Pose;
//import com.pedropathing.pathgen.BezierLine;
//import com.pedropathing.pathgen.PathChain;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import org.firstinspires.ftc.teamcode.Subsystems.Intake;
//import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
//import org.firstinspires.ftc.teamcode.commands.autos.driveAutoCommand;
//import org.firstinspires.ftc.teamcode.utils.FollowerEx;
//import pedroPathing.constants.FConstants;
//import pedroPathing.constants.LConstants;
////和手动一样，似乎已经没有意义尝试写auto，但是我可能太闲了。
//
//@Config
//@Autonomous(name = "AutoBlue")
//public class AutoBlue extends AutoOpModeEx {
//
//    Shooter shooter;
//    Intake intake;
//    private Follower follower;
//
//    public PathChain Path1;
//    public PathChain Path2;
//    public PathChain Path3;
//    public PathChain Path4;
//    public PathChain Path5;
//    public PathChain Path6;
//    public PathChain Path7;
//    public PathChain Path8;
//    public PathChain Path9;
//    public PathChain Path10;
//
//        public Command runAutoCommand(){
//
//            Path1 = follower  //start->score
//                    .pathBuilder()
//                    .addPath(
//                            new BezierLine(new Pose(123.850, -21.490), new Pose(105.644, -32.950, Math.toRadians(44)))
//                    )
//                    .setLinearHeadingInterpolation(Math.toRadians(45), Math.toRadians(44))
//                    .build();
//
//            Path2 = follower //score->intake1.1
//                    .pathBuilder()
//                    .addPath(
//                            new BezierLine(new Pose(105.644, -32.950, Math.toRadians(44)), new Pose(79.249, -38.147, Math.toRadians(90)))
//                    )
//                    .setLinearHeadingInterpolation(Math.toRadians(44), Math.toRadians(90))
//                    .build();
//
//            Path3 = follower// intake1.1->intake1.2
//                    .pathBuilder()
//                    .addPath(
//                            new BezierLine(new Pose(79.249, -38.147), new Pose(79.249, -8.239))
//                    )
//                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(90))
//                    .build();
//
//            Path4 = follower //intake1.2->score
//                    .pathBuilder()
//                    .addPath(
//                            new BezierLine(new Pose(79.249, -8.239), new Pose(105.644, -32.950))
//                    )
//                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(44))
//                    .build();
//
//            Path5 = follower //score->intake2.1
//                    .pathBuilder()
//                    .addPath(
//                            new BezierLine(new Pose(105.644, -32.950), new Pose(53.050, -35.834))
//                    )
//                    .setLinearHeadingInterpolation(Math.toRadians(44), Math.toRadians(90))
//                    .build();
//
//            Path6 = follower //intake2.1->intake2.2
//                    .pathBuilder()
//                    .addPath(
//                            new BezierLine(new Pose(53.050, -35.834), new Pose(53.764, 0.000))
//                    )
//                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(90))
//                    .build();
//
//            Path7 = follower //intake2.2->score
//                    .pathBuilder()
//                    .addPath(
//                            new BezierLine(new Pose(53.764, 0.000), new Pose(105.644, -32.950))
//                    )
//                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(44))
//                    .build();
//
//            Path8 = follower //score->intake3.1
//                    .pathBuilder()
//                    .addPath(
//                            new BezierLine(new Pose(105.644, -32.950), new Pose(30.000, -36.730))
//                    )
//                    .setLinearHeadingInterpolation(Math.toRadians(44), Math.toRadians(90))
//                    .build();
//
//            Path9 = follower //intake3.1->intake3.2
//                    .pathBuilder()
//                    .addPath(
//                            new BezierLine(new Pose(30.000, -36.730), new Pose(30, 3))
//                    )
//                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(90))
//                    .build();
//
//            //path10 intake3.2->score
//            Path10 = follower //intake3.1->intake3.2
//                    .pathBuilder()
//                    .addPath(
//                            new BezierLine(new Pose(30.000, -36.730), new Pose(30, 3))
//                    )
//                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(90))
//                    .build();
//
//
//
//            return new SequentialCommandGroup(
//                    new ParallelRaceGroup(
//                            new WaitCommand(3000),
//                            new InstantCommand(() -> shooter.autoShortshoot())
//                    ),
//
//                    new driveAutoCommand(follower,Path1),
//                    /*
//                    new ParallelRaceGroup(
//                            new driveAutoCommand(follower,Path1),
//                            new InstantCommand(()-> shooter.acc())
//                    ),
//
//                     */
//                    new ParallelRaceGroup(
//                            new driveAutoCommand(follower,Path2),
//                            new InstantCommand((()-> intake.intake()))
//                    ),
//                    new driveAutoCommand(follower,Path3),
//                    /*
//                    new ParallelRaceGroup(
//                            new driveAutoCommand(follower,Path3),
//                            new InstantCommand(()-> shooter.acc())
//                    ),
//
//                     */
//                    new ParallelRaceGroup(
//                            new WaitCommand(3000),
//                            new InstantCommand(() -> shooter.autoMidshoot())),
//                    new driveAutoCommand(follower,Path4),
//                    new ParallelRaceGroup(
//                            new driveAutoCommand(follower,Path5),
//                            new InstantCommand(()-> intake.intake())
//                    ),
//                    new driveAutoCommand(follower,Path6),
//                    /*
//                    new ParallelRaceGroup(
//                            new driveAutoCommand(follower,Path6),
//                            new InstantCommand(()-> shooter.acc())
//                    ),
//
//                     */
//                    new ParallelRaceGroup(
//                            new WaitCommand(3000),
//                            new InstantCommand(() -> shooter.autoMidshoot())
//                    ),
//                    new driveAutoCommand(follower,Path7),
//                    new ParallelRaceGroup(
//                            new driveAutoCommand(follower,Path8),
//                            new InstantCommand(()-> intake.intake())
//                    ),
//                    new driveAutoCommand(follower,Path9),
//                    /*
//                    new ParallelRaceGroup(
//                            new driveAutoCommand(follower,Path9),
//                            new InstantCommand(()-> shooter.acc())
//                    ),
//
//                     */
//                    new ParallelRaceGroup(
//                            new WaitCommand(5000),
//                            new InstantCommand(() -> shooter.autoShortshoot())
//                    )
//            );
//    }
//
//    @Override
//    public void initialize() {
//        CommandScheduler.getInstance().cancelAll();
//        this.telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
//        follower = new FollowerEx(hardwareMap, FConstants.class, LConstants.class);
//        shooter = new Shooter(hardwareMap);
//        intake = new Intake(hardwareMap);
//        CommandScheduler.getInstance().schedule(runAutoCommand());
//    }
//    private void periodic() {
//        CommandScheduler.getInstance().run();
//
//        follower.update();
//        telemetry.addData("x", follower.getPose().getX());
//        telemetry.addData("y", follower.getPose().getY());
//        telemetry.addData("heading", follower.getPose().getHeading());
//        telemetry.update();
//    }
//
//    @Override
//    public void run() {
//
//            periodic();
//    }
//}
//
//
//
