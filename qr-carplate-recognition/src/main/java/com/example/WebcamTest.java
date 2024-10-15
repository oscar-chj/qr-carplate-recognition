package com.example;

import java.awt.Dimension;

import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

/**
 * The WebcamTest class opens a GUI that displays the camera for the user.
 */
public class WebcamTest {

    // Webcam dimensions
    private static final int DIMENSION_WIDTH = 640;
    private static final int DIMENSION_HEIGHT = 480;
    private final Webcam webcam; // Declare Webcam
    public volatile boolean running; // Controls the running & stopping of WebcamTest
    private  final WebcamPanel panel;

    public WebcamTest(){

        //Running starts the scanning loop (Set to false before scanning)
        running = false;

        // Get default webcam
        webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(DIMENSION_WIDTH, DIMENSION_HEIGHT));
        webcam.open();
        webcam.getImage();

        // Create a panel to display the webcam feed
        panel = new WebcamPanel(webcam);
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

    // Getter for webcam (Allows object created by constructor to access the functions in the webcam class)
    public Webcam getWebcam(){
        return webcam;
    }

    // Getter for panel for the webcam
    public WebcamPanel getPanel(){
        return panel;
    }

    // Function to close the webcam
    public void  closWebcam(){
        webcam.close();
    }

    public static void main(String[] args) {
        
    }
}
