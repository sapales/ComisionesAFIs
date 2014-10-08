/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utiles;

import java.sql.*;

/**
 *
 * @author Santiago
 */
public class ConexionSQLite {
    
    public String driver, url, ip, bd, usr, pass;
    public Connection conexion;

    public ConexionSQLite(String fichero) {
    
        driver = "org.sqlite.JDBC";
        try {
            Class.forName(driver).newInstance();
            conexion = DriverManager.getConnection("jdbc:sqlite:"+fichero);
            System.out.println("Conexion a Base de Datos " + bd + " Ok");
        } catch (Exception exc) {
            System.out.println("Error al tratar de abrir la base de Datos" + bd + " : " + exc);
        }
    }

    public Connection getConexion() {
        return conexion;
    }

    public Connection cerrarConexion() throws SQLException {
        conexion.close();
        conexion = null;
        return conexion;
    }
}