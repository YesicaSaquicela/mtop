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
import org.mtop.modelo.dinamico.BussinesEntityType;
import org.mtop.modelo.Kardex;
import org.mtop.modelo.Requisicion;
import org.mtop.modelo.SolicitudReparacionMantenimiento;

import org.mtop.servicios.ServicioGenerico;

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
    @EJB
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
    private String palabrarna;

    private SolicitudReparacionMantenimiento solicitud;
    private Requisicion requisicion;
    private String observaciones = "";
    private List<Requisicion> listareqNoAp;
    private List<SolicitudReparacionMantenimiento> listasolNoAp;
    private List<Requisicion> listareqNoAp2;
    private String palabrasna;
    private List<SolicitudReparacionMantenimiento> listasolNoAp2;

    public List<SolicitudReparacionMantenimiento> getListasolNoAp2() {
        return listasolNoAp2;
    }

    public void setListasolNoAp2(List<SolicitudReparacionMantenimiento> listasolNoAp2) {
        this.listasolNoAp2 = listasolNoAp2;
    }
    
    

    public String getPalabrasna() {
        return palabrasna;
    }

    public void setPalabrasna(String palabrasna) {
        this.palabrasna = palabrasna;
    }
    
    

    public List<Requisicion> getListareqNoAp2() {
        return listareqNoAp2;
    }

    public void setListareqNoAp2(List<Requisicion> listareqNoAp2) {
        this.listareqNoAp2 = listareqNoAp2;
    }
    
    

    public List<SolicitudReparacionMantenimiento> getListasolNoAp() {
        return listasolNoAp;
    }

    public void setListasolNoAp(List<SolicitudReparacionMantenimiento> listasolNoAp) {
        this.listasolNoAp = listasolNoAp;
    }

    public SolicitudReparacionMantenimiento getSolicitud() {
        System.out.println("get solicitud" + solicitud);
        return solicitud;
    }

    public void setSolicitud(SolicitudReparacionMantenimiento solicitud) {
        this.solicitud = solicitud;
        System.out.println("asignando solicitud>>>>" + solicitud);
    }

    public String getPalabrarna() {
        System.out.println("obtiene palabra"+palabrarna);
        return palabrarna;
    }

    public void setPalabrarna(String palabrarna) {
        this.palabrarna = palabrarna;
        System.out.println("palabra fija>>>>" + palabrarna);
    }

    public List<Requisicion> getListareqNoAp() {
        return listareqNoAp;
    }

    public void setListareqNoAp(List<Requisicion> listareqNoAp) {
        this.listareqNoAp = listareqNoAp;
    }

//   
//    public void darDeBajaS(Long idSol) {
//        System.out.println("entro");
//        List<SolicitudReparacionMantenimiento> lsm = new ArrayList<SolicitudReparacionMantenimiento>();
//        Date now = Calendar.getInstance().getTime();
//        SolicitudReparacionMantenimiento sr = servgen.buscarPorId(SolicitudReparacionMantenimiento.class, idSol);
//        for (SolicitudReparacionMantenimiento sol : listaSol) {
//            System.out.println("entro al for>>>");
//            if (!sol.getId().equals(sr.getId())) {
//                System.out.println("entro al if ad");
//                lsm.add(sol);
//
//            }
//        }
//        listaSol = lsm;
//        getInstance().setLastUpdate(now);
//        getInstance().setListaSolicitudReparacion(listaSol);
//        save(getInstance());
//        System.out.println("rq" + sr);
//        System.out.println("num" + sr.getNumSolicitud());
//        sr.setLastUpdate(now);
//        sr.setAprobado(false);
//        System.out.println("cambio de estado aprobado" + sr.getAprobado());
//        sr.setKardex(null);
//        save(sr);
//        System.out.println("lista de requ no el" + listaSol);
//        listasolNoAp.add(sr);
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "La SOLICITUD seleccionada seleccionada " + sr.getNumSolicitud() + " ha pasado al estado  ", "NO APROBADA"));
//
//    }

