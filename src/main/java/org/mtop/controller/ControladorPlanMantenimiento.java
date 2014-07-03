/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mtop.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.mtop.model.ActividadPlanMantenimiento;
import org.mtop.model.ItemRequisicion;
import org.mtop.model.PlanMantenimiento;
import org.mtop.model.Vehiculo;
import org.mtop.service.ServicioEntidadGenerica;
import org.mtop.service.ServicioGenerico;

/**
 *
 * @author carlis
 */
@Named
@SessionScoped
public class ControladorPlanMantenimiento implements Serializable {
    
    @Inject //para poder utilizar los servicios 
    private ServicioGenerico servGen;
    
    @Inject //para cargar tipos de atributos de cada clase
    private ServicioEntidadGenerica seg;
    private Long id;
    private PlanMantenimiento planMantenimiento = new PlanMantenimiento();
    ActividadPlanMantenimiento actividadpm = new ActividadPlanMantenimiento();
    private List<ActividadPlanMantenimiento> listaActividadpm = new ArrayList<ActividadPlanMantenimiento>();
    
    @Inject
    Event<PlanMantenimiento> evento;
    
    public List<ActividadPlanMantenimiento> getListaActividadpm() {
        return listaActividadpm;
    }
    
    public void setListaActividadpm(List<ActividadPlanMantenimiento> listaActividadpm) {
        this.listaActividadpm = listaActividadpm;
    }
    
    public ActividadPlanMantenimiento getActividadpm() {
        return actividadpm;
    }
    
    public void setActividadpm(ActividadPlanMantenimiento actividadpm) {
        this.actividadpm = actividadpm;
    }
    
    public Event<PlanMantenimiento> getEvento() {
        return evento;
    }
    
    public void setEvento(Event<PlanMantenimiento> evento) {
        this.evento = evento;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
        if (id != null) {
            planMantenimiento = servGen.buscarPorId(PlanMantenimiento.class, id);
        }
    }
    
    public PlanMantenimiento getPlanMantenimiento() {
        return planMantenimiento;
    }
    
    public void setPlanMantenimiento(PlanMantenimiento planMantenimiento) {
        this.planMantenimiento = planMantenimiento;
    }
    
    @TransactionAttribute
    public String guardar() {
        planMantenimiento.setFechaActualizacion(new Date());
        planMantenimiento.setEstado(true);
        planMantenimiento.setListaActividadpm(listaActividadpm);
//        System.out.println("conductooor"+vehiculo.getConductor().getNombre());
        try {
            if (planMantenimiento.getId() != null) {
                servGen.actualizar(planMantenimiento);
            } else {
                planMantenimiento.setEstado(true);
                servGen.crear(planMantenimiento);
                System.out.println("Guardo exitosamente");
            }
            Init();
            evento.fire(planMantenimiento);
        } catch (Exception e) {
            e.printStackTrace();//muestra errores internos que se genera
            System.out.println("Error al guardar");
        }
        planMantenimiento= new PlanMantenimiento();
        listaActividadpm= new ArrayList<ActividadPlanMantenimiento>();
        return "/paginas/PlanMantenimiento/lista?faces-redirec=true";
        
    }
    
    public String editarActividad(ActividadPlanMantenimiento item) {
        actividadpm = item;
        listaActividadpm.remove(item);
        return "/paginas/PlanMantenimiento/crearEditar?faces-redirec=true";
    }
    
    public String crearActividad() {
        listaActividadpm.add(actividadpm);
        actividadpm = new ActividadPlanMantenimiento();
        return "/paginas/PlanMantenimiento/crearEditar?faces-redirec=true";
    }

 
   
         
    @PostConstruct
    public void Init() {
        planMantenimiento = crearEntidad();
        //persona.setListaSolicitud(new ArrayList<SolicitudReparacion>());
//        listap = servGen.buscarTodos(Persona.class);
    }
    
    public PlanMantenimiento crearEntidad() {
        Date fecha = Calendar.getInstance().getTime();
        //TipoAtributo ta = seg.buscarPorNombre(Persona.class.getName());//llamar el atributo
        PlanMantenimiento pm = new PlanMantenimiento();
        pm.setFechaCreacion(fecha);
//        vehiculo.setTipoAtributo(ta);//cargar los datos parametrizables 
//        vehiculo.construirAtributos(seg);
        return pm;
    }
    
}
