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
private PoseEst poseEst;
   
    public MatchStartup(Pigeon2 gyro, PoseEst poseEst){
        this.gyro = gyro;
        this.poseEst = poseEst;
    }

    @Override
    public void periodic() {
        // While the robot is disabled,
        // THIS MIGHT NEED TO BE CHANGED!
        // I believe (but I could be wrong!!!) the robot is disabled:
        //      - before auton
        //      - between auton and teleop
        //      - after teleop
        // We need to make sure this code only runs BEFORE AUTON otherwise it'll reset wherever the robot ends at the end of the pathplanner auton
        // DriverStation has a "DriverStation.isAutonomous()" so you might be able to use that, or you might have to do something else, I'm not sure

        if(DriverStation.isDisabled()){
            // Reset the gyro to assume we are facing either 0 degrees (blue alliance) or 180 degrees (red alliance)
            gyro.setYaw(AllianceInfo.getAllianceRotation().getDegrees());

            // Put the limelight's internal IMU to mode 1, which essentially says "listen to the pigeon and figure out which way you are facing"
            LimelightHelpers.SetIMUMode("limelight", 1); // Seed internal IMU

            // Call the resetBotPose function which resets the current pose to whatever MT1 thinks the pose is, USING THE GYRO YAW AND NOT THE CURRENT ROBOT ANGLE
            // When we call the code in the actual PoseEst periodic, it uses the current bot pose as the angle because after it is in motion, it keeps decent track
            // We could theoretically change that to use the gyro too (which is pretty easy and might be a good idea) but test it like this for now
            poseEst.resetBotPose();
        }
    }
}
