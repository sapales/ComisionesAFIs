/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comisionesafis.informes;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 *
 * @author Santiago
 */
public class LiquidacionComisionesCabecera extends PdfPageEventHelper {
    
    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        Font fontFecha = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase("11/04/2014",fontFecha), 500,830,0);
        Font fontPaginaNum = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase("Página " + document.getPageNumber(),fontPaginaNum), 500,823,0);
        Font fontLIQ03 = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase("LIQ03_0202", fontLIQ03), 500,816,0);
        Font fontTitulo = new Font(Font.FontFamily.COURIER, 12, Font.NORMAL);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("LIQUIDACIÓN DE COMISIONES: Marzo 2014", fontTitulo), 300,800,0);
        Font fontDireccion = new Font(Font.FontFamily.COURIER, 10, Font.NORMAL);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase("PELAYO MUTUA DE SEGUROS", fontDireccion), 400,750,0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase("CL SANTA ENGRACIA, 67", fontDireccion), 400,730,0);
    }
    
}
