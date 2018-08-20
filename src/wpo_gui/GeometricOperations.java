/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpo_gui;

import java.util.ArrayList;
import javax.swing.plaf.synth.Region;

/**
 *
 * @author Ja
 */
public class GeometricOperations {

//    //zad 3.1 - Przemieszczenie obrazu o wektor
    public static PCXImage moveImageByVector(PCXImage inputImage1, int dX, int dY) throws Exception { //ok
        PixelsMatrix inputMatrix = new PixelsMatrix(inputImage1);

        Type type = inputMatrix.type;
        int outputWidth = inputMatrix.width;
        int outputHeight = inputMatrix.height;

        PixelsMatrix outputMatrix = new PixelsMatrix(inputImage1);
        outputMatrix.type = type;
        outputMatrix.width = outputWidth;
        outputMatrix.height = outputHeight;

        outputMatrix.clear();   //wyczysc macierz wyjsciowa

        for (int row = 0; row < inputMatrix.height; row++) {
            for (int col = 0; col < inputMatrix.width; col++) {
                int newX = col + dX;
                int newY = row + dY;

                if ((newY > 0 && newY < outputHeight) && (newX > 0 && newX < outputWidth)) {//jezeli nie przekroczono zakresu przesunieciem
                    switch (type) {
                        case MONOB:
                        case GRAY8:
                            outputMatrix.setPixelAt(newX, newY, inputMatrix.getPixelAt(col, row));
                            break;
                        case RGB24:
                            outputMatrix.setPixelAt(newX, newY, inputMatrix.getPixelRat(col, row), inputMatrix.getPixelGat(col, row), inputMatrix.getPixelBat(col, row));
                            break;
                    }
                }
            }
        }

        return new PCXImage(outputMatrix.getImageBytes(), inputImage1);
    }

//    //zad 3.2 - Skalowanie jednorodne i niejednorodne obrazu
    public static PCXImage scaleImage(PCXImage inputImage1, double sX, double sY) throws Exception {
        ArrayList<Integer> imageBytes = new ArrayList<Integer>();
        ArrayList<Integer> outputBytes = new ArrayList<Integer>();
        
        int oldWidth = inputImage1.getWidth();
        int oldHeight = inputImage1.getHeight();
        int newWidth = (int) Math.ceil(oldWidth * sX);
        int newHeight = (int) Math.ceil(oldHeight * sY);
        int bytesPerLine = inputImage1.getBytesPerLine();

        for (int row = 0; row < newHeight; row++) {
            for (int col = 0; col < newWidth; col++) {
                double baseX = col * (double) oldWidth / newWidth;
                double baseY = row * (double) oldHeight / newHeight;

                int newCol = (int) Math.floor(baseX);
                int newRow = (int) Math.floor(baseY);
                double dx = baseX - newCol;
                double dy = baseY - newRow;
                int x, y;
                if (newCol > oldWidth) {
                    x = newCol;
                } else {
                    x = newCol + 1;
                }
                if (newRow > oldHeight) {
                    y = newRow;
                } else {
                    y = newRow + 1;
                }
                double result = (1 - dy) * imageBytes.get(newRow * bytesPerLine + newCol) * (1 - dx) + imageBytes.get(y * bytesPerLine + newCol)
                        * dx + imageBytes.get(y * bytesPerLine + newCol) * (1 - dx) + imageBytes.get(y * bytesPerLine + x) * dx * dy;

                outputBytes.add((int) result);
            }
        }

        PCXImage tmp = new PCXImage(inputImage1);
        tmp.setResolution(newWidth, newHeight);
        return new PCXImage(outputBytes, tmp);
    }

//    //zad 3.3 - obracanie obrazu o kat
    public static PCXImage rotateImage(PCXImage inputImage1, int angle) {
        return null;
//        PixelsMatrix inputMatrix = new PixelsMatrix(inputImage1);
//        PixelsMatrix outputMatrix = new PixelsMatrix(inputImage1);
//        
//        
////        ArrayList<Integer> inputImageBytes = inputImage1.getDecodedImageBytes();
////        ArrayList<Integer> outputImageBytes = new ArrayList<>();
////        
////        int width = inputImage1.getWidth();
////        int height = inputImage1.getHeight();
//        double sin = Math.sin(Math.toRadians(angle));      
//        double cos = Math.cos(Math.toRadians(angle));      
//
//        int newCol;
//        int newRow;
//        if (sin >= 0 && cos >= 0) {
//            newCol = (int) Math.round(sin * height);
//            newRow = 0;
//        } else if (sin <= 0 && cos <= 0) {
//            newCol = (int) Math.round(cos * (-height));
//            newRow = (int) Math.round(cos * (-height) - (sin * width));
//        } else if (sin >= 0 && cos <= 0) {
//            newCol = (int) Math.round(height * sin - width * cos);
//            newRow = (int) Math.round(height * (-cos));
//        } else {
//            newCol = 0;
//            newRow = (int) Math.round(sin * (-width));
//        }
////        
////        int newHeight = (int) Math.ceil(Math.abs(height * cos) + Math.abs(width* sin));
////        int newWidth = (int) Math.ceil(Math.abs(width * cos) + Math.abs(height * sin));
////        int startX = 1 - newCol, endX = newWidth - newCol, startY = 1 - newRow, endY = newHeight - newRow;
//////        int map[][] = new int[newHeight][newWidth];
////
////        for (int row = 0; row < height; row++) {
////            for (int col = 0; col < width; col++) {
////                double x1 = col * cos - row * sin;
////                double y1 = row * sin - col * cos;
////                if (x1 < 1 || y1 < 1 || x1 > width || y1 > height) {
////                    continue;
////                }
////                int var = inputImageBytes.get((int) (Math.ceil(x1) * width + Math.ceil(y1)));
////                outputImageBytes.set((col+newRow) + row*width, height)map[j + newRow][i + newCol] = var;
////            }
////        }
////        for (int i = 0; i < newHeight; i++) {
////            for (int j = 0; j < newWidth; j++) {
////                outputMatrix.add(map[i][j]);
////            }
////        }
////        return null;

    }

