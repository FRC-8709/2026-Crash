package frc.robot;

import com.ctre.phoenix6.controls.VelocityVoltage;

import edu.wpi.first.units.AngleUnit;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.math.geometry.Translation3d;

// ?????? what is this
import frc.robot.command.IntakeCommands.startRollers;

import com.ctre.phoenix6.controls.PositionVoltage;

    public class Constants {

        public class ShooterConstants {
            // Motor CAN ids
            public static final int leaderShooterMotorPort = 14;
            public static final int followerShooterMotor1Port = 15;
            public static final int followerShooterMotor2Port = 16;
            public static final int followerShooterMotor3Port = 17;

            // Object for speed control
            // Eventually we might want to do something similar to what I did with the rollerVelocity,
            // but for now this is fine

            // Kind of a misnomer cause speed can't be negative but whatever we are already using shooterVelocity
            public static final double shooterSpeed = -75;

            public static final VelocityVoltage shooterVelocity = new VelocityVoltage(shooterSpeed);
            
            // Shooter height from ground, in inches
            public static final Distance shooterHeight = Units.Inches.of(10);
        }

        public class HoodConstants {
            // Hood CAN ids
            public static final int hoodMotorPort = 19;
        //-.16 -0.177001953125 for a hood position close up speed 75
            public static final VelocityVoltage hoodVelocity = new VelocityVoltage(0);
        }

        public class IndexerConstants {
            // Indexer CAN ids
            public static final int indexerMotorPort = 18;
        
            public static final VelocityVoltage indexerVelocity = new VelocityVoltage(0);
        }

        public class SensorConsants {
            // Hood CANcoder ID
            public static final int pigeonPort = 13;
            public static final int hoodEncoderPort = 23;
            public static final int IntakeLiftEncoderPort = 24;
        }

        public class IntakeConstants {
          //Position values for intake 
            public static final double topPosition = .35;
            public static final double outPosition = -.35;

            // Motor CAN ids
            public static final int rollerMotorPort = 22;
            public static final int liftMotorPort = 21;

            // Speed constants
            private static final double rollerSpeed = -45;

            // Position constants
            // I also totally guessed at these, please update these when appropriate
            //public static final Angle liftUpValue = Units.Degrees.of(90);
            //public static final Angle liftDownValue = Units.Degrees.of(-90);

            // Object for speed control
            // We can hardcode the roller speed number cause we aren't planning on varying the speed at all
            public static final VelocityVoltage rollerVelocity = new VelocityVoltage(rollerSpeed);
            public static final VelocityVoltage liftVelocity = new VelocityVoltage(0);

            // Object for position control
            // I am making two objects, one for the up position and one for the down position
            // This makes it easier, cause we never want to stop it in between, so just having two states
            // is easier than using the numbers every time 
            //-.35 for the out position on the encoder 
            //.35 for the up position 
            public static final PositionVoltage liftPosition = new PositionVoltage(0);
        }

        public class FieldConstants {
            // Where is the goal on the field?
            // Make a blue goal and a red goal, right now this is just for testing
            // Also actually add the real position, I just made this up for testing
            public static final Translation3d goalPosition = new Translation3d(Units.Inches.of(3), Units.Inches.of(5), Units.Inches.of(7));
        }
}
