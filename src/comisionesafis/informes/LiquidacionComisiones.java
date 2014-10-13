/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comisionesafis.informes;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
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
public class LiquidacionComisiones {
    
    
    Connection conexion;
    ParametrosBean pb;
    int paginaNum=0;
    
    public LiquidacionComisiones(Connection conexion, ParametrosBean pb){
        
        this.conexion=conexion;
        this.pb=pb;
        
    }
    
    public boolean generar(){

        // Abrimos el fichero de comisiones
        Periodos periodos = new Periodos(pb.getFicheroComisiones());
        String sSQL="";
        Statement stmt;
        ResultSet rsAgentes;
        ResultSet rsRecibos;
        String sFactura1;
        String sFactura2;
        String linea="";
        PdfPCell celda;
        boolean cabeceraColumnas;
        
        // Generamos la sentencia de Selección de Datos
        try {

            // Generamos el PDF
            Document documento = new Document(PageSize.A4, 80, 80, 50, 50);
            FileOutputStream salida = new FileOutputStream("LiquidacionComisiones.pdf");
            PdfWriter writer = PdfWriter.getInstance(documento, salida);
            writer.setInitialLeading(0);
            
//            //  Obtenemos una instancia de nuestro manejador de eventos
//            LiquidacionComisionesCabecera cabecera = new LiquidacionComisionesCabecera();
//            //Asignamos el manejador de eventos al escritor.
//            writer.setPageEvent(cabecera);
//            
//            //  Obtenemos una instancia de nuestro manejador de eventos
//            LiquidacionComisionesPie pie = new LiquidacionComisionesPie();
//            //Asignamos el manejador de eventos al escritor.
//            writer.setPageEvent(pie);

            // Abrimos el Documento
            documento.open();
            
            // SELECT para extraer todos los códigos de los agentes con Recibos
            sSQL =  "SELECT DISTINCT (CodAgente) AS Agente ";
            sSQL += "  FROM ResumenComisiones";
            sSQL += " ORDER BY CodAgente";
            stmt = conexion.createStatement();
            rsAgentes = stmt.executeQuery(sSQL);
                        
            while (rsAgentes.next()) {
                   
                cabecera1(documento);
                cabeceraPelayo(documento);
                cabecera2(documento,rsAgentes);
            
                // SELECT para extraer todos los Recibos de un agente
                sSQL =  "SELECT * ";
                sSQL += "  FROM Recibos";
                sSQL += " WHERE CodAgente = '" + rsAgentes.getString("Agente") + "'"; 
                stmt = conexion.createStatement();
                rsRecibos = stmt.executeQuery(sSQL);
                
                cabeceraColumnas=true;
                while(rsRecibos.next()){
                    
                    float[] anchuras = {1f,1f,3f,4f,1f};
                    PdfPTable table = new PdfPTable(anchuras);
                    table.setWidthPercentage(100);
                    //table.setSpacingBefore(15f);
                    //table.setSpacingAfter(10f);

                    // Tipo de letra para la tabla
                    if(cabeceraColumnas){
                        Font font = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);

                        celda = new PdfPCell(new Phrase("NUMERO DE PÓLIZA",font));
                        celda.setBorder(Rectangle.NO_BORDER);
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(celda);
                        celda = new PdfPCell(new Phrase("FECHA DE VENCIMIENTO",font));
                        celda.setBorder(Rectangle.NO_BORDER);
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(celda);
                        celda = new PdfPCell(new Phrase("FORMA DE PAGO",font));
                        celda.setBorder(Rectangle.NO_BORDER);
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(celda);
                        celda = new PdfPCell(new Phrase("BASE COMISIÓN",font));
                        celda.setBorder(Rectangle.NO_BORDER);
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(celda);
                        celda = new PdfPCell(new Phrase("IMPORTE",font));
                        celda.setBorder(Rectangle.NO_BORDER);
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(celda);
                        cabeceraColumnas=false;
                        documento.add(table);
                    }
                    Font font = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);

                    celda = new PdfPCell(new Phrase(rsRecibos.getString("NPoliza"),font));
                    celda.setBorder(Rectangle.NO_BORDER);
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(celda);
                    celda = new PdfPCell(new Phrase(rsRecibos.getString("Fecha"),font));
                    celda.setBorder(Rectangle.NO_BORDER);
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(celda);
                    celda = new PdfPCell(new Phrase("FONDO",font));
                    celda.setBorder(Rectangle.NO_BORDER);
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(celda);
                    celda = new PdfPCell(new Phrase(rsRecibos.getString("Importe"),font));
                    celda.setBorder(Rectangle.NO_BORDER);
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda = new PdfPCell(new Phrase(rsRecibos.getString("ImpComision"),font));
                    celda.setBorder(Rectangle.NO_BORDER);
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(celda);

                    documento.add(table);
                    
                }
                
                documento.newPage();
            }
            documento.close();
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    private void cabecera1(Document documento){
        
        String fecha = "11/04/2014";
        Paragraph parrafo;
        
        try{
            paginaNum++;
            
            Font fontFecha = new Font(Font.FontFamily.COURIER, 7, Font.NORMAL);
            parrafo=new Paragraph(fecha, fontFecha);
            parrafo.setAlignment(Element.ALIGN_RIGHT);
            documento.add(parrafo);
            
            Font fontPaginaNum = new Font(Font.FontFamily.COURIER, 7, Font.NORMAL);
            parrafo=new Paragraph("Página " + Integer.toString(paginaNum) , fontPaginaNum);
            parrafo.setAlignment(Element.ALIGN_RIGHT);
            documento.add(parrafo);
            
            Font fontLIQ03 = new Font(Font.FontFamily.COURIER, 7, Font.NORMAL);
            parrafo=new Paragraph("LIQ03_0202" , fontLIQ03);
            parrafo.setAlignment(Element.ALIGN_RIGHT);
            documento.add(parrafo);
            
            Font fontTitulo = new Font(Font.FontFamily.COURIER, 12, Font.NORMAL);
            parrafo=new Paragraph("LIQUIDACIÓN DE COMISIONES: Marzo 2014" , fontTitulo);
            parrafo.setAlignment(Element.ALIGN_CENTER);
            documento.add(parrafo);

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    
    }
    
    private void cabeceraPelayo(Document documento){
        
        Paragraph parrafo;
        
        try{
            paginaNum++;
            
            // Separadores
            parrafo=new Paragraph(" ");
            documento.add(parrafo);
            parrafo=new Paragraph(" ");
            documento.add(parrafo);
            
            Font fuente = new Font(Font.FontFamily.COURIER, 9, Font.NORMAL);
            parrafo=new Paragraph("PELAYO MUTUA DE SEGUROS", fuente);
            parrafo.setAlignment(Element.ALIGN_RIGHT);
            documento.add(parrafo);

            parrafo=new Paragraph("CL SANTA ENGRACIA, 67", fuente);
            parrafo.setAlignment(Element.ALIGN_RIGHT);
            documento.add(parrafo);
            
            // Separadores
            parrafo=new Paragraph(" ");
            documento.add(parrafo);
            parrafo=new Paragraph(" ");
            documento.add(parrafo);

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    
    }
    
    private void cabecera2(Document documento, ResultSet rsAgentes){
        
        
        try{
            // Generamos la cabecera
            Font fuenteCabecera = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
            float[] anchuras = {3f,1f,5f};
            PdfPTable table1 = new PdfPTable(anchuras);

            // Tipo de letra para la tabla
            Font font = new Font(Font.FontFamily.COURIER, 9, Font.NORMAL);

            PdfPCell celda = new PdfPCell();

            // Sucursal/Delegación
            celda = new PdfPCell(new Phrase("SUCURSAL/DELEGACION",font));
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(celda);

            celda = new PdfPCell(new Phrase("02",font));
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(celda);

            celda = new PdfPCell(new Phrase("PELAYO MONDIALE VIDA",font));
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(celda);

            // Inspección
            celda = new PdfPCell(new Phrase("INSPECCIÓN",font));
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(celda);

            celda = new PdfPCell(new Phrase("000001",font));
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(celda);

            celda = new PdfPCell(new Phrase("ACUERDO DE DISTRIBUCIÓN",font));
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(celda);

            // Agente
            celda = new PdfPCell(new Phrase("AGENTE",font));
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(celda);

            celda = new PdfPCell(new Phrase(rsAgentes.getString("Agente"),font));
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(celda);

            celda = new PdfPCell(new Phrase("MUTUA PELAYO",font));
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(celda);

            documento.add(table1);
            
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        
    }
    
}
