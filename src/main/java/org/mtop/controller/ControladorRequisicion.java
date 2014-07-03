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
import org.mtop.model.Requisicion;
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

public class ControladorRequisicion implements Serializable {

    @Inject //para poder utilizar los servicios 
    private ServicioGenerico servGen;

    private Requisicion requisicion=new Requisicion();
    
    @Inject //para cargar tipos de atributos de cada clase
    private ServicioEntidadGenerica seg;
    Integer c=0;
    List<ItemRequisicion> listaitem=new ArrayList<ItemRequisicion>();
    Vehiculo vehiculo=new Vehiculo();
    
    private Long id;
    ItemRequisicion itemR=new ItemRequisicion();
    
    @Inject
    Event<Requisicion> evento;
    private String titulo;

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<ItemRequisicion> getListaitem() {
        return listaitem;
    }

    public void setListaitem(List<ItemRequisicion> listaitem) {
        this.listaitem = listaitem;
    }
    
    
    public Long getId() {
        return id;
    }

    public ItemRequisicion getItemSR() {
        return itemR;
    }

    public void setItemR(ItemRequisicion itemR) {
        this.itemR = itemR;
    }

    public void setId(Long id) {
        this.id = id;
        
        if(id!=null){
            requisicion= servGen.buscarPorId(Requisicion.class, id) ;
        }
    }

    public Requisicion getRequisicion() {
        return requisicion;
    }

    public void setRequisicion(Requisicion requisicion) {
        this.requisicion = requisicion;
    }

    //metodos acciones
    //si en vista se desea guardar y k dede een la misma vista el metoso no devuelve nada
    //si en vista kiero redireccionar a otra vista return ..>ruta
    @TransactionAttribute
    public String guardar() {
        requisicion.setFechaActualizacion(new Date());
        try {
            if (requisicion.getId() != null) {
                servGen.actualizar(requisicion);
            } else {
                requisicion.setEstado(true);
                servGen.crear(requisicion);
                System.out.println("Guardo exitosamente");
            }
            Init();
            
            evento.fire(requisicion);
        } catch (Exception e) {
            e.printStackTrace();//muestra errores internos que se genera
            System.out.println("Error al guardar");
        }
        return "/paginas/Vehiculo/lista?faces-redirec=true";

    }

    public Integer getC() {
        return c;
    }

    public void setC(Integer c) {
        this.c = c;
    }
    
    public String guardarItem(){
        c++;
        System.out.println("lista ini"+this.listaitem.toString());
        this.listaitem.add(itemR);
        System.out.println("lista desp"+this.listaitem.toString());
        
        return "/paginas/Requisicion/crearEditar1?faces-redirec=true";
    }
     public String guardarItem2(){
        c++;
        System.out.println("lista ini"+this.listaitem.toString());
        this.listaitem.add(itemR);
        System.out.println("lista desp"+this.listaitem.toString());
        
        return "/paginas/Requisicion/crearEditar2?faces-redirec=true";
    }
    public String editarItem(ItemRequisicion item){
        System.out.println("item>>>"+ item.getCantidad());
        itemR=item;
        return "/paginas/Requisicion/crearEditarItem?faces-redirec=true";
    }
    public String crearItem(){
        System.out.println("entra a reeireccionr");
        c++;
        itemR=new ItemRequisicion();
        return "/paginas/Requisicion/crearEditarItem?faces-redirec=true";
    }
    public String asignarVehiculo(){
        c++;
        itemR=new ItemRequisicion();
        return "/paginas/Requisicion/listaAVehiculos?faces-redirec=true";
    }
    
    

    @PostConstruct
    public void Init() {
        requisicion = crearEntidad();
        //persona.setListaSolicitud(new ArrayList<SolicitudReparacion>());
//        listap = servGen.buscarTodos(Persona.class);
    }

    public Requisicion crearEntidad() {
        Date fecha = Calendar.getInstance().getTime();
        //TipoAtributo ta = seg.buscarPorNombre(Persona.class.getName());//llamar el atributo
        Requisicion requisicions = new Requisicion();
        requisicions.setFechaCreacion(fecha);
//        vehiculo.setTipoAtributo(ta);//cargar los datos parametrizables 
//        vehiculo.construirAtributos(seg);
        return requisicion;
    }
    public String asignarv(Vehiculo v) {
        this.requisicion.setVehiculo(v);
        

        return "/Requisicion/crearEditar1?faces-redirect=true";
    }
    public String asignarv1() {
        
        return "/Requisicion/listaAVehiculos?faces-redirect=true";
    }

}
