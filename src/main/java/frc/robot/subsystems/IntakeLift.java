package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
// pheonix6 imports
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.SensorDirectionValue;

// wpilib imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleTopic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// custom imports
import frc.robot.Constants;
import frc.robot.helpers.Conversions;

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
        
        TalonFXConfiguration motorConfig = new TalonFXConfiguration();
        TalonFXConfiguration liftConfig = new TalonFXConfiguration();
        CANcoderConfiguration CANcoderConfig = new CANcoderConfiguration();        

        // Dummy numbers! Please tune!
        // Tune like arm for POSITION CONTROL (PositionVoltage)
        motorConfig.Slot0.kP = 0.15;
        motorConfig.Slot0.kI = 0.0;
        motorConfig.Slot0.kD = 0.0;
        motorConfig.Slot0.kV = 12;
        motorConfig.Slot0.kS = 0.20;
        CANcoderConfig.MagnetSensor.SensorDirection = SensorDirectionValue.Clockwise_Positive;



        liftConfig.Slot0.kP = 0.3;
        liftConfig.Slot0.kI = 0.0;
        liftConfig.Slot0.kD = 0.05;
        //liftConfig.Slot0.kV = 0.15;
        //liftConfig.Slot0.kS = 0.0;
        CANcoderConfig.MagnetSensor.SensorDirection = SensorDirectionValue.Clockwise_Positive;

        liftMotor.getConfigurator().apply(motorConfig);
        liftEncoder.getConfigurator().apply(CANcoderConfig);

        this.liftMotor = liftMotor;
        this.liftEncoder = liftEncoder;
        // liftMotor.setPosition(0);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        // Update speed in Network table
        //setMotorSpeedRPS(5);
        SmartDashboard.putNumber("lift value", liftMotor.getPosition().getValueAsDouble());
        positionPub.set(liftEncoder.getAbsolutePosition().getValueAsDouble());
    }

     /**
     * Spin the shooter motors at a given speed.
     * @param RPM Rotations per Minute
     */
    public void setMotorSpeedRPM(double RPM) {
        // Convert RPM to RPS and then call setMotorSpeedRPS function
        setMotorSpeedRPS(Conversions.RPMtoRPS(RPM));
    }

    /**
     * Spin the shooter motors at a given speed.
     * @param RPS Rotations per Second
     */
    private void setMotorSpeedRPS(double RPS) {
        // Publish the RPS to the network table
        liftMotor.setControl(Constants.IntakeConstants.liftVelocity.withVelocity(RPS));
    }

    /**
     * Stop the shooter motors.
     */
    public void stopLift() {
        liftMotor.setControl(Constants.IntakeConstants.liftVelocity.withVelocity(0));
        
    }
    /**
     * Lower the intake, extend it out over the bumpers.
     * This lets it pick up fuel, YOU SHOULD NOT SPIN THE ROLLER WITHOUT DOING THIS!
     */
   public void lowerLift() {
        // THIS WORKS WITH WHOLE NUMBERS BUT NOT DECIMALS FOR SOME REASON??? GOING TO LOSE MY MIND
        // liftMotor.setControl(Constants.IntakeConstants.liftPosition.withPosition(Constants.IntakeConstants.downPosition));
        liftMotor.setControl(Constants.IntakeConstants.liftPosition.withPosition(Constants.IntakeConstants.intakeOutPosition));
    }

    /**
     * Bring the intake back up into the robot.
     * THE INTAKE SHOULD BE STOPPED ALREADY!
     */
    public void raiseLift() {
        // liftMotor.setControl(Constants.IntakeConstants.liftPosition.withPosition(Constants.IntakeConstants.upPosition));
        // liftMotor.setControl(Constants.IntakeConstants.liftPosition.withPosition(Constants.IntakeConstants.middlePosition));
        liftMotor.setControl(Constants.IntakeConstants.liftPosition.withPosition(Constants.IntakeConstants.intakeMidPosition));

    }

    public void raiseLiftMiddle() {
        // liftMotor.setControl(Constants.IntakeConstants.liftPosition.withPosition(Constants.IntakeConstants.middlePosition));
        liftMotor.setControl(Constants.IntakeConstants.liftPosition.withPosition(Constants.IntakeConstants.intakeMidPosition));


    }

    public boolean isLowered() {
        return (liftMotor.getPosition().getValueAsDouble() < -40.0f);
    }
}
