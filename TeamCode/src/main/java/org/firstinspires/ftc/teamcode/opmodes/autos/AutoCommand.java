package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
//import com.pedropathing.localization.Pose;
//import com.pedropathing.pathgen.Point;


public class AutoCommand {
    Shooter shooter;
    Intake intake;

    Follower follower;
    boolean visionSucceed = false;
    int failedTime = 0;
    public AutoCommand(Shooter shooter, Intake intake) {
        this.shooter = shooter;
        this.intake = intake;
    }

    public Command accelerate(){
        return new InstantCommand(()->shooter.accelerate_slow());
    }

    public Command accelerateMid(){
        return new InstantCommand(()->shooter.accelerate_mid());
    }

    public Command preLimitOn(){
        return new InstantCommand(()->intake.limitOff());
    }


//    public Command autoAccelerate(){
//        return new InstantCommand(()->shooter.auto_accelerate_slow());
//    }

    public Command accelerateFast(){
        return new InstantCommand(()->shooter.accelerate_fast());
    }

    public Command shoot(){
        return new SequentialCommandGroup(
                new InstantCommand(()->shooter.shoot()),
                new WaitCommand(800),
                new InstantCommand(()->shooter.init())
        );
    }

    public Command shootMid(){
        return new SequentialCommandGroup(
                new InstantCommand(()->shooter.servosetpositon_mid_4()),
                new WaitCommand(130), 
                new InstantCommand(()->shooter.shoot()),
                new WaitCommand(1800),
                new InstantCommand(()->shooter.init())
        );
    }

    public Command shootFar(){
        return new SequentialCommandGroup(
                new WaitCommand(500),
                new WaitCommand(100),
                new InstantCommand(()->shooter.shoot()),
                new WaitCommand(1500),
                new InstantCommand(()->shooter.init())
        );
    }

    public Command intake(){
        return new SequentialCommandGroup(
                new InstantCommand(()->intake.intake()),
                new WaitCommand(200)
        );
    }

    public Command stopAll(){
        return new ParallelCommandGroup(
                new InstantCommand(()->intake.init()),
                new InstantCommand(()->shooter.init()),
                new InstantCommand(()->shooter.stopAccelerate())
        );
    }
    public Command park(){
        return new ParallelCommandGroup(
                new InstantCommand(()->intake.init()),
                new InstantCommand(()->shooter.init()),
                new InstantCommand(()->shooter.stopAccelerate()),
                new WaitCommand(99999999)
        );
    }


//    /*--------------OTHERS----------------*/
//    public Point midPoint(Pose start, Pose end){
//        return new Point((start.getX()+end.getX())/2,
//                (start.getY()+end.getY())/2);
//    }
//
//    public Pose2d poseToPose2d(@NonNull Pose pose) {
//        return new Pose2d(pose.getX(), pose.getY(), pose.getHeading());
//    }
//
//    public Pose pose2dToPose(@NonNull Pose2d pose2d) {
//        return new Pose(pose2d.getX(), pose2d.getY(), pose2d.getHeading());
//    }
//
//    @NonNull
//    public Point getCurrentPoint(){
//        return new Point(follower.getPose().getX(),follower.getPose().getY());
//    }
}
