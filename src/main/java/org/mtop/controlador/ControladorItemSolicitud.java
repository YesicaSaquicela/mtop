/*
    SOFTWARE PARA LA GESTIÓN DE INFORMACIÓN DEL ESTADO  MECÁNICO DE LOS 
    VEHÍCULOS DEL MINISTERIO DE TRANSPORTE Y OBRAS PÚBLICAS
    Copyright (C) 2014  Romero Carla, Saquicela Yesica

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.mtop.controlador;

import java.io.Serializable;
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
import org.mtop.modelo.ItemSolicitudReparacion;
import org.mtop.modelo.dinamico.BussinesEntityType;
import org.mtop.servicios.ServicioGenerico;

/**
 *
 * @author carla
 * @author yesica
 * 
 */
@Named
@ViewScoped
public class ControladorItemSolicitud extends BussinesEntityHome<ItemSolicitudReparacion> implements Serializable {

    @Inject
    @Web
    private EntityManager em;
    @Inject
    private ServicioGenerico servgen;
    List<ItemSolicitudReparacion> listaItemsSolicitud = new ArrayList<ItemSolicitudReparacion>();

    public Long getItemSolicitudRMId() {
        System.out.println("IIIIDEE" + getId());
        return (Long) getId();
    }

    public void setItemSolicitudRMId(Long itemSolicitudRMId) {

        setId(itemSolicitudRMId);
        
    }

    @TransactionAttribute   //
    public ItemSolicitudReparacion load() {
        if (isIdDefined()) {
            wire();
        }

        return getInstance();
    }

    @TransactionAttribute
    public void wire() {
        getInstance();
    }

    public List<ItemSolicitudReparacion> getListaItemsSolicitud() {
        return listaItemsSolicitud;
    }

    public void setListaItemsSolicitud(List<ItemSolicitudReparacion> listaItemsSolicitud) {
        this.listaItemsSolicitud = listaItemsSolicitud;
    }

    @PostConstruct
    public void init() {
        setEntityManager(em);
        /*el bussinesEntityService.setEntityManager(em) solo va si la Entidad en este caso (Vehiculo)
         *hereda de la Entidad BussinesEntity...  caso contrario no se lo agrega
         */
        bussinesEntityService.setEntityManager(em);
        servgen.setEm(em);
        listaItemsSolicitud = findAll(ItemSolicitudReparacion.class);

    }

    @Override
    protected ItemSolicitudReparacion createInstance() {
        //prellenado estable para cualquier clase 
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(ItemSolicitudReparacion.class.getName());
        Date now = Calendar.getInstance().getTime();
        ItemSolicitudReparacion itemSolicitudRm = new ItemSolicitudReparacion();
        itemSolicitudRm.setCreatedOn(now);
        itemSolicitudRm.setLastUpdate(now);
        itemSolicitudRm.setActivationTime(now);
        itemSolicitudRm.setType(_type);
        itemSolicitudRm.buildAttributes( bussinesEntityService);  //
        return itemSolicitudRm;
    }

    @Override
    public Class<ItemSolicitudReparacion> getEntityClass() {
        return ItemSolicitudReparacion.class;
    }

    @TransactionAttribute
    public String guardar() {

        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);

        System.out.println("cItem...PRESENTAR persisten>>>>>" + getInstance().isPersistent());
        try {
            if (getInstance().isPersistent()) {
                System.out.println("Entro a guradar>>>>>>>"+getInstance().isPersistent());
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizó Item de Solicitud" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            } else {
                System.out.println("ENTRO A crear Item>>>>>");
//                System.out.println("ENTRO A CREAR KILOMETRAJE>>>>>"+getInstance().getKilometraje()); 
                getInstance().setEstado(true);
                create(getInstance());
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se creoó una nueva Item de Solicitud" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
            System.out.println("Error al crear item");
        }
        return "/paginas/secretario/lista.xhtml?faces-redirect=true";
    }

    public void guardarItemSol(List<ItemSolicitudReparacion> ls) {
        Date now = Calendar.getInstance().getTime();
        for (ItemSolicitudReparacion itemSolicitudReparacion : ls) {
            setInstance(itemSolicitudReparacion);
            getInstance().setEstado(true);
            getInstance().setCreatedOn(now);
            getInstance().setLastUpdate(now);
            getInstance().setActivationTime(now);
            System.out.println("antes crear item");
            try {
                create(getInstance());
                System.out.println("antes guardar item");
                save(getInstance());
            } catch (Exception e) {
                System.out.println("error creando item");
            }

        }

    }

    public void editarItemSol(List<ItemSolicitudReparacion> ls) {
        Date now = Calendar.getInstance().getTime();
        for (ItemSolicitudReparacion itemSolicitudReparacion : ls) {
            setInstance(itemSolicitudReparacion);
            getInstance().setLastUpdate(now);
            save(getInstance());
        }
    }

    @Transactional
    public String borrarEntidad() {
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
        return "/paginas/secretario/lista.xhtml?faces-redirect=true";
    }

}
