
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;

import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.ITextUserAgent;
import org.xhtmlrenderer.resource.XMLResource;

import com.lowagie.text.DocumentException;
import java.io.ByteArrayInputStream;
import org.mtop.controlador.ControladorVehiculo;
import org.mtop.modelo.Vehiculo;
import org.w3c.tidy.Tidy;

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

    public static void createPDF(InputStream is, OutputStream os)
            throws IOException, DocumentException {
        try {
            is = new ByteArrayInputStream(getValidHtmlDocument(is));
            ITextRenderer renderer = new ITextRenderer();
            ResourceLoaderUserAgent callback = new ResourceLoaderUserAgent(
                    renderer.getOutputDevice());
            callback.setSharedContext(renderer.getSharedContext());
            renderer.getSharedContext().setUserAgentCallback(callback);

            Document doc = XMLResource.load(is).getDocument();

            renderer.setDocument(doc, null);
            renderer.layout();
            renderer.createPDF(os);

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
        ControladorVehiculo cv = new ControladorVehiculo();
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
        buf.append("<thead>");
        buf.append("<tr>");
        //1
        buf.append("<td align='left'>");
        buf.append("<p>");
        buf.append("Registro");
        buf.append("</p>");
        buf.append("</td>");
        buf.append("<td align='left'>");
        buf.append("<p>");
        buf.append("Clase");
        buf.append("</p>");
        buf.append("</td>");
        //2
        buf.append("<td align='left'>");
        buf.append("<p>");
        buf.append("Marca");
        buf.append("</p>");
        buf.append("</td>");
        //3
        buf.append("<td align='left'>");
        buf.append("<p>");
        buf.append("Modelo");
        buf.append("</p>");
        buf.append("</td>");
        //4
        buf.append("<td align='left'>");
        buf.append("<p>");
        buf.append("Localizacion");
        buf.append("</p>");
        buf.append("</td>");

        buf.append("<td align='right'>");
        buf.append("<span style='font-size:10pt !important; font-style: \"italic\"; float:right'>");
        buf.append("TR: ");
        buf.append("CJ");
        buf.append("</span>");
        buf.append("</td>");
        buf.append("</tr>");
        buf.append("</thead>");
        buf.append("<tbody>");
        for (Vehiculo v : cv.getListaVehiculos()) {
            buf.append("<tr>");
            //1
            buf.append("<td align='left'>");
            buf.append("<p>");
            buf.append(v.getNumRegistro());
            buf.append("</p>");
            buf.append("</td>");
            buf.append("<td align='left'>");
            buf.append("<p>");
            buf.append("Clase");
            buf.append("</p>");
            buf.append("</td>");
            //2
            buf.append("<td align='left'>");
            buf.append("<p>");
            buf.append(v.getMarca());
            buf.append("</p>");
            buf.append("</td>");
            //3
            buf.append("<td align='left'>");
            buf.append("<p>");
            buf.append(v.getModelo());
            buf.append("</p>");
            buf.append("</td>");
            //4
            buf.append("<td align='left'>");
            buf.append("<p>");
            buf.append(cv.obtenerUltimaUbicacionV(v));
            buf.append("</p>");
            buf.append("</td>");

            buf.append("<td align='right'>");
            buf.append("<span style='font-size:10pt !important; font-style: \"italic\"; float:right'>");
            buf.append("TR: ");
            buf.append("CJ");
            buf.append("</span>");
            buf.append("</td>");
            buf.append("</tr>");

        }
        buf.append("</tbody>");
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
