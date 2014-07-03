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
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.mtop.service.ServicioEntidadGenerica;
import org.mtop.service.ServicioGenerico;
import org.mtop.model.Persona;
/**
 *
 * @author jesica
 */
@Named
@SessionScoped
public class ControladorConductor implements Serializable {

    @Inject //para poder utilizar los servicios 
    private ServicioGenerico servGen;

    private Persona conductor;
    @Inject //para cargar tipos de atributos de cada clase
    private ServicioEntidadGenerica seg;

    private Long id;

    @Inject
    Event<Persona> evento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
        if(id!=null){
            conductor=servGen.buscarPorId(Persona.class, id);
        }
    }

    public Persona getConductor() {
        
        return conductor;
    }

    public void setConductor(Persona conductor) {
        this.conductor = conductor;
    }

    //metodos acciones
    //si en vista se desea guardar y k dede een la misma vista el metoso no devuelve nada
    //si en vista kiero redireccionar a otra vista return ..>ruta
    @TransactionAttribute
    public String guardar() {
        conductor.setFechaActualizacion(new Date());
        conductor.setTipo("conductor");
        try {
            if (conductor.getId() != null) {
                servGen.actualizar(conductor);
            } else {
                conductor.setEstado(true);
                servGen.crear(conductor);
                System.out.println("Guardo exitosamente");
            }
            Init();
            evento.fire(conductor);
        } catch (Exception e) {
            e.printStackTrace();//muestra errores internos que se genera
            System.out.println("Error al guardar");
        }
        return "/paginas/Conductor/lista?faces-redirec=true";

    }

    @PostConstruct
    public void Init() {
        conductor = crearEntidad();
        //persona.setListaSolicitud(new ArrayList<SolicitudReparacion>());
//        listap = servGen.buscarTodos(Persona.class);
    }

    public Persona crearEntidad() {
        Date fecha = Calendar.getInstance().getTime();
        //TipoAtributo ta = seg.buscarPorNombre(Persona.class.getName());//llamar el atributo
        Persona conductor = new Persona();
        conductor.setFechaCreacion(fecha);
//        vehiculo.setTipoAtributo(ta);//cargar los datos parametrizables 
//        vehiculo.construirAtributos(seg);
        return conductor;
    }
    

}