    static PCXImage cutImageFragment(PCXImage plikWe1, int x1, int y1, int x2, int y2) throws Exception {
        PixelsMatrix inputMatrix = new PixelsMatrix(plikWe1);
        PixelsMatrix outputMatrix = new PixelsMatrix(plikWe1);  //przepisz wszystko
        //outputMatrix.clear();
        Type type = inputMatrix.type;
        int width = plikWe1.getWidth();
        int height = plikWe1.getWidth();

        for (int row = y1; row < y2; row++) {
            for (int col = x1; col < x2; col++) {
                switch (type) {
                    case GRAY8: {
                        outputMatrix.setPixelAt(col, row, 0);
                    }
                    break;
                    case RGB24: {
                        outputMatrix.setPixelRat(col, row, 0);
                        outputMatrix.setPixelGat(col, row, 0);
                        outputMatrix.setPixelBat(col, row, 0);
                    }
                    break;
                    default:
                        throw new Exception("Wycinianie obrazu obsługiwane tylko dla obrazów szarych i kolorowych!");
                }
            }
        }
        return new PCXImage(outputMatrix.getImageBytes(), plikWe1);
    }

    static PCXImage copyImageFragment(PCXImage plikWe1, int x1, int y1, int x2, int y2) throws Exception {
        PixelsMatrix inputMatrix = new PixelsMatrix(plikWe1);
        PixelsMatrix outputMatrix = new PixelsMatrix(plikWe1);
        outputMatrix.clear();
        Type type = inputMatrix.type;

        for (int row = y1; row < y2; row++) {
            for (int col = x1; col < x2; col++) {
                switch (type) {
                    case GRAY8: {
                        outputMatrix.setPixelAt(col, row, inputMatrix.getPixelAt(col, row));
                    }
                    break;
                    case RGB24: {
                        outputMatrix.setPixelRat(col, row, inputMatrix.getPixelRat(col, row));
                        outputMatrix.setPixelGat(col, row, inputMatrix.getPixelGat(col, row));
                        outputMatrix.setPixelBat(col, row, inputMatrix.getPixelBat(col, row));
                    }
                    break;
                    default:
                        throw new Exception("Wycinianie obrazu obsługiwane tylko dla obrazów szarych i kolorowych!");
                }
            }
        }
        return new PCXImage(outputMatrix.getImageBytes(), plikWe1);
    }

