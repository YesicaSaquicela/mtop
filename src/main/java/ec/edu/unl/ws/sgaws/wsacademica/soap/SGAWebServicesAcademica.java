
/*
 * 
 */

package ec.edu.unl.ws.sgaws.wsacademica.soap;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.2.5
 * Fri Apr 26 10:59:26 ECT 2013
 * Generated source version: 2.2.5
 * 
 */


@WebServiceClient(name = "SGAWebServicesAcademica", 
                  wsdlLocation = "file:/home/cesar/wsld/personal.wsdl",
                  targetNamespace = "http://ws.unl.edu.ec/sgaws/wsacademica/soap/") 
public class SGAWebServicesAcademica extends Service {

    public final static URL WSDL_LOCATION;
    public final static QName SERVICE = new QName("http://ws.unl.edu.ec/sgaws/wsacademica/soap/", "SGAWebServicesAcademica");
    public final static QName SGAWebServicesAcademicaPortType = new QName("http://ws.unl.edu.ec/sgaws/wsacademica/soap/", "SGAWebServicesAcademica_PortType");
    static {
        URL url = null;
        try {
            url = new URL("file:/home/cesar/wsld/personal.wsdl");
        } catch (MalformedURLException e) {
            System.err.println("Can not initialize the default wsdl from file:/home/henry/Documentos/CODIGOS/jbossws-native-bin-dist/deploy/bin/wsdl/api.wsdl");
            // e.printStackTrace();
        }
        WSDL_LOCATION = url;
    }

    public SGAWebServicesAcademica(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public SGAWebServicesAcademica(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SGAWebServicesAcademica() {
        super(WSDL_LOCATION, SERVICE);
    }

    /**
     * 
     * @return
     *     returns SGAWebServicesAcademicaPortType
     */
    @WebEndpoint(name = "SGAWebServicesAcademica_PortType")
    public SGAWebServicesAcademicaPortType getSGAWebServicesAcademicaPortType() {
        return super.getPort(SGAWebServicesAcademicaPortType, SGAWebServicesAcademicaPortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SGAWebServicesAcademicaPortType
     */
    @WebEndpoint(name = "SGAWebServicesAcademica_PortType")
    public SGAWebServicesAcademicaPortType getSGAWebServicesAcademicaPortType(WebServiceFeature... features) {
        return super.getPort(SGAWebServicesAcademicaPortType, SGAWebServicesAcademicaPortType.class, features);
    }

}