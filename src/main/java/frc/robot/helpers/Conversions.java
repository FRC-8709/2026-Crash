package frc.robot.helpers;

public class Conversions {

    /**
    *Convert Rotations per Minute (RPM) to Rotations per Second (RPS)
    *@param RPM
    *@return RPS
    **/
    public final static double RPMtoRPS(double RPM) {
        // RPM = RPS / 60
        return(RPM / 60);
    }

    /**
    *Convert Rotations per Second (RPS) to Rotations per Minute (RPM) 
    *@param RPS
    *@return RPM
    **/
    public final static double RPStoRPM(double RPS) {
        // RPS = RPM*60
        return(RPS*60);
    }

    /**
    *Convert meters to feet
    *@param Meters
    *@return Feet
    **/
    public final static double MetersToFeet(double Meters) {
        return(Meters*3.2808);
    }

    /**
    *Convert meters to inches
    *@param Meters
    *@return Inches
    **/
    public final static double MetersToinches(double Meters) {
        return(Meters*39.3701);
    }
}
