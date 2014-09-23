/*
 * Copyright 2012 cesar.
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
package org.mtop.controlador.dinamico;

import org.mtop.modelo.dinamico.BussinesEntity;
import org.mtop.modelo.dinamico.Structure;
import org.mtop.modelo.dinamico.BussinesEntityAttribute;
import org.mtop.modelo.dinamico.Property;
import org.mtop.controlador.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.apache.commons.lang.SerializationUtils;
import org.mtop.cdi.Web;
import org.mtop.service.BussinesEntityService;
import org.mtop.service.BussinesEntityTypeService;
import org.mtop.util.UI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.TypedQuery;
import javax.swing.text.html.HTML;
import javax.validation.constraints.Pattern;
import org.jboss.seam.transaction.Transactional;
import org.mtop.modelo.profile.Profile;
import org.mtop.modelo.Vehiculo;
import org.mtop.modelo.dinamico.BussinesEntityType;
import org.mtop.servicios.ServicioGenerico;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author
 */
@Named
@ViewScoped
public class PropertyHome extends BussinesEntityHome<Property> implements Serializable {

    private static final long serialVersionUID = 7632987414391869389L;
    private static org.jboss.solder.logging.Logger log = org.jboss.solder.logging.Logger.getLogger(PropertyHome.class);
    @Inject
    @Web
    private EntityManager em;
    @Inject
    private BussinesEntityService bussinesEntityService;
    @Inject
    private BussinesEntityTypeService bussinesEntityTypeService;
    private Long structureId;
    private Long bussinesEntityTypeId;
    private String propertyStringValue;
    private Date propertyDateValue;
    private String propertyType;
    private List<Property> propiedades;

    @Inject
    private ServicioGenerico servgen;
    @Pattern(regexp = "[0-9]*", message = "Error: solo puede ingresar números")
    private String propertyNumberValue;
    private String mensaje;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public PropertyHome() {
        log.info("mtop --> Inicializo Property Home");
    }

    public Long getPropertyId() {
        return (Long) getId();
    }

    public String getPropertyNumberValue() {
        return propertyNumberValue;
    }

    public void setPropertyNumberValue(String propertyNumberValue) {
        this.propertyNumberValue = propertyNumberValue;
    }

    public void setPropertyId(Long propertyId) throws ParseException {
        System.out.println("fijando" + propertyId);
        setId(propertyId);
        if (getInstance().getType() != null) {
            propertyType = convertirPropiedadPresentar(getInstance().getType());
            System.out.println("lfijo" + propertyType);
//            if (propertyType.equals("java.util.Date")) {
//
//                Date fecha = Date.class.cast(getInstance().getValue());
//                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
//                //String fecha=sdf.format(getInstance().getValue());
//                //String fecha = sdf.format(getInstance().getValue());
//                System.out.println(sdf.format(fecha));
//                //setPropertyStringValue(fecha);
//
//                setPropertyDateValue(fecha);
//            }
        }
        if (getInstance().getValue() != null) {
            if (getInstance().getType().equals("java.lang.Integer")) {
                propertyNumberValue = getInstance().getValue().toString();
            } else {
                if (getInstance().getType().equals("java.util.Date")) {
                    System.out.println("hacer casting fecha");
                    propertyDateValue = Date.class.cast(getInstance().getValue());
                    System.out.println("paso hacer casting fecha");
                } else {
                    if (getInstance().getType().equals("java.lang.String[]")) {

                        propertyStringValue = getInstance().getValue().toString().substring(0, getInstance().getValue().toString().length() - 1);

                    } else {
                        propertyStringValue = getInstance().getValue().toString();
                    }

                }

            }

        }

    }

    public Long getStructureId() {
        return structureId;
    }

    public void setStructureId(Long structureId) {

        this.structureId = structureId;
    }

    public Long getBussinesEntityTypeId() {

        return bussinesEntityTypeId;
    }

    public void setBussinesEntityTypeId(Long bussinesEntityTypeId) {
        System.out.println("fijando id de entidad >>>>>>>>>>>>>>>>>>>>>>" + bussinesEntityTypeId);
        this.bussinesEntityTypeId = bussinesEntityTypeId;
    }

    public Date getPropertyDateValue() {
        return propertyDateValue;
    }

