package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
// pheonix6 imports
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.ctre.phoenix6.controls.VelocityVoltage;

// wpilib imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleTopic;

// custom file imports
import frc.robot.Constants;
import frc.robot.helpers.Conversions;

public class HoodSensor extends SubsystemBase {

    // Network tables stuff
    final DoublePublisher posPub;

    // Add more follower motors as needed
    private final CANcoder hoodSensor;

    public HoodSensor(CANcoder hoodSensor,  DoubleTopic posTopic) {
        posPub = posTopic.publish();
        posPub.setDefault(1.0);

        CANcoderConfiguration config = new CANcoderConfiguration();

        config.MagnetSensor.SensorDirection = SensorDirectionValue.Clockwise_Positive;

        hoodSensor.getConfigurator().apply(config);

        this.hoodSensor = hoodSensor;

        //https://v6.docs.ctr-electronics.com/en/latest/docs/migration/migration-guide/control-requests-guide.html
   }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        // Update speed in Network table
        //setMotorSpeedRPS(5);
        posPub.set(hoodSensor.getAbsolutePosition().getValueAsDouble());
    }
}
