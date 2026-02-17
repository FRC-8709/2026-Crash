package frc.robot.subsystems;

// pheonix6 imports
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

// wpilib imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleTopic;

// custom imports
import frc.robot.Constants;

// Roller motor is the motor that controls the intake roller
// Roller motor should be tuned like the flywheel, to maintain a semi-constant RPM

public class IntakeRoller extends SubsystemBase {

    // Network tables stuff
    final DoublePublisher speedPub;
    
    private final TalonFX rollerMotor;

    public IntakeRoller(TalonFX rollerMotor, DoubleTopic speedTopic) {
        speedPub = speedTopic.publish();
        speedPub.setDefault(0.0);

        TalonFXConfiguration rollerConfig = new TalonFXConfiguration();

        // Dummy numbers! Please tune!
        // Tune like flywheel for SPEED CONTROL (VelocityVoltage)
        rollerConfig.Slot0.kP = 0.08;
        rollerConfig.Slot0.kI = 0.0;
        rollerConfig.Slot0.kD = 0.0;
        rollerConfig.Slot0.kV = 12;
        rollerConfig.Slot0.kS = 0.20;

        rollerMotor.getConfigurator().apply(rollerConfig);

        rollerMotor.setNeutralMode(NeutralModeValue.Coast);
    
        this.rollerMotor = rollerMotor;
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        }

    /**
     * Spin the intake roller to pick up "fuel."
     * MAKE SURE THE INTAKE IS DOWN!
     */
    public void spinRoller() {
        // Aim for a target speed with PID, speed value in Constants file
        //rollerMotor.setControl(Constants.IntakeConstants.rollerVelocity.withVelocity(50));
        rollerMotor.setControl(Constants.ShooterConstants.velocityRequest.withVelocity(50));
        speedPub.set(50);
    }

    /**
     * Stop spinning the intake roller.
     */
    public void stopRoller() {
        // Stop the motor
        rollerMotor.stopMotor();
    }
}

