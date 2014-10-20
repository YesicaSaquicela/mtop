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
import org.mtop.modelo.dinamico.Property;
import org.mtop.modelo.dinamico.BussinesEntityType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import org.mtop.cdi.Web;
import org.mtop.service.BussinesEntityService;
import org.mtop.service.BussinesEntityTypeListService;
import org.mtop.service.BussinesEntityTypeService;
import org.jboss.seam.transaction.Transactional;

/**
 *
 * @author
 */
@Named
@ViewScoped
public class BussinesEntityTypeHome extends BussinesEntityHome<BussinesEntityType> implements Serializable {

    private static final long serialVersionUID = 7632987414391869389L;
    private static org.jboss.solder.logging.Logger log = org.jboss.solder.logging.Logger.getLogger(BussinesEntityHome.class);
    @Inject
    @Web
    private EntityManager em;
    @Inject
    private BussinesEntityTypeListService bussinesEntityTypeListService;
    @Inject
    private BussinesEntityTypeService bussinesEntityTypeService;
    @Inject
    private BussinesEntityService bussinesEntityService;
    private String name;
    private String palabrab;
    List<Property> listaPropiedades;
    List<Property> listaPropiedades2;
    private String valorIniSolicitud;
    private String viNumRequisicionRep;
    private String viNumRequisicionBie;
    private Property propiedad;
    private Property propiedadbien;
    private Property propiedadrepa;
    private String mensaje2;
    
   
    public String getMensaje2() {
        return mensaje2;
    }

    public void setMensaje2(String mensaje2) {
        this.mensaje2 = mensaje2;
    }
    
    

    public String getValorIniSolicitud() {
        return valorIniSolicitud;
    }

    public void setValorIniSolicitud(String valorIniSolicitud) {
        this.valorIniSolicitud = valorIniSolicitud;
    }

    public String getViNumRequisicionRep() {
        return viNumRequisicionRep;
    }

    public void setViNumRequisicionRep(String viNumRequisicionRep) {
        this.viNumRequisicionRep = viNumRequisicionRep;
    }

    public String getViNumRequisicionBie() {
        return viNumRequisicionBie;
    }

    public void setViNumRequisicionBie(String viNumRequisicionBie) {
        this.viNumRequisicionBie = viNumRequisicionBie;
    }

    @TransactionAttribute
    public void fijarValorSolicitud() {
        valorIniSolicitud = valorIniSolicitud.trim();
        if (!valorIniSolicitud.equals("")) {
            propiedad.setValidator("sdf");
            Object o = new Object();
            o = valorIniSolicitud;

            propiedad.setValue((Serializable) o);
            save(propiedad);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR! ", "Valor inicial de numero de solicitud necesita un valor"));
        }

    }

    @TransactionAttribute
    public void fijarValorRequisicion() {
        viNumRequisicionBie = viNumRequisicionBie.trim();
        viNumRequisicionRep = viNumRequisicionRep.trim();
                
        if (!viNumRequisicionRep.equals("") && !viNumRequisicionBie.equals("")) {
           
            Object o = new Object();
            o = viNumRequisicionBie;
            propiedadbien.setValue((Serializable) o);
            save(propiedadbien);
            
            o = viNumRequisicionRep;
            propiedadrepa.setValue((Serializable) o);
            save(propiedadrepa);
            
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR! ", "Valores iniciales de requisición necesitan un valores"));
        }

    }

    public List<Property> getListaPropiedades() {
        return listaPropiedades;
    }

