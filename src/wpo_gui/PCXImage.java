package wpo_gui;
//

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static wpo_gui.Tool.log;
import static wpo_gui.Tool.println;
import static wpo_gui.Type.GRAY8;
import static wpo_gui.Type.MONOB;
import static wpo_gui.Type.RGB24;

public class PCXImage {

    private String filename = "new_file";

    private int height;
    private int width;
    private int manufacturer;
    private int version;
    private int encoding;
    private int bitsPerPixel;
    private int xmin, ymin;
    private int xmax, ymax;
    private int hres;
    private int vres;
    private byte[] palette16 = new byte[48];
    private int reserved;
    private int planes;
    private int bytesPerLine;
    private int palleteType;
    private byte[] filler = new byte[58];

    private ArrayList<Integer> encodedImageBytes;
    private ArrayList<Integer> decodedImageBytes;

    private Type type;

    //Zapis zdjecia
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();

    private int resolution;

    private FileInputStream fileInputStream;
    private File file;

    public static int instances = 0;

    public PCXImage() {  //dla plikow we i wy
        instances++;
    }

    public PCXImage(String filename) throws Exception { //konstruktor otwierajacy plik o podanej nazwie i wczytujacy go do obiektu
        instances++;
        this.openFile(filename);
    }

    public PCXImage(PCXImage source) { //konstruktor kopiujacy
        instances++;
        copyPCX(source, this);
    }

    public PCXImage(ArrayList<Integer> decodedImageBytes, PCXImage source) {
        instances++;
        copyPCX(source, this);
        setDecodedImageBytes(decodedImageBytes);
    }

    public PCXImage(PCXImage source, ArrayList<Integer> decodedImageBytes, int newWidth, int newHeight) {   //generuje nowy plik po przeskalowaniu (zmiana rozmiarów)
        instances++;
        copyPCX(source, this);
        //nadpisz częśc ustawień!
        this.setResolution(newWidth, newHeight);

        if (source.type != MONOB) {
            this.bytesPerLine = newWidth * planes;
        } else {
            bytesPerLine = newWidth;
        }
        setDecodedImageBytes(decodedImageBytes);

    }
//
//    PCXImage(PixelsMatrix outputMatrix) throws Exception {    //Nie działa
//        instances++;
////        width = outputMatrix.width;
////        height = outputMatrix.height;
////        resolution = width*height;
////        type = outputMatrix.type;
//        setNewHeader(outputMatrix.type, outputMatrix.width, outputMatrix.height);
//        decodedImageBytes=outputMatrix.getImageBytes();
//        
//        
//    }

