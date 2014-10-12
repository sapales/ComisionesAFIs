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
public class CintaComisionesPie extends PdfPageEventHelper{
        
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        //Rectangle rect = writer.getBoxSize("art");
        Font font = new Font(Font.FontFamily.COURIER, 10, Font.NORMAL);
        Phrase texto = new Phrase("Página " + Integer.toString(document.getPageNumber()),font);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, texto, 300 , 30,0);
    }
    
}
