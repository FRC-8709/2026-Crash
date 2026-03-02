package frc.robot.subsystems;

// pheonix6 imports
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.StrictFollower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

// wpilib imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleTopic;

// custom file imports
import frc.robot.Constants;
import frc.robot.helpers.Conversions;

public class Shooter extends SubsystemBase {

    // Network tables stuff
    final DoublePublisher speedPub;

    // Add more follower motors as needed
    private final TalonFX leaderShooterMotor, followerShooterMotor1, followerShooterMotor2, followerShooterMotor3;

    public Shooter(TalonFX leaderShooterMotor, TalonFX followerShooterMotor1, TalonFX followerShooterMotor2, TalonFX followerShooterMotor3 , DoubleTopic speedTopic) {
        speedPub = speedTopic.publish();
        speedPub.setDefault(0.0);

        TalonFXConfiguration config = new TalonFXConfiguration();

        config.Slot0.kP = 0.08;
        config.Slot0.kI = 0.0;
        config.Slot0.kD = 0.0;
        config.Slot0.kV = 12;
        config.Slot0.kS = 0.20;

        leaderShooterMotor.getConfigurator().apply(config);

        this.leaderShooterMotor = leaderShooterMotor;
        this.followerShooterMotor1 = followerShooterMotor1;
        this.followerShooterMotor2 = followerShooterMotor2;
        this.followerShooterMotor3 = followerShooterMotor3;
        leaderShooterMotor.setNeutralMode(NeutralModeValue.Coast);
        followerShooterMotor1.setControl(new Follower(leaderShooterMotor.getDeviceID(),  MotorAlignmentValue.Aligned ));
        followerShooterMotor2.setControl(new Follower(leaderShooterMotor.getDeviceID(), MotorAlignmentValue.Opposed ));
        followerShooterMotor3.setControl(new Follower(leaderShooterMotor.getDeviceID(),  MotorAlignmentValue.Opposed ));

        //https://v6.docs.ctr-electronics.com/en/latest/docs/migration/migration-guide/control-requests-guide.html
   }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        // Update speed in Network table
        speedPub.set(leaderShooterMotor.getVelocity().getValueAsDouble());
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
        leaderShooterMotor.setControl(Constants.ShooterConstants.shooterVelocity.withVelocity(RPS));
    }

    /**
     * Stop the shooter motors.
     */
    public void stopMotors() {
        // Not sure how this works with the followers, experiment a lil
        leaderShooterMotor.stopMotor();
    }
}
