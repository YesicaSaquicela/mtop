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
import javax.faces.model.SelectItem;
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
import org.mtop.modelo.dinamico.BussinesEntityAttribute;
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
    private ItemSolicitudReparacion it;
    private String numeroSolicitud;
    private List<SolicitudReparacionMantenimiento> listaSolicitudes2 = new ArrayList<SolicitudReparacionMantenimiento>();
    private List<Requisicion> listaRequisicion2 = new ArrayList<Requisicion>();
    private String nombrew = "";
  

     public String getNombrew() {
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
        palabrabr = palabrabr.trim();
        
        if (palabrabr == null || palabrabr.equals("")) {
            palabrabr = "Ingrese algun valor a buscar";
        }
        List<Long> lrq = new ArrayList<Long>();
        for (Requisicion r : listaRequisicion2) {
            if (r.isEstado() && !lrq.contains(r)) {
                String s = r.getFechaRequisicion().toString();
                if (r.getNumRequisicion().contains(palabrabr) || s.contains(palabrabr)) {
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
                if (re.getSolicitudReparacionId() == null && re.isEstado() && re.getVehiculo().getId().equals(getInstance().getVehiculo().getId())) {
                  
                    listaRequisiciones.add(re);
                   

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

        List<Requisicion> lr = servgen.buscarTodoscoincidencia(Requisicion.class, Requisicion.class.getSimpleName(), Requisicion_.numRequisicion.getName(), query);

        for (Requisicion requisicion : listaRequisicion2) {
            if (requisicion.getVehiculo().getId().equals(getInstance().getVehiculo().getId()) && requisicion.isEstado() && requisicion.getSolicitudReparacionId() == null) {
                if (!ced.contains(requisicion.getNumRequisicion())) {
                    ced.add(requisicion.getNumRequisicion());
                }
                if (!ced.contains(requisicion.getFechaRequisicion().toString())) {
                    ced.add(requisicion.getFechaRequisicion().toString());
                }
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
            this.requisicion = new Requisicion();
        }
        
    }

    public List<Requisicion> getListaRequisiciones() {
        return listaRequisiciones;
    }

    public void setListaRequisiciones(List<Requisicion> listaRequisiciones) {
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
            String valorInicial = "8000";
            for (BussinesEntityAttribute a : bea) {
                if (a.getProperty().getName().equals("viNumSolicitud")) {
                   
                    valorInicial = a.getValue().toString();
                }
            }
            List<SolicitudReparacionMantenimiento> lista = findAll(SolicitudReparacionMantenimiento.class);
            int t = lista.size() + Integer.parseInt(valorInicial);

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
        nombrew = "req";
       
        if (skip) {

            skip = false;
            nombrew = "Final";
            return "confirm";
        } else {
            nombrew = "req";
            if (event.getNewStep().equals("confirm") && event.getOldStep().equals("requi")) {
                nombrew = "Final";//reset in case user goes back
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
        nombrew = "req";
        if (getSolicitudReparacionMantenimientoId() != null) {
            vehiculo = getInstance().getVehiculo();
        }

        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        nombrew = "req";
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
    }

    public Long getSolicitudReparacionMantenimientoId() {
        return (Long) getId();
    }


    public void setSolicitudReparacionMantenimientoId(Long solicitudReparacionMantenimientoId) {
        
        setId(solicitudReparacionMantenimientoId);
        vehiculo = getInstance().getVehiculo();
        if (getInstance().getRequisicionId() != null) {
                  
            requisicion = getInstance().getRequisicionId();
            reqSolicitud = new Requisicion();
            reqSolicitud.setId(requisicion.getId());
            reqSolicitud.setSolicitudReparacionId(null);

        }
    
        idPersona = getInstance().getPsolicita().getId();
        
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
        List<SolicitudReparacionMantenimiento> ls = servgen.buscarTodos(SolicitudReparacionMantenimiento.class);
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

        List<Requisicion> lr = servgen.buscarTodos(Requisicion.class);
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
        idPersona = 0l;
        getInstance().setRecibidor("");
        getInstance().setObservacion("");
        nombrew = "Solicitud";
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

        try {
            if (getInstance().isPersistent()) {
                guardarItem();
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
                }
                if (reqSolicitud != null) {
                    if (reqSolicitud.getId() != null) {

                        if (reqSolicitud.getId() != requisicion.getId()) {
                           
                            reqSolicitud=findById(Requisicion.class, reqSolicitud.getId());
                            reqSolicitud.setSolicitudReparacionId(null);
                            reqSolicitud.setLastUpdate(now);
                            save(reqSolicitud);

                        }
                    }
                }

                save(getInstance());

            } else {
                getInstance().setEstado(true);
                guardarItem();
                create(getInstance());
                save(getInstance());
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
