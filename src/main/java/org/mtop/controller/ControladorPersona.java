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
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import org.mtop.model.Persona;
import org.mtop.model.SolicitudReparacion;
import org.mtop.model.TipoAtributo;
import org.mtop.model.ValorAtributo;
import org.mtop.service.ServicioEntidadGenerica;
import org.mtop.service.ServicioGenerico;

/**
 *
 * @author carlis
 */
@Named // pongo esta etiqueta para ya no generan set y get de un objeto.
@ConversationScoped //para poder hacer peticiones intermedias
public class ControladorPersona implements Serializable {

    @Inject //para poder utilizar los servicios 
    private ServicioGenerico servGen;

    @Produces
    @Named
    private Persona persona=new Persona();
    @Inject //para cargar tipos de atributos de cada clase
    private ServicioEntidadGenerica seg;
    private List<Persona> listap = new ArrayList<Persona>();
    private Long id;
    @Inject
    Event<Persona> evento;

    public Long getId() {
        return id;
        
    }

    public void setId(Long id) {
        this.id = id;
        if(id!=null){
            persona=servGen.buscarPorId(Persona.class, id);
            System.out.println("personaaaaaa "+persona.toString());
        }else{
            persona=crearEntidad();
            System.out.println("nueva persona");
        }
        //
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }
    

    public List<Persona> getListap() {
        return listap;
    }

    public void setListap(List<Persona> listap) {
        this.listap = listap;
    }

    //metodos acciones
    //si en vista se desea guardar y que quede en la misma vista el metodo no devuelve nada
    //si en vista kiero redireccionar a otra vista return ..>ruta
    @TransactionAttribute
    public String guardarPersona() {
        persona.setFechaActualizacion(new Date());
        persona.setEstado(true);
        persona.setListaValorAtributo(persona.getListaValorAtributo());
        try {
            if (persona.getId() != null) {
                servGen.actualizar(persona);
            } else {
                persona.setEstado(true);
                System.out.println("IDPERSOOOONAAAAAA1111antes>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+persona.getId());
                servGen.crear(persona);
                             
                System.out.println("Guardo exitosamente");
            }
            System.out.println("LISTA DE VALORES AL GUARDAR======="+persona.getListaValorAtributo());
            Init();
            evento.fire(persona);
        } catch (Exception e) {
            e.printStackTrace();//muestra errores internos que se genera
            System.out.println("Error al guardar");
        }
        return "/paginas/Personal/lista?faces-redirec=true";
    }

    @PostConstruct
    public void Init() {
//        if(persona==null){
//            persona = crearEntidad();
//            System.out.println("creo persona");
//        }
//        
        //persona.setListaSolicitud(new ArrayList<SolicitudReparacion>());
        //listap = servGen.buscarTodos(Persona.class);
    }

    public Persona crearEntidad() {
        Date fecha = Calendar.getInstance().getTime();
        TipoAtributo ta = seg.buscarPorNombre(Persona.class.getName());//llamar el atributo
        Persona persona = new Persona();
        persona.setFechaCreacion(fecha);
        persona.setTipoAtributo(ta);//cargar los datos parametrizables 
        persona.construirAtributos(seg);
        return persona;
    }
}
