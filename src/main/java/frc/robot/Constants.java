package frc.robot;

import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;

import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Distance;

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
            public static final double shooterSpeed = 50;
            public static final double autonShooterSpeed = 33;

            public static final VelocityVoltage shooterVelocity = new VelocityVoltage(shooterSpeed);
            
            // Shooter height from ground, in inches
            // Added 3 inches for ball radius, actual robot height 25"
            public static final Distance shooterHeight = Units.Inches.of(28);
        }

        public class ScoringConstants {
            // ALL THESE VALUES ARE MADE UP AND NEED TO BE ADJUSTED/TESTED
            
            // max distance should be somewhat correct based on the 2d simulation i did and
            // where i think we should be able to score from

            // max distance robot can be from goal when scoring (inches)
            public static final double maxDistance = 125;

            // tolerance for how accuratly the robot needs to be aligned to score (degrees)
            public static final double goalFacingTolerance = 5;

            // tolerance for how accurate/close the hood needs to be to the set positionto score
            // (i think this would be in degrees)
            public static final double hoodAngleTolerance = 5;
        }

        public class HoodConstants {
            // Hood CAN ids
            public static final int hoodMotorPort = 19;

            //HOOD POSITIONS
            public static final double slightAngle = 5;
            public static final double passAngle = 8;

            // Minimum and Maximum positions for hood
            public static final double minPos = 0;
            public static final double maxPos = 100;

            // for hood position purley based on ditance, no math
            public static final double posMult = 0.055;

            public static final PositionVoltage hoodPosition = new PositionVoltage(0);

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

        public class LedConstants {
            //led port
            public static final int ledPort = 0;
            public static final int ledLength = 81;
        }

        public class IntakeConstants {
          //Position values for intake 
            public static final double upPosition = -30;
            public static final double middlePosition = -30;
            public static final double downPosition = -60;

            
            public static final double intakeOutPosition = -200;
            public static final double intakeMidPosition = -70;

            // Motor CAN ids
            public static final int rollerMotorPort = 22;
            public static final int liftMotorPort = 21;

            // Speed constants
            private static final double rollerSpeed = -35;
            private static final double slowRollerSpeed = -25;
            private static final double reverseRollerSpeed=35;


            // Object for speed control
            // We can hardcode the roller speed number cause we aren't planning on varying the speed at all
            public static final VelocityVoltage rollerVelocity = new VelocityVoltage(rollerSpeed);
            public static final VelocityVoltage slowRollerVelocity = new VelocityVoltage(slowRollerSpeed);
            public static final VelocityVoltage liftVelocity = new VelocityVoltage(0);
            public static final VelocityVoltage rollerReverseVelocity = new VelocityVoltage(reverseRollerSpeed);

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
            public static final Translation3d blueHubPosition = new Translation3d(Units.Inches.of(181.56), Units.Inches.of(158.32), Units.Inches.of(72));
            public static final Translation3d redHubPosition = new Translation3d(Units.Inches.of(468.56), Units.Inches.of(158.32), Units.Inches.of(72));

            public static final Translation2d redCorner = new Translation2d(Units.Inches.of(651.22), Units.Inches.of(317.60));
        }
}
