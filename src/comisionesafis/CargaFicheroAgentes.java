/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 */

/* TODO: 
 * - Implementar el log
 * - Implementar la actualizaciÃ³n de datos, no sÃ³lo carga
*/

package comisionesafis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;
import utiles.Fechas;

/**
 *
 * @author m0072
 */
public class CargaFicheroAgentes {
        
        static final int LONG_REG_AGENTES=550;
    
        ParametrosBean pb;
        Connection conexion;
    
        public boolean cargar(Connection conexion, ParametrosBean pb){
             
            this.pb=pb;
            this.conexion=conexion;
            
            // TODO. Implementar la actualizaciÃ³n de datos
            File fichero = new File(pb.getFicheroAgentes());
            if(!fichero.exists())
                return true;
            
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
            String dato[] = new String[11];
            int retencion;
            String sSQL="";
            Statement stmt;
            boolean retorno=false;

            try {
                // Apertura del fichero y creacion de BufferedReader para poder
                // hacer una lectura comoda (disponer del metodo readLine()).
                archivo = new File (pb.getFicheroAgentes());
                fr = new FileReader (archivo);
                br = new BufferedReader(fr);

                // Lectura del fichero
                String linea;
                while((linea=br.readLine())!=null){
                    if(linea.length()!=LONG_REG_AGENTES){
                        //TODO. Log
                        return false;
                    }
                    // Recuperamos los datos
                    dato=linea.split(";");
                    for(int i=0; i<12; i++){
                        dato[i]=dato[i].trim();
                        dato[i]=dato[i].replace("'", "''");
                    }
                    // Tratamos los datos antes de insertarlos en la BBDD
                    dato[0]=dato[0].substring(5,10);    // CÃ³digo de Agente
                    retencion=Integer.parseInt(dato[6]);

                    sSQL =  "INSERT INTO Agentes VALUES('";
                    sSQL += dato[0] + "','";
                    sSQL += dato[1] + "','";
                    sSQL += dato[2] + "','";
                    sSQL += dato[3] + "','";
                    sSQL += dato[4]  + "','";
                    sSQL += dato[5] + "',";
                    sSQL += retencion + ",'";
                    sSQL += dato[7]  + "','";
                    sSQL += dato[8]  + "','";
                    sSQL += dato[9]  + "','";
                    sSQL += dato[10] + "','";
                    sSQL += dato[11] + "')";
                    System.out.println(sSQL);
                    stmt = conexion.createStatement();
                    stmt.executeUpdate(sSQL);
                    stmt.close();
                }
                retorno=true;
            }
            catch(Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
                retorno=false;
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
            return retorno;
    }
    
    private boolean backupBBDD(){
        
        String nuevoFich;
        File origen = new File(pb.getFicheroAgentes());
        
        try{
            nuevoFich=pb.getDirBackup() + pb.getFicheroAgentes() + "-"  + Fechas.fechaFichero();
            File destino = new File(nuevoFich);
            origen.renameTo(destino);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
