package pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.DriveEncoderConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
        .mass(2.5)
        .forwardZeroPowerAcceleration(-25.7425)
        .lateralZeroPowerAcceleration(-45.1)
        .translationalPIDFCoefficients(new PIDFCoefficients(0.04, 0, 0.005, 0.02))
        .headingPIDFCoefficients(new PIDFCoefficients(0.6, 0, 0.03, 0.03))
        .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.025,0.0,1e-5,0.6,0.01))
        .centripetalScaling(0.005);

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
                /* other builder steps */
                .build();
    }

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("rightFrontMotor")
            .rightRearMotorName("rightBackMotor")
            .leftRearMotorName("leftBackMotor")
            .leftFrontMotorName("leftFrontMotor")
            .xVelocity(87.4514)
            .yVelocity(69.3625);

            //.rightFrontMotorDirection(DcMotorSimple.Direction.REVERSE)


    //不确定odo是不是这个"goBILDA Pinpoint Odometry Computer"
    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(3.5433070866142) //这个要改，应该就是之前测过的odo的offsetx和offsety，单位是inch
            .strafePodX(3.9370078740157)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("odo")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);

}
