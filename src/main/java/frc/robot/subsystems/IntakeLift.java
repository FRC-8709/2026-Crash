package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
// pheonix6 imports
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

// wpilib imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleTopic;
import edu.wpi.first.units.AngleUnit;
import edu.wpi.first.units.Units;
// custom imports
import frc.robot.Constants;

// Lift motor is the motor responsible for moving the intake in/out of the robot
// Lift motor should be tuned positionally like an arm, so it can spin to certain positions

public class IntakeLift extends SubsystemBase {
    //Publisher
    private final DoublePublisher positionPub;

    private final TalonFX liftMotor;
    private final CANcoder liftEncoder;

    // Ideally, we would have nice endstops with limit switches, but we are not so fortunate
    // This is the programmer's burden, work a miracle with what you are given because
    // the design team doesn't want to make your life easier

    // Instead, we are just gonna use PositionVoltage, which just means we can give the motor a position to turn to
    // This uses the RELATIVE ENCODERS built into the Krakens, so YOU MUST RESET THE INTAKE BEFORE YOU START THE ROBOT EACH TIME
    // THIS IS VERY IMPORTANT

    public IntakeLift(TalonFX liftMotor, CANcoder liftEncoder, DoubleTopic positionTopic) {
        positionPub = positionTopic.publish(); 
        positionPub.setDefault(0.0);

        TalonFXConfiguration liftConfig = new TalonFXConfiguration();
        CANcoderConfiguration CANcoderConfig = new CANcoderConfiguration();        

        // Dummy numbers! Please tune!
        // Tune like arm for POSITION CONTROL (PositionVoltage)
        liftConfig.Slot0.kP = 0.3;
        liftConfig.Slot0.kI = 0.0;
        liftConfig.Slot0.kD = 0.05;
        liftConfig.Slot0.kV = 0.15;
        liftConfig.Slot0.kS = 0.0;
        CANcoderConfig.MagnetSensor.SensorDirection = SensorDirectionValue.Clockwise_Positive;

        liftMotor.getConfigurator().apply(liftConfig);
        liftEncoder.getConfigurator().apply(CANcoderConfig);

        this.liftMotor = liftMotor;
        this.liftEncoder = liftEncoder;
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        // Update speed in Network table
        //setMotorSpeedRPS(5);
        positionPub.set(liftEncoder.getAbsolutePosition().getValueAsDouble());
    }

    /**
     * Lower the intake, extend it out over the bumpers.
     * This lets it pick up fuel, YOU SHOULD NOT SPIN THE ROLLER WITHOUT DOING THIS!
     */
    public void lowerLift() {
        // THIS WORKS WITH WHOLE NUMBERS BUT NOT DECIMALS FOR SOME REASON??? GOING TO LOSE MY MIND
        liftMotor.setControl(Constants.IntakeConstants.liftPosition.withPosition(-0.5));
    }

    /**
     * Bring the intake back up into the robot.
     * THE INTAKE SHOULD BE STOPPED ALREADY!
     */
    public void raiseLift() {
        liftMotor.setControl(Constants.IntakeConstants.liftPosition.withPosition(0.5));
    }
}
