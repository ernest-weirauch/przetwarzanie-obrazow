/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpo_gui;

import java.util.ArrayList;
import static wpo_gui.Tool.log;
import static wpo_gui.Tool.println;

/**
 *
 * @author Ja
 */
public class PixelsMatrix {

    private int[][] pixelsMono;
    private int[][] pixelsGray;
    private PixelRGB[][] pixelsRGB;

    public Type type;
    public int width;
    public int height;
    private int resolution;

    public void setResolution(int width, int height) {
        this.width = width;
        this.height = height;
        this.resolution = width * height;
    }

    public PixelsMatrix() {
        Tool.error("Próba utworzenia macierzy bez podania jej źródłowego obrazu (zaw. typ, rozdzielczość)");
    }
//
//    public PixelsMatrix(Type type, int width, int height) {
//        this.type = type;
//        this.width = width;
//        this.height = height;
//        resolution = width * height;
//
//        initPixels();
//    }

    public PixelsMatrix(PCXImage sourceImage) {
        type = sourceImage.getType();
        width = sourceImage.getWidth();
        height = sourceImage.getHeight();
        resolution = width * height;

        initPixels();

        this.setFromImageBytes(sourceImage.getDecodedImageBytes()); //ustaw zawartosc macierzy na podstawie pliku obrazowego
    }
    
    public PixelsMatrix(PCXImage sourceImage, boolean b){
        type = sourceImage.getType();
        width = sourceImage.getWidth();
        height = sourceImage.getHeight();
        resolution = width * height;

        initPixels();
        clear();
    }
    
    public PixelsMatrix(ArrayList<Integer> imageBytes, int width, int height, Type type){
        this.type = type;
        this.width = width;
        this.height = height;
        resolution = width * height;

        initPixels();

        this.setFromImageBytes(imageBytes); //ustaw zawartosc macierzy na podstawie pliku obrazowego
    }

    public void initPixels() {
        switch (type) {
            case MONOB: {
                pixelsMono = new int[width][height];
                clear();
            }
            break;
            case GRAY8: {
                pixelsGray = new int[width][height];
                clear();
            }
            break;
            case RGB24: {
                pixelsRGB = new PixelRGB[width][height];
                clear();
            }
            break;
            default:
                Tool.error("Undefinied pixels type!");
                break;
        }
    }

    public void setPixelAt(int col, int row, int value) throws Exception {  //TYLKO SZARE I MONO
        if(col<0 || col>width || row<0 || row>height) throw new Exception("Piksel ("+col+", "+row+") jest poza zakresem obrazu!");
        switch (type) {
            case MONOB: {
                pixelsMono[col][row] = value;
            }
            break;
            case GRAY8: {
                pixelsGray[col][row] = value;
            }
            break;
            default:
                Tool.error("Próba użycia metody dla piksli szarych/mono, mimo że takie nie są");
                break;
        }
    }

    public void setPixelAt(int col, int row, int rValue, int gValue, int bValue) throws Exception {    //PIKSLE RGB
        if(col<0 || col>width || row<0 || row>height) throw new Exception("Piksel ("+col+", "+row+") jest poza zakresem obrazu!");
        if (type != Type.RGB24) {
            Tool.error("Próba ustawienia piksela NIE RGB metodą dla pikseli RGB!");
        }

        pixelsRGB[col][row].setRGB(rValue, gValue, bValue);
    }

    public int getPixelAt(int col, int row) throws Exception {
        if(col<0 || col>width || row<0 || row>height) throw new Exception("Piksel ("+col+", "+row+") jest poza zakresem obrazu!");

        switch (type) {
            case MONOB:
                return pixelsMono[col][row];
            case GRAY8:
                return pixelsGray[col][row];
        }
        throw new Exception("proba pobrania piksela innego niz mono/szary");
    }

    public int getPixelRat(int col, int row) throws Exception {
        if(col<0 || col>width || row<0 || row>height) throw new Exception("Piksel ("+col+", "+row+") jest poza zakresem obrazu!");

        return this.pixelsRGB[col][row].getR();
    }

    public int getPixelGat(int col, int row) throws Exception {
       if(col<0 || col>width || row<0 || row>height) throw new Exception("Piksel ("+col+", "+row+") jest poza zakresem obrazu!");
       
        return this.pixelsRGB[col][row].getG();
    }

    public int getPixelBat(int col, int row) throws Exception {
       if(col<0 || col>width || row<0 || row>height) throw new Exception("Piksel ("+col+", "+row+") jest poza zakresem obrazu!");
       
        return this.pixelsRGB[col][row].getB();
    }

    public void setPixelRat(int col, int row, int rValue) throws Exception {
       if(col<0 || col>width || row<0 || row>height) throw new Exception("Piksel ("+col+", "+row+") jest poza zakresem obrazu!");
       
        pixelsRGB[col][row].setR(rValue);
    }

