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

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;
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

    // Move this to a different helper functions/math file later
    private double RPMtoRPS(double RPM) {
        // RPM = RPS * 60
        return(RPM * 60);

    }

    // Move this to a different helper functions/math file later
    private double RPStoRPM(double RPS) {
        // RPS = RPM/60
        return(RPS/60);
    }

    //shooter.setMotorSpeed(50);
    private void setMotorSpeedRPM(double RPM) {
        // Convert RPM to RPS and then call setMotorSpeedRPS function
        setMotorSpeedRPS(RPMtoRPS(RPM));
    }

    // function to drive both shooter motors at given speed in RPS (rotations persecond)
    private void setMotorSpeedRPS(double RPS) {
        StatusCode code;
        code = shooterMotor1.setControl(Constants.ShooterConstants.kshootermotorVelocity.withVelocity(RPS));
        if(code.equals(StatusCode.OK)) {
            System.out.println("Motor 1 good");
        }
        code = shooterMotor2.setControl(Constants.ShooterConstants.kshootermotorVelocity.withVelocity(RPS));
        if(code.equals(StatusCode.OK)) {
            System.out.println("Motor 2 good");
        }
    }

    public Command spinShooterRPM(double RPM) {
        System.out.print("Spin shooter at ");
        System.out.print(RPM);
        System.out.println("RPM.");
        return runOnce(() -> {setMotorSpeedRPM(RPM);});
    }
}
