package frc.robot.command.SwerveCommands;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class lookAtPointDrive extends Command{
    private Alliance alliance = DriverStation.getAlliance().orElse(Alliance.Red);

    private double MaxSpeed = 1.0 * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity
    
    private final CommandSwerveDrivetrain drivetrain;
    private final Joystick joystickLeft1;
    private final Joystick joystickLeft2;
    private final PS4Controller controller;

    private final SwerveRequest.FieldCentricFacingAngle driveWhileFacing = new SwerveRequest.FieldCentricFacingAngle()
    .withDeadband(MaxSpeed * 0.1)
    .withRotationalDeadband(MaxAngularRate * 0.1)
    .withDriveRequestType(DriveRequestType.OpenLoopVoltage);

    public lookAtPointDrive(CommandSwerveDrivetrain drivetrain, Joystick joystickLeft1, Joystick joystickLeft2, PS4Controller controller) {
        this.drivetrain = drivetrain;
        this.joystickLeft1 = joystickLeft1;
        this.joystickLeft2 = joystickLeft2;
        this.controller = controller;
        addRequirements(drivetrain);

        driveWhileFacing.HeadingController.setPID(10, 0, 0.1);
    }

    private Rotation2d getAngleToTarget() {
        var robotPose = drivetrain.getState().Pose;
        double dx = 0;
        double dy = 0;
        if(alliance.equals(Alliance.Blue)) {
            dx = Constants.FieldConstants.blueHubPosition.getMeasureX().in(Meters) - robotPose.getX();
            dy = Constants.FieldConstants.blueHubPosition.getMeasureY().in(Meters) - robotPose.getY();
        } else if (alliance.equals(Alliance.Red)) {
            dx = Constants.FieldConstants.redHubPosition.getMeasureX().in(Meters) - robotPose.getX();
            dy = Constants.FieldConstants.redHubPosition.getMeasureY().in(Meters) - robotPose.getY();
        }
        return new Rotation2d(Math.atan2(dy, dx));
    }

    @Override
    public void execute() {
        alliance = DriverStation.getAlliance().orElse(Alliance.Red);
        drivetrain.setControl(
        driveWhileFacing
            // .withVelocityX(-joystickLeft1.getY() * MaxSpeed)
            // .withVelocityY(-joystickLeft1.getX() * MaxSpeed)

            // ps4controller drive (for testing at home - parker)
            .withVelocityX(-controller.getLeftY() * MaxSpeed)
            .withVelocityY(-controller.getLeftX() * MaxSpeed)
            .withMaxAbsRotationalRate(1 * MaxAngularRate)
            .withTargetDirection(getAngleToTarget())
        );
    }


}
