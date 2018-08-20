/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpo_gui;

import java.util.ArrayList;
import static wpo_gui.Type.GRAY8;

/**
 *
 * @author Ja
 */
public class ArithmeticOperations {

    //zad1.1a
    public static PCXImage addValueToImage(PCXImage inputImage1, double value) throws Exception {
        PixelsMatrix inputMatrix = new PixelsMatrix(inputImage1);
        PixelsMatrix outputMatrix = new PixelsMatrix(inputImage1);
        outputMatrix.clear();

        for (int row = 0; row < inputImage1.getHeight(); row++) {
            for (int col = 0; col < inputImage1.getWidth(); col++) {
                switch (inputImage1.getType()) {
                    case GRAY8: {
                        int newValue = (int) (inputMatrix.getPixelAt(col, row) + value);
                        outputMatrix.setPixelAt(col, row, newValue);
                    }
                    break;
                    case RGB24: {
                        int newR = (int) (inputMatrix.getPixelRat(col, row) + value);
                        int newG = (int) (inputMatrix.getPixelGat(col, row) + value);
                        int newB = (int) (inputMatrix.getPixelBat(col, row) + value);

                        outputMatrix.setPixelAt(col, row, newR, newG, newB);
                    }
                    break;
                    default: throw new Exception("Operacja niezdefiniowana dla tego typu obrazu!");
                }
            }
        }

        return new PCXImage(outputMatrix.getImageBytes(), inputImage1);
    }

    //zad1.1b
    public static PCXImage addImages(PCXImage inputImage1, PCXImage inputImage2) throws Exception {
        PixelsMatrix inputMatrix1 = new PixelsMatrix(inputImage1);
        PixelsMatrix inputMatrix2 = new PixelsMatrix(inputImage2);
        PixelsMatrix outputMatrix = new PixelsMatrix(inputImage1);
        outputMatrix.clear();
        
        for (int row = 0; row < inputImage1.getHeight(); row++) {
            for (int col = 0; col < inputImage1.getWidth(); col++) {
                switch (inputImage1.getType()) {
                    case GRAY8: {
                        int newValue = inputMatrix1.getPixelAt(col, row) + inputMatrix2.getPixelAt(col, row);
                        outputMatrix.setPixelAt(col, row, newValue);
                    }
                    break;
                    case RGB24: {
                        int newR = inputMatrix1.getPixelRat(col, row) + inputMatrix2.getPixelRat(col, row);
                        int newG = inputMatrix1.getPixelGat(col, row) + inputMatrix2.getPixelGat(col, row);
                        int newB = inputMatrix1.getPixelBat(col, row) + inputMatrix2.getPixelBat(col, row);
                        outputMatrix.setPixelAt(col, row, newR, newG, newB);
                    }
                    break;
                    default: throw new Exception("Operacja niezdefiniowana dla tego typu obrazu!");
                }
            }
        }
        
        return new PCXImage(outputMatrix.getImageBytes(), inputImage1);
    }

    //zad1.2a
    public static PCXImage multiplyImageByValue(PCXImage inputImage1, double value) throws Exception {
        PixelsMatrix inputMatrix = new PixelsMatrix(inputImage1);
        PixelsMatrix outputMatrix = new PixelsMatrix(inputImage1);
        outputMatrix.clear();
        for (int row = 0; row < inputImage1.getHeight(); row++) {
            for (int col = 0; col < inputImage1.getWidth(); col++) {
                switch (inputImage1.getType()) {
                    case GRAY8: {
                        int newValue = (int) (inputMatrix.getPixelAt(col, row) * value);
                        outputMatrix.setPixelAt(col, row, newValue);
                    }
                    break;
                    case RGB24: {
                        int newR = (int) (inputMatrix.getPixelRat(col, row) * value);
                        int newG = (int) (inputMatrix.getPixelGat(col, row) * value);
                        int newB = (int) (inputMatrix.getPixelBat(col, row) * value);

                        outputMatrix.setPixelAt(col, row, newR, newG, newB);
                    }
                    break;
                    default: throw new Exception("Operacja niezdefiniowana dla tego typu obrazu!");
                }
            }
        }

        return new PCXImage(outputMatrix.getImageBytes(), inputImage1);
    }

