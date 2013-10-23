package ec.edu.unl.ws.sgaws.wsvalidacion.soap;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * WSDL File for SGAWebServicesValidacion
 *
 * This class was generated by Apache CXF 2.6.3
 * 2013-10-22T10:20:24.207-05:00
 * Generated source version: 2.6.3
 * 
 */
@WebServiceClient(name = "SGAWebServicesValidacion", 
                  wsdlLocation = "file:/home/cesar/wsdls/validacion.wsdl",
                  targetNamespace = "http://ws.unl.edu.ec/sgaws/wsvalidacion/soap/") 
public class SGAWebServicesValidacion extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://ws.unl.edu.ec/sgaws/wsvalidacion/soap/", "SGAWebServicesValidacion");
    public final static QName SGAWebServicesValidacionPortType = new QName("http://ws.unl.edu.ec/sgaws/wsvalidacion/soap/", "SGAWebServicesValidacion_PortType");
    static {
        URL url = null;
        try {
            url = new URL("file:/home/cesar/wsdls/validacion.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(SGAWebServicesValidacion.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:/home/cesar/wsdls/validacion.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public SGAWebServicesValidacion(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public SGAWebServicesValidacion(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SGAWebServicesValidacion() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public SGAWebServicesValidacion(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public SGAWebServicesValidacion(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public SGAWebServicesValidacion(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns SGAWebServicesValidacionPortType
     */
    @WebEndpoint(name = "SGAWebServicesValidacion_PortType")
    public SGAWebServicesValidacionPortType getSGAWebServicesValidacionPortType() {
        return super.getPort(SGAWebServicesValidacionPortType, SGAWebServicesValidacionPortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SGAWebServicesValidacionPortType
     */
    @WebEndpoint(name = "SGAWebServicesValidacion_PortType")
    public SGAWebServicesValidacionPortType getSGAWebServicesValidacionPortType(WebServiceFeature... features) {
        return super.getPort(SGAWebServicesValidacionPortType, SGAWebServicesValidacionPortType.class, features);
    }

}
