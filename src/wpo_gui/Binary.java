/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpo_gui;

import java.util.ArrayList;

/**
 *
 * @author Ja
 */
public class Binary {

    public static ArrayList<Integer> bytesToBits(ArrayList<Integer> imageBytes) {
        ArrayList<Integer> bits = new ArrayList<>(imageBytes.size() * 8);
//        for (int i = 0; i < imageBytes.size()*8; i++) {//zerowanie
//            bits.add(0);
//        }

        for (int i = 0; i < imageBytes.size(); i++) {
            int b = imageBytes.get(i);
            bits.add(getBit(b, 7));
            bits.add(getBit(b, 6));
            bits.add(getBit(b, 5));
            bits.add(getBit(b, 4));
            bits.add(getBit(b, 3));
            bits.add(getBit(b, 2));
            bits.add(getBit(b, 1));
            bits.add(getBit(b, 0));
        }

        return bits;
    }

    public static ArrayList<Integer> bitsToBytes(ArrayList<Integer> imageBits) {
        ArrayList<Integer> bytes = new ArrayList<>(imageBits.size() * 8);
        for (int i = 0; i < bytes.size(); i++) {  //zerowanie
            bytes.add(0);
        }

        for (int i = 0; i < bytes.size(); i++) {//przejdz po wszystkich bajtach
            ArrayList<Integer> tmpList = new ArrayList<>(8);
            for (int l = 0; l < 8; l++) {      //przejdz po wszystkich bitach aktualnego bajtu
                tmpList.add(imageBits.get(i + l));//wstaw do tmp listy
            }

            //wygeneruj bajt z tmpList
            int bajt = 0;
            for (int j = 7; j >=0; j--) {
                bajt += (int) (Math.pow(2, j-1) * tmpList.get(j));
            }
            bytes.set(i, bajt);
        }
        return bytes;
    }

    private static int getBit(int B, int pos) {
        return (B >> pos) & 1;
    }
}