//    @Transactional
//    public void darDeBajaR(Long idReq) {
//        
//           
//        System.out.println("entro");
//        System.out.println("llega el id>>>" + idReq);
//        List<Requisicion> lre = new ArrayList<Requisicion>();
//        Date now = Calendar.getInstance().getTime();
//        Requisicion rq = servgen.buscarPorId(Requisicion.class, idReq);
//        for (Requisicion requisicion : listaReq) {
//            System.out.println("entro al for>>>");
//            if (!requisicion.getId().equals(rq.getId())) {
//                System.out.println("entro al if ad");
//                lre.add(requisicion);
//
//            }
//        }
//        listaReq = lre;
//
//        System.out.println("rq" + rq);
//        System.out.println("num" + rq.getNumRequisicion());
//        rq.setLastUpdate(now);
//        rq.setAprobado(false);
//        System.out.println("cambio de estado aprobado" + rq.getAprobado());
//        rq.setKardex(null);
//        save(rq);
//        
//        getInstance().setLastUpdate(now);
//        getInstance().setListaRequisicion(listaReq);
//        save(getInstance());
//        System.out.println("lista de requ no el" + listaReq);
//        listakardex=findAll(Kardex.class);
//        em.close();
//        listareqNoAp.add(rq);
//        System.out.println("anadiendo a la lista de req no a" + listareqNoAp);
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "La REQUISICIÓN seleccionada seleccionada " + rq.getNumRequisicion() + " ha pasado al estado  ", "NO APROBADA"));
//
//    }

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
        System.out.println("lista kardex2" + listakardex2);

        for (Kardex kar : listakardex2) {
            if (kar.getNumero().contains(query)) {
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
            if (requisicion.getNumRequisicion().contains(query)) {

                ced.add(requisicion.getNumRequisicion());
            }
            s = requisicion.getFechaRequisicion().toString();
            System.out.println("valor de SSSSSSSSSSSS" + s);
            System.out.println("valor de aTributooooooooooooooooooooo" + String.class.cast(query));
            if (s.contains(String.class.cast(query)) && !ced.contains(s)) {
                System.out.println("anado una fecha>>>>" + s);
                ced.add(s);
            }

        }

        System.out.println("\n\nlistaaaaa autocompletar" + ced);
        return ced;

    }

    public ArrayList<String> autocompletarrna(String query) {

        System.out.println("\n\nQUEryyyyy" + query);

        ArrayList<String> ced = new ArrayList<String>();
        String s;
        for (Requisicion requisicion : listareqNoAp) {
            if (requisicion.getNumRequisicion().contains(query)) {
                ced.add(requisicion.getNumRequisicion());
            }
            s = requisicion.getFechaRequisicion().toString();
            System.out.println("valor de SSSSSSSSSSSS" + s);
            System.out.println("valor de aTributooooooooooooooooooooo" + String.class.cast(query));
            if (s.contains(String.class.cast(query)) && !ced.contains(s)) {
                System.out.println("anado una fecha>>>>" + s);
                ced.add(s);
            }

        }

        System.out.println("\n\nlistaaaaa autocompletar" + ced);
        System.out.println("pala en autocompletar" + palabrarna);
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
            if (s.contains(String.class.cast(query)) && !ced.contains(s)) {
                System.out.println("anado una fecha>>>>" + s);
                ced.add(s);
            }

        }

        System.out.println("listaaaaa autocompletar" + ced);
        return ced;

    }
    
    public ArrayList<String> autocompletarsna(String query) {
        System.out.println("QUEryyyyy" + query);

        ArrayList<String> ced = new ArrayList<String>();

        String s;
        for (SolicitudReparacionMantenimiento soli : listasolNoAp) {
            System.out.println("econtro uno " + soli.getNumSolicitud());
            //buscar por nuemro
            if (soli.getNumSolicitud().contains(query)) {
                ced.add(soli.getNumSolicitud());

            }
            //buscar por fechas
            s = soli.getFechaSolicitud().toString();
            System.out.println("valor de SSSSSSSSSSSS" + s);
            System.out.println("valor de aTributooooooooooooooooooooo" + String.class.cast(query));
            if (s.contains(String.class.cast(query)) && !ced.contains(s)) {
                System.out.println("anado una fecha>>>>" + s);
                ced.add(s);
            }

        }

        System.out.println("listaaaaa autocompletar" + ced);
        return ced;

    }

    public void buscar() {

        List<Kardex> le = new ArrayList<Kardex>();
        le.clear();
        palabrab = palabrab.trim();
        if (palabrab == null || palabrab.equals("")) {
            palabrab = "Ingrese algun valor a buscar";
        }

        List<Kardex> lk = new ArrayList<Kardex>();
        for (Kardex kd : listakardex2) {
            if (kd.getNumero().contains(palabrab)) {
                lk.add(kd);
            }
        }

        if (lk.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrab.equals("Ingrese algun valor a buscar")) {
                context.addMessage(null, new FacesMessage("INFORMACION: Ingrese algun valor a buscar"));
                palabrab = " ";
            } else {
                context.addMessage(null, new FacesMessage("INFORMACION: No se ha encontrado " + palabrab));
            }

        } else {
            listakardex = lk;
        }

    }

    public void buscars() {
        palabrabs = palabrabs.trim();
        if (palabrabs == null || palabrabs.equals("")) {
            palabrabs = "Ingrese algun valor a buscar";
        }
        System.out.println("ingreso la palabra>>>>" + palabrabs);
        //buscando por coincidencia
        String s;
        List<SolicitudReparacionMantenimiento> lsoli = new ArrayList<SolicitudReparacionMantenimiento>();
        for (SolicitudReparacionMantenimiento srm : listaSol2) {
            if (srm.getNumSolicitud().contains(palabrabs)) {
                lsoli.add(srm);
            } else {
                s = srm.getFechaSolicitud().toString();
                System.out.println("valor de SSSSSSSSSSSS" + s);
                System.out.println("valor de aTributooooooooooooooooooooo" + String.class.cast(palabrabs));
                if (s.contains(palabrabs)) {
                    System.out.println("anado una fecha>>>>" + s);
                    lsoli.add(srm);
                }
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
            listaSol = lsoli;
            palabrabs = "";
        }

    }

     
     
    public void buscarrna() {
        System.out.println("\n\n\nENTRO A BUSCAR REQU11111I>>>>>>" + palabrarna);
        palabrarna = palabrarna.trim();
        if (palabrarna == null || palabrarna.equals("")) {
            palabrarna = "Ingrese algun valor a buscar";
        }
        //buscando por coincidencia

        List<Requisicion> lrq = new ArrayList<Requisicion>();
        String s;
        for (Requisicion r : listareqNoAp) {
            if (r.getNumRequisicion().contains(palabrarna)) {
                lrq.add(r);
            } else {
                s = r.getFechaRequisicion().toString();
                System.out.println("valor de SSSSSSSSSSSS" + s);
                System.out.println("valor de aTributooooooooooooooooooooo" + String.class.cast(palabrarna));
                if (s.contains(palabrarna)) {
                    System.out.println("anado una fecha>>>>" + s);
                    lrq.add(r);
                }
            }

        }

        System.out.println("LEeeee" + lrq);

        if (lrq.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrarna.equals("Ingrese algun valor a buscar")) {
                context.addMessage(null, new FacesMessage("INFORMACION: Ingrese algun valor a buscar"));
                palabrarna = " ";
            } else {
                context.addMessage(null, new FacesMessage("INFORMACION: No se ha encontrado " + palabrarna));
            }

        } else {
            listareqNoAp = lrq;
        }

        palabrabr = "";

    }
    
    public void buscarsna() {
        palabrasna = palabrasna.trim();
        if (palabrasna == null || palabrasna.equals("")) {
            palabrasna = "Ingrese algun valor a buscar";
        }
        System.out.println("ingreso la palabra>>>>" + palabrasna);
        //buscando por coincidencia
        String s;
        List<SolicitudReparacionMantenimiento> lsoli = new ArrayList<SolicitudReparacionMantenimiento>();
        for (SolicitudReparacionMantenimiento srm : listasolNoAp) {
            if (srm.getNumSolicitud().contains(palabrasna)) {
                lsoli.add(srm);
            } else {
                s = srm.getFechaSolicitud().toString();
                System.out.println("valor de SSSSSSSSSSSS" + s);
                System.out.println("valor de aTributooooooooooooooooooooo" + String.class.cast(palabrasna));
                if (s.contains(palabrasna)) {
                    System.out.println("anado una fecha>>>>" + s);
                    lsoli.add(srm);
                }
            }

        }

        System.out.println("LEeeee" + lsoli);

        if (lsoli.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrasna.equals("Ingrese algun valor a buscar")) {
                context.addMessage(null, new FacesMessage("INFORMACION: Ingrese algun valor a buscar"));
                palabrasna = " ";
            } else {
                context.addMessage(null, new FacesMessage("INFORMACION: No se ha encontrado " + palabrasna));
            }

        } else {
            listasolNoAp = lsoli;
            palabrabs = "";
        }

    }

    public void buscarr() {
        System.out.println("\n\n\nENTRO A BUSCAR REQU11111I>>>>>>");
        palabrabr = palabrabr.trim();
        if (palabrabr == null || palabrabr.equals("")) {
            palabrabr = "Ingrese algun valor a buscar";
        }
        //buscando por coincidencia

        List<Requisicion> lrq = new ArrayList<Requisicion>();
        String s;
        for (Requisicion r : listaReq2) {
            if (r.getNumRequisicion().contains(palabrabr)) {
                lrq.add(r);
            } else {
                s = r.getFechaRequisicion().toString();
                System.out.println("valor de SSSSSSSSSSSS" + s);
                System.out.println("valor de aTributooooooooooooooooooooo" + String.class.cast(palabrabr));
                if (s.contains(palabrabr)) {
                    System.out.println("anado una fecha>>>>" + s);
                    lrq.add(r);
                }
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
            listaReq = lrq;
        }

        palabrabr = "";

    }

    public void limpiar() {
        palabrab = "";
        listakardex = findAll(Kardex.class);
    }

    public void limpiarr() {
        palabrabr = "";

        listaReq = listaReq2;
    }

    public void limpiarna() {
        palabrarna = "";
       listareqNoAp=listareqNoAp2;
        System.out.println("lista de req2"+listareqNoAp);
    }

    public void limpiars() {
        palabrabs = "";
        listaSol = listaSol2;
    }
    
    public void limpiarsna() {
        palabrasna = "";
        listasolNoAp = listasolNoAp2;
    }

    public Long getKardexId() {
        return (Long) getId();
    }

    public void setKardexId(Long kardexId) {
        System.out.println("recuprando krdezzzzzzzz");
        setId(kardexId);
        System.out.println("lista Sol" + listaSol);
        if (listaSol == null) {
            System.out.println("entro a listaSol");
            for (Kardex kardex : listakardex) {
                System.out.println("entro al for sol");
                System.out.println("id karrdex lista" + kardex.getId());
                System.out.println("id get Instance" + getInstance().getId());
                if (kardex.getId().equals(getInstance().getId())) {
                    System.out.println("entro al if... sol");
                    listaSol = kardex.getListaSolicitudReparacion();
                    listaSol2 = listaSol;

                }
            }
        }
        System.out.println("lista de soli desp for>>>>>" + listaSol);
        if (listaReq == null) {
            System.out.println("entro a listaSol");
            for (Kardex kardex : listakardex) {
                if (kardex.getId().equals(getInstance().getId())) {
                    System.out.println("entro al if... req");
                    listaReq = kardex.getListaRequisicion();
                    listaReq2 = listaReq;
                }
            }
        }
        System.out.println("lista de req desp for>>" + listaReq);
        listareqNoAp = findAll(Requisicion.class);
        List<Requisicion> lr = servgen.buscarTodos(Requisicion.class);
        listareqNoAp.clear();
        for (Requisicion rnp : lr) {
            if (rnp.getVehiculo() != null && getInstance().getVehiculo() != null) {

                System.out.println("vehiculo en kardex>>>>>" + getInstance().getVehiculo().getId());
                System.out.println("vehiculo requisicion>>>>>>" + rnp.getVehiculo().getId());
                System.out.println("estado requisiscion>>>>>>" + rnp.isEstado());
                if (rnp.getAprobado() == false && rnp.isEstado() && rnp.getVehiculo().getId().equals(getInstance().getVehiculo().getId())) {
                    System.out.println("\n\n\n\n\n\nagregooooo\n\n\n\n\n\n" + rnp);
                    listareqNoAp.add(rnp);
                    listareqNoAp2=listareqNoAp;
                }
            }
        }
        System.out.println("lsita de requisiciones>>>>>>>" + listareqNoAp);
        listasolNoAp = findAll(SolicitudReparacionMantenimiento.class);
        List<SolicitudReparacionMantenimiento> lsol = servgen.buscarTodos(SolicitudReparacionMantenimiento.class);
        listasolNoAp.clear();
        for (SolicitudReparacionMantenimiento srna : lsol) {
            if (srna.getVehiculo() != null && getInstance().getVehiculo() != null) {

                System.out.println("vehiculo en kardex>>>>>" + getInstance().getVehiculo().getId());
                System.out.println("vehiculo sol>>>>>>" + srna.getVehiculo().getId());
                System.out.println("estado sol>>>>>>" + srna.isEstado());
                if (srna.getAprobado() == false && srna.isEstado() && srna.getVehiculo().getId().equals(getInstance().getVehiculo().getId())) {
                    System.out.println("\n\n\n\n\n\nagregooooo\n\n\n\n\n\n" + srna);
                    listasolNoAp.add(srna);
                    listasolNoAp2=listasolNoAp;
                }
            }
        }
        System.out.println("lsita de requisiciones>>>>>>>" + listasolNoAp);

    }

    public void guardarSolicitud() {
        Date now = Calendar.getInstance().getTime();
        System.out.println("fijando SOlidituuzzxxzzuuud en guardar");

        if (solicitud != null) {

            try {

                System.out.println("recupero solicitud " + solicitud);
                solicitud.setKardex(getInstance());
                solicitud.setLastUpdate(now);
                solicitud.setAprobado(true);
                solicitud.setKardex(getInstance());
                save(solicitud);
                System.out.println("guando sol coon con kardex cooon" + solicitud.getKardex());

                getInstance().setLastUpdate(now);
                getInstance().getListaSolicitudReparacion().add(solicitud);
                servgen.actualizar(getInstance());
                System.out.println("guardo kardex con solicitudes" + getInstance().getListaSolicitudReparacion());
                listasolNoAp.remove(solicitud);
                System.out.println("lista no a sol" + listasolNoAp);
            } catch (Exception e) {
                System.out.println("errrrorrrrrrrr");
                e.printStackTrace();
            }
        } else {
            System.out.println("presenta mensaje");
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "No puede guardar la solicitud porque no se ", "Aprobado ninguna... ");
            FacesContext.getCurrentInstance().addMessage("", msg);
            //solicitud.setNumSolicitud("");
        }

        System.out.println("fijanddsasadasdadassssssssssss");

    }

    public void guardarRequisicion() {
        Date now = Calendar.getInstance().getTime();
        System.out.println("fijando requisicion en guardar");

        if (requisicion != null) {

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
                listareqNoAp.remove(requisicion);
                // listaReq.add(requisicion);
            } catch (Exception e) {
                System.out.println("erooooooooooorrrrrrrrr");
                e.printStackTrace();
            }
        } else {
            System.out.println("entro al presentar mensaje");
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "No puede guardar la requisición porque no se ", "Aprobado ninguna... ");
            FacesContext.getCurrentInstance().addMessage("", msg);

        }

        System.out.println("fijanddsasadasdadassssssssssss");

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
        listakardex2 = listakardex;
        //solicitud = new SolicitudReparacionMantenimiento();
        listasolNoAp = findAll(SolicitudReparacionMantenimiento.class);
        listareqNoAp = findAll(Requisicion.class);

    }

    @Override
    protected Kardex createInstance() {
        //prellenado estable para cualquier clase 
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(Kardex.class
                .getName());
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
