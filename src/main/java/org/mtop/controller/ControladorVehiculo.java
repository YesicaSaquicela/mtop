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

import org.mtop.data.ListaPersonas;
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
public class ControladorVehiculo implements Serializable {

    @Inject //para poder utilizar los servicios 
    private ServicioGenerico servGen;

    private Vehiculo vehiculo=new Vehiculo();
    @Inject //para cargar tipos de atributos de cada clase
    private ServicioEntidadGenerica seg;
    private List<Persona> listaConductores=new ArrayList<Persona>();

    private Long id;

    @Inject
    Event<Vehiculo> evento;

 

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
        if(id!=null){
            vehiculo=servGen.buscarPorId(Vehiculo.class, id);
        }
    }

    public List<Persona> getListaConductores() {
        ListaPersonas lc=new ListaPersonas();
        System.out.println("lista Personas"+lc.toString());
        listaConductores= lc.getPersona();
        return listaConductores;
    }

    public void setListaConductores(List<Persona> listaConductores) {
        this.listaConductores = listaConductores;
    }
    

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    //metodos acciones
    //si en vista se desea guardar y k dede een la misma vista el metoso no devuelve nada
    //si en vista kiero redireccionar a otra vista return ..>ruta
    @TransactionAttribute
    public String guardar() {
        vehiculo.setFechaActualizacion(new Date());
//        System.out.println("conductooor"+vehiculo.getConductor().getNombre());
        try {
            if (vehiculo.getId() != null) {
                servGen.actualizar(vehiculo);
            } else {
                vehiculo.setEstado(true);
                servGen.crear(vehiculo);
                System.out.println("Guardo exitosamente");
            }
            Init();
            evento.fire(vehiculo);
        } catch (Exception e) {
            e.printStackTrace();//muestra errores internos que se genera
            System.out.println("Error al guardar");
        }
        return "/paginas/Vehiculo/lista?faces-redirec=true";

    }

    @PostConstruct
    public void Init() {
        vehiculo = crearEntidad();
        //persona.setListaSolicitud(new ArrayList<SolicitudReparacion>());
//        listap = servGen.buscarTodos(Persona.class);
    }

    public Vehiculo crearEntidad() {
        Date fecha = Calendar.getInstance().getTime();
        //TipoAtributo ta = seg.buscarPorNombre(Persona.class.getName());//llamar el atributo
        this.listaConductores = new ArrayList<Persona>();
        Vehiculo vehicul = new Vehiculo();
        
        vehicul.setFechaCreacion(fecha);
//        vehiculo.setTipoAtributo(ta);//cargar los datos parametrizables 
//        vehiculo.construirAtributos(seg);
        return vehicul;
    }
    

}
