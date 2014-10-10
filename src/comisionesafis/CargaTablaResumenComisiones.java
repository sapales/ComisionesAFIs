/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comisionesafis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import utiles.Numeros;
import utiles.Periodos;

/**
 *
 * @author Santiago
 */
public class CargaTablaResumenComisiones {
    
    Connection conexion;
    ParametrosBean pb;
    
    public boolean cargar(Connection conexion, ParametrosBean pb){

        this.conexion=conexion;
        this.pb=pb;

        if(!cargarFichero())
            return false;

        return true;
            
    }

    private boolean cargarFichero(){
        
        // Abrimos el fichero de comisiones
        Periodos periodos = new Periodos(pb.getFicheroComisiones());
        String sSQL="";
        Statement stmt;
        ResultSet rsRecibos;
        ResultSet rsAgentes;
        String periodo;
        String nombre;
        String direccion;
        double retencionPorcentaje;
        double retencion;
        double totalPagar;

        try {
            
            // Borramos el contenido de la tabla antes de cargarla
            stmt = conexion.createStatement();
            stmt.execute("DELETE FROM ResumenComisiones");
            
            // Calculamos la suma del Importe de Comisión
            sSQL =  "SELECT CodAgente, SUM(ImpComision) as SumaComision ";
            sSQL += "  FROM Recibos rec";
            sSQL += " GROUP BY CodAgente";
            stmt = conexion.createStatement();
            rsRecibos = stmt.executeQuery(sSQL);
                        
            while (rsRecibos.next()) {
                
                sSQL =  "SELECT * ";
                sSQL += "  FROM Agentes";
                sSQL += " WHERE CodAgente = '" + rsRecibos.getString("CodAgente") + "'";
                stmt = conexion.createStatement();
                rsAgentes = stmt.executeQuery(sSQL);
                if(!rsAgentes.next()){
                    System.out.println("No hay datos del agente" + rsRecibos.getString("CodAgente"));
                }else{
                    // Cálculos previos
                    periodo = periodos.extraePeriodoMY("MMYYYY");
                    retencionPorcentaje=rsAgentes.getDouble("RetencionPorcentaje");
                    retencion  = Double.parseDouble(rsRecibos.getString("SumaComision")) * (retencionPorcentaje/100);
                    totalPagar = Double.parseDouble(rsRecibos.getString("SumaComision")) - retencion;
                    nombre = rsAgentes.getString("Nombre").replace("'","''");
                    direccion =rsAgentes.getString("Direccion").replace("'","''");

                    sSQL =  "INSERT INTO ResumenComisiones VALUES(";
                    sSQL += "null,'";
                    sSQL += rsRecibos.getString("CodAgente") + "','";
                    sSQL += nombre + "','";
                    sSQL += direccion + "','";
                    sSQL += rsAgentes.getString("CodPostal") + "','";
                    sSQL += rsAgentes.getString("Poblacion") + "','";
                    sSQL += rsAgentes.getString("Provincia") + "','";
                    sSQL += "" + "','";   // Hueco para el NIF
                    sSQL += periodo + "',";
                    sSQL += retencionPorcentaje + ",";
                    sSQL += rsRecibos.getDouble("SumaComision") + ",";
                    sSQL += retencion + ",";
                    sSQL += totalPagar + ")";
                    System.out.println(sSQL);
                    stmt = conexion.createStatement();
                    stmt.executeUpdate(sSQL);
                    stmt.close();
                }
            }
            return true;
        }        
        catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
}
