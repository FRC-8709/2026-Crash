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
    private final boolean manualHoodControl = false;

    public Hood(TalonFX hoodMotor, ZoneTracking zones, PoseEst poseEst) {
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
   }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Hood Speed", hoodMotor.getVelocity().getValueAsDouble());
        SmartDashboard.putNumber("Hood Position", hoodMotor.getPosition().getValueAsDouble());

        autoPositionHood(Constants.HoodConstants.posMult);
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

    /**
     * Set position multiplier for hood
     * @param multiplier hood position
     */
    public void autoPositionHood(double multiplier) {
        if(!manualHoodControl) {
            if(zones.currentZone() == FieldZones.NeutralZone) {
                goToPosition(10);
            } else if(zones.currentZone() == FieldZones.BlueAllianceZone || zones.currentZone() == FieldZones.RedAllianceZone) {
                goToPosition(Units.Meters.of(poseEst.getDistanceFromGoal().toTranslation2d().getNorm()).in(Inches) * multiplier);
            } else if(zones.currentZone() == FieldZones.BlueTransitionZone || zones.currentZone() == FieldZones.RedTransitionZone || zones.currentZone() == FieldZones.NoZone) {
                goToPosition(0);
            }
        }
    }

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
}