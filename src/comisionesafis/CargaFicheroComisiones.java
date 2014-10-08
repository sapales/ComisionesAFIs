/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comisionesafis;

import comisionesafis.ComisionesAFIs;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;

/**
 *
 * @author m0072
 */
public class CargaFicheroComisiones {

    static final int LONG_REG_COMISIONES=2000;
    
    ParametrosBean pb;
    Connection conexion;
    
        /**
     * Borramos los datos existentes en la tabla y cargamos los nuevos datos del fichero
     * @return 
     */
    public boolean cargar(Connection conexion, ParametrosBean pb){

        this.pb=pb;
        this.conexion=conexion;

        if(!cargarFichero())
            return false;

        backupBBDD();

        return true;
            
    }

    private boolean cargarFichero(){
        
        // Abrimos el fichero de comisiones
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        String dato[] = new String[9];
        boolean cabecera=true;
        double impComision;
        double importe;
        String sSQL="";
        Statement stmt;

        try {
            
            // Borramos el contenido de la tabla antes de cargarla
            stmt = conexion.createStatement();
            stmt.execute("DELETE FROM Recibos");
            
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File (pb.getFicheroComisiones());
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            while((linea=br.readLine())!=null){
                if(linea.length()!=LONG_REG_COMISIONES){
                    //TODO. Log
                    return false;
                }
                if(cabecera)
                    // La cabecera no la tratamos
                    cabecera=false;
                else{
                    // Recuperamos los datos
                    dato[0]=linea.substring(0,20);      // Número de Póliza
                    dato[1]=linea.substring(20,40);     // Número de Recibo
                    dato[2]=linea.substring(40,48);     // Código del agente
                    dato[3]=linea.substring(48,56);     // Fecha
                    dato[4]=linea.substring(56,68);     // Importe Comisión
                    dato[5]=linea.substring(68,74);     // Cod.Garantía
                    dato[6]=linea.substring(74,94);     // Descripción
                    dato[7]=linea.substring(94,106);    // Importe
                    dato[8]=linea.substring(828,829);   // Operación de comisión
                    System.out.println(linea);
                    // Tratamos los datos antes de insertarlos en la BBDD
                    dato[0]=dato[0].trim();
                    dato[1]=dato[1].trim();
                    dato[2]=dato[2].substring(3,8);
                    impComision=Double.parseDouble(dato[4]);
                    //dato[5]=dato[5];
                    //dato[6]=dato[6];
                    importe=Double.parseDouble(dato[7])/100;
                    //dato[8]=dato[8];
                    sSQL =  "INSERT INTO Recibos VALUES('";
                    sSQL += dato[0] + "','";
                    sSQL += dato[1] + "','";
                    sSQL += dato[2] + "','";
                    sSQL += dato[3] + "',";
                    sSQL += impComision + ",'";
                    sSQL += dato[5] + "','";
                    sSQL += dato[6] + "',";
                    sSQL += importe + ",'";
                    sSQL += dato[8] + "')";
                    stmt = conexion.createStatement();
                    stmt.executeUpdate(sSQL);
                    stmt.close();
                }
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }finally{
           // En el finally cerramos el fichero, para asegurarnos
           // que se cierra tanto si todo va bien como si salta
           // una excepcion.
           try{                   
              if( null != fr ){  
                 fr.close();    
              }                 
           }catch (Exception e2){
              e2.printStackTrace();
           }
        }
        return true;
    }

    private boolean backupBBDD(){
        
        String nuevoFich;
        File origen = new File(pb.getFicheroAgentes());
        
//        try{
//            nuevoFich=pb.getDirBackup() + fichero + "-"  + Fechas.fechaFichero();
//            File destino = new File(nuevoFich);
//            origen.renameTo(destino);
//            return true;
//        }catch(Exception e){
//            return false;
//        }

        return true;
    }
    
}
