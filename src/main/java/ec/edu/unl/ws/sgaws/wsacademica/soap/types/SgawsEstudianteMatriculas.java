
package ec.edu.unl.ws.sgaws.wsacademica.soap.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id_carrera" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cedula" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "idCarrera",
    "cedula"
})
@XmlRootElement(name = "sgaws_estudiante_matriculas")
public class SgawsEstudianteMatriculas {

    @XmlElement(name = "id_carrera", required = true)
    protected String idCarrera;
    @XmlElement(required = true)
    protected String cedula;

    /**
     * Gets the value of the idCarrera property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdCarrera() {
        return idCarrera;
    }

    /**
     * Sets the value of the idCarrera property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdCarrera(String value) {
        this.idCarrera = value;
    }

    /**
     * Gets the value of the cedula property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCedula() {
        return cedula;
    }

    /**
     * Sets the value of the cedula property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCedula(String value) {
        this.cedula = value;
    }

}
