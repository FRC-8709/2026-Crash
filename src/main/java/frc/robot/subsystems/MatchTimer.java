package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MatchTimer extends SubsystemBase {
    Timer teleopTimer = new Timer();
    boolean timerStarted = false;

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Teleop Timer", teleopTimer.get());
        if(DriverStation.isTeleopEnabled() && !timerStarted) {
            teleopTimer.reset();
            teleopTimer.start();
            timerStarted = true;
        }
        if(!DriverStation.isTeleopEnabled()) {
        timerStarted = false;
        teleopTimer.stop();
        }
    }

    public double getTeleopTime() {
        return teleopTimer.get();
    }
}
