/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utiles;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Santiago
 */
public class Fechas {
    
    public static String fechaFichero(){
        
        String retorno;
        
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        
        int dia  = calendar.get(Calendar.DAY_OF_MONTH);
        int mes  = calendar.get(Calendar.MONTH)+1;
        int anno = calendar.get(Calendar.YEAR);
        DecimalFormat df1 = new DecimalFormat("0000");
        retorno = df1.format(anno);
        DecimalFormat df2 = new DecimalFormat("00");
        retorno += df2.format(mes);
        retorno += df2.format(dia);

        return retorno;
        
    }
    
}
