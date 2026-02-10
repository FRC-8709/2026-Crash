//make motor spin foward and also backword just incase ball gets stuck

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.helpers.Conversions;

public class agitator extends SubsystemBase {

    public agitator(){

    }

    //shooter.setMotorSpeed(50);
    private void spinMotorSpeedRPM(double RPM) {
        // Convert RPM to RPS and then call setMotorSpeedRPS function
        //spinMotorSpeedRPS(Conversions.RPMtoRPS(RPM));
    }

    // function to drive both shooter motors at given speed in RPS (rotations persecond)
    private void spinMotorSpeedRPS(double RPS) {
        // shooterMotor1.setControl(Constants.ShooterConstants.kshootermotorVelocity.withVelocity(RPS));
        //shooterMotor2.setControl(Constants.ShooterConstants.kshootermotorVelocity.withVelocity(RPS));
    }

    public Command setMotorSpeedRPM(double RPM) {
        return runOnce(() -> {spinMotorSpeedRPM(RPM);});
    }

}