    public void setPropertyDateValue(Date propertyDateValue) {
        System.out.println("entro a fijar fecja" + propertyDateValue);
        if (propertyDateValue == null) {
            System.out.println("fijo nulll a fecha");
            this.propertyDateValue = null;
        } else {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            String fecha = sdf.format(propertyDateValue);
            System.out.println(fecha);
            setPropertyStringValue(fecha);
            this.propertyDateValue = propertyDateValue;

        }
        System.out.println("fijo fecha" + this.propertyDateValue);

    }

    public String getPropertyStringValue() {

        return propertyStringValue;
    }

    public void setPropertyStringValue(String propertyStringValue) {
        System.out.println("kkkkkkkk" + propertyStringValue);
        this.propertyStringValue = propertyStringValue;
        /// getInstance().setValue(this.propertyStringValue);
    }

    @Override
    protected Property createInstance() {
        Property property = new Property();
        return property;
    }

    @TransactionAttribute
    public void load() {
        log.info("mtop --> Loading instance from Property for " + getId());
        if (isIdDefined()) {
            wire();
        }
        log.info("mtop --> Loaded instance " + getInstance());
    }

    @TransactionAttribute
    public void wire() {
        getInstance();
    }

    public boolean isWired() {
        return true;
    }

    public Property getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

    @PostConstruct
    public void init() {
        setEntityManager(em);
        System.out.println("volvio a ejecutar init");
        bussinesEntityTypeService.setEntityManager(em);
        bussinesEntityService.setEntityManager(em);
        mensaje = "mensaje";
    }

    @Override
    public Class<Property> getEntityClass() {
        return Property.class;
    }

    public List<String> getTiposDatos() {
        List<String> tipos = new ArrayList<String>();
        // tipos.add("org.mtop.modelo.dinamico.Structure");
        tipos.add("Estructura");
        // tipos.add("org.mtop.modelo.dinamico.Group");

        List<Property> l = findAllPropiedades();
        Structure structura = bussinesEntityTypeService.getStructure(structureId);
        Property p = new Property();
        for (int i = 1; i < l.size(); i++) {
            if (structura.getName().equals(l.get(i).getName())) {
                p = l.get(i);
            }

        }
        System.out.println("p.name" + p.getName());
        System.out.println("p.getGroupName " + p.getGroupName());
        if (p.getGroupName() != null) {
            System.out.println("get group name" + p.getGroupName());
            if (p.getGroupName().equals("org.mtop.modelo.Vehiculo")) {
                //  tipos.add("org.mtop.modelo.EstadoParteMecanica");
                tipos.add("EstadoParteMecanica");
                System.out.println("si entro a grupo");
                // setPropertyStringValue("Bueno,Malo");
            }

        }

//        tipos.add("java.util.Date");
//        tipos.add("java.lang.String");
//        tipos.add("java.lang.Double");
//        tipos.add("java.lang.Integer");
//        tipos.add("java.lang.Long");
//        tipos.add("java.lang.Boolean");
//        tipos.add("java.lang.String[]");
//        tipos.add("java.lang.MultiLineString");
//        tipos.add("java.lang.Object");
        tipos.add("Fecha");
        tipos.add("Texto");
        tipos.add("Real");
        tipos.add("Entero");
        tipos.add("EnteroMayor");
        tipos.add("Booleano");
        tipos.add("Lista");
        tipos.add("AreaTexto");
        //  tipos.add("java.lang.Object");
        return tipos;
    }

