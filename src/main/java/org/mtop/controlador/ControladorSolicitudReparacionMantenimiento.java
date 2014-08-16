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

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import org.jboss.seam.transaction.Transactional;
import org.jfree.chart.block.Arrangement;
import org.mtop.cdi.Web;
import org.mtop.controlador.dinamico.BussinesEntityHome;
import org.mtop.modelo.ActividadPlanMantenimiento;
import org.mtop.modelo.ItemRequisicion;
import org.mtop.modelo.ItemSolicitudReparacion;
import org.mtop.modelo.PartidaContabilidad;
import org.mtop.modelo.Producto;
import org.mtop.modelo.Requisicion;
import org.mtop.modelo.Requisicion_;
import org.mtop.modelo.dinamico.BussinesEntityType;
import org.mtop.modelo.SolicitudReparacionMantenimiento;
import org.mtop.modelo.SolicitudReparacionMantenimiento_;
import static org.mtop.modelo.SolicitudReparacionMantenimiento_.vehiculo;
import org.mtop.modelo.Vehiculo;
import org.mtop.modelo.Vehiculo_;
import org.mtop.modelo.profile.Profile;
import org.mtop.servicios.ServicioGenerico;
import org.mtop.util.UI;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author carlis
 */
@Named
@ViewScoped
public class ControladorSolicitudReparacionMantenimiento extends BussinesEntityHome<SolicitudReparacionMantenimiento> implements Serializable {

    @Inject
    @Web
    private EntityManager em;
    @EJB
    private ServicioGenerico servgen;
    private List<SolicitudReparacionMantenimiento> listaSolicitud = new ArrayList<SolicitudReparacionMantenimiento>();
    private Long idVehiculo;
    private Vehiculo vehiculo;
    private List<Vehiculo> listaVehiculos;
    private ControladorItemSolicitud citemsolicitud;
    private List<Requisicion> listaRequisiciones;
    private Requisicion requisicion;
    private boolean skip;
    private List<ItemSolicitudReparacion> listaItemsSolicitud;
    private String palabrabv = "";
    private String palabrab = "";
    private long idPersona = 0l;
    private String palabrabr = "";
    private List<SolicitudReparacionMantenimiento> listaSolicitudAprobadas = new ArrayList<SolicitudReparacionMantenimiento>();
    private String vista;
    private Requisicion reqSolicitud;
    private Requisicion nuevaRequisicion = new Requisicion();
    private ItemSolicitudReparacion itemsr;
    private List<ItemSolicitudReparacion> itemsEliminar;
    private String wiz = "";

    public String getWiz() {
        return wiz;
    }

    public void setWiz(String wiz) {
        this.wiz = wiz;
    }

    public List<ItemSolicitudReparacion> getItemsEliminar() {
        return itemsEliminar;
    }

    public void setItemsEliminar(List<ItemSolicitudReparacion> itemsEliminar) {
        this.itemsEliminar = itemsEliminar;
    }

//    private long idRequisicion;
//
//    public long getIdRequisicion() {
//        return idRequisicion;
//    }
//
//    public void setIdRequisicion(long idRequisicion) {
//         System.out.println("llega id>>>>>"+idRequisicion);
//        this.idRequisicion = idRequisicion;
//        getInstance().setRequisicionId(findById(Requisicion.class, idRequisicion));
//        System.out.println("fijado en instance>>>>>"+getInstance().getRequisicionId());
//    }
    public Requisicion getNuevaRequisicion() {
        return nuevaRequisicion;
    }

    public void setNuevaRequisicion(Requisicion nuevaRequisicion) {
        System.out.println("\n\n\n\n\nfija solicituddddd" + nuevaRequisicion.getObservaciones());
        this.nuevaRequisicion = nuevaRequisicion;
    }

    public Requisicion getReqSolicitud() {
        return reqSolicitud;
    }

    public void setReqSolicitud(Requisicion reqSolicitud) {
        this.reqSolicitud = reqSolicitud;
    }

    public String getVista() {
        return vista;
    }

    public void setVista(String vista) {
        this.vista = vista;
        System.out.println("retornando vista>>>>>" + vista);
        System.out.println("nueva requi en set vista" + nuevaRequisicion.getObservaciones());
    }

    public List<ItemSolicitudReparacion> getListaItemsSolicitud() {
        return listaItemsSolicitud;
    }

    public void setListaItemsSolicitud(List<ItemSolicitudReparacion> listaItemsSolicitud) {
        this.listaItemsSolicitud = listaItemsSolicitud;
    }

    public List<SolicitudReparacionMantenimiento> getListaSolicitudAprobadas() {
        return listaSolicitudAprobadas;
    }

    public void setListaSolicitudAprobadas(List<SolicitudReparacionMantenimiento> listaSolicitudAprobadas) {
        this.listaSolicitudAprobadas = listaSolicitudAprobadas;
    }

