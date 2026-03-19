package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Inches;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ZoneTracking extends SubsystemBase {

    private CommandSwerveDrivetrain drivetrain;
    private double robotX;
    private double robotY;
    private LedControl leds;

    private FieldZones currentZone = FieldZones.RedAllianceZone;

    // values are formatted in {point1X,point1Y,point2X,point2Y}
    // These values should be correct, just need to test how well they work...
    private static final double[] BLUE_ALLIANCE_ZONE = {0,0,182.11,317.69};
    private static final double[] BLUE_SCORING_ZONE = {0,0,0,0}; // tbd
    private static final double[] NEUTRAL_ZONE = {182.11,0,469.11,317.69};
    private static final double[] RED_ALLIANCE_ZONE = {469.11,0,651.22,317.69};
    private static final double[] RED_SCORING_ZONE = {0,0,0,0}; // tbd

    public enum FieldZones {
        BlueAllianceZone,
        BlueScoringZone, // might change later
        RedAllianceZone,
        RedScoringZone, // also might change later
        NeutralZone,
        NoZone;
    }

    public ZoneTracking(CommandSwerveDrivetrain drivetrain, LedControl leds) {
        this.drivetrain = drivetrain;
        this.leds = leds;
    }

    private boolean inZone(double[] bounds) {
        return robotX >= bounds[0] && robotX <= bounds[2]
        && robotY >= bounds[1] && robotY <= bounds[3];
    }

    private FieldZones determinZone() {
        if(inZone(BLUE_ALLIANCE_ZONE)) {
            leds.setColor(0, 0, 255);
            return FieldZones.BlueAllianceZone;
        } else if(inZone(BLUE_SCORING_ZONE)) {
            return FieldZones.BlueScoringZone;
        } else if(inZone(RED_ALLIANCE_ZONE)) {
            leds.setColor(255, 0, 0);
            return FieldZones.RedAllianceZone;
        } else if(inZone(RED_SCORING_ZONE)) {
            return FieldZones.RedScoringZone;
        } else if(inZone(NEUTRAL_ZONE)) {
            leds.setColor(212, 175, 55);
            return FieldZones.NeutralZone;
        } else {
            return FieldZones.NoZone;
        }
    }

    @Override
    public void periodic() {
        robotX = drivetrain.getState().Pose.getTranslation().getMeasureX().in(Inches);
        robotY = drivetrain.getState().Pose.getTranslation().getMeasureY().in(Inches);

        currentZone = determinZone();
        SmartDashboard.putString("Current Zone", currentZone.toString());
    }

    public FieldZones currentZone() {
        return currentZone;
    }
}