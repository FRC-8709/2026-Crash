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

        // Hi, Ethan here leaving notes for until I get there

        // We want to use either Remote CANcoder or Fused CANcoder

        // Remote: ignore rotor, just use external CANcoder for feedback
        //      We need: "sensor to mechanism" ratio (usually just 1 in this case)
        
        // Fused: use rotor internal encoder and external CANcoder, requires us to
        //      We need: "rotor to sensor" ratio (motor to shaft CANcoder is on) and "sensor to mechanism" ratio (usually just 1 in this case)

        // Ideally we use Fused, but it requires a little more effort:
        
        // Here is the link to the docs:
        // https://v6.docs.ctr-electronics.com/en/latest/docs/api-reference/device-specific/talonfx/remote-sensors.html

        // ALSO PLEASE LOOK AT MY NOTES FOR "GO TO POSITION" FUNCTION
        // THAT IS **NOT**, I REPEAT *****NOT***** THE WAY IT SHOULD BE WRITTEN
        // LOVE YOU GUYS TO DEATH BUT YOU ARE STUCK ON HOW TIME BASED PROGRAMMING WORKS, WE CAN'T
        // DO STUFF LIKE THAT IN COMMAND BASE

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

    // THIS IS **NOT**, I REPEAT *****NOT***** THE WAY IT SHOULD BE WRITTEN
    // LOVE YOU GUYS TO DEATH BUT YOU ARE STUCK ON HOW TIME BASED PROGRAMMING WORKS, WE CAN'T
    // DO STUFF LIKE THIS IN COMMAND BASE

    // INSTEAD, WE USE PID LOOPS WHICH, DESPITE THE NAME, ARE NOT WHILE OR FOR LOOPS!
    // WE CANNOT HOLD THE CODE HOSTAGE HERE UNTIL WE LET IT OUT OF THE LOOP, THE ROBOT WILL FREAK OUT
    // INSTEAD, THE KRAKENS ARE SMART ENOUGH TO HAVE THEIR OWN BUILT IN PID LOOPS, WE JUST GIVE THEM NUMBERS (KP, KI, KD, etc.) AND THEY
    // CALCULATE WHAT THEY NEED TO DO TO GET TO A CERTAIN SPEED/POSITION

    // PLEASE USE setControl AND PositionVoltage REQUESTS, THAT IS THE ONLY WAY WE SHOULD BE DOING THIS
    // link to docs so you guys can see their example, we had a hard time getting it to work last week
    // but you should double check this, because this is quite literally the only CORRECT way to do this
    // https://v6.docs.ctr-electronics.com/en/latest/docs/api-reference/device-specific/talonfx/basic-pid-control.html#position-control

    // I have a feeling that our PID numbers were just garbage for doing positional control, TRY THEIR EXAMPLE NUMBERS AND SEE IF THAT FIXES IT
    // Hopefully it does, otherwise keep looking into it until I get there, but I PLEAD WITH YOU DO NOT CONTINUE WITH THIS WHILE LOOP THING

    public void goToPosition(double targetPos) {
        currentPos = hoodEncoder.getAbsolutePosition().getValueAsDouble();
        while(currentPos > targetPos) {
            
        }
    }
}
