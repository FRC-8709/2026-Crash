package frc.robot;

import com.ctre.phoenix6.controls.VelocityVoltage;

import edu.wpi.first.units.AngleUnit;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;

import com.ctre.phoenix6.controls.PositionVoltage;

    public class Constants {

        public class ShooterConstants {
            // Motor CAN ids
            public static final int leaderShooterMotorPort = 19;
            public static final int followerShooterMotor1Port = 18;

            // Object for speed control
            // Eventually we might want to do something similar to what I did with the rollerVelocity,
            // but for now this is fine
            public static final VelocityVoltage shooterVelocity = new VelocityVoltage(0);
        }

        public class IntakeConstants {
            // Motor CAN ids
            // Update these cause I made these up too
            public static final int rollerMotorPort = 19;
            public static final int liftMotorPort = 18;

            // Speed constants
            // I 100% made this number up, change it as soon as the intake is actually built
            private static final double rollerSpeed = 50;

            // Position constants
            // I also totally guessed at these, please update these when appropriate
            public static final Angle liftUpValue = Units.Degrees.of(90);
            public static final Angle liftDownValue = Units.Degrees.of(-90);

            // Object for speed control
            // We can hardcode the roller speed number cause we aren't planning on varying the speed at all
            public static final VelocityVoltage rollerVelocity = new VelocityVoltage(rollerSpeed);

            // Object for position control
            // I am making two objects, one for the up position and one for the down position
            // This makes it easier, cause we never want to stop it in between, so just having two states
            // is easier than using the numbers every time
            public static final PositionVoltage liftPosition = new PositionVoltage(0);
        }
}