    public void setListaPropiedades(List<Property> listaPropiedades) {
        try {
            
            if (getInstance().getLabel() != null) {
                if (getInstance().getLabel().equals("Solicitud Reparación y Mantenimiento")) {
                 
                    List<Property> lp = new ArrayList<Property>();
                    if (listaPropiedades != null) {
                        for (Property p : listaPropiedades) {
                            if (p.getName().equals("viNumSolicitud")) {
                                valorIniSolicitud = p.getValue().toString();
                                propiedad = p;
                            } else {
                                lp.add(p);
                            }
                        }
                        if (valorIniSolicitud != null) {
                            System.out.println("entro a fijar propiedades para soli");
                            listaPropiedades = lp;
                            listaPropiedades2 = listaPropiedades;
                        }
                    }

                }
              
                if (getInstance().getLabel().equals("Requisición")) {
                    List<Property> lp = new ArrayList<Property>();
                    if (listaPropiedades != null) {
                        for (Property p : listaPropiedades) {
                            if (p.getName().equals("viNumRequisicionReparacion")) {
                                viNumRequisicionRep = p.getValue().toString();
                                propiedadrepa = p;
                            } else {
                                if (p.getName().equals("viNumRequisicionBienes")) {
                                    viNumRequisicionBie=p.getValue().toString();
                                    propiedadbien=p;
                                } else {
                                    lp.add(p);
                                }
                            }
                         
                        }
                        if (valorIniSolicitud != null) {
                            listaPropiedades = lp;
                            listaPropiedades2 = listaPropiedades;
                        }
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("" + listaPropiedades);
        this.listaPropiedades2 = listaPropiedades;
        this.listaPropiedades = listaPropiedades;
    }

    public String getPalabrab() {
        return palabrab;
    }

    public void setPalabrab(String palabrab) {
        this.palabrab = palabrab;
    }

    public ArrayList<String> autocompletar(String query) {
        System.out.println("QUEryyyyy" + query);
        ArrayList<String> ced = new ArrayList<String>();
        ced = new ArrayList<String>();

        for (Property bet : listaPropiedades) {
            if (bet.getName().toLowerCase().contains(query.toLowerCase())) {
                ced.add(bet.getName());
            }

        }
        System.out.println("listaaaaa autocompletar" + ced);
        return ced;
    }

    public void buscar() {

        List<Property> le = new ArrayList<Property>();
        le.clear();
        palabrab = palabrab.trim();
        if (palabrab == null || palabrab.equals("")) {
            palabrab = "Ingrese algun valor a buscar";
        }
        for (Property property : listaPropiedades2) {

            if (property.getName().toLowerCase().contains(palabrab.toLowerCase())) {
                le.add(property);
            }
        }

        if (le.isEmpty()) {
           
            if (palabrab.equals("Ingrese algun valor a buscar")) {
               FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN:", " Ingrese algun valor a buscar");
                FacesContext.getCurrentInstance().addMessage("", msg);
                palabrab = " ";
            } else {
                listaPropiedades=le;
                 FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN:No se ha encontrado", palabrab);
                FacesContext.getCurrentInstance().addMessage("", msg);
            }

        } else {
            listaPropiedades = le;

        }

    }

    public void limpiar() {
        palabrab = "";
        listaPropiedades = listaPropiedades2;
    }

    public BussinesEntityTypeHome() {
        log.info("mtop --> Inicializo BussinesEntityTypeHome");
    }

    public Long getBussinesEntityTypeId() {
        return (Long) getId();
    }

    public void setBussinesEntityTypeId(Long bussinesEntityTypeId) {
        setId(bussinesEntityTypeId);

    }
    public void fijaEntidad(Long bussinesEntityTypeId) {
        System.out.println("enro a fija e la entidad ");
                
        setId(bussinesEntityTypeId);
        System.out.println("fijo "+getInstance().getName());

    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected BussinesEntityType createInstance() {
        log.info("mtop--> Creando instance ");
        BussinesEntityType bussinesEntityType = new BussinesEntityType();
        Date now = Calendar.getInstance().getTime();
        Calendar ago = Calendar.getInstance();
        ago.add(Calendar.DAY_OF_YEAR, (-1 * 364 * 18)); //18 años atras
        Structure structure = new Structure();
        structure.setCreatedOn(now);
        structure.setLastUpdate(now);
        /*
         List<Property> attributes = new ArrayList<Property>();
         attributes.add(buildStructureTypeProperty("PersonalData", "Datos Personales", "Información personal relevante", "/pages/profile/data/personal", 1L));
        
         structure.setProperties(attributes); */
        bussinesEntityType.addStructure(structure);

        return bussinesEntityType;
    }

    @TransactionAttribute
    public void load() {
        log.info("mtop --> Cargando instancia de EntidadGeneral para " + getId());
        if (isIdDefined()) {
            wire();
        }
        log.info("mtop --> Cargando instancia" + getInstance());
    }

    @TransactionAttribute
    public void wire() {
        getInstance();
    }

    public boolean isWired() {
        return true;
    }

    public BussinesEntityType getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

    @PostConstruct
    public void init() {
        setEntityManager(em);
        bussinesEntityTypeService.setEntityManager(em);
        bussinesEntityService.setEntityManager(em);
        valorIniSolicitud="";
        viNumRequisicionBie="";
        viNumRequisicionRep="";

    }

    @Override
    public Class<BussinesEntityType> getEntityClass() {
        return BussinesEntityType.class;
    }

    @TransactionAttribute
    public String saveBussinesEntityType() {
        log.info("mtop --> guardando " + getInstance().getName());
        if (getInstance().isPersistent()) {
            getInstance().getStructures().get(0).setName(getInstance().getName());
            save(getInstance());
        } else {
            try {
                log.info("mtop --> guandando nuevo" + getInstance().getName());
                create(getInstance());
                setBussinesEntityTypeId(getInstance().getId());
                wire();
                getInstance().getStructures().get(0).setName(getInstance().getName());
                save(getInstance());

            } catch (Exception ex) {
                log.info("mtop --> error al guardar nuevo" + getInstance().getName());
            }
        }
        return "/paginas/admin/bussinesentitytype/bussinesentitytype?faces-redirect=true&bussinesEntityTypeId=" + getBussinesEntityTypeId();
    }

    @Transactional
    public void saveBussinesEntity() {
        try {
            if (getBussinesEntity() == null) {
                throw new NullPointerException("bussinessEntityType is null");
            }

            if (getBussinesEntity().getName().equalsIgnoreCase("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No name", null));
            } else {
                save(getBussinesEntity());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizó con exito " + bussinesEntity.getName(), ""));
            }

        } catch (Exception e) {
            System.out.println("saveBussinesEntity ERROR = " + e.getMessage());
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRORE", e.toString()));
        }
    }

    @Transactional
    public String deleteBussinesEntityType() {
        System.out.println("ento a borrar entidad"+getInstance().getLabel());
        try {
            if (getInstance() == null) {
                throw new NullPointerException("property is null");
            }

            if (getInstance().isPersistent()) {
                delete(getInstance());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Se desactivo exitosamente:  " + getInstance().getName(), ""));
                //RequestContext.getCurrentInstance().execute("editDlg.hide()"); //cerrar el popup si se grabo correctamente

            } else {
                //remover de la lista, si aún no esta persistido
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "No se puede desactivarse este tipo de entidad de negocio", ""));
            }

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRORE", e.toString()));
        }
        return "/paginas/admin/bussinesentitytype/list";
    }
    

    @TransactionAttribute
    public void displayBootcampAjax() {
//        getInstance().setShowBootcamp(true);
        update();
    }

    @TransactionAttribute
    public void dismissBootcampAjax() {
//        getInstance().setShowBootcamp(false);
        update();
    }

    public boolean isAssociatedToBussinesEntity() {
        if (!isIdDefined()) {
            return false;
        }
        boolean b = false;
        List<BussinesEntity> bussinesEntityList = bussinesEntityService.findBussinesEntityForType(getInstance());
        b = (!bussinesEntityList.isEmpty() || isPropertyAssociatedBussinesEntityAttributes());
        log.info("mtop --> Ingreso a verificar entidadGeneral: " + b);  //true si esta asociado 
        return b;
    }

    public boolean isPropertyAssociatedBussinesEntityAttributes() {
        if (!isIdDefined()) {
            return false;
        }
        for (Property p : getInstance().getStructures().get(0).getProperties()) {
            if (!bussinesEntityService.findBussinesEntityAttributeForProperty(p).isEmpty()) {
                return true;   //retorna true si esta relacionado con Atributos
            }
        }
        return false;
    }

    public boolean esTipoEntidad(String nombrePropiedad) {

        for (BussinesEntityType bet : bussinesEntityTypeService.findAll()) {
            if (bet.getName().equals(nombrePropiedad)) {
                return true;
            }
        }
        return false;
    }

}
