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
import org.mtop.model.ItemRequisicion;
import org.mtop.model.ItemSolicitudReparacion;
import org.mtop.model.Persona;
import org.mtop.model.SolicitudReparacion;
import org.mtop.model.TipoAtributo;
import org.mtop.model.Vehiculo;
import org.mtop.service.ServicioEntidadGenerica;
import org.mtop.service.ServicioGenerico;

/**
 *
 * @author jesica
 */
@Named
@SessionScoped
public class ControladorSolicitudReparacion implements Serializable {

    @Inject //para poder utilizar los servicios 
    private ServicioGenerico servGen;

    private SolicitudReparacion solicitudReparacion;
    
    @Inject //para cargar tipos de atributos de cada clase
    private ServicioEntidadGenerica seg;
    List<ItemRequisicion> listaitem=new ArrayList<ItemRequisicion>();

    private Long id;
    ItemSolicitudReparacion itemSR= new ItemSolicitudReparacion();
    
    @Inject
    Event<SolicitudReparacion> evento;

    public Long getId() {
        return id;
    }

    public List<ItemRequisicion> getListaitem() {
        return listaitem;
    }

    public void setListaitem(List<ItemRequisicion> listaitem) {
        this.listaitem = listaitem;
    }

    public ItemSolicitudReparacion getItemSR() {
        return itemSR;
    }

    public void setItemSR(ItemSolicitudReparacion itemSR) {
        this.itemSR = itemSR;
    }

    public void setId(Long id) {
        this.id = id;
        if(id!=null){
            solicitudReparacion= servGen.buscarPorId(SolicitudReparacion.class, id) ;
        }
    }

    public SolicitudReparacion getSolicitudReparacion() {
        return solicitudReparacion;
    }

    public void setSolicitudReparacion(SolicitudReparacion solicitudReparacion) {
        this.solicitudReparacion = solicitudReparacion;
    }

    //metodos acciones
    //si en vista se desea guardar y k dede een la misma vista el metoso no devuelve nada
    //si en vista kiero redireccionar a otra vista return ..>ruta
    @TransactionAttribute
    public String guardar() {
        solicitudReparacion.setFechaActualizacion(new Date());
        try {
            if (solicitudReparacion.getId() != null) {
                servGen.actualizar(solicitudReparacion);
            } else {
                solicitudReparacion.setEstado(true);
                servGen.crear(solicitudReparacion);
                System.out.println("Guardo exitosamente");
            }
            Init();
            
            evento.fire(solicitudReparacion);
        } catch (Exception e) {
            e.printStackTrace();//muestra errores internos que se genera
            System.out.println("Error al guardar");
        }
        return "/paginas/Vehiculo/lista?faces-redirec=true";

    }
    public String guardarItem(){
        solicitudReparacion.getListaItemSR().add(itemSR);
        return "/paginas/Solicitud/crearEditar?faces-redirec=true";
    }
    public String editarItem(ItemSolicitudReparacion item){
        itemSR=item;
        solicitudReparacion.getListaItemSR().add(itemSR);
        return "/paginas/Solicitud/crearEditarItem?faces-redirec=true";
    }
     public String crearItem(){
       
        itemSR=new ItemSolicitudReparacion();
        return "/paginas/Solicitud/crearEditarItem?faces-redirec=true";
    }

    @PostConstruct
    public void Init() {
        solicitudReparacion = crearEntidad();
        //persona.setListaSolicitud(new ArrayList<SolicitudReparacion>());
//        listap = servGen.buscarTodos(Persona.class);
    }

    public SolicitudReparacion crearEntidad() {
        Date fecha = Calendar.getInstance().getTime();
        //TipoAtributo ta = seg.buscarPorNombre(Persona.class.getName());//llamar el atributo
        SolicitudReparacion solicitudRepa = new SolicitudReparacion();
        solicitudRepa.setFechaCreacion(fecha);
//        vehiculo.setTipoAtributo(ta);//cargar los datos parametrizables 
//        vehiculo.construirAtributos(seg);
        return solicitudReparacion;
    }
    public String asignarv(Vehiculo v) {
        this.solicitudReparacion.setVehiculo(v);
        

        return "/Solicitudes/crearEditar?faces-redirect=true";
    }
    public String asignarv1() {
        
        return "/Solicitudes/listaAVehiculos?faces-redirect=true";
    }

}
