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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.faces.application.FacesMessage;

import javax.faces.context.FacesContext;

import javax.inject.Inject;

import javax.persistence.EntityManager;
import org.mtop.cdi.Web;
import org.mtop.controlador.dinamico.BussinesEntityHome;
import org.mtop.modelo.dinamico.BussinesEntityType;
import org.mtop.modelo.PartidaContabilidad;
import org.mtop.modelo.Requisicion;
import org.mtop.modelo.Vehiculo;

import org.mtop.servicios.ServicioGenerico;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import org.mtop.modelo.Auxiliar;
import org.mtop.modelo.AuxiliarParReq;
import org.mtop.modelo.AuxiliarParReq_;
import org.mtop.modelo.Auxiliar_;
import org.mtop.modelo.ItemRequisicion;
import org.mtop.modelo.Producto;
import org.mtop.modelo.SolicitudReparacionMantenimiento;
import org.mtop.modelo.dinamico.BussinesEntityAttribute;
import org.mtop.modelo.dinamico.Property;
import org.mtop.modelo.profile.Profile;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author carla
 * @author yesica
 * 
 */
@Named
@ViewScoped
public class ControladorRequisicion extends BussinesEntityHome<Requisicion> implements Serializable {

    @Inject
    @Web
    private EntityManager em;
    @Inject
    private ServicioGenerico servgen;
    List<Requisicion> listaRequisicion = new ArrayList<Requisicion>();
    List<Requisicion> listaRequisicion2 = new ArrayList<Requisicion>();
    private Long idVehiculo;
    private Vehiculo vehiculo;
    private long idPersonals = 0l;
    private long idPersonala = 0l;
    private String numeroRequisicion;
    private Integer maximo;
    private ControladorItemRequisicion cir = new ControladorItemRequisicion();
    List<Producto> listaProductos = new ArrayList<Producto>();
    List<Profile> listaPersonal;
    private String palabrab;
    private String palabrabv = "";
    private String palabrabp = "";
    private String palabrabs = "";
    private String valorTipo;
    private ItemRequisicion itemr;
    private List<SolicitudReparacionMantenimiento> listaSolicitudes;
    private List<SolicitudReparacionMantenimiento> listaSolicitudes2;
    private SolicitudReparacionMantenimiento solicitudrep;
    List<ItemRequisicion> listaItemsRequisicion = new ArrayList<ItemRequisicion>();
    Producto pro;
    List<Requisicion> listaRequisicionAprobada = new ArrayList<Requisicion>();
    private String tipo;
    private String tipor;
    private String vista;
    private SolicitudReparacionMantenimiento solRequisicion;
    private SolicitudReparacionMantenimiento solicitudReparacionMantenimiento;
    private List<SolicitudReparacionMantenimiento> listaAux;
    private List<ItemRequisicion> itemsEliminar;
    private ItemRequisicion it;
    private Double total = 0.0;
    private List<Producto> listaproductos2 = new ArrayList<Producto>();
    private List<Profile> listadePersonas;
    private String mensaje = "";
    private String nombrew = "";
    private String aprobada;
    private List<PartidaContabilidad> listaPartida;
    private Integer numr = 0;

    private Property propertyRepa;
    private Property propertyBien;
    private String palbpartida;
    private List<PartidaContabilidad> listapartida2;
//    private String mensajei="";
    private List<String> listaPartidaAgregar;

    public String getPalbpartida() {
        return palbpartida;
    }

    public void setPalbpartida(String palbpartida) {
        this.palbpartida = palbpartida;
    }

    public Integer getNumr() {
        return numr;
    }

    public void setNumr(Integer numr) {
        this.numr = numr;
    }

    public String obtenernombre(long idP) {
        Profile nombrePersona;
        System.out.println("nllegas id>" + idP);
        String nombPersona = "";
        if (idP != 0) {
            nombrePersona = servgen.buscarPorId(Profile.class, idP);
            nombPersona = nombrePersona.concatenarNombre();

        }

        return nombPersona;
    }

    public String obtenerpartida(String idP) {
        PartidaContabilidad nombrePersona;
        System.out.println("nllegas id>" + idP);
        String nombPersona;

        nombrePersona = servgen.buscarPorId(PartidaContabilidad.class, Long.valueOf(idP));
        nombPersona = nombrePersona.concatenarPartida();

        return nombPersona;
    }

    public Integer obtenernumero() {
        numr = numr + 1;
        return numr;
    }

    public List<PartidaContabilidad> obtenerListaPar() {
        System.out.println("entro al metodo con lista" + listaPartidaAgregar);
        List<PartidaContabilidad> lp = new ArrayList<PartidaContabilidad>();
        if (!listaPartidaAgregar.isEmpty()) {

            System.out.println("entro al metodo al if" + listaPartidaAgregar);

            for (String partida1 : listaPartidaAgregar) {
                System.out.println("patida a presentar");
                System.out.println("patida a presentar" + partida1);
                System.out.println("patida a presentar" + findById(PartidaContabilidad.class, Long.valueOf(partida1)).concatenarPartida().toString());
                lp.add(findById(PartidaContabilidad.class, Long.valueOf(partida1)));
            }

            return lp;
        } else {
            System.out.println("retorna sss vacio");
            return lp;
        }
    }

    public List<String> getListaPartidaAgregar() {
        return listaPartidaAgregar;
    }

    public void setListaPartidaAgregar(List<String> listaPartidaAgregar) {
        System.out.println("setpatida agregar" + listaPartidaAgregar);
        this.listaPartidaAgregar = listaPartidaAgregar;
    }

    public List<Profile> getListadePersonas() {
        System.out.println("getpatida agregar" + listaPartidaAgregar);
        return listadePersonas;
    }

    public void setListadePersonas(List<Profile> listadePersonas) {
        this.listadePersonas = listadePersonas;
    }

    public String getNombrew() {
        return nombrew;
    }

