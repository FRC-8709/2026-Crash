package frc.robot.command.IntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeRoller;

public class startRollers extends Command {
    private IntakeRoller s_IntakeRoller;

    public startRollers(IntakeRoller s_IntakeRoller){
        this.s_IntakeRoller = s_IntakeRoller;

        addRequirements(s_IntakeRoller);
    }

    @Override
    public void initialize() {
        // Tell the PID to spin the rollers
        s_IntakeRoller.spinRoller();
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
        // In this case, we only need the command to run once, so we'll just set this to true

        // By setting this to return true, it will never run more than once.
        // This is how the runOnce(() -> ) shortcut works

        return true;
    }
}