    public void ConvertirStringPropiedades() {
        if (getInstance().getType().equals("AreaTexto")) {
            getInstance().setType("java.lang.MultiLineString");
        } else {
            if (getInstance().getType().equals("Lista")) {
                getInstance().setType("java.lang.String[]");
            } else {
                if (getInstance().getType().equals("Booleano")) {
                    getInstance().setType("java.lang.Boolean");
                } else {
                    if (getInstance().getType().equals("EnteroMayor")) {
                        getInstance().setType("java.lang.Long");
                    } else {
                        if (getInstance().getType().equals("Entero")) {
                            getInstance().setType("java.lang.Integer");
                        } else {
                            if (getInstance().getType().equals("Real")) {
                                getInstance().setType("java.lang.Double");
                            } else {
                                if (getInstance().getType().equals("Texto")) {
                                    getInstance().setType("java.lang.String");
                                } else {
                                    if (getInstance().getType().equals("EstadoParteMecanica")) {
                                        getInstance().setType("org.mtop.modelo.EstadoParteMecanica");
                                    } else {
                                        if (getInstance().getType().equals("Fecha")) {
                                            getInstance().setType("java.util.Date");
                                        } else {
                                            getInstance().setType("org.mtop.modelo.dinamico.Structure");
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public String convertirPropiedadPresentar(String nombre) {
        if (nombre.equals("java.lang.MultiLineString")) {
            return "AreaTexto";
        } else {
            if (nombre.equals("java.lang.String[]")) {
                return "Lista";
            } else {
                if (nombre.equals("java.lang.Boolean")) {
                    return "Booleano";
                } else {
                    if (nombre.equals("java.lang.Long")) {
                        return "EnteroMayor";
                    } else {
                        if (nombre.equals("java.lang.Integer")) {
                            return "Entero";
                        } else {
                            if (nombre.equals("java.lang.Double")) {
                                return "Real";
                            } else {
                                if (nombre.equals("java.lang.String")) {
                                    return "Texto";
                                } else {
                                    if (nombre.equals("org.mtop.modelo.EstadoParteMecanica")) {
                                        return "EstadoParteMecanica";
                                    } else {
                                        if (nombre.equals("java.util.Date")) {
                                            System.out.println("fue fecha" + propertyDateValue);
                                            return "Fecha";
                                        } else {
                                            return "Estructura";
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public String getPropertyType() {
        System.out.println("get instance" + getInstance());
        System.out.println("property typw" + propertyType);
        if (getInstance().getId() != null) {
            System.out.println("tiene valor " + hasValuesBussinesEntity());
            if (!hasValuesBussinesEntity()) {
                System.out.println("entro");
                System.out.println("tipo" + getInstance().getType());
                propertyType = convertirPropiedadPresentar(getInstance().getType());

            }
        } else {
            if (propertyType != null) {
                if (propertyType.equals("Booleano")) {
                    propertyStringValue = "true";
                } else {
                    propertyStringValue = "";
                }
            }

        }

        System.out.println("tipo a retornar " + propertyType);
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
        if (this.propertyType != null) {

            System.out.println("Entor a tipo>>>>>>" + this.propertyType);
            if (this.propertyType.equals("EstadoParteMecanica")) {
                setPropertyStringValue("Bueno,Malo*");
            } else {

                if (this.propertyType.equals("Fecha")) {
                    System.out.println("entro a fecha");
                    Date date = Calendar.getInstance().getTime();
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                    String fecha = sdf.format(date);
                    System.out.println(fecha);
                    setPropertyStringValue(fecha);
                    setPropertyDateValue(date);
                } else {
                    if (this.propertyType.equals("Estructura")) {
                        System.out.println("entro a estrusctura");

                    }
                }

            }

        }
        System.out.println("valor de tipo fijado " + propertyType);

    }

    public String ConvertirPropiedadesString(Property p) {
        if (p.getType().equals("java.lang.MultiLineString")) {
            return "AreaTexto";
        } else {
            if (p.getType().equals("java.lang.String[]")) {
                return "Lista";
            } else {
                if (p.getType().equals("java.lang.Boolean")) {
                    return "Booleano";
                } else {
                    if (p.getType().equals("java.lang.Long")) {
                        return "EnteroMayor";
                    } else {
                        if (p.getType().equals("java.lang.Integer")) {
                            return "Entero";
                        } else {
                            if (p.getType().equals("java.lang.Double")) {
                                return "Real";
                            } else {
                                if (p.getType().equals("java.lang.String")) {
                                    return "Texto";
                                } else {
                                    if (p.getType().equals("org.mtop.modelo.EstadoParteMecanica")) {
                                        return "EstadoParteMecanica";
                                    } else {
                                        if (p.getType().equals("java.util.Date")) {
                                            return "Fecha";
                                        } else {
                                            return "Estructura";
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public String formato(Date fecha) {
        System.out.println("\n\n\n\n\n\n\nhsadhjsdj" + fecha);
        String fechaFormato = "";
        if (fecha != null) {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            fechaFormato = formatter.format(fecha);
        }

        return fechaFormato;

    }

    @TransactionAttribute
    public String saveProperty() {
        getInstance().setType(propertyType);
        ConvertirStringPropiedades();

        log.info("eqaula --> saving " + getInstance().getName());
        //todo valor se asigna a property string value para poder convertir
        if (getInstance().getType().equals("java.util.Date")) {
            if (propertyDateValue != null) {
                DateFormat fechaHora = new SimpleDateFormat("dd/MM/yyyy");
                propertyStringValue = fechaHora.format(propertyDateValue);
                System.out.println("convertidooo" + propertyStringValue);
            } else {
                propertyStringValue = "";
            }

        }
        if (getInstance().getType().equals("java.lang.String[]")) {
            propertyStringValue = propertyStringValue + "*";
        }
        // if (getInstance().getType().equals("java.lang.Long") || getInstance().getType().equals("java.lang.Integer")) {
        if (getInstance().getType().equals("java.lang.Integer")) {
            propertyStringValue = this.propertyNumberValue.toString();
        }

        if (getInstance().isPersistent()) {
            //convierte en valor a serializable para poderlo guardar a la bases de datos
            getInstance().setValue(converterToType(propertyStringValue));
            save(getInstance());
        } else {
            try {

                Structure s = bussinesEntityTypeService.getStructure(getStructureId()); //Retornar la estrucura.
                //convierte en valor a serializable para poderlo guardar a la bases de datos
                getInstance().setValue(converterToType(propertyStringValue));
                s.addProperty(this.getInstance());

            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("ERROR>>>" + ex);
                //log.info("eqaula --> error saving new" + getInstance().getName());
            }
        }

        //crear un entity type atribute para una propiedad
        // que sea de tipo estadoParteMecanica
//        if (getInstance().getType().equals("org.mtop.modelo.EstadoParteMecanica")) {
//            BussinesEntityAttribute beta = new BussinesEntityAttribute();
//            for (Property p : findAll(Property.class)) {
//                if (p.getName().equals(this.getInstance().getName())) {
//                    beta.setProperty(p);
//                    beta.setType(p.getType());
//                    beta.setName(p.getLabel());
//                    beta.setValue("");
//                    //save(beta);
//                    break;
//                }
//            }
//            for (Vehiculo v : findAll(Vehiculo.class)) {
//                beta.setBussinesEntity(findById(BussinesEntity.class, v.getId()));
//                save(beta);
//            }
//        }
        return "/paginas/admin/bussinesentitytype/bussinesentitytype?faces-redirect=true&bussinesEntityTypeId=" + getBussinesEntityTypeId();
    }

    @Transactional
    public void deleteProperty(Property p) {
        setStructureId(p.getStructure().getId());
        setBussinesEntityTypeId(bussinesEntityService.findBussinesEntityTypeByName(p.getStructure().getName()).getId());
        setInstance(p);
        bussinesEntityTypeService.find(getBussinesEntityTypeId());
        System.out.println("encontro estrictura" + bussinesEntityTypeService.find(getBussinesEntityTypeId()).getStructures());
        for (Structure estr : bussinesEntityTypeService.find(getBussinesEntityTypeId()).getStructures()) {
            System.out.println("fijo propiedades" + estr.getProperties());
            propiedades = estr.getProperties();
            break;
        }
        propiedades.remove(p);
        System.out.println("fijo propiedades" + propiedades);

    }

    @Transactional
    public void deleteProperty() {
        // String outcome = null;
        try {
            if (getInstance() == null) {
                throw new NullPointerException("property is null");
            }

            if (getInstance().isPersistent()) {
                if (hasValuesBussinesEntity()) {
                    Structure s = bussinesEntityTypeService.getStructure(getStructureId());
                    boolean mensaje = s.removeProperty(getInstance());
                    delete(getInstance());
                    save(s);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Se borró exitosamente:  " + getInstance().getName(), ""));
                    RequestContext.getCurrentInstance().execute("deletedDlg.hide()"); //cerrar el popup si se grabo correctamente
                    //  outcome = "/paginas/admin/bussinesentitytype/bussinesentitytype?faces-redirect=true&bussinesEntityTypeId=" + getBussinesEntityTypeId();

                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, UI.getMessages("common.property.hasValues") + " " + getInstance().getName(), ""));
                    RequestContext.getCurrentInstance().execute("deletedDlg.hide()"); //cerrar el popup si se grabo correctamente
                    // outcome = "/paginas/admin/bussinesentitytype/property?faces-redirect=true&bussinesEntityTypeId=" + getBussinesEntityTypeId() + "&propertyId=" + getPropertyId();
                }

            } else {
                //remover de la lista, si aún no esta persistido
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe agregar una propiedad para ser borrada", ""));
            }

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRORE", e.toString()));
        }
    }

    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage(UI.getMessages("module.Property") + " " + UI.getMessages("common.selected"), ((Property) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage("", msg);
    }

    public void onRowUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage(UI.getMessages("module.Property") + " " + UI.getMessages("common.unselected"), ((Property) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage("", msg);
        this.setBussinesEntity(null);
    }

    public Serializable converterToType(String value) {
        Object o = new Object();
        if (value != null) {
            value = value.trim();
        } else {
            value = "";
        }

        System.out.println("value en integer" + value);
        if (value.equals("")) {
            System.out.println("entro a vacio");
            o = null;
        } else {
            try {
                if ("java.lang.String".equals(getInstance().getType()) || "java.lang.String[]".equals(getInstance().getType()) || "org.mtop.modelo.EstadoParteMecanica".equals(getInstance().getType()) || "java.lang.MultiLineString".equals(getInstance().getType())) {
                    o = value;
                } else if ("java.lang.Long".equals(getInstance().getType())) {
                    o = Long.valueOf(value);
                } else if ("java.lang.Float".equals(getInstance().getType())) {
                    o = Float.valueOf(value);
                } else if ("java.lang.Integer".equals(getInstance().getType())) {

                    o = (Object) value;
                } else if ("java.lang.Boolean".equals(getInstance().getType())) {
                    o = Boolean.valueOf(value);
                } else if ("java.util.Date".equals(getInstance().getType())) {

                    SimpleDateFormat sdf;
                    sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date fecha = null;
                    try {
                        fecha = sdf.parse(value);
                    } catch (ParseException pe) {
                        log.info("eqaula --> error converter date:" + pe.getMessage());
                    }
                    o = fecha;
                } else if ("java.lang.Double".equals(getInstance().getType())) {
                    o = Double.valueOf(value);
                } else {
                    o = value;
                }
            } catch (Exception e) {
                log.info("eqaula --> error converter: " + value);
            }
        }

        System.out.println(
                "valor de la propuiedad a convertir" + o);
        return (Serializable) o;
    }

    public boolean hasValuesBussinesEntity() {
        if (getInstance().getId() != null) {
            boolean ban = bussinesEntityService.findBussinesEntityForProperty(getInstance()).isEmpty() && bussinesEntityService.findBussinesEntityAttributeForProperty(getInstance()).isEmpty();
            //log.info("eqaula --> property tiene valores : " + ban);

            System.out.println("fijando p.name a intance" + getInstance().getName());
            System.out.println("valor de bandera" + ban);
            if (getInstance().getType().equals("org.mtop.modelo.dinamico.Structure")) {

                BussinesEntityType bet = bussinesEntityService.findBussinesEntityTypeByName(getInstance().getName());
                System.out.println("\n\n\nbet" + bet);
                if (bet != null) {
                    ban = false;
                }

            }
            System.out.println("ban2" + ban);
            return ban;

//            if (getInstance().getType().equals("org.mtop.modelo.dinamico.Structure")) {
//                BussinesEntityType bet = bussinesEntityService.findBussinesEntityTypeByName(getInstance().getName());
//                System.out.println("\n\n\nbet" + bet);
//                if (bet != null) {
//                    Structure s = getInstance().getStructure();
//                    for (Structure object : bet.getStructures()) {
//                        System.out.println("nombre de estructura" + object.getName());
//                        System.out.println("nombre de la propiedad" + getInstance().getName());
//                        if (object.getName().equals(getInstance().getName())) {
//                            s = object;
//                        }
//                    }
//
//                    System.out.println("valor de la structura" + s.getName());
//                    if (s.getProperties().isEmpty()) {
//                        System.out.println("no tiene propiedades");
//                        return true;
//
//                    } else {
//
//                        return false;
//                    }
//                } else {
//
//                    return true;
//                }
//
//            }
        } else {
            return true;
        }

    }

    public boolean hasValuesBussinesEntity(Property p) {

        boolean ban = bussinesEntityService.findBussinesEntityForProperty(p).isEmpty() && bussinesEntityService.findBussinesEntityAttributeForProperty(p).isEmpty();
        //log.info("eqaula --> property tiene valores : " + ban);

        System.out.println("fijando p.name a intance" + p.getName());
        System.out.println("valor de bandera" + ban);
        if (p.getType().equals("org.mtop.modelo.dinamico.Structure")) {

            BussinesEntityType bet = bussinesEntityService.findBussinesEntityTypeByName(p.getName());
            System.out.println("\n\n\nbet" + bet);
            if (bet != null) {
                ban = false;
            }

        }
        System.out.println("ban2" + ban);
        return ban;
//        if (ban == false) {
//            setStructureId(p.getStructure().getId());
//            setBussinesEntityTypeId(bussinesEntityService.findBussinesEntityTypeByName(p.getStructure().getName()).getId());
//            setInstance(p);
//            mensaje = "Esta propiedad tiene registros de una Entidad de Negocio";
//            System.out.println("retornando la evaluciaon");
//            return ban;
//        } else {
//            if (p.getType().equals("org.mtop.modelo.dinamico.Structure")) {
//                BussinesEntityType bet = bussinesEntityService.findBussinesEntityTypeByName(p.getName());
//                System.out.println("\n\n\nbet" + bet);
//                if (bet != null) {
//                    Structure s = p.getStructure();
//                    for (Structure object : bet.getStructures()) {
//                        System.out.println("nombre de estructura" + object.getName());
//                        System.out.println("nombre de la propiedad" + p.getName());
//                        if (object.getName().equals(p.getName())) {
//                            s = object;
//                        }
//                    }
//
//                    System.out.println("valor de la structura" + s.getName());
//                    if (!s.getProperties().isEmpty()) {
//                        System.out.println("presento propiedades");
//                        boolean b = false;
//                        for (Property p1 : s.getProperties()) {
//                            if (bussinesEntityService.findBussinesEntityForProperty(p1).isEmpty() && bussinesEntityService.findBussinesEntityAttributeForProperty(p1).isEmpty()) {
//                                b = true;
//                                System.out.println("encontro entidad llenba" + p1);
//                                break;
//                            }
//
//                        }
//                        if (b) {
//                            setStructureId(p.getStructure().getId());
//                            setBussinesEntityTypeId(bussinesEntityService.findBussinesEntityTypeByName(p.getStructure().getName()).getId());
//                            setInstance(p);
//                            System.out.println("Esta propiedad no es una Entidad con propiedades \n que tiene registros de una Entidad de Negocio");
//                            return b;
//                        } else {
//                            System.out.println("retornando false de nooo");
//                            mensaje = "Esta propiedad tiene registros de una Entidad de Negocio";
//
//                            return b;
//                        }
//                    } else {
//                        System.out.println("no tiene propiedades");
//                        return true;
//                    }
//                } else {
//
//                    return true;
//                }
//
//            } else {
//                return ban;
//            }
//
//        }
    }

    public String cargarValidador() {
        //log.info("ingreso a validador value");
        System.out.println("ingresa cargar validador");
        System.out.println("ingresa cargar validador" + getInstance().getType());
        if ("java.lang.String[]".equals(getInstance().getType().toString())) {
            log.info("ingreso a validador value valueTextPropertyValidator");
            return "valueTextPropertyValidator";
        }
        if ("org.mtop.modelo.EstadoParteMecanica".equals(getInstance().getType().toString())) {
            log.info("ingreso a validador value valueTextPropertyValidator");
            return "valueTextPropertyValidator";
        }
        return "";
    }

    public List<Property> findAllPropiedades() {
        return findAll(Property.class
        );
    }

    public Property obtenerNombre(final String nombre) throws NoResultException {
        System.out.println("obtener nombre");
        TypedQuery<Property> query = em.createQuery("SELECT p FROM Property p WHERE p.name = :nombre", Property.class);
        System.out.println("query" + query);
        query.setParameter("nombre", nombre);
        System.out.println("query.getSingleResult()" + query.getSingleResult());
        Property result = query.getSingleResult();
        System.out.println("resultdo" + result);
        return result;
    }

    public boolean nombreUnico(String nombre) {
        System.out.println("entro al validador" + getInstance().getId());

        List<Property> listaps = findAllPropiedades();

        if (getInstance().getId() == null) {
            System.out.println("entro>>>>>>>>>>");
            try {
                obtenerNombre(nombre);
                return false;
            } catch (NoResultException e) {
                return true;
            }
        } else {
            System.out.println("cual envia a elim" + getInstance().getId());
            System.out.println("lista v antes" + listaps);

            List<Property> lp = new ArrayList<Property>();
            for (Property p : listaps) {
                if (!p.getName().equals(findById(Property.class, getInstance().getId()).getName())) {
                    lp.add(p);
                }
            }
            listaps = lp;
            System.out.println("lista v" + listaps);
            System.out.println("ide instance" + getInstance().getId());
            for (Property v : listaps) {
                System.out.println("entro al for" + v.getId());

                if (v.getName().equals(getInstance().getName())) {
                    System.out.println("entro al if>>>");
                    obtenerNombre(nombre);
                    return false;
                }
            }
            System.out.println("nunca entro al if");
            return true;
        }

    }

    public List<Property> getPropiedades() {
        System.out.println("ontiene propiedades " + propiedades);
        return propiedades;
    }

    public void setPropiedades(List<Property> propiedades) {
        this.propiedades = propiedades;
    }

}
