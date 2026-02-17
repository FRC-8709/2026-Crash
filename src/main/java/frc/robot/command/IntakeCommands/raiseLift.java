package frc.robot.command.IntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeLift;

public class raiseLift extends Command {
    private IntakeLift s_IntakeLift;

    public raiseLift(IntakeLift s_IntakeLift){
        this.s_IntakeLift = s_IntakeLift;

        addRequirements(s_IntakeLift);
    }

    @Override
    public void initialize() {
        // Tell the PID to move the lift to the "up" position
        s_IntakeLift.raiseLift();
    }
    
    @Override
    public void execute() {
        // Nothing here for now, maybe we need to keep re-reading the position?
        // Probably not if we just do the check in isFinished but who knows
    }

    @Override
    public void end(boolean interrupted) {
        // Nothing here for now, but this should be used as "wrap up" space
    }

    @Override 
    public boolean isFinished() {
        // This should have a condition where we check if the command should be done or not
        // In this case, we want to know when the motor has reached it's position
        
        // In normal cases, we actually don't want to say "ok we made it there" as soon as
        // the motor position matches our target, because that doesn't account for overshoot at all.
        // The PID control will probably overshoot, and then correct itself, so if we stopped the motor
        // as soon as we hit the position for the first time, the inertia would carry us past our target,
        // unless our PID was really good. Fortunately, the PID will keep running anyways, we just want to make sure
        // the intake is no longer outside of the robot.

        // Replace this with the "did we go far enough" condition
        return false;
    }
}

