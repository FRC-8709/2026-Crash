package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
// pheonix6 imports
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.VelocityVoltage;

// wpilib imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleTopic;

// custom file imports
import frc.robot.Constants;
import frc.robot.helpers.Conversions;

public class Hood extends SubsystemBase {

    // Network tables stuff
    final DoublePublisher speedPub;
    final DoublePublisher posPub;

    //Motor('s)
    private final TalonFX hoodMotor;
    private final CANcoder hoodEncoder;

    private double currentPos;

    public Hood(TalonFX hoodMotor,  DoubleTopic speedTopic, CANcoder hoodEncoder,  DoubleTopic posTopic) {
        speedPub = speedTopic.publish();
        speedPub.setDefault(0.0);
        posPub = posTopic.publish();
        posPub.setDefault(0.0);

        TalonFXConfiguration motorConfig = new TalonFXConfiguration();
        CANcoderConfiguration CANcoderConfig = new CANcoderConfiguration();

        motorConfig.Slot0.kP = 0.08;
        motorConfig.Slot0.kI = 0.0;
        motorConfig.Slot0.kD = 0.0;
        motorConfig.Slot0.kV = 12;
        motorConfig.Slot0.kS = 0.20;
        CANcoderConfig.MagnetSensor.SensorDirection = SensorDirectionValue.Clockwise_Positive;

        hoodMotor.getConfigurator().apply(motorConfig);
        hoodEncoder.getConfigurator().apply(CANcoderConfig);

        this.hoodMotor = hoodMotor;
        this.hoodEncoder = hoodEncoder;

        hoodMotor.setNeutralMode(NeutralModeValue.Brake);

        //https://v6.docs.ctr-electronics.com/en/latest/docs/migration/migration-guide/control-requests-guide.html
   }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        // Update speed in Network table
        //setMotorSpeedRPS(5);
        speedPub.set(hoodMotor.getVelocity().getValueAsDouble());
        posPub.set(hoodEncoder.getAbsolutePosition().getValueAsDouble());
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
        hoodMotor.setControl(Constants.HoodConstants.hoodVelocity.withVelocity(RPS));
    }

    /**
     * Stop the shooter motors.
     */
    public void stopHood() {
        hoodMotor.setControl(Constants.HoodConstants.hoodVelocity.withVelocity(0));
        
    }

    // spin to position
    public void goToPosition(double targetPos) {
        currentPos = hoodEncoder.getAbsolutePosition().getValueAsDouble();
        while(currentPos > targetPos) {
            
        }
    }
}
