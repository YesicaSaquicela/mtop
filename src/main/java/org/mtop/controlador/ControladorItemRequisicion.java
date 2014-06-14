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
import org.mtop.model.Property;

import org.mtop.modelo.ItemRequisicion;

import org.mtop.servicios.ServicioGenerico;

/**
 *
 * @author carlis
 */
@Named
@ViewScoped
public class ControladorItemRequisicion extends BussinesEntityHome<ItemRequisicion> implements Serializable {

    @Inject
    @Web
    private EntityManager em;
    @Inject
    private ServicioGenerico servgen;
    List<ItemRequisicion> listaItemsRequisicion = new ArrayList<ItemRequisicion>();

    public Long getItemRequisicionId() {
        System.out.println("IIIIDEE" + getId());
        return (Long) getId();
    }

    public void setItemRequisicionId(Long itemRequisicionId) {

        setId(itemRequisicionId);

    }

    @TransactionAttribute   //
    public ItemRequisicion load() {
        if (isIdDefined()) {
            wire();
        }

        return getInstance();
    }

    @TransactionAttribute
    public void wire() {
        getInstance();
    }

    public List<ItemRequisicion> getListaItemRequisicion() {
        return listaItemsRequisicion;
    }

    public void setListaItemsRequisicion(List<ItemRequisicion> listaItemRequisicion) {
        this.listaItemsRequisicion = listaItemRequisicion;
    }

    @PostConstruct
    public void init() {
        setEntityManager(em);
        /*el bussinesEntityService.setEntityManager(em) solo va si la Entidad en este caso (Vehiculo)
         *hereda de la Entidad BussinesEntity...  caso contrario no se lo agrega
         */
        bussinesEntityService.setEntityManager(em);
        servgen.setEm(em);
        listaItemsRequisicion = servgen.buscarTodos(ItemRequisicion.class);
    }

    @Override
    protected ItemRequisicion createInstance() {
        //prellenado estable para cualquier clase 
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(ItemRequisicion.class.getName());
        Date now = Calendar.getInstance().getTime();
        ItemRequisicion itemRequisicion = new ItemRequisicion();
        itemRequisicion.setCreatedOn(now);
        itemRequisicion.setLastUpdate(now);
        itemRequisicion.setActivationTime(now);
        itemRequisicion.setType(_type);
        itemRequisicion.buildAttributes(bussinesEntityService);  //
        return itemRequisicion;
    }

    @Override
    public Class<ItemRequisicion> getEntityClass() {
        return ItemRequisicion.class;
    }

    @TransactionAttribute
    public String guardar() {

        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);

        System.out.println("PRESENTAR persisten>>>>>" + getInstance().isPersistent());
        try {
            if (getInstance().isPersistent()) {
                System.out.println("ENTRO A ACTUALIZAR ItemRequisicion>>>>>" );
                System.out.println("ENTRO A ACTUALIZAR KILOMETRAJE>>>>>" );
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizo Item deRequisicion" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            } else {
                System.out.println("ENTRO A crear ItemRequisicion>>>>>" );
//                System.out.println("ENTRO A CREAR KILOMETRAJE>>>>>"+getInstance().getKilometraje()); 
                getInstance().setEstado(true);
                create(getInstance());
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se creo una nueva Item de Requisicion" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
        }
        return "/paginas//lista.xhtml?faces-redirect=true";
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
        return "/paginas//lista.xhtml?faces-redirect=true";
    }

    public boolean tieneEstadosEstructura(Property propiedad) {
        for (Property p : servgen.buscarTodos(Property.class)) {
            if (p.getGroupName() != null) {
                if (p.getGroupName().equals(propiedad.getName())) {
                    System.out.println("encontro su propiedad>>>>> " + p.getName());
                    if (p.getType().equals("org.mtop.model.EstadoParteMecanica")) {
                        System.out.println("retornara true");
                        return true;
                    }
                }
            }

        }
        System.out.println("retornara false");
        return false;
    }

}
