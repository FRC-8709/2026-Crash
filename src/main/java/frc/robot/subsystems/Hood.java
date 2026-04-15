package frc.robot.subsystems;

// wpilib imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// custom file imports
import frc.robot.Constants;
import frc.robot.helpers.Conversions;
import frc.robot.subsystems.ZoneTracking.FieldZones;


public class Hood extends SubsystemBase {
    // Hood motor
    private final TalonFX hoodMotor;
    private final ZoneTracking zones;
    private final PoseEst poseEst;
    private final Shooter shooter;
    boolean hoodEnabled = false;
    
    double distanceFromGoal;

    private final boolean manualHoodControl = false;

    public Hood(TalonFX hoodMotor, ZoneTracking zones, PoseEst poseEst, Shooter shooter) {
        TalonFXConfiguration motorConfig = new TalonFXConfiguration();

        motorConfig.Slot0.kP = 0.15;
        motorConfig.Slot0.kV = 12;

        motorConfig.Slot0.kI = 0.0;
        motorConfig.Slot0.kD = 0.0;
        motorConfig.Slot0.kS = 0.20;

        hoodMotor.getConfigurator().apply(motorConfig);

        this.hoodMotor = hoodMotor;
        this.zones = zones;
        this.poseEst = poseEst;
        this.shooter = shooter;
        hoodMotor.setPosition(0);
   }

    @Override
    public void periodic() {
        SmartDashboard.putBoolean("isHoodEnabled", hoodEnabled);
        SmartDashboard.putNumber("Hood Speed", hoodMotor.getVelocity().getValueAsDouble());
        SmartDashboard.putNumber("Hood Position", hoodMotor.getPosition().getValueAsDouble());
        
        distanceFromGoal = Units.Meters.of(poseEst.getDistanceFromGoal().toTranslation2d().getNorm()).in(Inches);

        // calculateHoodAngle(distanceFromGoal, shooter.getCalculateSpeed(distanceFromGoal));
        // if(DriverStation.isTeleopEnabled()) {
        //     goToPosition(calculateHoodAngle(distanceFromGoal, shooter.getCalculateSpeed(distanceFromGoal)));
        // }

        if(hoodEnabled) {
            if(zones.currentZone() == FieldZones.BlueAllianceZone || zones.currentZone() == FieldZones.RedAllianceZone) {
                goToPosition(calculateHoodAngle(distanceFromGoal, shooter.getCalculateSpeed(distanceFromGoal)));
            } else if(zones.currentZone() == FieldZones.NeutralZone) {
                goToPosition(10);
            } else if(zones.currentZone() == FieldZones.NoZone || zones.currentZone() == FieldZones.BlueTransitionZone || zones.currentZone() == FieldZones.RedTransitionZone) {
                goToPosition(0);
            }
        } else {
            calculateHoodAngle(distanceFromGoal, shooter.getCalculateSpeed(distanceFromGoal));
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
    public void setMotorSpeedRPS(double RPS) {
        // Publish the RPS to the network table
        hoodMotor.setControl(Constants.HoodConstants.hoodVelocity.withVelocity(RPS));
    }

    public double calculateHoodAngle(double robotDistance, double shooterSpeed) {
        final double distanceOffset = 3;
        final double minPos = 0;
        final double maxPos = 10;
        // old model
        double clampedPosition = MathUtil.clamp(-0.495258 + (0.108459 * (robotDistance+distanceOffset)) + (-0.151605 * shooterSpeed), minPos, maxPos);
        // double clampedPosition = MathUtil.clamp(-0.307103 + (0.101938 * (robotDistance+distanceOffset)) + (-0.173569 * shooterSpeed), minPos, maxPos);
        SmartDashboard.putNumber("Calculated Hood Position", clampedPosition);
        return clampedPosition;
    }


    // public double autoSetPosition() {
    //     double distance = Units.Meters.of(poseEst.getDistanceFromGoal().toTranslation2d().getNorm()).in(Inches);
    //     if(distance <= 56) {
    //         return 0;
    //     } else if(distance > 56 && distance <= 68) {
    //         return 0;
    //     } else if(distance > 68 && distance <= 80) {
    //         return 2;
    //     } else if(distance > 80 && distance <= 92) {
    //         return 3.5;
    //     } else if(distance > 92 && distance <= 104) {
    //         return 4;
    //     } else if(distance > 104 && distance <= 116) {
    //         return 4;
    //     } else if(distance < 116) {
    //         return 4;
    //     } else {
    //         return 0;
    //     }
    // }

    /**
     * Set position of hood
     * @param position target position
     */
    public void goToPosition(double position) {
        double targetPos = MathUtil.clamp(position, Constants.HoodConstants.minPos, Constants.HoodConstants.maxPos);
        hoodMotor.setControl(Constants.HoodConstants.hoodPosition.withPosition(targetPos));
    }

    // stop & hold the hood
    public void stopHood() {
        hoodMotor.setControl(Constants.HoodConstants.hoodVelocity.withVelocity(0));
    }

    public void toggleHood() {
        hoodEnabled = !hoodEnabled;
    }

    public void enableHood() {
        hoodEnabled = !hoodEnabled;
    }

    public void disableHood() {
        hoodEnabled = !hoodEnabled;
    }
}