    //zad1.2b
    public static PCXImage multiplyImages(PCXImage inputImage1, PCXImage inputImage2) throws Exception {
        PixelsMatrix inputMatrix1 = new PixelsMatrix(inputImage1);
        PixelsMatrix inputMatrix2 = new PixelsMatrix(inputImage2);
        PixelsMatrix outputMatrix = new PixelsMatrix(inputImage1);
        outputMatrix.clear();

        for (int row = 0; row < inputImage1.getHeight(); row++) {
            for (int col = 0; col < inputImage1.getWidth(); col++) {
                switch (inputImage1.getType()) {
                    case GRAY8: {
                        int newValue = inputMatrix1.getPixelAt(col, row) * inputMatrix2.getPixelAt(col, row);
                        outputMatrix.setPixelAt(col, row, newValue);
                    }
                    break;
                    case RGB24: {
                        int newR = inputMatrix1.getPixelRat(col, row) * inputMatrix2.getPixelRat(col, row);
                        int newG = inputMatrix1.getPixelGat(col, row) * inputMatrix2.getPixelGat(col, row);
                        int newB = inputMatrix1.getPixelBat(col, row) * inputMatrix2.getPixelBat(col, row);
                        outputMatrix.setPixelAt(col, row, newR, newG, newB);
                    }
                    break;
                    default: throw new Exception("Operacja niezdefiniowana dla tego typu obrazu!");
                }
            }
        }

        return new PCXImage(outputMatrix.getImageBytes(), inputImage1);
    }

    //zad1.3
    public static PCXImage mixImages(PCXImage inputImage1, PCXImage inputImage2, double alpha) throws Exception {
        PixelsMatrix inputMatrix1 = new PixelsMatrix(inputImage1);
        PixelsMatrix inputMatrix2 = new PixelsMatrix(inputImage2);
        PixelsMatrix outputMatrix = new PixelsMatrix(inputImage1);
        outputMatrix.clear();

        for (int row = 0; row < inputImage1.getHeight(); row++) {
            for (int col = 0; col < inputImage1.getWidth(); col++) {
                switch (inputImage1.getType()) {
                    case GRAY8: {
                        int newValue = (int) (alpha * inputMatrix1.getPixelAt(col, row) + ((1 - alpha) * inputMatrix2.getPixelAt(col, row)));
                        outputMatrix.setPixelAt(col, row, newValue);
                    }
                    break;
                    case RGB24: {
                        int newR = (int) (alpha * inputMatrix1.getPixelRat(col, row) + ((1 - alpha) * inputMatrix2.getPixelRat(col, row)));
                        int newG = (int) (alpha * inputMatrix1.getPixelGat(col, row) + ((1 - alpha) * inputMatrix2.getPixelGat(col, row)));
                        int newB = (int) (alpha * inputMatrix1.getPixelBat(col, row) + ((1 - alpha) * inputMatrix2.getPixelBat(col, row)));

                        outputMatrix.setPixelAt(col, row, newR, newG, newB);
                    }
                    break;
                    default: throw new Exception("Operacja niezdefiniowana dla tego typu obrazu!");
                }
            }
        }

        return new PCXImage(outputMatrix.getImageBytes(), inputImage1);

    }

    //zad1.4
    public static PCXImage powerImageByValue(PCXImage inputImage1, double exponent) throws Exception {
        PixelsMatrix inputMatrix = new PixelsMatrix(inputImage1);
        PixelsMatrix outputMatrix = new PixelsMatrix(inputImage1);
        outputMatrix.clear();

        for (int row = 0; row < inputImage1.getHeight(); row++) {
            for (int col = 0; col < inputImage1.getWidth(); col++) {
                switch (inputImage1.getType()) {
                    case GRAY8: {
                        int newValue = (int) (Math.pow(inputMatrix.getPixelAt(col, row), exponent));
                        outputMatrix.setPixelAt(col, row, newValue);
                    }
                    break;
                    case RGB24: {
                        int newR = (int) (Math.pow(inputMatrix.getPixelRat(col, row), exponent));
                        int newG = (int) (Math.pow(inputMatrix.getPixelGat(col, row), exponent));
                        int newB = (int) (Math.pow(inputMatrix.getPixelBat(col, row), exponent));
                        
                        outputMatrix.setPixelAt(col, row, newR, newG, newB);
                    }
                    break;
                    default: throw new Exception("Operacja niezdefiniowana dla tego typu obrazu!");
                }
            }
        }

        return new PCXImage(outputMatrix.getImageBytes(), inputImage1);
    }

    public static PCXImage divideImageByNumber(PCXImage plikWe1, double d) throws Exception {
        if (d == 0) {
            throw new Exception("Nie mozna dzielić przez 0");
        }
        PixelsMatrix inputMatrix = new PixelsMatrix(plikWe1);
        PixelsMatrix outputMatrix = new PixelsMatrix(plikWe1);
        outputMatrix.clear();

        for (int row = 0; row < plikWe1.getHeight(); row++) {
            for (int col = 0; col < plikWe1.getWidth(); col++) {
                switch (plikWe1.getType()) {
                    case GRAY8: {
                        int value = (int) (inputMatrix.getPixelAt(col, row) / d);
                        outputMatrix.setPixelAt(col, row, value);
                    }
                    break;
                    case RGB24: {
                        int r = (int) (inputMatrix.getPixelRat(col, row) / d);
                        int g = (int) (inputMatrix.getPixelGat(col, row) / d);
                        int b = (int) (inputMatrix.getPixelBat(col, row) / d);

                        outputMatrix.setPixelAt(col, row, r, g, b);
                    }
                    break;
                    default: throw new Exception("Operacja niezdefiniowana dla tego typu obrazu!");
                }
            }
        }

        return new PCXImage(outputMatrix.getImageBytes(), plikWe1);
    }

