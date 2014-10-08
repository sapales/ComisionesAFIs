/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.io.FileOutputStream;

/**
 *
 * @author Santiago
 */
public class TestiText {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        TestiText test = new TestiText();
        
        try{
            Document documento = new Document(PageSize.A4, 80, 80, 50, 50);
            FileOutputStream salida = new FileOutputStream("archivo.pdf");
            PdfWriter writer = PdfWriter.getInstance(documento, salida);
            writer.setInitialLeading(0);
            
            //  Obtenemos una instancia de nuestro manejador de eventos
            Cabecera1 header = new Cabecera1();
            //Asignamos el manejador de eventos al escritor.
            writer.setPageEvent(header);
            
            Paragraph paragraph = new Paragraph();
            paragraph.add("Primera linea del documento");
            paragraph.setAlignment(Paragraph.ALIGN_CENTER);
             
            Paragraph parrafo = new Paragraph("Ejemplo de iText - El lado oscuro de java");
            
            Image image = Image.getInstance("pelayocom.gif");
            image.scaleToFit(100, 100);
            image.setAlignment(Chunk.ALIGN_LEFT);
            
            Paragraph parrafos[] = new Paragraph[80];
            for(int i=0; i<80; i++){
                parrafos[i] = new Paragraph();
                parrafos[i].setFont(new Font(FontFactory.getFont("Courier", 12, Font.NORMAL, BaseColor.BLACK)));
                parrafos[i].add("Linea " + Integer.toString(i) + " del documento");
                parrafos[i].setAlignment(Paragraph.ALIGN_LEFT);
            }
            
            documento.open();
            
            // Este codigo genera una tabla de 3 columnas
            PdfPTable table = new PdfPTable(3);                
            
            // addCell() agrega una celda a la tabla, el cambio de fila
            // ocurre automaticamente al llenar la fila
            table.addCell("Celda 1");
            table.addCell("Celda 2");
            table.addCell("Celda 3");
            
            table.addCell("Celda 4");
            table.addCell("Celda 5");
            table.addCell("Celda 6");
            
            table.addCell("Celda 7");
            table.addCell("Celda 8");
            table.addCell("Celda 9");
            
            // Si desea crear una celda de mas de una columna
            // Cree un objecto Cell y cambie su propiedad span
            
            PdfPCell celdaFinal = new PdfPCell(new Paragraph("Final de la tabla"));
            
            // Indicamos cuantas columnas ocupa la celda
            celdaFinal.setColspan(3);
            table.addCell(celdaFinal);
            
            // Agregamos la tabla al documento            
            documento.add(table);
            
            PdfContentByte cb = writer.getDirectContent();
            ColumnText ct = new ColumnText(cb);
            ct.setAlignment(Element.ALIGN_JUSTIFIED);
            ct.setText(new Phrase("Hola"));
            float[] left = { 36, (PageSize.A4.getWidth() / 2) + 18 };
            float[] right = { (PageSize.A4.getWidth() / 2) - 18, PageSize.A4.getWidth() - 36 };
            int status = ColumnText.NO_MORE_COLUMN;
            int column = 0;
            while (ColumnText.hasMoreText(status)) {
                ct.setSimpleColumn(left[column], 36, right[column], PageSize.A4.getHeight() - 36);
                status = ct.go();
                column++;
                if (column > 1) {
                    column = 0;
                    documento.newPage();
                }
                documento.newPage();
            }
            
            documento.add(paragraph);
            documento.add(image);
            documento.newPage();
            for(int i=1; i<80; i++){
                documento.add(parrafos[i]);
            }
            
            
            documento.close();
            
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}