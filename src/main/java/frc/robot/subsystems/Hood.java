package frc.robot.subsystems;

// wpilib imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// custom file imports
import frc.robot.Constants;

public class Hood extends SubsystemBase {
    // servos
    private final Servo leftHoodServo;
    private final Servo rightHoodServo;
    private final Joystick testJoystick;

    private final double maxLength = Constants.HoodConstants.actuatorLengthMM;

    private double setPosition;

    public Hood(Servo leftHoodServo, Servo rightHoodServo, Joystick testJoystick) {
        this.leftHoodServo = leftHoodServo;
        this.rightHoodServo = rightHoodServo;
        this.testJoystick = testJoystick;

        leftHoodServo.setBoundsMicroseconds(2000, 1800, 1500, 1200, 1000);
        rightHoodServo.setBoundsMicroseconds(2000, 1800, 1500, 1200, 1000);

        goToPosition(0);
   }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Hood Position", leftHoodServo.getPosition());
        SmartDashboard.putNumber("Hood Speed", leftHoodServo.getSpeed());
        // double setPosition = MathUtil.clamp(((testJoystick.getRawAxis(3) + 1)/2) * maxLength, 0, 105);
        // SmartDashboard.putNumber("Hood Set Position", setPosition);
        // goToPosition(setPosition);
    }

    /**
     * Set position of actuatrs (in mm)
     * @param mm target position, 0 = fully retracted, 140 = fully extended
     */
    public void goToPosition(double mm) {
        double position = mm/maxLength;
        leftHoodServo.set(position);
        rightHoodServo.set(position);
    }

    // stop & hold the actuators
    public void stopHood() {
        leftHoodServo.setSpeed(0);
        rightHoodServo.setSpeed(0);
    }
    
}