    public static PCXImage divideImages(PCXImage plikWe1, PCXImage plikWe2) throws Exception {
        PixelsMatrix inputMatrix1 = new PixelsMatrix(plikWe1);
        PixelsMatrix inputMatrix2 = new PixelsMatrix(plikWe2);
        PixelsMatrix outputMatrix = new PixelsMatrix(plikWe1);

        for (int row = 0; row < inputMatrix1.height; row++) {
            for (int col = 0; col < inputMatrix1.width; col++) {
                switch (inputMatrix1.type) {
                    case GRAY8: {
                        if (inputMatrix2.getPixelAt(col, row) != 0) {
                            int dzielna = inputMatrix1.getPixelAt(col, row);
                            int dzielnik = inputMatrix2.getPixelAt(col, row);
                            outputMatrix.setPixelAt(col, row, (int) (dzielna / dzielnik));
                        } else {
                            outputMatrix.setPixelAt(col, row, inputMatrix1.getPixelAt(col, row)); //jeżeli drugi piksel=0 to przepisz tylko tez z pierwszego obrazu
                        }
                    }
                    break;
                    case RGB24: {
                        //j.w. tylko ze dla kazdego kanalu osobno
                        if (inputMatrix2.getPixelRat(col, row) != 0) {  //red
                            int dzielna = inputMatrix1.getPixelRat(col, row);
                            int dzielnik = inputMatrix2.getPixelRat(col, row);
                            outputMatrix.setPixelRat(col, row, (int) (dzielna / dzielnik));
                        } else {
                            outputMatrix.setPixelRat(col, row, inputMatrix1.getPixelRat(col, row)); //jeżeli drugi piksel=0 to przepisz tylko tez z pierwszego obrazu
                        }

                        if (inputMatrix2.getPixelGat(col, row) != 0) {  //green
                            int dzielna = inputMatrix1.getPixelGat(col, row);
                            int dzielnik = inputMatrix2.getPixelGat(col, row);
                            outputMatrix.setPixelGat(col, row, (int) (dzielna / dzielnik));
                        } else {
                            outputMatrix.setPixelGat(col, row, inputMatrix1.getPixelGat(col, row)); //jeżeli drugi piksel=0 to przepisz tylko tez z pierwszego obrazu
                        }

                        if (inputMatrix2.getPixelBat(col, row) != 0) {  //blue
                            int dzielna = inputMatrix1.getPixelBat(col, row);
                            int dzielnik = inputMatrix2.getPixelBat(col, row);
                            outputMatrix.setPixelBat(col, row, (int) (dzielna / dzielnik));
                        } else {
                            outputMatrix.setPixelBat(col, row, inputMatrix1.getPixelBat(col, row)); //jeżeli drugi piksel=0 to przepisz tylko tez z pierwszego obrazu
                        }
                    }
                    break;
                    default: throw new Exception("Operacja niezdefiniowana dla tego typu obrazu!");
                }
            }
        }
        return new PCXImage(outputMatrix.getImageBytes(), plikWe1);
    }

    static PCXImage rootImage(PCXImage plikWe1) throws Exception {
        PixelsMatrix inputMatrix = new PixelsMatrix(plikWe1);
        PixelsMatrix outputMatrix = new PixelsMatrix(plikWe1);
        outputMatrix.clear();
        
        for (int row = 0; row < inputMatrix.height; row++) {
            for (int col = 0; col < inputMatrix.width; col++) {
                switch (inputMatrix.type) {
                    case GRAY8: {
                        outputMatrix.setPixelAt(col, row, (int) (Math.sqrt(inputMatrix.getPixelAt(col, row))));
                    }
                    break;
                    case RGB24: {
                        outputMatrix.setPixelRat(col, row, (int) (Math.sqrt(inputMatrix.getPixelRat(col, row))));
                        outputMatrix.setPixelGat(col, row, (int) (Math.sqrt(inputMatrix.getPixelGat(col, row))));
                        outputMatrix.setPixelBat(col, row, (int) (Math.sqrt(inputMatrix.getPixelBat(col, row))));
                    }
                    break;
                    default: throw new Exception("Operacja niezdefiniowana dla tego typu obrazu!");
                }
            }
        }
        return new PCXImage(outputMatrix.getImageBytes(), plikWe1);
    }
}
