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
public enum Type {
    UNDEFINIED,
    MONOB,
    GRAY8,
    RGB24;
    @Override
    public String toString(){
        if(this==MONOB) return new String("MONOB");
        else if(this==GRAY8) return new String("GRAY8");
        else if(this==RGB24) return new String("RGB24");
        return new String("UNDEFINED");
    }
    
}
