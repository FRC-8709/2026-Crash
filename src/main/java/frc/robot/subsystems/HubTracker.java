package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class HubTracker extends SubsystemBase {

    public enum HubState {
        Red,
        Blue,
        Both;
    }

    private HubState activeHub = HubState.Blue;
    private boolean redInactiveFirst;
    private String gameData;
    
    public HubTracker() {

    }

    @Override
    public void periodic() {
        if(gameData.isEmpty()) {
            gameData = DriverStation.getGameSpecificMessage();
        } else {
            if(gameData.equals("R")) {
                redInactiveFirst = true;
            } else if(gameData.equals("B")) {
                redInactiveFirst = false;
            }
        }
        Alliance alliance = DriverStation.getAlliance().orElse(Alliance.Red);
        SmartDashboard.putString("Alliance", alliance.toString());
        SmartDashboard.putBoolean("Is Hub Active", isHubActive());
    }

    public boolean isHubActive() {
        if(DriverStation.getAlliance().orElse(Alliance.Red).toString().equals(activeHub.toString())) {
            return true;
        } else if(activeHub.toString().equals("Both")) {
            return true;
        } else {
            return false;
        }
    }
}
