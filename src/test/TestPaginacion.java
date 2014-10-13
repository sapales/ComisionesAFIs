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
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import comisionesafis.informes.FacturasComisionesAgentesPie;
import java.io.FileOutputStream;

/**
 *
 * @author Santiago
 */
public class TestPaginacion {
    
    public static void main(String args[]){
    
    try{
        
            // Generamos el PDF
            Document documento = new Document(PageSize.A4, 80, 80, 50, 50);
            FileOutputStream salida = new FileOutputStream("Test.pdf");
            PdfWriter writer = PdfWriter.getInstance(documento, salida);
            writer.setInitialLeading(80);

//            //  Obtenemos una instancia de nuestro manejador de eventos
//            FacturasComisionesAgentesPie pie = new FacturasComisionesAgentesPie();
//            //Asignamos el manejador de eventos al escritor.
//            writer.setPageEvent(pie);

            // Abrimos el Documento
            documento.open();
            
            Paragraph Titulo = new Paragraph();
            Titulo.setAlignment(Element.ALIGN_CENTER);
            Titulo.add("CINTA COMISIONES");
            documento.add(Titulo);
            
            Titulo = new Paragraph();
            Titulo.setAlignment(Element.ALIGN_CENTER);
            Titulo.add("LINEA 2");
            documento.add(Titulo);
            
            Titulo = new Paragraph();
            Titulo.setAlignment(Element.ALIGN_CENTER);
            Titulo.add("LINEA 3");
            documento.add(Titulo);

            Titulo = new Paragraph();
            Titulo.setAlignment(Element.ALIGN_CENTER);
            Titulo.add("LINEA 4");
            documento.add(Titulo);

            int pagina =0;
            for(int i=1; i<150; i++)
            {
                if(i%40==0){
                    pagina++;
                    Titulo = new Paragraph();
                    Titulo.setAlignment(Element.ALIGN_LEFT);
                    Titulo.add("Página " + Integer.toString(pagina));
                    documento.add(Titulo);
                    documento.newPage();
                }
                Titulo = new Paragraph();
                Titulo.setAlignment(Element.ALIGN_LEFT);
                Titulo.add("LINEA DE DATOS NUMERO " + Integer.toString(i));
                documento.add(Titulo);
            }
                
            documento.close();
            
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }
}
    

