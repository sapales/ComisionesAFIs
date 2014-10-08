/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comisionesafis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;
import java.util.Set;

import comisionesafis.informes.FacturasComisionesAgentes;
import utiles.ConexionSQLite;
import utiles.Fechas;
import utiles.Ficheros;


/**
 *
 * @author Santiago
 */
public class ComisionesAFIs {
        
    static final String PROPERTIES_AFIS="comisionesafis.properties";
    static final String BBDDPrefijo="AFIs";
    static final String BBDDSufijo=".sqlite";
    static final String DIR_DATOS="Datos/";
    static final String DIR_BACKUP="Backup/";
    // Constantes con las longitudes de los campos de la BBDD
    static ParametrosBean pb;
    ConexionSQLite sqlite;
    Connection conexion;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ComisionesAFIs comisionesAFIs=new ComisionesAFIs();
        
        // Inicializamos las variables que contienen los ficheros y directorios de trabajo
        if(!comisionesAFIs.init(args))
          System.exit(1);    
        
        // Abrimos la base de datos (conexión)
        if(!comisionesAFIs.conectarBBDD())
            System.exit(1);            
        
        // Pendiente de rediseño
        // Cargamos el fichero de agentes
        if(!comisionesAFIs.cargaFicheroAgentes())
            System.exit(1);           
        
        // Cargamos el fichero de comisiones
        if(!comisionesAFIs.cargaFicheroComisiones())
            System.exit(1);            
            
        // Generamos el informe de Facturas Comisiones Agentes
        if(!comisionesAFIs.generaInformes())
            System.exit(1);            
        
        // Hacemos backup de la base de datos AFIs.sqlite a AFIsYYYYMMDD.sqlite
        if(!comisionesAFIs.backupBD())
            System.exit(1);

        System.exit(0);
    }
    
    /**
     * init(). Método principal de la clase
     * @param args 
     */
    private boolean init(String args[]){
        
        pb = new ParametrosBean();
        
        // Comprobamos si recibimos 0 ó 3 parámetros 
        if(args.length==0){
            if (!leemosProperties(pb)){
                return false;
            }
        }
        else if(args.length==3){
            pb.setFicheroComisiones(args[0]);
            pb.setFicheroAgentes(args[1]);
            pb.setDirectorioSalida(args[2]);
        }
        else{
            return false;
        }
        // Añadimos los directorios de Datos y Backup que cuelgan del raiz de la aplicación
        pb.setDirDatos(DIR_DATOS);
        if(!existeDirectorio(pb.getDirDatos()))
            return false;
        pb.setDirBackup(DIR_BACKUP);
        if(!existeDirectorio(pb.getDirBackup()))
            return false;
        return true;
    }
    
    /**
     * 
     * @param pb
     * @return 
     */
    // TODO:  terminarlo
    private boolean leemosProperties(ParametrosBean pb){
       
        Properties prop = new Properties();
       
        String ficheroProperties=new String(PROPERTIES_AFIS);
       
        try{
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(ficheroProperties);
            prop.load(inputStream);
            if (inputStream == null) {
                throw new FileNotFoundException("property file '" + ficheroProperties + "' not found in the classpath");
            }
        }catch (Exception e){
            System.out.println("Error en properties");
        }
       
       return true; 
    }

    private boolean existeDirectorio(String sDirectorio){

        File folder = new File(sDirectorio);
        if (folder.exists())
            return true;
        
        // No existe. Tratamos de crea el directorio
        folder.mkdirs();
        if (folder.exists())
            return true;
        else
            return false;
        
    }
    
    private boolean existenFicherosEntrada(){
        
        File ficheroComisiones;
        File ficheroAgentes;

        try{
        
            ficheroComisiones = new File(pb.getFicheroComisiones());
            // Comprobamos si existe
            if(!ficheroComisiones.exists()){
                return false;
            }
            ficheroAgentes = new File(pb.getFicheroAgentes());
            // Comprobamos si existe
            if(!ficheroAgentes.exists()){
                return false;
            }
        }
        catch (Exception e){
            
        }
        
        return true;
    }
    
    /**
     * 
     * @param 
     * @return 
     */
    // TODO:  terminarlo
    private boolean backupBD(){
       
        File bdOrigen;
        File bdDestino;
        Ficheros fich = new Ficheros();
        String bdDestinoTexto="";
        String temp = BBDDPrefijo + BBDDSufijo;

        try{
        
            // Creamos un objeto File con el fichero origen
            bdOrigen = new File(temp);
            // Creamos el nombre del fichero destino
            bdDestinoTexto = pb.getDirDatos() +  BBDDPrefijo + Fechas.fechaFichero() + BBDDSufijo;
            // Creamos un objeto File con el fichero destino
            bdDestino = new File(bdDestinoTexto);
            // Comprobamos si existe
            if(bdDestino.exists()){
                // Tratamos de borrarlo
                if (!bdDestino.delete()){
                    return false;
                }
            }

            if (!fich.copiarFichero(bdOrigen, bdDestino))
                return false;
            else
                return true;
        } catch(Exception e){
            return false;
        }
    }
    
    // Abre la conexión la BBDD de SQLite
    private boolean conectarBBDD(){
        
        try{
            sqlite = new ConexionSQLite(BBDDPrefijo+BBDDSufijo);
            conexion=sqlite.getConexion();
            return true;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
        
    }

    private boolean cargaFicheroAgentes(){
        
        CargaFicheroAgentes cfa = new CargaFicheroAgentes();
        if(!cfa.cargar(conexion, pb))
            return false;
        else
            return true;
        
    }
    
    private boolean cargaFicheroComisiones(){
        
        CargaFicheroComisiones cfc = new CargaFicheroComisiones();
        if(!cfc.cargar(conexion, pb))
            return false;
        else
            return true;

    }
        
    private boolean backup(String fichero){
        
        String nuevoFich;
        File origen = new File(fichero);
        
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
    
    private boolean generaInformes(){

        FacturasComisionesAgentes facturasComisionesAgentes;
        
        facturasComisionesAgentes = new FacturasComisionesAgentes(conexion, pb.getDirectorioSalida());
        if(!facturasComisionesAgentes.generar()){
            return false;
        }
        
        
        return true;
    }
    
}
