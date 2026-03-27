package frc.robot.subsystems;

import static edu.wpi.first.units.Units.*;

// pheonix6 imports
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

// wpilib imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.networktables.DoubleEntry;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleTopic;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// custom file imports
import frc.robot.Constants;
import frc.robot.helpers.Conversions;
import frc.robot.subsystems.ZoneTracking.FieldZones;

public class Shooter extends SubsystemBase {

    // Network tables stuff
    final DoublePublisher speedPub;
    final DoubleEntry shooterKP;
    final DoubleEntry shooterKV;

    // Add more follower motors as needed
    private final TalonFX leaderShooterMotor, followerShooterMotor1, followerShooterMotor2, followerShooterMotor3;
    private final boolean usingAutomaticSpeed = false;
    private final PoseEst poseEst;
    private final ZoneTracking zones;
    boolean isScoring = false;
    double distanceFromGoal;

    TalonFXConfiguration config = new TalonFXConfiguration();

    public void updatePIDValues(){
        // PARKER UNCOMMENT THIS WHEN YOU UPDATE THEM VIA ELASTIC
        //0.08
        config.Slot0.kP = shooterKP.get();
        //12
        config.Slot0.kV = shooterKV.get();

        config.Slot0.kI = 0.0;
        config.Slot0.kD = 0.0;
        config.Slot0.kS = 0.0;

        leaderShooterMotor.getConfigurator().apply(config);
    }

    public Shooter(TalonFX leaderShooterMotor, TalonFX followerShooterMotor1, TalonFX followerShooterMotor2, TalonFX followerShooterMotor3 , DoubleTopic speedTopic, DoubleTopic shooterKPTopic, DoubleTopic shooterKVTopic, PoseEst poseEst, ZoneTracking zones) {
        speedPub = speedTopic.publish();
        speedPub.setDefault(0.0);

        shooterKP = shooterKPTopic.getEntry(0);
        shooterKV = shooterKVTopic.getEntry(0);

        shooterKP.set(0.075);
        shooterKV.set(12);

        this.poseEst = poseEst;
        this.zones = zones;
        this.leaderShooterMotor = leaderShooterMotor;
        this.followerShooterMotor1 = followerShooterMotor1;
        this.followerShooterMotor2 = followerShooterMotor2;
        this.followerShooterMotor3 = followerShooterMotor3;

        updatePIDValues();

        leaderShooterMotor.setNeutralMode(NeutralModeValue.Coast);
        followerShooterMotor1.setControl(new Follower(leaderShooterMotor.getDeviceID(),  MotorAlignmentValue.Aligned ));
        followerShooterMotor2.setControl(new Follower(leaderShooterMotor.getDeviceID(), MotorAlignmentValue.Opposed ));
        followerShooterMotor3.setControl(new Follower(leaderShooterMotor.getDeviceID(),  MotorAlignmentValue.Opposed ));

        //https://v6.docs.ctr-electronics.com/en/latest/docs/migration/migration-guide/control-requests-guide.html
   }

    @Override
    public void periodic() {
        distanceFromGoal = Units.Meters.of(poseEst.getDistanceFromGoal().toTranslation2d().getNorm()).in(Inches);
        // This method will be called once per scheduler run
        // Update speed in Network table
        speedPub.set(leaderShooterMotor.getVelocity().getValueAsDouble());
        // SmartDashboard.putNumber("Shooter Set Speed", getSpeed());
        // calculateShooterSpeed(Units.Meters.of(poseEst.getDistanceFromGoal().toTranslation2d().getNorm()).in(Inches));
        if(isScoring) {
            if(zones.currentZone() == FieldZones.BlueAllianceZone || zones.currentZone() == FieldZones.RedAllianceZone) {
                setMotorSpeedRPM(calculateShooterSpeed(Units.Meters.of(poseEst.getDistanceFromGoal().toTranslation2d().getNorm()).in(Inches)));
            } else if(zones.currentZone() == FieldZones.NeutralZone) {
                setMotorSpeedRPM(30);
            } else if(zones.currentZone() == FieldZones.NoZone || zones.currentZone() == FieldZones.BlueTransitionZone || zones.currentZone() == FieldZones.RedTransitionZone) {
                setMotorSpeedRPM(10);
            }
        } else {
            calculateShooterSpeed(Units.Meters.of(poseEst.getDistanceFromGoal().toTranslation2d().getNorm()).in(Inches));
        }
    }

    public double calculateShooterSpeed(double goalDistance) {
        final double distanceOffset = 50;
        final double minSpeed = 35;
        final double maxSpeed = 100;
        final double increasePerFoot = 2.25;
        
        double speed = minSpeed + (((goalDistance-distanceOffset)/12) * increasePerFoot);
        SmartDashboard.putNumber("Calculated Shooter Speed", Math.min(speed, maxSpeed));
        return Math.min(speed, maxSpeed);
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
        // double targetSpeed = MathUtil.clamp(RPS, -100, 100);
        leaderShooterMotor.setControl(Constants.ShooterConstants.shooterVelocity.withVelocity(RPS));
    }

    public void startScoring() {
        isScoring = true;
    }

    public void stopScoring() {
        isScoring = false;
    }

    /**
     * Stop the shooter motors.
     */
    public void stopMotors() {
        // Not sure how this works with the followers, experiment a lil
        leaderShooterMotor.stopMotor();
        // leaderShooterMotor.setControl(Constants.ShooterConstants.shooterVelocity.withVelocity(0));
    }

    public double getCalculateSpeed(double goalDistance) {
        return calculateShooterSpeed(goalDistance);
    }
}
