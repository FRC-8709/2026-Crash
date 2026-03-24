package frc.robot.subsystems;

import static edu.wpi.first.units.Units.*;

import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.ZoneTracking.FieldZones;

public class ScoringControl extends SubsystemBase {
    Alliance alliance = DriverStation.getAlliance().orElse(Alliance.Red);
    private boolean isScoring = false;

    ZoneTracking zones;
    PoseEst poseEst;

    public ScoringControl(ZoneTracking zones, PoseEst poseEst) {
        this.zones = zones;
        this.poseEst = poseEst;
    }

    @Override
    public void periodic() {
        alliance = DriverStation.getAlliance().orElse(Alliance.Red);
        SmartDashboard.putBoolean("Can Score", canScore());
    }

    public boolean canScore() {
        if(alliance.equals(Alliance.Blue)) {
            if(zones.currentZone() == FieldZones.BlueAllianceZone && Units.Meters.of(poseEst.getDistanceFromGoal(Constants.FieldConstants.blueHubPosition).toTranslation2d().getNorm()).in(Inches) <= Constants.ScoringConstants.maxDistance) {
                return true;
            } else {
                return false;
            }
        } else if(alliance.equals(Alliance.Red)) {
            if(zones.currentZone() == FieldZones.RedAllianceZone && Units.Meters.of(poseEst.getDistanceFromGoal(Constants.FieldConstants.redHubPosition).toTranslation2d().getNorm()).in(Inches) <= Constants.ScoringConstants.maxDistance) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
