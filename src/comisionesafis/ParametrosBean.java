/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comisionesafis;

/**
 *
 * @author Santiago
 */
public class ParametrosBean {
    
    private String ficheroComisiones;
    private String ficheroAgentes;
    private String directorioSalida;
    private String dirBackup;
    private String dirDatos;

    public String getDirBackup() {
        return dirBackup;
    }

    public void setDirBackup(String dirBackup) {
        this.dirBackup = dirBackup;
    }

    public String getDirDatos() {
        return dirDatos;
    }

    public void setDirDatos(String datos) {
        this.dirDatos = datos;
    }

    public String getFicheroComisiones() {
        return ficheroComisiones;
    }

    public void setFicheroComisiones(String ficheroComisiones) {
        this.ficheroComisiones = ficheroComisiones;
    }

    public String getFicheroAgentes() {
        return ficheroAgentes;
    }

    public void setFicheroAgentes(String ficheroAgentes) {
        this.ficheroAgentes = ficheroAgentes;
    }

    public String getDirectorioSalida() {
        return directorioSalida;
    }

    public void setDirectorioSalida(String directorioSalida) {
        this.directorioSalida = directorioSalida;
    }
    
}