    void openFile(String filename) throws FileNotFoundException, Exception {
        log("Opening file: " + filename);
        this.filename = filename;

        file = new File(filename);
        fileInputStream = new FileInputStream(file);

        loadImageHeader();

        try {
            loadImageBytes();            //wczytywanie pixeli z pliku
        } catch (IOException ex) {
            Logger.getLogger(PCXImage.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            setDecodedImageBytes(decodeRLE(encodedImageBytes));   //dekodowanie
        } catch (Exception ex) {
            Logger.getLogger(PCXImage.class.getName()).log(Level.SEVERE, null, ex);
        }

        println(this.toString());    //Wypisz info o wczytanym obrazku

    }

    void saveFile(String filename) throws FileNotFoundException, Exception {
        log("Saving file: " + filename);
        this.filename = filename;

        Tool.saveRawDecodedImageBytes(this, "output.raw");
        file = new File(filename);
        file.delete();  //usun poprzedni output
        FileOutputStream fos = new FileOutputStream(file);

        fos.write(imageHeaderTobaos());    //zapis naglowka do pliku

        if ((type != type.MONOB) && (decodedImageBytes.size() != resolution * (bitsPerPixel / 8) * planes)) {
            throw new Exception("decodedImageBytes nie zawiera tyle bajtow ile powinno!\nma:" + decodedImageBytes.size() + ", a powinno mieć: " + resolution * (bitsPerPixel / 8) * planes);
        } else if ((type == type.MONOB) && (decodedImageBytes.size() != resolution * planes)) {
            throw new Exception("decodedImageBytes nie zawiera tyle bajtow ile powinno!\nma:" + decodedImageBytes.size() + ", a powinno mieć: " + resolution * planes);
        }

        ArrayList<Integer> encodedByteArray = encodeRLE(decodedImageBytes);  //kodowanie obrazu
        fos.write(Tool.arrayListToByteArray(baos, encodedByteArray));

        fos.close();

        log("Wygenerowano plik wyjściowy: ");
        println(toString());

        runFile();
    }

    private void loadImageBytes() throws IOException {
        log("loading image bytes: " + filename);
        encodedImageBytes = new ArrayList<Integer>();

        DataInputStream dis = new DataInputStream(fileInputStream);
        int read = 0;

        while ((read = dis.read()) != -1) { // returns numOfBytesRead or -1 at EOF
            encodedImageBytes.add(read);
        }

        //usuwanie palety z konca pliku 
        if (encodedImageBytes.size() > 768) {   //zabezepiczenie na wypadek obrazu o liczbie bajtow obrazu mniejszej niz 768
            if (encodedImageBytes.get(encodedImageBytes.size() - 768) == 0x0c) {    //jezeli jest paleta na koncu (768B)
                for (int i = encodedImageBytes.size() - 768; i < encodedImageBytes.size(); i++) {
                    encodedImageBytes.remove(i);
                }
            }
        }

    }

    public void loadImageHeader() {
        log("loading image header: " + filename);
        try {
            manufacturer = fileInputStream.read();
            version = fileInputStream.read();
            encoding = fileInputStream.read();
            bitsPerPixel = fileInputStream.read();

            xmin = fileInputStream.read() + fileInputStream.read() * 256;
            ymin = fileInputStream.read() + fileInputStream.read() * 256;
            xmax = fileInputStream.read() + fileInputStream.read() * 256;
            ymax = fileInputStream.read() + fileInputStream.read() * 256;
            hres = fileInputStream.read() + fileInputStream.read() * 256;
            vres = fileInputStream.read() + fileInputStream.read() * 256;

            fileInputStream.read(palette16);
            reserved = fileInputStream.read();
            planes = fileInputStream.read();
            bytesPerLine = fileInputStream.read() + fileInputStream.read() * 256;   //video memory bytes per image row
            palleteType = (short) (fileInputStream.read() + fileInputStream.read() * 256);
            fileInputStream.read(filler); //58 = 54(smieci)+2(hsr) + 2(vsr)

            //width, height <- dobrze
            width = 1 + xmax - xmin;
            height = 1 + ymax - ymin;
            resolution = width * height;

            if (width % 2 == 1) {    //width musi byc parzyste!!! Jezeli nie jest to +1
                width++;
            }

            //Ustal typ obrazka na podstawie naglowka
            if (bitsPerPixel == 8 && planes == 1) {
                type = Type.GRAY8;
            } else if (bitsPerPixel == 8 && planes == 3) {
                type = Type.RGB24;
            } else if (bitsPerPixel == 1 && planes == 1) {
                type = Type.MONOB;
            } else {
                type = Type.UNDEFINIED;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        String str = "--- [ filename: " + filename + " ] ---\n";
        String output = new String(str
                + "manufacturer:\t " + Integer.toHexString(manufacturer) + "\n"
                + "version:\t " + Integer.toHexString(version) + "\n"
                + "encoding:\t " + Integer.toHexString(encoding) + "\n"
                + "bitsPerPixel:\t " + bitsPerPixel + "\n"
                + "Xmin, Ymin:\t " + xmin + ", " + xmax + "\n"
                + "Xmax, Ymax:\t " + xmax + ", " + ymax + "\n"
                + "reserved:\t " + reserved + "\n"
                + "planes:\t " + planes + "\n"
                + "bytesPerLine:\t " + bytesPerLine + " (video memory bytes per image row)\n"
                + "resolution:\t " + width + "x" + height + "\n"
                + "pictureType:\t " + type + "\n"
        );
        if (encodedImageBytes != null) {
            output += new String("encodedImageBytes: " + encodedImageBytes.size() + "\n");
        }
        if (decodedImageBytes != null && (type == GRAY8 || type == RGB24)) {    //tylko dla szarych i rgb (bez monochrom.)
            output += new String("decodedImageBytes: " + decodedImageBytes.size() + "\n"
                    + " should be: " + resolution * (bitsPerPixel / 8) * planes + "\n"
            );
        } else if (decodedImageBytes != null && (type == MONOB)) {
            output += new String("decodedImageBytes: " + decodedImageBytes.size() * 8 + "\n"
                    + " should be: " + resolution * planes + "\n"
            );
        }

        // kreska na koncu
        for (int i = 0; i < str.length(); i++) {
            output += "-";
        }
        output += "\n";

        return output;
    }

    private ArrayList<Integer> decodeRLE(ArrayList<Integer> encodedImageBytes) throws Exception {   //dekompresja
        log("RLE decoding: " + filename);
        int i = 0;
        decodedImageBytes = new ArrayList<Integer>();

        for (i = 0; i < encodedImageBytes.size(); i++) {    //przejdz po src
            if (i + 1 < encodedImageBytes.size() && encodedImageBytes.get(i) > 192) {//jezeli kod znaku[i] > 192
                int counter = (encodedImageBytes.get(i) - 192);
                Integer nextValue = encodedImageBytes.get(i + 1);
                while (counter != 0) {
                    decodedImageBytes.add(nextValue);
                    counter--;
                }
                if (i + 1 < encodedImageBytes.size()) {
                    i++;
                }
            } else {
                decodedImageBytes.add(encodedImageBytes.get(i));
            }
        }

        cutExtraBytesAtEnd();
        return decodedImageBytes;
    }

    private void cutExtraBytesAtEnd() {
        if (type == GRAY8 || type == RGB24) {   //wytnij ekstra bajty tylko na koncu
            int p = resolution * (bitsPerPixel / 8) * planes;
            //int p = resolution;
            //print("Powinno byc bajtow: "+p+", A jest: "+encodedImageBytes.size());
            if (decodedImageBytes.size() > p) {
                decodedImageBytes.subList(p, decodedImageBytes.size()).clear();
            }
        } else if (type == MONOB) {
            int p = resolution * planes / 8;
            if (decodedImageBytes.size() > p) {
                decodedImageBytes.subList(p, decodedImageBytes.size()).clear();
            }
        }
    }

    private byte[] imageHeaderTobaos() {
        baos.reset();
        Tool.writeByte(baos, manufacturer);
        Tool.writeByte(baos, version);
        Tool.writeByte(baos, encoding);
        Tool.writeByte(baos, bitsPerPixel);
        Tool.writeInt(baos, xmin);
        Tool.writeInt(baos, ymin);
        Tool.writeInt(baos, xmax);
        Tool.writeInt(baos, ymax);
        Tool.writeInt(baos, hres);
        Tool.writeInt(baos, vres);
        Tool.writeZeroes(baos, 1);
        for (int i = 0; i < palette16.length; i++) {
            Tool.writeByte(baos, palette16[i]);
        }
        Tool.writeZeroes(baos, reserved);
        Tool.writeByte(baos, planes);
        Tool.writeInt(baos, bytesPerLine);
        Tool.writeInt(baos, palleteType);
        Tool.writeZeroes(baos, filler.length);
        return baos.toByteArray();
    }

    private ArrayList<Integer> encodeRLE(ArrayList<Integer> src) throws Exception {   //kodowanie
        log("RLE encoding: " + filename);
        ArrayList<Integer> dest = new ArrayList<Integer>();

        for (int i = 0; i < src.size(); i++) {
            int repeats = 0;  //liczba powtorzen znaku
            while (i + 1 < src.size() && src.get(i) == src.get(i + 1)) {   //dopoki nie dojdzie do konca src && src[i] == src[i-1](czyli znaki sie powtarzaja)
                repeats++;
                i++;
                if (repeats >= 255 - 192) {  //jezeli repeats osiagnelo max wartosc w 1-bajcie
                    dest.add(192 + repeats);// + src[i]);
                    dest.add(src.get(i));
                    repeats = 0;
                }
            }
            if (repeats >= 1 && i + 1 < src.size() && src.get(i) != src.get(i + 1)) {
                dest.add(193 + repeats);// + src[i]);
                dest.add(src.get(i));
            } else if (i < src.size()) {
                if (src.get(i) >= 192) {
                    dest.add(193);
                    dest.add(src.get(i));
                } else {
                    dest.add(src.get(i));
                }
            }
        }

        return dest;
    }

    public static ArrayList<Integer> normalize(ArrayList<Integer> imageBytes, int width, int height, Type type) throws Exception {    //normalizacja obrazu  NADPISUJE ORGINAL
        log("normalizowanie");
        //PixelsMatrix inputMatrix = new PixelsMatrix(this.getDecodedImageBytes(), this.width, this.height, type);
        //PixelsMatrix outputMatrix = new PixelsMatrix(this);
        //outputMatrix.clear();
        //PixelsMatrix matrix = new PixelsMatrix(this.decodedImageBytes, this.width, this.height, this.type);
        ArrayList<Integer> normalizedBytes = new ArrayList<>();

        int min = findMin(imageBytes);
        int max = findMax(imageBytes);
        if (max - min == 0) {
            throw new Exception("Normalizacja nie moze zostac przeprowadzona: f_max - f_min jest rowne 0, nie mozna dzielic przez 0!!!");
        }

        switch (type) {
            case MONOB:
            case GRAY8: {
                for (int row = 0; row < height; row++) {
                    for (int col = 0; col < width; col++) {
                        //log("nieznormalizowany piksel["+col+"]["+row+"]="+inputMatrix.getPixelAt(col, row));
                        //outputMatrix.setPixelAt(col, row, 255 * ((inputMatrix.getPixelAt(col, row)) - min) / (max - min));
//                        if (matrix.getPixelAt(col, row) > 255 || matrix.getPixelAt(col, row) < 0) {
//                            //pbr(wyn):= OBR{255[(f(ij)-f(min)]/{f(max)-f(min)}}; 
//                            int newValue = 255 *( (matrix.getPixelAt(col, row) - min) / (max - min));
//                            matrix.setPixelAt(col, row, newValue);
//                            
//                        }
                        int f = imageBytes.get(col + row * width);
                        double newValue = 255 * (double) ((f - min) / (double) (max - min));
                        normalizedBytes.add((int) newValue);
                        //log("znormalizowany piksel["+col+"]["+row+"]="+outputMatrix.getPixelAt(col, row));
                        //System.in.read();
                    }
                }
            }
            break;
            case RGB24: {
                PixelsMatrix inputMatrix = new PixelsMatrix(imageBytes, width, height, type);
                PixelsMatrix normalizedMatrix = new PixelsMatrix(imageBytes, width, height, type);
                normalizedMatrix.clear();
                PixelsMatrix matrix = new PixelsMatrix(imageBytes, width, height, type);
                
                for (int row = 0; row < height; row++) {
                    for (int col = 0; col < width; col++) {
                        normalizedMatrix.setPixelRat(col, row, 255 * ((inputMatrix.getPixelRat(col, row)) - min) / (max - min));
                        normalizedMatrix.setPixelGat(col, row, 255 * ((inputMatrix.getPixelGat(col, row)) - min) / (max - min));
                        normalizedMatrix.setPixelBat(col, row, 255 * ((inputMatrix.getPixelBat(col, row)) - min) / (max - min));
                    }
                }
                normalizedBytes = normalizedMatrix.getImageBytes();
                
            }
            break;
            default:
                throw new Exception("Normalizacja nie obsługuje tego typu obrazu!");
        }

        // this.decodedImageBytes = outputMatrix.getImageBytes();
        return normalizedBytes;
    }

    public static void copyPCX(PCXImage src, PCXImage dest) {
        log("copying pcx settings from: " + src.filename + "\tto: " + dest.filename);
        dest.encoding = src.encoding;
        dest.bitsPerPixel = src.bitsPerPixel;
        dest.bytesPerLine = src.bytesPerLine;
        dest.planes = src.planes;
        dest.filler = src.filler;
        dest.hres = src.hres;
        dest.manufacturer = src.manufacturer;
        dest.palette16 = src.palette16;
        dest.palleteType = src.palleteType;
        dest.type = src.type;
        dest.reserved = src.reserved;
        dest.width = src.width;
        dest.height = src.height;
        dest.resolution = src.resolution;
        dest.version = src.version;
        dest.vres = src.vres;
        dest.xmax = src.xmax;
        dest.xmin = src.xmin;
        dest.ymax = src.ymax;
        dest.ymin = src.ymin;

        //dest.encodedImageBytes = src.encodedImageBytes;
        dest.decodedImageBytes = src.decodedImageBytes;

    }

    public File getFile() {
        return file;
    }

    public static boolean compareImages(PCXImage img1, PCXImage img2) {
        boolean b = false;

        if (img1.resolution == img2.resolution
                && img1.type == img2.type) {
            b = true;
        } else {
            println("Obrazy " + img1.filename + " oraz " + img2.filename + " NIE SĄ TAKICH SAMYCH ROZMIARÓW |& KOLORÓW!!!");
        }

        return b;
    }

    void runFile() throws IOException {
        log("opening: " + this.filename + " in default image viewer...");
        Desktop.getDesktop().open(this.file);
    }

    void setFile(String filename) {
        this.filename = filename;
        this.file = new File(filename);
    }

    public ArrayList<Integer> getDecodedImageBytes() {
        return decodedImageBytes;
    }

    public void setDecodedImageBytes(ArrayList<Integer> decodedImageBytes) {
        this.decodedImageBytes = decodedImageBytes;
    }

    public Type getType() {
        return type;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public static int findMin(ArrayList<Integer> imageBytes) {
        int min = imageBytes.get(0);

        for (int i = 0; i < imageBytes.size(); i++) {
            if (imageBytes.get(i) < min) {
                min = imageBytes.get(i);
            }
        }

        return min;
    }

    public static int findMax(ArrayList<Integer> imageBytes) {
        int max = imageBytes.get(0);

        for (int i = 0; i < imageBytes.size(); i++) {
            if (imageBytes.get(i) > max) {
                max = imageBytes.get(i);
            }
        }

        return max;
    }

    public String getFilename() {
        return this.filename;
    }

    public ByteArrayOutputStream getBaos() {
        return this.baos;
    }

    int getResolution() {
        return this.resolution;
    }

    public void printEncodedImageBytes(int begin, int end) {

        for (int i = begin; i < end; i++) {
            System.out.print(Integer.toHexString(encodedImageBytes.get(i)) + " ");
        }
        println("");
    }

    public void printDecodedImageBytes(int begin, int end) {
        println("Decoded image bytes from range: [" + begin + "; " + end + "]: ");
        int counter = 0;
        for (int i = begin; i < end; i++, counter++) {
            System.out.print(Integer.toHexString(decodedImageBytes.get(i)) + " ");
            if (counter >= 15) {
                println("");
                counter = -1;
            }
        }
        println("");
    }

//    public void setNewHeader(Type type, int width, int height) {
//        hres = vres = 300; //300dpi
//
//        this.type = type;
//        this.width = width;
//        this.height = height;
//        this.resolution = width * height;
//
//        this.xmin = 0;
//        this.ymin = width - 1;
//        this.xmax = width - 1;
//        this.ymax = height - 1;
//
//        encoding = 1;
//        manufacturer = 0x0a;
//        version = 5;
//
//        switch (type) {
//            case MONOB: {
//                planes = 1;
//                bitsPerPixel = 1;
//            }
//            break;
//            case GRAY8: {
//                planes = 1;
//                bitsPerPixel = 8;
//
//            }
//            break;
//            case RGB24: {
//                planes = 3;
//                bitsPerPixel = 8;
//            }
//            break;
//            default:
//                Tool.error("setNewHeader: Niewłaściwy typ");
//                break;
//        }
//
//        bytesPerLine = width * planes * (bitsPerPixel / 8);
//
//        filler = new byte[58];
//
//        palette16 = new byte[48];
//
//        palleteType = 0;    //???
//        reserved = 0; //???
//
//        encodedImageBytes = null;
//        //this.setDecodedImageBytes(out);
//
//        //file = new File();
//        baos = new ByteArrayOutputStream();
//        //fileInputStream = new FileInputStream();
//
//        isNormalized = false;
//
//    }
    int getPlanes() {
        return planes;
    }

    int getBytesPerLine() {
        return bytesPerLine;
    }

    void setResolution(int width, int height) {
        this.width = width;
        this.height = height;
        resolution = width * height;

        this.xmin = 0;
        this.ymin = width - 1;
        this.xmax = ymin;
        this.ymax = height - 1;
        if (type != MONOB) {
            bytesPerLine = width * planes/* * (bitsPerPixel / 8)*/;
        } else {
            bytesPerLine = width;
        }
        decodedImageBytes = new ArrayList<Integer>(resolution * planes);
    }

    public static void writePixelsMatrixToTextFile(PixelsMatrix matrix, String filename) throws FileNotFoundException, Exception {
        if(matrix.type!=GRAY8) return;
        PrintWriter pw = new PrintWriter(filename);
        int width = matrix.width;
        int height = matrix.height;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int currentVal = matrix.getPixelAt(col, row);
                pw.print(currentVal);
                if (currentVal >= 100) {
                    pw.print(" ");
                } else if (currentVal < 100) {
                    pw.print("  ");
                }
            }
            pw.println();
        }

        pw.close();
    }

}
