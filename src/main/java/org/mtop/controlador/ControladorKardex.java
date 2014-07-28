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
import org.mtop.modelo.dinamico.BussinesEntityType;
import org.mtop.modelo.Kardex;
import org.mtop.modelo.Kardex_;
import org.mtop.modelo.Requisicion;
import org.mtop.modelo.Requisicion_;
import org.mtop.modelo.SolicitudReparacionMantenimiento;
import org.mtop.modelo.SolicitudReparacionMantenimiento_;
import org.mtop.modelo.Vehiculo;

import org.mtop.servicios.ServicioGenerico;
import org.mtop.util.UI;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author carlis
 */
@Named
@ViewScoped
public class ControladorKardex extends BussinesEntityHome<Kardex> implements Serializable {

    @Inject
    @Web
    private EntityManager em;
    @Inject
    private ServicioGenerico servgen;
    List<Kardex> listakardex = new ArrayList<Kardex>();
    private String palabrab;
    private String palabrabr;
    private String palabrabs;
    private String vista;
    private List<SolicitudReparacionMantenimiento> listaSol;
    private List<Requisicion> listaReq;
    private SolicitudReparacionMantenimiento solicitud;
    private Requisicion requisicion;
  

    public String getPalabrabs() {
        return palabrabs;
    }

    public void setPalabrabs(String palabrabs) {
        this.palabrabs = palabrabs;
    }

    
    
    
    
    
    
    public String getPalabrabr() {
        return palabrabr;
    }

    public void setPalabrabr(String palabrabr) {
        this.palabrabr = palabrabr;
    }
    
    
    


    public Requisicion getRequisicion() {
        return requisicion;
    }

    public void setRequisicion(Requisicion requisicion) {
        this.requisicion = requisicion;
    }

    public List<SolicitudReparacionMantenimiento> getListaSol() {
        return listaSol;
    }

    public void setListaSol(List<SolicitudReparacionMantenimiento> listaSol) {
        this.listaSol = listaSol;
    }

    public List<Requisicion> getListaReq() {
        return listaReq;
    }

    public void setListaReq(List<Requisicion> listaReq) {
        this.listaReq = listaReq;
    }
    
    public void irASolicitud() {
        this.vista = "solicitud";
        System.out.println("fijando a >>>>" + this.vista);

    }

    public void irARequisicion() {
        this.vista = "requisicion";
        System.out.println("fijando a >>>>" + this.vista);

    }

    public String irAKardex() {
        this.vista = "kardex";
        System.out.println("fijando a >>>>" + this.vista);
        return "/paginas/admin/kardex/crear.xhtml?faces-redirect=true";
    }

    public String getVista() {
        System.out.println("retornando vistaaaaa" + vista);
        return vista;
    }

    public void setVista(String vista) {
        System.out.println("VISta>>>>" + vista);
        this.vista = vista;
    }

    public String formato(Date fecha) {
        String fechaFormato = "";
        if (fecha != null) {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            fechaFormato = formatter.format(fecha);
        }

        return fechaFormato;

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
        System.out.println("REEEEq>>>>>>>" + Requisicion.class.getName());
        System.out.println("atruiii>>>>>>>" + Requisicion_.numRequisicion.getName());
        List<Kardex> lr = servgen.buscarTodoscoincidencia(Kardex.class, "Kardex", Kardex_.numero.getName(), query);

        for (Kardex kar : lr) {
            System.out.println("econtro uno " + kar.getNumero());
            ced.add(kar.getNumero());
        }
        System.out.println("listaaaaa autocompletar" + ced);
        return ced;
    }

