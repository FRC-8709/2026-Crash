package frc.robot.command.SwerveCommands;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class regularDrive extends Command{

    private double MaxSpeed = 1.0 * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity
    
    private final CommandSwerveDrivetrain drivetrain;
    private final Joystick joystickLeft1;
    private final Joystick joystickLeft2;
    private final PS4Controller controller;

    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
        .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1)
        .withDriveRequestType(DriveRequestType.OpenLoopVoltage);

    public regularDrive(CommandSwerveDrivetrain drivetrain, Joystick joystickLeft1, Joystick joystickLeft2, PS4Controller controller) {
        this.drivetrain = drivetrain;
        this.joystickLeft1 = joystickLeft1;
        this.joystickLeft2 = joystickLeft2;
        this.controller = controller;
        addRequirements(drivetrain);
    }

    @Override
    public void execute() {
        drivetrain.setControl(
        drive
            .withVelocityX(-joystickLeft1.getY() * MaxSpeed)
            .withVelocityY(-joystickLeft1.getX() * MaxSpeed)
            .withRotationalRate(-joystickLeft2.getX() * MaxAngularRate)

            // ps4controller drive (for testing at home - parker)
            // .withVelocityX(-controller.getLeftY() * MaxSpeed)
            // .withVelocityY(-controller.getLeftX() * MaxSpeed)
            // .withRotationalRate(-controller.getRightX() * MaxAngularRate)
        );
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
