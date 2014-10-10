/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utiles;

import java.text.DecimalFormat;

/**
 *
 * @author m0072
 */
public class Numeros {
    
    public static String eliminaCerosPorIzquierda(String valor){
        
        int posicion=-1;
        
        for(int i=0;i< valor.length(); i++)
        {
            if(valor.charAt(i)!='0'){
                posicion=i;
                break;
            }
        }
        if(posicion==-1)
            return "0";
                    
        return valor.substring(posicion);
    }
    
    public static String formateaDosDecimales(Double valor){
        
        //Creación de un formato con separadores de decimales y millares, con 2 decimales
        DecimalFormat formato = new DecimalFormat("#,###.00");
        
        if(valor==0)
            return "0,00";
        else
            return formato.format(valor);
        
    } 

}
