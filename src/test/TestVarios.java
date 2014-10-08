/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import utiles.Periodos;

/**
 *
 * @author Santiago
 */
public class TestVarios {
    
    public static void main(String args[]){
        
        Periodos per1 = new Periodos("AVI20140915.txt");
        Periodos per2 = new Periodos("AVI20140215.txt");
        Periodos per3 = new Periodos("AVI20140715.txt");
        
        System.out.println(per1.getUltimoDiaMes());
        System.out.println(per2.getUltimoDiaMes());
        System.out.println(per3.getUltimoDiaMes());
        
    }
    
}
