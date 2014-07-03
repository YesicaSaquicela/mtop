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
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.mtop.model.Persona;
import org.mtop.model.TipoAtributo;
import org.mtop.model.Vehiculo;
import org.mtop.service.ServicioEntidadGenerica;
import org.mtop.service.ServicioGenerico;

/**
 *
 * @author jesica
 */
@Named
@ConversationScoped
public class ControladorMecanico implements Serializable {

    @Inject //para poder utilizar los servicios 
    private ServicioGenerico servGen;

    private Persona mecanico;
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
            mecanico=servGen.buscarPorId(Persona.class, id);
        }
    }

    public Persona getMecanico() {
        return mecanico;
    }

    public void setMecanico(Persona mecanico) {
        this.mecanico = mecanico;
    }

    //metodos acciones
    //si en vista se desea guardar y k dede een la misma vista el metoso no devuelve nada
    //si en vista kiero redireccionar a otra vista return ..>ruta
    @TransactionAttribute
    public String guardar() {
        mecanico.setFechaActualizacion(new Date());
        mecanico.setTipo("mecanico");
        System.out.println(mecanico.getNombre());
        System.out.println(mecanico.getApellido());
        System.out.println(mecanico.getEdad());
        System.out.println(mecanico.getEdad());
        try {
            if (mecanico.getId() != null) {
                servGen.actualizar(mecanico);
            } else {
                mecanico.setEstado(true);
                servGen.crear(mecanico);
                System.out.println("Guardo exitosamente");
            }
            Init();
            evento.fire(mecanico);
        } catch (Exception e) {
            e.printStackTrace();//muestra errores internos que se genera
            System.out.println("Error al guardar");
        }
        return "/paginas/Mecanico/lista?faces-redirec=true";

    }

    @PostConstruct
    public void Init() {
        mecanico = crearEntidad();
        //persona.setListaSolicitud(new ArrayList<SolicitudReparacion>());
//        listap = servGen.buscarTodos(Persona.class);
    }

    public Persona crearEntidad() {
        
        Date fecha = Calendar.getInstance().getTime();
        TipoAtributo ta = seg.buscarPorNombre(Persona.class.getName());//llamar el atributo
        Persona mecanic = new Persona();
        mecanic.setFechaCreacion(fecha);
        mecanic.setTipoAtributo(ta);//cargar los datos parametrizables 
        mecanic.construirAtributos(seg);
        return mecanic;
    }
    

}
