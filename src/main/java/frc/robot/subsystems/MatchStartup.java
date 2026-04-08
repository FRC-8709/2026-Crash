package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.helpers.AllianceInfo;
import frc.robot.helpers.LimelightHelpers;
import frc.robot.RobotContainer;
 



public class MatchStartup extends SubsystemBase {
private Pigeon2 gyro;
   
    public MatchStartup(Pigeon2 gyro){
        this.gyro = gyro;

    }

    @Override
    public void periodic() {
        if(DriverStation.isDisabled()){
        gyro.setYaw(AllianceInfo.getAllianceRotation().getDegrees());
        LimelightHelpers.SetIMUMode("limelight", 1); // Seed internal IMU
        LimelightHelpers.SetRobotOrientation("limelight", gyro.getYaw().getValueAsDouble(), 0, 0, 0, 0, 0);
        }
    }
}
