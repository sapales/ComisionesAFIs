/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import utiles.Numeros;

/**
 *
 * @author m0072
 */
public class TestNumeros {
    
    public static void main(String args[]){
        
        System.out.println(Numeros.eliminaCerosPorIzquierda("00000576"));
        System.out.println(Numeros.eliminaCerosPorIzquierda("0001256"));
        System.out.println(Numeros.eliminaCerosPorIzquierda("576"));
        System.out.println(Numeros.eliminaCerosPorIzquierda("0000-576"));
        System.out.println(Numeros.eliminaCerosPorIzquierda("00000000"));
        
        System.out.println(Numeros.formateaDosDecimales(Double.parseDouble("123563.9")));
        System.out.println(Numeros.formateaDosDecimales(Double.parseDouble("12.56539")));
        System.out.println(Numeros.formateaDosDecimales(Double.parseDouble("0.5639")));
        System.out.println(Numeros.formateaDosDecimales(Double.parseDouble("13.5639")));
        System.out.println(Numeros.formateaDosDecimales(Double.parseDouble("123.563")));
        
    }
    
}
