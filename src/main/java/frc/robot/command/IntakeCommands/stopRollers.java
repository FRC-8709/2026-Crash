package frc.robot.command.IntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeRoller;

public class stopRollers extends Command {
    private IntakeRoller s_IntakeRoller;

    public stopRollers(IntakeRoller s_IntakeRoller){
        this.s_IntakeRoller = s_IntakeRoller;

        addRequirements(s_IntakeRoller);
    }

    @Override
    public void initialize() {
        // Tell the PID to stop the roller
        s_IntakeRoller.stopRoller();
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
        // In this case, we want to know when the motors have slowed down enough so we can retract the intake

        // Replace with the "are the rollers going slow enough" condition, maybe like 1/4 speed or something?
        return true;
    }
}