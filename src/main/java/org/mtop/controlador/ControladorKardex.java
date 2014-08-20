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
    private List<Kardex> listakardex = new ArrayList<Kardex>();
    private List<SolicitudReparacionMantenimiento> listaSol;
    private List<Requisicion> listaReq;
    
    private List<Kardex> listakardex2 = new ArrayList<Kardex>();
    private List<SolicitudReparacionMantenimiento> listaSol2;
    private List<Requisicion> listaReq2;
    
    private String palabrab;
    private String palabrabr;
    private String palabrabs;
    private String vista;
    
    private SolicitudReparacionMantenimiento solicitud;
    private Requisicion requisicion;
    private String observaciones = "";

    public List<Kardex> getListakardex2() {
        return listakardex2;
    }

    public void setListakardex2(List<Kardex> listakardex2) {
        this.listakardex2 = listakardex2;
    }

    public List<SolicitudReparacionMantenimiento> getListaSol2() {
        return listaSol2;
    }

    public void setListaSol2(List<SolicitudReparacionMantenimiento> listaSol2) {
        this.listaSol2 = listaSol2;
    }

    public List<Requisicion> getListaReq2() {
        return listaReq2;
    }

    public void setListaReq2(List<Requisicion> listaReq2) {
        this.listaReq2 = listaReq2;
    }
    
    
    
    
    
    
    public String getObservaciones() {
        System.out.println("recuperando" + observaciones);
        return observaciones;

    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
        System.out.println("fijo observaciones" + observaciones);
    }

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
        System.out.println("retornando lista en kardes soliss en get" + listaSol);
        return listaSol;
    }

    public void setListaSol(List<SolicitudReparacionMantenimiento> listaSol) {
        this.listaSol = listaSol;
    }

    public List<Requisicion> getListaReq() {
        System.out.println("retornando lista en kardes requisicc" + listaReq);
        System.out.println("retornando lista en kardes soliss" + listaSol);
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
        System.out.println("lista reqs" + listaReq);
        System.out.println("lista sols" + listaSol);
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
        System.out.println("lista kardex2"+listakardex2);
       
        for (Kardex kar : listakardex2) {
            if(kar.getNumero().contains(query)){
                ced.add(kar.getNumero());
                  System.out.println("econtro uno " + kar.getNumero());
            }
          
            
        }
        System.out.println("listaaaaa autocompletar" + ced);
        return ced;
    }

    public ArrayList<String> autocompletarr(String query) {
        System.out.println("\n\nQUEryyyyy" + query);

        ArrayList<String> ced = new ArrayList<String>();
        String s;
        for (Requisicion requisicion : listaReq2) {
            if (requisicion.getNumRequisicion().contains(query) ) {
                
                    ced.add(requisicion.getNumRequisicion());
            }
            s = requisicion.getFechaRequisicion().toString();
            System.out.println("valor de SSSSSSSSSSSS" + s);
            System.out.println("valor de aTributooooooooooooooooooooo" + String.class.cast(query));
            if (s.contains(String.class.cast(query))) {
                System.out.println("anado una fecha>>>>"+s);
                ced.add(s);
            }

        }

      

        System.out.println("\n\nlistaaaaa autocompletar" + ced);
        return ced;

    }

    public ArrayList<String> autocompletars(String query) {
        System.out.println("QUEryyyyy" + query);

        ArrayList<String> ced = new ArrayList<String>();

       
        String s;
        for (SolicitudReparacionMantenimiento soli : listaSol2) {
            System.out.println("econtro uno " + soli.getNumSolicitud());
            //buscar por nuemro
            if (soli.getNumSolicitud().contains(query)) {
                    ced.add(soli.getNumSolicitud());
               
            }
            //buscar por fechas
            s = soli.getFechaSolicitud().toString();
            System.out.println("valor de SSSSSSSSSSSS" + s);
            System.out.println("valor de aTributooooooooooooooooooooo" + String.class.cast(query));
            if (s.contains(String.class.cast(query))) {
                System.out.println("anado una fecha>>>>"+s);
                ced.add(s);
            }

        }
        

        System.out.println("listaaaaa autocompletar" + ced);
        return ced;

    }

    public void buscar() {

        List<Kardex> le = new ArrayList<Kardex>();
        le.clear();
        palabrab=palabrab.trim();
        if (palabrab == null || palabrab.equals("") ) {
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
        palabrabs = palabrabs.trim();
        if (palabrabs == null || palabrabs.equals("")) {
            palabrabs = "Ingrese algun valor a buscar";
        }
        System.out.println("ingreso la palabra>>>>" + palabrabs);
        //buscando por coincidencia
        List<SolicitudReparacionMantenimiento> le = servgen.buscarTodoscoincidencia(SolicitudReparacionMantenimiento.class, SolicitudReparacionMantenimiento.class.getSimpleName(), SolicitudReparacionMantenimiento_.numSolicitud.getName(), palabrabs);
        //buscando por fechas
        List<SolicitudReparacionMantenimiento> lef = servgen.buscarSolicitudporFecha(SolicitudReparacionMantenimiento_.fechaSolicitud.getName(), palabrabs);
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
            if (palabrabs.equals("Ingrese algun valor a buscar")) {
                context.addMessage(null, new FacesMessage("INFORMACION: Ingrese algun valor a buscar"));
                palabrabs = " ";
            } else {
                context.addMessage(null, new FacesMessage("INFORMACION: No se ha encontrado " + palabrab));
            }

        } else {
            listaSol.clear();
            SolicitudReparacionMantenimiento sol = new SolicitudReparacionMantenimiento();
            for (Long r : lsoli) {
                System.out.println("Entro al for>>>>>>");
                sol = findById(SolicitudReparacionMantenimiento.class, r);
                System.out.println("estado>>>>" + sol.isEstado());
                System.out.println("vehiculo>>>>>>>>>" + getInstance().getVehiculo().getId());
                System.out.println("vehiculo>>>>>>>>>" + sol.getVehiculo().getId());
                System.out.println("numero de requisicion>>>>" + sol.getNumSolicitud());
                if (sol.isEstado() && sol.getVehiculo().getId().equals(getInstance().getVehiculo().getId())) {
                    System.out.println("entro a comparacion>>>>>");
                    listaSol.add(sol);
                    System.out.println("\n\nlista final>>>>" + listaSol);

                }
            }
            palabrabs = "";
        }

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
            listaReq.clear();
            Requisicion re = new Requisicion();
            System.out.println("lista que esta recorriendo>>>>>" + lrq);
            for (Long r : lrq) {
                System.out.println("Entro al for>>>>>>");
                re = findById(Requisicion.class, r);
                System.out.println("estado>>>>" + re.isEstado());
                System.out.println("vehiculo>>>>>>>>>" + getInstance().getVehiculo().getId());
                System.out.println("vehiculo>>>>>>>>>" + re.getVehiculo().getId());
                System.out.println("numero de requisicion>>>>" + re.getNumRequisicion());
                if (re.isEstado() && re.getVehiculo().getId() == getInstance().getVehiculo().getId()) {
                    System.out.println("entro a comparacion>>>>>");
                    listaReq.add(re);
                    System.out.println("\n\nlista final>>>>" + listaReq);

                }
            }

            palabrabr = "";
        }

    }

    public void limpiar() {
        palabrab = "";
        listakardex = findAll(Kardex.class);
    }

    public void limpiarr() {
        palabrabr = "";
        List<Requisicion> lr = servgen.buscarTodos(Requisicion.class);
        listaReq.clear();
        System.out.println("lppp" + lr);

        for (Requisicion r : lr) {
            if (r.getKardex() != null) {
                if (r.getKardex().getId().equals(getInstance().getId())) {
                    System.out.println("listatesssa" + listaReq);
                    listaReq.add(r);

                }
            }

        }
    }

    public void limpiars() {
        palabrabs = "";

        List<SolicitudReparacionMantenimiento> ls = findAll(SolicitudReparacionMantenimiento.class);

        listaSol.clear();
        System.out.println("lppp" + ls);
        System.out.println("vehisuclo " + getInstance().getVehiculo());
        for (SolicitudReparacionMantenimiento soli : ls) {
            System.out.println("num sel la soli en lipiar " + soli.getNumSolicitud());
            System.out.println("estado sel la soli en lipiar " + soli.isEstado());
            System.out.println("req sel la soli en lipiar " + soli.getRequisicionId());
            System.out.println("vehiculo sel la soli en lipiar " + soli.getVehiculo());
            if (soli.getKardex() != null) {
                if (soli.getKardex().getId().equals(getInstance().getId())) {
                    System.out.println("listatesssa" + listaSol);
                    listaSol.add(soli);
                }

            }

        }
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
                listaSol = getInstance().getListaSolicitudReparacion();
                listaSol2=listaSol;
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

                System.out.println("recupero requisicion " + requisicion);
                requisicion.setKardex(null);
                requisicion.setLastUpdate(now);
                requisicion.setAprobado(false);
                save(requisicion);
                getInstance().setLastUpdate(now);
                getInstance().getListaRequisicion().remove(requisicion);

                servgen.actualizar(getInstance());
                System.out.println("guardo kardex con solicitudes" + getInstance().getListaSolicitudReparacion());
                listaReq = getInstance().getListaRequisicion();
                listaReq2=listaReq;
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

                System.out.println("recupero requisicion " + requisicion);
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
        listakardex2=listakardex;
        solicitud = new SolicitudReparacionMantenimiento();
        System.out.println("lista Sol" + listaSol);
        if (listaSol == null) {

            for (Kardex kardex : listakardex) {
                if (kardex.getId().equals(getInstance().getId())) {
                    listaSol = kardex.getListaSolicitudReparacion();
                    listaSol2=listaSol;

                }
            }
        }
        if (listaReq == null) {

            for (Kardex kardex : listakardex) {
                if (kardex.getId().equals(getInstance().getId())) {

                    listaReq = kardex.getListaRequisicion();
                    listaReq2=listaReq;
                }
            }
        }
        System.out.println("lista Soliss" + listaSol);
         System.out.println("lista requiss" + listaReq);

//      
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
        System.out.println("\n\n\n\n\nentro guardar kardexxxxxxxxxx");
        this.instance.setVehiculo(getInstance().getVehiculo());
        System.out.println("observacion kardex fuera" + getInstance().getObservaciones());
        try {
            if (getInstance().isPersistent()) {
                System.out.println("observacion kardex" + getInstance().getObservaciones());
                save(getInstance());

                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizo Kardex" + getInstance().getId() + " con éxito", " ");
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
