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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
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
import org.mtop.modelo.ActividadPlanMantenimiento;
import org.mtop.modelo.ItemRequisicion;
import org.mtop.modelo.ItemSolicitudReparacion;
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
    @Inject
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
        if (palabrabr == null || palabrabr.equals("") || palabrabr.contains(" ")) {
            palabrabr = "Ingrese algun valor a buscar";
        }
        //buscando por coincidencia
        List<Requisicion> le = servgen.buscarTodoscoincidencia(Requisicion.class, Requisicion.class.getSimpleName(), Requisicion_.numRequisicion.getName(), palabrabr);
        //buscando por fechas
        List<Requisicion> lef = servgen.buscarRequisicionporFecha(Requisicion_.fechaRequisicion.getName(), palabrabr);
        for (Requisicion requisicion : lef) {
            if (!le.contains(requisicion)) {
                le.add(requisicion);
            }
        }

        if (le.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrabr.equals("Ingrese algun valor a buscar")) {
                context.addMessage(null, new FacesMessage("INFORMACION: Ingrese algun valor a buscar"));
                palabrabr = " ";
            } else {
                context.addMessage(null, new FacesMessage("INFORMACION: No se ha encontrado " + palabrabr));
            }

        } else {
            listaRequisiciones = le;
            palabrabr = "";
        }

    }

    public void limpiarr() {
        palabrabr = "";
        List<Requisicion> lr = servgen.buscarTodos(Requisicion.class);
        listaRequisiciones.clear();
        System.out.println("lppp" + lr);

        for (Requisicion r : lr) {
            if (r.isEstado()) {
                System.out.println("listatesssa" + listaRequisiciones);
                listaRequisiciones.add(r);

            }

        }
    }

    public ArrayList<String> autocompletarr(String query) {
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

        System.out.println("listaaaaa autocompletar" + ced);
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
            listaSolicitud = le;
            palabrab = "";
        }

    }

    public void limpiar() {
        palabrab = "";
        List<SolicitudReparacionMantenimiento> ls = servgen.buscarTodos(SolicitudReparacionMantenimiento.class);
        listaSolicitud.clear();
        System.out.println("lppp" + ls);

        for (SolicitudReparacionMantenimiento soli : ls) {
            if (soli.isEstado()) {
                System.out.println("listatesssa" + listaSolicitud);
                listaSolicitud.add(soli);

            }

        }
    }

    public ArrayList<String> autocompletar(String query) {
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

//        for (Vehiculo vehiculo : lk) {
//            if (!lv.contains(vehiculo)) {
//                lv.add(vehiculo);
//            }
//        }
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

//    public List<ItemSolicitudReparacion> getListaItemsSolicitud() {
//        return listaItemsSolicitud;
//    }
//
//    public void setListaItemsSolicitud(List<ItemSolicitudReparacion> listaItemsSolicitud) {
//        this.listaItemsSolicitud = listaItemsSolicitud;
//    }

    public Requisicion getRequisicion() {
        return requisicion;
    }

    public void setRequisicion(Requisicion requisicion) {
        this.requisicion = requisicion;
    }

    public List<Requisicion> getListaRequisiciones() {
        listaRequisiciones = findAll(Requisicion.class);
        for (SolicitudReparacionMantenimiento sol : listaSolicitud) {
            if (sol.getRequisicionId() != null) {
                listaRequisiciones.remove(sol.getRequisicionId());
            }

        }
        System.out.println("lista requisicionessss sin solicitud" + listaRequisiciones);
        System.out.println("lista antes" + listaRequisiciones);
        System.out.println("id de vehiculo actual" + getVehiculo());
        List<Requisicion> lr = servgen.buscarTodos(Requisicion.class);
        for (Requisicion req : listaRequisiciones) {
            if (req.getVehiculo().getId() != getVehiculo().getId()) {
                listaRequisiciones.remove(req);
            }
        }
        System.out.println("lista despues" + listaRequisiciones);
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

        if (skip) {
            skip = false;   //reset in case user goes back  

            return "confirm";
        } else {
            System.out.println("pasoooo");
            if (event.getOldStep().equals("address") && this.vehiculo.getId() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "debe escoger un vehiculo"));

                return event.getOldStep();
            } else {

                if (event.getOldStep().equals("items") && citemsolicitud.listaItemsSolicitud.isEmpty()) {
                    System.out.println("estas vaciaaaaaa");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "debe ingresar al menos un item a la solicitud"));

                    return event.getOldStep();
                } else {

                    return event.getNewStep();
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

        if (getSolicitudReparacionMantenimientoId() != null) {
            System.out.println("vehiculo " + getInstance().getVehiculo());
            vehiculo = getInstance().getVehiculo();
        }

        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        System.out.println("entra a fijar un vehiculo con su iddd" + vehiculo.getId());

        this.vehiculo = vehiculo;

    }

    public List<Vehiculo> getListaVehiculos() {
//        listaVehiculos = findAll(Vehiculo.class);
        System.out.println("ENTRO A BUSCAR>vehiculos>>>>>>>>>>");
        return listaVehiculos;
    }

    public void setListaVehiculos(List<Vehiculo> listaVehiculos) {
        this.listaVehiculos = listaVehiculos;
    }

    public Long getSolicitudReparacionMantenimientoId() {
        return (Long) getId();
    }

    public void setSolicitudReparacionMantenimientoId(Long solicitudReparacionMantenimientoId) {

        setId(solicitudReparacionMantenimientoId);
        vehiculo = getInstance().getVehiculo();

        requisicion = getInstance().getRequisicionId();
        idPersona = getInstance().getPsolicita().getId();
        System.out.println("entro a obtener la lista de solicitudes>>>>>."+getInstance().getListaItemSR());
        citemsolicitud.listaItemsSolicitud = getInstance().getListaItemSR();

//        listaItemsSolicitud = new ArrayList<ItemSolicitudReparacion>();
//        for (ItemSolicitudReparacion itr : findAll(ItemSolicitudReparacion.class)) {
//
//            if (getInstance().getId() != null) {
//                System.out.println("entro a itemsSSSSSSSSSSSSSSSSSSS" + getInstance().getId());
//
//                System.out.println("idde itr" + itr.getSolicitudReparacion().getId());
//                if (getInstance().getId().equals(itr.getSolicitudReparacion().getId())) {
//                    System.out.println("fijo un ide");
//                    listaItemsSolicitud.add(itr);
//                }
//
//            }
//        }
//        System.out.println("lista de items" + listaItemsSolicitud);

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
        List<ItemSolicitudReparacion> li=citemsolicitud.listaItemsSolicitud;
        for (ItemSolicitudReparacion items : li) {
            System.out.println("entro al for>>>>>>>");
            if (items.getDescripcionElementoRevisar().equals(itemsol.getDescripcionElementoRevisar())
                    && items.getDescripcionFalla().equals(itemsol.getDescripcionFalla())) {
                System.out.println("entro a remover>>>>>");
                citemsolicitud.listaItemsSolicitud.remove(con);
                citemsolicitud.setInstance(items);
                break;

            }
            con++;

        }

    }
    
      public void eliminarItemS(ItemSolicitudReparacion itemsol) {

        int con = 0;
        List<ItemSolicitudReparacion> li=citemsolicitud.listaItemsSolicitud;
        for (ItemSolicitudReparacion items : li) {
            System.out.println("entro al for>>>>>>>");
            if (items.getDescripcionElementoRevisar().equals(itemsol.getDescripcionElementoRevisar())
                    && items.getDescripcionFalla().equals(itemsol.getDescripcionFalla())) {
                System.out.println("entro a remover>>>>>");
                citemsolicitud.listaItemsSolicitud.remove(con);
               
                break;

            }
            con++;

        }

    }

    public void agregarItemS() {
        if (citemsolicitud.getInstance().getDescripcionElementoRevisar().equals("") || citemsolicitud.getInstance().getDescripcionFalla().equals("")) {
            System.out.println("\n\nENTRO A PRESENTAR MENSAJE>>>>>>>>>");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "campos abligatorios, elemento a revisar, descripción de la falla"));

        } else {
            System.out.println("\n\nentro a agregar>>>>>>>.");
            citemsolicitud.listaItemsSolicitud.add(citemsolicitud.getInstance());
            System.out.println("a;ade a lista>>>>>."+citemsolicitud.getInstance());
            citemsolicitud.setInstance(new ItemSolicitudReparacion());

        }

    }

    public void setListaSolicitud(List<SolicitudReparacionMantenimiento> listaSolicitud) {
        this.listaSolicitud = listaSolicitud;
//        listaItemsSolicitud = getInstance().getListaItemSR();
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
        listaRequisiciones.clear();
        listaSolicitud.clear();
        System.out.println("listaaaaa solicituddd" + listaSolicitud);
        for (SolicitudReparacionMantenimiento sol : findAll(SolicitudReparacionMantenimiento.class)) {

            if (sol.getRequisicionId() == null) {
                listaRequisiciones.add(sol.getRequisicionId());
            }
            if (!sol.getAprobado()) {
                listaSolicitud.add(sol);
            }

        }

//        System.out.println("lppp" + ls);
//        for (SolicitudReparacionMantenimiento solicitudr : ls) {
//            System.out.println("iddddd" + solicitudr.getId());
//            System.out.println("entro a for lista>>>>" + solicitudr.isEstado());
//            if (solicitudr.isEstado()) {
//                System.out.println("listatesssa" + listaSolicitud);
//                listaSolicitud.add(solicitudr);
//
//                System.out.println("Entro a remover>>>>");
//                System.out.println("a;iadia" + listaSolicitud);
//
//            }
//
//        }
        System.out.println("lista requisicionessss sin solicitud" + listaRequisiciones);
        vehiculo = new Vehiculo();
        idVehiculo = 0l;
        citemsolicitud = new ControladorItemSolicitud();
        citemsolicitud.setInstance(new ItemSolicitudReparacion());
        requisicion = new Requisicion();
//        listaItemsSolicitud = new ArrayList<ItemSolicitudReparacion>();
        
        listaPersonal = findAll(Profile.class);
        listaVehiculos = findAll(Vehiculo.class);

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
        Profile psolicita = servgen.buscarPorId(Profile.class, idPersona);
        getInstance().setPsolicita(psolicita);
        getInstance().setVehiculo(vehiculo);
        if(requisicion!=null){
        if (requisicion.getId() != null) {
            requisicion.setSolicitudReparacionId(getInstance());
            requisicion.setLastUpdate(now);

            try {
                if (getInstance().getRequisicionId() != null) {
                    Requisicion r = getInstance().getRequisicionId();
                    r.setSolicitudReparacionId(null);
                    servgen.actualizar(r);
                }
                servgen.actualizar(requisicion);
            } catch (Exception e) {
                e.printStackTrace();
            }
            getInstance().setRequisicionId(requisicion);
        }}

        try {
            if (getInstance().isPersistent()) {

                guardarItem();
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizo Solicitud de Reparacion y Mantenimiento" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            } else {
                getInstance().setEstado(true);
                create(getInstance());
                guardarItem();;

                save(getInstance());
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

    public void guardarItem() {
        for (ItemSolicitudReparacion apm : citemsolicitud.listaItemsSolicitud) {
            Date now = Calendar.getInstance().getTime();
            apm.setSolicitudReparacion(getInstance());//fijarle un plan de mantenimiento a cada actividad de plan de mantenimiento
            citemsolicitud.setInstance(apm);//fija la actividad del plan de mantenimiento al controlador de actividad de plan de mantenimiento
            citemsolicitud.getInstance().setLastUpdate(now);
            citemsolicitud.guardar();
        }
        getInstance().setListaItemSR(citemsolicitud.listaItemsSolicitud);//fija la lista de actividades al plan de mantenimietno
    
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
