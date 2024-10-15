package com.example;

import java.awt.image.BufferedImage;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;





public class QRTest {

    //Call in Webcam object
    WebcamTest webcam = new WebcamTest();

    //Decoder function for QR code
    private String qrDecoder(BufferedImage bufferedImage){
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap (new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            return null;
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
            BufferedImage image = webcam.getWebcam().getImage();
            if(image != null){
                String decodedText = qrDecoder(image);
                if(decodedText != null){
                    System.out.println("Decoded text: " + decodedText);
                    scanningStop();
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                scanningStop();
            }
        }
        
        webcam.closWebcam();   
    }


    public QRTest(){
        
    }   
}
