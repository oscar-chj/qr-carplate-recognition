package com.example;

/**
 * The App class serves as the entry point for the application.
 * It initializes the QR code scanner to begin scanning.
 */
public final class App {
    private App() {
    }

    /**
     * Main method to start the application.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        // Create a new QRTest instance and start scanning
        QRTest qrScanner = new QRTest();
        qrScanner.scanningStart();
    }
}
