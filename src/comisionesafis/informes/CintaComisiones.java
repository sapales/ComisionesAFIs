/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comisionesafis.informes;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import comisionesafis.ParametrosBean;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import utiles.Periodos;

/**
 *
 * @author Santiago
 */
public class CintaComisiones {
    
    private static String FICHERO_PDF ="CintaComisiones.pdf"; 
    Connection conexion;
    ParametrosBean pb;
    
    public CintaComisiones(Connection conexion, ParametrosBean pb){
        
        this.conexion=conexion;
        this.pb=pb;
        
    }
    
    public boolean generar(){

        // Abrimos el fichero de comisiones
        String sSQL="";
        Statement stmt;
        ResultSet rsComisiones;
        ResultSet rsBanco;
        String cuenta;
        
        // Generamos la sentencia de Selección de Datos
        try {

            // Generamos el PDF
            Document documento = new Document(PageSize.A4, 80, 80, 50, 50);
            FileOutputStream salida = new FileOutputStream(FICHERO_PDF);
            PdfWriter writer = PdfWriter.getInstance(documento, salida);
            writer.setInitialLeading(0);
            
            //  Obtenemos una instancia de nuestro manejador de eventos
            CintaComisionesPie pie = new CintaComisionesPie();
            //Asignamos el manejador de eventos al escritor.
            writer.setPageEvent(pie);

            // Abrimos el Documento
            documento.open();
            
            sSQL =  "SELECT * ";
            sSQL += "  FROM ResumenComisiones";
            sSQL += " ORDER BY CodAgente";
            stmt = conexion.createStatement();
            rsComisiones = stmt.executeQuery(sSQL);

            Paragraph Titulo = new Paragraph();
            Titulo.setAlignment(Element.ALIGN_CENTER);
            Titulo.add("CINTA COMISIONES");

            float[] anchuras = {1f,1.5f,3f,4f,1f,1.5f};
            PdfPTable table = new PdfPTable(anchuras);
            table.setWidthPercentage(100);
            table.setSpacingBefore(15f);
            table.setSpacingAfter(10f);
            PdfPCell celda;

            // Tipo de letra para la tabla
            Font font = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
            
            celda = new PdfPCell(new Phrase("Agente",font));
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(celda);
            celda = new PdfPCell(new Phrase("Importe",font));
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(celda);
            celda = new PdfPCell(new Phrase("Cuenta Bancaria",font));
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(celda);
            celda = new PdfPCell(new Phrase("Nombre",font));
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(celda);
            celda = new PdfPCell(new Phrase("Irpf",font));
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(celda);
            celda = new PdfPCell(new Phrase("Total",font));
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(celda);
            
            while (rsComisiones.next()) {
                   
                // Buscamos la Cuenta Bancaria
                sSQL =  "SELECT * ";
                sSQL += "  FROM Agentes";
                sSQL += " WHERE CodAgente='" +  rsComisiones.getString("CodAgente") + "'";
                stmt = conexion.createStatement();
                rsBanco = stmt.executeQuery(sSQL); 
                if(!rsBanco.next()){
                    cuenta = "                    ";
                } else {
                    cuenta  =  rsBanco.getString("Banco");
                    cuenta +=  rsBanco.getString("Sucursal");
                    cuenta +=  rsBanco.getString("DC");
                    cuenta +=  rsBanco.getString("Cuenta");
                }
                
                // Datos del agente
                celda = new PdfPCell(new Phrase(rsComisiones.getString("CodAgente"),font));
                celda.setBorder(Rectangle.NO_BORDER);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(celda);

                celda = new PdfPCell(new Phrase(rsComisiones.getString("TotalComisiones"),font));
                celda.setBorder(Rectangle.NO_BORDER);
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(celda);

                celda = new PdfPCell(new Phrase(cuenta,font));
                celda.setBorder(Rectangle.NO_BORDER);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(celda);
                
                celda = new PdfPCell(new Phrase(rsComisiones.getString("Nombre"),font));
                celda.setBorder(Rectangle.NO_BORDER);
                table.addCell(celda);
                
                celda = new PdfPCell(new Phrase(rsComisiones.getString("TotalRetencion"),font));
                celda.setBorder(Rectangle.NO_BORDER);
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(celda);

                celda = new PdfPCell(new Phrase(rsComisiones.getString("TotalPagar"),font));
                celda.setBorder(Rectangle.NO_BORDER);
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(celda);

            }

            documento.add(Titulo);
            documento.add(new Paragraph(" "));

            // Agregamos la tabla al documento            
            documento.add(table);
            
            documento.close();
            
            return true;
            
        }catch(Exception e){
            return false;
        }
    }    
}