    public String getPalabrabr() {
        return palabrabr;
    }

    public void setPalabrabr(String palabrabr) {
        this.palabrabr = palabrabr;
    }

    public List<Profile> getListaPersonal() {
        return listaPersonal;
    }

    public void setListaPersonal(List<Profile> listaPersonal) {
        this.listaPersonal = listaPersonal;
    }
    List<Profile> listaPersonal;

    public long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(long idPersona) {
        this.idPersona = idPersona;
        Profile psolicita = servgen.buscarPorId(Profile.class, idPersona);
        getInstance().setPsolicita(psolicita);
    }

    public String getPalabrab() {
        return palabrab;
    }

    public void setPalabrab(String palabrab) {
        this.palabrab = palabrab;
    }

    public String getPalabrabv() {
        return palabrabv;
    }

    public void setPalabrabv(String palabrabv) {
        this.palabrabv = palabrabv;
    }

    public void buscarr() {
        System.out.println("\n\n\nENTRO A BUSCAR REQU11111I>>>>>>");
        if (palabrabr == null || palabrabr.equals("") || palabrabr.contains(" ")) {
            palabrabr = "Ingrese algun valor a buscar";
        }
        //buscando por coincidencia
        List<Requisicion> le = servgen.buscarTodoscoincidencia(Requisicion.class, Requisicion.class.getSimpleName(), Requisicion_.numRequisicion.getName(), palabrabr);
        //buscando por fechas
        List<Requisicion> lef = servgen.buscarRequisicionporFecha(Requisicion_.fechaRequisicion.getName(), palabrabr);
        System.out.println("Todas>>>>>>" + lef);
        System.out.println("Todascoincidencia>>>>>>" + le);
        List<Long> lrq = new ArrayList<Long>();
        for (Requisicion r : le) {
            lrq.add(r.getId());
        }
        for (Requisicion requis : lef) {
            System.out.println("rerere " + requis);
            System.out.println("lrq>>>> " + lrq);

            if (!lrq.contains(requis.getId())) {
                System.out.println("entro al if>>>>>>>");
                lrq.add(requis.getId());
            }
        }

        System.out.println("LEeeee" + lrq);

        if (lrq.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrabr.equals("Ingrese algun valor a buscar")) {
                context.addMessage(null, new FacesMessage("INFORMACION: Ingrese algun valor a buscar"));
                palabrabr = " ";
            } else {
                context.addMessage(null, new FacesMessage("INFORMACION: No se ha encontrado " + palabrabr));
            }

        } else {
            listaRequisiciones.clear();
            Requisicion re = new Requisicion();
            System.out.println("lista que esta recorriendo>>>>>" + lrq);
            for (Long r : lrq) {
                System.out.println("Entro al for>>>>>>");
                re = findById(Requisicion.class, r);
                System.out.println("estado>>>>" + re.isEstado());
                System.out.println("vehiculo>>>>>>>>>" + getInstance().getVehiculo().getId());
                System.out.println("vehiculo>>>>>>>>>" + re.getVehiculo().getId());
                System.out.println("numero de requisicion>>>>" + re.getNumRequisicion());
                if (re.getSolicitudReparacionId() == null && re.isEstado() && re.getVehiculo().getId().equals(getInstance().getVehiculo().getId())) {
                    System.out.println("entro a comparacion>>>>>");
                    listaRequisiciones.add(re);
                    System.out.println("\n\nlista final>>>>" + listaRequisiciones);

                }
            }

            palabrabr = "";
        }

    }

    public void limpiarr() {
        palabrabr = "";
        List<Requisicion> lr = servgen.buscarTodos(Requisicion.class);
        listaRequisiciones.clear();
        System.out.println("lppp" + lr);

        for (Requisicion r : lr) {
            if (r.isEstado() && r.getSolicitudReparacionId() == null && r.getVehiculo().getId().equals(getInstance().getVehiculo().getId())) {
                System.out.println("listatesssa" + listaRequisiciones);
                listaRequisiciones.add(r);

            }

        }
    }

    public ArrayList<String> autocompletarr(String query) {
        System.out.println("\n\nQUEryyyyy" + query);

        ArrayList<String> ced = new ArrayList<String>();

        List<Requisicion> lr = servgen.buscarTodoscoincidencia(Requisicion.class, Requisicion.class.getSimpleName(), Requisicion_.numRequisicion.getName(), query);

        for (Requisicion requisicion : lr) {
            if (requisicion.getVehiculo().getId().equals(getInstance().getVehiculo().getId()) && requisicion.isEstado() && !ced.contains(requisicion.getNumRequisicion()) && requisicion.getSolicitudReparacionId() == null) {
                System.out.println("\n\necontro uno " + requisicion.getNumRequisicion());
                ced.add(requisicion.getNumRequisicion());
            }

        }

        List<Requisicion> lef = servgen.buscarRequisicionporFecha(Requisicion_.fechaRequisicion.getName(), query);
        for (Requisicion requisicion : lef) {
            if (requisicion.getVehiculo().getId().equals(getInstance().getVehiculo().getId()) && requisicion.isEstado() && !ced.contains(requisicion.getFechaRequisicion().toString())
                    && requisicion.getSolicitudReparacionId() == null) {
                ced.add(requisicion.getFechaRequisicion().toString());
            }

        }

        System.out.println("\n\nlistaaaaa autocompletar" + ced);
        return ced;

    }

    public void buscar() {
        if (palabrab == null || palabrab.equals("") || palabrab.contains(" ")) {
            palabrab = "Ingrese algun valor a buscar";
        }
        //buscando por coincidencia
        List<SolicitudReparacionMantenimiento> le = servgen.buscarTodoscoincidencia(SolicitudReparacionMantenimiento.class, SolicitudReparacionMantenimiento.class.getSimpleName(), SolicitudReparacionMantenimiento_.numSolicitud.getName(), palabrab);
        //buscando por fechas
        List<SolicitudReparacionMantenimiento> lef = servgen.buscarSolicitudporFecha(SolicitudReparacionMantenimiento_.fechaSolicitud.getName(), palabrab);
        System.out.println("Todas>>>>>>" + lef);
        System.out.println("Todascoincidencia>>>>>>" + le);
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
            listaSolicitud.clear();
            listaSolicitudAprobadas.clear();
            SolicitudReparacionMantenimiento solRM = new SolicitudReparacionMantenimiento();
            for (Long s : lsoli) {
                solRM = findById(SolicitudReparacionMantenimiento.class, s);
                if (solRM.isEstado()) {
                    if (solRM.getAprobado()) {
                        if (!listaSolicitudAprobadas.contains(s)) {

                            listaSolicitudAprobadas.add(solRM);
                        }

                    } else {
                        if (!listaSolicitud.contains(s)) {
                            listaSolicitud.add(solRM);
                        }

                    }
                }
            }

            palabrab = "";
        }
    }

    public void limpiar() {
        palabrab = "";
        List<SolicitudReparacionMantenimiento> ls = servgen.buscarTodos(SolicitudReparacionMantenimiento.class);
        listaSolicitud.clear();
        listaSolicitudAprobadas.clear();
        System.out.println("lppp" + ls);

        for (SolicitudReparacionMantenimiento soli : ls) {
            if (soli.isEstado()) {
                System.out.println("entro a estado true");
                if (soli.getAprobado()) {
                    System.out.println("listatesssa" + listaSolicitud);
                    listaSolicitudAprobadas.add(soli);

                } else {
                    listaSolicitud.add(soli);
                }

            }

        }
    }
