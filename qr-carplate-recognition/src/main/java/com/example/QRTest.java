package com.example;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

/**
 * The QRTest class handles the QR code scanning, decoding, and displaying results.
 * Integrates with the WebcamTest for webcam access and displays results using a dialog box.
 */
public class QRTest {
    public boolean foundQRText = false; // Tracks if a QR code was found
    public String decodedText = null;   // Stores the decoded text from the QR code

    private final WebcamTest webcam;
    private Timer inactivityTimer;

    // Constants for inactivity handling
    private static final long DIM_DELAY = 10000; // 10 seconds of inactivity before dimming
    private static final long PROMPT_DELAY = 20000; // 20 seconds before prompting

    /**
     * Constructor initializes WebcamTest.
     */
    public QRTest() {
        this.webcam = new WebcamTest(this);  // Pass QRTest instance to allow state sharing
        startInactivityTimer();
    }

    /**
     * Starts the inactivity timer to monitor for inactivity and dim or close webcam.
     */
    private void startInactivityTimer() {
        inactivityTimer = new Timer();
        inactivityTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Dim the webcam screen by 50% after inactivity
                webcam.dimScreen(0.5f);

                // Schedule a further check to prompt the user if still inactive
                inactivityTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        int choice = JOptionPane.showConfirmDialog(null, "Are you still there?", "Inactivity Detected",
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (choice == JOptionPane.NO_OPTION) {
                            shutdown();
                        } else {
                            // Reset the scanning process if the user is still there
                            resetInactivityTimer();
                        }
                    }
                }, PROMPT_DELAY);
            }
        }, DIM_DELAY);
    }

    /**
     * Resets the inactivity timer whenever activity is detected.
     */
    private void resetInactivityTimer() {
        inactivityTimer.cancel();
        startInactivityTimer();
        webcam.dimScreen(1.0f); // Restore screen brightness
    }

    /**
     * Decode the given buffered image to extract QR code text.
     * @param bufferedImage The image captured from the webcam.
     * @return Decoded QR text if found; otherwise, null.
     */
    private String qrDecoder(BufferedImage bufferedImage) {
        resetInactivityTimer(); // Reset timer whenever decoding is attempted
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            return null;
        }
    }

    /**
     * Stops the QR scanning process.
     */
    public void scanningStop() {
        webcam.running = false;
    }

    /**
     * Starts the QR scanning process in a separate thread to avoid blocking the main GUI.
     */
    public void scanningStart() {
        // Reset flags for new scanning session
        foundQRText = false;
        decodedText = null;
        webcam.running = true;

        // Opens GUI window for webcam and turns on webcam
        webcam.getWebcam().open();
        webcam.openWindow();

        // Run scanning in a background thread to keep the GUI responsive
        new Thread(() -> {
            while (webcam.running) {
                BufferedImage image = webcam.getWebcam().getImage(); // Capture webcam image
                if (image != null) {
                    decodedText = qrDecoder(image);
                    if (decodedText != null) {
                        foundQRText = true;
                        System.out.println(decodedText);
                        scanningStop();
                    }
                }

                try {
                    Thread.sleep(100); // Small delay (1 second) to manage CPU usage and webcam processing
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    scanningStop();
                }
            }

            // Once QR code is found, close the webcam and show result dialog
            if (foundQRText) {
                showResultDialog(decodedText);
            }

        }).start();
    }

    /**
     * Displays a dialog box with "OPEN" and "CANCEL" buttons to handle the decoded text.
     * If "OPEN" is selected, opens the link in the default browser if it's a valid URL.
     * If "CANCEL" is selected, restarts the scanning process.
     * @param message The decoded QR message to display.
     */
    private void showResultDialog(String message) {
        Object[] options = {"OPEN", "CANCEL"};
        int choice = JOptionPane.showOptionDialog(null, message, "Decoded Text",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        if (choice == JOptionPane.YES_OPTION) {
            try {
                URI uri = new URI(message);
                if (uri.getScheme() == null) {
                    throw new URISyntaxException(message, "No scheme found in URI");
                }

                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(uri);

                    // End process
                    webcam.closeWebcam();
                    webcam.closeWindow();
                    scanningStop();
                } else {
                    JOptionPane.showMessageDialog(null, "Opening links is not supported on this environment.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (URISyntaxException | IOException e) {
                JOptionPane.showMessageDialog(null, "Invalid link or failed to open link.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            scanningStart();
        }
    }

    /**
     * Shuts down the application by closing resources and stopping the webcam.
     */
    private void shutdown() {
        webcam.closeWebcam();
        System.exit(0);
    }
}
