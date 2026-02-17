package frc.robot.subsystems;

// pheonix6 imports
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

// wpilib imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// custom imports
import frc.robot.Constants;

// Lift motor is the motor responsible for moving the intake in/out of the robot
// Lift motor should be tuned positionally like an arm, so it can spin to certain positions

public class IntakeLift extends SubsystemBase {
    
    private final TalonFX liftMotor;

    // Ideally, we would have nice endstops with limit switches, but we are not so fortunate
    // This is the programmer's burden, work a miracle with what you are given because
    // the design team doesn't want to make your life easier

    // Instead, we are just gonna use PositionVoltage, which just means we can give the motor a position to turn to
    // This uses the RELATIVE ENCODERS built into the Krakens, so YOU MUST RESET THE INTAKE BEFORE YOU START THE ROBOT EACH TIME
    // THIS IS VERY IMPORTANT

    public IntakeLift(TalonFX liftMotor) {
        this.liftMotor = liftMotor;

        TalonFXConfiguration liftConfig = new TalonFXConfiguration();

        // Dummy numbers! Please tune!
        // Tune like arm for POSITION CONTROL (PositionVoltage)
        liftConfig.Slot0.kP = 0.08;
        liftConfig.Slot0.kI = 0.0;
        liftConfig.Slot0.kD = 0.0;
        liftConfig.Slot0.kV = 12;
        liftConfig.Slot0.kS = 0.20;

        liftMotor.getConfigurator().apply(liftConfig);
    }

    /**
     * Lower the intake, extend it out over the bumpers.
     * This lets it pick up fuel, YOU SHOULD NOT SPIN THE ROLLER WITHOUT DOING THIS!
     */
    public void lowerLift() {
        liftMotor.setControl(Constants.IntakeConstants.liftDownPosition);
    }

    /**
     * Bring the intake back up into the robot.
     * THE INTAKE SHOULD BE STOPPED ALREADY!
     */
    public void raiseLift() {
        liftMotor.setControl(Constants.IntakeConstants.liftUpPosition);
    }
}
