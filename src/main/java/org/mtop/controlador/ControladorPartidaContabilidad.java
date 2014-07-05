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
import org.mtop.modelo.dinamico.BussinesEntityType;
import org.mtop.modelo.PartidaContabilidad;
import org.mtop.servicios.ServicioGenerico;

/**
 *
 * @author carlis
 */
@Named
@ViewScoped
public class ControladorPartidaContabilidad extends BussinesEntityHome<PartidaContabilidad> implements Serializable {

    @Inject
    @Web
    private EntityManager em;
    @Inject
    private ServicioGenerico servgen;
    private List<PartidaContabilidad> listaPartidaC = new ArrayList<PartidaContabilidad>();
    private String mensaje = "";

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String inactivarPartida(Long idpartidac) {

        for (PartidaContabilidad partidaC : findAll(PartidaContabilidad.class)) {

            setInstance(partidaC);

            getInstance().setEstado(false);
            guardarCambioPlan();

        }

        setId(idpartidac);
        System.out.println("plaaan" + getInstance());
        getInstance().setEstado(true);

        guardarCambioPlan();
        addMessage("Información", "El Plan de Mantenimiento seleccionado ha sido activado");
        ControladorVehiculo cv = new ControladorVehiculo();

        return "/paginas/partidaContabilidad/lista.xhtml?faces-redirect=true";
    }

    public void guardarCambioPlan() {
        try {

            save(getInstance());

        } catch (Exception e) {
            System.out.println("no activo plannnn");
        }
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Long getPartidaCId() {
        return (Long) getId();

    }

    public void setPartidaCId(Long partidaCId) {
        setId(partidaCId);
    }

    @TransactionAttribute   //
    public PartidaContabilidad load() {
        if (isIdDefined()) {
            wire();
        }
        return getInstance();
    }

    @TransactionAttribute
    public void wire() {
        getInstance();

    }

    public List<PartidaContabilidad> getListaPartidaC() {
        return listaPartidaC;
    }

    public void setListaPartidaC(List<PartidaContabilidad> listaPartidaC) {
        this.listaPartidaC = listaPartidaC;
    }

    @PostConstruct
    public void init() {
        setEntityManager(em);
        /*el bussinesEntityService.setEntityManager(em) solo va si la Entidad en este caso (ConsultaMedia)
         *hereda de la Entidad BussinesEntity...  caso contrario no se lo agrega
         */

        bussinesEntityService.setEntityManager(em);
        servgen.setEm(em);
        listaPartidaC = servgen.buscarTodos(PartidaContabilidad.class);
    }

    @Override
    protected PartidaContabilidad createInstance() {
        //prellenado estable para cualquier clase 
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(PartidaContabilidad.class.getName());
        Date now = Calendar.getInstance().getTime();
        PartidaContabilidad partidaC = new PartidaContabilidad();
        partidaC.setCreatedOn(now);
        partidaC.setLastUpdate(now);
        partidaC.setActivationTime(now);

        //fichaMedic.setResponsable(null);    //cambiar atributo a 
        partidaC.setType(_type);
        partidaC.buildAttributes(bussinesEntityService);  //
        return partidaC;
    }

    @Override
    public Class<PartidaContabilidad> getEntityClass() {
        return PartidaContabilidad.class;
    }

    public boolean verificarPartida() {
        boolean ban = true;
        for (PartidaContabilidad partidaContabilidad : listaPartidaC) {
            if (partidaContabilidad.concatenarPartida().equals(getInstance().concatenarPartida())) {
                ban = false;
                break;
            }
        }
        return ban;

    }

    @TransactionAttribute
    public String guardar() {
        if (verificarPartida() == true) {
            Date now = Calendar.getInstance().getTime();
            getInstance().setLastUpdate(now);

            try {

                if (getInstance().isPersistent()) {
                    System.out.println("Entro a Editar>>>>>>>>");
                    save(getInstance());
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizo Partida contabilidad" + getInstance().getId() + " con éxito", " ");
                    FacesContext.getCurrentInstance().addMessage("", msg);
                } else {
                    System.out.println("Entro a crear>>>>>>>>");
                    getInstance().setEstado(false);
                    create(getInstance());
                    save(getInstance());
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se creo una nueva Partida contabilidad" + getInstance().getId() + " con éxito", " ");
                    FacesContext.getCurrentInstance().addMessage("", msg);
                }
            } catch (Exception e) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            }

            return "/paginas/partidaContabilidad/lista.xhtml?faces-redirect=true";

        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "La partida de contabilidad", "ya exite ");
            FacesContext.getCurrentInstance().addMessage("", msg);
            mensaje = "La partida de contabilidad ya exite";

            return "";
        }

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
        return "/paginas/partidaContabilidad/lista.xhtml?faces-redirect=true";
    }

}
