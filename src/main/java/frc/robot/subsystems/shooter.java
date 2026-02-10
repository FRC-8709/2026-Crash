package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.helpers.Conversions;

public class shooter extends SubsystemBase {
    
    public final TalonFX shooterMotor1;
    public final TalonFX shooterMotor2;

    // When we make a shooter,
    // Pass it 2 motors
    public shooter(TalonFX shooterMotor1, TalonFX shooterMotor2) {
        // My 2 motors are the motors you just told me to use
        // this.
        this.shooterMotor1 = shooterMotor1;
        this.shooterMotor2 = shooterMotor2;

        // I am going to start the motors in neutral mode
        shooterMotor1.setNeutralMode(NeutralModeValue.Coast);
        shooterMotor2.setNeutralMode(NeutralModeValue.Coast);
    }

    //shooter.setMotorSpeed(50);
    private void spinMotorSpeedRPM(double RPM) {
        // Convert RPM to RPS and then call setMotorSpeedRPS function
        spinMotorSpeedRPS(Conversions.RPStoRPM(RPM));
    }

    // function to drive both shooter motors at given speed in RPS (rotations persecond)
    private void spinMotorSpeedRPS(double RPS) {
        shooterMotor1.setControl(Constants.ShooterConstants.kshootermotorVelocity.withVelocity(RPS));
        //shooterMotor2.setControl(Constants.ShooterConstants.kshootermotorVelocity.withVelocity(RPS));
    }

    public Command setMotorSpeedRPM(double RPM) {
        return runOnce(() -> {spinMotorSpeedRPM(RPM);});
    }
}
