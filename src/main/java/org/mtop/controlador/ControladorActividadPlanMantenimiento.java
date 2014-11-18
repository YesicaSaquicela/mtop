/*
    SOFTWARE PARA LA GESTIÓN DE INFORMACIÓN DEL ESTADO  MECÁNICO DE LOS 
    VEHÍCULOS DEL MINISTERIO DE TRANSPORTE Y OBRAS PÚBLICAS
    Copyright (C) 2014  Romero Carla, Saquicela Yesica

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


package org.mtop.controlador;


import java.io.Serializable;
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
import org.jboss.seam.transaction.Transactional;
import org.mtop.cdi.Web;
import org.mtop.controlador.dinamico.BussinesEntityHome;
import org.mtop.modelo.dinamico.BussinesEntityType;
import org.mtop.modelo.dinamico.Property;
import org.mtop.modelo.ActividadPlanMantenimiento;
import org.mtop.servicios.ServicioGenerico;

/**
 *
 * @author carla
 * @author yesica
 * 
 */

@Named
@ViewScoped
public class ControladorActividadPlanMantenimiento extends BussinesEntityHome<ActividadPlanMantenimiento> implements Serializable {

    @Inject
    @Web
    private EntityManager em;
    @Inject
    private ServicioGenerico servgen;
   
    public Long getActividadPlanMantenimientoId() {
        System.out.println("IIIIDEE"+getId());
        return (Long) getId();
    }

    public void setActividadPlanMantenimientoId(Long actividadPlanMantenimientoId) {

        setId(actividadPlanMantenimientoId);

    }

    @TransactionAttribute   //
    public ActividadPlanMantenimiento load() {
        if (isIdDefined()) {
            wire();
        }

        return getInstance();
    }

    @TransactionAttribute
    public void wire() {
        getInstance();
    }

    

    

    @PostConstruct
    public void init() {
        setEntityManager(em);
        /*el bussinesEntityService.setEntityManager(em) solo va si la Entidad en este caso (Vehiculo)
         *hereda de la Entidad BussinesEntity...  caso contrario no se lo agrega
         */
        bussinesEntityService.setEntityManager(em);
        servgen.setEm(em);
      
    }

    @Override
    protected ActividadPlanMantenimiento createInstance() {
        //prellenado estable para cualquier clase 
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(ActividadPlanMantenimiento.class.getName());
        Date now = Calendar.getInstance().getTime();
        ActividadPlanMantenimiento actividadpm = new ActividadPlanMantenimiento();
        actividadpm.setCreatedOn(now);
        actividadpm.setLastUpdate(now);
        actividadpm.setActivationTime(now);
        actividadpm.setType(_type);
        actividadpm.buildAttributes(bussinesEntityService);  //
        return actividadpm;
    }

    @Override
    public Class<ActividadPlanMantenimiento> getEntityClass() {
        return ActividadPlanMantenimiento.class;
    }

    @TransactionAttribute
    public String guardar() {
       
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
      
        System.out.println("PRESENTAR persisten>>>>>"+getInstance().isPersistent());
        try {
            if (getInstance().isPersistent()) {
                System.out.println("ENTRO A ACTUALIZAR ACTIVIDAD>>>>>"+getInstance().getActividad());   
                System.out.println("ENTRO A ACTUALIZAR KILOMETRAJE>>>>>"+getInstance().getKilometraje()); 
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizó actividad Plan de Mantenimiento" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            } else {
                System.out.println("ENTRO A crear ACTIVIDAD>>>>>"+getInstance().getActividad());   
//                System.out.println("ENTRO A CREAR KILOMETRAJE>>>>>"+getInstance().getKilometraje()); 
                getInstance().setEstado(true);
                create(getInstance());
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se creó una nueva Actividad del plan de mantenimiento" + getInstance().getId() + " con éxito"," ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error al guardar actividad: " + getInstance().getId()," ");
            FacesContext.getCurrentInstance().addMessage("", msg);
            System.out.println("no guardo actividaddddd");
        }
        return "/paginas/secretario/lista.xhtml?faces-redirect=true";
    }
    
    @Transactional
    public String borrarEntidad() {
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
        return "/paginas//lista.xhtml?faces-redirect=true";
    }
    public boolean tieneEstadosEstructura(Property propiedad){
        for(Property p: servgen.buscarTodos(Property.class)){
            if(p.getGroupName()!=null){
                if(p.getGroupName().equals(propiedad.getName())){
                System.out.println("encontro su propiedad>>>>> "+p.getName());
                if(p.getType().equals("org.mtop.modelo.EstadoParteMecanica")){
                    System.out.println("retornara true");
                    return true;
                }
            }
            }
            
        }
        System.out.println("retornara false");
        return false;
    }

}
