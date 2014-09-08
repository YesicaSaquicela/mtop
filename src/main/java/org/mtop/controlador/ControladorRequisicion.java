/*
 * Copyright 2014 carlis.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mtop.controlador;

import com.sun.imageio.plugins.jpeg.JPEG;
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
import org.jboss.seam.transaction.Transactional;
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
//import javax.faces.application.FacesMessage;

import javax.faces.bean.ViewScoped;

import javax.inject.Named;
import org.mtop.modelo.ItemRequisicion;
import org.mtop.modelo.ItemSolicitudReparacion;
import org.mtop.modelo.Producto;
import org.mtop.modelo.Producto_;
import org.mtop.modelo.Requisicion_;
import org.mtop.modelo.SolicitudReparacionMantenimiento;
import org.mtop.modelo.SolicitudReparacionMantenimiento_;
import org.mtop.modelo.Vehiculo_;
import org.mtop.modelo.dinamico.BussinesEntityAttribute;
import org.mtop.modelo.dinamico.PersistentObject_;

import org.mtop.modelo.profile.Profile;
//import javax.faces.context.FacesContext;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author carlis
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
    private List<Vehiculo> vehiculos;
    private long idPartidaC = 0l;
    private long idPersonal = 0l;
    private PartidaContabilidad partidaC;
    private List<PartidaContabilidad> listaPartida;
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

    private List<Requisicion> listaRequisicionfiltrada;
    private String tipo;
    private String tipor;
    private String vista;
    private SolicitudReparacionMantenimiento solRequisicion;

    private SolicitudReparacionMantenimiento solicitudReparacionMantenimiento;
    private List<SolicitudReparacionMantenimiento> listaAux;

    private List<ItemRequisicion> itemsEliminar;
    private ItemRequisicion it;
    private Double total = 0.0;

    public Double getTotal() {
        System.out.println("obteniendo total" + total);

        for (ItemRequisicion ir : listaItemsRequisicion) {
            total = total + (ir.getCantidad() * ir.getProducto().getCosto());
        }
        System.out.println("total a retornar");
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setIt(ItemRequisicion it) {
        this.it = it;
    }

    public List<ItemRequisicion> getItemsEliminar() {
        return itemsEliminar;
    }

    public void setItemsEliminar(List<ItemRequisicion> itemsEliminar) {
        this.itemsEliminar = itemsEliminar;
    }

    public void eliminarItemS(ItemRequisicion itemreq) {

        List<ItemRequisicion> li = new ArrayList<ItemRequisicion>();
        System.out.println("lista items" + listaItemsRequisicion);
        for (ItemRequisicion items : listaItemsRequisicion) {

            System.out.println("entro al for>>>>>>>");
            if (!items.getCantidad().equals(itemreq.getCantidad())
                    && !items.getDescription().equals(itemreq.getDescription())
                    && !items.getUnidadMedida().equals(itemreq.getUnidadMedida())) {
                System.out.println("entro a remover>>>>>");
                li.add(items);

            } else {
                if (items.isPersistent()) {
                    itemsEliminar.add(items);

                }

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

    @TransactionAttribute
    public void guardarSol(SolicitudReparacionMantenimiento solis) {
        Date now = Calendar.getInstance().getTime();
        List<ItemSolicitudReparacion> lir = new ArrayList<ItemSolicitudReparacion>();
        for (ItemSolicitudReparacion apm : solis.getListaItemSR()) {

            System.out.println("entrofor" + apm);

            apm.setSolicitudReparacion(solis);//fijarle un plan de mantenimiento a cada actividad de plan de mantenimiento
            //citemsolicitud.setInstance(apm);//fija la actividad del plan de mantenimiento al controlador de actividad de plan de mantenimiento

            System.out.println("al crear");
//            BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(ItemSolicitudReparacion.class.getName());
//
//            apm.setCreatedOn(now);
//            apm.setLastUpdate(now);
//            apm.setActivationTime(now);
//            apm.setType(_type);
//            apm.buildAttributes(bussinesEntityService);  //
//           
            System.out.println("creo instance" + apm);

            lir.add(apm);
            servgen.actualizar(apm);

        }

        solis.setListaItemSR(lir);//fija la lista de actividades a la solicitud
        System.out.println("termino de gusradar" + solis.getListaItemSR());

        BussinesEntityType _type1 = bussinesEntityService.findBussinesEntityTypeByName(SolicitudReparacionMantenimiento.class.getName());
        System.out.println("entra crear" + solis);
        solis.setCreatedOn(now);
        solis.setLastUpdate(now);
        solis.setActivationTime(now);
        solis.setType(_type1);
        solis.buildAttributes(bussinesEntityService);  //
        System.out.println("pasa a crearrr");
        create(solis);
        save(solis);
        listaSolicitudes.add(solis);
        System.out.println("despues a crearrr" + solis);

    }

    public ItemRequisicion getItemr() {
        return itemr;
    }

    public void setItemr(ItemRequisicion itemr) {
        this.itemr = itemr;
        cir.setInstance(itemr);
        agregarItem();
    }

    public String getVista() {
        System.out.println("recueperando vista " + this.vista);
        System.out.println("presentar instance" + getInstance().getId());
        System.out.println("presenta solicitud" + solicitudReparacionMantenimiento);
        System.out.println("lista de requisisicones>>>>>" + listaRequisicion);
        System.out.println("lista de solicitudes de la requisisicon" + listaSolicitudes);
        return vista;

    }

    public void setVista(String vista) {
        System.out.println("fijando a vista " + vista);

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
        System.out.println("fijo " + solicitudReparacionMantenimiento);
        limpiars();

    }

//    public void guardarSolicitud(SolicitudReparacionMantenimiento solicitudReparacionMantenimiento) {
//        this.solicitudReparacionMantenimiento = solicitudReparacionMantenimiento;
//        System.out.println("\n\n\n\n\nsolicitud asignada\n\n\n\n\n" + this.solicitudReparacionMantenimiento.getNumSolicitud());
//        System.out.println("sus items " + this.solicitudReparacionMantenimiento.getListaItemSR());
//
//    }
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
                    System.out.println("tipo repa" + requisicion);
                    lr.add(requisicion);
                }
            }
            setListaRequisicion(lr);

            System.out.println("lista requisiciones aprobadas" + listaRequisicionAprobada);
            for (Requisicion requisicion : listaRequisicionAprobada) {
                if (requisicion.getTipoRequisicion().equals("Requisición de Reparación")) {
                    System.out.println("tipo repa" + requisicion);
                    lr2.add(requisicion);
                }
            }
            setListaRequisicionAprobada(lr);
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
                setListaRequisicionAprobada(lr);
                setListaRequisicionAprobada(lr2);
            }

        }
        System.out.println("lrrrrr>>" + lr);
        this.tipo = tipo;
    }

    public List<Requisicion> getListaRequisicionfiltrada() {
        System.out.println("entro a obtener" + listaRequisicionfiltrada);
        return listaRequisicionfiltrada;
    }

    public String getValorTipo() {
        return valorTipo;
    }

    public void setValorTipo(String valorTipo) {
        this.valorTipo = valorTipo;
        System.out.println("fijo valor tipo" + valorTipo);
        List<Requisicion> lr = new ArrayList<Requisicion>();
        if (valorTipo.equals("repa")) {
            for (Requisicion r : listaRequisicion) {
                if (r.getTipoRequisicion().equals("Requisición de Reparación")) {
                    lr.add(r);
                }
            }

        } else {
            if (valorTipo.equals("bien")) {
                for (Requisicion r : listaRequisicion) {
                    if (r.getTipoRequisicion().equals("Requisición de Bienes y Servicios")) {
                        lr.add(r);
                    }
                }
            }

        }
        if (!lr.isEmpty()) {
            listaRequisicion = lr;
        }
    }

    public void fijarvalorTipo(String s) {
        System.out.println("entro al metodo con " + s);

        setValorTipo(s);
    }

    public void setListaRequisicionfiltrada(List<Requisicion> listaRequisicionfiltrada) {
        this.listaRequisicionfiltrada = listaRequisicionfiltrada;
        System.out.println("entro a fijar" + listaRequisicionfiltrada);
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

    public String darDeBaja(Requisicion requisicion) {

        Date now = Calendar.getInstance().getTime();

        listaProductos.clear();
        System.out.println("requisicion" + requisicion);
        setRequisicionId(requisicion.getId());

        getInstance().setSolicitudReparacionId(null);
        System.out.println("lista de items" + listaItemsRequisicion);
        //  listaItemsRequisicion=getInstance().getListaItems();

        for (ItemRequisicion itemr : listaItemsRequisicion) {
            System.out.println("sdhhfds" + itemr);
            System.out.println("el prodc" + itemr.getProducto());

            if (itemr.getProducto() != null) {
                System.out.println("canti anterior" + itemr.getProducto().getCantidad());
                Producto p = itemr.getProducto();
                p.setCantidad(itemr.getCantidad() + itemr.getProducto().getCantidad());
                System.out.println("cantidad despues " + p.getCantidad());
                p.setLastUpdate(now);
                servgen.actualizar(p);
            }

        }

        getInstance().setEstado(false);
        System.out.println("\n\n\n\ncambio de estado a " + getInstance().isEstado());
        getInstance().setLastUpdate(now);
        listaRequisicion.remove(requisicion);

        servgen.actualizar(getInstance());
        if (getInstance().getSolicitudReparacionId() != null) {
            SolicitudReparacionMantenimiento s = getInstance().getSolicitudReparacionId();
            System.out.println("solo" + s);
            save(s);
        }

        return "/paginas/secretario/requisicion/lista.xhtml?faces-redirect=true";

    }

    public ArrayList<String> autocompletarv(String query) {
        System.out.println("QUEryyyyy" + query);

        ArrayList<String> ced = new ArrayList<String>();

        List<Vehiculo> lv = servgen.buscarTodoscoincidencia(Vehiculo.class, Vehiculo.class.getSimpleName(), Vehiculo_.numRegistro.getName(), query);

        for (Vehiculo vehiculo1 : lv) {
            System.out.println("econtro uno " + vehiculo1.getNumRegistro());
            ced.add(vehiculo1.getNumRegistro());
        }
        List<Vehiculo> lc = servgen.buscarTodoscoincidencia(Vehiculo.class, Vehiculo.class.getSimpleName(), Vehiculo_.placa.getName(), query);
        for (Vehiculo vehiculo1 : lc) {
            System.out.println("econtro uno " + vehiculo1.getPlaca());
            ced.add(vehiculo1.getPlaca());
        }

//         List<Vehiculo> lk = servgen.buscarTodoscoincidencia(Vehiculo.class, Vehiculo.class.getSimpleName(), Vehiculo_.kilometraje.getName(), palabrab);
//          for (Vehiculo vehiculo : lk) {
//            System.out.println("econtro uno " + vehiculo.getPlaca());
//            String cad=vehiculo.getKilometraje().toString();
//            ced.add(cad);
//        }
        System.out.println("listaaaaa autocompletar" + ced);
        return ced;

    }

    public ArrayList<String> autocompletarp(String query) {
        System.out.println("QUEryyyyy" + query);

        ArrayList<String> ced = new ArrayList<String>();

        List<Producto> lp = servgen.buscarTodoscoincidencia(Producto.class, Producto.class.getSimpleName(), PersistentObject_.description.getName(), query);

        for (Producto producto : lp) {
            System.out.println("econtro uno " + producto.getDescription());
            if (!producto.getCantidad().equals(0) && producto.isEstado()) {
                ced.add(producto.getDescription());
            }

        }
        List<Producto> lc = servgen.buscarTodoscoincidencia(Producto.class, Producto.class.getSimpleName(), Producto_.codigo.getName(), query);
        for (Producto producto : lc) {
            if (!producto.getCantidad().equals(0) && producto.isEstado()) {
                System.out.println("econtro uno " + producto.getCodigo());
                ced.add(producto.getCodigo());
            }

        }
        System.out.println("listaaaaa autocompletar" + ced);
        return ced;

    }

    public void buscarv() {
        if (palabrabv == null || palabrabv.equals("") || palabrabv.contains(" ")) {
            palabrabv = "Ingrese algun valor a buscar";
        }
        //buscando por coincidencia descripciion
        System.out.println("\n\n\n\n esta buscando" + palabrabv);
        List<Vehiculo> lv = servgen.buscarTodoscoincidencia(Vehiculo.class, Vehiculo.class.getSimpleName(), Vehiculo_.numRegistro.getName(), palabrabv);
        //buscando por codigo
        System.out.println("\n\n\n\n lv\n\n\n\n" + lv);
        List<Vehiculo> lc = servgen.buscarTodoscoincidencia(Vehiculo.class, Vehiculo.class.getSimpleName(), Vehiculo_.placa.getName(), palabrabv);
//        List<Vehiculo> lk = servgen.buscarTodoscoincidencia(Vehiculo.class, Vehiculo.class.getSimpleName(), Vehiculo_.kilometraje.getName(), palabrab);

        for (Vehiculo vehiculo1 : lc) {
            if (!lv.contains(vehiculo1)) {
                lv.add(vehiculo1);
            }
        }

//        for (Vehiculo vehiculo : lk) {
//            if (!lv.contains(vehiculo)) {
//                lv.add(vehiculo);
//            }
//        }
        if (lv.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrabv.equals("Ingrese algun valor a buscar")) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN:", " Ingrese algún valor a buscar"));
                palabrabv = " ";
            } else {

                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN:", "No se ha encontrado " + palabrab));
            }

        } else {
            vehiculos = lv;
        }
        System.out.println("\n\n\n busco \n\n" + vehiculo);
        System.out.println("listaaaaa autocompletar" + vehiculos);
    }

    public void buscarp() {
        System.out.println("Entroa a buscar al producto>>>>>>>>>" + palabrabp);
        palabrabp = palabrabp.trim();
        if (palabrabp == null || palabrabp.equals("")) {
            palabrabp = "Ingrese algun valor a buscar";
        }
        System.out.println("Entroa a buscar al producto despues>>>>>>>>>" + palabrabp);
        //buscando por coincidencia descripciion
        List<Producto> lp = servgen.buscarTodoscoincidencia(Producto.class, Producto.class.getSimpleName(), PersistentObject_.description.getName(), palabrabp);
        //buscando por codigo
        List<Producto> lc = servgen.buscarTodoscoincidencia(Producto.class, Producto.class.getSimpleName(), Producto_.codigo.getName(), palabrabp);
        for (Producto producto : lc) {
            if (!lp.contains(producto)) {
                lp.add(producto);
            }
        }

        if (lp.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrabp.equals("Ingrese algun valor a buscar")) {
                context.addMessage(null, new FacesMessage("INFORMACIÓN: Ingrese algún valor a buscar"));
                palabrabp = " ";
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN:", "No se ha encontrado " + palabrabp));
            }

        } else {

            for (Producto productol : lp) {
                System.out.println("entroa al for lista productos");
                if (productol.isEstado() && !productol.getCantidad().equals(0)) {
                    System.out.println("entroa a comparar>>>");
                    listaProductos = lp;
                    System.out.println("devuelve lista>>>>" + listaProductos);
                }
            }

        }

    }

    public void limpiarv() {
        palabrabv = "";
        List<Vehiculo> lv = servgen.buscarTodos(Vehiculo.class);
        vehiculos.clear();
        System.out.println("lppp" + lv);

        for (Vehiculo vehiculo1 : lv) {
            System.out.println("iddddd" + vehiculo1.getId());
            System.out.println("entro a for lista>>>>" + vehiculo1.isEstado());
            if (vehiculo1.isEstado()) {
                System.out.println("listatesssa" + vehiculos);
                vehiculos.add(vehiculo1);

                System.out.println("Entro a remover>>>>");
                System.out.println("a;iadia" + vehiculos);

            }

        }
    }

    public void limpiarp() {
        palabrabp = "";
        List<Producto> lp = servgen.buscarTodos(Producto.class);
        listaProductos.clear();
        System.out.println("lppp" + lp);

        for (Producto produ : lp) {
            System.out.println("iddddd" + produ.getId());
            System.out.println("entro a for lista>>>>" + produ.isEstado());
            if (produ.isEstado() && !produ.getCantidad().equals(0)) {
                System.out.println("listatesssa" + listaProductos);
                listaProductos.add(produ);

                System.out.println("Entro a remover>>>>");
                System.out.println("a;iadia" + listaProductos);

            }

        }
    }

    public List<Profile> getListaPersonal() {
        return listaPersonal;
    }

    public void setListaPersonal(List<Profile> listaPersonal) {
        this.listaPersonal = listaPersonal;
    }

    public List<ItemRequisicion> getListaItemsRequisicion() {
        System.out.println("getInstance().getTipoAdquisicion()" + getInstance().getTipoAdquisicion());
        if (getInstance().getTipoAdquisicion() != null) {
            if (getInstance().getTipoAdquisicion().equals("bodega")) {
                System.out.println("lista de items requ1>>>>" + listaItemsRequisicion);
                List<ItemRequisicion> lirq = listaItemsRequisicion;
                System.out.println("lista de items lirq>>>>" + lirq);
                for (ItemRequisicion itemRequisicion : lirq) {
                    System.out.println("entro for con producto" + itemRequisicion.getProducto());
                    if (itemRequisicion.getProducto() == null) {
                        listaItemsRequisicion.clear();
                        break;
                    }
                }

            }
        }

        System.out.println("lista de items" + listaItemsRequisicion);
        return listaItemsRequisicion;
    }

    public void setListaItemsRequisicion(List<ItemRequisicion> listaItemsRequisicion) {
        System.out.println("setlista ITEMSSSSS" + listaItemsRequisicion);
        this.listaItemsRequisicion = listaItemsRequisicion;
    }

    public SolicitudReparacionMantenimiento getSolicitudrep() {
        System.out.println("getlista ITEMSSSSS" + listaItemsRequisicion);

        return solicitudrep;
    }

    public void setSolicitudrep(SolicitudReparacionMantenimiento solicitudrep) {
        System.out.println("asignar una solicitud" + solicitudrep);
        if (solicitudrep != null) {
            this.solicitudrep = solicitudrep;
            getInstance().setSolicitudReparacionId(solicitudrep);
        } else {
            this.solicitudrep = new SolicitudReparacionMantenimiento();
        }

    }

    public List<SolicitudReparacionMantenimiento> getListaSolicitudes() {
        System.out.println("getlista solicitude" + listaSolicitudes);
        System.out.println("solRequisicion id" + solRequisicion.getId());
        System.out.println("ssolRequisisicon id" + solRequisicion.getId());
        if (solRequisicion.getId() != solicitudrep.getId()) {
            System.out.println("entro al if>>>>>>> getListaSoli");
            listaSolicitudes.add(solRequisicion);
        }
        System.out.println("Lista de solicitudes en getListaS>>>" + listaSolicitudes);
        return listaSolicitudes;
    }

    public void setListaSolicitudes(List<SolicitudReparacionMantenimiento> listaSolicitudes) {
        System.out.println("setlista solicitude" + listaSolicitudes);
        this.listaSolicitudes = listaSolicitudes;
        System.out.println("Lista de solicitudes en setListaS>>>" + listaSolicitudes);
    }

    public void buscar() {
        palabrab = palabrab.trim();
        if (palabrab == null || palabrab.equals("")) {
            System.out.println("entro vacio");
            palabrab = "Ingrese algun valor a buscar";
        }
        //buscando por coincidencia
        List<Requisicion> le = servgen.buscarTodoscoincidencia(Requisicion.class, Requisicion.class.getSimpleName(), Requisicion_.numRequisicion.getName(), palabrab);
        //buscando por fechas
        List<Requisicion> lef = servgen.buscarRequisicionporFecha(Requisicion_.fechaRequisicion.getName(), palabrab);
        System.out.println("Todas>>>>>>" + lef);
        System.out.println("Todascoincidencia>>>>>>" + le);
        List<Long> lrq = new ArrayList<Long>();
        for (Requisicion r : listaRequisicion2) {
            if (r.isEstado()&& !lrq.contains(r)) {
                String s = r.getFechaRequisicion().toString();
                if (r.getNumRequisicion().contains(palabrab) || s.contains(palabrab)) {
                    lrq.add(r.getId());
                }

            }

        }
        
        System.out.println("LEeeee" + lrq);

//        Vehiculo v = new Vehiculo();
//        UI ui = new UI();
//        for (Requisicion requisicion : findAll(Requisicion.class)) {
//            v = findById(Vehiculo.class, requisicion.getVehiculo().getId());
//            List<Property> lp = ui.getProperties(v);
//            for (Property p : lp) {
//                if (p.getType().equals("org.mtop.modelo.dinamico.Structure") && p.getName().equals("chasis")) {
//
//                    List<BussinesEntityAttribute> lbea = v.findBussinesEntityAttribute(p.getName());
//                    for (BussinesEntityAttribute a : lbea) {
//                        if (a.getName().equals("serie")) {
//                            if (a.getValue().toString().contains(palabrab)) {
//                                if (!le.contains(requisicion)) {
//                                    le.add(requisicion);
//                                }
//
//                            }
//                        }
//                    }
//                }
//            }
//            if (v.getNumRegistro().contains(palabrab)) {
//                if (!le.contains(requisicion)) {
//                    le.add(requisicion);
//                }
//            }
//
//        }
        if (lrq.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrab.equals("Ingrese algun valor a buscar")) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN:", " Ingrese algún valor a buscar"));
                palabrab = " ";
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN:", "No se ha encontrado " + palabrab));
            }

        } else {
            listaRequisicion.clear();
            listaRequisicionAprobada.clear();
            Requisicion re = new Requisicion();
            for (Long r : lrq) {
                re = findById(Requisicion.class, r);
                if (re.isEstado()) {
                    if (re.getAprobado()) {
                        if (!listaRequisicionAprobada.contains(r)) {

                            listaRequisicionAprobada.add(re);
                        }

                    } else {
                        if (!listaRequisicion.contains(r)) {
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
        List<Requisicion> lr = servgen.buscarTodos(Requisicion.class);
        listaRequisicion.clear();
        listaRequisicionAprobada.clear();
        System.out.println("lppp" + lr);

        for (Requisicion r : lr) {
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

        for (Requisicion requisicion : listaRequisicion2) {
            System.out.println("econtro uno " + requisicion.getNumRequisicion());
            if (requisicion.isEstado() && requisicion.getNumRequisicion().contains(query)) {
                ced.add(requisicion.getNumRequisicion());
            }

        }
        String s = "";
        for (Requisicion requisicion : listaRequisicion2) {
            s = requisicion.getFechaRequisicion().toString();
            if (s.contains(query) && requisicion.isEstado() && !ced.contains(requisicion.getFechaRequisicion().toString())) {
                ced.add(requisicion.getFechaRequisicion().toString());
            }
        }

//        Vehiculo v = new Vehiculo();
//        UI ui = new UI();
////        for (Requisicion requisicion : findAll(Requisicion.class)) {
////            v = findById(Vehiculo.class, requisicion.getVehiculo().getId());
////            BussinesEntityAttribute bea=v.getBussinessEntityAttribute("chasis");
////            bea=bea.getBussinesEntity().getBussinessEntityAttribute("serie");
////            System.out.println("beaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+bea.getName());
////            List<Property> lp = ui.getProperties(v);
////            for (Property p : lp) {
////                if (p.getType().equals("org.mtop.modelo.dinamico.Structure") && p.getName().equals("chasis")) {
////
////                    List<BussinesEntityAttribute> lbea = v.findBussinesEntityAttribute(p.getName());
////                    for (BussinesEntityAttribute a : lbea) {
////                        if (a.getName().equals("serie")) {
////                            if (a.getValue().toString().contains(query)) {
////                                ced.add(a.getValue().toString());
////                            }
////                        }
////                    }
////                }
////            }
//        if (v.getNumRegistro().contains(query)) {
//            ced.add(v.getNumRegistro());
//        }
        System.out.println("listaaaaa autocompletar" + ced);
        return ced;

    }

    public void buscars() {
        palabrabs = palabrabs.trim();
        if (palabrabs == null || palabrabs.equals("")) {
            palabrabs = "Ingrese algun valor a buscar";
        }
        //buscando por coincidencia
        List<SolicitudReparacionMantenimiento> le = servgen.buscarTodoscoincidencia(SolicitudReparacionMantenimiento.class, SolicitudReparacionMantenimiento.class.getSimpleName(), SolicitudReparacionMantenimiento_.numSolicitud.getName(), palabrabs);
        //buscando por fechas
        List<SolicitudReparacionMantenimiento> lef = servgen.buscarSolicitudporFecha(SolicitudReparacionMantenimiento_.fechaSolicitud.getName(), palabrabs);
        List<Long> lsoli = new ArrayList<Long>();
        for (SolicitudReparacionMantenimiento srm : le) {
            lsoli.add(srm.getId());
        }
        for (SolicitudReparacionMantenimiento solicitudesRM : lef) {
            System.out.println("rerere " + solicitudesRM);
            System.out.println("lrq>>>> " + lsoli);

            if (!lsoli.contains(solicitudesRM.getId())) {
                lsoli.add(solicitudesRM.getId());
            }
        }

        System.out.println("LEeeee" + lsoli);

        if (lsoli.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrab.equals("Ingrese algun valor a buscar")) {
                context.addMessage(null, new FacesMessage("INFORMACION: Ingrese algun valor a buscar"));
                palabrab = " ";
            } else {
                context.addMessage(null, new FacesMessage("INFORMACION: No se ha encontrado " + palabrab));
            }

        } else {
            listaSolicitudes.clear();
            SolicitudReparacionMantenimiento solRM = new SolicitudReparacionMantenimiento();
            for (Long s : lsoli) {
                solRM = findById(SolicitudReparacionMantenimiento.class, s);
                if (solRM.getRequisicionId() == null && solRM.isEstado() && solRM.getVehiculo().getId().equals(getInstance().getVehiculo().getId())) {
                    System.out.println("entro a comparacion>>>>>");
                    listaSolicitudes.add(solRM);
                    System.out.println("\n\nlista final>>>>" + listaSolicitudes);

                }
            }

            palabrab = "";
        }

    }

    public void limpiars() {
        palabrabs = "";

        List<SolicitudReparacionMantenimiento> ls = findAll(SolicitudReparacionMantenimiento.class);

        listaSolicitudes.clear();
        System.out.println("lppp" + ls);
        System.out.println("vehisuclo " + getInstance().getVehiculo());
        for (SolicitudReparacionMantenimiento soli : ls) {
            System.out.println("num sel la soli en lipiar " + soli.getNumSolicitud());
            System.out.println("estado sel la soli en lipiar " + soli.isEstado());
            System.out.println("req sel la soli en lipiar " + soli.getRequisicionId());
            System.out.println("vehiculo sel la soli en lipiar " + soli.getVehiculo());
            if (soli.isEstado() && soli.getRequisicionId() == null && soli.getVehiculo().getId().equals(getInstance().getVehiculo().getId())) {
                System.out.println("listatesssa" + listaSolicitudes);
                listaSolicitudes.add(soli);

            }

        }
    }

    public ArrayList<String> autocompletars(String query) {
        System.out.println("QUEryyyyy" + query);

        ArrayList<String> ced = new ArrayList<String>();

        List<SolicitudReparacionMantenimiento> le = servgen.buscarTodoscoincidencia(SolicitudReparacionMantenimiento.class, SolicitudReparacionMantenimiento.class.getSimpleName(), SolicitudReparacionMantenimiento_.numSolicitud.getName(), query);
        //buscando por fechas
        String s="";
        for (SolicitudReparacionMantenimiento soli : listaSolicitudes2) {
            System.out.println("econtro uno " + soli.getNumSolicitud());
            if (soli.isEstado() 
                    && soli.getVehiculo().getId().equals(getInstance().getVehiculo().getId()) 
                    && soli.getRequisicionId() == null 
                   
                    ) {
                s=soli.getFechaSolicitud().toString();
                if( soli.getNumSolicitud().contains(query)
                        && !ced.contains(soli.getNumSolicitud())
                       ){
                    ced.add(soli.getNumSolicitud());
                }
                 if(s.contains(query) &&  !ced.contains(s)){
                    ced.add(s);
                }
                
            }

        }
        

        System.out.println("listaaaaa autocompletar" + ced);
        return ced;

    }

    public Integer getMaximo() {
        System.out.println("get Instance" + getInstance().getTipoAdquisicion());
        if (getInstance().getTipoAdquisicion() != null) {
            if (getInstance().getTipoAdquisicion().equals("bodega")) {
                if (pro.getId() != null) {
                    boolean ban = true;
                    System.out.println("cir" + cir.getInstance().getCantidad());
                    System.out.println("prod" + pro);
                    maximo = pro.getCantidad();
//                    for (Producto p : listaProductos) {
//                        System.out.println("ppp" + p);
//                        if (p.getCantidad() == 0) {
//                            setMaximo(cir.getInstance().getCantidad());
//                            ban = false;
//                            break;
//                        }
//                    }
//                    if (ban) {
//
//                        System.out.println("enrra a obtener >>>>>>>>>>>>>>>>>>>>>>>");
//                        setMaximo(pro.getCantidad());
//                    }

                } else {
                    setMaximo(100);
                }

            } else {
                setMaximo(100);
            }

        } else {
            setMaximo(100);
        }
        System.out.println("lista itemmmm" + listaItemsRequisicion);
        System.out.println("maaaaaaaximo" + maximo);
        return maximo;

    }

    public void setMaximo(Integer maximo) {
        this.maximo = maximo;
    }

    public Producto getPro() {
        System.out.println("reto::::::::::::::::");

        System.out.println(pro);
        System.out.println("maximooo" + maximo);
        return pro;
    }

    public void setPro(Producto pro) {
        System.out.println("fijandooooooooooooooooooooooooooooo" + pro);
        this.pro = pro;
        cir.getInstance().setDescription(pro.getDescription());
        cir.getInstance().setCantidad(0);
        cir.getInstance().setUnidadMedida(" ");
        cir.getInstance().setDescription(pro.getDescription());
        cir.getInstance().setProducto(pro);
        maximo = pro.getCantidad();
        palabrab = "";

    }

    public void guardarProducto(Producto p) {
        System.out.println("entra guarrrrrrrrrrrrrrrrrrrrrrrdarrrrrrrrrrrrrrr");
        cir.getInstance().setProducto(pro);
        pro = cir.getInstance().getProducto();
        if (cir.getInstance().getProducto() != null) {
            System.out.println("pro");
            System.out.println("producto " + cir.getInstance().getProducto().getCodigo());
        } else {
            System.out.println("no hay producto");
        }

    }

    public List<Producto> getListaProductos() {
        if (listaProductos.isEmpty()) {

            listaProductos = findAll(Producto.class);
        }
        System.out.println("lista de productos" + listaProductos);
        return listaProductos;
    }

    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public long getIdPartidaC() {
        if (getRequisicionId() != null && idPartidaC == 0l) {
            idPartidaC = getInstance().getPartidaContabilidad().getId();
        }

        return idPartidaC;
    }

    public void setIdPartidaC(long idPartidaC) {
        this.idPartidaC = idPartidaC;
    }

    public long getIdPersonal() {
        if (getRequisicionId() != null && idPersonal == 0l) {
            idPartidaC = getInstance().getPartidaContabilidad().getId();
        }
        return idPersonal;
    }

    public void setIdPersonal(long idPersonal) {
        this.idPersonal = idPersonal;
    }

    public String getNumeroRequisicion() {
        System.out.println("antes\n\n\n" + numeroRequisicion);
        if (getId() == null) {
            List<BussinesEntityAttribute> bea = getInstance().findBussinesEntityAttribute("org.mtop.modelo.Requisicion");
            System.out.println("bea size" + bea.size());
            String valorInicialReparacion = "900";
            String valorInicialBien = "200";
            for (BussinesEntityAttribute a : bea) {
                if (a.getProperty().getName().equals("viNumRequisicionReparacion")) {
                    System.out.println("entro a onbtener el valor " + a.getValue().toString());
                    valorInicialReparacion = a.getValue().toString();
                }
                if (a.getProperty().getName().equals("viNumRequisicionBienes")) {
                    System.out.println("entro a onbtener el valor " + a.getValue().toString());
                    valorInicialBien = a.getValue().toString();
                }
            }

            System.out.println("numero" + getInstance().getNumRequisicion());
            List<Requisicion> lr = servgen.buscarTodoscoincidencia(Requisicion.class, Requisicion.class.getSimpleName(), Requisicion_.tipoRequisicion.getName(), "Requisición de Bienes y Servicios");
            int t;
            System.out.println("jshdsajd" + getInstance().getTipoRequisicion());
            if (!getInstance().getTipoRequisicion().equals("")) {
                System.out.println("endbjhsdsasljkas");
                if (getInstance().getTipoRequisicion().equals("Requisición de Bienes y Servicios")) {

                    // List<Requisicion> lr=servgen.buscarTodoscoincidencia(Requisicion.class, Requisicion.class.getSimpleName(), Requisicion_.tipoRequisicion.getName(), "Requisición de Bienes y Servicios");
                    t = lr.size() + Integer.parseInt(valorInicialBien);
                    System.out.println("tttttt" + t);
                } else {
                    lr = servgen.buscarTodoscoincidencia(Requisicion.class, Requisicion.class.getSimpleName(), Requisicion_.tipoRequisicion.getName(), "Requisición de Reparación");
                    t = lr.size() + Integer.parseInt(valorInicialReparacion);
                }
            } else {
                t = lr.size() + Integer.parseInt(valorInicialReparacion);
            }
            System.out.println("ttttttt" + t);
            setNumeroRequisicion(String.valueOf(t + 1));

        } else {
            setNumeroRequisicion(getInstance().getNumRequisicion());
        }
        System.out.println("cambio a \n\n\n" + numeroRequisicion);
        return numeroRequisicion;

    }

    public void guardarItem() {
        System.out.println("lista productos" + listaProductos);
        listaProductos.clear();
        System.out.println("lista productos" + listaProductos);
        for (ItemRequisicion apm : listaItemsRequisicion) {
            if (apm.getProducto() != null) {
                System.out.println("aniadio producto");
                listaProductos.add(apm.getProducto());
            }

            System.out.println("guardando" + apm);
            Date now = Calendar.getInstance().getTime();
            apm.setRequisicion(getInstance());//fijarle un plan de mantenimiento a cada actividad de plan de mantenimiento
            cir.setInstance(apm);//fija la actividad del plan de mantenimiento al controlador de actividad de plan de mantenimiento

            if (getInstance().isPersistent()) {
                cir.getInstance().setLastUpdate(now);
                cir.guardar();

            } else {
                System.out.println("al crear");
                BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(ItemRequisicion.class.getName());

                apm.setCreatedOn(now);
                apm.setLastUpdate(now);
                apm.setActivationTime(now);
                apm.setType(_type);
                apm.buildAttributes(bussinesEntityService);  //
                cir.setInstance(apm);
                System.out.println("antes guardar");
                cir.guardar();
                System.out.println("despues guardar");
            }

        }
        if (!listaProductos.isEmpty()) {
            for (Producto pr : listaProductos) {
                System.out.println("guaradndo" + pr);
                Date now = Calendar.getInstance().getTime();
                pr.setLastUpdate(now);
                save(pr);
            }
        }
        getInstance().setListaItems(listaItemsRequisicion);//fija la lista de actividades al plan de mantenimietno
        System.out.println("termino de gusradar" + getInstance().getListaItems());
        System.out.println("lsiat de items a eliminar " + itemsEliminar);
        for (ItemRequisicion isr : itemsEliminar) {
            delete(isr);
        }
        System.out.println("termino de gusradar" + getInstance().getListaItems());
    }

    public void agregarItem() {

        String des = cir.getInstance().getDescription().trim();
        String uni = cir.getInstance().getUnidadMedida().trim();

        if (cir.getInstance().getCantidad().equals(0) || des.equals("") || uni.equals("")) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR!", "campos abligatorios, cantidad, descripción y unidad de medida"));
            System.out.println("\n\n\n\n no entro guardarrrrr\n\n\n\n");
        } else {
            if (pro != null) {

                if (!pro.getCodigo().equals("")) {

                    Producto p = new Producto();
                    List<Producto> lp = new ArrayList<Producto>();
                    System.out.println("proooooo+" + pro);
                    System.out.println("lista productos" + listaProductos);
                    for (Producto prod : listaProductos) {
                        System.out.println("prod+prod" + prod);
                        if (!prod.getId().equals(pro.getId())) {
                            System.out.println("a aniadir" + prod);
                            lp.add(prod);

                        }
                    }
                    listaProductos = lp;
                    System.out.println("lista productos" + listaProductos);
                    pro.setCantidad(pro.getCantidad() - cir.getInstance().getCantidad());
                    listaProductos.add(pro);
                    System.out.println("lista productosdespus" + listaProductos);
                }
            }
            if (it != null) {
                System.out.println("entro a it");
                if (!it.getCantidad().equals(cir.getInstance().getCantidad())
                        && !it.getDescription().equals(cir.getInstance().getDescription())
                        && !it.getUnidadMedida().equals(cir.getInstance().getUnidadMedida())) {
                    listaItemsRequisicion.remove(it);
                    listaItemsRequisicion.add(cir.getInstance());
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", " Se actualizó Item " + cir.getInstance().getDescription() + " con éxito ");
                    FacesContext.getCurrentInstance().addMessage("", msg);
                } else {
                    it = null;
                }
            } else {

                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", " Se creó Item " + cir.getInstance().getDescription() + " con éxito ");
                FacesContext.getCurrentInstance().addMessage("", msg);
                listaItemsRequisicion.add(cir.getInstance());
            }

            cir.setInstance(new ItemRequisicion());
        }

        pro = new Producto();
        pro.setCodigo("");
        cir.getInstance().setCantidad(0);
        cir.getInstance().setUnidadMedida(" ");
        cir.getInstance().setDescription(" ");
        getInstance().setListaItems(listaItemsRequisicion);
        editarItem(cir.getInstance());
        System.out.println("lista items ddd" + listaItemsRequisicion);

    }

    public void editarItem(ItemRequisicion itemReq) {
        List<ItemRequisicion> lir = listaItemsRequisicion;
        for (ItemRequisicion i : lir) {

            if (i.getCantidad().equals(itemReq.getCantidad())
                    && i.getUnidadMedida().equals(itemReq.getUnidadMedida())) {
//                listaItemsRequisicion.remove(con);
                System.out.println("a fijar cant" + i.getCantidad());
                System.out.println("a fijar unid" + i.getUnidadMedida());
                cir.setInstance(i);
                it = i;

                if (itemReq != null) {
                    if (itemReq.getProducto() != null) {
                        pro = itemReq.getProducto();
                        int j = 0;
                        for (Producto p : listaProductos) {
                            if (p.getId().equals(pro.getId())) {
                                j++;
                                break;
                            }
                        }
                        pro.setCantidad(itemReq.getCantidad() + listaProductos.get(j).getCantidad());
                        listaProductos.set(j, pro);

                        pro = listaProductos.get(j);
                        maximo = pro.getCantidad() + it.getCantidad();
                    }

                }
                break;

            }

        }
        getInstance().setListaItems(listaItemsRequisicion);

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
                System.out.println("a fijar cant" + i.getCantidad());
                System.out.println("a fijar unid" + i.getUnidadMedida());
                pro = itemReq.getProducto();
                int j = listaProductos.lastIndexOf(pro);
                System.out.println("pro" + pro.getCantidad());
                System.out.println("pro de lista" + pro.getCantidad());
                pro.setCantidad(itemReq.getCantidad() + listaProductos.get(j).getCantidad());
                listaProductos.set(j, pro);
                pro = listaProductos.get(j);

                System.out.println("a fijada" + cir.getInstance().getCantidad());
                System.out.println("a fijada" + cir.getInstance().getUnidadMedida());
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
    //private static Logger logger = Logger.getLogger(UserWizard.class.getName());

    public String formato(Date fecha) {
        System.out.println("\n\n\n\n\n\n\nhsadhjsdj" + fecha);
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
            System.out.println("pasoooo");
            if (getInstance().getId() != null) {
                this.cir.setListaItemsRequisicion(getInstance().getListaItems());
            }
            if (event.getNewStep().equals("req1") && event.getOldStep().equals("items1")) {
                return event.getNewStep();
            } else {
                if (event.getOldStep().equals("items1") && this.listaItemsRequisicion.isEmpty()) {

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "debe ingresar al menos un item a la lista"));

                    return event.getOldStep();
                } else {

                    return event.getNewStep();

                }

            }

        }
    }

    public String onFlowProcess(FlowEvent event) {
        System.out.println("\nn\n\n\nentro flow proces \nn\n\n\n" + this.vehiculo);
        System.out.println("mac¿ximo" + cir.getInstance().getCantidad());
        System.out.println("maximo" + getMaximo());

        if (skip) {
            skip = false;   //reset in case user goes back  

            return "confirm";
        } else {
            System.out.println("pasoooo");
            if (getInstance().getId() != null) {
                this.cir.setListaItemsRequisicion(getInstance().getListaItems());
            }
            if (event.getNewStep().equals("req") && event.getOldStep().equals("address")) {
                return event.getNewStep();
            } else {
                if (event.getNewStep().equals("address") && event.getOldStep().equals("items")) {
                    return event.getNewStep();
                } else {
                    if (event.getOldStep().equals("address")) {
                        System.out.println("id d vechicilooo\n\n\n\n" + this.vehiculo);
                        System.out.println("id tipod e re q escogida" + getInstance().getTipoRequisicion());
                        if (this.vehiculo != null) {
                            if (this.vehiculo.getId() == null && getInstance().getTipoRequisicion().equals("Requisición de Reparación")) {

                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "debe escoger un vehiculo"));

                                return event.getOldStep();
                            } else {
                                return event.getNewStep();
                            }
                        } else {
                            return event.getNewStep();
                        }

                    } else {

                        if (event.getOldStep().equals("items") && this.listaItemsRequisicion.isEmpty()) {

                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "debe ingresar al menos un item a la lista"));

                            return event.getOldStep();
                        } else {

//                    if (event.getNewStep().equals("items") && this.listaItemsRequisicion.isEmpty() ) {
//                        System.out.println("\n\n\n\n entro a regresar\n\n\n\n");
//                        return event.getOldStep();
//                    } else {
                            return event.getNewStep();
//                    }
                        }
                    }

                }
            }

        }

    }

    public List<Vehiculo> getVehiculos() {

        System.out.println("ENTRO A BUSCAR>vehiculos>>>>>>>>>>");

        return vehiculos;
    }

    public void setVehiculos(List<Vehiculo> vehiculos) {

        this.vehiculos = vehiculos;
    }

    public Long getRequisicionId() {
        return (Long) getId();
    }

    public void setRequisicionId(Long requisicionId) {
        if (requisicionId == null) {
            Date now = Calendar.getInstance().getTime();
            getInstance().setFechaRequisicion(now);
        }
        System.out.println("\n\n\n\n\n\n\nfechaa\n\n\n\n\n" + getInstance().getFechaRequisicion());
        setId(requisicionId);
        System.out.println("instance" + getInstance());
        vehiculo = getInstance().getVehiculo();
        System.out.println("\n\n\nsolicitud antes en set \n\n\n" + solicitudrep);
        if (getInstance().getSolicitudReparacionId() != null) {
            solicitudrep = getInstance().getSolicitudReparacionId();
            solRequisicion = getInstance().getSolicitudReparacionId();
            System.out.println("solrequisicion" + solRequisicion);
            System.out.println("solrequisicion" + solRequisicion.getRequisicionId());
            solRequisicion.setRequisicionId(null);
        }

        System.out.println("\n\n\nsolicitud depuestes en set\n\n\n" + solicitudrep);
        idPersonal = getInstance().getPsolicita().getId();

        if (getInstance().isPersistent()) {
            listaItemsRequisicion = getInstance().getListaItems();
            System.out.println("entro a obtener lisra" + listaItemsRequisicion);
        }

        List<SolicitudReparacionMantenimiento> lr = findAll(SolicitudReparacionMantenimiento.class);
        List<SolicitudReparacionMantenimiento> lrq = new ArrayList<SolicitudReparacionMantenimiento>();
        //mosttrar lista de requisiciones con igual vehiculo ,estado true
        System.out.println("lista toda set requis>>>" + lr);
        for (SolicitudReparacionMantenimiento sol : lr) {
            System.out.println("\n\nEntro a for solis set req...>>>" + sol.getVehiculo().getId());
            System.out.println("soli setreq" + sol + "estadosetsoli" + sol.isEstado());
            if (sol.isEstado() && sol.getRequisicionId() == null) {
                if (getInstance().getVehiculo() != null) {
                    if ((sol.getVehiculo().getId() == getInstance().getVehiculo().getId())) {
                        System.out.println("\n\nentro a comparar set req.....");
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

    public void asignarIdVehiculo(Long idVehiculo) {
        this.idVehiculo = idVehiculo;
        System.out.println("ID del vehiculo>>>>><<<<<<<<<<<<<<<<<<<<<" + idVehiculo);
        vehiculo = findById(Vehiculo.class, idVehiculo);

    }

    public Vehiculo getVehiculo() {
        vehiculo = getInstance().getVehiculo();
        System.out.println("jsjsjsjsjsjsjjsjsjsjjsjsjjs" + vehiculo);
        System.out.println("getinstancevehiculo" + getInstance().getVehiculo());
        return getInstance().getVehiculo();
    }

    public void setVehiculo(Vehiculo vehiculo) {
        List<SolicitudReparacionMantenimiento> lss = new ArrayList<SolicitudReparacionMantenimiento>();
        if (vehiculo != null) {
            System.out.println("entra a fijar un vehiculo con su iddd" + vehiculo.getId());

            this.vehiculo = vehiculo;
            getInstance().setVehiculo(vehiculo);
            palabrab = "";

            // for para poder cambiar el id del vehiculo y las requisiciones
            System.out.println("solicicitud repa" + solicitudrep);
            if (solicitudrep != null) {
                System.out.println("solicitud.vehiculo " + solicitudrep.getVehiculo());
                if (solicitudrep.getVehiculo() != getInstance().getVehiculo() && getInstance().getSolicitudReparacionId() != null) {
                    getInstance().setSolicitudReparacionId(null);
                    solicitudrep = new SolicitudReparacionMantenimiento();
                }
            }

            for (SolicitudReparacionMantenimiento sol : findAll(SolicitudReparacionMantenimiento.class)) {
                System.out.println("\n\nEntro a for solis  veh...>>>" + sol.getVehiculo().getId());
                System.out.println("sol.req" + sol.getRequisicionId() + "estado" + sol.isEstado());
                if (sol.isEstado() && sol.getRequisicionId() == null) {
                    System.out.println("veh instance>>>" + getInstance().getVehiculo().getId());
                    if ((sol.getVehiculo().getId().equals(getInstance().getVehiculo().getId()))) {
                        System.out.println("\n\nentro a comparar en veh.....");
                        lss.add(sol);
                    }
                }

            }

        } else {
            vehiculo = new Vehiculo();
            getInstance().setVehiculo(vehiculo);
            System.out.println("vevevevev" + vehiculo);
            System.out.println("getinstancevehiculo" + getInstance().getVehiculo());
            if (solicitudrep != null) {
                System.out.println("solicitud.vehiculo " + solicitudrep.getVehiculo());
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
        listaRequisicion.clear();
        for (Requisicion requisicion : findAll(Requisicion.class)) {
            if (requisicion.isEstado()) {
                if (!requisicion.getAprobado()) {
                    listaRequisicion.add(requisicion);
                } else {
                    listaRequisicionAprobada.add(requisicion);
                }
            }

        }
        listaRequisicion2 = listaRequisicion;
       List<SolicitudReparacionMantenimiento> ls = servgen.buscarTodos(SolicitudReparacionMantenimiento.class);
        listaSolicitudes.clear();
        for (SolicitudReparacionMantenimiento sol : ls) {
            System.out.println("iddddddddddddddd" + sol);
            if (sol.getRequisicionId() == null && sol.isEstado()) {
                System.out.println("entro" + sol);

                listaSolicitudes.add(sol);

            }

        }
        listaSolicitudes2 = listaSolicitudes;
        System.out.println("lsiat de solicitudes" + listaSolicitudes);
        vehiculo = new Vehiculo();
        idVehiculo = 0l;
        getInstance().setTipoRequisicion("Requisición de Reparación");
        listaPartida = findAll(PartidaContabilidad.class);
        listaProductos = new ArrayList<Producto>();
        cir = new ControladorItemRequisicion();
        cir.setInstance(new ItemRequisicion());
        cir.getInstance().setCantidad(0);
        cir.getInstance().setUnidadMedida(" ");
        cir.getInstance().setDescription(" ");
        maximo = 100;
        pro = new Producto();
        pro.setCodigo("");
        palabrab = "";
        solicitudrep = new SolicitudReparacionMantenimiento();
        listaPersonal = findAll(Profile.class);
        vehiculos = findAll(Vehiculo.class);
        listaItemsRequisicion = new ArrayList<ItemRequisicion>();
        idPartidaC = 0l;
        idPersonal = 0l;
        getInstance().setTipoAdquisicion("");
        getInstance().setObservaciones("");
        itemsEliminar = new ArrayList<ItemRequisicion>();
        solRequisicion = new SolicitudReparacionMantenimiento();
    }

    @Override
    protected Requisicion createInstance() {
        //prellenado estable para cualquier clase 
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(Requisicion.class.getName());
        Date now = Calendar.getInstance().getTime();
        Requisicion requisicion = new Requisicion();
        requisicion.setCreatedOn(now);
        requisicion.setFechaRequisicion(now);
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
        System.out.println("vehiculosss" + getInstance().getVehiculo());
        if (getInstance().getVehiculo() != null) {
            if (getInstance().getVehiculo().getId() != null) {
                System.out.println("entro a fijar vehciulo\n\n\n\n\n" + getInstance().getVehiculo());
                System.out.println("vehiculo" + vehiculo);
                getInstance().setVehiculo(vehiculo);
            } else {
                getInstance().setVehiculo(null);
            }

        } else {
            getInstance().setVehiculo(null);
        }

        System.out.println("PRESENTADNOIDE requisicion>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>...>" + vehiculo);
        PartidaContabilidad p = servgen.buscarPorId(PartidaContabilidad.class, idPartidaC);
        Profile psolicita = servgen.buscarPorId(Profile.class, idPersonal);
        getInstance().setPartidaContabilidad(p);
        getInstance().setPsolicita(psolicita);
        System.out.println("id de ala partidaaaaaaaaaaaaa" + p.getId());

        try {
            if (getInstance().isPersistent()) {
                guardarItem();
                System.out.println("solicitudrepapapapapa" + solicitudrep);
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
                if (solRequisicion.getId() != null) {
                    if (!solRequisicion.getId().equals(solicitudrep.getId())) {
                        solRequisicion.setLastUpdate(now);
                        try {
                            System.out.println("guaradar solRequisicion sin reqqq" + solRequisicion);
                            servgen.actualizar(solRequisicion);
                        } catch (Exception e) {
                            System.out.println("errorrrrr solRequisicion");
                            e.printStackTrace();
                        }
                    }

                }
                System.out.println("ingresa a editar>>>>>>>");
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizo Requisicion" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            } else {
                System.out.println("ingresa a creaaar>>>>>>>");
                getInstance().setEstado(true);
                System.out.println("\n\n\n\ncrea requi con estado" + getInstance().isEstado());
                //  getInstance().setEstado(true);
                guardarItem();
                create(getInstance());

                save(getInstance());
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
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se creo una nueva Requisicion" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("EROOR en al cera requisicion");
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
        }

        System.out.println("salido de gusrdar " + getInstance());
        return "/paginas/secretario/requisicion/lista.xhtml?faces-redirect=true";

    }

    public void guardarRequisicion() {
        System.out.println("\n\n\n entro a guardar REQUISISIOCN\n");
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        if (!this.listaItemsRequisicion.isEmpty()) {
            System.out.println("PRESENTADNOIDE requisicion>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>...>" + vehiculo);
            PartidaContabilidad p = servgen.buscarPorId(PartidaContabilidad.class, idPartidaC);
            Profile psolicita = servgen.buscarPorId(Profile.class, idPersonal);
            getInstance().setPartidaContabilidad(p);
            getInstance().setPsolicita(psolicita);
            String observacion = getInstance().getObservaciones();
            getInstance().setObservaciones(observacion);
            System.out.println("id de ala partidaaaaaaaaaaaaa" + p);
            System.out.println("fijo observacion>>>>" + getInstance().getObservaciones());

            try {
                listaProductos.clear();
                List<ItemRequisicion> lir = new ArrayList<ItemRequisicion>();
                for (ItemRequisicion apm : getInstance().getListaItems()) {
                    if (apm.getProducto() != null) {
                        System.out.println("aniadio producto");
                        listaProductos.add(apm.getProducto());
                    }

                    System.out.println("entrofor" + apm);

                    apm.setRequisicion(getInstance());//fijarle un plan de mantenimiento a cada actividad de plan de mantenimiento
                    //citemsolicitud.setInstance(apm);//fija la actividad del plan de mantenimiento al controlador de actividad de plan de mantenimiento

                    System.out.println("al crear");
                    BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(ItemRequisicion.class.getName());

                    apm.setCreatedOn(now);
                    apm.setLastUpdate(now);
                    apm.setActivationTime(now);
                    apm.setType(_type);
                    apm.buildAttributes(bussinesEntityService);  //

                    System.out.println("creo instance" + apm);

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
                System.out.println("ingresa a creaaar>>>>>>>");
                getInstance().setEstado(true);
                System.out.println("\n\n\n\ncrea requi con estado" + getInstance().isEstado());

                create(getInstance());
                save(getInstance());

                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se creo una nueva Requisicion  " + getInstance().getNumRequisicion() + "  con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("EROOR en al cera requisicion");
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getNumRequisicion(), " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            }

            System.out.println("salido de gusrdar " + getInstance());
            listaRequisicion.clear();
            List<Requisicion> lr = findAll(Requisicion.class);
            System.out.println("antes del for>>>>>>>");
            for (Requisicion req : lr) {

                if (req.isEstado() && req.getSolicitudReparacionId() == null && req.getVehiculo().getId().equals(getInstance().getVehiculo().getId())) {
                    System.out.println("entro a comparar>>>>>");
                    System.out.println("listatesssa" + listaRequisicion);
                    listaRequisicion.add(req);

                }

            }
            listaRequisicion.add(getInstance());
            System.out.println("lista de requisiciones>>>>>" + listaRequisicion);
            listaItemsRequisicion = null;

        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: ", " La lista de items de la requisición se encuentra vacia ");
            FacesContext.getCurrentInstance().addMessage("", msg);

        }

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
        return "/paginas/secretario/requisicion/lista.xhtml?faces-redirect=true";
    }

}
