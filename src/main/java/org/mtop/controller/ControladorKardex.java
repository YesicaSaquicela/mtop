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

import org.mtop.data.ListaRequisicion;
import org.mtop.data.ListaSolicitudesReparacion;
import org.mtop.model.Kardex;
import org.mtop.model.Persona;
import org.mtop.model.Requisicion;
import org.mtop.model.SolicitudReparacion;
import org.mtop.model.TipoAtributo;

import org.mtop.service.ServicioEntidadGenerica;
import org.mtop.service.ServicioGenerico;

/**
 *
 * @author jesica
 */
@Named
@SessionScoped
public class ControladorKardex implements Serializable {

    @Inject //para poder utilizar los servicios 
    private ServicioGenerico servGen;

    private Kardex kardex=new Kardex();
    @Inject //para cargar tipos de atributos de cada clase
    private ServicioEntidadGenerica seg;
    private List<SolicitudReparacion> listaSolicitudes=new ArrayList<SolicitudReparacion>();
    private List<Requisicion> listaRequisiciones=new ArrayList<Requisicion>();
    private SolicitudReparacion solicitudReparacion=new SolicitudReparacion();
    private Date fechaSalidaTaller;
    private Date fechaEntradaTaller;
    private Long id;

    @Inject
    Event<Kardex> evento;

    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
        if(id!=null){
            kardex=servGen.buscarPorId(Kardex.class, id);
        }
    }

    public Date getFechaSalidaTaller() {
        return fechaSalidaTaller;
    }

    public void setFechaSalidaTaller(Date fechaSalidaTaller) {
        this.fechaSalidaTaller = fechaSalidaTaller;
    }

    public Date getFechaEntradaTaller() {
        return fechaEntradaTaller;
    }

    public void setFechaEntradaTaller(Date fechaEntradaTaller) {
        this.fechaEntradaTaller = fechaEntradaTaller;
    }

    
    public SolicitudReparacion getSolicitudReparacion() {
        return solicitudReparacion;
    }

    public void setSolicitudReparacion(SolicitudReparacion solicitudReparacion) {
        this.solicitudReparacion = solicitudReparacion;
    }

    
    public List<Requisicion> getRequisiciones() {
        ListaRequisicion lr=new ListaRequisicion();
        listaRequisiciones= lr.getRequisicion();
        return listaRequisiciones;
    }

    public void setRequisiciones(List<Requisicion> requisiciones) {
        this.listaRequisiciones = requisiciones;
    }

    public List<SolicitudReparacion> getListaSolicitudes() {
        ListaSolicitudesReparacion lc=new ListaSolicitudesReparacion();
        listaSolicitudes= lc.getSolicitudesReparacion();
        return listaSolicitudes;
    }

    public void setListaSolicitudes(List<SolicitudReparacion> listaSolicitudes) {
        this.listaSolicitudes = listaSolicitudes;
    }
    

    public Kardex getKardex() {
        return kardex;
    }

    public void setKardex(Kardex kardex) {
        this.kardex = kardex;
    }
    public String asignarSolicitud(){
        return "/paginas/Kardex/crearEditarSolicitud?faces-redirec=true";
    }
    public String asignarRequisicion(Long id){
        //Requisicion r = seg.buscarPorNombre(Persona.class.getName());
        return "/paginas/Kardex/listaRequisicion?faces-redirec=true";
    }


    
    
    //metodos acciones
    //si en vista se desea guardar y k dede een la misma vista el metoso no devuelve nada
    //si en vista kiero redireccionar a otra vista return ..>ruta
    @TransactionAttribute
    public String guardar() {
        kardex.setFechaActualizacion(new Date());
//        System.out.println("conductooor"+vehiculo.getConductor().getNombre());
        try {
            if (kardex.getId() != null) {
                servGen.actualizar(kardex);
            } else {
                kardex.setEstado(true);
                servGen.crear(kardex);
                System.out.println("Guardo exitosamente");
            }
            Init();
            evento.fire(kardex);
        } catch (Exception e) {
            e.printStackTrace();//muestra errores internos que se genera
            System.out.println("Error al guardar");
        }
        return "/paginas/Vehiculo/lista?faces-redirec=true";

    }

    @PostConstruct
    public void Init() {
        kardex = crearEntidad();
        //persona.setListaSolicitud(new ArrayList<SolicitudReparacion>());
//        listap = servGen.buscarTodos(Persona.class);
    }

    public Kardex crearEntidad() {
        Date fecha = Calendar.getInstance().getTime();
        //TipoAtributo ta = seg.buscarPorNombre(Persona.class.getName());//llamar el atributo
        this.listaSolicitudes = new ArrayList<SolicitudReparacion>();
        Kardex kardex = new Kardex();
        
        kardex.setFechaCreacion(fecha);
//        vehiculo.setTipoAtributo(ta);//cargar los datos parametrizables 
//        vehiculo.construirAtributos(seg);
        return kardex;
    }
    

}
