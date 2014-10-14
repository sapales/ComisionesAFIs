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
import java.text.SimpleDateFormat;
import java.util.Date;
import utiles.Fechas;
import utiles.Numeros;
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
        String sSQL="";
        Statement stmt;
        ResultSet rsAgentes;
        ResultSet rsRecibos;
        PdfPCell celda;
        boolean cabeceraColumnas;
        int filasPorPagina=0;
        int fila=0;
        PdfPTable table;
        Double dblTotal=0.0;
        
        // Generamos la sentencia de SelecciÃ³n de Datos
        try {

            // Generamos el PDF
            Document documento = new Document(PageSize.A4, 80, 80, 50, 50);
            FileOutputStream salida = new FileOutputStream("LiquidacionComisiones.pdf");
            PdfWriter writer = PdfWriter.getInstance(documento, salida);
            writer.setInitialLeading(0);
            
            //  Obtenemos una instancia de nuestro manejador de eventos
            LiquidacionComisionesPie pie = new LiquidacionComisionesPie();
            //Asignamos el manejador de eventos al escritor.
            writer.setPageEvent(pie);

            // Abrimos el Documento
            documento.open();
            
            // SELECT para extraer todos los códigos de los agentes con Recibos
            sSQL =  "SELECT DISTINCT (CodAgente) AS Agente ";
            sSQL += "  FROM ResumenComisiones";
            sSQL += " ORDER BY CodAgente";
            stmt = conexion.createStatement();
            rsAgentes = stmt.executeQuery(sSQL);
                        
            while (rsAgentes.next()) {
                   
                printCabecera1(documento);
                printCabeceraPelayo(documento);
                printCabecera2(documento,rsAgentes);
            
                // SELECT para extraer todos los Recibos de un agente
                sSQL =  "SELECT * ";
                sSQL += "  FROM Recibos";
                sSQL += " WHERE CodAgente = '" + rsAgentes.getString("Agente") + "'"; 
                stmt = conexion.createStatement();
                rsRecibos = stmt.executeQuery(sSQL);
                
                // Creamos la tabla formateada
                table=creaTabla();
                
//                if(rsAgentes.getString("Agente").equals("12355")){
//                    System.out.println("Hola");
//                }
                
                cabeceraColumnas=true;
                filasPorPagina=42;
                paginaNum=0;
                dblTotal=0.0;
                while(rsRecibos.next()){
                    if(fila>=filasPorPagina){
                        // Salto de página
                        // Imprimimos el contenido de la tabla
                        documento.add(table);
                        documento.newPage();
                        table=creaTabla();
                        saltoDePagina(documento, table, rsAgentes);
                        fila=1;
                        filasPorPagina=47;
                    }else if(cabeceraColumnas){
                        // Primera página
                        printCabeceraColumnas(table);
                        cabeceraColumnas=false;
                        fila=1;
                    }
                    Font font = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
                    celda = new PdfPCell(new Phrase(rsRecibos.getString("NPoliza"),font));
                    celda.setBorder(Rectangle.NO_BORDER);
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(celda);
                    celda = new PdfPCell(new Phrase(Fechas.fechaVencimiento(rsRecibos.getString("Fecha")),font));
                    celda.setBorder(Rectangle.NO_BORDER);
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(celda);
                    celda = new PdfPCell(new Phrase(fondoRecibo(rsRecibos.getString("Descripcion")),font));
                    celda.setBorder(Rectangle.NO_BORDER);
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(celda);
                    celda = new PdfPCell(new Phrase(rsRecibos.getString("Importe"),font));
                    celda.setBorder(Rectangle.NO_BORDER);
                    celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(celda);
                    celda = new PdfPCell(new Phrase(rsRecibos.getString("ImpComision"),font));
                    celda.setBorder(Rectangle.NO_BORDER);
                    celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(celda);
                    dblTotal+=Double.parseDouble(rsRecibos.getString("ImpComision"));
                    fila++;
                }
                if(fila>=filasPorPagina-5){
                    documento.add(table);
                    documento.newPage();
                    table=creaTabla();
                }
                printResumenContable(table, dblTotal, rsAgentes.getString("Agente"));
                documento.add(table);
                documento.newPage();
            }
            documento.close();
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    private void printCabecera1(Document documento){
        
        Paragraph parrafo;
        
        String patron = "dd/MM/yyyy";
        SimpleDateFormat formato = new SimpleDateFormat(patron);
        String fecha= (formato.format(new Date()));
        
        
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
            parrafo=new Paragraph("LIQUIDACIÓN DE COMISIONES: Septiembre 2014" , fontTitulo);
            parrafo.setAlignment(Element.ALIGN_CENTER);
            documento.add(parrafo);

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    
    }
    
    private void printCabeceraPelayo(Document documento){
        
        Paragraph parrafo;
        
        try{            
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
    
    private void printCabecera2(Document documento, ResultSet rsAgentes){
        
        
        try{
            // Generamos la cabecera
            Font fuenteCabecera = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
            float[] anchuras = {3f,1f,5f};
            PdfPTable table1 = new PdfPTable(anchuras);

            // Tipo de letra para la tabla
            Font font = new Font(Font.FontFamily.COURIER, 9, Font.NORMAL);

            PdfPCell celda = new PdfPCell();

            // Sucursal/DelegaciÃ³n
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

            // InspecciÃ³n
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
    
    private void printCabeceraColumnas(PdfPTable table){
        
        PdfPCell celda;
        
        Font font = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
        celda = new PdfPCell(new Phrase("NUMERO DE PÓLIZA",font));
        celda.setBorder(Rectangle.TOP + Rectangle.BOTTOM + Rectangle.LEFT);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(celda);
        celda = new PdfPCell(new Phrase("FECHA DE VENCIMIENTO",font));
        celda.setBorder(Rectangle.TOP + Rectangle.BOTTOM);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(celda);
        celda = new PdfPCell(new Phrase("FORMA DE PAGO",font));
        celda.setBorder(Rectangle.TOP + Rectangle.BOTTOM);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(celda);
        celda = new PdfPCell(new Phrase("BASE COMISIÓN",font));
        celda.setBorder(Rectangle.TOP + Rectangle.BOTTOM);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(celda);
        celda = new PdfPCell(new Phrase("IMPORTE",font));
        celda.setBorder(Rectangle.TOP + Rectangle.BOTTOM + Rectangle.RIGHT);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(celda);
        
    }

    private PdfPTable creaTabla(){
        
        float[] anchuras = {2f,1f,1f,1f,1f};
        PdfPTable tabla = new PdfPTable(anchuras);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(4f);
        return tabla;
        
    }
    
    private void saltoDePagina(Document documento, PdfPTable tabla, ResultSet rsAgentes){
        
        Paragraph parrafo;
        
        try{
            // Creamos la tabla formateada
            //tabla=creaTabla();
            printCabecera1(documento);
            parrafo=new Paragraph(" ");
            documento.add(parrafo);
            printCabecera2(documento, rsAgentes);
            printCabeceraColumnas(tabla);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
    }
    
    private void printResumenContable(PdfPTable tabla, Double total, String codAgente){
        
        ResultSet rsAgente;
        Statement stmt;
        String sSQL ="";
        Double retencion;
        Double liquido;
        String patron = "dd/MM/yyyy";
        SimpleDateFormat formato = new SimpleDateFormat(patron);
        String fecha= (formato.format(new Date()));
        String cuenta;
        
        try{
            // SELECT para extraer todos los códigos de los agentes con Recibos
            sSQL =  "SELECT * ";
            sSQL += "  FROM Agentes";
            sSQL += " WHERE CodAgente ='" + codAgente + "'";
            stmt = conexion.createStatement();
            rsAgente = stmt.executeQuery(sSQL);

            PdfPCell celda;
            Font font;

            // Primera fila de datos
            font = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
            celda = new PdfPCell(new Phrase(" ",font));
            celda.setColspan(3);
            celda.setBorder(Rectangle.TOP);
            tabla.addCell(celda);

            font = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
            celda = new PdfPCell(new Phrase("TOTAL ",font));
            celda.setBorder(Rectangle.TOP);
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celda);
            
            font = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
            celda = new PdfPCell(new Phrase(Numeros.formateaDosDecimales(total),font));
            celda.setBorder(Rectangle.TOP);
            celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celda);
            
            // Segunda fila de datos
            font = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
            celda = new PdfPCell(new Phrase(" ",font));
            celda.setColspan(3);
            celda.setBorder(Rectangle.NO_BORDER);
            tabla.addCell(celda);

            font = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
            celda = new PdfPCell(new Phrase("I.R.P.F. " + rsAgente.getString("RetencionPorcentaje") + "%" ,font));
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celda);
            
            retencion=total * (Double.parseDouble(rsAgente.getString("RetencionPorcentaje"))/100);
            font = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
            celda = new PdfPCell(new Phrase(Numeros.formateaDosDecimales(retencion),font));
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celda);
            
            // Tercera fila de datos
            font = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
            celda = new PdfPCell(new Phrase(" ",font));
            celda.setColspan(3);
            celda.setBorder(Rectangle.BOTTOM);
            tabla.addCell(celda);

            font = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
            celda = new PdfPCell(new Phrase("LÍQUIDO ",font));
            celda.setBorder(Rectangle.BOTTOM);
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celda);
            
            liquido=total-retencion;
            font = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
            celda = new PdfPCell(new Phrase(Numeros.formateaDosDecimales(liquido),font));
            celda.setBorder(Rectangle.BOTTOM);
            celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celda);
            
            // Cuenta
            cuenta=rsAgente.getString("Banco") + " ";
            cuenta+=rsAgente.getString("Sucursal") + " ";
            cuenta+=rsAgente.getString("DC") + " ";
            cuenta+=rsAgente.getString("Cuenta") + " ";
            
            font = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
            celda = new PdfPCell(new Phrase("CTA nº " + cuenta,font));
            celda.setColspan(5);
            celda.setBorder(Rectangle.NO_BORDER);
            tabla.addCell(celda);

            // Firma
            font = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
            celda = new PdfPCell(new Phrase("En Madrid a " + fecha,font));
            celda.setColspan(5);
            celda.setBorder(Rectangle.NO_BORDER );
            tabla.addCell(celda);
            
            font = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
            celda = new PdfPCell(new Phrase("V.Bo. ",font));
            celda.setColspan(5);
            celda.setBorder(Rectangle.NO_BORDER);
            tabla.addCell(celda);

            font = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
            celda = new PdfPCell(new Phrase(" ",font));
            celda.setColspan(5);
            celda.setBorder(Rectangle.NO_BORDER);
            tabla.addCell(celda);
            
            font = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
            celda = new PdfPCell(new Phrase(" ",font));
            celda.setColspan(5);
            celda.setBorder(Rectangle.NO_BORDER);
            tabla.addCell(celda);
            
            font = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
            celda = new PdfPCell(new Phrase("PELAYO VIDA",font));
            celda.setColspan(5);
            celda.setBorder(Rectangle.NO_BORDER);
            tabla.addCell(celda);

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
    }
    
    private String fondoRecibo(String garantia){
        
        if(garantia.startsWith("F"))
            return "FONDO";
        if(garantia.startsWith("R"))
            return "RECIBO";
        else
            return "";
        
    }

}
