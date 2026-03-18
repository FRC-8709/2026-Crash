package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Inches;

import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ZoneTracking extends SubsystemBase {

    private CommandSwerveDrivetrain drivetrain;
    private Pigeon2 gyro;
    private double robotX;
    private double robotY;

    private FieldZones currentZone = FieldZones.RedAllianceZone;

    // values are formatted in {point1X,point2X,point1Y,point2Y}
    // THESE ARE NOT CORRECT AT ALL, JUST TESTING RANDOM STUFF
    private static final double[] BLUE_ALLIANCE_ZONE = {0,0,0,0};
    private static final double[] BLUE_SCORING_ZONE = {85,110,130,200};
    private static final double[] NEUTRAL_ZONE = {0,0,0,0};
    private static final double[] RED_ALLIANCE_ZONE = {0,0,0,0};
    private static final double[] RED_SCORING_ZONE = {0,0,0,0};

    public enum FieldZones {
        BlueAllianceZone,
        BlueScoringZone,
        RedAllianceZone,
        RedScoringZone,
        NeutralZone,
        NoZone;
    }

    public ZoneTracking(CommandSwerveDrivetrain drivetrain, Pigeon2 gyro) {
        this.drivetrain = drivetrain;
        this.gyro = gyro;
    }

    private boolean inZone(double x, double y, double[] bounds) {
        return x >= bounds[0] && x <= bounds[1]
        && y >= bounds[2] && y <= bounds[3];
    }

    public FieldZones determinZone(double x, double y) {
        if(inZone(x, y, BLUE_ALLIANCE_ZONE)) {
            return FieldZones.BlueAllianceZone;
        } else if(inZone(x, y, BLUE_SCORING_ZONE)) {
            return FieldZones.BlueScoringZone;
        } else if(inZone(x, y, RED_ALLIANCE_ZONE)) {
            return FieldZones.RedAllianceZone;
        } else if(inZone(x, y, RED_SCORING_ZONE)) {
            return FieldZones.RedScoringZone;
        } else if(inZone(x, y, NEUTRAL_ZONE)) {
            return FieldZones.NeutralZone;
        } else {
            return FieldZones.NoZone;
        }
    }

    @Override
    public void periodic() {
        robotX = drivetrain.getState().Pose.getTranslation().getMeasureX().in(Inches);
        robotY = drivetrain.getState().Pose.getTranslation().getMeasureY().in(Inches);
        SmartDashboard.putNumber("Zone X Value", robotX);
        SmartDashboard.putNumber("Zone Y Value", robotY);

        currentZone = determinZone(robotX, robotY);
        SmartDashboard.putString("Current Zone", currentZone.toString());
    }

    public FieldZones currentZone() {
        return currentZone;
    }
}