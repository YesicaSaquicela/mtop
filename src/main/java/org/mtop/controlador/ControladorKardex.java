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
import org.mtop.modelo.dinamico.BussinesEntityAttribute;

import org.mtop.servicios.ServicioGenerico;
import org.primefaces.context.RequestContext;

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

    private List<Requisicion> listareqNoAp;
    private List<SolicitudReparacionMantenimiento> listasolNoAp;
    private List<Requisicion> listareqNoAp2;
    private String palabrasna;
    private List<SolicitudReparacionMantenimiento> listasolNoAp2;
    private String estado;
    private Integer cont = 0;

    public Integer getCont() {
        return cont;
    }

    public void setCont(Integer cont) {
        this.cont = cont;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

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

        return solicitud;
    }

    public void setSolicitud(SolicitudReparacionMantenimiento solicitud) {
        this.solicitud = solicitud;

    }

    public String getPalabrarna() {

        return palabrarna;
    }

    public void setPalabrarna(String palabrarna) {
        this.palabrarna = palabrarna;

    }

    public List<Requisicion> getListareqNoAp() {
        return listareqNoAp;
    }

    public void setListareqNoAp(List<Requisicion> listareqNoAp) {
        this.listareqNoAp = listareqNoAp;
    }

    public void darDeBajaS(Long idSol) {

        List<SolicitudReparacionMantenimiento> lsm = new ArrayList<SolicitudReparacionMantenimiento>();
        Date now = Calendar.getInstance().getTime();
        SolicitudReparacionMantenimiento sr = servgen.buscarPorId(SolicitudReparacionMantenimiento.class, idSol);
        for (SolicitudReparacionMantenimiento sol : listaSol) {
            if (!sol.getId().equals(sr.getId())) {
                lsm.add(sol);

            }
        }
        listaSol = lsm;
        getInstance().setLastUpdate(now);
        getInstance().setListaSolicitudReparacion(listaSol);
        save(getInstance());
        sr.setLastUpdate(now);
        sr.setAprobado(false);
        sr.setKardex(null);
        save(sr);
        System.out.println("lista de requ no el" + listaSol);
        listasolNoAp.add(sr);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "La solicitud seleccionada " + sr.getNumSolicitud() + " ha pasado al estado  ", "NO APROBADA"));

    }

    @Transactional
    public String darDeBajaR(Long idReq) {

        return "/admin/kardex/crear.xhtml&valorVista=kardex&kardexId=" + getInstance().getId() + "&requisicionId=" + String.valueOf(idReq);

    }

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

    }

    public void irARequisicion() {
        this.vista = "requisicion";

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

        ArrayList<String> ced = new ArrayList<String>();
        ControladorVehiculo cv = new ControladorVehiculo();
        cv.setBussinesEntity(bussinesEntity);
        cv.setEntityManager(em);
        List<BussinesEntityAttribute> bea = new ArrayList<BussinesEntityAttribute>();

        for (Kardex kar : listakardex2) {
            if (kar.getNumero().contains(query)) {
                ced.add(kar.getNumero());
            }
              cv.setId(kar.getVehiculo().getId());

            bea = cv.getInstance().findBussinesEntityAttribute("Motor");
            for (BussinesEntityAttribute bussinesEntityAttribute : bea) {

                if (bussinesEntityAttribute.getName().equals("serieMotor")) {
                    System.out.println("!lk.contains(kd)"+!ced.contains((String) bussinesEntityAttribute.getValue()));
                    System.out.println("((String) bussinesEntityAttribute.getValue()).equals(palabrab)"+((String) bussinesEntityAttribute.getValue()).equals(palabrab));
                    if (((String) bussinesEntityAttribute.getValue()).toLowerCase().contains(query.toLowerCase()) 
                            && !ced.contains((String) bussinesEntityAttribute.getValue())) {

                        ced.add((String) bussinesEntityAttribute.getValue());

                    }

                }
            }
            bea = cv.getInstance().findBussinesEntityAttribute("Chasis");
            for (BussinesEntityAttribute bussinesEntityAttribute : bea) {
                if (bussinesEntityAttribute.getName().equals("serieChasis")) {
                    System.out.println("get value"+(String) bussinesEntityAttribute.getValue());
                    System.out.println("!lk.contains(kd)"+!ced.contains((String) bussinesEntityAttribute.getValue()));
                    System.out.println("((String) bussinesEntityAttribute.getValue()).equals(palabrab)"+((String) bussinesEntityAttribute.getValue()).equals(palabrab));
                    if (((String) bussinesEntityAttribute.getValue()).toLowerCase().contains(query.toLowerCase()) 
                            && !ced.contains((String) bussinesEntityAttribute.getValue())) {

                        ced.add((String) bussinesEntityAttribute.getValue());

                    }

                }
            }

        }
        System.out.println("listaaaaa autocompletar" + ced);
        return ced;
    }

    public ArrayList<String> autocompletarr(String query) {

        ArrayList<String> ced = new ArrayList<String>();
        String s;
        for (Requisicion requisicion : listaReq2) {
            if (requisicion.getNumRequisicion().contains(query)) {

                ced.add(requisicion.getNumRequisicion());
            }
            s = requisicion.getFechaRequisicion().toString();

            if (s.contains(String.class.cast(query)) && !ced.contains(s)) {

                ced.add(s);
            }

        }

        System.out.println("\n\nlistaaaaa autocompletar" + ced);
        return ced;

    }

    public ArrayList<String> autocompletarrna(String query) {

        ArrayList<String> ced = new ArrayList<String>();
        String s;
        for (Requisicion requisicion : listareqNoAp2) {
            if (requisicion.isEstado()) {
                if (requisicion.getNumRequisicion().contains(query)) {
                    ced.add(requisicion.getNumRequisicion());
                }
                s = requisicion.getFechaRequisicion().toString();

                if (s.contains(String.class.cast(query)) && !ced.contains(s)) {
                    ced.add(s);
                }
            }

        }
        System.out.println("pala en autocompletar" + palabrarna);
        return ced;

    }

    public ArrayList<String> autocompletars(String query) {

        ArrayList<String> ced = new ArrayList<String>();

        String s;
        for (SolicitudReparacionMantenimiento soli : listaSol2) {
            //buscar por nuemro
            if (soli.getNumSolicitud().contains(query)) {
                ced.add(soli.getNumSolicitud());

            }
            //buscar por fechas
            s = soli.getFechaSolicitud().toString();
            if (s.contains(String.class.cast(query)) && !ced.contains(s)) {
                ced.add(s);
            }

        }

        return ced;

    }

    public ArrayList<String> autocompletarsna(String query) {
        System.out.println("QUEryyyyy" + query);

        ArrayList<String> ced = new ArrayList<String>();

        String s;
        for (SolicitudReparacionMantenimiento soli : listasolNoAp2) {
            //buscar por nuemro
            if (soli.isEstado()) {
                if (soli.getNumSolicitud().contains(query)) {
                    ced.add(soli.getNumSolicitud());

                }
                //buscar por fechas
                s = soli.getFechaSolicitud().toString();
                if (s.contains(String.class.cast(query)) && !ced.contains(s)) {

                    ced.add(s);
                }

            }

        }

        return ced;

    }

    public void buscar() {

        List<Kardex> le = new ArrayList<Kardex>();
        le.clear();
        palabrab = palabrab.trim();
        ControladorVehiculo cv = new ControladorVehiculo();
        cv.setBussinesEntity(bussinesEntity);
        cv.setEntityManager(em);
        List<BussinesEntityAttribute> bea = new ArrayList<BussinesEntityAttribute>();

        if (palabrab == null || palabrab.equals("")) {
            palabrab = "Ingrese algun valor a buscar";
        }

        List<Kardex> lk = new ArrayList<Kardex>();
        for (Kardex kd : listakardex2) {
            if (kd.getNumero().toLowerCase().contains(palabrab.toLowerCase())) {
                lk.add(kd);
            }
            cv.setId(kd.getVehiculo().getId());

            bea = cv.getInstance().findBussinesEntityAttribute("Motor");
            for (BussinesEntityAttribute bussinesEntityAttribute : bea) {

                if (bussinesEntityAttribute.getName().equals("serieMotor")) {
                    if (((String) bussinesEntityAttribute.getValue()).toLowerCase().equals(palabrab.toLowerCase()) && !lk.contains(kd)) {

                        lk.add(kd);

                    }

                }
            }
            bea = cv.getInstance().findBussinesEntityAttribute("Chasis");
            for (BussinesEntityAttribute bussinesEntityAttribute : bea) {
                if (bussinesEntityAttribute.getName().equals("serieChasis")) {
                    if (((String) bussinesEntityAttribute.getValue()).toLowerCase().equals(palabrab.toLowerCase()) && !lk.contains(kd)) {

                        lk.add(kd);

                    }

                }
            }

        }

        if (lk.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrab.equals("Ingrese algun valor a buscar")) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "Ingrese algun valor a buscar ");
                FacesContext.getCurrentInstance().addMessage("", msg);

                palabrab = " ";
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "No se ha encontrado " + palabrab);
                FacesContext.getCurrentInstance().addMessage("", msg);
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
        //buscando por coincidencia
        String s;
        List<SolicitudReparacionMantenimiento> lsoli = new ArrayList<SolicitudReparacionMantenimiento>();
        for (SolicitudReparacionMantenimiento srm : listaSol2) {
            if (srm.getNumSolicitud().contains(palabrabs)) {
                lsoli.add(srm);
            } else {
                s = srm.getFechaSolicitud().toString();
                if (s.contains(palabrabs)) {
                    lsoli.add(srm);
                }
            }

        }

        if (lsoli.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (palabrabs.equals("Ingrese algun valor a buscar")) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "Ingrese algun valor a buscar ");
                FacesContext.getCurrentInstance().addMessage("", msg);
                palabrabs = " ";
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "No se ha encontrado " + palabrabs);
                FacesContext.getCurrentInstance().addMessage("", msg);

            }

        } else {
            listaSol = lsoli;
            palabrabs = "";
        }

    }

    public void buscarrna() {
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
                if (s.contains(palabrarna)) {
                    lrq.add(r);
                }
            }

        }

        if (lrq.isEmpty()) {

            if (palabrarna.equals("Ingrese algun valor a buscar")) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "Ingrese algun valor a buscar ");
                FacesContext.getCurrentInstance().addMessage("", msg);
                palabrarna = " ";
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "No se ha encontrado " + palabrarna);
                FacesContext.getCurrentInstance().addMessage("", msg);
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
        //buscando por coincidencia
        String s;
        List<SolicitudReparacionMantenimiento> lsoli = new ArrayList<SolicitudReparacionMantenimiento>();
        for (SolicitudReparacionMantenimiento srm : listasolNoAp) {
            if (srm.getNumSolicitud().contains(palabrasna)) {
                lsoli.add(srm);
            } else {
                s = srm.getFechaSolicitud().toString();
                if (s.contains(palabrasna)) {

                    lsoli.add(srm);
                }
            }

        }

        if (lsoli.isEmpty()) {

            if (palabrasna.equals("Ingrese algun valor a buscar")) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "Ingrese algun valor a buscar ");
                FacesContext.getCurrentInstance().addMessage("", msg);
                palabrasna = " ";
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "No se ha encontrado " + palabrasna);
                FacesContext.getCurrentInstance().addMessage("", msg);

            }

        } else {
            listasolNoAp = lsoli;
            palabrasna = "";
        }

    }

    public void buscarr() {
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
                if (s.contains(palabrabr)) {
                    lrq.add(r);
                }
            }

        }

        if (lrq.isEmpty()) {

            if (palabrabr.equals("Ingrese algun valor a buscar")) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "Ingrese algun valor a buscar ");
                FacesContext.getCurrentInstance().addMessage("", msg);
                palabrabr = " ";
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACIÓN: ", "No se ha encontrado " + palabrabr);
                FacesContext.getCurrentInstance().addMessage("", msg);

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
        listareqNoAp = listareqNoAp2;
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

        setId(kardexId);

        if (listaSol == null) {
            for (Kardex kardex : listakardex) {
                if (kardex.getId().equals(getInstance().getId())) {

                    listaSol = kardex.getListaSolicitudReparacion();
                    listaSol2 = listaSol;

                }
            }
        }
        if (listaReq == null) {
            for (Kardex kardex : listakardex) {
                if (kardex.getId().equals(getInstance().getId())) {
                    listaReq = kardex.getListaRequisicion();
                    listaReq2 = listaReq;
                }
            }
        }
        listareqNoAp = findAll(Requisicion.class);
        List<Requisicion> lr = findAll(Requisicion.class);
        listareqNoAp.clear();
        for (Requisicion rnp : lr) {
            if (rnp.getVehiculo() != null && getInstance().getVehiculo() != null) {

                if (rnp.getAprobado() == false && rnp.isEstado() && rnp.getVehiculo().getId().equals(getInstance().getVehiculo().getId())) {
                    listareqNoAp.add(rnp);
                    listareqNoAp2 = listareqNoAp;
                }
            }
        }

        listasolNoAp = findAll(SolicitudReparacionMantenimiento.class);
        List<SolicitudReparacionMantenimiento> lsol = servgen.buscarTodos(SolicitudReparacionMantenimiento.class);
        listasolNoAp.clear();
        for (SolicitudReparacionMantenimiento srna : lsol) {
            if (srna.getVehiculo() != null && getInstance().getVehiculo() != null) {

                if (srna.getAprobado() == false && srna.isEstado() && srna.getVehiculo().getId().equals(getInstance().getVehiculo().getId())) {
                    listasolNoAp.add(srna);
                    listasolNoAp2 = listasolNoAp;
                }
            }
        }

    }

    public void guardarSolicitud() {
        Date now = Calendar.getInstance().getTime();

        if (solicitud != null) {

            try {

                System.out.println("recupero solicitud " + solicitud);
                solicitud.setKardex(getInstance());
                solicitud.setLastUpdate(now);
                solicitud.setAprobado(true);
                solicitud.setKardex(getInstance());
                save(solicitud);

                getInstance().setLastUpdate(now);
                getInstance().getListaSolicitudReparacion().add(solicitud);
                servgen.actualizar(getInstance());
                listasolNoAp.remove(solicitud);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

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
                listareqNoAp.remove(requisicion);
                requisicion.setKardex(getInstance());
                requisicion.setLastUpdate(now);
                requisicion.setAprobado(true);
                requisicion.setKardex(getInstance());
                save(requisicion);
                getInstance().setLastUpdate(now);
                getInstance().getListaRequisicion().add(requisicion);
                servgen.actualizar(getInstance());
                listaReq.add(requisicion);
                requisicion = null;
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
        listasolNoAp2 = findAll(SolicitudReparacionMantenimiento.class);
        listareqNoAp2 = findAll(Requisicion.class);

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

        this.instance.setVehiculo(getInstance().getVehiculo());
        try {
            if (getInstance().isPersistent()) {
                save(getInstance());

                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizó Kardex" + getInstance().getId() + " con éxito", " ");
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

    public Long getSolicitudId() {
        if (solicitud != null) {
            return solicitud.getId();
        } else {
            SolicitudReparacionMantenimiento s = new SolicitudReparacionMantenimiento();
            return s.getId();
        }

    }

    public void setSolicitudId(Long id) {
        List<SolicitudReparacionMantenimiento> lre = new ArrayList<SolicitudReparacionMantenimiento>();
        Date now = Calendar.getInstance().getTime();

        System.out.println("\n\n\n\n\n\n\n\nfijando SOlidituuuuud en guardar\n\n\n\n");
        SolicitudReparacionMantenimiento sl = servgen.buscarPorId(SolicitudReparacionMantenimiento.class, id);

        if (estado.equals("false")) {
            for (SolicitudReparacionMantenimiento solicit : listaSol) {
                System.out.println("entro al for>>>");
                if (!solicit.getId().equals(sl.getId())) {
                    System.out.println("entro al if ad");
                    lre.add(solicit);

                }
            }
            listaSol = lre;
            sl.setLastUpdate(now);
            sl.setAprobado(false);
          
            sl.setKardex(null);
            listasolNoAp.add(sl);

        } else {
            for (SolicitudReparacionMantenimiento solicit : listasolNoAp) {
               
                if (!solicit.getId().equals(sl.getId())) {
                   
                    lre.add(solicit);

                }
            }
            listasolNoAp = lre;
            sl.setLastUpdate(now);
            sl.setKardex(getInstance());

            sl.setAprobado(true);
            sl.setKardex(getInstance());

            listaSol.add(sl);

        }

        save(sl);

        getInstance().setLastUpdate(now);
        getInstance().setListaSolicitudReparacion(listaSol);
        save(getInstance());
        System.out.println("lista de sol no el" + listaSol);
        listakardex = findAll(Kardex.class);
        listaSol2 = listaSol;
        listasolNoAp2 = listasolNoAp;
    }

    public String getVista() {

        return vista;
    }

    public void setVista(String vista) {
        this.vista = vista;
    }

    public Long getRequisicionId() {

        if (requisicion != null) {
            return requisicion.getId();
        } else {
            Requisicion requis = new Requisicion();
            return requis.getId();
        }

    }

    public void setRequisicionId(Long id) {
        List<Requisicion> lre = new ArrayList<Requisicion>();
        Date now = Calendar.getInstance().getTime();
        Requisicion rq = servgen.buscarPorId(Requisicion.class, id);

        if (estado.equals("false")) {
            for (Requisicion requisicio : listaReq) {
                if (!requisicio.getId().equals(rq.getId())) {
                    lre.add(requisicio);

                }
            }
            listaReq = lre;

            rq.setLastUpdate(now);
            rq.setAprobado(false);
            rq.setKardex(null);
            listareqNoAp.add(rq);

        } else {
            for (Requisicion requisicio : listareqNoAp) {
                if (!requisicio.getId().equals(rq.getId())) {
                    lre.add(requisicio);

                }
            }
            listareqNoAp = lre;
            rq.setLastUpdate(now);
            rq.setKardex(getInstance());

            rq.setAprobado(true);
            rq.setKardex(getInstance());

            listaReq.add(rq);

        }

        save(rq);

        getInstance().setLastUpdate(now);
        getInstance().setListaRequisicion(listaReq);
        save(getInstance());
        System.out.println("lista de requ no el" + listaReq);
        listakardex = findAll(Kardex.class);
        listaReq2 = listaReq;
        listareqNoAp2 = listareqNoAp;

    }

    public void fijarRequi(Requisicion req) {
        this.requisicion = req;

        //        return "dlg1.show();";
    }

    public void viewCars() {
        RequestContext.getCurrentInstance().openDialog("viewCars");
    }

    public void chooseCar() {
        RequestContext.getCurrentInstance().openDialog("selectCar");
    }

    public void fijarSoli(SolicitudReparacionMantenimiento sol) {
        System.out.println("fijando en fijar" + sol);
        this.solicitud = sol;
        // return "deletedDlg1.show();";
    }

}
