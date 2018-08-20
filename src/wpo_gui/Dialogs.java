/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpo_gui;

import java.awt.Frame;
import javax.swing.JOptionPane;
import static wpo_gui.Tool.log;

/**
 *
 * @author Ja
 */
public class Dialogs {

    public static Double askForDouble(Frame frame, String title, String question) {
        String s = (String) JOptionPane.showInputDialog(frame, question+" [liczba zmienoprzecinkowa]", title, JOptionPane.QUESTION_MESSAGE);
        double d = Double.parseDouble(s);
        log("Użytkownik podał liczbę: " + d);
        return d;
    }

    public static void Error(Frame frame, String desc) {
        JOptionPane.showMessageDialog(frame, desc, "BŁĄD", JOptionPane.ERROR);
    }

    public static int askForInt(Frame frame, String title, String question) {
        String s = (String) JOptionPane.showInputDialog(frame, question+" [liczba całkowita]", title, JOptionPane.QUESTION_MESSAGE);
        int i = Integer.parseInt(s);
        log("Użytkownik podał liczbę: " + i);
        return i;
    }

    public static String askForString(Frame frame, String title, String question){
        String s = (String) JOptionPane.showInputDialog(frame, question, title, JOptionPane.QUESTION_MESSAGE);
        log("Użytkownik podał tekst: "+s);
        return s;
    }

}
