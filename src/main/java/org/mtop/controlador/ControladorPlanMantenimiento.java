/*
 * Copyright 2014 carlis.
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
package org.mtop.controlador;

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
import org.jboss.seam.transaction.Transactional;
import org.mtop.cdi.Web;
import org.mtop.controller.BussinesEntityHome;
import org.mtop.model.BussinesEntityType;
import org.mtop.modelo.ActividadPlanMantenimiento;
import org.mtop.modelo.PlanMantenimiento;
import org.mtop.servicios.ServicioGenerico;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author carlis
 */
@Named
@ViewScoped
public class ControladorPlanMantenimiento extends BussinesEntityHome<PlanMantenimiento> implements Serializable {

    @Inject
    @Web
    private EntityManager em;
    @Inject
    private ServicioGenerico servgen;
    private List<PlanMantenimiento> listaPlanMantenimiento = new ArrayList<PlanMantenimiento>();
    private ControladorActividadPlanMantenimiento cactividadpm;

    // @Named provides access the return value via the EL variable name "members" in the UI (e.g.
    // Facelets or JSP view)
    private boolean skip;
    //private static Logger logger = Logger.getLogger(UserWizard.class.getName());

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public String onFlowProcess(FlowEvent event) {
        if (skip) {
            skip = false;   //reset in case user goes back  
            return "confirm";
        } else {
            System.out.println("lista vacia" + this.cactividadpm.listaActividades.isEmpty());
            if (event.getOldStep().equals("address") && this.cactividadpm.listaActividades.isEmpty()) {
                System.out.println("estas vaciaaaaaa");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "debe ingresar al menos una actividas al plan de mantenimiento"));

                return event.getOldStep();
            } else {

                return event.getNewStep();
            }

        }
    }

    public void editarActividad(ActividadPlanMantenimiento actividad) {
        System.out.println("ACTIVIDAD>>>>>" + cactividadpm.getInstance().getActividad());;
        System.out.println("REMOVE>>>>>>>>>>>." + actividad);

        int con = 0;
        for (ActividadPlanMantenimiento apm : cactividadpm.listaActividades) {

            if (apm.getKilometraje().equals(actividad.getKilometraje())
                    && apm.getActividad().equals(actividad.getActividad())) {
                cactividadpm.listaActividades.remove(con);
                break;

            }
            con++;

        }
        System.out.println("tama;o de la lista" + cactividadpm.listaActividades.size());

    }

    public ControladorActividadPlanMantenimiento getCactividadpm() {
        return cactividadpm;
    }

    public void setCactividadpm(ControladorActividadPlanMantenimiento cactividadpm) {
        this.cactividadpm = cactividadpm;

    }

    public void agregarActividad() {
        if (cactividadpm.getInstance().getActividad().equals("") || cactividadpm.getInstance().getKilometraje().equals("")) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "campos abligatorios."));

        } else {
            cactividadpm.listaActividades.add(cactividadpm.getInstance());
            cactividadpm.setInstance(new ActividadPlanMantenimiento());

        }

    }

    public void guardarActividad() {
        for (ActividadPlanMantenimiento apm : cactividadpm.listaActividades) {
            apm.setPlanMantenimiento(getInstance());//fijarle un plan de mantenimiento a cada actividad de plan de mantenimiento
            cactividadpm.setInstance(apm);//fija la actividad del plan de mantenimiento al controlador de actividad de plan de mantenimiento
            cactividadpm.guardar();
        }
        getInstance().setListaActividadpm(cactividadpm.listaActividades);//fija la lista de actividades al plan de mantenimietno

    }

    public Long getPlanMantenimientoId() {
        System.out.println("IIIIDEE" + getId());
        return (Long) getId();
    }

    public void setPlanMantenimientoId(Long planMantenimientoId) {

        setId(planMantenimientoId);
        //se fija la lista actividades que hay en el plan mantenimiento a la lista actividades.
        cactividadpm.listaActividades = getInstance().getListaActividadpm();

    }


    @TransactionAttribute   //
    public PlanMantenimiento load() {
        if (isIdDefined()) {
            wire();
        }

        return getInstance();
    }

    @TransactionAttribute
    public void wire() {
        getInstance();
    }

    public List<PlanMantenimiento> getListaPlanMantenimiento() {
        return listaPlanMantenimiento;
    }

    public void setListaPlanMantenimiento(List<PlanMantenimiento> listaPlanMantenimiento) {
        this.listaPlanMantenimiento = listaPlanMantenimiento;
    }

    @PostConstruct
    public void init() {
        setEntityManager(em);
        /*el bussinesEntityService.setEntityManager(em) solo va si la Entidad en este caso (Vehiculo)
         *hereda de la Entidad BussinesEntity...  caso contrario no se lo agrega
         */
        bussinesEntityService.setEntityManager(em);
        servgen.setEm(em);
        listaPlanMantenimiento = servgen.buscarTodos(PlanMantenimiento.class);

        cactividadpm = new ControladorActividadPlanMantenimiento();
        cactividadpm.setInstance(new ActividadPlanMantenimiento());
    }

    @Override
    protected PlanMantenimiento createInstance() {
        //prellenado estable para cualquier clase 
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(PlanMantenimiento.class.getName());
        Date now = Calendar.getInstance().getTime();
        PlanMantenimiento planM = new PlanMantenimiento();
        planM.setCreatedOn(now);
        planM.setLastUpdate(now);
        planM.setActivationTime(now);
        planM.setType(_type);
        planM.buildAttributes(bussinesEntityService);  //
        return planM;
    }

    @Override
    public Class<PlanMantenimiento> getEntityClass() {
        return PlanMantenimiento.class;
    }

    @TransactionAttribute
    public String guardar() {

        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);

        try {
            if (getInstance().isPersistent()) {
                guardarActividad();
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizo Plan Mantenimiento" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            } else {

                getInstance().setEstado(true);
                create(getInstance());
                guardarActividad();
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se creo un nuevo Plan MAntenimiento" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
        }
        return "/paginas/planMantenimiento/lista.xhtml?faces-redirect=true";

    }

    @Transactional
    public String borrarEntidad() {
        //       log.info("sgssalud --> ingreso a eliminar: " + getInstance().getId());
        try {
            if (getInstance() == null) {
                throw new NullPointerException("Servicio is null");
            }
            if (getInstance().isPersistent()) {
                delete(getInstance());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Se borró exitosamente:  " + getInstance().getName(), ""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "¡No existe una entidad para ser borrada!", ""));
            }

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.toString()));
        }
        return "/paginas/planMantenimient/lista.xhtml?faces-redirect=true";
    }

}
