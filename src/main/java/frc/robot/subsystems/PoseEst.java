package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Meter;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
// pheonix6 imports
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.wpilibj2.command.Command;
// wpilib imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleTopic;
import edu.wpi.first.units.AngleUnit;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;

// custom imports
import frc.robot.Constants;
import frc.robot.helpers.Conversions;
import frc.robot.helpers.LimelightHelpers;


public class PoseEst extends SubsystemBase {

    private CommandSwerveDrivetrain drivetrain;
    private Pigeon2 gyro;
    private boolean doRejectUpdate = false;

    //Limeligt positions
    private DoublePublisher distancePub, posXPub, posYPub;

    public PoseEst(CommandSwerveDrivetrain drivetrain, Pigeon2 gyro, DoubleTopic distancePos, DoubleTopic posX, DoubleTopic posY) {
        this.drivetrain = drivetrain;
        this.gyro = gyro;

        distancePub = distancePos.publish();
        posXPub = posX.publish();
        posYPub = posY.publish();

        distancePub.setDefault(0.0);
        posXPub.setDefault(0.0);
        posYPub.setDefault(0.0);
    }

    @Override
    public void periodic() {
        LimelightHelpers.SetRobotOrientation("limelight", getRotation(), 0, 0, 0, 0, 0);
        LimelightHelpers.PoseEstimate mt2 = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2("limelight");
   
        // if our angular velocity is greater than 360 degrees per second, ignore vision updates
        if(Math.abs(gyro.getAngularVelocityZDevice().getValueAsDouble()) > 360)
        {
            doRejectUpdate = true;
        }
        if(mt2.tagCount == 0)
        {
            doRejectUpdate = true;
        }
        if(!doRejectUpdate)
        {
            drivetrain.setVisionMeasurementStdDevs(VecBuilder.fill(.7,.7,9999999));
            drivetrain.addVisionMeasurement(
                mt2.pose,
                mt2.timestampSeconds);
        }
    }
    
    private Pose2d getPose2d(){
        return drivetrain.getState().Pose;
    }

    private double getRotation(){
        return getPose2d().getRotation().getDegrees();
    }

    /* Publishes these values to network tables
     * posXPub is x distance from (0,0), which is the blue right corner in inches
     * posYPub is the y distance from (0,0) in inches
     * distancePub is the distance (as a straight line!) from (0,0) in inches
     */
    public void LimelightDistance() {
        // Pose2d = translation (position) & rotation
        // We just want the translation for this so I'll pulling that out directly
        Translation2d translation;
        double x, y, distance;

        // Get just the translation
        translation = getPose2d().getTranslation();

        // Find x and y
        x = translation.getMeasureX().in(Inches);
        y = translation.getMeasureY().in(Inches);

        // How far are we from the origin? Need to make a new translation at (0,0) for it to find out
        // This returns a double and not a measurement like the other ones do
        // So not sure if this is accurate or what units it is in, we might end up calculating the distance ourself
        // if we want it to be accurate, cause I think the default is meters
        // Or maybe not who cares, life is precious and time is fleeting so do whatever you want
        distance = translation.getDistance(new Translation2d(0,0));

        // I'm just gonna show you how to calculate it yourself IF YOU WANTED TO KNOW
        // Of course we have a right triangle here so our distance is "d = sqrt(x^2 + y^2)" which is this in java:
        //distance = Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
        // sqrt is of course square root
        // pow is exponents, so in this case its Math.pow(a,b) = a^b, or Math.pow(x,2) = x^2
        // That line actually works so if you want to use that instead just uncomment it

        // Publish the values
        posXPub.set(x);
        posYPub.set(y);
        distancePub.set(distance);
    }

    private Translation3d getDistanceFromGoal() {
        Translation2d robotPosition2d;
        Translation3d robotPosition3d, distanceFromGoal;

        // Get the 2d position
        robotPosition2d = getPose2d().getTranslation();
        // Now go up by shooterHeight inches
        robotPosition3d = new Translation3d(robotPosition2d).plus(new Translation3d(Inches.of(0), Inches.of(0), Constants.ShooterConstants.shooterHeight));

        // Find the difference between the robot and the goal
        // Might have this backwards, check later
        return Constants.FieldConstants.goalPosition.minus(robotPosition3d);
    }

    /* Function to calculate the launch angle of the ball based on the distance and current flywheel velocity
     * Ideally we actually give a number here but I'll hardcode it just because
     * https://en.wikipedia.org/wiki/Projectile_motion#Angle_%CE%B8_required_to_hit_coordinate_(x,_y)
     */
    public Angle calculateLaunchAngle() {
        // I'm going to break this up into a couple smaller pieces so the math doesn't look gross
        double x, y, z;
        double a, b, c;
        double angleInRadians;
        Angle launchAngle;

        // Gravity in m/s^2, gonna do all of the math in m/s^2
        double g = 9.81;

        // Need to find distance between where we are now and the goal
        Translation3d translationFromGoal = getDistanceFromGoal();
        x = translationFromGoal.getMeasureX().in(Meter);
        y = translationFromGoal.getMeasureY().in(Meter);
        z = translationFromGoal.getMeasureZ().in(Meter);

        // The math needs the DISTANCE between the robot and the goal, in terms of horizontal (hypotenuse of x and y) and vertical distance (z)
        double horizontal_distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));

        // This comes up a couple times so just declaring it here
        double velocity_squared = Math.pow(Constants.ShooterConstants.shooterSpeed, 2);
        

        // For the math, x is actually the horizontal distance, and y is z
        // I know this is confusing but the wikipedia equation was written for 2d and not 3d
        // I'm making do with what I got here ok

        // a = g^2*x^2
        // b = 2*g*y*v^2
        a = Math.pow(g, 2) * Math.pow(horizontal_distance, 2);
        b = 2 * g * z * velocity_squared;

        c = (velocity_squared + Math.sqrt(Math.pow(velocity_squared, 2) - (a + b)))/(g*horizontal_distance);

        // Angle in radians
        angleInRadians = Math.atan(c);
        launchAngle = Units.Radians.of(angleInRadians);

        // If we return it as an angle, we can get it in degrees or radians
        return launchAngle;
    }
}
