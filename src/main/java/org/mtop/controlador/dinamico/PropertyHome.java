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
    @Inject
    private ServicioGenerico servgen;
    @Pattern(regexp = "[0-9]+", message = "Error: solo puede ingresar números")
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
            propertyType = getInstance().getType();
            if (propertyType.equals("java.util.Date")) {

                Date fecha = Date.class.cast(getInstance().getValue());
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                //String fecha=sdf.format(getInstance().getValue());
                //String fecha = sdf.format(getInstance().getValue());
                System.out.println(sdf.format(fecha));
                //setPropertyStringValue(fecha);

                setPropertyDateValue(fecha);
            }
        }
        if (getInstance().getValue() != null) {
            propertyStringValue = getInstance().getValue().toString();

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
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        String fecha = sdf.format(propertyDateValue);
        System.out.println(fecha);
        setPropertyStringValue(fecha);
        this.propertyDateValue = propertyDateValue;
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
        if (p.getGroupName() != null) {
            if (p.getGroupName().equals("org.mtop.modelo.Vehiculo")) {
                //  tipos.add("org.mtop.modelo.EstadoParteMecanica");
                tipos.add("EstadoParteMecanica");
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

    public String getPropertyType() {
        if (getInstance() != null) {
            if (getInstance().getType().equals("java.lang.MultiLineString")) {
                propertyType = "AreaTexto";
            } else {
                if (getInstance().getType().equals("java.lang.String[]")) {
                    propertyType = "Lista";
                } else {
                    if (getInstance().getType().equals("java.lang.Boolean")) {
                        propertyType = "Booleano";
                    } else {
                        if (getInstance().getType().equals("java.lang.Long")) {
                            propertyType = "EnteroMayor";
                        } else {
                            if (getInstance().getType().equals("java.lang.Integer")) {
                                propertyType = "Entero";
                            } else {
                                if (getInstance().getType().equals("java.lang.Double")) {
                                    propertyType = "Real";
                                } else {
                                    if (getInstance().getType().equals("java.lang.String")) {
                                        propertyType = "Texto";
                                    } else {
                                        if (getInstance().getType().equals("org.mtop.modelo.EstadoParteMecanica")) {
                                            propertyType = "EstadoParteMecanica";
                                        } else {
                                            if (getInstance().getType().equals("java.util.Date")) {
                                                propertyType = "Fecha";
                                            } else {
                                                propertyType = "EstadoParteMecanica";
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
                if (this.propertyType.equals("Texto")
                        || this.propertyType.equals("Lista")
                        || this.propertyType.equals("AreaTexto")
                        || this.propertyType.equals("java.lang.Object")
                        || this.propertyType.equals("Booleano")) {

                    setPropertyStringValue(" ");
                } else {
                    if (this.propertyType.equals("Real")
                            || this.propertyType.equals("EnteroMayor")
                            || this.propertyType.equals("Entero")) {
                        setPropertyStringValue("0");
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

    @TransactionAttribute
    public String saveProperty() {

        getInstance().setType(propertyType);
        System.out.println("TIpo::" + getInstance().getType());
        ConvertirStringPropiedades();
        System.out.println("tipo de la propiedaaaaaaaad>>>>>>>>>> " + getInstance().getType());
        System.out.println("valor de la propiedad>>>>>" + this.propertyStringValue);
        log.info("eqaula --> saving " + getInstance().getName());

        if (getInstance().getType().equals("java.util.Date")) {

            getInstance().setValue(Calendar.getInstance().getTime());
        }
        // if (getInstance().getType().equals("java.lang.Long") || getInstance().getType().equals("java.lang.Integer")) {
        if (getInstance().getType().equals("java.lang.Long")) {
            propertyStringValue = this.propertyStringValue.substring(0, (propertyStringValue.length() - 2));
        }
        if (getInstance().getType().equals("java.lang.Integer")) {
            propertyStringValue = this.propertyNumberValue.toString();
        }
        if (getInstance().isPersistent()) {
            System.out.println("PRESENTAR GUERADAR>>>>>");
            getInstance().setValue(converterToType(propertyStringValue));
            System.out.println("propiedad convertida +++++" + getInstance().getValue());
            save(getInstance());

        } else {
            try {
                System.out.println("PRESENTAR Editaaarrr>>>>>");
                Structure s = bussinesEntityTypeService.getStructure(getStructureId()); //Retornar la estrucura.
                getInstance().setValue(converterToType(propertyStringValue));
                System.out.println("propiedad convertida +++++" + getInstance().getValue());
                s.addProperty(this.getInstance());

            } catch (Exception ex) {
                System.out.println("ERROR>>>" + ex);
                //log.info("eqaula --> error saving new" + getInstance().getName());
            }
        }

        //crear un entity type atribute para una propiedad
        // que sea de tipo estadoParteMecanica
        if (getInstance().getType().equals("org.mtop.modelo.EstadoParteMecanica")) {
            BussinesEntityAttribute beta = new BussinesEntityAttribute();
            for (Property p : findAll(Property.class)) {
                if (p.getName().equals(this.getInstance().getName())) {
                    beta.setProperty(p);
                    beta.setType(p.getType());
                    beta.setName(p.getLabel());
                    beta.setValue("");
                    //save(beta);
                    break;
                }
            }
            for (Vehiculo v : findAll(Vehiculo.class)) {
                beta.setBussinesEntity(findById(BussinesEntity.class, v.getId()));
                save(beta);
            }
        }

        return "/paginas/admin/bussinesentitytype/bussinesentitytype?faces-redirect=true&bussinesEntityTypeId=" + getBussinesEntityTypeId();
    }

    @Transactional
    public String deleteProperty(Property p) {
        setStructureId(p.getStructure().getId());
        setBussinesEntityTypeId(bussinesEntityService.findBussinesEntityTypeByName(p.getStructure().getName()).getId());
        setInstance(p);
        return deleteProperty();
    }

    @Transactional
    public String deleteProperty() {
        System.out.println("entro elimianrrrr");

        System.out.println("entro e eliminar" + getInstance().getName());
        String outcome = null;
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
                    outcome = "/paginas/admin/bussinesentitytype/bussinesentitytype?faces-redirect=true&bussinesEntityTypeId=" + getBussinesEntityTypeId();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, UI.getMessages("common.property.hasValues") + " " + getInstance().getName(), ""));
                    RequestContext.getCurrentInstance().execute("deletedDlg.hide()"); //cerrar el popup si se grabo correctamente
                    outcome = "/paginas/admin/bussinesentitytype/property?faces-redirect=true&bussinesEntityTypeId=" + getBussinesEntityTypeId() + "&propertyId=" + getPropertyId();
                }

            } else {
                //remover de la lista, si aún no esta persistido
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe agregar una propiedad para ser borrada", ""));
            }

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRORE", e.toString()));
        }
        return outcome;
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
        try {
            if ("java.lang.String".equals(getInstance().getType()) || "java.lang.String[]".equals(getInstance().getType()) || "org.mtop.modelo.EstadoParteMecanica".equals(getInstance().getType()) || "java.lang.MultiLineString".equals(getInstance().getType())) {
                o = value;
            } else if ("java.lang.Long".equals(getInstance().getType())) {
                o = Long.valueOf(value);
            } else if ("java.lang.Float".equals(getInstance().getType())) {
                o = Float.valueOf(value);
            } else if ("java.lang.Integer".equals(getInstance().getType())) {
                o = Integer.valueOf(value);
            } else if ("java.lang.Boolena".equals(getInstance().getType())) {
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
        System.out.println("valor de la propuiedad a convertir" + o);
        return (Serializable) o;
    }

    public boolean hasValuesBussinesEntity() {
        boolean ban = bussinesEntityService.findBussinesEntityForProperty(getInstance()).isEmpty() && bussinesEntityService.findBussinesEntityAttributeForProperty(getInstance()).isEmpty();
        //log.info("eqaula --> property tiene valores : " + ban);
        System.out.println("borrando " + getInstance().getName());
        return ban;
    }

    public boolean hasValuesBussinesEntity(Property p) {

        boolean ban = bussinesEntityService.findBussinesEntityForProperty(p).isEmpty() && bussinesEntityService.findBussinesEntityAttributeForProperty(p).isEmpty();
        //log.info("eqaula --> property tiene valores : " + ban);
        System.out.println("fijando p.name a intance" + p.getName());
        System.out.println("valor de bandera" + ban);
        if (ban == false) {
            setStructureId(p.getStructure().getId());
            setBussinesEntityTypeId(bussinesEntityService.findBussinesEntityTypeByName(p.getStructure().getName()).getId());
            setInstance(p);
            mensaje = "Esta propiedad tiene registros de una Entidad de Negocio";
            System.out.println("retornando la evaluciaon");
            return ban;
        } else {
            if (p.getType().equals("org.mtop.modelo.dinamico.Structure")) {
                BussinesEntityType bet = bussinesEntityService.findBussinesEntityTypeByName(p.getName());
                System.out.println("\n\n\nbet" + bet);
                if (bet != null) {
                    Structure s = p.getStructure();
                    for (Structure object : bet.getStructures()) {
                        System.out.println("nombre de estructura" + object.getName());
                        System.out.println("nombre de la propiedad" + p.getName());
                        if (object.getName().equals(p.getName())) {
                            s = object;
                        }
                    }

                    System.out.println("valor de la structura" + s.getName());
                    if (!s.getProperties().isEmpty()) {
                        System.out.println("presento propiedades");
                        boolean b = false;
                        for (Property p1 : s.getProperties()) {
                            if (bussinesEntityService.findBussinesEntityForProperty(p1).isEmpty() && bussinesEntityService.findBussinesEntityAttributeForProperty(p1).isEmpty()) {
                                b = true;
                                System.out.println("encontro entidad llenba" + p1);
                                break;
                            }

                        }
                        if (b) {
                            setStructureId(p.getStructure().getId());
                            setBussinesEntityTypeId(bussinesEntityService.findBussinesEntityTypeByName(p.getStructure().getName()).getId());
                            setInstance(p);
                            System.out.println("Esta propiedad no es una Entidad con propiedades \n que tiene registros de una Entidad de Negocio");
                            return b;
                        } else {
                            System.out.println("retornando false de nooo");
                            mensaje = "Esta propiedad tiene registros de una Entidad de Negocio";

                            return b;
                        }
                    } else {
                        System.out.println("no tiene propiedades");
                        return true;
                    }
                } else {

                    return true;
                }

            } else {
                return ban;
            }

        }
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
        return findAll(Property.class);
    }
}
