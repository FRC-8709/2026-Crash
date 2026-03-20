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

    // these are variables that are essentially just to make the zone checking easier to read
    // they could go into constants because they dont change, might do that later
    // values are formatted in {point1X,point1Y,point2X,point2Y}
    // These values should be correct, just need to test how well they work...
    private static final double[] BLUE_ALLIANCE_ZONE = {0,0,182.11,317.69};
    private static final double[] BLUE_SCORING_ZONE = {0,0,0,0}; // tbd
    private static final double[] NEUTRAL_ZONE = {182.11,0,469.11,317.69};
    private static final double[] RED_ALLIANCE_ZONE = {469.11,0,651.22,317.69};
    private static final double[] RED_SCORING_ZONE = {0,0,0,0}; // tbd

    public enum FieldZones {
        BlueAllianceZone,
        BlueScoringZone,
        RedAllianceZone,
        RedScoringZone,
        NeutralZone,
        NoZone;
    }

    public ZoneTracking(CommandSwerveDrivetrain drivetrain, LedControl leds) {
        this.drivetrain = drivetrain;
        this.leds = leds;
    }

    // helper function to check if the robot is in zone
    private boolean inZone(double[] bounds) {
        return robotX >= bounds[0] && robotX <= bounds[2]
        && robotY >= bounds[1] && robotY <= bounds[3];
    }

    // check what zone the robot is in
    private FieldZones determinZone() {
        if(inZone(BLUE_ALLIANCE_ZONE)) {
            leds.setColor(0, 0, 255);
            // this is so alliance zone doesnt override the alliance scoring zone
            // it would return only the first on if we just had it in a bunch of else ifs
            // this is the first way i thought of doing it, same applies to red zone & scoring
            if(inZone(BLUE_SCORING_ZONE)) {
                return FieldZones.BlueScoringZone;
            } else {
                return FieldZones.BlueAllianceZone;
            }
        } else if(inZone(RED_ALLIANCE_ZONE)) {
            leds.setColor(255, 0, 0);
            if(inZone(RED_SCORING_ZONE)) {
                return FieldZones.RedScoringZone;
            } else {
                return FieldZones.RedAllianceZone;
            }
        } else if(inZone(NEUTRAL_ZONE)) {
            leds.setColor(212, 175, 55);
            return FieldZones.NeutralZone;
        } else {
            // in case we cant determin a zone for some reason
            // we SHOULD NEVER get this as a result, but just in case
            return FieldZones.NoZone;
        }
    }

    @Override
    public void periodic() {
        // we could pull the x & y values from somewhere else but i just
        // desided to add them because it was easy enough to do
        robotX = drivetrain.getState().Pose.getTranslation().getMeasureX().in(Inches);
        robotY = drivetrain.getState().Pose.getTranslation().getMeasureY().in(Inches);

        currentZone = determinZone();
        SmartDashboard.putString("Current Zone", currentZone.toString());
    }

    public FieldZones currentZone() {
        return currentZone;
    }
}