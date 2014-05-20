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
import org.mtop.modelo.Producto;
import org.mtop.servicios.ServicioGenerico;


/**
 *
 * @author carlis
 */
@Named
@ViewScoped
public class ControladorProducto extends BussinesEntityHome<Producto> implements Serializable{
   @Inject
    @Web
    private EntityManager em;
    @Inject
    private ServicioGenerico servgen;
    List<Producto> listaProducto = new ArrayList<Producto>();

    public Long getProductoId() {
        return (Long) getId();
    }

    public void getProductoId(Long producto) {
        setId(producto);
    }

    @TransactionAttribute   //
    public Producto load() {
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

    public List<Producto> getListaProducto() {
        return listaProducto;
    }

    public void setListaProducto(List<Producto> listaProducto) {
        this.listaProducto = listaProducto;
    }

 
    @PostConstruct
    public void init() {
        setEntityManager(em);
        /*el bussinesEntityService.setEntityManager(em) solo va si la Entidad en este caso (ConsultaMedia)
         *hereda de la Entidad BussinesEntity...  caso contrario no se lo agrega
         */
        bussinesEntityService.setEntityManager(em);
        servgen.setEm(em);
        listaProducto = servgen.buscarTodos(Producto.class);
    }

    @Override
    protected Producto createInstance() {
        //prellenado estable para cualquier clase 
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(Producto.class.getName());
        Date now = Calendar.getInstance().getTime();
        Producto producto = new Producto();
        producto.setCreatedOn(now);
        producto.setLastUpdate(now);
        producto.setActivationTime(now);

        //fichaMedic.setResponsable(null);    //cambiar atributo a 
        producto.setType(_type);
        producto.buildAttributes(bussinesEntityService);  //
        return producto;
    }

    @Override
    public Class<Producto> getEntityClass() {
        return Producto.class;
    }

    @TransactionAttribute
    public String guardar() {
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        try {
            if (getInstance().isPersistent()) {
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizo Producto" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            } else {
                create(getInstance());
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se creo un nuevo Producto" + getInstance().getId() + " con éxito"," ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error al guardar: " + getInstance().getId()," ");
            FacesContext.getCurrentInstance().addMessage("", msg);
        }
        return "/paginas/producto/lista.xhtml?faces-redirect=true";
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
        return "/paginas/producto/lista.xhtml?faces-redirect=true";
    }   
}
