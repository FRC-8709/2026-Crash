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
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleTopic;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import frc.robot.Constants;

public class shooter extends SubsystemBase {

    // Network tables stuff
    final DoublePublisher speedPub;

    // Normal stuff
    
    public final TalonFX shooterMotor1;
    public final TalonFX shooterMotor2;

    private final VelocityVoltage velocityRequest = new VelocityVoltage(0);

    // When we make a shooter,
    // Pass it 2 motors
    public shooter(TalonFX shooterMotor1, TalonFX shooterMotor2, DoubleTopic speedTopic) {
        speedPub = speedTopic.publish();
        speedPub.setDefault(0.0);

        TalonFXConfiguration config = new TalonFXConfiguration();

        config.Slot0.kP = 0.08;
        config.Slot0.kI = 0.0;
        config.Slot0.kD = 0.0;
        config.Slot0.kV = 12;
        config.Slot0.kS = 0.20;

        shooterMotor1.getConfigurator().apply(config);

        // My 2 motors are the motors you just told me to use
        // this.
        this.shooterMotor1 = shooterMotor1;
        this.shooterMotor2 = shooterMotor2;

        // I am going to start the motors in neutral mode
        shooterMotor1.setNeutralMode(NeutralModeValue.Coast);
        //shooterMotor2.setNeutralMode(NeutralModeValue.Coast);
    }

      @Override
    public void periodic() {
        // This method will be called once per scheduler run
        //setRPS(20);
        //setMotorSpeedRPM(30);
        speedPub.set(shooterMotor1.getVelocity().getValueAsDouble());
    }

    /*
    public void setRPS(double rps) {
        shooterMotor1.setControl(velocityRequest.withVelocity(rps));
    }
    */

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
    public void setMotorSpeedRPM(double RPM) {
        // Convert RPM to RPS and then call setMotorSpeedRPS function
        setMotorSpeedRPS(RPMtoRPS(RPM));
    }

    // function to drive both shooter motors at given speed in RPS (rotations persecond)
    private void setMotorSpeedRPS(double RPS) {
        // Publish the RPS to the network table
        speedPub.set(RPS);
        shooterMotor1.setControl(velocityRequest.withVelocity(RPS));

        /*
        StatusCode code;
        code = shooterMotor1.setControl(Constants.ShooterConstants.kshootermotorVelocity.withVelocity(RPS));
        if(code.equals(StatusCode.OK)) {
            System.out.println("Motor 1 good");
        }
        
        code = shooterMotor2.setControl(Constants.ShooterConstants.kshootermotorVelocity.withVelocity(RPS));
        if(code.equals(StatusCode.OK)) {
            System.out.println("Motor 2 good");
        }
        */
    }
    
    /*
    public Command spinShooterRPM(double RPM) {
        System.out.print("Spin shooter at ");
        System.out.print(RPM);
        System.out.println("RPM.");
        //speedPub.set(RPM);
        return this.runOnce(() -> {setMotorSpeedRPM(RPM);});
    }
     */
}
