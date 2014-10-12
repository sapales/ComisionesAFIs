/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
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
            writer.setInitialLeading(80);

            //  Obtenemos una instancia de nuestro manejador de eventos
            FacturasComisionesAgentesPie pie = new FacturasComisionesAgentesPie();
            //Asignamos el manejador de eventos al escritor.
            writer.setPageEvent(pie);

            // Abrimos el Documento
            documento.open();
            
            Paragraph Titulo = new Paragraph();
            Titulo.setAlignment(Element.ALIGN_CENTER);
            Titulo.add("CINTA COMISIONES");

            /* chapter06/MyFirstPdfPTable.java */
            PdfPTable table = new PdfPTable(6);
//            PdfPCell cell = new PdfPCell(new Paragraph("header with colspan 3"));
//            cell.setColspan(3);
//            table.addCell(cell);
            table.addCell("Agente");
            table.addCell("Importe");
            table.addCell("Cuenta Bancaria");
            table.addCell("Nombre");
            table.addCell("Irpf");
            table.addCell("Total");
            
            for(int i=0;i<10;i++){
                table.addCell("Agente" + Integer.toString(i));
                table.addCell("17338,00");
                table.addCell("12345678901234567890");
                table.addCell("Luis Javier Sánchez Bermejo");
                table.addCell("6,42");
                table.addCell("17331,58");
            }
            
            // Añadimos los elementos al documento
            documento.add(Titulo);
            documento.add(table);
            documento.close();
            
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
    }

}