    public void setNombrew(String nombrew) {
        this.nombrew = nombrew;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getAprobada() {

        return aprobada;
    }

    public void setAprobada(String aprobada) {
        this.aprobada = aprobada;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public ItemRequisicion getIt() {
        return it;
    }

    public void setIt(ItemRequisicion it) {
        this.it = it;

        if (it.getProducto() != null) {
            pro = it.getProducto();
            int j = 0;
            for (Producto p : listaProductos) {
                System.out.println("cantidad " + p.getCantidad());
                if (p.getId().equals(pro.getId())) {

                    break;
                }
                j++;
            }

            pro.setCantidad(it.getCantidad() + listaProductos.get(j).getCantidad());
            listaProductos.set(j, pro);

            pro = listaProductos.get(j);
            maximo = pro.getCantidad();
            System.out.println("maximo en set it" + maximo);
        }
        this.it = it;
    }
//public void fijarString(){
//            System.out.println("entro a fijar it vacios " );
//            it.setCantidad(0);
//            it.setUnidadMedida(" ");
//            it.setDescripcion(" ");
//    }
    public void editar() {
        System.out.println("llego a editar " + it.getDescripcion());

        if (pro != null) {

            if (!pro.getCodigo().equals("")) {

                Producto p = new Producto();
                List<Producto> lp = new ArrayList<Producto>();
                for (Producto prod : listaProductos) {
                    if (!prod.getId().equals(pro.getId())) {
                        lp.add(prod);

                    }
                }
                listaProductos = lp;
                pro.setCantidad(pro.getCantidad() - it.getCantidad());

                listaProductos.add(pro);
            }
        }
        it = new ItemRequisicion();

        it.setCantidad(0);
        it.setUnidadMedida(" ");
        it.setDescripcion(" ");

    }

    public List<Producto> getListaproductos2() {
        return listaproductos2;
    }

    public void setListaproductos2(List<Producto> listaproductos2) {
        this.listaproductos2 = listaproductos2;
    }

    public Double getTotal() {
        total=0.0;
        for (ItemRequisicion ir : listaItemsRequisicion) {
            total = total + (ir.getCantidad() * ir.getProducto().getCosto());
        }

        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<ItemRequisicion> getItemsEliminar() {
        return itemsEliminar;
    }

    public void setItemsEliminar(List<ItemRequisicion> itemsEliminar) {
        this.itemsEliminar = itemsEliminar;
    }

    public void eliminarItemS(ItemRequisicion itemreq) {
        List<ItemRequisicion> li = new ArrayList<ItemRequisicion>();
        for (ItemRequisicion items : listaItemsRequisicion) {

            if (!items.getCantidad().equals(itemreq.getCantidad())
                    && !items.getDescripcion().equals(itemreq.getDescripcion())
                    && !items.getUnidadMedida().equals(itemreq.getUnidadMedida())) {

                li.add(items);

            } else {
                if (items.isPersistent()) {
                    itemsEliminar.add(items);

                }

                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", " Item " + items.getDescripcion() + " dado de baja con éxito ");
                FacesContext.getCurrentInstance().addMessage("", msg);

            }

        }
        listaItemsRequisicion = li;
        System.out.println("lista nueva " + listaItemsRequisicion);
        getInstance().setListaItems(li);

    }

    public String getTipor() {
        return tipor;
    }

    public void setTipor(String tipor) {
        this.tipor = tipor;

    }

    public List<SolicitudReparacionMantenimiento> getListaAux() {
        return listaAux;
    }

    public void setListaAux(List<SolicitudReparacionMantenimiento> listaAux) {
        this.listaAux = listaAux;
        this.limpiar();
    }

//    @TransactionAttribute
//    public void guardarSol(SolicitudReparacionMantenimiento solis) {
//        Date now = Calendar.getInstance().getTime();
//        List<ItemSolicitudReparacion> lir = new ArrayList<ItemSolicitudReparacion>();
//        for (ItemSolicitudReparacion apm : solis.getListaItemSR()) {
//
//            apm.setSolicitudReparacion(solis);//fijarle un plan de mantenimiento a cada actividad de plan de mantenimiento
//
//            lir.add(apm);
//            servgen.actualizar(apm);
//
//        }
//
//        solis.setListaItemSR(lir);//fija la lista de actividades a la solicitud
//
//        BussinesEntityType _type1 = bussinesEntityService.findBussinesEntityTypeByName(SolicitudReparacionMantenimiento.class.getName());
//
//        solis.setCreatedOn(now);
//        solis.setLastUpdate(now);
//        solis.setActivationTime(now);
//        solis.setType(_type1);
//        solis.buildAttributes(bussinesEntityService);  //
//        create(solis);
//        save(solis);
//        listaSolicitudes.add(solis);
//
//    }
    public ItemRequisicion getItemr() {
        return itemr;
    }

    public void setItemr(ItemRequisicion itemr) {
        this.itemr = itemr;
        cir.setInstance(itemr);
        agregarItem();
    }

    public String getVista() {
        return vista;

    }

    public void setVista(String vista) {

        this.vista = vista;
    }

    public SolicitudReparacionMantenimiento getSolRequisicion() {
        return solRequisicion;
    }

    public void setSolRequisicion(SolicitudReparacionMantenimiento solRequisicion) {
        this.solRequisicion = solRequisicion;
    }

    public SolicitudReparacionMantenimiento getSolicitudReparacionMantenimiento() {
        return solicitudReparacionMantenimiento;
    }

    public void setSolicitudReparacionMantenimiento(SolicitudReparacionMantenimiento solicitudReparacionMantenimiento) {
        this.solicitudReparacionMantenimiento = solicitudReparacionMantenimiento;

        limpiars();

    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        List<Requisicion> lr = new ArrayList<Requisicion>();
        List<Requisicion> lr2 = new ArrayList<Requisicion>();
        System.out.println("tipo a fijar" + tipo);
        if ("repa".equals(tipo)) {
            System.out.println("lissta requiciiones " + listaRequisicion);
            for (Requisicion requisicion : listaRequisicion) {
                if (requisicion.getTipoRequisicion().equals("Requisición de Reparación")) {

                    lr.add(requisicion);
                }
            }
            setListaRequisicion(lr);

            System.out.println("lista requisiciones aprobadas" + listaRequisicionAprobada);
            for (Requisicion requisicion : listaRequisicionAprobada) {
                if (requisicion.getTipoRequisicion().equals("Requisición de Reparación")) {

                    lr2.add(requisicion);
                }
            }
            setListaRequisicionAprobada(lr2);

        } else {
            if ("bien".equals(tipo)) {

                for (Requisicion requisicion : listaRequisicion) {
                    if (requisicion.getTipoRequisicion().equals("Requisición de Bienes y Servicios")) {
                        lr.add(requisicion);
                    }
                }
                setListaRequisicion(lr);

                for (Requisicion requisicion : listaRequisicionAprobada) {
                    if (requisicion.getTipoRequisicion().equals("Requisición de Bienes y Servicios")) {
                        lr2.add(requisicion);
                    }
                }
                setListaRequisicionAprobada(lr2);
            }

        }
        System.out.println("lrrrrr>>" + lr);
        this.tipo = tipo;
    }

    public String getValorTipo() {
        return valorTipo;
    }

    public void setValorTipo(String valorTipo) {
        this.valorTipo = valorTipo;

        List<Requisicion> lr = new ArrayList<Requisicion>();
        List<Requisicion> lr1 = new ArrayList<Requisicion>();
        if (valorTipo.equals("repa")) {
            for (Requisicion r : listaRequisicion) {
                if (r.getTipoRequisicion().equals("Requisición de Reparación")) {
                    lr.add(r);
                }
            }
            for (Requisicion requis : listaRequisicionAprobada) {
                if (requis.getTipoRequisicion().equals("Requisición de Reparación")) {
                    lr1.add(requis);
                }
            }

        } else {
            if (valorTipo.equals("bien")) {
                for (Requisicion r : listaRequisicion) {
                    if (r.getTipoRequisicion().equals("Requisición de Bienes y Servicios")) {
                        lr.add(r);
                    }
                }
                for (Requisicion r : listaRequisicionAprobada) {
                    if (r.getTipoRequisicion().equals("Requisición de Bienes y Servicios")) {
                        lr1.add(r);
                    }
                }
            }

        }
        if (!lr.isEmpty()) {
            listaRequisicion = lr;
        }
        if (!lr1.isEmpty()) {
            listaRequisicionAprobada = lr1;
        }

    }

    public void fijarvalorTipo(String s) {

        setValorTipo(s);
    }

    public List<Requisicion> getListaRequisicionAprobada() {
        return listaRequisicionAprobada;
    }

    public void setListaRequisicionAprobada(List<Requisicion> listaRequisicionAprobada) {
        this.listaRequisicionAprobada = listaRequisicionAprobada;
    }

    public String getPalabrabs() {
        return palabrabs;
    }

    public void setPalabrabs(String palabrabs) {
        this.palabrabs = palabrabs;
    }

    public String getPalabrabp() {
        return palabrabp;
    }

    public void setPalabrabp(String palabrabp) {
        this.palabrabp = palabrabp;
    }

    public String getPalabrabv() {
        return palabrabv;
    }

    public void setPalabrabv(String palabrabv) {
        this.palabrabv = palabrabv;
    }

    public void darDeBaja(Requisicion requisicion) {

        Date now = Calendar.getInstance().getTime();

        listaProductos.clear();
        setRequisicionId(requisicion.getId());

        getInstance().setSolicitudReparacionId(null);

        getInstance().setEstado(false);
        getInstance().setLastUpdate(now);
        servgen.actualizar(getInstance());

        List<Requisicion> lrqn = findAll(Requisicion.class);
        listaRequisicion2 = new ArrayList<Requisicion>();
        listaRequisicion.clear();
        for (Requisicion requisicion1 : lrqn) {
            if (requisicion1.isEstado()) {
                if (!requisicion1.getAprobado()) {
                    listaRequisicion.add(requisicion1);
                }
                listaRequisicion2.add(requisicion1);
            }

        }

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "La requisición " + requisicion.getNumRequisicion() + " ha sido dada de baja"));

    }

    public ArrayList<String> autocompletarpartida(String query) {
        System.out.println("QUEryyyyy" + query);
        System.out.println("QUEryyyyy" + palbpartida);
        ArrayList<String> ced = new ArrayList<String>();
        for (PartidaContabilidad partidaContabilidad : listaPartida) {

            String resultado = partidaContabilidad.concatenarPartida();

            if (resultado.contains(query)) {

                if (!ced.contains(partidaContabilidad.concatenarPartida())) {
                    ced.add(resultado);
                }
            } else {
                if (partidaContabilidad.getDescripcion().toLowerCase().contains(query.toLowerCase()) && !ced.contains(partidaContabilidad.getDescripcion().toLowerCase())) {
                    ced.add(partidaContabilidad.getDescripcion());
                }
            }
        }

        System.out.println("listaaaaa rretorn lista" + ced);
        return ced;

    }

    public void buscarpartida() {
        System.out.println("entro a buscar patida" + palbpartida);

        palbpartida = palbpartida.trim();
        if (palbpartida == null || palbpartida.equals("")) {
            palbpartida = "Ingrese algun valor a buscar";
        }
        //buscando por coincidencia
        List<PartidaContabilidad> lp = new ArrayList<PartidaContabilidad>();
        //buscando por numero de partidalistaPartidaC
        System.out.println("lista partida" + listaPartida);
        for (PartidaContabilidad p : listaPartida) {
            String resultado = p.concatenarPartida();
            if (p.getDescripcion().toLowerCase().contains(palbpartida.toLowerCase())) {
                lp.add(p);
            } else {
                if (resultado.contains(palbpartida)) {
                    lp.add(p);
                }
            }
        }

        System.out.println("lp" + lp);
        if (lp.isEmpty()) {

            if (palbpartida.equals("Ingrese algun valor a buscar")) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN:", " Ingrese algun valor a buscar");
                FacesContext.getCurrentInstance().addMessage("", msg);
                palbpartida = " ";
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN:No se ha encontrado", palbpartida);
                FacesContext.getCurrentInstance().addMessage("", msg);
            }

        } else {
            listaPartida = lp;
        }
        System.out.println("salio de buscar" + listaPartida);

    }

