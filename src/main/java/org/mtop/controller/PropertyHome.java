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
package org.mtop.controller;

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
import org.mtop.model.*;
import org.mtop.service.BussinesEntityService;
import org.mtop.service.BussinesEntityTypeService;
import org.mtop.util.UI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jboss.seam.transaction.Transactional;
import org.mtop.model.profile.Profile;
import org.mtop.modelo.Vehiculo;
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

    public PropertyHome() {
        log.info("mtop --> Inicializo Property Home");
    }

    public Long getPropertyId() {
        return (Long) getId();
    }

    public void setPropertyId(Long propertyId) throws ParseException {
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

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
        if (this.propertyType != null) {

            System.out.println("Entor a tipo>>>>>>" + this.propertyType);
            if (this.propertyType.equals("org.mtop.model.EstadoParteMecanica")) {
                setPropertyStringValue("Bueno,Malo*");
            } else {
                if (this.propertyType.equals("java.lang.String")
                        || this.propertyType.equals("java.lang.String[]")
                        || this.propertyType.equals("java.lang.MultiLineString")
                        || this.propertyType.equals("java.lang.Object")
                        || this.propertyType.equals("java.lang.Boolean")) {

                    setPropertyStringValue(" ");
                } else {
                    if (this.propertyType.equals("java.lang.Double")
                            || this.propertyType.equals("java.lang.Long")
                            || this.propertyType.equals("java.lang.Integer")) {
                        setPropertyStringValue("0");
                    } else {
                        if (this.propertyType.equals("java.util.Date")) {
                            System.out.println("entro a fecha");
                            Date date = Calendar.getInstance().getTime();
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                            String fecha = sdf.format(date);
                            System.out.println(fecha);
                            setPropertyStringValue(fecha);
                            setPropertyDateValue(date);
                        }

                    }
                }

            }
        }

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
    }

    @Override
    public Class<Property> getEntityClass() {
        return Property.class;
    }

    @TransactionAttribute
    public String saveProperty() {
        getInstance().setType(propertyType);
        System.out.println("tipo de la propiedaaaaaaaad>>>>>>>>>> " + getInstance().getType());
        System.out.println("valor de la propiedad>>>>>" + this.propertyStringValue);
        log.info("eqaula --> saving " + getInstance().getName());

        if (getInstance().getType().equals("java.util.Date")) {
            getInstance().setValue(Calendar.getInstance().getTime());
        }
        if (getInstance().getType().equals("java.lang.Long")||getInstance().getType().equals("java.lang.Integer")) {
            propertyStringValue = this.propertyStringValue.substring(0, (propertyStringValue.length() - 2));
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
        if (getInstance().getType().equals("org.mtop.model.EstadoParteMecanica")) {
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
                beta.setBussinesEntity(servgen.buscarPorId(BussinesEntity.class, v.getId()));
                save(beta);
            }
        }

        return "/pages/admin/bussinesentitytype/bussinesentitytype?faces-redirect=true&bussinesEntityTypeId=" + getBussinesEntityTypeId();
    }

    @Transactional
    public String deleteProperty() {
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
                    outcome = "/pages/admin/bussinesentitytype/bussinesentitytype?faces-redirect=true&bussinesEntityTypeId=" + getBussinesEntityTypeId();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, UI.getMessages("common.property.hasValues") + " " + getInstance().getName(), ""));
                    RequestContext.getCurrentInstance().execute("deletedDlg.hide()"); //cerrar el popup si se grabo correctamente
                    outcome = "/pages/admin/bussinesentitytype/property?faces-redirect=true&bussinesEntityTypeId=" + getBussinesEntityTypeId() + "&propertyId=" + getPropertyId();
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
            if ("java.lang.String".equals(getInstance().getType()) || "java.lang.String[]".equals(getInstance().getType()) || "org.mtop.model.EstadoParteMecanica".equals(getInstance().getType()) || "java.lang.MultiLineString".equals(getInstance().getType())) {
                o = value;
            } else if ("java.lang.Double".equals(getInstance().getType())) {
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
//            } else if ("java.lang.Double".equals(getInstance().getType())) {
//                o = Double.valueOf(value);
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
        return ban;
    }

    public String cargarValidador() {
        //log.info("ingreso a validador value");
        System.out.println("ingresa cargar validador");
        System.out.println("ingresa cargar validador" + getInstance().getType());
        if ("java.lang.String[]".equals(getInstance().getType().toString())) {
            log.info("ingreso a validador value valueTextPropertyValidator");
            return "valueTextPropertyValidator";
        }
        if ("org.mtop.model.EstadoParteMecanica".equals(getInstance().getType().toString())) {
            log.info("ingreso a validador value valueTextPropertyValidator");
            return "valueTextPropertyValidator";
        }
        return "";
    }

    public List<String> getTiposDatos() {
        List<String> tipos = new ArrayList<String>();
        tipos.add("org.mtop.model.Structure");
        tipos.add("org.mtop.model.Group");

        List<Property> l = findAllPropiedades();
        Structure structura = bussinesEntityTypeService.getStructure(structureId);
        Property p = new Property();
        for (int i = 1; i < l.size(); i++) {
            if (structura.getName().equals(l.get(i).getName())) {
                p = l.get(i);
            }

        }
        if (p.getGroupName() != null) {
            if (p.getGroupName().equals("Vehiculo")) {
                tipos.add("org.mtop.model.EstadoParteMecanica");
                // setPropertyStringValue("Bueno,Malo");
            }

        }

        tipos.add("java.util.Date");
        tipos.add("java.lang.String");
        tipos.add("java.lang.Double");
        tipos.add("java.lang.Integer");
        tipos.add("java.lang.Long");
        tipos.add("java.lang.Boolean");
        tipos.add("java.lang.String[]");
        tipos.add("java.lang.MultiLineString");
        tipos.add("java.lang.Object");
        return tipos;
    }

    public List<Property> findAllPropiedades() {
        return findAll(Property.class);
    }
}