    static PCXImage verticalSymmetry(PCXImage plikWe1) throws Exception {
        PixelsMatrix inputMatrix = new PixelsMatrix(plikWe1);
        PixelsMatrix outputMatrix = new PixelsMatrix(plikWe1);
        outputMatrix.clear();
        int width = plikWe1.getWidth();
        int height = plikWe1.getHeight();
        Type type = plikWe1.getType();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width / 2; col++) {
                switch (type) {
                    case GRAY8: {
                        outputMatrix.setPixelAt(col, row, inputMatrix.getPixelAt(width - col - 1, row));
                        outputMatrix.setPixelAt(width - col - 1, row, inputMatrix.getPixelAt(col, row));
                    }
                    break;
                    case RGB24: {
                        outputMatrix.setPixelRat(col, row, inputMatrix.getPixelRat(width - col - 1, row));
                        outputMatrix.setPixelRat(width - col - 1, row, inputMatrix.getPixelRat(col, row));

                        outputMatrix.setPixelGat(col, row, inputMatrix.getPixelGat(width - col - 1, row));
                        outputMatrix.setPixelGat(width - col - 1, row, inputMatrix.getPixelGat(col, row));

                        outputMatrix.setPixelBat(col, row, inputMatrix.getPixelBat(width - col - 1, row));
                        outputMatrix.setPixelBat(width - col - 1, row, inputMatrix.getPixelBat(col, row));
                    }
                    break;
                    default:
                        throw new Exception("Symetria w poziomie obsługiwana tylko dla obrazów szarych i kolorowych!");
                }
            }
        }
        return new PCXImage(outputMatrix.getImageBytes(), plikWe1);
    }

    static PCXImage horizontalSymmetry(PCXImage plikWe1) throws Exception {
        PixelsMatrix inputMatrix = new PixelsMatrix(plikWe1);
        PixelsMatrix outputMatrix = new PixelsMatrix(plikWe1);
        outputMatrix.clear();
        int width = plikWe1.getWidth();
        int height = plikWe1.getHeight();
        Type type = plikWe1.getType();

        for (int row = 0; row < height / 2; row++) {
            for (int col = 0; col < width; col++) {
                switch (type) {
                    case GRAY8: {
                        outputMatrix.setPixelAt(col, row, inputMatrix.getPixelAt(col, height - row - 1));
                        outputMatrix.setPixelAt(col, height - row - 1, inputMatrix.getPixelAt(col, row));
                    }
                    break;
                    case RGB24: {
                        outputMatrix.setPixelRat(col, row, inputMatrix.getPixelRat(col, height - row - 1));
                        outputMatrix.setPixelRat(col, height - row - 1, inputMatrix.getPixelRat(col, row));

                        outputMatrix.setPixelGat(col, row, inputMatrix.getPixelGat(col, height - row - 1));
                        outputMatrix.setPixelGat(col, height - row - 1, inputMatrix.getPixelGat(col, row));

                        outputMatrix.setPixelBat(col, row, inputMatrix.getPixelBat(col, height - row - 1));
                        outputMatrix.setPixelBat(col, height - row - 1, inputMatrix.getPixelBat(col, row));
                    }
                    break;
                    default:
                        throw new Exception("Symetria w pionie obsługiwana tylko dla obrazów szarych i kolorowych!");
                }
            }
        }
        return new PCXImage(outputMatrix.getImageBytes(), plikWe1);
    }
}
