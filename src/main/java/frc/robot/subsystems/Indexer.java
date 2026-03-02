package frc.robot.subsystems;

// pheonix6 imports
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.controls.VelocityVoltage;

// wpilib imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleTopic;

// custom file imports
import frc.robot.Constants;
import frc.robot.helpers.Conversions;

public class Indexer extends SubsystemBase {

    // Network tables stuff
    final DoublePublisher speedPub;

    // Add more follower motors as needed
    private final TalonFX indexerMotor;

    public Indexer(TalonFX indexerMotor,  DoubleTopic speedTopic) {
        speedPub = speedTopic.publish();
        speedPub.setDefault(1.0);

        TalonFXConfiguration config = new TalonFXConfiguration();

        config.Slot0.kP = 0.08;
        config.Slot0.kI = 0.0;
        config.Slot0.kD = 0.0;
        config.Slot0.kV = 12;
        config.Slot0.kS = 0.20;

        indexerMotor.getConfigurator().apply(config);

        this.indexerMotor = indexerMotor;
        //this.followerShooterMotor1 = followerShooterMotor1;

        indexerMotor.setNeutralMode(NeutralModeValue.Brake);

        //speedPub.set(5.0);

        //https://v6.docs.ctr-electronics.com/en/latest/docs/migration/migration-guide/control-requests-guide.html
   }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        // Update speed in Network table
        //setMotorSpeedRPS(5);
        speedPub.set(indexerMotor.getVelocity().getValueAsDouble());
    }

    // /**
    //  * Spin the shooter motors at a given speed.
    //  * @param RPS Rotations per Second
    //  */
    // public void spinRoller() {
    //     // Publish the RPS to the network table
    //     hoodMotor.setControl(Constants.IntakeConstants.rollerVelocity);
    // }

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
        indexerMotor.setControl(Constants.IndexerConstants.indexerVelocity.withVelocity(RPS));
    }

    /**
     * Stop the shooter motors.
     */
    public void stopRoller() {
        // Not sure how this works with the followers, experiment a lil
        indexerMotor.stopMotor();
    }
}
