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

        .mass(14)
        .forwardZeroPowerAcceleration(-33)
        .lateralZeroPowerAcceleration(-63)
        .translationalPIDFCoefficients(new PIDFCoefficients(0.053, 0, 0.003, 0.02))
        .headingPIDFCoefficients(new PIDFCoefficients(0.6, 0, 0.01, 0.02))
        .drivePIDFCoefficients(new FilteredPIDFCoefficients(1,0.0,0.015,0.6,0.019))
        .centripetalScaling(0.0006);

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1.5
            , 2.5);

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
            .rightRearMotorDirection(DcMotorEx.Direction.FORWARD)
            .xVelocity(82.28)
            .yVelocity(74.57);

    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(0)
            .strafePodX(-3.4375)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("pinpoint")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);
}
