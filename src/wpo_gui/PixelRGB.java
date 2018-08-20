/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wpo_gui;

/**
 *
 * @author Ja
 */
public class PixelRGB {

    private int Rvalue;
    private int Gvalue;
    private int Bvalue;
    private boolean initializedR = false;
    private boolean initializedG = false;
    private boolean initializedB = false;

    public PixelRGB(int Rvalue, int Gvalue, int Bvalue) {
        setRGB(Rvalue, Gvalue, Bvalue);
    }

    public int getR() throws Exception {
        if(!initializedR) throw new Exception("PixelRGB: Niezainicjalizowane R");
        return Rvalue;
    }

    public void setR(int Rvalue) {
        this.Rvalue = Rvalue;
        initializedR = true;
    }

    public int getG() throws Exception {
        if(!initializedG) throw new Exception("PixelRGB: Niezainicjalizowane G");
        return Gvalue;
    }

    public void setG(int Gvalue) {
        this.Gvalue = Gvalue;
        initializedG = true;
    }

    public int getB() throws Exception {
        if(!initializedB) throw new Exception("PixelRGB: Niezainicjalizowane B");
        return Bvalue;
    }

    public void setB(int Bvalue) {
        this.Bvalue = Bvalue;
        initializedB = true;
    }

    void setRGB(int rValue, int gValue, int bValue) {
        this.Rvalue = rValue;
        this.Gvalue = gValue;
        this.Bvalue = bValue;
        initializedR = initializedG = initializedB = true;
    }

}
