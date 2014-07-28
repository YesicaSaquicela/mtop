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
import org.mtop.modelo.Producto;
import org.mtop.modelo.Producto_;
import org.mtop.modelo.Requisicion_;
import org.mtop.modelo.SolicitudReparacionMantenimiento;
import org.mtop.modelo.SolicitudReparacionMantenimiento_;
import org.mtop.modelo.Vehiculo_;
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

    private List<SolicitudReparacionMantenimiento> listaSolicitudes;
    private SolicitudReparacionMantenimiento solicitudrep;
    List<ItemRequisicion> listaItemsRequisicion = new ArrayList<ItemRequisicion>();
    Producto pro;

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
        requisicion.setLastUpdate(now);
        requisicion.setEstado(false);
        SolicitudReparacionMantenimiento s = requisicion.getSolicitudReparacionId();
        requisicion.setSolicitudReparacionId(null);
        listaProductos.clear();
        for (ItemRequisicion itemr : requisicion.getListaItems()) {
            System.out.println("sdhhfds"+itemr);
            System.out.println("el prodc"+itemr.getProducto());
                    
           if(itemr.getProducto()!=null){
                listaProductos.add(itemr.getProducto());
            }
            
        }
        System.out.println("pro"+listaProductos);
        if (!listaProductos.isEmpty()) {
            for (Producto prod : listaProductos) {
                System.out.println("prod id"+prod);
                pro = servgen.buscarPorId(Producto.class, prod.getId());
                pro.setCantidad(pro.getCantidad() + prod.getCantidad());
                pro.setLastUpdate(now);
                save(pro);
            }

        }

        save(requisicion);
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
            ced.add(producto.getDescription());
        }
        List<Producto> lc = servgen.buscarTodoscoincidencia(Producto.class, Producto.class.getSimpleName(), Producto_.codigo.getName(), palabrab);
        for (Producto producto : lc) {
            System.out.println("econtro uno " + producto.getCodigo());
            ced.add(producto.getCodigo());
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
                context.addMessage(null, new FacesMessage("INFORMAdN: Ingrese algun valor a buscar"));
                palabrabv = " ";
            } else {
                context.addMessage(null, new FacesMessage("INFORMACION: No se ha encontrado " + palabrab));
            }

        } else {
            vehiculos = lv;
        }
        System.out.println("\n\n\n busco \n\n" + vehiculo);
        System.out.println("listaaaaa autocompletar" + vehiculos);
    }

    public void buscarp() {
        palabrabp = palabrabp.trim();
        if (palabrabp == null || palabrabp.equals("")) {
            palabrabp = "Ingrese algun valor a buscar";
        }
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
                context.addMessage(null, new FacesMessage("INFORMACION: Ingrese algun valor a buscar"));
                palabrabp = " ";
            } else {
                context.addMessage(null, new FacesMessage("INFORMACION: No se ha encontrado " + palabrab));
            }

        } else {
            listaProductos = lp;
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
        palabrab = "";
        List<Producto> lp = servgen.buscarTodos(Producto.class);
        listaProductos.clear();
        System.out.println("lppp" + lp);

        for (Producto produ : lp) {
            if (produ.isEstado()) {
                System.out.println("listatesssa" + listaProductos);
                listaProductos.add(produ);

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
        return listaItemsRequisicion;
    }

    public void setListaItemsRequisicion(List<ItemRequisicion> listaItemsRequisicion) {
        this.listaItemsRequisicion = listaItemsRequisicion;
    }

    public SolicitudReparacionMantenimiento getSolicitudrep() {
        return solicitudrep;
    }

    public void setSolicitudrep(SolicitudReparacionMantenimiento solicitudrep) {
        this.solicitudrep = solicitudrep;
    }

    public List<SolicitudReparacionMantenimiento> getListaSolicitudes() {
        for (SolicitudReparacionMantenimiento sol : listaSolicitudes) {
            System.out.println("id vehiculo soli" + sol.getVehiculo().getId());
            System.out.println("id vehiculo req" + getInstance().getVehiculo().getId());
            if (sol.getVehiculo().getId() != this.vehiculo.getId()) {
                listaSolicitudes.remove(sol);
            }
        }
        return listaSolicitudes;
    }

    public void setListaSolicitudes(List<SolicitudReparacionMantenimiento> listaSolicitudes) {
        this.listaSolicitudes = listaSolicitudes;
    }

    public void buscar() {
        if (palabrab == null || palabrab.equals("") || palabrab.contains(" ")) {
            palabrab = "Ingrese algun valor a buscar";
        }
        //buscando por coincidencia
        List<Requisicion> le = servgen.buscarTodoscoincidencia(Requisicion.class, Requisicion.class.getSimpleName(), Requisicion_.numRequisicion.getName(), palabrab);
        //buscando por fechas
        List<Requisicion> lef = servgen.buscarRequisicionporFecha(Requisicion_.fechaRequisicion.getName(), palabrab);
        for (Requisicion requisicion : lef) {
            if (!le.contains(requisicion)) {
                le.add(requisicion);
            }
        }

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
        if (le.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrab.equals("Ingrese algun valor a buscar")) {
                context.addMessage(null, new FacesMessage("INFORMACION: Ingrese algun valor a buscar"));
                palabrab = " ";
            } else {
                context.addMessage(null, new FacesMessage("INFORMACION: No se ha encontrado " + palabrab));
            }

        } else {
            listaRequisicion = le;
            palabrab = "";
        }

    }

    public void limpiar() {
        palabrab = "";
        List<Requisicion> lr = servgen.buscarTodos(Requisicion.class);
        listaRequisicion.clear();
        System.out.println("lppp" + lr);

        for (Requisicion r : lr) {
            if (r.isEstado()) {
                System.out.println("listatesssa" + listaRequisicion);
                listaRequisicion.add(r);

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

        List<Requisicion> lr = servgen.buscarTodoscoincidencia(Requisicion.class, Requisicion.class.getSimpleName(), Requisicion_.numRequisicion.getName(), query);

        for (Requisicion requisicion : lr) {
            System.out.println("econtro uno " + requisicion.getNumRequisicion());
            ced.add(requisicion.getNumRequisicion());
        }

        List<Requisicion> lef = servgen.buscarRequisicionporFecha(Requisicion_.fechaRequisicion.getName(), query);
        for (Requisicion requisicion : lef) {
            ced.add(requisicion.getFechaRequisicion().toString());
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
        if (palabrab == null || palabrab.equals("") || palabrab.contains(" ")) {
            palabrab = "Ingrese algun valor a buscar";
        }
        //buscando por coincidencia
        List<SolicitudReparacionMantenimiento> le = servgen.buscarTodoscoincidencia(SolicitudReparacionMantenimiento.class, SolicitudReparacionMantenimiento.class.getSimpleName(), SolicitudReparacionMantenimiento_.numSolicitud.getName(), palabrab);
        //buscando por fechas
        List<SolicitudReparacionMantenimiento> lef = servgen.buscarSolicitudporFecha(SolicitudReparacionMantenimiento_.fechaSolicitud.getName(), palabrab);
        for (SolicitudReparacionMantenimiento solicitudrm : lef) {
            if (!le.contains(solicitudrm)) {
                le.add(solicitudrm);
            }
        }

        if (le.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrab.equals("Ingrese algun valor a buscar")) {
                context.addMessage(null, new FacesMessage("INFORMACION: Ingrese algun valor a buscar"));
                palabrab = " ";
            } else {
                context.addMessage(null, new FacesMessage("INFORMACION: No se ha encontrado " + palabrab));
            }

        } else {
            listaSolicitudes = le;
            palabrab = "";
        }

    }

    public void limpiars() {
        palabrab = "";
        List<SolicitudReparacionMantenimiento> ls = servgen.buscarTodos(SolicitudReparacionMantenimiento.class);
        listaSolicitudes.clear();
        System.out.println("lppp" + ls);

        for (SolicitudReparacionMantenimiento soli : ls) {
            if (soli.isEstado()) {
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

        for (SolicitudReparacionMantenimiento soli : le) {
            System.out.println("econtro uno " + soli.getNumSolicitud());
            ced.add(soli.getNumSolicitud());
        }
        List<SolicitudReparacionMantenimiento> lef = servgen.buscarSolicitudporFecha(SolicitudReparacionMantenimiento_.fechaSolicitud.getName(), query);

        for (SolicitudReparacionMantenimiento soli : lef) {
            ced.add(soli.getFechaSolicitud().toString());
        }

        System.out.println("listaaaaa autocompletar" + ced);
        return ced;

    }

    public Integer getMaximo() {
        if (getInstance().getTipoAdquisicion().equals("bodega")) {
            if (pro.getId() != null) {
                System.out.println("enrra a obtener cantidaaaaa>>>>>>>>>>>>>>>>>>>>>>>");
                setMaximo(pro.getCantidad());
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
        cir.getInstance().setCantidad(1);
        cir.getInstance().setUnidadMedida(" ");
        cir.getInstance().setDescription(pro.getDescription());
        cir.getInstance().setProducto(pro);
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
            for (Producto p : findAll(Producto.class)) {

                if (p.getCantidad() > 0) {

                    listaProductos.add(p);

                }
            }
        }

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
        if (getId() == null) {
            System.out.println("numero" + getInstance().getNumRequisicion());
            List<Requisicion> lista = findAll(Requisicion.class);
            int t;
            if (getInstance().getTipoAdquisicion() != null) {
                if (getInstance().getTipoRequisicion().equals("Requisición de Bienes y Servicios")) {
                    t = lista.size() + 200;
                } else {
                    t = lista.size() + 900;
                }
            } else {
                t = lista.size() + 900;
            }

            setNumeroRequisicion(String.valueOf(t + 1));

        } else {
            setNumeroRequisicion(getInstance().getNumRequisicion());
        }

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
            cir.getInstance().setLastUpdate(now);
            cir.guardar();
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
    }

    public void agregarItem() {
        String des = cir.getInstance().getDescription().trim();
        String uni = cir.getInstance().getUnidadMedida().trim();
        if (cir.getInstance().getCantidad().equals(0) || des.equals("") || uni.equals("")) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "campos abligatorios, cantidad, descripción y unidad de medida"));
            System.out.println("\n\n\n\n no entro guardarrrrr\n\n\n\n");
        } else {
            if (pro != null) {
                if (!pro.getCodigo().equals("")) {

                    int i = listaProductos.lastIndexOf(pro);
                    pro.setCantidad(pro.getCantidad() - cir.getInstance().getCantidad());
                    listaProductos.set(i, pro);
                }
                System.out.println("\n\nanadioooo\n\n\n" + cir.getInstance());
            }
            listaItemsRequisicion.add(cir.getInstance());
            cir.setInstance(new ItemRequisicion());

        }

        pro = new Producto();
        pro.setCodigo("");
        cir.getInstance().setCantidad(0);
        cir.getInstance().setUnidadMedida(" ");
        cir.getInstance().setDescription(" ");
        editarItem(cir.getInstance());

    }

    public void editarItem(ItemRequisicion itemReq) {

        int con = 0;
        List<ItemRequisicion> lir = listaItemsRequisicion;
        for (ItemRequisicion i : lir) {

            if (i.getCantidad().equals(itemReq.getCantidad())
                    && i.getUnidadMedida().equals(itemReq.getUnidadMedida())) {
                listaItemsRequisicion.remove(con);
                System.out.println("a fijar cant" + i.getCantidad());
                System.out.println("a fijar unid" + i.getUnidadMedida());
                cir.setInstance(i);
                if (itemReq != null) {
                    pro = itemReq.getProducto();

                    int j = listaProductos.lastIndexOf(pro);
                    System.out.println("pro" + pro.getCantidad());
                    System.out.println("pro de lista" + pro.getCantidad());
                    pro.setCantidad(itemReq.getCantidad() + listaProductos.get(j).getCantidad());
                    listaProductos.set(j, pro);
                    pro = listaProductos.get(j);

                    System.out.println("a fijada" + cir.getInstance().getCantidad());
                    System.out.println("a fijada" + cir.getInstance().getUnidadMedida());

                }
                break;

            }
            con++;

        }

    }

    public void eliminarItem(ItemRequisicion itemReq) {

        int con = 0;
        for (ItemRequisicion i : listaItemsRequisicion) {

            if (i.getCantidad().equals(itemReq.getCantidad())
                    && i.getUnidadMedida().equals(itemReq.getUnidadMedida())) {
                listaItemsRequisicion.remove(con);
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

    public String onFlowProcess(FlowEvent event) {
        System.out.println("\nn\n\n\nentro flow proces \nn\n\n\n" + this.vehiculo);

        if (skip) {
            skip = false;   //reset in case user goes back  

            return "confirm";
        } else {
            System.out.println("pasoooo");
            if (getInstance().getId() != null) {
                this.cir.setListaItemsRequisicion(getInstance().getListaItems());
            }
            if (event.getOldStep().equals("address")) {
                System.out.println("id d vechicilooo\n\n\n\n" + this.vehiculo);
                System.out.println("id tipod e re q escogida" + getInstance().getTipoRequisicion());
                if (this.vehiculo.getId() == null && getInstance().getTipoRequisicion().equals("requisicionRep")) {

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "debe escoger un vehiculo"));

                    return event.getOldStep();
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

        vehiculo = getInstance().getVehiculo();

        solicitudrep = getInstance().getSolicitudReparacionId();
        idPersonal = getInstance().getPsolicita().getId();
        listaItemsRequisicion = new ArrayList<ItemRequisicion>();
        for (ItemRequisicion itr : findAll(ItemRequisicion.class)) {
            System.out.println("entra al for");
            if (getInstance().getId() != null) {

                System.out.println("idde itr" + itr.getRequisicion().getId());
                if (getInstance().getId().equals(itr.getRequisicion().getId())) {
                    listaItemsRequisicion.add(itr);
                }

            }
        }

    }

    public Long getIdVehiculo() {
        return idVehiculo;
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
        System.out.println("jsjsjsjsjsjsjjsjsjsjjsjsjjs");

        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        System.out.println("entra a fijar un vehiculo con su iddd" + vehiculo.getId());

        this.vehiculo = vehiculo;
        getInstance().setVehiculo(vehiculo);
        palabrab = "";
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
        listaItemsRequisicion = getInstance().getListaItems();
    }

    @PostConstruct
    public void init() {
        setEntityManager(em);
        /*el bussinesEntityService.setEntityManager(em) solo va si la Entidad en este caso (ConsultaMedia)
         *hereda de la Entidad BussinesEntity...  caso contrario no se lo agrega
         */
        bussinesEntityService.setEntityManager(em);
        servgen.setEm(em);
        listaRequisicion = findAll(Requisicion.class);
        listaSolicitudes = findAll(SolicitudReparacionMantenimiento.class);
        listaRequisicion.clear();
        for (Requisicion requisicion : findAll(Requisicion.class)) {

            if (!requisicion.getAprobado()) {

                listaRequisicion.add(requisicion);
            }
        }

        List<SolicitudReparacionMantenimiento> ls = servgen.buscarTodos(SolicitudReparacionMantenimiento.class);
        listaSolicitudes.clear();
        for (SolicitudReparacionMantenimiento sol : ls) {
            System.out.println("iddddddddddddddd" + sol);
            if (sol.getRequisicionId() == null) {
                System.out.println("entro" + sol);
                listaSolicitudes.add(sol);
            }

        }
        System.out.println("lsiat de solicitudes" + listaSolicitudes);
        vehiculo = new Vehiculo();
        idVehiculo = 0l;

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
        if (getInstance().getVehiculo() != null) {
            System.out.println("entro a fijar vehciulo\n\n\n\n\n" + getInstance().getVehiculo());
            System.out.println("vehiculo" + vehiculo);
            getInstance().setVehiculo(vehiculo);
        }

        System.out.println("PRESENTADNOIDE requisicion>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>...>" + vehiculo);
        PartidaContabilidad p = servgen.buscarPorId(PartidaContabilidad.class, idPartidaC);
        Profile psolicita = servgen.buscarPorId(Profile.class, idPersonal);
        getInstance().setPartidaContabilidad(p);
        getInstance().setPsolicita(psolicita);
        System.out.println("id de ala partidaaaaaaaaaaaaa" + p.getId());
        if (solicitudrep != null) {
            if (solicitudrep.getId() != null) {
                System.out.println("anaidiando la solicitud" + solicitudrep);
                getInstance().setSolicitudReparacionId(solicitudrep);
                solicitudrep.setRequisicionId(getInstance());
                solicitudrep.setLastUpdate(now);
                try {
                    servgen.actualizar(solicitudrep);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getInstance().setSolicitudReparacionId(solicitudrep);

            }
        }

        try {
            if (getInstance().isPersistent()) {
                guardarItem();
                System.out.println("ingresa a editar>>>>>>>");
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizo Requisicion" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            } else {
                System.out.println("ingresa a creaaar>>>>>>>");
                getInstance().setEstado(true);
                //  getInstance().setEstado(true);
                create(getInstance());
                guardarItem();
                save(getInstance());
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

    @Transactional
    public String borrarEntidad() {
        //       log.info("sgssalud --> ingreso a eliminar: " + getInstance().getId());
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