    public ArrayList<String> autocompletarr(String query) {
        ArrayList<String> ced = new ArrayList<String>();
       
        List<Requisicion> lr = servgen.buscarTodoscoincidencia(Requisicion.class, Requisicion.class.getSimpleName(), Requisicion_.numRequisicion.getName(), query);

        for (Requisicion item : lr) {
            System.out.println("econtro uno " + item.getNumRequisicion());
            if (!ced.contains(item.getNumRequisicion())) {
                ced.add(item.getNumRequisicion());
            }

        }
        System.out.println("listaaaaa autocompletar" + ced);
        return ced;
    }
     public ArrayList<String> autocompletars(String query) {
        ArrayList<String> ced = new ArrayList<String>();
        List<SolicitudReparacionMantenimiento> ls = servgen.buscarTodoscoincidencia(SolicitudReparacionMantenimiento.class, SolicitudReparacionMantenimiento.class.getSimpleName(), SolicitudReparacionMantenimiento_.numSolicitud.getName(), query);

        for (SolicitudReparacionMantenimiento item : ls) {
            System.out.println("econtro uno " + item.getNumSolicitud());
            ced.add(item.getNumSolicitud());
        }
        
        System.out.println("listaaaaa autocompletar" + ced);
        return ced;
    }
    

    public void buscar() {

        List<Kardex> le = new ArrayList<Kardex>();
        le.clear();
        if (palabrab == null || palabrab.equals("") || palabrab.contains(" ")) {
            palabrab = "Ingrese algun valor a buscar";
        }
        le = servgen.buscarTodoscoincidencia(Kardex.class, Kardex.class.getSimpleName(), Kardex_.numero.getName(), palabrab);
        if (le.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrab.equals("Ingrese algun valor a buscar")) {
                context.addMessage(null, new FacesMessage("INFORMACION: Ingrese algun valor a buscar"));
                palabrab = " ";
            } else {
                context.addMessage(null, new FacesMessage("INFORMACION: No se ha encontrado " + palabrab));
            }

        } else {
            listakardex = le;
        }

    }
    public void buscars() {

        List<SolicitudReparacionMantenimiento> le = new ArrayList<SolicitudReparacionMantenimiento>();
        le.clear();
        if (palabrab == null || palabrab.equals("") || palabrab.contains(" ")) {
            palabrab = "Ingrese algun valor a buscar";
        }
        le = servgen.buscarTodoscoincidencia(SolicitudReparacionMantenimiento.class, SolicitudReparacionMantenimiento.class.getSimpleName(), SolicitudReparacionMantenimiento_.numSolicitud.getName(), palabrab);
        if (le.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrab.equals("Ingrese algun valor a buscar")) {
                context.addMessage(null, new FacesMessage("INFORMACION: Ingrese algun valor a buscar"));
                palabrab = " ";
            } else {
                context.addMessage(null, new FacesMessage("INFORMACION: No se ha encontrado " + palabrab));
            }

        } else {
            listaSol = le;
           // getInstance().setListaSolicitudReparacion(listaSol);
        }

    }
    public void buscarr() {

         List<Requisicion> le = new ArrayList<Requisicion>();
        le.clear();
        if (palabrab == null || palabrab.equals("") || palabrab.contains(" ")) {
            palabrab = "Ingrese algun valor a buscar";
        }
        le = servgen.buscarTodoscoincidencia(Requisicion.class, Requisicion.class.getSimpleName(), Requisicion_.numRequisicion.getName(), palabrab);
        if (le.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrab.equals("Ingrese algun valor a buscar")) {
                context.addMessage(null, new FacesMessage("INFORMACION: Ingrese algun valor a buscar"));
                palabrab = " ";
            } else {
                context.addMessage(null, new FacesMessage("INFORMACION: No se ha encontrado " + palabrab));
            }

        } else {
            listaReq = le;
           // getInstance().setListaSolicitudReparacion(listaSol);
        }

    }

    public void limpiar() {
        palabrab = "";
        listakardex = findAll(Kardex.class);
    }
    public void limpiarr() {
        palabrab = "";
        listaReq = getInstance().getListaRequisicion();
    }
    public void limpiars() {
        palabrab = "";
        listaSol = getInstance().getListaSolicitudReparacion();
    }

    public Long getKardexId() {
        return (Long) getId();
    }

    public void setKardexId(Long kardexId) {
        System.out.println("recuprando krdezzzzzzzz");
        setId(kardexId);

    }

    public Long getSolicitudId() {
        if (solicitud != null) {
            return solicitud.getId();
        } else {
            return 0l;
        }

    }

    public void setSolicitudId(Long id) {
          
        System.out.println("\n\n\n\n\n\n\n\nfijando SOlidituuuuud en guardar\n\n\n\n");
        solicitud = findById(SolicitudReparacionMantenimiento.class, id);
        Date now = Calendar.getInstance().getTime();

        if (solicitud != null) {

            try {

                System.out.println("\n\n\n\n\nrecupero solicitud\n" + solicitud);
                solicitud.setKardex(null);
                solicitud.setLastUpdate(now);
                solicitud.setAprobado(false);
              
                save(solicitud);
                System.out.println("\n\n\n\n\n\n\nguando solicicitud con kardex cooon" + solicitud.getKardex());
                getInstance().setLastUpdate(now);
                System.out.println("\n\n\n\nantessguardo kardex con solicitudes" + getInstance().getListaSolicitudReparacion());
                getInstance().getListaSolicitudReparacion().remove(solicitud);

                servgen.actualizar(getInstance());
                System.out.println("\n\n\n\nguardo kardex con solicitudes" + getInstance().getListaSolicitudReparacion());
              listaSol=getInstance().getListaSolicitudReparacion();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            solicitud.setNumSolicitud("");
        }

        System.out.println("fijanddsasadasdadassssssssssss");
    }

    public Long getRequisicionId() {
        if (solicitud != null) {
            return solicitud.getId();
        } else {
            return 0l;
        }

    }

    public void setRequisicionId(Long id) {
        requisicion = findById(Requisicion.class, id);
        Date now = Calendar.getInstance().getTime();
        System.out.println("fijando REquisicion en guardar");

        if (solicitud != null) {

            try {

                System.out.println("recupero requisicion " +requisicion);
                requisicion.setKardex(null);
                requisicion.setLastUpdate(now);
                requisicion.setAprobado(false);
                save(requisicion);
                getInstance().setLastUpdate(now);
                getInstance().getListaRequisicion().remove(requisicion);

                servgen.actualizar(getInstance());
                System.out.println("guardo kardex con solicitudes" + getInstance().getListaSolicitudReparacion());
                listaReq=getInstance().getListaRequisicion();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            solicitud.setNumSolicitud("");
        }

        System.out.println("fijanddsasadasdadassssssssssss");
    }

    public void guardarSolicitud(SolicitudReparacionMantenimiento sol) {
        Date now = Calendar.getInstance().getTime();
        System.out.println("fijando SOlidituuzzxxzzuuud en guardar");
        solicitud = sol;
        setVista("kardex");
        if (solicitud != null) {

            try {

                System.out.println("recupero requisicion " +requisicion);
                requisicion.setKardex(getInstance());
                requisicion.setLastUpdate(now);
                requisicion.setAprobado(true);
                requisicion.setKardex(getInstance());
                save(requisicion);
                System.out.println("guando requi coon con kardex cooon" + requisicion.getKardex());
                getInstance().setLastUpdate(now);
                getInstance().getListaRequisicion().add(requisicion);

                servgen.actualizar(getInstance());
                System.out.println("guardo kardex con solicitudes" + getInstance().getListaSolicitudReparacion());
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            solicitud.setNumSolicitud("");
        }

        System.out.println("fijanddsasadasdadassssssssssss");
       // return "/paginas/admin/kardex/crear.xhtml?faces-redirect=true";
    }
    
    
    public void guardarRequisicion(Requisicion req) {
        Date now = Calendar.getInstance().getTime();
        System.out.println("fijando SOlidituuzzxxzzuuud en guardar");
        requisicion = req;
        setVista("kardex");
        if (solicitud != null) {

            try {

                System.out.println("recupero requisicion" + requisicion);
                requisicion.setKardex(getInstance());
                requisicion.setLastUpdate(now);
                requisicion.setAprobado(true);
                requisicion.setKardex(getInstance());
                
                save(requisicion);
                System.out.println("guando requisicion con kardex cooon" + requisicion.getKardex());
                getInstance().setLastUpdate(now);
                getInstance().getListaRequisicion().add(requisicion);

                servgen.actualizar(getInstance());
                System.out.println("guardo kardex con solicitudes" + getInstance().getListaRequisicion());
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            solicitud.setNumSolicitud("");
        }

        System.out.println("fijanddsasadasdadassssssssssss");
        // return "/paginas/admin/kardex/crear.xhtml?faces-redirect=true";
    }

    @TransactionAttribute   //
    public Kardex load() {
        if (isIdDefined()) {
            wire();
        }
        //  log.info("sgssalud --> cargar instance " + getInstance());
        return getInstance();
    }

    @TransactionAttribute
    public void wire() {
        getInstance();
    }

    public List<Kardex> getListakardex() {

        return listakardex;
    }

    public void setListakardex(List<Kardex> listakardex) {
        this.listakardex = listakardex;
    }

    @PostConstruct
    public void init() {
        setEntityManager(em);
        /*el bussinesEntityService.setEntityManager(em) solo va si la Entidad en este caso (ConsultaMedia)
         *hereda de la Entidad BussinesEntity...  caso contrario no se lo agrega
         */
        System.out.println("antes de fija em en kardex");
        bussinesEntityService.setEntityManager(em);
        System.out.println("despues de fija em en kardex");
        servgen.setEm(em);
        listakardex = servgen.buscarTodos(Kardex.class);
        solicitud = new SolicitudReparacionMantenimiento();
        listaSol=new ArrayList<SolicitudReparacionMantenimiento>();
        listaReq=new ArrayList<Requisicion>();
        for (Kardex kardex : listakardex) {
            if(kardex.getId().equals(getInstance().getId())){
               listaSol=kardex.getListaSolicitudReparacion();
               listaReq=kardex.getListaRequisicion();
            }
        }
        
//        List<Kardex> lk = servgen.buscarTodos(Kardex.class);
//        listakardex.clear();
//
//        for (Kardex kardex : lk) {
//            if (kardex.isEstado()) {
//                listakardex.add(kardex);
//                System.out.println("Entro a remover>>>>");
//                System.out.println("a;iadia" + listakardex);
//
//            }
//
//        }
    }

    @Override
    protected Kardex createInstance() {
        //prellenado estable para cualquier clase 
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(Kardex.class.getName());
        Date now = Calendar.getInstance().getTime();
        Kardex kardex = new Kardex();
        kardex.setCreatedOn(now);
        kardex.setLastUpdate(now);
        kardex.setActivationTime(now);
        kardex.setType(_type);
        kardex.buildAttributes(bussinesEntityService);  //
        return kardex;
    }

    @Override
    public Class<Kardex> getEntityClass() {
        return Kardex.class;
    }

    public void saveK() {
        save(this);
    }

    @TransactionAttribute
    public String guardar() {
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        System.out.println("entro guardar kardexxxxxxxxxx");
        this.instance.setVehiculo(getInstance().getVehiculo());

        try {
            if (getInstance().isPersistent()) {
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizo Kardex" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            } else {
                System.out.println("Guardando kardeXXXXXXXXXXXXXX" + getInstance().getNumero());
                System.out.println("Guardando kardeXXXXXXXXXXXXXX" + getInstance().getVehiculo());
                getInstance().setEstado(true);
                System.out.println("lallalalallalalallalalalla");
                create(getInstance());

                save(getInstance());
                System.out.println("se ha creado000000000000000000000000000000000000000000000 " + getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se creo Kardex" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ErrorRRRRRRRRRRRRRRRRRRRRRR al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
            System.out.println("errorrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
        }
        return "/paginas/admin/kardex/lista.xhtml?faces-redirect=true";
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
        return "/paginas/admin/kardex/lista.xhtml?faces-redirect=true";
    }
}
