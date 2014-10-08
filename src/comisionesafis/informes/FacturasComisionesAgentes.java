/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comisionesafis.informes;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import test.Cabecera1;

/**
 *
 * @author m0072
 */
public class FacturasComisionesAgentes {

    Connection conexion;
    String dirDestino;
    
    public FacturasComisionesAgentes(Connection conexion, String dirDestino){
        
        this.conexion=conexion;
        this.dirDestino=dirDestino;
        
    }
    
    public boolean generar(){

        // Abrimos el fichero de comisiones
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        String dato[] = new String[9];
        boolean cabecera=true;
        double impComision;
        double importe;
        String sSQL="";
        Statement stmt;
        ResultSet rsRecibos;
        ResultSet rsAgentes;
        double sumaComision; 
        String sFactura1;
        String sFactura2;
        
        // Generamos la sentencia de Selección de Datos
        try {

            // Generamos el PDF
            Document documento = new Document(PageSize.A4, 80, 80, 50, 50);
            FileOutputStream salida = new FileOutputStream("FacturasComisionesAgentes.pdf");
            PdfWriter writer = PdfWriter.getInstance(documento, salida);
            writer.setInitialLeading(0);
            
            //  Obtenemos una instancia de nuestro manejador de eventos
            FacturasComisionesAgentesPie pie = new FacturasComisionesAgentesPie();
            //Asignamos el manejador de eventos al escritor.
            writer.setPageEvent(pie);

            // Abrimos el Documento
            documento.open();
            
            sSQL =  "SELECT CodAgente, SUM(ImpComision) as SumaComision ";
            sSQL += "  FROM Recibos rec";
            sSQL += " GROUP BY CodAgente";
            stmt = conexion.createStatement();
            rsRecibos = stmt.executeQuery(sSQL);
                        
            while (rsRecibos.next()) {
            
                sSQL =  "SELECT * ";
                sSQL += "  FROM Agentes";
                sSQL += " WHERE CodAgente = '" + rsRecibos.getString("CodAgente") + "'";
                stmt = conexion.createStatement();
                rsAgentes = stmt.executeQuery(sSQL);
                if(!rsAgentes.next()){
                    // Error. enviar error al log porque vienen recibos pero no tenemos datos del agente
                }else{
                    // Calculamos la comisión total
                    sumaComision=rsRecibos.getDouble("SumaComision");
                    sumaComision=sumaComision/100;
                    
                    // Generamos los párrafos
                    // Datos del agente
                    Paragraph pAgente[] = new Paragraph[5];
                    pAgente[0] = new Paragraph();
                    pAgente[0].add(rsAgentes.getString("DSNombre"));
                    pAgente[0].setAlignment(Paragraph.ALIGN_LEFT);
                    pAgente[1] = new Paragraph();
                    pAgente[1].add(rsAgentes.getString("Direccion"));
                    pAgente[1].setAlignment(Paragraph.ALIGN_LEFT);
                    pAgente[2] = new Paragraph();
                    pAgente[2].add(rsAgentes.getString("CodPostal") + "  " + rsAgentes.getString("Poblacion") );
                    pAgente[2].setAlignment(Paragraph.ALIGN_LEFT);
                    pAgente[3] = new Paragraph();
                    pAgente[3].add(rsAgentes.getString("Provincia"));
                    pAgente[3].setAlignment(Paragraph.ALIGN_LEFT);
                    pAgente[4] = new Paragraph();
                    pAgente[4].add("¿NIF?");
                    pAgente[4].setAlignment(Paragraph.ALIGN_LEFT);
                    
                    // creating separators
                    LineSeparator separador  = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, -2);
                    
                    // Datos fijos
                    Paragraph pPelayo[] = new Paragraph[5];
                    pPelayo[0] = new Paragraph();
                    pPelayo[0].add("PELAYO VIDA");
                    pPelayo[0].setAlignment(Paragraph.ALIGN_RIGHT);
                    pPelayo[1] = new Paragraph();
                    pPelayo[1].add("CL SANTA ENGRACIA 69");
                    pPelayo[1].setAlignment(Paragraph.ALIGN_RIGHT);
                    pPelayo[2] = new Paragraph();
                    pPelayo[2].add("28010  MADRID");
                    pPelayo[2].setAlignment(Paragraph.ALIGN_RIGHT);
                    pPelayo[3] = new Paragraph();
                    pPelayo[3].add("MADRID");
                    pPelayo[3].setAlignment(Paragraph.ALIGN_RIGHT);
                    pPelayo[4] = new Paragraph();
                    pPelayo[4].add("06422");
                    pPelayo[4].setAlignment(Paragraph.ALIGN_RIGHT);
                    
                    // Fecha
                    Paragraph pFecha= new Paragraph();
                    pFecha = new Paragraph("Madrid, a 31 de agosto de 2014");
                    pFecha.setAlignment(Paragraph.ALIGN_RIGHT);
            
                    // Línea 1 de FACTURA
                    Paragraph pFactura1= new Paragraph();
                    sFactura1 = "FACTURA: nº factura ";
                    sFactura1 += rsRecibos.getString("CodAgente") +  " - ";
                    sFactura1 += "08-2014";
                    pFactura1= new Paragraph(sFactura1);

                    // Línea 2 de FACTURA
                    Paragraph pFactura2= new Paragraph();
                    sFactura2 = "Factura por la prestación de servicios relativos a las operaciones ";
                    sFactura2 += "de intermediación de seguros realizadas para su entidad del mes de ";
                    sFactura2 += "AGOSTO 2014";
                    pFactura2= new Paragraph(sFactura2);
                    
                    // Datos Económicos
                    PdfPTable table = new PdfPTable(2);                
            
                    table.getDefaultCell().setBorder(0);
                    
                    PdfPCell celda1 = new PdfPCell();
                    celda1.addElement(new Chunk(Double.toString(rsRecibos.getDouble("SumaComision"))));
                    //celda1.getPhrase().add(Double.toString(rsRecibos.getDouble("SumaComision")));
                    celda1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    celda1.setBorder(0);
                    
                    table.addCell("Comisiones pagadas");
                    table.addCell(celda1);

                    table.addCell("Otros conceptos");
                    table.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell("");                    

                    table.addCell("Retención (" + Double.toString(rsAgentes.getDouble("Retencion")) + "%)");
                    Double dblRetencion = rsRecibos.getDouble("SumaComision") * (rsAgentes.getDouble("Retencion")/100); 
                    table.addCell(Double.toString(dblRetencion));

                    table.addCell("Conceptos no sujetos");
                    table.addCell("");
            
                    table.addCell("Total a pagar");
                    Double dblTotalPagar = rsRecibos.getDouble("SumaComision") - dblRetencion;
                    table.addCell(Double.toString(dblTotalPagar));

                    // Literal: Operación exenta de IVA
                    Paragraph pColetilla = new Paragraph();
                    pColetilla.add("Operación exenta de IVA");
                    
                    // Añadimos los párrafos al Documento
                    for(int i=0; i<5; i++)
                        documento.add(pAgente[i]);
                                
                    documento.add(separador);
                    
                    for(int i=0; i<5; i++)
                        documento.add(pPelayo[i]);
                                        
                    documento.add(pFecha);
                    documento.add(pFactura1);
                    documento.add(separador);
                    documento.add(pFactura2);
                    
                    // Agregamos la tabla al documento            
                    documento.add(table);
                    
                    documento.add(pColetilla);
                    
//                    System.out.println(rsAgentes.getString("CodAgente"));
//                    System.out.println(sumaComision);
//                    System.out.println(rs.getString("DSNombre"));
                }
                documento.newPage();
            }
            documento.close();
        }catch(Exception e){
            return false;
        }
       
        return true;
    }
    
}
