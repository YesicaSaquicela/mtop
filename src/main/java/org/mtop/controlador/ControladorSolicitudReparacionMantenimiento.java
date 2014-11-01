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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.mtop.cdi.Web;
import org.mtop.controlador.dinamico.BussinesEntityHome;
import org.mtop.modelo.Auxiliar;
import org.mtop.modelo.Auxiliar_;
import org.mtop.modelo.ItemSolicitudReparacion;
import org.mtop.modelo.Requisicion;
import org.mtop.modelo.Requisicion_;
import org.mtop.modelo.dinamico.BussinesEntityType;
import org.mtop.modelo.SolicitudReparacionMantenimiento;
import org.mtop.modelo.Vehiculo;
import org.mtop.modelo.dinamico.BussinesEntityAttribute;
import org.mtop.modelo.dinamico.Property;
import org.mtop.modelo.profile.Profile;
import org.mtop.servicios.ServicioGenerico;
import org.primefaces.event.FlowEvent;

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

    private ControladorItemSolicitud citemsolicitud;
    private List<Requisicion> listaRequisiciones;
    private Requisicion requisicion;
    private boolean skip;
    private List<ItemSolicitudReparacion> listaItemsSolicitud;
    private String palabrabv = "";
    private String palabrab = "";
    private long idPersonas = 0l;
    private long idPersonar = 0l;
    private long idPersonaa = 0l;
    private String palabrabr = "";
    private List<SolicitudReparacionMantenimiento> listaSolicitudAprobadas = new ArrayList<SolicitudReparacionMantenimiento>();
    private String vista;
    private Requisicion reqSolicitud;
    private Requisicion nuevaRequisicion = new Requisicion();
    private ItemSolicitudReparacion itemsr;
    private List<ItemSolicitudReparacion> itemsEliminar;
    private ItemSolicitudReparacion it;
    private String numeroSolicitud;
    private List<SolicitudReparacionMantenimiento> listaSolicitudes2 = new ArrayList<SolicitudReparacionMantenimiento>();
    private List<Requisicion> listaRequisicion2 = new ArrayList<Requisicion>();
    private String nombrew = "";
    private List<Profile> listadepersonas;
    private List<Profile> listaPersonal;
    private Property propiedadNum;

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

    public List<Profile> getListadepersonas() {
        return listadepersonas;
    }

    public void setListadepersonas(List<Profile> listadepersonas) {
        this.listadepersonas = listadepersonas;
    }

    public long getIdPersonas() {
        return idPersonas;
    }

    public void setIdPersonas(long idPersonas) {
        this.idPersonas = idPersonas;

    }

    public long getIdPersonar() {
        return idPersonar;
    }

    public void setIdPersonar(long idPersonar) {
        this.idPersonar = idPersonar;

    }

    public long getIdPersonaa() {
        return idPersonaa;
    }

    public void setIdPersonaa(long idPersonaa) {
        this.idPersonaa = idPersonaa;

    }

    public String getNombrew() {
        System.out.println("nombre en get" + nombrew);
        return nombrew;
    }

    public void setNombrew(String nombrew) {
        this.nombrew = nombrew;
    }

    public ItemSolicitudReparacion getIt() {
        return it;
    }

    public void setIt(ItemSolicitudReparacion it) {
        this.it = it;
    }

    public List<ItemSolicitudReparacion> getItemsEliminar() {
        return itemsEliminar;
    }

    public void setItemsEliminar(List<ItemSolicitudReparacion> itemsEliminar) {
        this.itemsEliminar = itemsEliminar;
    }

    public Requisicion getNuevaRequisicion() {
        return nuevaRequisicion;
    }

    public void setNuevaRequisicion(Requisicion nuevaRequisicion) {
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

    public String obtenernombre(long idP) {
        Profile nombrePersona;
        System.out.println("nllegas id>" + idP);
        String nombPersona;

        nombrePersona = servgen.buscarPorId(Profile.class, idP);
        nombPersona = nombrePersona.concatenarNombre();

        return nombPersona;
    }

    public void buscarr() {
        palabrabr = palabrabr.trim();

        if (palabrabr == null || palabrabr.equals("")) {
            palabrabr = "Ingrese algun valor a buscar";
        }
        List<Long> lrq = new ArrayList<Long>();
        for (Requisicion r : listaRequisicion2) {

            if (!lrq.contains(r.getId())) {
                String s = r.getFechaRequisicion().toString();
                if (r.getNumRequisicion().contains(palabrabr) 
                        || s.contains(palabrabr) 
                        || r.getTipoRequisicion().toLowerCase().contains(palabrabr)) {
                    lrq.add(r.getId());
                }

            }

        }

        System.out.println("LEeeee" + lrq);

        if (lrq.isEmpty()) {

            if (palabrabr.equals("Ingrese algun valor a buscar")) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN:", " Ingrese algun valor a buscar");
                FacesContext.getCurrentInstance().addMessage("", msg);
                palabrabr = " ";
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: No se ha encontrado", palabrabr);
                FacesContext.getCurrentInstance().addMessage("", msg);
            }

        } else {
            listaRequisiciones.clear();
            Requisicion re = new Requisicion();
            for (Long r : lrq) {
                re = findById(Requisicion.class, r);
                if (re.getVehiculo() != null) {
                    if (re.getSolicitudReparacionId() == null && re.isEstado() && re.getVehiculo().getId().equals(getInstance().getVehiculo().getId())) {

                        listaRequisiciones.add(re);

                    }

                }

            }

            palabrabr = "";
        }

    }

    public void limpiarr() {
        palabrabr = "";
        List<Requisicion> lr = servgen.buscarTodos(Requisicion.class);
        listaRequisiciones.clear();

        for (Requisicion r : listaRequisicion2) {
            if (r.isEstado() && r.getSolicitudReparacionId() == null && r.getVehiculo().getId().equals(getInstance().getVehiculo().getId())) {

                listaRequisiciones.add(r);

            }

        }
    }
    

    public ArrayList<String> autocompletarr(String query) {

        ArrayList<String> ced = new ArrayList<String>();

        for (Requisicion requisicion1 : listaRequisicion2) {
            if (requisicion1.getVehiculo() != null) {
                if (requisicion1.getVehiculo().getId().equals(getInstance().getVehiculo().getId())) {
                    if (requisicion1.getNumRequisicion().contains(query)
                            && !ced.contains(requisicion1.getNumRequisicion())) {
                        ced.add(requisicion1.getNumRequisicion());
                    }
                    if (requisicion1.getFechaRequisicion().toString().contains(query)
                            && !ced.contains(requisicion1.getFechaRequisicion().toString())) {
                        ced.add(requisicion1.getFechaRequisicion().toString());
                    }
                     if (requisicion1.getTipoRequisicion().toString().contains(query)
                            && !ced.contains(requisicion1.getTipoRequisicion())) {
                        ced.add(requisicion1.getTipoRequisicion());
                    }
                }
//
            }

        }

        System.out.println("\n\nlistaaaaa autocompletar" + ced);
        return ced;

    }

    public void buscar() {
        palabrab = palabrab.trim();
        if (palabrab == null || palabrab.equals("")) {
            palabrab = "Ingrese algun valor a buscar";
        }

        List<Long> lsoli = new ArrayList<Long>();
        ControladorVehiculo cv = new ControladorVehiculo();
        cv.setBussinesEntity(bussinesEntity);
        cv.setEntityManager(em);

        for (SolicitudReparacionMantenimiento sol : listaSolicitudes2) {
            if (!lsoli.contains(sol.getId())) {
                String s = sol.getFechaSolicitud().toString();
                if (sol.getNumSolicitud().contains(palabrab) || s.contains(palabrab)) {
                    lsoli.add(sol.getId());

                }

                List<BussinesEntityAttribute> bea = new ArrayList<BussinesEntityAttribute>();
                if ((sol.getVehiculo().getNumRegistro().equals(palabrab)
                        || sol.getVehiculo().getPlaca().equals(palabrab)) && !lsoli.contains(sol.getId())) {

                    lsoli.add(sol.getId());

                }
                cv.setId(sol.getVehiculo().getId());
                bea = cv.getInstance().findBussinesEntityAttribute("Motor");
                for (BussinesEntityAttribute bussinesEntityAttribute : bea) {

                    if (bussinesEntityAttribute.getName().equals("serieMotor")) {
                        if (((String) bussinesEntityAttribute.getValue()).equals(palabrab) && !lsoli.contains(sol.getId())) {

                            lsoli.add(sol.getId());

                        }

                    }
                }
                bea = cv.getInstance().findBussinesEntityAttribute("Chasis");
                for (BussinesEntityAttribute bussinesEntityAttribute : bea) {
                    if (bussinesEntityAttribute.getName().equals("serieChasis")) {

                        if (((String) bussinesEntityAttribute.getValue()).equals(palabrab) && !lsoli.contains(sol.getId())) {

                            lsoli.add(sol.getId());

                        }

                    }
                }

            }

        }

        System.out.println("LEeeee" + lsoli);

        if (lsoli.isEmpty()) {
            if (palabrab.equals("Ingrese algun valor a buscar")) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "Ingrese algun valor a buscar ");
                FacesContext.getCurrentInstance().addMessage("", msg);
                palabrab = " ";
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "No se ha encontrado " + palabrab);
                FacesContext.getCurrentInstance().addMessage("", msg);
            }

        } else {
            listaSolicitud.clear();
            listaSolicitudAprobadas.clear();
            SolicitudReparacionMantenimiento solRM = new SolicitudReparacionMantenimiento();
            for (Long s : lsoli) {
                solRM = findById(SolicitudReparacionMantenimiento.class, s);
                if (solRM.isEstado()) {
                    if (solRM.getAprobado()) {
                        if (!listaSolicitudAprobadas.contains(solRM)) {

                            listaSolicitudAprobadas.add(solRM);
                        }

                    } else {
                        if (!listaSolicitud.contains(solRM)) {
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

        for (SolicitudReparacionMantenimiento soli : listaSolicitudes2) {
            if (soli.isEstado()) {
                if (soli.getAprobado()) {
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
        ControladorVehiculo cv = new ControladorVehiculo();
        cv.setBussinesEntity(bussinesEntity);
        cv.setEntityManager(em);
        for (SolicitudReparacionMantenimiento soli : listaSolicitudes2) {
            if (soli.getNumSolicitud().contains(query)) {
                ced.add(soli.getNumSolicitud());
            }
            if (!ced.contains(soli.getFechaSolicitud().toString()) && soli.getFechaSolicitud().toString().contains(query)) {
                ced.add(soli.getFechaSolicitud().toString());
            }
            List<BussinesEntityAttribute> bea = new ArrayList<BussinesEntityAttribute>();
            if ((soli.getVehiculo().getNumRegistro().contains(query)
                    || soli.getVehiculo().getPlaca().equals(palabrab)) && !ced.contains(soli.getVehiculo().getNumRegistro())) {
                ced.add(soli.getVehiculo().getNumRegistro());
            }
            cv.setId(soli.getVehiculo().getId());
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

        System.out.println("listaaaaa autocompletar" + ced);
        return ced;

    }

    public Requisicion getRequisicion() {
        return requisicion;
    }

    public void setRequisicion(Requisicion requisicion) {
        if (requisicion != null) {

            this.requisicion = requisicion;
            getInstance().setRequisicionId(requisicion);
            System.out.println("ide de requisicion>>>>>>" + requisicion);
        } else {
            Boolean ban=true;
            for (Requisicion req : listaRequisiciones) {
               if(req.getId().equals(getInstance().getRequisicionId().getId())){
                    ban=false;
                }
            }
            if(ban){
             
                listaRequisiciones.add(getInstance().getRequisicionId());
           
            }
           

            this.requisicion = new Requisicion();
        }

    }

    public List<Requisicion> getListaRequisiciones() {
        return listaRequisiciones;
    }

    public void setListaRequisiciones(List<Requisicion> listaRequisiciones) {
        System.out.println("fijando lista de requisiciones>>>>>>" + listaRequisiciones);

        this.listaRequisiciones = listaRequisiciones;
    }

    public ControladorItemSolicitud getCitemsolicitud() {
        return citemsolicitud;
    }

    public void setCitemsolicitud(ControladorItemSolicitud citemsolicitud) {
        this.citemsolicitud = citemsolicitud;
    }

    public String getNumeroSolicitud() {
        if (getId() == null) {
            List<BussinesEntityAttribute> bea = getInstance().findBussinesEntityAttribute("org.mtop.modelo.SolicitudReparacionMantenimiento");
            System.out.println("bea size" + bea.size());

            for (BussinesEntityAttribute a : bea) {
                if (a.getProperty().getName().equals("viNumSolicitud")) {
                    propiedadNum = a.getProperty();
                    setNumeroSolicitud(a.getValue().toString());
                    return numeroSolicitud;
                }
            }

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
        nombrew = "req";
        System.out.println("\n\n\n\n\n\nENRTRO FLOWPROCESS" + event.getNewStep());
        System.out.println("Entro getOld" + event.getOldStep());
        System.out.println("Lista de itemmSSSS" + this.listaItemsSolicitud);
        System.out.println("vehiculo>>>>>" + getInstance().getVehiculo());
        System.out.println("otro retorna");
        System.out.println("nombre wizard111" + nombrew);
        if (skip) {

            skip = false;
            nombrew = "Final";
            return "confirm";
        } else {

            if (event.getNewStep().equals("confirm") && event.getOldStep().equals("requi")) {

                nombrew = "Final";//reset in case user goes back
                System.out.println("nombre wizard111skip dfdsfds" + nombrew);
            }
            if (getInstance().getId() != null) {
                this.citemsolicitud.setListaItemsSolicitud(getInstance().getListaItemSR());
            }
            if (event.getNewStep().equals("sol") && event.getOldStep().equals("address")) {
                nombrew = "Solicitud";
                return event.getNewStep();
            } else {
                if (event.getNewStep().equals("address") && event.getOldStep().equals("items")) {

                    return event.getNewStep();
                } else {
                    if (event.getOldStep().equals("address") && getInstance().getVehiculo() == null) {

                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", " Debe escoger un vehiculo"));

                        return event.getOldStep();
                    } else {

                        if (event.getOldStep().equals("items") && this.listaItemsSolicitud.isEmpty()) {

                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Debe ingresar al menos un item a la solicitud"));

                            return event.getOldStep();
                        } else {
                            return event.getNewStep();
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
            vehiculo = getInstance().getVehiculo();
        }

        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {

        this.vehiculo = vehiculo;
        getInstance().setVehiculo(vehiculo);

        // for para poder cambiar el id del vehiculo y las requisiciones
        if (requisicion != null) {
            if (requisicion.getVehiculo() != getInstance().getVehiculo()
                    && getInstance().getRequisicionId() != null) {
                getInstance().setRequisicionId(null);
                requisicion = new Requisicion();

            }
        }

        List<Requisicion> lrs = new ArrayList<Requisicion>();
        //mosttrar lista de requisiciones con igual vehiculo ,estado true
        for (Requisicion req : findAll(Requisicion.class)) {
            if (req.isEstado() && req.getSolicitudReparacionId() == null) {
                if (req.getVehiculo() != null) {

                    if ((req.getVehiculo().getId().equals(getInstance().getVehiculo().getId()))) {

                        lrs.add(req);
                    }
                }

            }

        }
        listaRequisiciones = lrs;
        listaRequisicion2 = lrs;
    }

    public Long getSolicitudReparacionMantenimientoId() {
        return (Long) getId();
    }

    public void setSolicitudReparacionMantenimientoId(Long solicitudReparacionMantenimientoId) {

        setId(solicitudReparacionMantenimientoId);

        vehiculo = getInstance().getVehiculo();
        System.out.println("requi en sol>>" + getInstance().getRequisicionId());
        if (getInstance().getRequisicionId() != null) {

            requisicion = getInstance().getRequisicionId();
            reqSolicitud = new Requisicion();
            reqSolicitud.setId(requisicion.getId());
            reqSolicitud.setSolicitudReparacionId(null);

        }

        List<Auxiliar> listaaux = servgen.buscarAuxiliarPorIdSolicitud(Auxiliar_.soliciudId.getName(), getInstance().getId());
        System.out.println("size" + listaaux.size());
        for (Auxiliar auxiliar : listaaux) {
            if (auxiliar.getTipoRelacion().equals("solicitadoS")) {
                idPersonas = auxiliar.getPersonalId().getId();

            }
            if (auxiliar.getTipoRelacion().equals("aprobadoS")) {
                idPersonaa = auxiliar.getPersonalId().getId();
                System.out.println("nombreee apr" + auxiliar.getPersonalId().concatenarNombre());
            }
            if (auxiliar.getTipoRelacion().equals("recibeS")) {
                idPersonar = auxiliar.getPersonalId().getId();

            }
        }
        if (getInstance().isPersistent()) {

            System.out.println("entro111111111111");
            listaItemsSolicitud = getInstance().getListaItemSR();
            System.out.println("entro a obtener lisra" + listaItemsSolicitud);
        }

        List<Requisicion> lr = findAll(Requisicion.class);
        List<Requisicion> lrq = new ArrayList<Requisicion>();
        //mosttrar lista de requisiciones con igual vehiculo ,estado true

        for (Requisicion req : lr) {

            if (req.isEstado() && req.getSolicitudReparacionId() == null) {
                if (req.getVehiculo() != null) {

                    if ((req.getVehiculo().getId() == getInstance().getVehiculo().getId())) {

                        lrq.add(req);
                    }
                }

            }
        }
        listaRequisiciones = lrq;

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

    public void eliminarItemS(ItemSolicitudReparacion itemsol) {

        List<ItemSolicitudReparacion> li = new ArrayList<ItemSolicitudReparacion>();

        for (ItemSolicitudReparacion items : listaItemsSolicitud) {

            if (!items.getDescripcionElementoRevisar().equals(itemsol.getDescripcionElementoRevisar())
                    && !items.getDescripcionFalla().equals(itemsol.getDescripcionFalla())) {
                li.add(items);

            } else {
                if (items.isPersistent()) {
                    itemsEliminar.add(items);

                }
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", " Se eliminó Item " + items.getDescripcionElementoRevisar() + " con éxito ");
                FacesContext.getCurrentInstance().addMessage("", msg);

            }

        }
        listaItemsSolicitud = li;

        getInstance().setListaItemSR(li);

    }

    public void editar() {
        it = new ItemSolicitudReparacion();
        it.setDescripcionElementoRevisar(" ");
        it.setDescripcionFalla(" ");

    }

    public void agregarItemS() {
        String desEl = citemsolicitud.getInstance().getDescripcionElementoRevisar().trim();
        String desfa = citemsolicitud.getInstance().getDescripcionFalla().trim();
        if (desEl.equals("") || desfa.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", " Campos abligatorios, elemento a revisar, descripción de la falla"));

        } else {

            listaItemsSolicitud.add(citemsolicitud.getInstance());

            citemsolicitud.setInstance(new ItemSolicitudReparacion());
            citemsolicitud.getInstance().setDescripcionElementoRevisar("");
            citemsolicitud.getInstance().setDescripcionFalla("");

        }
        //aumente
        getInstance().setListaItemSR(listaItemsSolicitud);

    }

    public ItemSolicitudReparacion getItemsr() {
        return itemsr;
    }

    public void setItemsr(ItemSolicitudReparacion itemsr) {
        this.itemsr = itemsr;
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
        bussinesEntityService.setEntityManager(em);
        servgen.setEm(em);
        listaSolicitud = findAll(SolicitudReparacionMantenimiento.class);
        listaRequisiciones = findAll(Requisicion.class);
        List<SolicitudReparacionMantenimiento> ls = findAll(SolicitudReparacionMantenimiento.class);
        listaSolicitudes2 = new ArrayList<SolicitudReparacionMantenimiento>();
        listaSolicitud.clear();
        for (SolicitudReparacionMantenimiento sol : ls) {

            if (sol.isEstado()) {
                if (!sol.getAprobado()) {
                    listaSolicitud.add(sol);
                } else {
                    listaSolicitudAprobadas.add(sol);
                }
                listaSolicitudes2.add(sol);
            }

        }

        List<Requisicion> lr = findAll(Requisicion.class);
        listaRequisiciones.clear();
        //for para presentar lsita de solicitudes sin requisicion
        for (Requisicion req : lr) {
            if (req.getSolicitudReparacionId() == null && req.isEstado()) {
                System.out.println("entro" + req);
                listaRequisiciones.add(req);
            }

        }
        listaRequisicion2 = listaRequisiciones;
        vehiculo = new Vehiculo();
        idVehiculo = 0l;
        citemsolicitud = new ControladorItemSolicitud();
        citemsolicitud.setInstance(new ItemSolicitudReparacion());
        citemsolicitud.getInstance().setDescripcionElementoRevisar(" ");
        citemsolicitud.getInstance().setDescripcionFalla(" ");
        it = new ItemSolicitudReparacion();
        it.setDescripcionElementoRevisar(" ");
        it.setDescripcionFalla(" ");
        requisicion = new Requisicion();
        listaPersonal = findAll(Profile.class);
        listaItemsSolicitud = new ArrayList<ItemSolicitudReparacion>();
        itemsEliminar = new ArrayList<ItemSolicitudReparacion>();
        idPersonas = 0l;
        idPersonar = 0l;
        idPersonaa = 0l;
        //  getInstance().setRecibidor("");
        getInstance().setObservacion("");
        nombrew = "Solicitud";
        listadepersonas = new ArrayList<Profile>();
        listadepersonas.add(new Profile());
        listadepersonas.add(new Profile());
        listadepersonas.add(new Profile());
    }

    @Override
    protected SolicitudReparacionMantenimiento createInstance() {
        //prellenado estable para cualquier clase 
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(SolicitudReparacionMantenimiento.class.getName());
        Date now = Calendar.getInstance().getTime();
        SolicitudReparacionMantenimiento solicitudRepMant = new SolicitudReparacionMantenimiento();
        solicitudRepMant.setCreatedOn(now);
        solicitudRepMant.setLastUpdate(now);
        //  solicitudRepMant.setFechaSolicitud(now);
        solicitudRepMant.setActivationTime(now);
        solicitudRepMant.setType(_type);
        solicitudRepMant.buildAttributes(bussinesEntityService);  //
        return solicitudRepMant;
    }

    @Override
    public Class<SolicitudReparacionMantenimiento> getEntityClass() {
        return SolicitudReparacionMantenimiento.class;
    }

    public void guardarRelacion() {

        Auxiliar auxiliar = new Auxiliar();
        auxiliar.setSoliciudId(getInstance());
        auxiliar.setPersonalId(servgen.buscarPorId(Profile.class, idPersonas));
        auxiliar.setTipoRelacion("solicitadoS");

        save(auxiliar);
        auxiliar.setSoliciudId(getInstance());
        auxiliar.setPersonalId(servgen.buscarPorId(Profile.class, idPersonaa));
        auxiliar.setTipoRelacion("aprobadoS");

        save(auxiliar);

        auxiliar.setSoliciudId(getInstance());
        auxiliar.setPersonalId(servgen.buscarPorId(Profile.class, idPersonar));
        auxiliar.setTipoRelacion("recibeS");
        save(auxiliar);

        System.out.println("termino de guardar relaciones");

    }

    @TransactionAttribute
    public String guardar() {

        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);

        try {
            if (getInstance().isPersistent()) {
                guardarItem();

                System.out.println("requisicion a asignar" + requisicion);
                if (requisicion != null) {
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
                    } else {

                        getInstance().setRequisicionId(null);
                    }
                } else {
                    getInstance().setRequisicionId(null);

                }
                System.out.println("reqSolicitud" + reqSolicitud);
                if (reqSolicitud != null) {
                    if (reqSolicitud.getId() != null) {

                        if (reqSolicitud.getId() != requisicion.getId()) {

                            reqSolicitud = findById(Requisicion.class, reqSolicitud.getId());
                            reqSolicitud.setSolicitudReparacionId(null);
                            reqSolicitud.setLastUpdate(now);
                            System.out.println("guardao requis in soli" + reqSolicitud.getSolicitudReparacionId());
                            save(reqSolicitud);

                        }
                    }
                }
                System.out.println("va a guardar con requi " + getInstance().getRequisicionId());

                save(getInstance());

                List<Auxiliar> listaaux = servgen.buscarAuxiliarPorIdSolicitud(Auxiliar_.soliciudId.getName(), getInstance().getId());
                System.out.println("size" + listaaux.size());
                for (Auxiliar auxiliar : listaaux) {
                    if (auxiliar.getTipoRelacion().equals("solicitadoS")) {
                        auxiliar.setPersonalId(servgen.buscarPorId(Profile.class, idPersonas));
                        save(auxiliar);
                    }
                    if (auxiliar.getTipoRelacion().equals("aprobadoS")) {
                        auxiliar.setPersonalId(servgen.buscarPorId(Profile.class, idPersonaa));
                        save(auxiliar);
                    }

                    if (auxiliar.getTipoRelacion().equals("recibeS")) {
                        auxiliar.setPersonalId(servgen.buscarPorId(Profile.class, idPersonar));
                        save(auxiliar);
                    }
                }

            } else {
                getInstance().setEstado(true);
                guardarItem();
                create(getInstance());
                save(getInstance());
                guardarRelacion();
                if (requisicion != null) {

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
                    }
                }
                Integer t = Integer.parseInt(propiedadNum.getValue().toString());
                t++;
                propiedadNum.setValue((Serializable) t);
                System.out.println("nuevo valor a guardar de soli" + propiedadNum.getValue().toString());

                save(propiedadNum);
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
            System.out.println("Error al crear solicitud");
        }
        return "/paginas/secretario/solicitud/lista.xhtml?faces-redirect=true";
    }

    @TransactionAttribute
    public void guardarSol() {

        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);

        if (this.listaItemsSolicitud.isEmpty()) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Debe ingresar al menos un item a la solicitud"));

        } else {
            try {
                List<ItemSolicitudReparacion> lir = new ArrayList<ItemSolicitudReparacion>();
                for (ItemSolicitudReparacion apm : getInstance().getListaItemSR()) {

                    apm.setSolicitudReparacion(getInstance());//fijarle un plan de mantenimiento a cada actividad de plan de mantenimiento

                    BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(ItemSolicitudReparacion.class.getName());

                    apm.setCreatedOn(now);
                    apm.setLastUpdate(now);
                    apm.setActivationTime(now);
                    apm.setType(_type);
                    apm.buildAttributes(bussinesEntityService);  //

                    lir.add(apm);

                }
                getInstance().setListaItemSR(lir);
                getInstance().setEstado(true);

                create(getInstance());

                save(getInstance());

                guardarRelacion();
                Integer t = Integer.parseInt(propiedadNum.getValue().toString());
                t++;
                propiedadNum.setValue((Serializable) t);
                System.out.println("nuevo valor a guardar de soli" + propiedadNum.getValue().toString());

                save(propiedadNum);
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se creó una nueva Solicitud de Reparación y Mantenimiento " + getInstance().getNumSolicitud() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            } catch (Exception e) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
                System.out.println("Error al crear solicitud");
            }

            List<SolicitudReparacionMantenimiento> ls = findAll(SolicitudReparacionMantenimiento.class);
            listaSolicitud.clear();
            for (SolicitudReparacionMantenimiento soli : ls) {
//      
                if (soli.isEstado() && soli.getRequisicionId() == null && soli.getVehiculo().getId().equals(getInstance().getVehiculo().getId())) {

                    listaSolicitud.add(soli);

                }

            }

        }

    }

    public void guardarItem() {
        List<ItemSolicitudReparacion> lir = new ArrayList<ItemSolicitudReparacion>();
        for (ItemSolicitudReparacion apm : listaItemsSolicitud) {

            Date now = Calendar.getInstance().getTime();
            apm.setSolicitudReparacion(getInstance());//fijarle un plan de mantenimiento a cada actividad de plan de mantenimiento
            //kite lo comentado
            citemsolicitud.setInstance(apm);//fija la actividad del plan de mantenimiento al controlador de actividad de plan de mantenimiento
            if (apm.isPersistent()) {

                apm.setLastUpdate(now);
                System.out.println("antes guardar");
                servgen.actualizar(apm);
                System.out.println("despues guardar");
                lir.add(apm);
            } else {
                citemsolicitud.setInstance(apm);
                citemsolicitud.getInstance().setLastUpdate(now);

                lir.add(citemsolicitud.getInstance());
            }

        }
        getInstance().setListaItemSR(lir);//fija la lista de actividades a la solicitudout

        for (ItemSolicitudReparacion isr : itemsEliminar) {
            delete(isr);
        }

    }

    @Transactional
    public void darDeBaja(Long idSolicitud) {
        setId(idSolicitud);
        setInstance(servgen.buscarPorId(SolicitudReparacionMantenimiento.class, idSolicitud));
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        getInstance().setEstado(false);
        save(getInstance());
        List<SolicitudReparacionMantenimiento> ls = servgen.buscarTodos(SolicitudReparacionMantenimiento.class);
        listaSolicitudes2 = new ArrayList<SolicitudReparacionMantenimiento>();
        listaSolicitud.clear();
        for (SolicitudReparacionMantenimiento sol : ls) {

            if (sol.isEstado()) {
                if (!sol.getAprobado()) {
                    listaSolicitud.add(sol);
                }
                listaSolicitudes2.add(sol);
            }

        }
        if (getInstance().getRequisicionId() != null) {
            Requisicion r = getInstance().getRequisicionId();
            r.setLastUpdate(now);
            r.setSolicitudReparacionId(null);
            System.out.println("solo" + r);
            save(r);
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "La solicitud " + getInstance().getNumSolicitud() + " ha sido dada de baja"));

    }

}
