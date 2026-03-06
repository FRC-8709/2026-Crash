package frc.robot.subsystems;

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
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleTopic;
import edu.wpi.first.units.AngleUnit;
import edu.wpi.first.units.Units;

//Limelight imports
import frc.robot.LimelightHelpers;

// custom imports
import frc.robot.Constants;
import frc.robot.helpers.Conversions;


public class PoseEst extends SubsystemBase {

    private CommandSwerveDrivetrain drivetrain;
    private Pigeon2 gyro;
    private boolean doRejectUpdate = false;

    
    private Pose2d getPose2d(){
        return drivetrain.getState().Pose;
    }

    private double getRotation(){
        return getPose2d().getRotation().getDegrees();
    }

    public PoseEst(CommandSwerveDrivetrain drivetrain, Pigeon2 gyro) {
        this.drivetrain = drivetrain;
        this.gyro = gyro;
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
}
