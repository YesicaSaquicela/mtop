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
import org.mtop.controller.BussinesEntityHome;
import org.mtop.model.BussinesEntityType;
import org.mtop.modelo.SolicitudReparacionMantenimiento;
import static org.mtop.modelo.SolicitudReparacionMantenimiento_.vehiculo;
import org.mtop.modelo.Vehiculo;
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
    @Inject
    private ServicioGenerico servgen;
    private List<SolicitudReparacionMantenimiento> listaSolicitud = new ArrayList<SolicitudReparacionMantenimiento>();
    private Long idVehiculo;
    private Vehiculo vehiculo;
    private List<Vehiculo> listaVehiculos;

    private boolean skip;
    
    private String numeroSolicitud;

    public String getNumeroSolicitud() {
        if (getId() == null) {
            System.out.println("numero"+getInstance().getNumSolicitud());
            List<SolicitudReparacionMantenimiento> lista = findAll(SolicitudReparacionMantenimiento.class);
            int t = lista.size();
            System.out.println("valor de t :::::::::::"+t);
            if (t < 9) {
                setNumeroSolicitud("000".concat(String.valueOf(t + 1)));
            } else {
                if (t >= 9 && t < 99) {
                    setNumeroSolicitud("00".concat(String.valueOf(t + 1)));
                } else {
                    if (t >= 99 && t < 999) {
                        setNumeroSolicitud("0".concat(String.valueOf(t + 1)));
                    } else {
                        setNumeroSolicitud(String.valueOf(t + 1));
                    }
                }
            }
        }else{
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

                return event.getNewStep();
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
        listaVehiculos = findAll(Vehiculo.class);
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

    public void setListaSolicitud(List<SolicitudReparacionMantenimiento> listaSolicitud) {
        this.listaSolicitud = listaSolicitud;
    }

    @PostConstruct
    public void init() {
        setEntityManager(em);
        /*el bussinesEntityService.setEntityManager(em) solo va si la Entidad en este caso (ConsultaMedia)
         *hereda de la Entidad BussinesEntity...  caso contrario no se lo agrega
         */

        bussinesEntityService.setEntityManager(em);
        servgen.setEm(em);
        listaSolicitud = servgen.buscarTodos(SolicitudReparacionMantenimiento.class);
        vehiculo = new Vehiculo();
        idVehiculo = 0l;
    }

    @Override
    protected SolicitudReparacionMantenimiento createInstance() {
        //prellenado estable para cualquier clase 
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(SolicitudReparacionMantenimiento.class.getName());
        Date now = Calendar.getInstance().getTime();
        SolicitudReparacionMantenimiento solicitudRepMant = new SolicitudReparacionMantenimiento();
        solicitudRepMant.setCreatedOn(now);
        solicitudRepMant.setLastUpdate(now);
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

        getInstance().setVehiculo(vehiculo);
        System.out.println("IIIIDEEEntro>>>>>>" + getSolicitudReparacionMantenimientoId());
        System.out.println("IIIIDEPERSISTEN  >>>>>>" + getInstance().isPersistent());

        try {
            if (getInstance().isPersistent()) {
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizo Solicitud de Reparacion y Mantenimiento" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            } else {
                getInstance().setEstado(true);
                create(getInstance());
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se creo una nueva Solicitud de Reparacion y Mantenimiento" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
        }
        return "/paginas/solicitud/lista.xhtml?faces-redirect=true";
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
        return "/paginas/solicitud/lista.xhtml?faces-redirect=true";
    }

}
