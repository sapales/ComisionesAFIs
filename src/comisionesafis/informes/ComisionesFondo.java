/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comisionesafis.informes;

import comisionesafis.ParametrosBean;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import utiles.Periodos;

/**
 *
 * @author Santiago
 */
public class ComisionesFondo {
    
    static String PREFIJO_FICHERO = "ComisionesFondo_";
    Connection conexion;
    ParametrosBean pb;
    
    public ComisionesFondo(Connection conexion, ParametrosBean pb){
        
        this.conexion=conexion;
        this.pb=pb;
        
    }
    
    public boolean generar(){
        
        String nombreFichero="";
        String sSQL;
        String linea;
        Statement stmt;
        ResultSet rsFondos;
        Periodos periodo = new Periodos(pb.getFicheroComisiones());
        
        nombreFichero = PREFIJO_FICHERO + periodo.extraePeriodoMY("MMYYYY") + ".TXT";
        
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            // Seleccionamos datos de los Fondos
            sSQL =  "SELECT NPoliza, Fecha, ImpComision, rec.CodAgente as Agente, Nombre ";
            sSQL += "  FROM Recibos rec, Agentes age";
            sSQL += " WHERE rec.CodAgente = age.CodAgente";
            sSQL += "   AND rec.Descripcion LIKE 'F%'";
            stmt = conexion.createStatement();
            rsFondos = stmt.executeQuery(sSQL);
        
            fichero = new FileWriter(nombreFichero);
            pw = new PrintWriter(fichero);

            pw.println("modalidad;poliza;fecha_calculo;comision;agente;nombre_agente;");
            
            while(rsFondos.next()){
 
                linea = "PJUB;";
                linea += rsFondos.getString("NPoliza")+";";
                linea += rsFondos.getString("Fecha")+";";
                linea += rsFondos.getString("ImpComision")+";";
                linea += rsFondos.getString("Agente")+";";
                linea += rsFondos.getString("Nombre")+";";
                pw.println(linea);
                
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           // Nuevamente aprovechamos el finally para
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
        
        return true;
    }

}
