/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpo_gui;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import static wpo_gui.Tool.print;
import static wpo_gui.Tool.println;
import static wpo_gui.Type.*;

/**
 *
 * @author Ja
 */
public class Filtering {

    public static PCXImage filter(PCXImage inputImage, int[] maskMatrix) throws Exception {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        Type type = inputImage.getType();

        PixelsMatrix inputMatrix = new PixelsMatrix(inputImage);
        PixelsMatrix outputMatrix = new PixelsMatrix(inputImage);

        int sumaWagMaski = 0;
        for (int i = 0; i < maskMatrix.length; i++) {
            if (maskMatrix[i] == 0) {
                continue;
            }
            sumaWagMaski += maskMatrix[i];
        }
        if (sumaWagMaski == 0) {
            sumaWagMaski = 1;
        }

        switch (type) {
            case GRAY8: {
                for (int row = 0; row < height; row++) {  //przejdz po wszystkich pikselach
                    for (int col = 0; col < width; col++) {
                        int sumaWazona = maskMatrix[0] * getPixel(Channel.GRAY, col - 1, row - 1, inputMatrix)
                                + maskMatrix[1] * getPixel(Channel.GRAY, col, row - 1, inputMatrix)
                                + maskMatrix[2] * getPixel(Channel.GRAY, col + 1, row - 1, inputMatrix)
                                + maskMatrix[3] * getPixel(Channel.GRAY, col - 1, row, inputMatrix)
                                + maskMatrix[4] * getPixel(Channel.GRAY, col, row, inputMatrix)
                                + maskMatrix[5] * getPixel(Channel.GRAY, col + 1, row, inputMatrix)
                                + maskMatrix[6] * getPixel(Channel.GRAY, col - 1, row + 1, inputMatrix)
                                + maskMatrix[7] * getPixel(Channel.GRAY, col, row + 1, inputMatrix)
                                + maskMatrix[8] * getPixel(Channel.GRAY, col + 1, row + 1, inputMatrix);

                        outputMatrix.setPixelAt(col, row, sumaWazona / 1);
                    }

                }

            }
            break;
            case RGB24: {
                for (int row = 0; row < height; row++) {  //przejdz po wszystkich pikselach
                    for (int col = 0; col < width; col++) {
                        //czerwony
                        int sumaWazonaR = maskMatrix[0] * getPixel(Channel.RED, col - 1, row - 1, inputMatrix)
                                + maskMatrix[1] * getPixel(Channel.RED, col, row - 1, inputMatrix)
                                + maskMatrix[2] * getPixel(Channel.RED, col + 1, row - 1, inputMatrix)
                                + maskMatrix[3] * getPixel(Channel.RED, col - 1, row, inputMatrix)
                                + maskMatrix[4] * getPixel(Channel.RED, col, row, inputMatrix)
                                + maskMatrix[5] * getPixel(Channel.RED, col + 1, row, inputMatrix)
                                + maskMatrix[6] * getPixel(Channel.RED, col - 1, row + 1, inputMatrix)
                                + maskMatrix[7] * getPixel(Channel.RED, col, row + 1, inputMatrix)
                                + maskMatrix[8] * getPixel(Channel.RED, col + 1, row + 1, inputMatrix);

                        outputMatrix.setPixelRat(col, row, sumaWazonaR / sumaWagMaski);

                        //zielony
                        int sumaWazonaG = maskMatrix[0] * getPixel(Channel.GREEN, col - 1, row - 1, inputMatrix)
                                + maskMatrix[1] * getPixel(Channel.GREEN, col, row - 1, inputMatrix)
                                + maskMatrix[2] * getPixel(Channel.GREEN, col + 1, row - 1, inputMatrix)
                                + maskMatrix[3] * getPixel(Channel.GREEN, col - 1, row, inputMatrix)
                                + maskMatrix[4] * getPixel(Channel.GREEN, col, row, inputMatrix)
                                + maskMatrix[5] * getPixel(Channel.GREEN, col + 1, row, inputMatrix)
                                + maskMatrix[6] * getPixel(Channel.GREEN, col - 1, row + 1, inputMatrix)
                                + maskMatrix[7] * getPixel(Channel.GREEN, col, row + 1, inputMatrix)
                                + maskMatrix[8] * getPixel(Channel.GREEN, col + 1, row + 1, inputMatrix);

                        outputMatrix.setPixelGat(col, row, sumaWazonaG / sumaWagMaski);

                        //niebieski
                        int sumaWazonaB = maskMatrix[0] * getPixel(Channel.BLUE, col - 1, row - 1, inputMatrix)
                                + maskMatrix[1] * getPixel(Channel.BLUE, col, row - 1, inputMatrix)
                                + maskMatrix[2] * getPixel(Channel.BLUE, col + 1, row - 1, inputMatrix)
                                + maskMatrix[3] * getPixel(Channel.BLUE, col - 1, row, inputMatrix)
                                + maskMatrix[4] * getPixel(Channel.BLUE, col, row, inputMatrix)
                                + maskMatrix[5] * getPixel(Channel.BLUE, col + 1, row, inputMatrix)
                                + maskMatrix[6] * getPixel(Channel.BLUE, col - 1, row + 1, inputMatrix)
                                + maskMatrix[7] * getPixel(Channel.BLUE, col, row + 1, inputMatrix)
                                + maskMatrix[8] * getPixel(Channel.BLUE, col + 1, row + 1, inputMatrix);

                        outputMatrix.setPixelBat(col, row, sumaWazonaB / sumaWagMaski);
                    }
                }

            }
            break;
            default: {
                Tool.error("Filtr zdefiniowany tylko dla obrazów szarych i RGB");
                return null;
            }

        }
        return new PCXImage(outputMatrix.getImageBytes(), inputImage);
    }

