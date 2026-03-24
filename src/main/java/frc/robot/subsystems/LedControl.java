package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LedControl extends SubsystemBase {
    AddressableLED led;
    AddressableLEDBuffer ledBuffer;

    //This is just becuase i wanted to see if i could get a color to display in elastic (yes i can)
    private String colorHex;

    public LedControl(int port, int length) {
        led = new AddressableLED(port);
        ledBuffer = new AddressableLEDBuffer(length);
        led.setLength(length);
        led.start();
        setColor(255, 0, 0);
    }

    @Override
    public void periodic() {
        SmartDashboard.putString("LED Color", colorHex);
    }

    public void hubActive() {
        setColor(0, 50, 0);
    }

    public void hubUpcoming() {
        setColor(0, 50, 0);
    }

    public void WeWin() {
        int[] letters = new int[]{49,2,53,57,4,51,59,6,46,8,9,10,47,46,62,63,64,14,16,18,41,39,37,69,71,20,21,22,34,74,75,76,24,25,31,29,78,80};
        for (int n : letters) {
            this.ledBuffer.setRGB(n-1, 255, 165, 0);
        }

        int[] emptySpace = new int[]{1,54,5,50,55,56,3,52,58,7,48,60,61,11,12,13,45,44,43,42,65,66,67,68,15,40,70,17,38,72,73,19,36,35,23,32,33,77,26,27,30,28,79,81};
        for (int n : emptySpace) {
            this.ledBuffer.setRGB(n-1, 0, 0, 20);
        }
        led.setData(this.ledBuffer);
        led.start();
    }

    public void setColor(int r, int g, int b) {
        colorHex = String.format("#%02X%02X%02X", r, g, b);
        for (int i = 0; i < ledBuffer.getLength(); i++) ledBuffer.setRGB(i, r, g, b);
        led.setData(ledBuffer);
    }
}
