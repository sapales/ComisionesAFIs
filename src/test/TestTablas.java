/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import comisionesafis.informes.FacturasComisionesAgentesPie;
import java.io.FileOutputStream;

/**
 *
 * @author Santiago
 */
public class TestTablas {
    
    public static void main(String args[]){
        
        try{
        
            // Generamos el PDF
            Document documento = new Document(PageSize.A4, 80, 80, 50, 50);
            FileOutputStream salida = new FileOutputStream("CintaComisiones.pdf");
            PdfWriter writer = PdfWriter.getInstance(documento, salida);
            writer.setInitialLeading(0);

            //  Obtenemos una instancia de nuestro manejador de eventos
            FacturasComisionesAgentesPie pie = new FacturasComisionesAgentesPie();
            //Asignamos el manejador de eventos al escritor.
            writer.setPageEvent(pie);

            // Abrimos el Documento
            documento.open();

            /* chapter06/MyFirstPdfPTable.java */
            PdfPTable table = new PdfPTable(3);
            PdfPCell cell =
            new PdfPCell(new Paragraph("header with colspan 3"));
            cell.setColspan(3);
            table.addCell(cell);
            table.addCell("1.1");
            table.addCell("2.1");
            table.addCell("3.1");
            table.addCell("1.2");
            table.addCell("2.2");
            table.addCell("3.2");
            documento.add(table);
            documento.close();
            
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
    }

}
