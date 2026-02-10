package frc.robot.helpers;

public class Conversions {
    
    // Move this to a different helper functions/math file later
    public static double RPMtoRPS(double RPM) {
        // RPM = RPS * 60
        return(RPM * 60);
    }

    // Move this to a different helper functions/math file later
    public static double RPStoRPM(double RPS) {
        // RPS = RPM/60
        return(RPS/60);
    }

   
}
