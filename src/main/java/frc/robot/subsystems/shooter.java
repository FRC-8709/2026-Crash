// package frc.robot.subsystems;

// import com.ctre.phoenix6.hardware.TalonFX;
// import com.ctre.phoenix6.signals.NeutralModeValue;

// import edu.wpi.first.wpilibj2.command.SubsystemBase;
// import frc.robot.Constants;

// public class shooter extends SubsystemBase {
    
//     public final TalonFX shooterMotor;

//     public shooter(TalonFX shooterMotor) {
//         this.shooterMotor = shooterMotor;
//         shooterMotor.setNeutralMode(NeutralModeValue.Brake);
//     }

//     public void setMotorSpeed(double speed) {
//         shooterMotor.setControl(Constants.ShooterConstants.kshootermotorVoltageOut.withOutput(speed));
//     }
// }


package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

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
    public void setMotorSpeed(double speed) {
        // move motor "speed"
        shooterMotor1.setControl(Constants.ShooterConstants.kshootermotorVoltageOut.withOutput(speed));
        shooterMotor2.setControl(Constants.ShooterConstants.kshootermotorVoltageOut.withOutput(-speed));
    }
}