//falta que cuando vaya autocompletar solo presente una coincidenci ay no repita

    public ArrayList<String> autocompletar(String query) {
        System.out.println("QUEryyyyy" + query);

        ArrayList<String> ced = new ArrayList<String>();

        List<SolicitudReparacionMantenimiento> le = servgen.buscarTodoscoincidencia(SolicitudReparacionMantenimiento.class, SolicitudReparacionMantenimiento.class.getSimpleName(), SolicitudReparacionMantenimiento_.numSolicitud.getName(), query);
        //buscando por fechas

        for (SolicitudReparacionMantenimiento soli : le) {
            if (soli.isEstado() && !ced.contains(soli.getNumSolicitud())) {
                System.out.println("econtro uno " + soli.getNumSolicitud());
                ced.add(soli.getNumSolicitud());
            }

        }
        List<SolicitudReparacionMantenimiento> lef = servgen.buscarSolicitudporFecha(SolicitudReparacionMantenimiento_.fechaSolicitud.getName(), query);

        for (SolicitudReparacionMantenimiento soli : lef) {
            if (soli.isEstado() && !ced.contains(soli.getFechaSolicitud().toString())) {
                ced.add(soli.getFechaSolicitud().toString());
            }

        }

        System.out.println("listaaaaa autocompletar" + ced);
        return ced;

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

//
        if (lv.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrabv.equals("Ingrese algun valor a buscar")) {
                context.addMessage(null, new FacesMessage("INFORMACION: Ingrese algun valor a buscar"));
                palabrabv = " ";
            } else {
                context.addMessage(null, new FacesMessage("INFORMACION: No se ha encontrado " + palabrabv));
            }

        } else {
            listaVehiculos = lv;
        }
        System.out.println("\n\n\n busco \n\n" + vehiculo);
        System.out.println("listaaaaa autocompletar" + listaVehiculos);
    }

    public void limpiarv() {
        palabrabv = "";
        List<Vehiculo> lv = servgen.buscarTodos(Vehiculo.class);
        listaVehiculos.clear();
        System.out.println("lppp" + lv);

        for (Vehiculo vehiculo1 : lv) {
            System.out.println("iddddd" + vehiculo1.getId());
            System.out.println("entro a for lista>>>>" + vehiculo1.isEstado());
            if (vehiculo1.isEstado()) {
                System.out.println("listatesssa" + listaVehiculos);
                listaVehiculos.add(vehiculo1);

                System.out.println("Entro a remover>>>>");
                System.out.println("a;iadia" + listaVehiculos);

            }

        }
    }

    public Requisicion getRequisicion() {
        return requisicion;
    }

    public void setRequisicion(Requisicion requisicion) {
        System.out.println("llega requisicion>>>>" + requisicion);
        this.requisicion = requisicion;
        getInstance().setRequisicionId(requisicion);
        System.out.println("ide de requisicion>>>>>>" + requisicion);
        System.out.println("instance requi>>>>" + getInstance().getRequisicionId());
    }

    public List<Requisicion> getListaRequisiciones() {
        return listaRequisiciones;
    }

    public void setListaRequisiciones(List<Requisicion> listaRequisiciones) {
        this.listaRequisiciones = listaRequisiciones;
    }

    private String numeroSolicitud;

    public ControladorItemSolicitud getCitemsolicitud() {
        return citemsolicitud;
    }

    public void setCitemsolicitud(ControladorItemSolicitud citemsolicitud) {
        this.citemsolicitud = citemsolicitud;
    }

    public String getNumeroSolicitud() {
        if (getId() == null) {
            System.out.println("numero" + getInstance().getNumSolicitud());
            List<SolicitudReparacionMantenimiento> lista = findAll(SolicitudReparacionMantenimiento.class);
            int t = lista.size() + 8000;
            System.out.println("valor de t :::::::::::" + t);

            setNumeroSolicitud(String.valueOf(t + 1));

        } else {
            setNumeroSolicitud(getInstance().getNumSolicitud());
        }

        return numeroSolicitud;

    }

    public void setNumeroSolicitud(String numRegistro) {
        this.numeroSolicitud = numRegistro;
        getInstance().setNumSolicitud(this.numeroSolicitud);

    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public String onFlowProcess(FlowEvent event) {
        System.out.println("ENRTRO FLOWPROCESS" + event.getNewStep());
        System.out.println("Entro getOld" + event.getOldStep());
        System.out.println("Lista de itemmSSSS" + this.listaItemsSolicitud);
        System.out.println("vehiculo>>>>>" + getInstance().getVehiculo());
        if (wiz.equals("sol")) {
            wiz = "";
            System.out.println("retornando sol");
            return "sol";
        } else {
            System.out.println("otro retorna");
            if (skip) {
                skip = false;   //reset in case user goes back  

                return "confirm";
            } else {
                System.out.println("pasoooo");
                if (getInstance().getId() != null) {
                    this.citemsolicitud.setListaItemsSolicitud(getInstance().getListaItemSR());
                }
                if (event.getNewStep().equals("sol") && event.getOldStep().equals("address")) {
                    return event.getNewStep();
                } else {
                    if (event.getNewStep().equals("address") && event.getOldStep().equals("items")) {
                        return event.getNewStep();
                    } else {
                        if (event.getOldStep().equals("address") && getInstance().getVehiculo() == null) {
                            System.out.println("entro al mensaje vehiculo");
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "debe escoger un vehiculo"));

                            return event.getOldStep();
                        } else {

                            if (event.getOldStep().equals("items") && this.listaItemsSolicitud.isEmpty()) {
                                System.out.println("estas vaciaaaaaa");
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "debe ingresar al menos un item a la solicitud"));

                                return event.getOldStep();
                            } else {

                                return event.getNewStep();
                            }
                        }
                    }
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

    public Vehiculo getVehiculo() {

        if (getSolicitudReparacionMantenimientoId() != null) {
            System.out.println("vehiculo " + getInstance().getVehiculo());
            vehiculo = getInstance().getVehiculo();
        }

        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {

        System.out.println("vehiculo  en set" + getInstance().getVehiculo());
        System.out.println("entra a fijar un vehiculo con su iddd" + vehiculo.getId());
        this.vehiculo = vehiculo;
        getInstance().setVehiculo(vehiculo);
        System.out.println("vehicylo set>>>" + getInstance().getVehiculo());

        // for para poder cambiar el id del vehiculo y las requisiciones
        if (requisicion != null) {
            if (requisicion.getVehiculo() != getInstance().getVehiculo() && getInstance().getRequisicionId() != null) {

                getInstance().setRequisicionId(null);
                requisicion = new Requisicion();

            }
        }

        System.out.println("requisicion anterior" + requisicion);

        System.out.println("id de vehiculo actual" + getInstance().getVehiculo().getId());
        System.out.println("id de vehiculo actual1111" + this.vehiculo.getId());
        List<Requisicion> lrs = new ArrayList<Requisicion>();
        System.out.println("lista antes" + findAll(Requisicion.class));
        //mosttrar lista de requisiciones con igual vehiculo ,estado true
        for (Requisicion req : findAll(Requisicion.class)) {
            System.out.println("\n\nEntro a for requis  veh...>>>" + req.getVehiculo().getId());
            System.out.println("req.sol" + req.getSolicitudReparacionId() + "estado" + req.isEstado());
            if (req.isEstado() && req.getSolicitudReparacionId() == null) {
                System.out.println("veh instance>>>" + getInstance().getVehiculo().getId());
                if ((req.getVehiculo().getId().equals(getInstance().getVehiculo().getId()))) {
                    System.out.println("\n\nentro a comparar en veh.....");
                    lrs.add(req);
                }
            }

        }
        listaRequisiciones = lrs;
        System.out.println("lista despues en veh" + listaRequisiciones);
    }

    public List<Vehiculo> getListaVehiculos() {

        return listaVehiculos;
    }

    public void setListaVehiculos(List<Vehiculo> listaVehiculos) {
        this.listaVehiculos = listaVehiculos;
    }

    public Long getSolicitudReparacionMantenimientoId() {
        return (Long) getId();
    }

    public void setSolicitudReparacionMantenimientoId(Long solicitudReparacionMantenimientoId) {
        System.out.println("vehiculo  en set soli" + getInstance().getVehiculo());
        setId(solicitudReparacionMantenimientoId);
        vehiculo = getInstance().getVehiculo();
        if (getInstance().getRequisicionId()!=null) {
            requisicion = getInstance().getRequisicionId();
            reqSolicitud = requisicion;
            reqSolicitud.setSolicitudReparacionId(null);
        }

        idPersona = getInstance().getPsolicita().getId();
        System.out.println("entro a obtener la lista de solicitudes>>>>>." + getInstance().getListaItemSR());
        if (getInstance().isPersistent()) {
            listaItemsSolicitud = getInstance().getListaItemSR();
        }

        System.out.println("asignado lista itemss" + listaItemsSolicitud);

        System.out.println("id de vehiculo actual set soli" + getInstance().getVehiculo().getId());
        System.out.println("id de vehiculo actual1111 set soli" + this.vehiculo.getId());
        List<Requisicion> lr = findAll(Requisicion.class);
        List<Requisicion> lrq = new ArrayList<Requisicion>();
        //mosttrar lista de requisiciones con igual vehiculo ,estado true
        System.out.println("lista toda set soli>>>" + lr);
        for (Requisicion req : lr) {
            System.out.println("\n\nEntro a for requis set soli...>>>" + req.getVehiculo().getId());
            System.out.println("req setsol" + req + "estadosetsoli" + req.isEstado());
            if (req.isEstado() && req.getSolicitudReparacionId() == null) {
                if ((req.getVehiculo().getId() == getInstance().getVehiculo().getId())) {
                    System.out.println("\n\nentro a comparar set soli.....");
                    lrq.add(req);
                }
            }
        }
        listaRequisiciones = lrq;
        System.out.println("lista despues set soli" + listaRequisiciones);
    }

    public String formato(Date fecha) {
        String fechaFormato = "";
        if (fecha != null) {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            fechaFormato = formatter.format(fecha);
        }
        return fechaFormato;

    }

    @TransactionAttribute   //
    public SolicitudReparacionMantenimiento load() {
        if (isIdDefined()) {
            wire();
        }
        return getInstance();
    }

    @TransactionAttribute
    public void wire() {
        getInstance();

    }

    public List<SolicitudReparacionMantenimiento> getListaSolicitud() {
        return listaSolicitud;
    }

    public void editarItemS(ItemSolicitudReparacion itemsol) {

        int con = 0;
        List<ItemSolicitudReparacion> li = listaItemsSolicitud;
        for (ItemSolicitudReparacion items : li) {
            System.out.println("entro al for>>>>>>>");
            if (items.getDescripcionElementoRevisar().equals(itemsol.getDescripcionElementoRevisar())
                    && items.getDescripcionFalla().equals(itemsol.getDescripcionFalla())) {
                System.out.println("entro a remover>>>>>");
                listaItemsSolicitud.remove(con);
                citemsolicitud.setInstance(items);
                break;

            }
            con++;

        }

    }

    public void eliminarItemS(ItemSolicitudReparacion itemsol) {

        List<ItemSolicitudReparacion> li = new ArrayList<ItemSolicitudReparacion>();
        System.out.println("lista items" + listaItemsSolicitud);
        for (ItemSolicitudReparacion items : listaItemsSolicitud) {
            System.out.println("entro al for>>>>>>>");
            if (!items.getDescripcionElementoRevisar().equals(itemsol.getDescripcionElementoRevisar())
                    && !items.getDescripcionFalla().equals(itemsol.getDescripcionFalla())) {
                System.out.println("entro a remover>>>>>");
                li.add(items);

            } else {
                if (items.isPersistent()) {
                    itemsEliminar.add(items);

                }

            }

        }
        listaItemsSolicitud = li;
        System.out.println("lista nueva " + listaItemsSolicitud);
        getInstance().setListaItemSR(li);

    }

    public void agregarItemS() {
        System.out.println("sada" + citemsolicitud.getInstance().getDescripcionElementoRevisar());
        System.out.println("dsas" + citemsolicitud.getInstance().getDescripcionFalla());

        if (citemsolicitud.getInstance().getDescripcionElementoRevisar().equals("") || citemsolicitud.getInstance().getDescripcionFalla().equals("")) {
            System.out.println("\n\nENTRO A PRESENTAR MENSAJE>>>>>>>>>");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "campos abligatorios, elemento a revisar, descripción de la falla"));

        } else {
            System.out.println("\n\nentro a agregar>>>>>>>.");
            listaItemsSolicitud.add(citemsolicitud.getInstance());
            getInstance().setListaItemSR(listaItemsSolicitud);
            System.out.println("a;ade a lista>>>>>." + citemsolicitud.getInstance());
            citemsolicitud.setInstance(new ItemSolicitudReparacion());

        }

    }

    public ItemSolicitudReparacion getItemsr() {
        return itemsr;
    }

    public void setItemsr(ItemSolicitudReparacion itemsr) {
        this.itemsr = itemsr;
        System.out.println("fijando el item " + itemsr.getDescripcionElementoRevisar());
        citemsolicitud.setInstance(itemsr);
        agregarItemS();
    }

    public void setListaSolicitud(List<SolicitudReparacionMantenimiento> listaSolicitud) {
        this.listaSolicitud = listaSolicitud;
        listaItemsSolicitud = getInstance().getListaItemSR();

    }

    @PostConstruct
    public void init() {
        setEntityManager(em);
        /*el bussinesEntityService.setEntityManager(em) solo va si la Entidad en este caso (ConsultaMedia)
         *hereda de la Entidad BussinesEntity...  caso contrario no se lo agrega
         */

        bussinesEntityService.setEntityManager(em);
        servgen.setEm(em);
        listaSolicitud = findAll(SolicitudReparacionMantenimiento.class);
        listaRequisiciones = findAll(Requisicion.class);
        List<SolicitudReparacionMantenimiento> ls = servgen.buscarTodos(SolicitudReparacionMantenimiento.class);

        listaSolicitud.clear();
        System.out.println("cslistaaaaa solicituddd" + listaSolicitud);
        for (SolicitudReparacionMantenimiento sol : ls) {

            if (sol.isEstado()) {
                if (!sol.getAprobado()) {
                    System.out.println("entro a listar>>>>>>");
                    listaSolicitud.add(sol);
                } else {
                    listaSolicitudAprobadas.add(sol);
                }
            }

        }

        List<Requisicion> lr = servgen.buscarTodos(Requisicion.class);
        listaRequisiciones.clear();
        //for para presentar lsita de solicitudes sin requisicion
        for (Requisicion req : lr) {
            System.out.println("iddddddddddddddd" + req);
            if (req.getSolicitudReparacionId() == null && req.isEstado()) {
                System.out.println("entro" + req);
                listaRequisiciones.add(req);

            }

        }

        System.out.println("lista solicitudes sin requisicion" + listaSolicitud);
        vehiculo = new Vehiculo();
        idVehiculo = 0l;
        citemsolicitud = new ControladorItemSolicitud();
        citemsolicitud.setInstance(new ItemSolicitudReparacion());
        citemsolicitud.getInstance().setDescripcionElementoRevisar("");
        citemsolicitud.getInstance().setDescripcionFalla("");
        requisicion = new Requisicion();
        listaPersonal = findAll(Profile.class);
        listaVehiculos = findAll(Vehiculo.class);
        listaItemsSolicitud = new ArrayList<ItemSolicitudReparacion>();
        System.out.println("valor de vista en inittttt" + vista);
        itemsEliminar = new ArrayList<ItemSolicitudReparacion>();
        idPersona = 0l;
        getInstance().setRecibidor("");
        getInstance().setObservacion("");

    }

    @Override
    protected SolicitudReparacionMantenimiento createInstance() {
        //prellenado estable para cualquier clase 
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(SolicitudReparacionMantenimiento.class.getName());
        Date now = Calendar.getInstance().getTime();
        SolicitudReparacionMantenimiento solicitudRepMant = new SolicitudReparacionMantenimiento();
        solicitudRepMant.setCreatedOn(now);
        solicitudRepMant.setLastUpdate(now);
        solicitudRepMant.setFechaSolicitud(now);
        solicitudRepMant.setActivationTime(now);
        solicitudRepMant.setType(_type);
        solicitudRepMant.buildAttributes(bussinesEntityService);  //
        return solicitudRepMant;
    }

    @Override
    public Class<SolicitudReparacionMantenimiento> getEntityClass() {
        return SolicitudReparacionMantenimiento.class;
    }

    @TransactionAttribute
    public String guardar() {

        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);

        System.out.println("vehiculo antes de guardar>>>" + getInstance().getVehiculo());

        try {
            if (getInstance().isPersistent()) {
                System.out.println("ENtro a editar>>>>>");
                guardarItem();
                if (requisicion != null) {
                    System.out.println("entro requisicion.....");
                    System.out.println("entri fijar requisicion" + requisicion);
                    if (requisicion.getId() != null) {
                        getInstance().setRequisicionId(requisicion);
                        requisicion.setSolicitudReparacionId(getInstance());
                        requisicion.setLastUpdate(now);

                        try {
//                            
                            servgen.actualizar(requisicion);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        getInstance().setRequisicionId(requisicion);
                        System.out.println("presentando requi >>>" + requisicion);
                    }
                }
                System.out.println("antes del 2do if");
                System.out.println("volvio a guardar soli" + reqSolicitud);
                if (reqSolicitud != null) {
                    if (reqSolicitud.getId() != null) {
                        System.out.println("entro reSoli.....");
                        System.out.println("diferente de null" + reqSolicitud.isPersistent());
                        if (reqSolicitud != requisicion) {
                            System.out.println("entro al if resolicitud");
                            reqSolicitud.setLastUpdate(now);
                            save(reqSolicitud);
                            System.out.println("paso save reqsolicitud");

                        }
                    }
                }

                System.out.println("antes de l LISTA,,,,");
                System.out.println("lista items" + getInstance().getListaItemSR());
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizo Solicitud de Reparacion y Mantenimiento" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            } else {
                getInstance().setEstado(true);
                guardarItem();
                create(getInstance());
                System.out.println("ENtro a crear items>>>>>");

                System.out.println("volvio a guardar soli");
                System.out.println("lista items" + getInstance().getListaItemSR());
                save(getInstance());
                if (requisicion != null) {
                    System.out.println("entro al 1if crear req");
                    if (requisicion.getId() != null) {
                        System.out.println("entro al2if reqi crear");
                        getInstance().setRequisicionId(requisicion);
                        requisicion.setSolicitudReparacionId(getInstance());
                        requisicion.setLastUpdate(now);

                        try {
//                            if (getInstance().getRequisicionId() != null) {
//                                System.out.println("entro al 3if req crear");
//                                Requisicion r = getInstance().getRequisicionId();
//                                r.setSolicitudReparacionId(null);
//                                 
//                                servgen.actualizar(r);
//                                
//                            }
                            System.out.println("guaradar solicitud con reqqq" + requisicion);
                            servgen.actualizar(requisicion);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        getInstance().setRequisicionId(requisicion);
                        System.out.println("fijando requ en crear" + requisicion);
                    }
                }
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se creo una nueva Solicitud de Reparacion y Mantenimiento" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
            System.out.println("Error al crear solicitud");
        }
        System.out.println("antes de fija solicitud");

        System.out.println("despues de fijar solicitud");
        return "/paginas/secretario/solicitud/lista.xhtml?faces-redirect=true";
    }

    @TransactionAttribute
    public void guardarSol() {

        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);

        System.out.println("vehiculo antes de guardar>>>" + getInstance().getVehiculo());
        //  getInstance().setVehiculo(vehiculo);

        System.out.println("termino de gusradar" + getInstance().getListaItemSR());

        if (this.listaItemsSolicitud.isEmpty()) {
            System.out.println("estas vaciaaaaaa");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "debe ingresar al menos un item a la solicitud"));

        } else {
            try {
                List<ItemSolicitudReparacion> lir = new ArrayList<ItemSolicitudReparacion>();
                for (ItemSolicitudReparacion apm : getInstance().getListaItemSR()) {

                    System.out.println("entrofor" + apm);

                    apm.setSolicitudReparacion(getInstance());//fijarle un plan de mantenimiento a cada actividad de plan de mantenimiento
                    //citemsolicitud.setInstance(apm);//fija la actividad del plan de mantenimiento al controlador de actividad de plan de mantenimiento

                    System.out.println("al crear");
                    BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(ItemSolicitudReparacion.class.getName());

                    apm.setCreatedOn(now);
                    apm.setLastUpdate(now);
                    apm.setActivationTime(now);
                    apm.setType(_type);
                    apm.buildAttributes(bussinesEntityService);  //

                    System.out.println("creo instance" + apm);

                    lir.add(apm);

                }
                getInstance().setListaItemSR(lir);
                getInstance().setEstado(true);

                create(getInstance());
                System.out.println("ENtro a crear items>>>>>");

                System.out.println("volvio a guardar soli");
                System.out.println("lista items" + getInstance().getListaItemSR());
                save(getInstance());

                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se creo una nueva Solicitud de Reparacion y Mantenimiento" + getInstance().getNumSolicitud() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            } catch (Exception e) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
                System.out.println("Error al crear solicitud");
            }
            System.out.println("antes de fija solicitud");

            System.out.println("despues de fijar solicitud");

            List<SolicitudReparacionMantenimiento> ls = findAll(SolicitudReparacionMantenimiento.class);
            listaSolicitud.clear();
            for (SolicitudReparacionMantenimiento soli : ls) {
//                System.out.println("num sel la soli en lipiar " + soli.getNumSolicitud());
//                System.out.println("estado sel la soli en lipiar " + soli.isEstado());
//                System.out.println("req sel la soli en lipiar " + soli.getRequisicionId());
//                System.out.println("vehiculo sel la soli en lipiar " + soli.getVehiculo());
                if (soli.isEstado() && soli.getRequisicionId() == null && soli.getVehiculo().getId().equals(getInstance().getVehiculo().getId())) {
                    System.out.println("listatesssa" + soli);
                    listaSolicitud.add(soli);

                }

            }

            System.out.println("listaSolicitudes" + listaSolicitud);
            idPersona = 0l;
            getInstance().setRecibidor("");
            getInstance().setObservacion("");
        }

    }

    public void guardarRequisicion(Requisicion nrequisicion) {
        nuevaRequisicion = nrequisicion;
        System.out.println("instancia" + getInstance().getId());
        System.out.println("Nueva requisicion>>>" + nuevaRequisicion.getObservaciones());

    }

    protected void createInstanceIS() {
        //prellenado estable para cualquier clase 
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(ItemSolicitudReparacion.class.getName());
        Date now = Calendar.getInstance().getTime();

        citemsolicitud.getInstance().setCreatedOn(now);
        citemsolicitud.getInstance().setLastUpdate(now);
        citemsolicitud.getInstance().setActivationTime(now);
        citemsolicitud.getInstance().setType(_type);
        citemsolicitud.getInstance().buildAttributes(bussinesEntityService);  //

    }

    public void guardarItem() {
        List<ItemSolicitudReparacion> lir = new ArrayList<ItemSolicitudReparacion>();
        for (ItemSolicitudReparacion apm : getInstance().getListaItemSR()) {

            System.out.println("entrofor" + apm);
            Date now = Calendar.getInstance().getTime();
            apm.setSolicitudReparacion(getInstance());//fijarle un plan de mantenimiento a cada actividad de plan de mantenimiento
            //citemsolicitud.setInstance(apm);//fija la actividad del plan de mantenimiento al controlador de actividad de plan de mantenimiento
            if (apm.isPersistent()) {

                apm.setLastUpdate(now);
                System.out.println("antes guardar");
                servgen.actualizar(apm);
                System.out.println("despues guardar");
                lir.add(apm);
            } else {
                System.out.println("al crear");
                citemsolicitud.setInstance(apm);
                citemsolicitud.getInstance().setLastUpdate(now);
//                createInstanceIS();
                if (getInstance().isPersistent()) {
                    System.out.println("antes guardar");
//                    create(citemsolicitud.getInstance());
                    System.out.println("paso de create>>>>>.");

//                    save(citemsolicitud.getInstance());
                    System.out.println("despues guardar");

                }
                System.out.println("creo instance" + citemsolicitud.getInstance());
                lir.add(citemsolicitud.getInstance());
            }

        }
        getInstance().setListaItemSR(lir);//fija la lista de actividades a la solicitudout

        System.out.println("lsiat de items a eliminar " + itemsEliminar);
        for (ItemSolicitudReparacion isr : itemsEliminar) {
            delete(isr);
        }
        System.out.println("termino de gusradar" + getInstance().getListaItemSR());
    }

    @Transactional
    public String darDeBaja(Long idSolicitud) {
        System.out.println("Entro a dar de baja>>>>>>" + idSolicitud);
        setId(idSolicitud);
        setInstance(servgen.buscarPorId(SolicitudReparacionMantenimiento.class, idSolicitud));
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        getInstance().setEstado(false);

        save(getInstance());

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "La partida seleccionada ha sido dada de baja ", "exitosamente"));
        return "/paginas/secretario/solicitud/lista.xhtml?faces-redirect=true";
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
        return "/paginas/secretario/solicitud/lista.xhtml?faces-redirect=true";
    }

}
