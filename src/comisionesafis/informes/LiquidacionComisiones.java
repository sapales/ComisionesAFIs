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
        
        // Generamos la sentencia de Selección de Datos
        try {

            // Generamos el PDF
            Document documento = new Document(PageSize.A4, 80, 80, 50, 50);
            FileOutputStream salida = new FileOutputStream("LiquidacionComisiones.pdf");
            PdfWriter writer = PdfWriter.getInstance(documento, salida);
            writer.setInitialLeading(0);
            
            //  Obtenemos una instancia de nuestro manejador de eventos
            LiquidacionComisionesCabecera cabecera = new LiquidacionComisionesCabecera();
            //Asignamos el manejador de eventos al escritor.
            writer.setPageEvent(cabecera);
            
            //  Obtenemos una instancia de nuestro manejador de eventos
            LiquidacionComisionesPie pie = new LiquidacionComisionesPie();
            //Asignamos el manejador de eventos al escritor.
            writer.setPageEvent(pie);

            // Abrimos el Documento
            documento.open();
            
            sSQL =  "SELECT Distinct(CodAgente) as Agente ";
            sSQL += "  FROM Recibos";
            sSQL += " GROUP BY CodAgente";
            stmt = conexion.createStatement();
            rsAgentes = stmt.executeQuery(sSQL);
                        
            while (rsAgentes.next()) {
                   
                // Generamos la cabecera
                Font fuenteCabecera = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
                Paragraph pLineas[] = new Paragraph[3];

                linea="SUCURSAL/DELEGACION";
                linea+= "  ";
                linea+= "02";
                linea+= "  ";
                linea+= "PELAYO MONDIALE VIDA";

                pLineas[0] = new Paragraph();
                pLineas[0].setFont(fuenteCabecera);
                pLineas[0].add(linea);
                pLineas[0].setAlignment(Paragraph.ALIGN_LEFT);
                
                linea="INSPECCIÓN";
                linea+= "  ";
                linea+= "000001";
                linea+= "  ";
                linea+= "ACUERDO DE DISTRIBUCIÓN";
                pLineas[1] = new Paragraph();
                pLineas[1].setFont(fuenteCabecera);
                pLineas[1].add(linea);
                pLineas[1].setAlignment(Paragraph.ALIGN_LEFT);

                linea="AGENTE";
                linea+= "  ";
                linea+= rsAgentes.getString("Agente");
                linea+= "  ";
                linea+= "MUTUA PELAYO";
                pLineas[2] = new Paragraph();
                pLineas[2].setFont(fuenteCabecera);
                pLineas[2].add(linea);
                pLineas[2].setAlignment(Paragraph.ALIGN_LEFT);
                
//                // creating separators
//                LineSeparator separador  = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, -2);
//
//                // Datos fijos
//                Paragraph pPelayo[] = new Paragraph[5];
//                pPelayo[0] = new Paragraph();
//                pPelayo[0].add("PELAYO VIDA");
//                pPelayo[0].setAlignment(Paragraph.ALIGN_RIGHT);
//                pPelayo[1] = new Paragraph();
//                pPelayo[1].add("CL SANTA ENGRACIA 69");
//                pPelayo[1].setAlignment(Paragraph.ALIGN_RIGHT);
//                pPelayo[2] = new Paragraph();
//                pPelayo[2].add("28010  MADRID");
//                pPelayo[2].setAlignment(Paragraph.ALIGN_RIGHT);
//                pPelayo[3] = new Paragraph();
//                pPelayo[3].add("MADRID");
//                pPelayo[3].setAlignment(Paragraph.ALIGN_RIGHT);
//                pPelayo[4] = new Paragraph();
//                pPelayo[4].add("06422");
//                pPelayo[4].setAlignment(Paragraph.ALIGN_RIGHT);
//
//                // Fecha
//                Paragraph pFecha= new Paragraph();
//                pFecha = new Paragraph("Madrid, a " + periodos.extraeFechaLarga());
//                pFecha.setAlignment(Paragraph.ALIGN_RIGHT);
//
//                // Línea 1 de FACTURA
//                Paragraph pFactura1= new Paragraph();
//                sFactura1 = "FACTURA: nº factura ";
//                sFactura1 += rsComisiones.getString("CodAgente") +  " - ";
//                sFactura1 += periodos.extraePeriodoMY("MM-YYYY");
//                pFactura1= new Paragraph(sFactura1);
//
//                // Línea 2 de FACTURA
//                Paragraph pFactura2= new Paragraph();
//                sFactura2 = "Factura por la prestación de servicios relativos a las operaciones ";
//                sFactura2 += "de intermediación de seguros realizadas para su entidad del mes de ";
//                sFactura2 += periodos.extraePeriodoMY("MM YYYY");;
//                pFactura2= new Paragraph(sFactura2);
//
//                // Datos Económicos
//                PdfPTable table = new PdfPTable(2);                
//
//                table.getDefaultCell().setBorder(0);
//
//                PdfPCell celda1 = new PdfPCell();
//                celda1.addElement(new Chunk(Double.toString(rsComisiones.getDouble("TotalComisiones"))));
//                //celda1.getPhrase().add(Double.toString(rsRecibos.getDouble("SumaComision")));
//                celda1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                celda1.setBorder(0);
//
//                table.addCell("Comisiones pagadas");
//                table.addCell(celda1);
//
//                table.addCell("Otros conceptos");
//                table.setHorizontalAlignment(Element.ALIGN_LEFT);
//                table.addCell("");                    
//
//                table.addCell("Retención (" + Double.toString(rsComisiones.getDouble("RetencionPorcentaje")) + "%)");
//                Double dblRetencion = rsComisiones.getDouble("TotalComisiones") * (rsComisiones.getDouble("RetencionPorcentaje")/100); 
//                table.addCell(Double.toString(dblRetencion));
//
//                table.addCell("Conceptos no sujetos");
//                table.addCell("");
//
//                table.addCell("Total a pagar");
//                Double dblTotalPagar = rsComisiones.getDouble("TotalComisiones") - dblRetencion;
//                table.addCell(Double.toString(dblTotalPagar));
//
//                // Literal: Operación exenta de IVA
//                Paragraph pColetilla = new Paragraph();
//                pColetilla.add("Operación exenta de IVA");
//
                // Añadimos los párrafos al Documento
                for(int i=0; i<3; i++)
                    documento.add(pLineas[i]);
//
//                documento.add(separador);
//
//                for(int i=0; i<5; i++)
//                    documento.add(pPelayo[i]);
//
//                documento.add(pFecha);
//                documento.add(pFactura1);
//                documento.add(separador);
//                documento.add(pFactura2);
//
//                // Agregamos la tabla al documento            
//                documento.add(table);
//
//                documento.add(pColetilla);
                documento.newPage();
            }
            documento.close();
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
}
