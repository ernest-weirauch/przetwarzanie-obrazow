/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpo_gui;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import static wpo_gui.Tool.print;
import static wpo_gui.Tool.println;

/**
 *
 * @author Ja
 */
public class Test {

    public static void main(String args[]) throws Exception {
        //wygen tab bajtow N
        final int N = 10;
        ArrayList<Integer> listaBajtow = new ArrayList<>();

        println("lista bajtow we: ");
        for (int i = 0; i < N; i++) {
            listaBajtow.add(i);
            print(listaBajtow.get(i) + ", ");
        }
        println("");

        ArrayList<Integer> listaBitow = Binary.bytesToBits(listaBajtow);
        println("lista bitow wy: ");
        for (int i = 0; i < listaBitow.size(); i++) { //wypisz liste bitow
            print(listaBitow.get(i) + " ");
            if (i % 8 == 0 && i != 0) {
                print(", ");
            }
        }

        println("");
        println("");
        //zamien liste bitow spowrotem na liste bajtow
        listaBajtow = Binary.bitsToBytes(listaBitow);

        //wypisz zmieniona liste
        println("lista bajtow z listy bitów: ");
        for (int i = 0; i < listaBajtow.size(); i++) {
            print(listaBajtow.get(i) + ", ");
        }

    }

}
