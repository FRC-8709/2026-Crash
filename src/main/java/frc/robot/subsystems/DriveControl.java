package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.command.SwerveCommands.lookAtPointDrive;
import frc.robot.command.SwerveCommands.regularDrive;

public class DriveControl extends SubsystemBase {
    private final CommandSwerveDrivetrain drivetrain;
    private final Joystick joystickLeft1, joystickLeft2;
    private final PS4Controller controller;
    boolean isTargeting = false;

    public DriveControl(CommandSwerveDrivetrain drivetrain, Joystick joystickLeft1, Joystick joystickLeft2, PS4Controller controller) {
        this.drivetrain = drivetrain;
        this.joystickLeft1 = joystickLeft1;
        this.joystickLeft2 = joystickLeft2;
        this.controller = controller;

        SmartDashboard.putBoolean("isRobotTargeting", isTargeting);
    }

    @Override
    public void periodic() {
        
    }

    public void stopTargeting() {
        new regularDrive(drivetrain, joystickLeft1, joystickLeft2, controller).schedule();
        isTargeting = false;
        SmartDashboard.putBoolean("isRobotTargeting", isTargeting);
    }

    public void toggleTargeting() {
        if(!isTargeting) {
            new lookAtPointDrive(drivetrain, joystickLeft1, joystickLeft2, controller).schedule();
        } else {
            new regularDrive(drivetrain, joystickLeft1, joystickLeft2, controller).schedule();
        }
        isTargeting = !isTargeting;
        SmartDashboard.putBoolean("isRobotTargeting", isTargeting);
    }
}
