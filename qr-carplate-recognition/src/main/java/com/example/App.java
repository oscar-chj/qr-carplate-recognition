package com.example;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * The App class serves as the entry point for the application.
 * It initializes the webcam and handles QR code and car plate scanning.
 */
public final class App {

    private static final int DIMENSION_WIDTH = 640;
    private static final int DIMENSION_HEIGHT = 480;

    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        // Gets the default webcam of user's device
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(DIMENSION_WIDTH, DIMENSION_HEIGHT));
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
