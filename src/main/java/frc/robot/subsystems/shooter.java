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
    private final Hood hood;

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

    public Shooter(TalonFX leaderShooterMotor, TalonFX followerShooterMotor1, TalonFX followerShooterMotor2, TalonFX followerShooterMotor3 , DoubleTopic speedTopic, DoubleTopic shooterKPTopic, DoubleTopic shooterKVTopic, PoseEst poseEst, ZoneTracking zones, Hood hood) {
        speedPub = speedTopic.publish();
        speedPub.setDefault(0.0);

        shooterKP = shooterKPTopic.getEntry(0);
        shooterKV = shooterKVTopic.getEntry(0);

        shooterKP.set(0.075);
        shooterKV.set(12);

        this.poseEst = poseEst;
        this.zones = zones;
        this.hood = hood;
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

    // private double getSpeed() {
    //     double robotDistance = Units.Meters.of(poseEst.getDistanceFromGoal().toTranslation2d().getNorm()).in(Inches);
    //     if(robotDistance < 50) {
    //         hood.goToPosition(0);
    //         return 35;
    //     } else if(robotDistance >= 50 && robotDistance <= 62) {
    //         hood.goToPosition(0);
    //         return 40;
    //     } else if(robotDistance >= 62 && robotDistance <= 74) {
    //         hood.goToPosition(4.3);
    //         return 40;
    //     } else if(robotDistance >= 74 && robotDistance <= 86) {
    //         hood.goToPosition(4.3);
    //         return 45;
    //     } else if(robotDistance >= 86 && robotDistance <= 98) {
    //         hood.goToPosition(0);
    //         return 45;
    //     } else if(robotDistance >= 98 && robotDistance <= 110) {
    //         hood.goToPosition(0);
    //         return 50;
    //     } else if(robotDistance >= 110 && robotDistance <= 124) {
    //         hood.goToPosition(0);
    //         return 50;
    //     } else if(robotDistance >= 124 && robotDistance <= 136) {
    //         hood.goToPosition(0);
    //         return 55;
    //     } else if(robotDistance >= 136 && robotDistance <= 148) {
    //         hood.goToPosition(0);
    //         return 55;
    //     } else if(robotDistance >= 148 && robotDistance <= 160) {
    //         hood.goToPosition(0);
    //         return 60;
    //     } else {
    //         hood.goToPosition(0);
    //         return 60;
    //     }
    // }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        // Update speed in Network table
        speedPub.set(leaderShooterMotor.getVelocity().getValueAsDouble());
        // SmartDashboard.putNumber("Shooter Set Speed", getSpeed());

        autoSetShooterSpeed();
    }

    // automatically change shooter speed based on how far from the goal
    public void autoSetShooterSpeed() {
        if(usingAutomaticSpeed) {
            if(zones.currentZone() == FieldZones.NeutralZone) {
                setMotorSpeedRPS(0);
            } else if(zones.currentZone() == FieldZones.BlueAllianceZone || zones.currentZone() == FieldZones.RedAllianceZone) {
                // setMotorSpeedRPS(getSpeed());
                setMotorSpeedRPS(0);
            } else if(zones.currentZone() == FieldZones.BlueTransitionZone || zones.currentZone() == FieldZones.RedTransitionZone || zones.currentZone() == FieldZones.NoZone) {
                setMotorSpeedRPS(0);
            }
        }
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

    /**
     * Stop the shooter motors.
     */
    public void stopMotors() {
        // Not sure how this works with the followers, experiment a lil
        // leaderShooterMotor.stopMotor();
        leaderShooterMotor.setControl(Constants.ShooterConstants.shooterVelocity.withVelocity(0));
    }
    
}
