package frc.robot.subsystems;

// pheonix6 imports
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

// wpilib imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.DoubleEntry;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.DoubleTopic;

// custom file imports
import frc.robot.Constants;

public class IntakeRoller extends SubsystemBase {

    // Network tables stuff
    final DoublePublisher speedPub;
    final DoubleEntry rollerKP;
    final DoubleEntry rollerKV;

    // Add more follower motors as needed
    private final TalonFX rollerMotor;

    TalonFXConfiguration config = new TalonFXConfiguration();

    public void updatePIDValues(){
        config.Slot0.kP = rollerKP.get();
        config.Slot0.kV = rollerKV.get();
        
        config.Slot0.kI = 0.0;
        config.Slot0.kD = 0.0;
        config.Slot0.kS = 0.0;

        rollerMotor.getConfigurator().apply(config);
    }

    public IntakeRoller(TalonFX rollerMotor,  DoubleTopic speedTopic, DoubleTopic rollerKPTopic, DoubleTopic rollerKVTopic ) {
        rollerKP = rollerKPTopic.getEntry(0);
        rollerKV = rollerKVTopic.getEntry(0);



        //.01
        //.55
        rollerKP.set(0.01);
        rollerKV.set(0.55);

        
        
        speedPub = speedTopic.publish();
        speedPub.setDefault(1.0);

        this.rollerMotor = rollerMotor;
        //this.followerShooterMotor1 = followerShooterMotor1;

        rollerMotor.setNeutralMode(NeutralModeValue.Coast);

        //speedPub.set(5.0);
        updatePIDValues();
        //https://v6.docs.ctr-electronics.com/en/latest/docs/migration/migration-guide/control-requests-guide.html
   }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        // Update speed in Network table
        //setMotorSpeedRPS(5);
        speedPub.set(rollerMotor.getVelocity().getValueAsDouble());
    }

    /**
     * Spin the shooter motors at a given speed.
     * @param RPS Rotations per Second
     */
    public void spinRoller() {
        // Publish the RPS to the network table
        rollerMotor.setControl(Constants.IntakeConstants.rollerVelocity);
    }

    /**
     * Stop the shooter motors.
     */
    public void stopRoller() {
        // Not sure how this works with the followers, experiment a lil
        rollerMotor.stopMotor();
    }
}
