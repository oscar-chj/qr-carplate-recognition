package com.example;

import java.awt.image.BufferedImage;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

/**********************************************************************************
 This class intergrates the WebcamTest class to scan and interpret qr codes

 Remember to use .scanningStart after creating the QRTest object to start scanning
 **********************************************************************************/

public class QRTest {

    // Call in Webcam object
    WebcamTest webcam = new WebcamTest();

    // Decoder function for QR code
    // Process from BufferedImage -> LuminanceSouce -> BinaryBitmap -> result (via MultiFormatReader)
    private String qrDecoder(BufferedImage bufferedImage){
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);// Create Luminance Source from image
        BinaryBitmap bitmap = new BinaryBitmap (new HybridBinarizer(source));// Create a binary bitmap for decoding

        try {
            Result result = new MultiFormatReader().decode(bitmap);// Decode the bitmap
            return result.getText();// Return decoded text
        } catch (NotFoundException e) {
            return null;// Returns null if unable to decode
        }
    }

    //Stop Scanning for QR code
    public void scanningStop(){
        webcam.running = false;
    }

    //Starts the scanning for the QR code
    public void scanningStart(){
        webcam.running = true;

        while(webcam.running){
            BufferedImage image = webcam.getWebcam().getImage();// It captures pictures (buffered images) from the webcam
            if(image != null){
                String decodedText = qrDecoder(image);// puts the image through decoder function
                if(decodedText != null){
                    System.out.println("Decoded text: " + decodedText);// prints out function
                    scanningStop();// Stops the scanning process
                }
            }

            try {
                Thread.sleep(100);// Sleep for 100ms (to reduce lag) **Don't mind the error**
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();// Handle interuption 
                scanningStop();// Stop scanning if the proccess is interupted
            }
        }
        
        webcam.closWebcam();// closes webcam   
    }
  
}
