package frc.robot.command;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter;


public class TeleopShooter extends Command {

    private final shooter m_subsystem;
    private final Joystick left1Joystick;

    public TeleopShooter(shooter m_subsystem, Joystick left1Joystick){
        this.m_subsystem = m_subsystem;
        this.left1Joystick = left1Joystick;
       
        addRequirements(m_subsystem);
    }
    
    @Override
    public void execute() {
        // if (left1Joystick.getRawButtonPressed(6)) m_subsystem.setMotorSpeed(1);
        
        if (left1Joystick.getRawButton(6)) {
            m_subsystem.setMotorSpeed(10);
        } else if (left1Joystick.getRawButton(4)) {
            m_subsystem.setMotorSpeed(-10);
        } else {
            m_subsystem.setMotorSpeed(0);
        }

        super.execute();
    }
}