    public static int getPixel(Channel whichChannel, int col, int row, PixelsMatrix pixelsMatrix) throws Exception {
        int width = pixelsMatrix.width;
        int height = pixelsMatrix.height;

        if (col > 0 && row > 0 && col < width && row < height) {
            switch (whichChannel) {
                case GRAY:
                    return pixelsMatrix.getPixelAt(col, row);
                case RED:
                    return pixelsMatrix.getPixelRat(col, row);
                case GREEN:
                    return pixelsMatrix.getPixelGat(col, row);
                case BLUE:
                    return pixelsMatrix.getPixelBat(col, row);
            }
        }

        return 0;
    }

    public static PCXImage medianFilter(PCXImage inputImage) throws Exception {
        PixelsMatrix inputMatrix = new PixelsMatrix(inputImage);
        PixelsMatrix outputMatrix = new PixelsMatrix(inputImage);
        outputMatrix.clear();
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        Type type = inputImage.getType();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                switch (type) {
                    case GRAY8: {
                        int newValue = median(new int[]{
                                getPixel(Channel.GRAY, col - 1, row - 1, inputMatrix),
                                getPixel(Channel.GRAY, col, row - 1, inputMatrix),
                                getPixel(Channel.GRAY, col + 1, row - 1, inputMatrix),
                                getPixel(Channel.GRAY, col - 1, row, inputMatrix),
                                getPixel(Channel.GRAY, col, row, inputMatrix),
                                getPixel(Channel.GRAY, col + 1, row, inputMatrix),
                                getPixel(Channel.GRAY, col - 1, row + 1, inputMatrix),
                                getPixel(Channel.GRAY, col, row + 1, inputMatrix),
                                getPixel(Channel.GRAY, col + 1, row + 1, inputMatrix)}
                        );
                        
                        outputMatrix.setPixelAt(col, row, newValue);
                    }
                    break;
                    case RGB24: {
                        int newR = median(new int[]{
                                getPixel(Channel.RED, col - 1, row - 1, inputMatrix),
                                getPixel(Channel.RED, col, row - 1, inputMatrix),
                                getPixel(Channel.RED, col + 1, row - 1, inputMatrix),
                                getPixel(Channel.RED, col - 1, row, inputMatrix),
                                getPixel(Channel.RED, col, row, inputMatrix),
                                getPixel(Channel.RED, col + 1, row, inputMatrix),
                                getPixel(Channel.RED, col - 1, row + 1, inputMatrix),
                                getPixel(Channel.RED, col, row + 1, inputMatrix),
                                getPixel(Channel.RED, col + 1, row + 1, inputMatrix)}
                        );
                        
                        int newG = median(new int[]{
                                getPixel(Channel.GREEN, col - 1, row - 1, inputMatrix),
                                getPixel(Channel.GREEN, col, row - 1, inputMatrix),
                                getPixel(Channel.GREEN, col + 1, row - 1, inputMatrix),
                                getPixel(Channel.GREEN, col - 1, row, inputMatrix),
                                getPixel(Channel.GREEN, col, row, inputMatrix),
                                getPixel(Channel.GREEN, col + 1, row, inputMatrix),
                                getPixel(Channel.GREEN, col - 1, row + 1, inputMatrix),
                                getPixel(Channel.GREEN, col, row + 1, inputMatrix),
                                getPixel(Channel.GREEN, col + 1, row + 1, inputMatrix)}
                        );
                        
                        int newB = median(new int[]{
                                getPixel(Channel.BLUE, col - 1, row - 1, inputMatrix),
                                getPixel(Channel.BLUE, col, row - 1, inputMatrix),
                                getPixel(Channel.BLUE, col + 1, row - 1, inputMatrix),
                                getPixel(Channel.BLUE, col - 1, row, inputMatrix),
                                getPixel(Channel.BLUE, col, row, inputMatrix),
                                getPixel(Channel.BLUE, col + 1, row, inputMatrix),
                                getPixel(Channel.BLUE, col - 1, row + 1, inputMatrix),
                                getPixel(Channel.BLUE, col, row + 1, inputMatrix),
                                getPixel(Channel.BLUE, col + 1, row + 1, inputMatrix)}
                        );
                        
                        outputMatrix.setPixelAt(col, row, newR, newG, newB);
                    }
                    break;
                    default:
                        throw new Exception("Filtrowanie medianowe zdefiniowano tylko dla obrazów szarych i kolorowych!");
                }
            }
        }
        
        

        return new PCXImage(outputMatrix.getImageBytes(), inputImage);
    }

    private static int median(int[] tab) {
        //uporzadkuj wartosci piksli rosnaco
        Arrays.sort(tab);
        
        return tab[5];
    }

    
  
}
