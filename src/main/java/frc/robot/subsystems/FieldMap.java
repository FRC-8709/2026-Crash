package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class FieldMap extends SubsystemBase {
    
    private CommandSwerveDrivetrain drivetrain;
    private final Field2d fieldMap = new Field2d();

    public FieldMap(CommandSwerveDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
    }

    @Override
    public void periodic() {
        fieldMap.setRobotPose(drivetrain.getState().Pose);
        SmartDashboard.putData("Field Map", fieldMap);
    }
}
