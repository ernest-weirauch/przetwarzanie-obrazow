/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpo_gui;

import java.util.ArrayList;
import static wpo_gui.Tool.println;

/**
 *
 * @author Ja
 */
public class MorphologicOperations {

    public static PCXImage erodeImage(PCXImage image1) throws Exception {
        ArrayList<Integer> imageBytes = image1.getDecodedImageBytes();
        ArrayList<Integer> outputBytes = new ArrayList<Integer>(image1.getResolution());
        for (int i = 0; i < image1.getResolution(); i++) {
            outputBytes.add(0);
        }

        int width = image1.getWidth();
        int height = image1.getHeight();
        Type type = image1.getType();
        int min = 255;  //min=max

        if (type != Type.MONOB && type != Type.GRAY8) {
            throw new Exception("erozja tylko dla obrazów mono i szarych!");
        }

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                min = getMinNeighborOfPixel(col, row, imageBytes, width, height);
                outputBytes.set(col + row * width, min);
            }
        }

        return new PCXImage(outputBytes, image1);
    }

    public static PCXImage dylateImage(PCXImage image1) throws Exception {
        ArrayList<Integer> imageBytes = image1.getDecodedImageBytes();
        ArrayList<Integer> outputBytes = new ArrayList<Integer>(image1.getResolution());
        for (int i = 0; i < image1.getResolution(); i++) {
            outputBytes.add(0);
        }

        int width = image1.getWidth();
        int height = image1.getHeight();
        Type type = image1.getType();
        int max = 0;  //min=max

        if (type != Type.MONOB && type != Type.GRAY8) {
            throw new Exception("erozja tylko dla obrazów mono i szarych!");
        }

     
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                max = getMaxNeighborOfPixel(col, row, imageBytes, width, height);
                outputBytes.set(col + row * width, max);
            }
        }

        return new PCXImage(outputBytes, image1);    }

    public static int getMinNeighborOfPixel(int col, int row, ArrayList<Integer> imageBytes, int width, int height) throws Exception {
        ArrayList<Integer> neighbors = new ArrayList<>();

        if (isPixelExist(col, row - 1, width, height)) {//piksel powyzej
            neighbors.add(imageBytes.get(col + (row - 1) * width));
        }

        if (isPixelExist(col + 1, row, width, height)) {   //piksel z prawej strony
            neighbors.add(imageBytes.get((col + 1) + row * width));
        }

        if (isPixelExist(col, row + 1, width, height)) {//piksel z dolu
            neighbors.add(imageBytes.get(col + (row + 1) * width));
        }

        if (isPixelExist(col - 1, row, width, height)) {    //piksel z lewej
            neighbors.add(imageBytes.get((col - 1) + row * width));
        }

        int min = neighbors.get(0);
        for (int i = 0; i < neighbors.size(); i++) {
            if (neighbors.get(i) < min) {
                min = neighbors.get(i);
            }
        }

        return min;
    }

    public static int getMinNeighborOfPixel(int col, int row, PixelsMatrix pixelsMatrix) throws Exception {
        ArrayList<Integer> neighbors = new ArrayList<>();

        switch (pixelsMatrix.type) {
            case MONOB: {
                throw new Exception("mono jeszcze nie wspierane!");
            }
            case GRAY8: {
                //czy pozycje istnieja, jesli tak to dodaj piksele z tych pozycji do listy sąsiadów

                if (isPixelExist(col, row - 1, pixelsMatrix)) {//piksel powyzej
                    neighbors.add(pixelsMatrix.getPixelAt(col, row - 1));
                }

                if (isPixelExist(col + 1, row, pixelsMatrix)) {   //piksel z prawej strony
                    neighbors.add(pixelsMatrix.getPixelAt(col + 1, row));
                }

                if (isPixelExist(col, row + 1, pixelsMatrix)) {//piksel z dolu
                    neighbors.add(pixelsMatrix.getPixelAt(col, row + 1));
                }

                if (isPixelExist(col - 1, row, pixelsMatrix)) {    //piksel z lewej
                    neighbors.add(pixelsMatrix.getPixelAt(col - 1, row));
                }

                if (neighbors.isEmpty()) {
                    throw new Exception("MorphologicOperations\\getMinNeighborOfPixel: brak sąsiadów?");
                }
            }
            break;
        }

        int min = neighbors.get(0);
        for (int i = 0; i < neighbors.size(); i++) {
            if (neighbors.get(i) < min) {
                min = neighbors.get(i);
            }
        }

        return min;
    }
    
    public static int getMaxNeighborOfPixel(int col, int row, ArrayList<Integer> imageBytes, int width, int height) throws Exception {
        ArrayList<Integer> neighbors = new ArrayList<>();

        if (isPixelExist(col, row - 1, width, height)) {//piksel powyzej
            neighbors.add(imageBytes.get(col + (row - 1) * width));
        }

        if (isPixelExist(col + 1, row, width, height)) {   //piksel z prawej strony
            neighbors.add(imageBytes.get((col + 1) + row * width));
        }

        if (isPixelExist(col, row + 1, width, height)) {//piksel z dolu
            neighbors.add(imageBytes.get(col + (row + 1) * width));
        }

        if (isPixelExist(col - 1, row, width, height)) {    //piksel z lewej
            neighbors.add(imageBytes.get((col - 1) + row * width));
        }

        if (neighbors.isEmpty()) {
            throw new Exception("MorphologicOperations\\getMinNeighborOfPixel: brak sąsiadów?");
        }

        int max = neighbors.get(0);
        for (int i = 0; i < neighbors.size(); i++) {
            if (neighbors.get(i) > max) {
                max= neighbors.get(i);
            }
        }

        return max;
    }


    public static int getMaxNeighborOfPixel(int col, int row, PixelsMatrix pixelsMatrix) throws Exception {
        ArrayList<Integer> neighbors = new ArrayList<>();

        //czy pozycje istnieja, jesli tak to dodaj piksele z tych pozycji do listy sąsiadów
        if (isPixelExist(col, row - 1, pixelsMatrix)) {//piksel powyzej
            neighbors.add(pixelsMatrix.getPixelAt(col, row - 1));
        }

        if (isPixelExist(col + 1, row, pixelsMatrix)) {   //piksel z prawej strony
            neighbors.add(pixelsMatrix.getPixelAt(col + 1, row));
        }

        if (isPixelExist(col, row + 1, pixelsMatrix)) {//piksel z dolu
            neighbors.add(pixelsMatrix.getPixelAt(col, row + 1));
        }

        if (isPixelExist(col - 1, row, pixelsMatrix)) {    //piksel z lewej
            neighbors.add(pixelsMatrix.getPixelAt(col - 1, row));
        }

        if (neighbors.isEmpty()) {
            throw new Exception("MorphologicOperations\\getMaxNeighborOfPixel: brak sąsiadów?");
        }

        int max = neighbors.get(0);
        for (int i = 0; i < neighbors.size(); i++) {
            if (neighbors.get(i) > max) {
                max = neighbors.get(i);
            }
        }

        return max;
    }

    public static boolean isPixelExist(int col, int row, PixelsMatrix pixelMatrix) throws Exception {
        int width = pixelMatrix.width;
        int height = pixelMatrix.height;

        if (col > 0 && row > 0 && col < width && row < height) {
            return true;
        }
        return false;
    }

    public static boolean isPixelExist(int col, int row, int width, int height) {
        if (col < 0 || row < 0 || col >= width || row >= height) {
            return false;
        }
        return true;
    }
}
