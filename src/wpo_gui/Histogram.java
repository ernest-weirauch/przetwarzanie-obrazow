/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpo_gui;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.JFrame;
import static wpo_gui.Tool.*;

/**
 *
 * @author Ja
 */
public class Histogram {

    private static int[] grayHistogram = new int[256];  //tab przechowujaca histogram szary
    private static int[][] rgbHistogram = new int[3][256];

    public static void initTabs() {
        for (int i = 0; i < grayHistogram.length; i++) {
            grayHistogram[i] = 0;
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < rgbHistogram[0].length; j++) {
                rgbHistogram[i][j] = 0;
            }
        }
    }

    //zad4.1
    public static void countHistogram(PCXImage inputFile1) throws Exception {
        if (inputFile1.getDecodedImageBytes() == null) {
            Tool.error("inputFile1.decodedImageBytes=null");
        }

        initTabs();
        Type type = inputFile1.getType();
        ArrayList<Integer> imageBytes = inputFile1.getDecodedImageBytes();
        int width = inputFile1.getWidth();
        int height = inputFile1.getHeight();

        log("liczenie histogramu dla obrazu " + type.toString());
        switch (inputFile1.getType()) {
            case GRAY8: {
                for (int i = 0; i < imageBytes.size(); i++) {
                    (grayHistogram[imageBytes.get(i)])++;
                }
            }
            break;
            case RGB24: {
                PixelsMatrix pixelsMatrix = new PixelsMatrix(inputFile1);

                for (int row = 0; row < height; row++) {
                    for (int col = 0; col < width; col++) {
                        rgbHistogram[0][pixelsMatrix.getPixelRat(col, row)]++;  //red
                        rgbHistogram[1][pixelsMatrix.getPixelGat(col, row)]++;  //green
                        rgbHistogram[2][pixelsMatrix.getPixelBat(col, row)]++;  //blue
                    }
                }
            }
            break;
            default:
                Tool.error("Histogram moze byc liczony tylko dla obrazow rgb24 i gray8!");
                break;
        }
        isHistogramValid(inputFile1);

        showHistogram(type);
    }

    public static void showHistogram(Type type) throws Exception {
        log("wypisywanie histogramu dla obrazu " + type.toString());
        switch (type) {
            case GRAY8: {
                double[] gray = new double[256];    //konwersja na double dla potrzeb biblioteki rysującej wykresy
                for (int i = 0; i < 256; i++) {
                    gray[i] = grayHistogram[i];

                }
                HistogramFrame histogramFrame = new HistogramFrame(gray);
                histogramFrame.setVisible(true);
            }
            break;
            case RGB24: {
                double[] r = new double[256];//konwersja na double dla potrzeb biblioteki rysującej wykresy
                double[] g = new double[256];
                double[] b = new double[256];

                for (int i = 0; i < 256; i++) {
                    r[i] = rgbHistogram[0][i];
                    g[i] = rgbHistogram[1][i];
                    b[i] = rgbHistogram[2][i];
                }

                HistogramFrame histogramFrame = new HistogramFrame(r, g, b);
                histogramFrame.setVisible(true);
            }
            break;
            default:
                error("printHistogram: nieprawidlowy typ obrazu");
                break;
        }

    }

    //zad4.2 - przemieszczanie histogramu (wyrównanie histogramu)
    public static PCXImage moveHistogram(PCXImage inputImage1) {
        return null;
    }

    //zad4.3 - rozciaganie histogramu
    public static PCXImage stretchHistogram(PCXImage inputImage1) throws Exception {
        PixelsMatrix inputMatrix = new PixelsMatrix(inputImage1);
        PixelsMatrix outputMatrix = new PixelsMatrix(inputImage1, false);
        
        int width = inputMatrix.width;
        int height = inputMatrix.height;
        Type type = inputMatrix.type;
        int min = PCXImage.findMin(inputMatrix.getImageBytes());
        int max = PCXImage.findMax(inputMatrix.getImageBytes());

        try {
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    switch (type) {
                        case GRAY8: {
                            int newValue = (inputMatrix.getPixelAt(col, row) - min) * 255 / (max - min);
                            outputMatrix.setPixelAt(col, row, newValue);
                        }
                        break;
                        case RGB24: {
                            int r = ((inputMatrix.getPixelRat(col, row) - min) * 255) / (max - min);
                            int g = ((inputMatrix.getPixelGat(col, row) - min) * 255) / (max - min);
                            int b = ((inputMatrix.getPixelBat(col, row) - min) * 255) / (max - min);
                            outputMatrix.setPixelAt(col, row, r, g, b);
                        }
                        break;
                        default:
                            throw new Exception("Rozciaganie histogramu obsługuje tylko gray i rgb");
                    }
                }
            }
        } catch (ArithmeticException e) {
            Tool.error("Próba dzielenia przez zero!");
        }
        return new PCXImage(outputMatrix.getImageBytes(), inputImage1);
    }

    public static PCXImage thresholding1threshold(PCXImage inputImage1) { //progowanie 1-progowe (tylko dla histogramu obrazu barwowego)
        return null;
    }

    public static void saveHistogramToTextFile(String outputTxtFilename, Type type) throws FileNotFoundException, Exception {
        log("saving histogram data for image: " + type.toString());
        
        switch (type) {
            case GRAY8: {
                PrintWriter pw = new PrintWriter(outputTxtFilename+"GRAY.txt");
                pw.println("#GRAYpixel\tcount");
                for (int i = 0; i < grayHistogram.length; i++) {
                    pw.println(i + "\t" + grayHistogram[i]);
                }
                
                pw.close();
            }
            break;
            case RGB24: {
                PrintWriter pwR = new PrintWriter(outputTxtFilename + "RED.txt");
                PrintWriter pwG = new PrintWriter(outputTxtFilename + "GREEN.txt");
                PrintWriter pwB = new PrintWriter(outputTxtFilename + "_BLUE.txt");

                pwR.println("#REDpixel\tcount");
                for (int i = 0; i < rgbHistogram[0].length; i++) {
                    pwR.println(i + "\t" + rgbHistogram[0][i]);
                }

                pwG.println("#GREENpixel\tcount");
                for (int i = 0; i < rgbHistogram[1].length; i++) {
                    pwG.println(i + "\t" + rgbHistogram[1][i]);
                }

                pwB.println("#BLUEpixel\tcount");
                for (int i = 0; i < rgbHistogram[2].length; i++) {
                    pwB.println(i + "\t" + rgbHistogram[2][i]);
                }

                pwR.close();
                pwG.close();
                pwB.close();
            }
            break;
            default:
                Tool.error("Obecna glebia kolorow nie jest obslugiwana!");
                break;
        }

    }

    private static void isHistogramValid(PCXImage pcxImage) {
        Type type = pcxImage.getType();
        int sum = 0;

        switch (type) {
            case GRAY8: {
                for (int i = 0; i < grayHistogram.length; i++) {
                    sum += grayHistogram[i];
                }
            }
            break;
            case RGB24: {
                for (int j = 0; j < 3; j++) {
                    for (int i = 0; i < rgbHistogram[j].length; i++) {
                        sum += rgbHistogram[j][i];
                    }
                }
            }
            break;
            default:
                Tool.error("Nieprawidlowy type");
                break;
        }
        if (sum != pcxImage.getResolution() * pcxImage.getPlanes()) {
            error("Histogram nie policzony prawidlowo: Pikseli naliczono: " + sum + ", a jest w rzeczywistości: " + pcxImage.getResolution());
        }
    }

}