    public void setPixelGat(int col, int row, int gValue) throws Exception {
        if(col<0 || col>width || row<0 || row>height) throw new Exception("Piksel ("+col+", "+row+") jest poza zakresem obrazu!");
        
        pixelsRGB[col][row].setG(gValue);
    }

    public void setPixelBat(int col, int row, int bValue) throws Exception {
        if(col<0 || col>width || row<0 || row>height) throw new Exception("Piksel ("+col+", "+row+") jest poza zakresem obrazu!");
        
        pixelsRGB[col][row].setB(bValue);
    }

    private void setFromImageBytes(ArrayList<Integer> imageBytes) {
        //log("PixelsMatrix: setFromImageBytes > converting decodedImageBytes "+imageBytes.size()+" to matrix "+width+"x"+height);
        switch (type) {
            case RGB24: {
                int scanLineLength = width * 3;

                //rozbij imageBytes na listy kolorow
                ArrayList<Integer> listR = new ArrayList<>(resolution);
                ArrayList<Integer> listG = new ArrayList<>(resolution);
                ArrayList<Integer> listB = new ArrayList<>(resolution);

                for (int row = 0; row < height; row++) {
                    for (int i = 0; i < width; i++) {
                        listR.add(imageBytes.get(i + row * scanLineLength));
                    }
                    for (int i = width; i < (2 * width); i++) {
                        listG.add(imageBytes.get(i + row * scanLineLength));
                    }
                    for (int i = 2 * width; i < (3 * width); i++) {
                        listB.add(imageBytes.get(i + row * scanLineLength));
                    }
                }

                //przekonwertuj listy kolorow na macierz pikseli RGB
                for (int row = 0; row < height; row++) {
                    for (int col = 0; col < width; col++) {
                        pixelsRGB[col][row].setRGB(listR.get(col + row * width),
                                listG.get(col + row * width),
                                listB.get(col + row * width));
                    }
                }
            }
            break;
            case GRAY8: {
                for (int row = 0; row < height; row++) {
                    for (int col = 0; col < width; col++) {
                        pixelsGray[col][row] = imageBytes.get(col + row * width);
                    }
                }
            }
            break;
            case MONOB: {
                for (int row = 0; row < height; row++) {
                    for (int col = 0; col < width; col++) {
                        pixelsMono[col][row] = imageBytes.get(col + row * width);
                    }
                }
            }
            break;
        }
    }

    public ArrayList<Integer> getImageBytes() throws Exception {
        //log("PixelsMatrix: getImageBytes > converting matrix "+width+"x"+height+ " to decodedImageBytes");
        ArrayList<Integer> imageBytes = new ArrayList<Integer>();

        switch (type) {
            case RGB24: {
                //Zamien macierz na listy kolorow
                ArrayList<Integer> listR = new ArrayList<Integer>();
                ArrayList<Integer> listG = new ArrayList<Integer>();
                ArrayList<Integer> listB = new ArrayList<Integer>();

                for (int row = 0; row < height; row++) {    //przejdz wierszach calej macierzy
                    for (int col = 0; col < width; col++) {
                        listR.add(pixelsRGB[col][row].getR());
                        listG.add(pixelsRGB[col][row].getG());
                        listB.add(pixelsRGB[col][row].getB());
                    }
                }

                int i = 0;
                for (int row = 0; row < height; row++) {
                    for (int j = i; j < width * (row + 1); j++) {
                        imageBytes.add(listR.get(j));
                    }
                    for (int j = i; j < width * (row + 1); j++) {
                        imageBytes.add(listG.get(j));
                    }
                    for (int j = i; j < width * (row + 1); j++) {
                        imageBytes.add(listB.get(j));
                    }
                    i += width;
                }
            }
            break;
            case GRAY8: {
                for (int row = 0; row < height; row++) {
                    for (int col = 0; col < width; col++) {
                        imageBytes.add(pixelsGray[col][row]);
                    }
                }
            }
            break;
            case MONOB: {
                for (int row = 0; row < height; row++) {
                    for (int col = 0; col < width; col++) {
                        imageBytes.add(pixelsMono[col][row]);
                    }
                }
            }
            break;
        }
        
        //log("\tdługość listy bajtów: "+imageBytes.size());
        return imageBytes;
    }

    public void clear() {    //zapisz w macierzy zera
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                switch (type) {
                    case MONOB:
                        pixelsMono[col][row] = 0;
                        break;
                    case GRAY8:
                        pixelsGray[col][row] = 0;
                        break;
                    case RGB24:
                        pixelsRGB[col][row] = new PixelRGB(0, 0, 0);
                        break;
                }
            }
        }
    }

}
