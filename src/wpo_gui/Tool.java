/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpo_gui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import static wpo_gui.Tool.log;

/**
 *
 * @author Ja
 */
public class Tool {

    public static byte[] arrayListToByteArray(ByteArrayOutputStream baos, ArrayList<Integer> src) throws Exception {
        baos.reset();
        ArrayList<Integer> table = new ArrayList<Integer>();
        for (int i = 0; i < src.size(); i++) {
            table.add(src.get(i));
        }

        for (int i = 0; i < table.size(); i++) {
            writeByte(baos, table.get(i));
        }

        return baos.toByteArray();
    }

    public static void writeByte(ByteArrayOutputStream baos, int b) {
        baos.write(b);
    }

    public static void writeInt(ByteArrayOutputStream baos, int i) {
        baos.write(i & 0xFF);
        baos.write((i >> 8) & 0xFF);
    }

    public static void writeZeroes(ByteArrayOutputStream baos, int c) {
        for (int i = 0; i < c; i++) {
            writeByte(baos, 0x00);
        }
    }

    public static void saveRawDecodedImageBytes(PCXImage pcxImage, String rawFilename) throws Exception {
        String filename = pcxImage.getFilename();
        byte[] byteArray = arrayListToByteArray(pcxImage.getBaos(), pcxImage.getDecodedImageBytes());

        log("RAW: Saving [" + filename + "] RAW: " + rawFilename);
        File rawFile = new File(rawFilename);
        FileOutputStream rawFileOutputStream = new FileOutputStream(rawFile);
        rawFileOutputStream.write(byteArray);    //zapis naglowka do pliku
        rawFileOutputStream.close();
    }

    public static ArrayList<Integer> getImageBytesFromRawFile(String filename, int width, int height, int bpp) throws FileNotFoundException, IOException {
        log("RAW: Otwieranie pliku raw: " + filename);
        File rawFile = new File(filename);
        FileInputStream rawFileInputStream = new FileInputStream(rawFile);
        byte[] rawImageBytes = new byte[width * height * bpp];

        rawFileInputStream.read(rawImageBytes);

        ArrayList<Integer> tmp = new ArrayList<Integer>();
        for (int i = 0; i < width * height * bpp; i++) {
            tmp.add(Byte.toUnsignedInt(rawImageBytes[i]));
        }

        return tmp;
    }

//    public static void saveRawDecodedImageChannels(PCXImage pcxImage, String rawFilename) throws Exception {
//        
//        String filename = pcxImage.getFilename();
//        int resolution = pcxImage.getResolution();
//        ArrayList<Integer> imageBytes = pcxImage.getDecodedImageBytes();
//        
//        byte[] Rchannel = new byte[resolution];
//        byte[] Gchannel = new byte[resolution];
//        byte[] Bchannel = new byte[resolution];
//        
//        int width = pcxImage.getWidth();
//        int height = pcxImage.getHeight();
//
//        for (int row = 0; row < height; row++) {    //czerwony
//            for (int col = 0; col < (1 / 3 * width) - 1; col++) {
//                Rchannel[col + row*width] = (byte)(imageBytes.get(col + row*width));
//            }
//        }
//
//        for (int row = 0; row < height; row++) {    //zielony
//            for (int col = ((1 / 3) * width); col < (2 / 3 * width) - 1; col++) {
//                Gchannel[col + row*width] = imageBytes.get(col + row*width);
//            }
//        }
//
//        for (int row = 0; row < height; row++) {  //niebieski
//            for (int col = (2 / 3 * width); col < width; col++) {
//                Bchannel[col + row*width] = imageBytes.get(col + row*width);
//            }
//        }
//
//        byte[] byteArray = arrayListToByteArray(pcxImage.getBaos(), pcxImage.getDecodedImageBytes());
//
//        log("RAW: Zapisywanie kanałów do raw: " + rawFilename);
//        //System.out.println("Zapisywanie [" + pcxFileName + "] do raw: " + filename);
//        File rawFile = new File(rawFilename);
//        FileOutputStream rawFileOutputStream = new FileOutputStream(rawFile);
//        rawFileOutputStream.write(byteArray);    //zapis naglowka do pliku
//        rawFileOutputStream.close();
//    }

    public static void log(String s) {
        System.out.println("\t[L] " + s);
    }

    public static void println(String s) {
        System.out.println(s);
    }
    
    public static void print(String s) {
        System.out.print(s);
    }
    
    public static void error(String s) {
        System.out.println("\t[ERROR] " + s + " !!!!!!!!!!!");
    }

}
