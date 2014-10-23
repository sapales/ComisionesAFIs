/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utiles;

import java.util.Calendar;

/**
 *
 * @author Santiago
 */
public class Periodos {
    
    String fichero="";
    String mes;
    String anno;
            
    public Periodos(String fichero){
        
        this.fichero=fichero;
        mes  = fichero.substring(7,9);
        anno = fichero.substring(3,7);
        
    }
    
    public String extraePeriodoMY(String formato){
                
        if(formato=="MM-YYYY")
            return mes + "-" + anno;
        else if(formato=="MMYYYY")
            return mes + anno;
        else if(formato=="MESANNO")
            return mesEnTexto(mes) + "-" + anno;
        else if(formato=="MES ANNO")
            return mesEnTexto(mes).toUpperCase() + " " + anno;
        else
            return mes + anno;
        
    }
    
    public String extraeMesTxtAnno(){
        
        return mesEnTexto(mes) + " de " + anno;
        
    }
    
    public String extraeFechaLarga(){
        
        return "31 de " + mesEnTexto(mes) + " de " + anno;
        
    }
    
    private String mesEnTexto(String mes){
        
        int iMes = Integer.parseInt(mes);
        
        switch(iMes){
            case 1:
                return "enero";
            case 2:
                return "febrero";
            case 3:
                return "marzo";
            case 4:
                return "abril";
            case 5:
                return "mayo";
            case 6:
                return "junio";
            case 7:
                return "julio";
            case 8:
                return "agosto";
            case 9:
                return "septiembre";
            case 10:
                return "octubre";
            case 11:
                return "noviembre";
            case 12:
                return "diciembre";
            default:
                return "";
       }
    }
    
    public int getUltimoDiaMes(){
        
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(anno), Integer.parseInt(mes), 1);
        System.out.println(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        
    }

}
