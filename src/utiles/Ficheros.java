/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utiles;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 *
 * @author Santiago
 */

// TODO. Hacer esta clase estática (static)
public class Ficheros {
    
    private String descError;
    
    public Ficheros(){     
        this.setDescError("");
    }
    
    public String getDescError() {
        return descError;
    }

    private void setDescError(String descError) {
        this.descError = descError;
    }
    
    public boolean renombrarFichero(String ficheroActual, String ficheroNuevo){
    
        File fichero = new File(ficheroActual);

        File fichero2 = new File(ficheroNuevo);

        boolean success = fichero.renameTo(fichero2);
        if (!success) {
            this.setDescError("Error intentando cambiar el nombre de fichero");   
        }
        return success;
    }
 
    public boolean copiarFichero(File sourceFile, File destFile) throws IOException {
        if(!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel origen = null;
        FileChannel destino = null;
        try {
            origen = new FileInputStream(sourceFile).getChannel();
            destino = new FileOutputStream(destFile).getChannel();

            long count = 0;
            long size = origen.size();             
            while((count += destino.transferFrom(origen, count, size-count))<size);
            return true;
        }
        catch(Exception e){
            this.setDescError(e.getMessage());
            return false;
        }
        finally {
            if(origen != null) {
                origen.close();
            }
            if(destino != null) {
                destino.close();
            }
        }
    }

}
