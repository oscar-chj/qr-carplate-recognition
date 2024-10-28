package com.example;

import javax.swing.*;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import java.awt.*;

/**
 * The WebcamTest class manages the webcam feed, GUI display, and window control.
 * Allows real-time display of webcam feed for QR code scanning.
 */
public class WebcamTest {
    private static final int DIMENSION_WIDTH = 640;
    private static final int DIMENSION_HEIGHT = 480;
    private final Webcam webcam;
    public volatile boolean running;
    private final WebcamPanel panel;
    private final JFrame window;
    private final QRTest qrTest;

    /**
     * Constructor initializes the webcam and sets up the GUI.
     * Does not open the webcam until explicitly needed.
     * @param qrTest Reference to the QRTest instance for status sharing.
     */
    public WebcamTest(QRTest qrTest) {
        this.qrTest = qrTest;  // Reference to QRTest for state synchronization
        running = false;

        // Initialize and configure the webcam
        webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(DIMENSION_WIDTH, DIMENSION_HEIGHT));

        // Set up the webcam panel
        panel = new WebcamPanel(webcam);
        panel.setFPSDisplayed(true);
        panel.setDisplayDebugInfo(true);
        panel.setImageSizeDisplayed(true);
        panel.setMirrored(true);

        // Set up the GUI window for webcam display
        window = new JFrame("Webcam Test");
    }

    /**
     * Opens the webcam window if not already open.
     */
    public void openWindow() {
        window.add(panel);
        window.pack();
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Closes the webcam resource when done.
     */
    public void closeWebcam() {
        if (webcam.isOpen()) {
            webcam.close();
        }
    }

    /**
     * Closes the webcam window and releases the webcam resource.
     */
    public void closeWindow() {
        window.dispose();
    }

    /**
     * Dims the screen by setting the opacity of the webcam window.
     * @param opacity The opacity level (0.0 to 1.0).
     */
    public void dimScreen(float opacity) {
        window.setOpacity(opacity);
    }

    /**
     * Provides access to the webcam instance.
     * @return The Webcam instance.
     */
    public Webcam getWebcam() {
        return webcam;
    }
}