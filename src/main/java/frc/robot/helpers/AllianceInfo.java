package frc.robot.helpers;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class AllianceInfo {

    public static Alliance getAlliance() {
        return DriverStation.getAlliance().get();
    }

    public static Rotation2d getAllianceRotation() {
        Alliance ourAlliance = getAlliance();

        // Are we red?
        if(ourAlliance == Alliance.Red) {
            // Return 180 degrees
            return Rotation2d.k180deg;
        }
        // We must be blue then
        else {
            // Return 0 degrees
            return Rotation2d.kZero;
        }
        
    }
}