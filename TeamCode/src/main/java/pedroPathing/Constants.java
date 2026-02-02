package pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()

            .useSecondaryDrivePIDF(true)
            .useSecondaryHeadingPIDF(true)
            .mass(12.2)
            .forwardZeroPowerAcceleration(-29.3511)
            .lateralZeroPowerAcceleration(-57)
            .translationalPIDFCoefficients(new PIDFCoefficients(0.18, 0, 0.004, 0.017))

            .headingPIDFCoefficients(new PIDFCoefficients(1.23, 0, 0.01, 0.01))

            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.04,0,0.001,0.6,0.05))

            .secondaryDrivePIDFCoefficients(new FilteredPIDFCoefficients(0,0,0.000001,0.6,0.2))
            .secondaryHeadingPIDFCoefficients(new PIDFCoefficients(1,0,0.0000001,0))
            .centripetalScaling(0.000005);

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1.4
            , 1.1);

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
            .rightFrontMotorName("rightFront")
            .rightRearMotorName("rightRear")
            .leftRearMotorName("leftRear")
            .leftFrontMotorName("leftFront")
            .useBrakeModeInTeleOp(true)
            .rightFrontMotorDirection(DcMotorEx.Direction.FORWARD)
            .leftFrontMotorDirection(DcMotorEx.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorEx.Direction.REVERSE)
            .rightRearMotorDirection(DcMotorEx.Direction.REVERSE)
            .xVelocity(81.8421)
            .yVelocity(61.9859);

    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(-3.7785)
            .strafePodX(-1.905)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("pinpoint")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);
}
