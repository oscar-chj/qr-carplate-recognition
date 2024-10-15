package com.example;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import java.awt.Dimension;
import javax.swing.JFrame;

public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        // Gets the default webcam of user's device
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(640, 480));
        webcam.open(); 

        // Create a panel to display the webcam feed
        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setFPSDisplayed(true);
        panel.setDisplayDebugInfo(true);
        panel.setImageSizeDisplayed(true);
        panel.setMirrored(true);

        // Create a window to hold the panel
        JFrame window = new JFrame("Webcam Test");
        window.add(panel);
        window.setResizable(true);
        window.pack();
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