    public void limpiarpartida() {
        palbpartida = "";
        listaPartida = listapartida2;
    }

    public ArrayList<String> autocompletarp(String query) {
        System.out.println("QUEryyyyy" + query);

        ArrayList<String> ced = new ArrayList<String>();
        for (Producto producto : listaproductos2) {
            if (producto.isEstado()) {
                if (producto.getDescripcion().toLowerCase().contains(query.toLowerCase())) {
                    ced.add(producto.getDescripcion());
                }
                if (producto.getCodigo().contains(query)) {
                    ced.add(producto.getCodigo());
                }

            }
        }

        System.out.println("listaaaaa autocompletar" + ced);
        return ced;

    }

    public void buscarp() {
        palabrabp = palabrabp.trim();
        if (palabrabp == null || palabrabp.equals("")) {
            palabrabp = "Ingrese algun valor a buscar";
        }
        List<Producto> lp = new ArrayList<Producto>();
        //buscando por coincidencia descripciion

        for (Producto p : listaproductos2) {
            if (p.getCodigo().contains(palabrabp)) {
                lp.add(p);
            } else {
                if (p.getDescripcion().toLowerCase().contains(palabrabp.toLowerCase())) {
                    lp.add(p);
                }
            }
        }

        if (lp.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrabp.equals("Ingrese algun valor a buscar")) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN:", " Ingrese algun valor a buscar");
                FacesContext.getCurrentInstance().addMessage("", msg);
                palabrabp = " ";
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: No se ha encontrado", palabrabp);
                FacesContext.getCurrentInstance().addMessage("", msg);
            }

        } else {

            for (Producto productol : lp) {
                if (productol.isEstado() && !productol.getCantidad().equals(0)) {
                    listaProductos = lp;
                }
            }

        }

    }

    public void limpiarp() {
        palabrabp = "";
        listaProductos = listaproductos2;
    }

    public List<Profile> getListaPersonal() {
        return listaPersonal;
    }

    public void setListaPersonal(List<Profile> listaPersonal) {
        this.listaPersonal = listaPersonal;
    }

    public List<ItemRequisicion> getListaItemsRequisicion() {
        if (getInstance().getTipoAdquisicion() != null) {

            if (getInstance().getTipoAdquisicion().equals("bodega")) {
                List<ItemRequisicion> lirq = listaItemsRequisicion;
                for (ItemRequisicion itemRequisicion : lirq) {
                    if (itemRequisicion.getProducto() == null) {
                        listaItemsRequisicion.clear();
                        break;
                    }
                }

            }
        }
        System.out.println("paso de if");
        System.out.println("rretorn lista de items" + listaItemsRequisicion);
        System.out.println("get vehiculo " + getInstance().getVehiculo());
        System.out.println("get vehiculo " + vehiculo);
        return listaItemsRequisicion;
    }

    public void setListaItemsRequisicion(List<ItemRequisicion> listaItemsRequisicion) {

        this.listaItemsRequisicion = listaItemsRequisicion;
    }

    public SolicitudReparacionMantenimiento getSolicitudrep() {

        return solicitudrep;
    }

    public void setSolicitudrep(SolicitudReparacionMantenimiento solicitudrep) {

        if (solicitudrep != null) {
            this.solicitudrep = solicitudrep;
            getInstance().setSolicitudReparacionId(solicitudrep);
        } else {
           Boolean ban=true;
            for (SolicitudReparacionMantenimiento sol : listaSolicitudes) {
               if(sol.getId().equals(getInstance().getSolicitudReparacionId().getId())){
                    ban=false;
                }
            }
            if(ban){
             
                listaSolicitudes.add(getInstance().getSolicitudReparacionId());
           
            }
            this.solicitudrep = new SolicitudReparacionMantenimiento();
        }

    }

    public List<SolicitudReparacionMantenimiento> getListaSolicitudes() {

        return listaSolicitudes;
    }

    public void setListaSolicitudes(List<SolicitudReparacionMantenimiento> listaSolicitudes) {

        this.listaSolicitudes = listaSolicitudes;

    }

    public void buscar() {
        palabrab = palabrab.trim();
        if (palabrab == null || palabrab.equals("")) {
            palabrab = "Ingrese algun valor a buscar";
        }

        List<Long> lrq = new ArrayList<Long>();
        ControladorVehiculo cv = new ControladorVehiculo();
        cv.setBussinesEntity(bussinesEntity);
        cv.setEntityManager(em);
        for (Requisicion r : listaRequisicion2) {
            List<AuxiliarParReq> lpc = servgen.buscarAuxiliarPorIdReqPa(AuxiliarParReq_.requisicionId.getName(), r.getId());
            for (AuxiliarParReq ar : lpc) {
                if (ar.getPartidaId().concatenarPartida().contains(palabrab) && !lrq.contains(r.getId())) {
                    lrq.add(r.getId());
                }
            }

            System.out.println("r.id" + r.getId());
            System.out.println("contaisns" + !lrq.contains(r.getId()));
            if (r.isEstado() && !lrq.contains(r.getId())) {
                String s = r.getFechaRequisicion().toString();

                if (r.getNumRequisicion().contains(palabrab) || s.contains(palabrab)) {
                    lrq.add(r.getId());
                }

                if (r.getVehiculo() != null) {

                    System.out.println("lista de lrq" + lrq);
                    System.out.println("numero de requistro del vehiculo" + r.getVehiculo().getNumRegistro());
                    System.out.println("no lo contiene " + !lrq.contains(r.getId()));
                    System.out.println("palabrab" + palabrab);
                    if (r.getVehiculo().getNumRegistro().equals(palabrab)
                            && !lrq.contains(r.getId())) {
                        System.out.println("agrego " + r.getId());
                        lrq.add(r.getId());

                    }
                    if (r.getVehiculo().getPlaca().toLowerCase().contains(palabrab.toLowerCase())
                            && !lrq.contains(r.getId())) {

                        lrq.add(r.getId());

                    }
                    List<BussinesEntityAttribute> bea = new ArrayList<BussinesEntityAttribute>();

                    cv.setId(r.getVehiculo().getId());
                    bea = cv.getInstance().findBussinesEntityAttribute("Motor");
                    for (BussinesEntityAttribute bussinesEntityAttribute : bea) {

                        if (bussinesEntityAttribute.getName().equals("serieMotor")) {
                            if (((String) bussinesEntityAttribute.getValue()).equals(palabrab) && !lrq.contains(r.getId())) {

                                lrq.add(r.getId());

                            }

                        }
                    }
                    bea = cv.getInstance().findBussinesEntityAttribute("Chasis");
                    for (BussinesEntityAttribute bussinesEntityAttribute : bea) {
                        if (bussinesEntityAttribute.getName().equals("serieChasis")) {

                            if (((String) bussinesEntityAttribute.getValue()).equals(palabrab) && !lrq.contains(r.getId())) {

                                lrq.add(r.getId());

                            }

                        }
                    }

                }

            }

        }

        System.out.println("LEeeee" + lrq);

        if (lrq.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrab.equals("Ingrese algun valor a buscar")) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN:", " Ingrese algun valor a buscar");
                FacesContext.getCurrentInstance().addMessage("", msg);
                palabrab = " ";
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: No se ha encontrado", palabrab);
                FacesContext.getCurrentInstance().addMessage("", msg);
            }

        } else {
            listaRequisicion.clear();
            listaRequisicionAprobada.clear();
            Requisicion re = new Requisicion();
            for (Long r : lrq) {
                re = findById(Requisicion.class, r);
                if (re.isEstado()) {
                    if (re.getAprobado()) {
                        if (!listaRequisicionAprobada.contains(re)) {

                            listaRequisicionAprobada.add(re);
                        }

                    } else {
                        if (!listaRequisicion.contains(re)) {
                            listaRequisicion.add(re);
                        }

                    }
                }
            }

            palabrab = "";
        }
        System.out.println("lista de  requisiciones Aprobadas" + listaRequisicionAprobada);
        System.out.println("lista de  requisiciones no Aprobadas" + listaRequisicion);
    }

    public void limpiar() {

        palabrab = "";

        listaRequisicion.clear();
        listaRequisicionAprobada.clear();

        for (Requisicion r : listaRequisicion2) {
            if (r.isEstado()) {
                if (r.getAprobado()) {
                    listaRequisicionAprobada.add(r);
                } else {
                    System.out.println("listatesssa" + listaRequisicion);
                    listaRequisicion.add(r);
                }

            }

        }
    }

    public String getPalabrab() {
        return palabrab;
    }

    public void setPalabrab(String palabrab) {
        this.palabrab = palabrab;
    }

    public ArrayList<String> autocompletar(String query) {
        System.out.println("QUEryyyyy" + query);

        ArrayList<String> ced = new ArrayList<String>();
        ControladorVehiculo cv = new ControladorVehiculo();
        cv.setBussinesEntity(bussinesEntity);
        cv.setEntityManager(em);

        for (Requisicion requisicion : listaRequisicion2) {

            List<AuxiliarParReq> lpc = servgen.buscarAuxiliarPorIdReqPa(AuxiliarParReq_.requisicionId.getName(), requisicion.getId());
            for (AuxiliarParReq ar : lpc) {
                if (ar.getPartidaId().concatenarPartida().contains(query) && !ced.contains(ar.getPartidaId().concatenarPartida())) {
                    ced.add(ar.getPartidaId().concatenarPartida());
                }
            }

            if (requisicion.isEstado() && requisicion.getNumRequisicion().contains(query)) {
                ced.add(requisicion.getNumRequisicion());
            }

            if (requisicion.getVehiculo() != null) {
                if (requisicion.getVehiculo().getNumRegistro().contains(query) && !ced.contains(requisicion.getVehiculo().getNumRegistro())) {
                    ced.add(requisicion.getVehiculo().getNumRegistro());
                }
                if (requisicion.getVehiculo().getPlaca().toLowerCase().contains(query.toLowerCase()) && !ced.contains(requisicion.getVehiculo().getPlaca())) {
                    ced.add(requisicion.getVehiculo().getPlaca());
                }
                List<BussinesEntityAttribute> bea = new ArrayList<BussinesEntityAttribute>();
                cv.setId(requisicion.getVehiculo().getId());
                bea = cv.getInstance().findBussinesEntityAttribute("Motor");

                for (BussinesEntityAttribute bussinesEntityAttribute : bea) {

                    if (bussinesEntityAttribute.getName().equals("serieMotor")) {
                        if (((String) bussinesEntityAttribute.getValue()).toLowerCase().contains(query.toLowerCase())
                                && !ced.contains((String) bussinesEntityAttribute.getValue())) {
                            ced.add((String) bussinesEntityAttribute.getValue());

                        }

                    }
                }
                bea = cv.getInstance().findBussinesEntityAttribute("Chasis");
                for (BussinesEntityAttribute bussinesEntityAttribute : bea) {
                    if (bussinesEntityAttribute.getName().equals("serieChasis")) {
                        if (((String) bussinesEntityAttribute.getValue()).toLowerCase().contains(query.toLowerCase()) && !ced.contains((String) bussinesEntityAttribute.getValue())) {
                            ced.add((String) bussinesEntityAttribute.getValue());

                        }

                    }
                }

            }

        }
        String s = "";
        for (Requisicion requisicion : listaRequisicion2) {
            s = requisicion.getFechaRequisicion().toString();
            if (s.contains(query) && requisicion.isEstado() && !ced.contains(s)) {
                ced.add(requisicion.getFechaRequisicion().toString());
            }
        }

        System.out.println("listaaaaa autocompletar" + ced);
        return ced;

    }

    public void buscars() {
        palabrabs = palabrabs.trim();
        if (palabrabs == null || palabrabs.equals("")) {
            palabrabs = "Ingrese algun valor a buscar";
        }
        List<Long> lsoli = new ArrayList<Long>();
        for (SolicitudReparacionMantenimiento sol : listaSolicitudes2) {
            if (sol.isEstado() && !lsoli.contains(sol.getId())) {
                String s = sol.getFechaSolicitud().toString();
                if (sol.getNumSolicitud().contains(palabrabs) || s.contains(palabrabs)) {
                    lsoli.add(sol.getId());
                }

            }

        }

        if (lsoli.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrabs.equals("Ingrese algun valor a buscar")) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN:", " Ingrese algun valor a buscar");
                FacesContext.getCurrentInstance().addMessage("", msg);
                palabrabs = " ";
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: No se ha encontrado", palabrabs);
                FacesContext.getCurrentInstance().addMessage("", msg);
            }

        } else {
            listaSolicitudes.clear();
            SolicitudReparacionMantenimiento solRM = new SolicitudReparacionMantenimiento();
            for (Long s : lsoli) {
                solRM = findById(SolicitudReparacionMantenimiento.class, s);
                if (solRM.getRequisicionId() == null && solRM.isEstado() && solRM.getVehiculo().getId().equals(getInstance().getVehiculo().getId())) {

                    listaSolicitudes.add(solRM);

                }
            }

            palabrabs = "";
        }

    }

    public void limpiars() {
        palabrabs = "";
        listaSolicitudes.clear();
        for (SolicitudReparacionMantenimiento soli : listaSolicitudes2) {
            if (soli.isEstado() && soli.getRequisicionId() == null && soli.getVehiculo().getId().equals(getInstance().getVehiculo().getId())) {

                listaSolicitudes.add(soli);

            }

        }
    }

    public ArrayList<String> autocompletars(String query) {
        ArrayList<String> ced = new ArrayList<String>();

        String s = "";
        for (SolicitudReparacionMantenimiento soli : listaSolicitudes2) {
            if (soli.isEstado()
                    && soli.getVehiculo().getId().equals(getInstance().getVehiculo().getId())
                    && soli.getRequisicionId() == null) {
                s = soli.getFechaSolicitud().toString();
                if (soli.getNumSolicitud().contains(query)
                        && !ced.contains(soli.getNumSolicitud())) {
                    ced.add(soli.getNumSolicitud());
                }
                if (s.contains(query) && !ced.contains(s)) {
                    ced.add(s);
                }

            }

        }
        return ced;

    }

    public Integer getMaximo() {
        System.out.println("entro a maximo");

        System.out.println("get maximo " + maximo);
        if (getInstance().getTipoAdquisicion() != null) {
            System.out.println("entroa a tipo dif de null " + getInstance().getTipoAdquisicion());
            if (getInstance().getTipoAdquisicion().equals("bodega")) {
                System.out.println("entro igual a bodega");

                if (pro.getId() != null) {
                    boolean ban = true;
                    System.out.println("cir" + cir.getInstance().getCantidad());
                    System.out.println("prod" + pro);
                    maximo = pro.getCantidad();

                } else {
                    setMaximo(100);
                }

            } else {
                System.out.println("entro a fijar 101");
                maximo = 100;
                System.out.println("paso a fijar 101");
            }

        } else {
            System.out.println("entro a caso contario");
            setMaximo(100);
        }

        System.out.println("maaaaaaaximo" + maximo);
        return maximo;

    }

    public void setMaximo(Integer maximo) {
        this.maximo = maximo;
    }

    public Producto getPro() {
        return pro;
    }

    public void setPro(Producto pro) {
        this.pro = pro;
        cir.getInstance().setDescripcion(pro.getDescripcion());
        cir.getInstance().setCantidad(0);
        cir.getInstance().setUnidadMedida(" ");
        cir.getInstance().setDescripcion(pro.getDescripcion());
        cir.getInstance().setProducto(pro);
        maximo = pro.getCantidad();
        palabrab = "";

    }

    public void guardarProducto(Producto p) {
        cir.getInstance().setProducto(pro);
        pro = cir.getInstance().getProducto();

    }

    public List<Producto> getListaProductos() {
        if (listaProductos.isEmpty()) {

            listaProductos = servgen.buscarTodos(Producto.class);
        }
        return listaProductos;
    }

    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public long getIdPersonals() {

        return idPersonals;
    }

    public void setIdPersonals(long idPersonals) {
        this.idPersonals = idPersonals;

    }

    public long getIdPersonala() {
        return idPersonala;
    }

    public void setIdPersonala(long idPersonala) {
        this.idPersonala = idPersonala;

    }

    public void guardarItem() {
        listaProductos.clear();
        for (ItemRequisicion apm : listaItemsRequisicion) {
            if (apm.getProducto() != null) {
                listaProductos.add(apm.getProducto());
            }

            Date now = Calendar.getInstance().getTime();
            apm.setRequisicion(getInstance());//fijarle un plan de mantenimiento a cada actividad de plan de mantenimiento
            cir.setInstance(apm);//fija la actividad del plan de mantenimiento al controlador de actividad de plan de mantenimiento

            if (getInstance().isPersistent()) {
                cir.getInstance().setLastUpdate(now);
                cir.guardar();

            } else {
                BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(ItemRequisicion.class.getName());

                apm.setCreatedOn(now);
                apm.setLastUpdate(now);
                apm.setActivationTime(now);
                apm.setType(_type);
                apm.buildAttributes(bussinesEntityService);  //
                cir.setInstance(apm);
                cir.guardar();
            }

        }
        if (!listaProductos.isEmpty()) {
            for (Producto pr : listaProductos) {
                Date now = Calendar.getInstance().getTime();
                pr.setLastUpdate(now);
                save(pr);
            }
        }
        getInstance().setListaItems(listaItemsRequisicion);//fija la lista de actividades al plan de mantenimietno

        for (ItemRequisicion isr : itemsEliminar) {
            delete(isr);
        }
        System.out.println("termino de gusradar" + getInstance().getListaItems());
    }

    public void guardarRelacion() {

        Auxiliar auxiliar = new Auxiliar();
        auxiliar.setRequisicionId(getInstance());
        auxiliar.setPersonalId(servgen.buscarPorId(Profile.class, idPersonals));
        auxiliar.setTipoRelacion("solicitadoR");
        save(auxiliar);
        auxiliar.setRequisicionId(getInstance());
        auxiliar.setPersonalId(servgen.buscarPorId(Profile.class, idPersonala));
        auxiliar.setTipoRelacion("aprobadoR");
        save(auxiliar);
        AuxiliarParReq auxpr = new AuxiliarParReq();
        System.out.println("lista part" + listaPartidaAgregar);
        for (String partida : listaPartidaAgregar) {
            System.out.println("id de relacion en guardar" + auxpr.getId());
            auxpr.setPartidaId(servgen.buscarPorId(PartidaContabilidad.class, Long.valueOf(partida)));
            auxpr.setRequisicionId(getInstance());
            save(auxpr);
        }

        System.out.println("termino de guardar relaciones");

    }

    public void agregarItem() {

        String des = cir.getInstance().getDescripcion().trim();
        String uni = cir.getInstance().getUnidadMedida().trim();

        if (cir.getInstance().getCantidad().equals(0) || des.equals("") || uni.equals("")) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR!", " Campos abligatorios, cantidad, descripción y unidad de medida"));

        } else {
            if (pro != null) {

                if (!pro.getCodigo().equals("")) {

                    Producto p = new Producto();
                    List<Producto> lp = new ArrayList<Producto>();
                    for (Producto prod : listaProductos) {

                        if (!prod.getId().equals(pro.getId())) {

                            lp.add(prod);

                        }
                    }
                    listaProductos = lp;

                    pro.setCantidad(pro.getCantidad() - cir.getInstance().getCantidad());
                    listaProductos.add(pro);

                }
            }

            listaItemsRequisicion.add(cir.getInstance());
//            }

            cir.setInstance(new ItemRequisicion());
            cir.getInstance().setCantidad(0);
            cir.getInstance().setUnidadMedida(" ");
            cir.getInstance().setDescripcion(" ");
        }

        pro = new Producto();
        pro.setCodigo("");
        getInstance().setListaItems(listaItemsRequisicion);
        System.out.println("lista items ddd" + listaItemsRequisicion);

    }

    public void eliminarItem(ItemRequisicion itemReq) {

        int con = 0;
        for (ItemRequisicion i : listaItemsRequisicion) {

            if (i.getCantidad().equals(itemReq.getCantidad())
                    && i.getUnidadMedida().equals(itemReq.getUnidadMedida())) {
                listaItemsRequisicion.remove(con);
                if (i.isPersistent()) {
                    itemsEliminar.add(i);
                }
                pro = itemReq.getProducto();

                if (pro != null) {
                    int j = listaProductos.lastIndexOf(pro);
                    pro.setCantidad(itemReq.getCantidad() + listaProductos.get(j).getCantidad());
                    listaProductos.set(j, pro);
                    pro = listaProductos.get(j);
                }

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "ítem " + i.getDescripcion()+" dado de baja"));

                break;

            }
            con++;

        }

    }

    public void setNumeroRequisicion(String numRegistro) {
        this.numeroRequisicion = numRegistro;
        getInstance().setNumRequisicion(this.numeroRequisicion);

    }

    public List<PartidaContabilidad> getListaPartida() {

        return listaPartida;
    }

    public void setListaPartida(List<PartidaContabilidad> listaPartida) {
        this.listaPartida = listaPartida;
    }

    // @Named provides access the return value via the EL variable name "members" in the UI (e.g.
    // Facelets or JSP view)
    private boolean skip;
    
    public String formato(Date fecha) {
        String fechaFormato = "";
        if (fecha != null) {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            fechaFormato = formatter.format(fecha);
        }

        return fechaFormato;

    }

    public ControladorItemRequisicion getCir() {

        return cir;
    }

    public void setCir(ControladorItemRequisicion cir) {
        this.cir = cir;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public String onFlowprocessR(FlowEvent event) {

        if (skip) {
            skip = false;   //reset in case user goes back  

            return "confirm";
        } else {
            if (getInstance().getId() != null) {
                this.cir.setListaItemsRequisicion(getInstance().getListaItems());
            }
            if (event.getNewStep().equals("req1") && event.getOldStep().equals("items1")) {
                return event.getNewStep();
            } else {
                if (event.getOldStep().equals("items1") && this.listaItemsRequisicion.isEmpty()) {

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Debe ingresar al menos un item a la lista"));

                    return event.getOldStep();
                } else {

                    return event.getNewStep();

                }

            }

        }
    }

    public String onFlowProcess(FlowEvent event) {
        nombrew = "soli";
        mensaje = "";
         System.out.println("entro a wizar" );

        System.out.println("lista part" + listaPartidaAgregar);
        if (skip) {
            skip = false;   //reset in case user goes back  
            nombrew = "Final";
            return "confirm";
        } else {
            nombrew = "soli";
            if (event.getNewStep().equals("confirm") && (event.getOldStep().equals("solicitud") || event.getOldStep().equals("items"))) {
                nombrew = "Final";//reset in case user goes back
            }
            if (getInstance().getId() != null) {
                this.cir.setListaItemsRequisicion(getInstance().getListaItems());
            }

            if (event.getNewStep().equals("req") && event.getOldStep().equals("address")) {
                nombrew = "Requisicion";
                return event.getNewStep();
            } else {
                if (event.getNewStep().equals("address") && event.getOldStep().equals("items")) {
                    return event.getNewStep();
                } else {
                    if (event.getOldStep().equals("address")) {
                        if (getInstance().getTipoRequisicion().equals("Requisición de Reparación")) {

                            if (this.vehiculo == null) {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Debe escoger un vehiculo"));
                                return event.getOldStep();

                            } else {
                                if (this.vehiculo.getId() == null) {
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Debe escoger un vehiculo"));
                                    return event.getOldStep();
                                } else {
                                    return event.getNewStep();
                                }

                            }
                        } else {
                            return event.getNewStep();
                        }

                    } else {

                        if (event.getOldStep().equals("items") && this.listaItemsRequisicion.isEmpty()) {

                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", " Debe ingresar al menos un item a la lista"));

                            return event.getOldStep();
                        } else {
                            if (event.getOldStep().equals("req") && listaPartidaAgregar.isEmpty()) {
                                mensaje = "Error! Debe asignar al menos una partida de contabilidad";
                                return event.getOldStep();
                            } else {
                                
                                    System.out.println("retorna new sztep" );

//                    if (event.getNewStep().equals("items") && this.listaItemsRequisicion.isEmpty() ) {
//                        System.out.println("\n\n\n\n entro a regresar\n\n\n\n");
//                        return event.getOldStep();
//                    } else {
                                return event.getNewStep();
                            }
//                    }
                        }
                    }

                }
            }

        }

    }

    public Long getRequisicionId() {
        return (Long) getId();
    }

    //fijar requisicion si viene de editar
    public void setRequisicionId(Long requisicionId) {
        if (requisicionId == null) {
            Date now = Calendar.getInstance().getTime();
            getInstance().setFechaRequisicion(now);
        }

        setId(requisicionId);
        vehiculo = getInstance().getVehiculo();

        if (getInstance().getSolicitudReparacionId() != null) {
            solicitudrep = getInstance().getSolicitudReparacionId();

            solRequisicion = getInstance().getSolicitudReparacionId();
            solRequisicion.setRequisicionId(null);
            System.out.println("requi en set" + solicitudrep);
            System.out.println("sol de esta req" + solicitudrep.getRequisicionId());
            //no quitar estos souts
            System.out.println("otra req " + solRequisicion);
            System.out.println("sol de la otra q " + solRequisicion.getRequisicionId());
        }
        List<AuxiliarParReq> listaAuxParReq = servgen.buscarAuxiliarPorIdReqPa(AuxiliarParReq_.requisicionId.getName(), getInstance().getId());
        listaPartidaAgregar.clear();
        for (AuxiliarParReq parreq : listaAuxParReq) {

            System.out.println("id de relacion" + parreq.getId());
            listaPartidaAgregar.add(parreq.getPartidaId().getId().toString());
            if (!parreq.getPartidaId().isEstado()) {
                listaPartida.add(parreq.getPartidaId());
            }
        }
        System.out.println("lista de listaPartidaAgregar" + listaPartidaAgregar);
        System.out.println("lista de patidas" + listaPartida);

        List<Auxiliar> listaaux = servgen.buscarAuxiliarPorIdReq(Auxiliar_.requisicionId.getName(), getInstance().getId());
        System.out.println("lista aux" + listaaux);

        for (Auxiliar auxiliar : listaaux) {
            System.out.println("tipo de relacion " + auxiliar.getTipoRelacion());
            if (auxiliar.getTipoRelacion().equals("solicitadoR")) {
                idPersonals = auxiliar.getPersonalId().getId();
            }
            if (auxiliar.getTipoRelacion().equals("aprobadoR")) {
                idPersonala = auxiliar.getPersonalId().getId();
            }
        }

        if (getInstance().isPersistent()) {
            System.out.println("entro a obtener lista de items de la requie" + listaItemsRequisicion);
            listaItemsRequisicion = getInstance().getListaItems();
            System.out.println("entro a obtener lista de items de la requie despies" + listaItemsRequisicion);
        }

        List<SolicitudReparacionMantenimiento> lr = findAll(SolicitudReparacionMantenimiento.class);
        List<SolicitudReparacionMantenimiento> lrq = new ArrayList<SolicitudReparacionMantenimiento>();
        //mosttrar lista de requisiciones con igual vehiculo ,estado true

        for (SolicitudReparacionMantenimiento sol : listaSolicitudes) {
            System.out.println("id sol" + sol);
            System.out.println("nun sol" + sol.getNumSolicitud());
            System.out.println("nun sol" + sol.getRequisicionId());
            if (sol.isEstado() && sol.getRequisicionId() == null && sol != getInstance().getSolicitudReparacionId()) {
                System.out.println("anidaio" + sol.getNumSolicitud());
                if (getInstance().getVehiculo() != null) {
                    if ((sol.getVehiculo().getId() == getInstance().getVehiculo().getId())) {

                        lrq.add(sol);
                    }
                }

            }

        }
        listaSolicitudes = lrq;
        System.out.println("lista despues set soli" + listaSolicitudes);

    }

    public Long getIdVehiculo() {
        return idVehiculo;
    }

    public void fijarSolicitud(SolicitudReparacionMantenimiento sol) {
        System.out.println("fijando " + sol);
        setSolicitudrep(sol);
    }

    public void setIdVehiculo(Long idVehiculo) {
        this.idVehiculo = idVehiculo;
        vehiculo = findById(Vehiculo.class, idVehiculo);

    }

    public Vehiculo getVehiculo() {
        vehiculo = getInstance().getVehiculo();
        return getInstance().getVehiculo();
    }
//metodo para fijar el vehiculo a una requisicion

    public void setVehiculo(Vehiculo vehiculo) {

        List<SolicitudReparacionMantenimiento> lss = new ArrayList<SolicitudReparacionMantenimiento>();
        if (vehiculo != null) {
            this.vehiculo = vehiculo;
            getInstance().setVehiculo(vehiculo);
            palabrab = "";
            // for para poder cambiar el id del vehiculo y las requisiciones
            if (solicitudrep != null) {

                if (solicitudrep.getId() != null) {

                    if (solicitudrep.getVehiculo() != getInstance().getVehiculo()
                            && getInstance().getSolicitudReparacionId() != null) {
                        getInstance().setSolicitudReparacionId(null);
                        solicitudrep = new SolicitudReparacionMantenimiento();
                    }
                }

            }

            for (SolicitudReparacionMantenimiento sol : findAll(SolicitudReparacionMantenimiento.class)) {

                if (sol.isEstado() && sol.getRequisicionId() == null) {
                    //aniadir las solicitudes que coincidan con el vehiculo selecionado
                    if ((sol.getVehiculo().getId().equals(getInstance().getVehiculo().getId()))) {

                        lss.add(sol);
                    }
                }

            }

        } else {
            vehiculo = new Vehiculo();
            getInstance().setVehiculo(vehiculo);
            if (solicitudrep != null) {
                if (getInstance().getSolicitudReparacionId() != null) {

                    getInstance().setSolicitudReparacionId(null);
                    solicitudrep = new SolicitudReparacionMantenimiento();

                }
            }
        }
        listaSolicitudes = lss;
        System.out.println("lista despues en veh" + listaSolicitudes);

    }

    @TransactionAttribute   //
    public Requisicion load() {
        if (isIdDefined()) {
            wire();
        }

        return getInstance();
    }

    @TransactionAttribute
    public void wire() {
        getInstance();
    }

    public List<Requisicion> getListaRequisicion() {
        System.out.println("lista de requisicion" + listaRequisicion);

        return listaRequisicion;
    }

    public void setListaRequisicion(List<Requisicion> listaRequisicion) {
        this.listaRequisicion = listaRequisicion;
        //listaItemsRequisicion = getInstance().getListaItems();
    }

    @PostConstruct
    public void init() {
        setEntityManager(em);
        /*el bussinesEntityService.setEntityManager(em) solo va si la Entidad en este caso (ConsultaMedia)
         *hereda de la Entidad BussinesEntity...  caso contrario no se lo agrega
         */
        bussinesEntityService.setEntityManager(em);
        servgen.setEm(em);
        listaRequisicion = new ArrayList<Requisicion>();
        listaSolicitudes = new ArrayList<SolicitudReparacionMantenimiento>();
        List<Requisicion> lrqn = findAll(Requisicion.class);
        listaRequisicion2 = new ArrayList<Requisicion>();
        listaRequisicion.clear();
        for (Requisicion requisicion : lrqn) {
            if (requisicion.isEstado()) {
                if (!requisicion.getAprobado()) {
                    listaRequisicion.add(requisicion);
                } else {
                    listaRequisicionAprobada.add(requisicion);
                }
                listaRequisicion2.add(requisicion);
            }

        }
        List<SolicitudReparacionMantenimiento> ls = findAll(SolicitudReparacionMantenimiento.class);
        listaSolicitudes.clear();
        for (SolicitudReparacionMantenimiento sol : ls) {
            if (sol.getRequisicionId() == null && sol.isEstado()) {

                listaSolicitudes.add(sol);

            }

        }
        listaSolicitudes2 = listaSolicitudes;
        vehiculo = new Vehiculo();
        idVehiculo = 0l;
        getInstance().setTipoRequisicion("Requisición de Reparación");
        listaPartida = findAll(PartidaContabilidad.class);
        List<PartidaContabilidad> lpc = new ArrayList<PartidaContabilidad>();
        for (PartidaContabilidad pcont : listaPartida) {
            if (pcont.isEstado()) {
                lpc.add(pcont);
            }
        }
        listaPartida = lpc;
        listapartida2 = listaPartida;

        cir = new ControladorItemRequisicion();
        cir.setInstance(new ItemRequisicion());
        cir.getInstance().setCantidad(0);
        cir.getInstance().setUnidadMedida(" ");
        cir.getInstance().setDescripcion(" ");
        maximo = 100;
        pro = new Producto();
        pro.setCodigo("");
        palabrab = "";
        solicitudrep = new SolicitudReparacionMantenimiento();
        listaPersonal = findAll(Profile.class);

        listaItemsRequisicion = new ArrayList<ItemRequisicion>();

        idPersonala = 0l;
        idPersonals = 0l;
        getInstance().setTipoAdquisicion("");
        getInstance().setObservaciones("");
        itemsEliminar = new ArrayList<ItemRequisicion>();
        solRequisicion = new SolicitudReparacionMantenimiento();
        List<Producto> lproductos = new ArrayList<Producto>();
        listaProductos = findAll(Producto.class);
        for (Producto pd : listaProductos) {
            if (pd.isEstado()) {
                lproductos.add(pd);
            }
        }
        listaProductos = lproductos;
        listaproductos2 = listaProductos;
        it = new ItemRequisicion();
        it.setCantidad(0);
        it.setUnidadMedida(" ");
        it.setDescripcion(" ");
        listadePersonas = new ArrayList<Profile>();
        listadePersonas.add(new Profile());
        listadePersonas.add(new Profile());
        nombrew = "Requisicion";
        System.out.println("salio de init con lista de items" + listaItemsRequisicion);
        mensaje = "";
        listaPartidaAgregar = new ArrayList<String>();

        palbpartida = "";

    }

    @Override
    protected Requisicion createInstance() {
        //prellenado estable para cualquier clase 
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(Requisicion.class.getName());
        Date now = Calendar.getInstance().getTime();
        Requisicion requisicion = new Requisicion();
        requisicion.setCreatedOn(now);
        //     requisicion.setFechaRequisicion(now);
        requisicion.setLastUpdate(now);
        requisicion.setActivationTime(now);

        requisicion.setType(_type);
        requisicion.buildAttributes(bussinesEntityService);  //
        return requisicion;
    }

    @Override
    public Class<Requisicion> getEntityClass() {
        return Requisicion.class;
    }

    @TransactionAttribute
    public String guardar() {

        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        if (getInstance().getVehiculo() != null) {
            if (getInstance().getVehiculo().getId() != null) {
                getInstance().setVehiculo(vehiculo);
            } else {
                getInstance().setVehiculo(null);
            }

        } else {
            getInstance().setVehiculo(null);
        }

        try {
            if (getInstance().isPersistent()) {
                guardarItem();

                if (solicitudrep != null) {
                    if (solicitudrep.getId() != null) {

                        getInstance().setSolicitudReparacionId(solicitudrep);
                        solicitudrep.setRequisicionId(getInstance());
                        solicitudrep.setLastUpdate(now);
                        try {

                            servgen.actualizar(solicitudrep);
                        } catch (Exception e) {
                            System.out.println("errorrrrr");
                            e.printStackTrace();
                        }
                        getInstance().setSolicitudReparacionId(solicitudrep);

                    } else {
                        System.out.println("fijo null en 1");
                        getInstance().setSolicitudReparacionId(null);
                    }
                } else {
                    System.out.println("fijo null en 2");
                    getInstance().setSolicitudReparacionId(null);
                }
                System.out.println("solRequisicion en gusradra" + solRequisicion);
                if (solRequisicion.getId() != null) {
                    System.out.println("solicitudrep en gusradra" + solicitudrep);
                    if (!solRequisicion.getId().equals(solicitudrep.getId())) {
                        solRequisicion.setLastUpdate(now);
                        try {
                            System.out.println("guaradar solRequisicion sin reqqq" + solRequisicion);
                            System.out.println("guaradar req sin reqqq" + solRequisicion.getRequisicionId());
                            System.out.println("solicitud  del instance" + getInstance().getSolicitudReparacionId());
                            servgen.actualizar(solRequisicion);
                        } catch (Exception e) {
                            System.out.println("errorrrrr solRequisicion");
                            e.printStackTrace();
                        }
                    }

                }
                System.out.println("ingresa a editar>>>>>>>");
                save(getInstance());
                List<Auxiliar> listaaux = servgen.buscarAuxiliarPorIdReq(Auxiliar_.requisicionId.getName(), getInstance().getId());
                System.out.println("size" + listaaux.size());
                for (Auxiliar auxiliar : listaaux) {
                    if (auxiliar.getTipoRelacion().equals("solicitadoR")) {
                        auxiliar.setPersonalId(servgen.buscarPorId(Profile.class, idPersonals));
                        save(auxiliar);
                    }
                    if (auxiliar.getTipoRelacion().equals("aprobadoR")) {
                        auxiliar.setPersonalId(servgen.buscarPorId(Profile.class, idPersonala));
                        save(auxiliar);
                    }
                }

                System.out.println("lista part" + listaPartidaAgregar);
                List<AuxiliarParReq> lp = servgen.buscarAuxiliarPorIdReqPa(AuxiliarParReq_.requisicionId.getName(), getInstance().getId());

                if (lp.size() > listaPartidaAgregar.size()) {
                    //en caso de que en la base de datos existan mas 
                    //partidas de las que se agregan, entonces se editan y las restantes
                    //las elimina
                    for (int i = 0; i < listaPartidaAgregar.size(); i++) {
                        if (!lp.get(i).getPartidaId().getId().equals(Long.valueOf(listaPartidaAgregar.get(i)))) {
                            lp.get(i).setPartidaId(servgen.buscarPorId(PartidaContabilidad.class, Long.valueOf(listaPartidaAgregar.get(i))));
                            servgen.actualizar(lp.get(i));
                        }

                    }
                    for (int i = listaPartidaAgregar.size(); i < lp.size(); i++) {
                        delete(lp.get(i));
                    }
                } else {
                    //en saco de que en las que se agrega existan mas
                    //que las que ya habian en la base de datos, entonces se editan y las restantes
                    //las crea
                    for (int i = 0; i < lp.size(); i++) {
                        if (!lp.get(i).getPartidaId().getId().equals(listaPartidaAgregar.get(i))) {
                            lp.get(i).setPartidaId(servgen.buscarPorId(PartidaContabilidad.class, Long.valueOf(listaPartidaAgregar.get(i))));
                            servgen.actualizar(lp.get(i));
                        }

                    }
                    AuxiliarParReq aux = new AuxiliarParReq();
                    aux.setRequisicionId(getInstance());
                    for (int i = lp.size(); i < listaPartidaAgregar.size(); i++) {
                        aux.setPartidaId(servgen.buscarPorId(PartidaContabilidad.class, Long.valueOf(listaPartidaAgregar.get(i))));
                        save(aux);
                    }

                }

            } else {
                getInstance().setEstado(true);
                guardarItem();
                create(getInstance());
                save(getInstance());
                guardarRelacion();
                if (solicitudrep != null) {
                    if (solicitudrep.getId() != null) {
                        System.out.println("anaidiando la solicitud" + solicitudrep);
                        getInstance().setSolicitudReparacionId(solicitudrep);
                        solicitudrep.setRequisicionId(getInstance());
                        solicitudrep.setLastUpdate(now);
                        try {
                            System.out.println("guaradar solicitud con reqqq" + solicitudrep);
                            servgen.actualizar(solicitudrep);
                        } catch (Exception e) {
                            System.out.println("errorrrrr");
                            e.printStackTrace();
                        }
                        getInstance().setSolicitudReparacionId(solicitudrep);

                    }
                }
                //volver aumentar en uno el valor inicial de la requisicion
                if (getInstance().getTipoRequisicion().equals("Requisición de Reparación")) {
                    Integer t = Integer.parseInt(propertyRepa.getValue().toString());
                    t++;
                    propertyRepa.setValue((Serializable) t);
                    System.out.println("nuevo valor a guardar de repra" + propertyRepa.getValue().toString());

                    save(propertyRepa);
                }
                if (getInstance().getTipoRequisicion().equals("Requisición de Bienes y Servicios")) {
                    Integer t = Integer.parseInt(propertyBien.getValue().toString());
                    t++;
                    propertyBien.setValue((Serializable) t);
                    System.out.println("valor a guardar de bien" + propertyBien.getValue().toString());
                    save(propertyBien);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("EROOR en al cera requisicion");
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
        }

        return "/paginas/secretario/requisicion/lista.xhtml?faces-redirect=true";

    }

    public String getNumeroRequisicion() {
        if (getId() == null) {
            List<BussinesEntityAttribute> bea = getInstance().findBussinesEntityAttribute("org.mtop.modelo.Requisicion");

            if (!getInstance().getTipoRequisicion().equals("")) {
                for (BussinesEntityAttribute a : bea) {
                    if (getInstance().getTipoRequisicion().equals("Requisición de Bienes y Servicios")) {
                        if (a.getProperty().getName().equals("viNumRequisicionBienes")) {

                            propertyBien = a.getProperty();
                            setNumeroRequisicion(a.getValue().toString());
                            return a.getValue().toString();
                        }
                    } else {
                        if (a.getProperty().getName().equals("viNumRequisicionReparacion")) {

                            propertyRepa = a.getProperty();
                            setNumeroRequisicion(a.getValue().toString());
                            return a.getValue().toString();
                        }
                    }

                }

            }

        } else {
            setNumeroRequisicion(getInstance().getNumRequisicion());
        }
        System.out.println("cambio a \n\n\n" + numeroRequisicion);
        return numeroRequisicion;

    }

    public void guardarRequisicion() {
        System.out.println("\n\n\n entro a guardar REQUISISIOCN\n");
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        if (!this.listaItemsRequisicion.isEmpty()) {

            String observacion = getInstance().getObservaciones();
            getInstance().setObservaciones(observacion);

            try {
                listaProductos.clear();
                List<ItemRequisicion> lir = new ArrayList<ItemRequisicion>();
                for (ItemRequisicion apm : getInstance().getListaItems()) {
                    if (apm.getProducto() != null) {
                        listaProductos.add(apm.getProducto());
                    }

                    apm.setRequisicion(getInstance());//fijarle un plan de mantenimiento a cada actividad de plan de mantenimiento
                    //citemsolicitud.setInstance(apm);//fija la actividad del plan de mantenimiento al controlador de actividad de plan de mantenimiento

                    BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(ItemRequisicion.class.getName());

                    apm.setCreatedOn(now);
                    apm.setLastUpdate(now);
                    apm.setActivationTime(now);
                    apm.setType(_type);
                    apm.buildAttributes(bussinesEntityService);  //

                    lir.add(apm);

                }
                if (!listaProductos.isEmpty()) {
                    for (Producto pr : listaProductos) {
                        System.out.println("guaradndo" + pr);
                        pr.setLastUpdate(now);
                        save(pr);
                    }
                }
                getInstance().setListaItems(listaItemsRequisicion);
                getInstance().setListaItems(lir);

                getInstance().setEstado(true);

                create(getInstance());
                save(getInstance());
                guardarRelacion();
                if (getInstance().getTipoRequisicion().equals("Requisición de Reparación")) {
                    Integer t = Integer.parseInt(propertyRepa.getValue().toString());
                    t++;
                    propertyRepa.setValue((Serializable) t);
                    System.out.println("nuevo valor a guardar de repra" + propertyRepa.getValue().toString());

                    save(propertyRepa);
                }
                if (getInstance().getTipoRequisicion().equals("Requisición de Bienes y Servicios")) {
                    Integer t = Integer.parseInt(propertyBien.getValue().toString());
                    t++;
                    propertyBien.setValue((Serializable) t);
                    System.out.println("valor a guardar de bien" + propertyBien.getValue().toString());
                    save(propertyBien);

                }

                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se creó una nueva Requisición  " + getInstance().getNumRequisicion() + "  con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("EROOR en al cera requisicion");
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getNumRequisicion(), " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            }

            listaRequisicion.clear();
            List<Requisicion> lr = findAll(Requisicion.class);
            for (Requisicion req : lr) {

                if (req.isEstado() && req.getSolicitudReparacionId() == null) {
                    if (getInstance().getVehiculo() != null && req.getVehiculo() != null) {
                        if ((req.getVehiculo().getId().equals(getInstance().getVehiculo().getId()))) {

                            listaRequisicion.add(req);
                        }
                    }

                }

            }
//            listaRequisicion.add(getInstance());
            listaItemsRequisicion = null;

        } else {

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: ", " La lista de items de la requisición se encuentra vacia ");
            FacesContext.getCurrentInstance().addMessage("", msg);

        }

    }

    public String obtenercargo(long idP) {
        Profile nombrePersona;
        System.out.println("nllegas id>" + idP);
        String nombPersona = "";
        if (idP != 0) {
            nombrePersona = servgen.buscarPorId(Profile.class, idP);
            nombPersona = nombrePersona.getCargo();
        }

        return nombPersona;
    }

//    public PartidaContabilidad getPartidaC() {
//        return partidaC;
//    }
//
//    public void setPartidaC(PartidaContabilidad partidaC) {
//        listaPartidaAgregar.add(partidaC);
//        //remover de listaPartida
//        List<PartidaContabilidad> lp = new ArrayList<PartidaContabilidad>();
//        for (PartidaContabilidad partidaContabilidad : listaPartida) {
//            if (!partidaContabilidad.getId().equals(partidaC.getId())) {
//                lp.add(partidaContabilidad);
//            }
//        }
//        listaPartida = lp;
//        this.partidaC = partidaC;
//    }
//
//    public void eliminarPartida(PartidaContabilidad partida) {
//        System.out.println("entro A ELIMINAR PARTIDA  " + partida);
//        listaPartida.add(partida);
//        //remover de listaPartidaAgregar
//        List<PartidaContabilidad> lp = new ArrayList<PartidaContabilidad>();
//        for (PartidaContabilidad partidaContabilidad : listaPartidaAgregar) {
//            if (!partidaContabilidad.getId().equals(partidaC.getId())) {
//                lp.add(partidaContabilidad);
//            }
//        }
//        listaPartidaAgregar = lp;
//        System.out.println("li  " + listaPartida);
//    }
//
//    public String getMensajei() {
//        return mensajei;
//    }
//
//    public void setMensajei(String mensajei) {
//        this.mensajei = mensajei;
//    }

}