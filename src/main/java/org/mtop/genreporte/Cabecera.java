/*
 * Copyright 2014 yesica.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mtop.genreporte;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Phrase;
/**
 *
 * @author yesica
 */
public class Cabecera extends PdfPageEventHelper {
    private String encabezado;
    PdfTemplate total;
    
    /**
     * Crea el objecto PdfTemplate el cual contiene el numero total de
     * paginas en el documento
     */
    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(30, 16);
    }
    
    /**
     * Esta es el metodo a llamar cuando ocurra el evento <b>onEndPage</b>, es en este evento
     * donde crearemos el encabeazado de la pagina con los elementos indicados.
     */
    public void onEndPage(PdfWriter writer, Document document) {
        PdfPTable table = new PdfPTable(3);
        try {
            // Se determina el ancho y altura de la tabla 
            table.setWidths(new int[]{24, 24, 2});
            table.setTotalWidth(527);
            table.setLockedWidth(true);
            table.getDefaultCell().setFixedHeight(20);
            
            // Borde de la celda
            table.getDefaultCell().setBorder(Rectangle.BOTTOM);
            
            table.addCell(encabezado);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            System.out.println("entro a l conteo de paginas");
            table.addCell(String.format("Pagina %010d de", writer.getPageNumber()));
            
            PdfPCell cell = new PdfPCell(Image.getInstance(total));
            
            cell.setBorder(Rectangle.BOTTOM);
            
            table.addCell(cell);
            // Esta linea escribe la tabla como encabezado
            table.writeSelectedRows(0, -1, 34, 803, writer.getDirectContent());
        }
        catch(DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
    
    
    /**
     * Realiza el conteo de paginas al momento de cerrar el documento
     */
    public void onCloseDocument(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(total, Element.ALIGN_LEFT, new Phrase(String.valueOf(writer.getPageNumber()-1)), 2, 2, 0);
       
      }    
    
    // Getter and Setters
    
    public String getEncabezado() {
        return encabezado;
    }
    public void setEncabezado(String encabezado) {
        this.encabezado = encabezado;
    }
}
