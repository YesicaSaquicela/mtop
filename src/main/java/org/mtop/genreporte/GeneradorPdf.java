package org.mtop.genreporte;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import static com.itextpdf.text.BaseColor.BLUE;
import static com.itextpdf.text.BaseColor.GREEN;
import static com.itextpdf.text.BaseColor.RED;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import static com.itextpdf.text.pdf.BidiOrder.PDF;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.mtop.controlador.ControladorVehiculo;
import org.mtop.modelo.EstadoVehiculo;
import org.mtop.modelo.Vehiculo;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextUserAgent;

/**
 * @author
 *
 */
public class GeneradorPdf {

    public static byte[] getValidHtmlDocument(InputStream is) {
        Tidy t = new Tidy();
        t.setXmlOut(true);
        ByteArrayOutputStream baus = new ByteArrayOutputStream();
        t.parse(is, baus);
        return baus.toByteArray();
    }

    public static void createPDF(String[] tipoSeleccionados, List<Vehiculo> listaVehiculos, List<EstadoVehiculo> listaEstados, String[] mesyanioSeleccionados)
            throws IOException, DocumentException {
        ControladorVehiculo cv = new ControladorVehiculo();

        System.out.println("lista vehiculos" + listaVehiculos);
        System.out.println("lista vehiculos" + cv.getListaVehiculos());

        System.out.println("lsiata despie sd eeliminar" + listaVehiculos);
//        StringBuilder buf = new StringBuilder();
//        StringBuilder buf1 = new StringBuilder();
//        buf1.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
//        buf1.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
//        buf1.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
//        buf1.append("<head>");
//        // buf.append("<div id='page-header' class='header'>" + "<img  "
//        // + "height='" + parameters.get("templateHeaderHeight")
//        // + "' width='" + parameters.get("templateHeaderWidth") + "' "
//        // + "src='" + parameters.get("templateHeader") + "'/>" + "</div>");
//        buf1.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
//        buf1.append("<title>Ppless PDF Document</title> ");
//        buf1.append("<style type='text/css' media='print'> ");
//        buf1.append("* {font-family: Arial, 'sans-serif'  !important; font-size:12pt !important; }");
//        // buf.append("@page { size:8.5in 11in; margin: 0.25in; padding:1em; @bottom-left { content: element(footer); } } ");
//
//        buf1.append("@page { size: landscape;"
//                + "	margin: 0.5cm;"
//                + "; padding:0.5em"
//                + "; margin-top:"
//                + "50"
//                + "px;margin-bottom:"
//                + "50"
//                + "px;@bottom-left{ content: element(footer); };background-image: url('"
//                + ""
//                + "');background-repeat: no-repeat;background-position: 0px 0px; }");
//        // buf.append("#footer { font-size: 80%; font-style: italic;  position: running(footer); top: 0; left: 0;  background-image: url(http://localhost:8080/assets/img/fondo.jpg); background-repeat: no-repeat; left bottom;}");
//        buf1.append("#footer { font-size: 20%; font-style: italic;  padding:0em;position: running(footer); top: 0; right:0;background-image: url('"
//                + "" + "')}");
//        buf1.append("#header { font-size: 30%; font-style: italic;  margin:0px;padding:0em;position: running(page-header); top: 0; right:0;}");
//        buf1.append("#pagenumber:before { content: counter(page); } ");
//        buf1.append("#pagecount:before { content: counter(pages); }"
//                + "H1.SaltoDePagina{PAGE-BREAK-AFTER: always} "
//                + ".page-number:before {content: counter(page); }"
//                + ".page-count:before {content: counter(pages); }");
//        buf1.append("</style></head>");
//        buf1.append("<body><div style=\"text-align:center;\">"
//                + "<table style=\"text-align:center;\" width=\"9800\" border=\"0\" cellspacing=\"0\"  cellpadding=\"0\">"
//                + "<tr>"
//                + "  <td ><p>Page <span class=\"page-number\"/> of <span class=\"page-count\"/></p></td>\n"
//                + "</tr>"
//                + "<tr>"
//                + "  <td rowspan=\"2\"><IMG SRC=\"/home/yesica/NetBeansProjects/mtop/src/main/webapp/resources/mtop1.jpg\" WIDTH=120 HEIGHT=70 ALT=\"Obra de K. Haring\"></td>"
//                + " <td height=30 width=500 ><h3 align=\"center\">MINISTERIO DE TRANSPORTE Y OBRAS P&Uacute;BLICAS: DIRECCI&Oacute;N PROVINCIAL DE LOJA</h3></td>"
//                + "</tr>"
//                + "<tr><td width=500><h3 align=\"center\">CONTROL DE ESTADO DE EQUIPO CAMINERO</h3></td></tr></table>"
//                + "<table style=\"text-align:center;\"  height=5 ><tr><td  width=150 ><h6>CLAVE DE N&Uacute;MERO Y COLOR -></h6>"
//                + "</td><td style=\"background-color:#3333FF\" width=20 VALIGN=TOP ><h5>1</h5></td>"
//                + "<td width=150 rowspan=\"2\" ><h6>EQUIPO BUENO INACTIVO</h6></td><td style=\"background-color:#669900\" width=20 VALIGN=TOP ><h4>2</h4></td>"
//                + "<td width=150 rowspan=\"2\" ><h6>EQUIPO TRABAJANDO NORMAL</h6></td><td style=\"background-color:#FFFF00\" width=20 VALIGN=TOP> <h4>3</h4></td>"
//                + "<td width=150 rowspan=\"2\" ><h6>EQUIPO TRABAJANDO CON FALLAS</h6></td><td  style=\"background-color:red\" width=20 VALIGN=TOP><h4>4</h4></td>"
//                + "<td width=150 rowspan=\"2\"><h6>EQUIPO EN REPARACI&Oacute;N</h6></td><td style=\"background-color:#993300\" width=20 VALIGN=TOP><h4>5</h4></td>"
//                + "<td width=150 rowspan=\"2\"><h6>EQUIPO PARA BAJA O REMATE</h6></td></tr>"
//                + "<tr><td></td></tr>"
//                + "</table></div>");
//
//        buf1.append("</body>");
//        buf1.append("</html>");
//
//        buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
//        buf.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
//        buf.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
//        buf.append("<head>");
//        // buf.append("<div id='page-header' class='header'>" + "<img  "
//        // + "height='" + parameters.get("templateHeaderHeight")
//        // + "' width='" + parameters.get("templateHeaderWidth") + "' "
//        // + "src='" + parameters.get("templateHeader") + "'/>" + "</div>");
//        buf.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
//        buf.append("<title>Ppless PDF Document</title> ");
//        buf.append("<style type='text/css' media='print'> ");
//        buf.append("* {font-family: Arial, 'sans-serif'  !important; font-size:12pt !important; }");
//        // buf.append("@page { size:8.5in 11in; margin: 0.25in; padding:1em; @bottom-left { content: element(footer); } } ");
//
//        buf.append("@page { size: landscape;"
//                + "	margin: 0.5cm;"
//                + "; padding:0.5em"
//                + "; margin-top:"
//                + "50"
//                + "px;margin-bottom:"
//                + "50"
//                + "px;@bottom-left{ content: element(footer); };background-image: url('"
//                + ""
//                + "');background-repeat: no-repeat;background-position: 0px 0px; }");
//        // buf.append("#footer { font-size: 80%; font-style: italic;  position: running(footer); top: 0; left: 0;  background-image: url(http://localhost:8080/assets/img/fondo.jpg); background-repeat: no-repeat; left bottom;}");
//        buf.append("#footer { font-size: 20%; font-style: italic;  padding:0em;position: running(footer); top: 0; right:0;background-image: url('"
//                + "" + "')}");
//        buf.append("#header { font-size: 30%; font-style: italic;  margin:0px;padding:0em;position: running(page-header); top: 0; right:0;}");
//        buf.append("#pagenumber:before { content: counter(page); } ");
//        buf.append("#pagecount:before { content: counter(pages); }"
//                + "H1.SaltoDePagina{PAGE-BREAK-AFTER: always} "
//                + ".page-number:before {content: counter(page); }"
//                + ".page-count:before {content: counter(pages); }");
//        buf.append("</style></head>");
//        buf.append("<body><div style=\"text-align:center;\">"
//                + "<table style=\"text-align:center;\" width=\"9800\" border=\"0\" cellspacing=\"0\"  cellpadding=\"0\">"
//                + "<tr>"
//                + "  <td ><p>Page <span class=\"page-number\"/> of <span class=\"page-count\"/></p></td>\n"
//                + "</tr>"
//                + "<tr>"
//                + "  <td rowspan=\"2\"><IMG SRC=\"/home/yesica/NetBeansProjects/mtop/src/main/webapp/resources/mtop1.jpg\" WIDTH=120 HEIGHT=70 ALT=\"Obra de K. Haring\"></td>"
//                + " <td height=30 width=500 ><h3 align=\"center\">MINISTERIO DE TRANSPORTE Y OBRAS P&Uacute;BLICAS: DIRECCI&Oacute;N PROVINCIAL DE LOJA</h3></td>"
//                + "</tr>"
//                + "<tr><td width=500><h3 align=\"center\">CONTROL DE ESTADO DE EQUIPO CAMINERO</h3></td></tr></table>"
//                + "<table style=\"text-align:center;\"  height=5 ><tr><td  width=150 ><h6>CLAVE DE N&Uacute;MERO Y COLOR -></h6>"
//                + "</td><td style=\"background-color:#3333FF\" width=20 VALIGN=TOP ><h5>1</h5></td>"
//                + "<td width=150 rowspan=\"2\" ><h6>EQUIPO BUENO INACTIVO</h6></td><td style=\"background-color:#669900\" width=20 VALIGN=TOP ><h4>2</h4></td>"
//                + "<td width=150 rowspan=\"2\" ><h6>EQUIPO TRABAJANDO NORMAL</h6></td><td style=\"background-color:#FFFF00\" width=20 VALIGN=TOP> <h4>3</h4></td>"
//                + "<td width=150 rowspan=\"2\" ><h6>EQUIPO TRABAJANDO CON FALLAS</h6></td><td  style=\"background-color:red\" width=20 VALIGN=TOP><h4>4</h4></td>"
//                + "<td width=150 rowspan=\"2\"><h6>EQUIPO EN REPARACI&Oacute;N</h6></td><td style=\"background-color:#993300\" width=20 VALIGN=TOP><h4>5</h4></td>"
//                + "<td width=150 rowspan=\"2\"><h6>EQUIPO PARA BAJA O REMATE</h6></td></tr>"
//                + "<tr><td></td></tr>"
//                + "</table></div>");

//        for (String s : tipoSeleccionados) {
//            buf.append("<table border=\"1\" cellspacing=\"0\"  cellpadding=\"0\"> <tr><td>EQUIPO: ");
//            buf.append(s);
//            buf.append("</td></tr></table>");
//            buf.append("<table border=\"0\" cellspacing=\"0\"  cellpadding=\"0\">"
//                    + "<tr><td width=10 ><h6>N&#176;</h6></td><td width=50 ><h6>REGISTRO</h6></td> <td width=50 VALIGN=TOP><h6>MARCA</h6></td><td width=50 VALIGN=TOP><h6>MODELO</h6></td><td width=50><h6>OPERADOR</h6></td><td width=50><h6>LOCALIZACI&Oacute;N</h6></td></tr>");
//
//            System.out.println("valor de s" + s);
//            Integer c = 0;
//            for (Vehiculo v : listaVehiculos) {
//                c++;
//                buf.append("<tr>");
//                //1
//                buf.append("<td align='left'>");
//                buf.append("<h6>");
//                buf.append(c);
//                buf.append("</h6>");
//                buf.append("</td>");
//                //1
//                buf.append("<td align='left' >");
//                buf.append("<h6>");
//                buf.append(v.getNumRegistro());
//                buf.append("</h6>");
//                buf.append("</td>");
//
//                //2
//                buf.append("<td align='left'  >");
//                buf.append("<h6>");
//                buf.append(v.getMarca());
//                buf.append("</h6>");
//                buf.append("</td>");
//                //3
//                buf.append("<td align='left' >");
//                buf.append("<h6>");
//                buf.append(v.getModelo());
//                buf.append("</h6>");
//                buf.append("</td>");
//                //4
//                buf.append("<td align='left' >");
//                buf.append("<h6>");
//                buf.append(v.getPersona().concatenarNombre());
//                buf.append("</h6>");
//                buf.append("</td>");
//                buf.append("<td align='left'  >");
//                buf.append("<h6>");
//                buf.append(cv.obtenerUltimaUbicacionV(v));
//                buf.append("</h6>");
//                buf.append("</td>");
//
//                buf.append("<td align='right'>");
//                buf.append("<span style='font-size:10pt !important; font-style: \"italic\"; float:right'>");
//                buf.append("TR: ");
//                buf.append("CJ");
//                buf.append("</span>");
//                buf.append("</td>");
//                buf.append("</tr>");
//
//            }
//
//            buf.append("<tr>");
//            buf.append("<td align='left'>");
//            buf.append("<p>");
//            buf.append("2014");
//            buf.append("</p>");
//            buf.append("</td>");
//            buf.append("<td></td>");
//            buf.append("</tr>");
//            buf.append("</table>");
//        }
//
//        buf.append("<p>&nbsp;</p>\n"
//                + "<table border=\"0\" cellspacing=\"0\"  cellpadding=\"3\">\n"
//                + "<tr>\n"
//                + "<td><strong> <span>PARA:                <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></span> </strong></td>\n"
//                + "<td>\n"
//                + "<p><span>Dr. Gustavo Jalkh               <span>(respetar orden jer&aacute;rquico seg&uacute;n               organigrama)</span></span></p>\n"
//                + "</td>\n"
//                + "</tr>\n"
//                + "<tr>\n"
//                + "<td>&nbsp;</td>\n"
//                + "<td>\n"
//                + "<p><strong> <span>PRESIDENTE</span> </strong></p>\n"
//                + "</td>\n"
//                + "</tr>\n"
//                + "</table>\n"
//                + "<table border=\"0\" cellspacing=\"0\" cellpadding=\"3\">\n"
//                + "<tr>\n"
//                + "<td>\n"
//                + "<p><strong> <span>DE:                  <span>&nbsp;&nbsp;&nbsp;</span> <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></span> </strong></p>\n"
//                + "</td>\n"
//                + "<td><span> Karina Paola Logro&ntilde;o Santill&aacute;n</span></td>\n"
//                + "</tr>\n"
//                + "<tr>\n"
//                + "<td>&nbsp;</td>\n"
//                + "<td>\n"
//                + "<p><strong> <span>Secretario</span> </strong></p>\n"
//                + "</td>\n"
//                + "</tr>\n"
//                + "</table>\n"
//                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
//                + "<table border=\"0\" cellspacing=\"0\" cellpadding=\"5\">\n"
//                + "<tbody>\n"
//                + "<tr>\n"
//                + "<td><strong> <span>ASUNTO:               <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></span> </strong></td>\n"
//                + "<td><span>PRUEBA 3</span></td>\n"
//                + "<td>&nbsp;</td>\n"
//                + "</tr>\n"
//                + "</tbody>\n"
//                + "</table>\n"
//                + "<p>&nbsp;&nbsp;<strong>&nbsp;</strong><span>&nbsp;</span></p>\n"
//                + "<p><strong> <span>Se expone el tema directamente, conciso y         claro.</span> </strong></p>\n"
//                + "<p><span>&nbsp;</span></p>\n"
//                + "<p><span>Solicito que todas las unidades utilicen las siglas de      "
//                + " identificaci&oacute;n aprobadas, a partir del d&iacute;a lunes 13       "
//                + "de mayo de 2013, con car&aacute;cter obligatorio en los       "
//                + "memorandos y oficios, de las unidades administrativas del     "
//                + "  Consejo de la Judicatura.</span></p>\n"
//                + "<p><span>&nbsp;</span></p>\n"
//                + "<p><span>&nbsp;</span></p>\n"
//                + "<p><span>Atentamente,</span></p>\n"
//                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
//                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
//                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
//                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
//                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
//                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
//                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
//                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
//                + "<table border=\"0\" cellspacing=\"0\" cellpadding=\"5\">\n"
//                + "<tbody>\n"
//                + "<tr>\n"
//                + "<td><span> Karina Paola Logro&ntilde;o Santill&aacute;n</span></td>\n"
//                + "</tr>\n"
//                + "<tr>\n"
//                + "<td><strong> <span>Secretario</span> </strong></td>\n"
//                + "</tr>\n"
//                + "</tbody>\n"
//                + "</table>\n"
//                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
//                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
//                + "<p><strong> <span>C.C.:</span> </strong> <strong> <span>Si se requiere que m&aacute;s unidades reciban el         documento detallar el nombre y cargo</span> </strong></p>");
//
//        buf.append("</body>");
//        buf.append("</html>");
//
//        ByteArrayInputStream bais = new ByteArrayInputStream(buf.toString()
//                .getBytes());
//        ByteArrayInputStream bais1 = new ByteArrayInputStream(buf1.toString()
//                .getBytes());
        System.out.println("\n\n\n\n tipos seleccionados" + tipoSeleccionados.length);
        System.out.println("\n\n\n\n " + mesyanioSeleccionados.length);
        OutputStream os = new FileOutputStream("controlEstado.pdf");

        try {
            //2 lineas para hacerlo horizontal
            Rectangle a4 = PageSize.A4;
            Rectangle a4Landscape = a4.rotate();
            // Document document = new Document();

            Document document = new Document(a4Landscape);

            PdfWriter writer = PdfWriter.getInstance(document, os);
            Rectangle rct = new Rectangle(200, 54, 180, 550);
            //Definimos un nombre y un tamaño para el PageBox los nombres posibles son: “crop”, “trim”, “art” and “bleed”.
            writer.setBoxSize("art", rct);

            HeaderFooter event = new HeaderFooter();
//            StringBuilder s=new StringBuilder(getValidHtmlDocument(bais1).length);
//            for(int i=0 ;i<getValidHtmlDocument(bais1).length;i++){
//               s.append((char)getValidHtmlDocument(bais)[i]);
//            }
////            String valueOf = IOUtils.toString(getValidHtmlDocument(bais1), "UTF-8");
////            bais1 = new ByteArrayInputStream(getValidHtmlDocument(bais1));
//            event.setEncabezado(s.toString());
            writer.setPageEvent(event);
            Paragraph parrafo;
            int i;

            writer.open();
            // indicamos que objecto manejara los eventos al escribir el Pdf

            document.open();
            document.setMargins(20, 20, 60, 20);
////            PdfPTable table = new PdfPTable(4);
//            table.setWidths(new int[]{1, 3, 3, 3});
//            table.setWidthPercentage(10F);
//
//            PdfPCell cell;

//
//            bais = new ByteArrayInputStream(getValidHtmlDocument(bais));
//            XMLWorkerHelper.getInstance().parseXHtml(writer, document, bais);
            //Creamos una cantidad significativa de paginas para probar el encabezado
            int val = 15;
            String mes = "";
            String anio = "";
            Boolean bandera = false;
            SimpleDateFormat sdf = new SimpleDateFormat("MMMMM/yyyy");
            Calendar f = Calendar.getInstance().getInstance();
            Calendar cal = Calendar.getInstance();
            for (String mesSeleccionado : mesyanioSeleccionados) {

                mes = "";
                anio = "";
                for (int j = 0; j < mesSeleccionado.length(); j++) {
                    if (Character.isDigit(mesSeleccionado.charAt(j))) {
                        anio = anio + mesSeleccionado.substring(j, mesSeleccionado.length());
                        break;
                    } else {
                        mes = mes + mesSeleccionado.charAt(j);
                    }
                }
                mes = mes.substring(0, mes.length() - 1);
                System.out.println("mes" + mes);
                System.out.println("anio" + anio);
                val = 15;

                for (String s : tipoSeleccionados) {
                    bandera = false;
                    for (Vehiculo v : listaVehiculos) {
                        if (v.getTipo().equals(s)) {
                            f = Calendar.getInstance().getInstance();
                            cal = Calendar.getInstance().getInstance();
                            for (EstadoVehiculo ev : v.getListaEstados()) {
                                f.setTime(ev.getFechaEntrada());
                                cal = Calendar.getInstance();
                                cal.add(Calendar.MONTH, 1);

                                System.out.println("ffff" + sdf.format(f.getTime()));
                                System.out.println("cal" + sdf.format(cal.getTime()));

                                System.out.println("del vehiculo" + v + " fechas a aniadir" + sdf.format(ev.getFechaEntrada().getTime()));
                                if (mesSeleccionado.equals(sdf.format(ev.getFechaEntrada().getTime())) || f.compareTo(cal) > 0) {
                                    bandera = true;
                                }

                            }

                        }
                    }
                    System.out.println("valor de la bandera+"+bandera);
                            
                    if (bandera == true) {
                        System.out.println("ssssssss" + s);
//                    document.newPage();
                        PdfPTable table = new PdfPTable(4);
                        table.setWidths(new int[]{1, 3, 3, 3});
                        table.setWidthPercentage(10F);
                        PdfPCell cell;
                        PdfPCell cell1;
                        //             row 1, cell 1
                        cell = new PdfPCell(new Phrase(" "));
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setFixedHeight(72f);
                        table.addCell(cell);
                        table.addCell(cell);
                        table.addCell(cell);
                        table.addCell(cell);
                        document.add(table);

                        // row 1, cell 1
                        cell1 = new PdfPCell(new Phrase("CLAVE DE NÚMERO Y COLOR ->", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        PdfPTable table1 = new PdfPTable(11);
                        table1.setWidths(new int[]{100, 30, 100, 30, 100, 30, 100, 30, 100, 30, 100});
                        table1.setWidthPercentage(100F);
                        table1.addCell(cell1);
                        // row 1, cell 2
                        cell1 = new PdfPCell(new Phrase("1"));

                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

                        cell1.setBackgroundColor(BaseColor.BLUE);
                        table1.addCell(cell1);
                        // row 1, cell 3
                        cell1 = new PdfPCell(new Phrase("EQUIPO BUENO INACTIVO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        table1.addCell(cell1);
                        // row 1, cell 4
                        cell1 = new PdfPCell(new Phrase("2"));
                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell1.setBackgroundColor(new BaseColor(98, 156, 16));
                        table1.addCell(cell1);
                        // row 1, cell 5
                        cell1 = new PdfPCell(new Phrase("EQUIPO TRABAJANDO NORMAL", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        table1.addCell(cell1);
                        // row 1, cell 6
                        cell1 = new PdfPCell(new Phrase("3"));
                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell1.setBackgroundColor(BaseColor.YELLOW);
                        table1.addCell(cell1);
                        // row 1, cell 7
                        cell1 = new PdfPCell(new Phrase("EQUIPO TRABAJANDO CON FALLAS", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        table1.addCell(cell1);
                        // row 1, cell 8
                        cell1 = new PdfPCell(new Phrase("4"));
                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell1.setBackgroundColor(BaseColor.RED);
                        table1.addCell(cell1);
                        // row 1, cell 9
                        cell1 = new PdfPCell(new Phrase("EQUIPO EN REPARACIÓN", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        table1.addCell(cell1);
                        // row 1, cell 10
                        cell1 = new PdfPCell(new Phrase("5"));
                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell1.setBackgroundColor(new BaseColor(122, 45, 5));
                        table1.addCell(cell1);
                        // row 1, cell 11
                        cell1 = new PdfPCell(new Phrase("EQUIPO PARA BAJA O REMATE", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        table1.addCell(cell1);

                        document.add(table1);

                        table1 = new PdfPTable(5);
                        table1.setWidths(new int[]{200, 100, 100, 50, 200});
                        table1.setWidthPercentage(100F);
                        cell1 = new PdfPCell(new Phrase("EQUIPO: " + s, FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD)));
                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        table1.addCell(cell1);
                        cell1 = new PdfPCell(new Phrase("MES: ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD)));
                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        table1.addCell(cell1);
                        cell1 = new PdfPCell(new Phrase(mes, FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD)));
                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        table1.addCell(cell1);
                        cell1 = new PdfPCell(new Phrase("AÑO: ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD)));
                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        table1.addCell(cell1);
                        cell1 = new PdfPCell(new Phrase(anio, FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD)));
                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        table1.addCell(cell1);

                        document.add(table1);

                        table1 = new PdfPTable(38);
                        table1.setWidths(new int[]{30, 100, 80, 100, 110, 140, 80, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30});
                        table1.setWidthPercentage(100F);
                        cell1 = new PdfPCell(new Phrase("Nº", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        table1.addCell(cell1);
                        cell1 = new PdfPCell(new Phrase("REGISTRO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        table1.addCell(cell1);
                        cell1 = new PdfPCell(new Phrase("MARCA", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        table1.addCell(cell1);
                        cell1 = new PdfPCell(new Phrase("MODELO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        table1.addCell(cell1);
                        cell1 = new PdfPCell(new Phrase("OPERADOR", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        table1.addCell(cell1);
                        cell1 = new PdfPCell(new Phrase("LOCALIZACIÓN", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        table1.addCell(cell1);
                        cell1 = new PdfPCell(new Phrase("CLAVE", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        table1.addCell(cell1);

                        for (int j = 1; j <= 31; j++) {
                            cell1 = new PdfPCell(new Phrase(String.valueOf(j), FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            table1.addCell(cell1);
                        }
//                document.add(table1);
                        Integer c = 0;
                        String s1 = "";

                        for (Vehiculo v : listaVehiculos) {

                            if (v.getTipo().equals(s)) {

                                List<EstadoVehiculo> listaFechasyEstados = new ArrayList<EstadoVehiculo>();
                                f = Calendar.getInstance();

                                for (EstadoVehiculo ev : v.getListaEstados()) {
                                    f.setTime(ev.getFechaEntrada());
                                    cal = Calendar.getInstance();
                                    cal.add(Calendar.MONTH, 1);

                                    Integer mes1 = f.get(Calendar.MONTH) + 1;
                                    System.out.println("ffff" + sdf.format(f.getTime()));
                                    System.out.println("cal" + sdf.format(cal.getTime()));

                                    System.out.println("del vehiculo" + v + " fechas a aniadir" + sdf.format(ev.getFechaEntrada().getTime()));
                                    if (mesSeleccionado.equals(sdf.format(ev.getFechaEntrada().getTime())) || f.compareTo(cal) > 0) {
                                        listaFechasyEstados.add(ev);
                                        System.out.println("del vehiculo" + v + "lista de fechas aniadiendo" + sdf.format(ev.getFechaEntrada().getTime()));

                                    }
                                }
                                System.out.println("mes seleccionado" + mesSeleccionado);
                                System.out.println("del vehiculo " + v);
                                System.out.println("lista de estado"+listaFechasyEstados);
                                if (!listaFechasyEstados.isEmpty()) {
                                    c++;

                                    System.out.println("ccccc" + c);
                                    if (c == val) {
                                        document.add(table1);
                                        c = 0;
                                        val = 15;
                                        System.out.println("entro a new page");
                                        document.newPage();
                                        table = new PdfPTable(4);
                                        table.setWidths(new int[]{1, 3, 3, 3});
                                        table.setWidthPercentage(10F);

//             row 1, cell 1
                                        cell = new PdfPCell(new Phrase(" "));
                                        cell.setBorder(Rectangle.NO_BORDER);
                                        cell.setFixedHeight(72f);
                                        table.addCell(cell);
                                        table.addCell(cell);
                                        table.addCell(cell);
                                        table.addCell(cell);
                                        document.add(table);

                                        // row 1, cell 1
                                        cell1 = new PdfPCell(new Phrase("CLAVE DE NÚMERO Y COLOR ->", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        table1 = new PdfPTable(11);
                                        table1.setWidths(new int[]{100, 30, 100, 30, 100, 30, 100, 30, 100, 30, 100});
                                        table1.setWidthPercentage(100F);
                                        table1.addCell(cell1);
                                        // row 1, cell 2
                                        cell1 = new PdfPCell(new Phrase("1"));

                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

                                        cell1.setBackgroundColor(BaseColor.BLUE);
                                        table1.addCell(cell1);
                                        // row 1, cell 3
                                        cell1 = new PdfPCell(new Phrase("EQUIPO BUENO INACTIVO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        table1.addCell(cell1);
                                        // row 1, cell 4
                                        cell1 = new PdfPCell(new Phrase("2"));
                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        cell1.setBackgroundColor(new BaseColor(98, 156, 16));
                                        table1.addCell(cell1);
                                        // row 1, cell 5
                                        cell1 = new PdfPCell(new Phrase("EQUIPO TRABAJANDO NORMAL", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        table1.addCell(cell1);
                                        // row 1, cell 6
                                        cell1 = new PdfPCell(new Phrase("3"));
                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        cell1.setBackgroundColor(BaseColor.YELLOW);
                                        table1.addCell(cell1);
                                        // row 1, cell 7
                                        cell1 = new PdfPCell(new Phrase("EQUIPO TRABAJANDO CON FALLAS", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        table1.addCell(cell1);
                                        // row 1, cell 8
                                        cell1 = new PdfPCell(new Phrase("4"));
                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        cell1.setBackgroundColor(BaseColor.RED);
                                        table1.addCell(cell1);
                                        // row 1, cell 9
                                        cell1 = new PdfPCell(new Phrase("EQUIPO EN REPARACIÓN", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        table1.addCell(cell1);
                                        // row 1, cell 10
                                        cell1 = new PdfPCell(new Phrase("5"));
                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        cell1.setBackgroundColor(new BaseColor(122, 45, 5));
                                        table1.addCell(cell1);
                                        // row 1, cell 11
                                        cell1 = new PdfPCell(new Phrase("EQUIPO PARA BAJA O REMATE", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        table1.addCell(cell1);

                                        document.add(table1);

                                        table1 = new PdfPTable(5);
                                        table1.setWidths(new int[]{200, 100, 100, 50, 200});
                                        table1.setWidthPercentage(100F);
                                        cell1 = new PdfPCell(new Phrase("EQUIPO: " + s, FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD)));
                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        table1.addCell(cell1);
                                        cell1 = new PdfPCell(new Phrase("MES22: ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD)));
                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        table1.addCell(cell1);
                                        cell1 = new PdfPCell(new Phrase(mes, FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD)));
                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        table1.addCell(cell1);
                                        cell1 = new PdfPCell(new Phrase("AÑO: ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD)));
                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        table1.addCell(cell1);
                                        cell1 = new PdfPCell(new Phrase(anio, FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD)));
                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        table1.addCell(cell1);
                                        document.add(table1);

                                        table1 = new PdfPTable(38);
                                        table1.setWidths(new int[]{30, 100, 80, 100, 110, 140, 80, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30});
                                        table1.setWidthPercentage(100F);
                                        cell1 = new PdfPCell(new Phrase("Nº", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        table1.addCell(cell1);
                                        cell1 = new PdfPCell(new Phrase("REGISTRO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        table1.addCell(cell1);
                                        cell1 = new PdfPCell(new Phrase("MARCA", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        table1.addCell(cell1);
                                        cell1 = new PdfPCell(new Phrase("MODELO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        table1.addCell(cell1);
                                        cell1 = new PdfPCell(new Phrase("OPERADOR", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        table1.addCell(cell1);
                                        cell1 = new PdfPCell(new Phrase("LOCALIZACIÓN", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        table1.addCell(cell1);
                                        cell1 = new PdfPCell(new Phrase("CLAVE", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

                                        table1.addCell(cell1);

                                        for (int j = 1; j <= 31; j++) {
                                            cell1 = new PdfPCell(new Phrase(String.valueOf(j), FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                            table1.addCell(cell1);
                                        }

                                    }
//                        table1.setWidths(new int[]{100, 30, 100, 30, 100, 30, 100, 30, 100, 30, 100});
//                            table1.setWidthPercentage(100F);

                                    s1 = v.getNumRegistro();
                                    if (s1.length() > 20) {
                                        s1 = s1.substring(0, 20);
                                    }

                                    System.out.println("vs1." + v.getListaEstados());

                                    cell1 = new PdfPCell(new Phrase(String.valueOf(c).toString() + " ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                    cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    cell1.setRowspan(2);
                                    table1.addCell(cell1);

                                    cell1 = new PdfPCell(new Phrase(s1, FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                    cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    cell1.setRowspan(2);
                                    table1.addCell(cell1);

                                    s1 = v.getMarca();
                                    if (s1.length() > 20) {
                                        s1 = s1.substring(0, 20);
                                    }
                                    cell1 = new PdfPCell(new Phrase(s1, FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                    cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    cell1.setRowspan(2);
                                    table1.addCell(cell1);

                                    s1 = v.getModelo();
                                    if (s1.length() > 20) {
                                        s1 = s1.substring(0, 20);
                                    }
                                    cell1 = new PdfPCell(new Phrase(s1, FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                    cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    cell1.setRowspan(2);
                                    table1.addCell(cell1);

                                    s1 = v.getPersona().concatenarNombre();
                                    if (s1.length() > 30) {
                                        s1 = s1.substring(0, 30);
                                    }
                                    cell1 = new PdfPCell(new Phrase(s1, FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                    cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    cell1.setRowspan(2);
                                    table1.addCell(cell1);

                                    s1 = cv.obtenerUltimaUbicacionV2(v, listaEstados);
                                    if (s1.length() > 30) {
                                        s1 = s1.substring(0, 30);
                                    }
                                    cell1 = new PdfPCell(new Phrase(s1, FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                    cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    cell1.setRowspan(2);
                                    table1.addCell(cell1);
                                    cell1 = new PdfPCell(new Phrase("número", FontFactory.getFont(FontFactory.TIMES_ROMAN, 5, Font.BOLD)));
                                    cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

                                    table1.addCell(cell1);
                                   
                                    System.out.println("estados" + listaEstados);
                                    Calendar fechamayor = Calendar.getInstance();
                                    Integer aux = 0;

                                    if (sdf.format(fechamayor.getTime()).equals(mesSeleccionado)) {

                                        fechamayor.setTime(listaFechasyEstados.get(aux).getFechaEntrada());
                                        System.out.println("fecha mayor noooo 31" + fechamayor);
                                    } else {
                                        fechamayor.set(Calendar.DAY_OF_MONTH, 31);
                                        System.out.println("fecha mayor 31" + fechamayor);
                                    }
                                    for (int j = 1; j <= 31; j++) {
                                        System.out.println("jjjjjj" + j);
                                        System.out.println("dia del mes de cambio sumar 1" + fechamayor.get(Calendar.DAY_OF_MONTH));
                                        if (j <= fechamayor.get(Calendar.DAY_OF_MONTH)) {
                                            System.out.println("vehiculo de id" + v + "nombreeee" + listaFechasyEstados.get(aux).getNombre());
                                            if (listaFechasyEstados.get(aux).getNombre().equals("Equipo bueno inactivo")) {
                                                System.out.println("nombreeee" + listaFechasyEstados.get(aux).getNombre());

                                                cell1 = new PdfPCell(new Phrase("1", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                                table1.addCell(cell1);
                                            } else {
                                                if (listaFechasyEstados.get(aux).getNombre().equals("Equipo trabajando normal")) {
                                                    System.out.println("nombreeee" + listaFechasyEstados.get(aux).getNombre());
                                                    cell1 = new PdfPCell(new Phrase("2", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                                    cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                                    table1.addCell(cell1);
                                                } else {
                                                    if (listaFechasyEstados.get(aux).getNombre().equals("Equipo trabajando con fallas")) {
                                                        System.out.println("nombreeee" + listaFechasyEstados.get(aux).getNombre());
                                                        cell1 = new PdfPCell(new Phrase("3", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                                        table1.addCell(cell1);
                                                    } else {
                                                        if (listaFechasyEstados.get(aux).getNombre().equals("Equipo en reparación")) {
                                                            System.out.println("nombreeee" + listaFechasyEstados.get(aux).getNombre());
                                                            cell1 = new PdfPCell(new Phrase("4", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                                            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                                            table1.addCell(cell1);
                                                        } else {
                                                            //Equipo para baja o remate
                                                            System.out.println("nombreeee" + listaFechasyEstados.get(aux).getNombre());
                                                            cell1 = new PdfPCell(new Phrase("5", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                                            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                                            table1.addCell(cell1);

                                                        }

                                                    }

                                                }

                                            }
                                        } else {
                                            aux++;
                                            j--;
                                            System.out.println("auxxxxx" + aux);
                                            System.out.println("listade de estados " + listaFechasyEstados.size());
                                            if (aux < listaFechasyEstados.size()) {
                                                System.out.println("entro a siguiente estado");

                                                System.out.println("fecha mayor del antigup estado" + fechamayor);
                                                fechamayor.setTime(listaFechasyEstados.get(aux).getFechaEntrada());
                                                System.out.println("fecha mayor del siguiente estado" + fechamayor);
                                            } else {
                                                System.out.println("");
                                                aux--;
                                                System.out.println("fecha mayor del antigup estado" + fechamayor);
                                                fechamayor = Calendar.getInstance();
                                                fechamayor.set(Calendar.DAY_OF_MONTH, 31);
//                                                fechamayor.add(Calendar.DAY_OF_MONTH, 30 - Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                                                System.out.println("fecha mayor del siguiente estado" + fechamayor);
                                            }
                                        }

                                    }
                                    cell1 = new PdfPCell(new Phrase("color", FontFactory.getFont(FontFactory.TIMES_ROMAN, 5, Font.BOLD)));
                                    cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

                                    table1.addCell(cell1);

                                    fechamayor = Calendar.getInstance();
                                    aux = 0;

                                    if (sdf.format(fechamayor.getTime()).equals(mesSeleccionado)) {
                                        fechamayor.setTime(listaFechasyEstados.get(aux).getFechaEntrada());
                                    } else {
                                        fechamayor.set(Calendar.DAY_OF_MONTH, 31);
                                    }
                                    for (int j = 1; j <= 31; j++) {
                                        if (j <= fechamayor.get(Calendar.DAY_OF_MONTH) ) {
                                            System.out.println("vehiculo de id" + v + "nombreeee" + listaFechasyEstados.get(aux).getNombre());
                                            if (listaFechasyEstados.get(aux).getNombre().equals("Equipo bueno inactivo")) {
                                                System.out.println("nombreeee" + listaFechasyEstados.get(aux).getNombre());

                                                cell1 = new PdfPCell(new Phrase(" ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                                cell1.setBackgroundColor(BaseColor.BLUE);
                                                table1.addCell(cell1);
                                            } else {
                                                if (listaFechasyEstados.get(aux).getNombre().equals("Equipo trabajando normal")) {
                                                    System.out.println("nombreeee" + listaFechasyEstados.get(aux).getNombre());
                                                    cell1 = new PdfPCell(new Phrase(" ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                                    cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                                    cell1.setBackgroundColor(new BaseColor(98, 156, 16));
                                                    table1.addCell(cell1);
                                                } else {
                                                    if (listaFechasyEstados.get(aux).getNombre().equals("Equipo trabajando con fallas")) {
                                                        System.out.println("nombreeee" + listaFechasyEstados.get(aux).getNombre());
                                                        cell1 = new PdfPCell(new Phrase(" ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                                        cell1.setBackgroundColor(BaseColor.YELLOW);
                                                        table1.addCell(cell1);
                                                    } else {
                                                        if (listaFechasyEstados.get(aux).getNombre().equals("Equipo en reparación")) {
                                                            System.out.println("nombreeee" + listaFechasyEstados.get(aux).getNombre());
                                                            cell1 = new PdfPCell(new Phrase(" ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                                            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                                            cell1.setBackgroundColor(BaseColor.RED);
                                                            table1.addCell(cell1);
                                                        } else {
                                                            //Equipo para baja o remate
                                                            System.out.println("nombreeee" + listaFechasyEstados.get(aux).getNombre());
                                                            cell1 = new PdfPCell(new Phrase(" ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                                                            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                                            cell1.setBackgroundColor(new BaseColor(122, 45, 5));
                                                            table1.addCell(cell1);

                                                        }

                                                    }

                                                }

                                            }
                                        } else {
                                            aux++;
                                            j--;
                                            System.out.println("auxxxxx2" + aux);
                                            System.out.println("listade de estados2 " + listaFechasyEstados.size());
                                            if (aux < listaFechasyEstados.size()) {
                                                fechamayor.setTime(listaFechasyEstados.get(aux).getFechaEntrada());
                                            } else {
                                                aux--;
                                                fechamayor = Calendar.getInstance();
//                                                fechamayor.add(Calendar.DAY_OF_MONTH, 30 - Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                                                fechamayor.set(Calendar.DAY_OF_MONTH, 31);
                                            }
                                        }
                                    }
                                }

                            }

                        }
                        document.add(table1);
                        document.newPage();
                        document.setPageCount(1);
                    }else{
//                        parrafo = new Paragraph(
//                                " \n \n \n  No existe registro del tipo "+s+" y mes seleccionado "+mesSeleccionado); 
//
//                        parrafo.setAlignment(Element.ALIGN_CENTER);
//
//                        document.add(parrafo);
                    }

                }
            }

//            table = new PdfPTable(4);
//            table.setWidths(new int[]{1, 3, 3, 3});
//            table.setWidthPercentage(100);
//           
//            // row 1, cell 1
//            cell = new PdfPCell(new Phrase("COLOR"));
//            cell.setRotation(90);
//            cell.setVerticalAlignment(Element.ALIGN_TOP);
//            table.addCell(cell);
//            // row 1, cell 2
//            cell = new PdfPCell(new Phrase("red / no borders"));
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setBackgroundColor(BaseColor.RED);
//            table.addCell(cell);
//            // row 1, cell 3
//            cell = new PdfPCell(new Phrase("green / black bottom border"));
//            cell.setBorder(Rectangle.BOTTOM);
//            cell.setBorderColorBottom(BaseColor.BLACK);
//            cell.setBorderWidthBottom(10f);
//            cell.setBackgroundColor(BaseColor.GREEN);
//            table.addCell(cell);
//            // row 1, cell 4
//            cell = new PdfPCell(new Phrase(
//                    "cyan / blue top border + padding"));
//            cell.setBorder(Rectangle.TOP);
//            cell.setUseBorderPadding(true);
//            cell.setBorderWidthTop(5f);
//            cell.setBorderColorTop(BaseColor.BLUE);
//            cell.setBackgroundColor(BaseColor.CYAN);
//            table.addCell(cell);
//            // row 2, cell 1
//            cell = new PdfPCell(new Phrase("GRAY"));
//            cell.setRotation(90);
//            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table.addCell(cell);
//            // row 2, cell 2
//            cell = new PdfPCell(new Phrase("0.6"));
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setGrayFill(0.6f);
//            table.addCell(cell);
//            // row 2, cell 3
//            cell = new PdfPCell(new Phrase("0.75"));
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setGrayFill(0.75f);
//            table.addCell(cell);
//            // row 2, cell 4
//            cell = new PdfPCell(new Phrase("0.9"));
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setGrayFill(0.9f);
//            table.addCell(cell);
//            // row 3, cell 1
//            cell = new PdfPCell(new Phrase("BORDERS"));
//            cell.setRotation(90);
//            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
//            table.addCell(cell);
//            // row 3, cell 2
//            cell = new PdfPCell(new Phrase("different borders"));
//            cell.setBorderWidthLeft(16f);
//            cell.setBorderWidthBottom(12f);
//            cell.setBorderWidthRight(8f);
//            cell.setBorderWidthTop(4f);
//            cell.setBorderColorLeft(BaseColor.RED);
//            cell.setBorderColorBottom(BaseColor.ORANGE);
//            cell.setBorderColorRight(BaseColor.YELLOW);
//            cell.setBorderColorTop(BaseColor.GREEN);
//            table.addCell(cell);
//            // row 3, cell 3
//            cell = new PdfPCell(new Phrase("with correct padding"));
//            cell.setUseBorderPadding(true);
//            cell.setBorderWidthLeft(16f);
//            cell.setBorderWidthBottom(12f);
//            cell.setBorderWidthRight(8f);
//            cell.setBorderWidthTop(4f);
//            cell.setBorderColorLeft(BaseColor.RED);
//            cell.setBorderColorBottom(BaseColor.ORANGE);
//            cell.setBorderColorRight(BaseColor.YELLOW);
//            cell.setBorderColorTop(BaseColor.GREEN);
//            table.addCell(cell);
//            // row 3, cell 4
//            cell = new PdfPCell(new Phrase("red border"));
//            cell.setBorderWidth(8f);
//            cell.setBorderColor(BaseColor.RED);
//            table.addCell(cell);
//            document.add(table);
//            document.newPage();
//            for (i = 0; i < 10; i++) {
//
//                parrafo = new Paragraph(
//                        "Esta es una de las paginas de prueba de nuestro programa, es la pagina numero 0x" + String.format("%03X", i + 42
//                        ));
//
//                parrafo.setAlignment(Element.ALIGN_CENTER);
//
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//                document.add(parrafo);
//
//                if (document.isInline()) {
//                    parrafo = new Paragraph(
//                            "nuevo documento" + String.format("%03X", i + 42
//                            ));
//
//                }
//                document.newPage();
//            }
            System.out.println("pagina"+document.getPageNumber());
            
            if(document.getPageNumber()==0){
                 parrafo = new Paragraph(
                                " \n \n \n  No existe registro del tipo  y mes seleccionado "); 

                        parrafo.setAlignment(Element.ALIGN_CENTER);

                        document.add(parrafo);
            }
            document.close();

            float b = writer.getVerticalPosition(true);
            System.out.println("bbbbbb" + b);

            writer.close();
            os.close();
            os = null;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    static class HeaderFooter extends PdfPageEventHelper {

        private String encabezado;

        public void onEndPage(PdfWriter writer, Document document) {

            Image logo = null;
            try {
                logo = Image.getInstance("mtop1.jpg");
                logo.setAlignment(Image.ALIGN_LEFT);
                logo.scaleAbsoluteHeight(20);
                logo.scaleAbsoluteWidth(20);
                logo.scalePercent(100);

            } catch (BadElementException ex) {
                Logger.getLogger(GeneradorPdf.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(GeneradorPdf.class.getName()).log(Level.SEVERE, null, ex);
            }
            Chunk chunk = new Chunk(logo, 0, -45);

            System.out.println(new File(".").getAbsolutePath());

            Rectangle rect = writer.getBoxSize("art");

            //Cabecera
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_RIGHT, new Phrase(chunk),
                    rect.getRight(), rect.getTop(), 0);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_LEFT, new Phrase("MINISTERIO DE TRANSPORTE Y OBRAS PÚBLICAS: DIRECCIÓN PROVINCIAL DE LOJA \n CONTROL DE ESTADO DE EQUIPO CAMINERO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD)),
                    rect.getLeft(), rect.getTop(), 0);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_LEFT, new Phrase("CONTROL DE ESTADO DE EQUIPO CAMINERO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD)),
                    rect.getLeft() + 70, rect.getTop() - 20, 0);

            //Pie
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_RIGHT, new Phrase(String.format("página %d ", writer.getPageNumber())),
                    (rect.getLeft()-150 + rect.getRight()) / 2, rect.getBottom() + 18, 0);

        }

    }

    public static void createPDF(InputStream is, OutputStream os)
            throws IOException, DocumentException {
        try {
            //2 lineas para hacerlo horizontal
            Rectangle a4 = PageSize.A4;
            Rectangle a4Landscape = a4.rotate();
            // Document document = new Document();

            Document document = new Document(a4Landscape);

            PdfWriter writer = PdfWriter.getInstance(document, os);
            Rectangle rct = new Rectangle(200, 54, 480, 550);
            //Definimos un nombre y un tamaño para el PageBox los nombres posibles son: “crop”, “trim”, “art” and “bleed”.
            writer.setBoxSize("art", rct);
            HeaderFooter event = new HeaderFooter();
            writer.setPageEvent(event);
            Paragraph parrafo;
            int i;

            writer.open();
            // indicamos que objecto manejara los eventos al escribir el Pdf

            document.open();
            is = new ByteArrayInputStream(getValidHtmlDocument(is));
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
            //Creamos una cantidad significativa de paginas para probar el encabezado
            for (i = 0; i < 10; i++) {
                parrafo = new Paragraph(
                        "Esta es una de las paginas de prueba de nuestro programa, es la pagina numero 0x" + String.format("%03X", i + 42
                        ));
                parrafo.setAlignment(Element.ALIGN_CENTER);

                document.add(parrafo);
                document.newPage();
            }

            document.close();

            float b = writer.getVerticalPosition(true);
            System.out.println("bbbbbb" + b);

            writer.close();
            os.close();
            os = null;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    private static class ResourceLoaderUserAgent extends ITextUserAgent {

        public ResourceLoaderUserAgent(ITextOutputDevice outputDevice) {
            super(outputDevice);
        }

        protected InputStream resolveAndOpenStream(String uri) {
            InputStream is = super.resolveAndOpenStream(uri);
            System.out.println("IN resolveAndOpenStream() " + uri);
            return is;
        }
    }

    public static void main(String args[]) throws IOException, com.lowagie.text.DocumentException {
        GeneradorPdf pu = new GeneradorPdf();
        StringBuilder buf = new StringBuilder();

        buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        buf.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        buf.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        buf.append("<head>");
        // buf.append("<div id='page-header' class='header'>" + "<img  "
        // + "height='" + parameters.get("templateHeaderHeight")
        // + "' width='" + parameters.get("templateHeaderWidth") + "' "
        // + "src='" + parameters.get("templateHeader") + "'/>" + "</div>");
        buf.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
        buf.append("<title>Ppless PDF Document</title> ");
        buf.append("<style type='text/css' media='print'>  ");
        buf.append("* {font-family: Arial, 'sans-serif'  !important; font-size:12pt !important; }");
        // buf.append("@page { size:8.5in 11in; margin: 0.25in; padding:1em; @bottom-left { content: element(footer); } } ");

        buf.append("@page { size:"
                + "A4"
                + "; margin: cm; padding:1em"
                + "; margin-top:"
                + "144"
                + "px;margin-bottom:"
                + "108"
                + "px;@bottom-left{ content: element(footer); };background-image: url('"
                + ""
                + "');background-repeat: no-repeat;background-position: 0px 0px;}");
        // buf.append("#footer { font-size: 80%; font-style: italic;  position: running(footer); top: 0; left: 0;  background-image: url(http://localhost:8080/assets/img/fondo.jpg); background-repeat: no-repeat; left bottom;}");
        buf.append("#footer { font-size: 70%; font-style: italic;  padding:0em;position: running(footer); top: 0; right:0;background-image: url('"
                + "" + "')}");
        buf.append("#header { font-size: 80%; font-style: italic;  margin:0px;padding:0em;position: running(page-header); top: 0; right:0;}");
        buf.append("#pagenumber:before { content: counter(page); } ");
        buf.append("#pagecount:before { content: counter(pages); } ");
        buf.append("</style></head>");
        buf.append("<body>");

        buf.append("<table width=\"100%\"  border=\"0\" cellspacing=\"0\"  cellpadding=\"3\">");
        buf.append("<tr>");
        buf.append("<td align='left'>");
        buf.append("<p>");
        buf.append("111111");
        buf.append("</p>");
        buf.append("</td>");
        buf.append("<td align='right'>");
        buf.append("<span style='font-size:10pt !important; font-style: \"italic\"; float:right'>");
        buf.append("TR: ");
        buf.append("CJ");
        buf.append("</span>");
        buf.append("</td>");
        buf.append("</tr>");
        buf.append("<tr>");
        buf.append("<td align='left'>");
        buf.append("<p>");
        buf.append("2014");
        buf.append("</p>");
        buf.append("</td>");
        buf.append("<td></td>");
        buf.append("</tr>");
        buf.append("</table>");

        buf.append("<p>&nbsp;</p>\n"
                + "<table border=\"0\" cellspacing=\"0\"  cellpadding=\"3\">\n"
                + "<tr>\n"
                + "<td><strong> <span>PARA:                <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></span> </strong></td>\n"
                + "<td>\n"
                + "<p><span>Dr. Gustavo Jalkh               <span>(respetar orden jer&aacute;rquico seg&uacute;n               organigrama)</span></span></p>\n"
                + "</td>\n"
                + "</tr>\n"
                + "<tr>\n"
                + "<td>&nbsp;</td>\n"
                + "<td>\n"
                + "<p><strong> <span>PRESIDENTE</span> </strong></p>\n"
                + "</td>\n"
                + "</tr>\n"
                + "</table>\n"
                + "<table border=\"0\" cellspacing=\"0\" cellpadding=\"3\">\n"
                + "<tr>\n"
                + "<td>\n"
                + "<p><strong> <span>DE:                  <span>&nbsp;&nbsp;&nbsp;</span> <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></span> </strong></p>\n"
                + "</td>\n"
                + "<td><span> Karina Paola Logro&ntilde;o Santill&aacute;n</span></td>\n"
                + "</tr>\n"
                + "<tr>\n"
                + "<td>&nbsp;</td>\n"
                + "<td>\n"
                + "<p><strong> <span>Secretario</span> </strong></p>\n"
                + "</td>\n"
                + "</tr>\n"
                + "</table>\n"
                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
                + "<table border=\"0\" cellspacing=\"0\" cellpadding=\"5\">\n"
                + "<tbody>\n"
                + "<tr>\n"
                + "<td><strong> <span>ASUNTO:               <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></span> </strong></td>\n"
                + "<td><span>PRUEBA 3</span></td>\n"
                + "<td>&nbsp;</td>\n"
                + "</tr>\n"
                + "</tbody>\n"
                + "</table>\n"
                + "<p>&nbsp;&nbsp;<strong>&nbsp;</strong><span>&nbsp;</span></p>\n"
                + "<p><strong> <span>Se expone el tema directamente, conciso y         claro.</span> </strong></p>\n"
                + "<p><span>&nbsp;</span></p>\n"
                + "<p><span>Solicito que todas las unidades utilicen las siglas de       identificaci&oacute;n aprobadas, a partir del d&iacute;a lunes 13       de mayo de 2013, con car&aacute;cter obligatorio en los       memorandos y oficios, de las unidades administrativas del       Consejo de la Judicatura.</span></p>\n"
                + "<p><span>&nbsp;</span></p>\n"
                + "<p><span>&nbsp;</span></p>\n"
                + "<p><span>Atentamente,</span></p>\n"
                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
                + "<table border=\"0\" cellspacing=\"0\" cellpadding=\"5\">\n"
                + "<tbody>\n"
                + "<tr>\n"
                + "<td><span> Karina Paola Logro&ntilde;o Santill&aacute;n</span></td>\n"
                + "</tr>\n"
                + "<tr>\n"
                + "<td><strong> <span>Secretario</span> </strong></td>\n"
                + "</tr>\n"
                + "</tbody>\n"
                + "</table>\n"
                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
                + "<p><strong> <span>&nbsp;</span> </strong></p>\n"
                + "<p><strong> <span>C.C.:</span> </strong> <strong> <span>Si se requiere que m&aacute;s unidades reciban el         documento detallar el nombre y cargo</span> </strong></p>");

        buf.append("</body>");
        buf.append("</html>");

        ByteArrayInputStream bais = new ByteArrayInputStream(buf.toString()
                .getBytes());
        try {
            OutputStream os = new FileOutputStream("/tmp/examplePie5.pdf");
            pu.createPDF(bais, os);
        } catch (DocumentException ex) {
            Logger.getLogger(GeneradorPdf.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }

}
