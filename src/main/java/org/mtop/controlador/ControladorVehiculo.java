/*
 * Copyright 2014 jesica.
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
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import org.jboss.seam.transaction.Transactional;
import org.mtop.cdi.Web;
import org.mtop.controlador.dinamico.BussinesEntityHome;
import org.mtop.modelo.dinamico.BussinesEntityAttribute;
import org.mtop.modelo.dinamico.BussinesEntityType;
import org.mtop.modelo.dinamico.Property;
import org.mtop.modelo.Vehiculo;
import org.mtop.servicios.ServicioGenerico;

/**
 *
 * @author jesica
 */
@Named
@ViewScoped
public class ControladorVehiculo extends BussinesEntityHome<Vehiculo> implements Serializable {

    @Inject
    @Web
    private EntityManager em;
    @Inject
    private ServicioGenerico servgen;
    List<Vehiculo> listaVehiculos = new ArrayList<Vehiculo>();
    private String numeroRegistro;

    public String getNumeroRegistro() {
        if (getId() == null) {
            System.out.println("numero"+getInstance().getNumRegistro());
            List<Vehiculo> lista = findAll(Vehiculo.class);
            int t = lista.size();
            if (t < 9) {
                setNumeroRegistro("000".concat(String.valueOf(t + 1)));
            } else {
                if (t >= 9 && t < 99) {
                    setNumeroRegistro("00".concat(String.valueOf(t + 1)));
                } else {
                    if (t >= 99 && t < 999) {
                        setNumeroRegistro("0".concat(String.valueOf(t + 1)));
                    } else {
                        setNumeroRegistro(String.valueOf(t + 1));
                    }
                }
            }
        }else{
            setNumeroRegistro(getInstance().getNumRegistro());
        }
        
        return numeroRegistro;

    }

    public void setNumeroRegistro(String numRegistro) {
        this.numeroRegistro = numRegistro;
        getInstance().setNumRegistro(this.numeroRegistro);

    }

    public Long getVehiculoId() {
        System.out.println("IIIIDEE" + getId());
        return (Long) getId();
    }

    public void setVehiculoId(Long vehiculoId) {

        setId(vehiculoId);

    }

    @TransactionAttribute   //
    public Vehiculo load() {
        if (isIdDefined()) {
            wire();
        }

        return getInstance();
    }

    @TransactionAttribute
    public void wire() {
        getInstance();
    }

    public List<Vehiculo> getListaVehiculos() {
        return listaVehiculos;
    }

    public void setListaVehiculos(List<Vehiculo> listaVehiculos) {
        this.listaVehiculos = listaVehiculos;
    }

    @PostConstruct
    public void init() {
        setEntityManager(em);
        
        /*el bussinesEntityService.setEntityManager(em) solo va si la Entidad en este caso (Vehiculo)
         *hereda de la Entidad BussinesEntity...  caso contrario no se lo agrega
         */
        bussinesEntityService.setEntityManager(em);
        servgen.setEm(em);
        listaVehiculos = servgen.buscarTodos(Vehiculo.class);
        
        
    }

    @Override
    protected Vehiculo createInstance() {
        //prellenado estable para cualquier clase 
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(Vehiculo.class.getName());
        Date now = Calendar.getInstance().getTime();
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setCreatedOn(now);
        vehiculo.setLastUpdate(now);
        vehiculo.setActivationTime(now);
        vehiculo.setType(_type);
        vehiculo.buildAttributes(bussinesEntityService);  //
        return vehiculo;
    }

    @Override
    public Class<Vehiculo> getEntityClass() {
        return Vehiculo.class;
    }

    @TransactionAttribute
    public String guardar() {

        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        System.out.println("PRESENTAR ANTES>>>>>" + getInstance().getNumRegistro());
        System.out.println("IIIIDEEEntro>>>>>>" + getVehiculoId());
        System.out.println("PRESENTAR persisten>>>>>" + getInstance().isPersistent());
        try {
            if (getInstance().isPersistent()) {
                System.out.println("PRESENTAR GUERADAR>>>>>" + getInstance().getNumRegistro());

                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizo Vehiculo" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            } else {

                System.out.println("PRESENTAR EDITAR>>>>>" + getInstance().getNumRegistro());
                getInstance().setEstado(true);
                create(getInstance());
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se creo una nueva vehiculo" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
        }
        return "/paginas/vehiculo/lista.xhtml?faces-redirect=true";
    }

    @TransactionAttribute
    public String editarEstado() {
        getInstance().findBussinesEntityAttribute(numeroRegistro).size();
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        System.out.println("PRESENTAR ANTES>>>>>" + getInstance().getNumRegistro());
        System.out.println("IIIIDEEEntro>>>>>>" + getVehiculoId());
        System.out.println("PRESENTAR persisten>>>>>" + getInstance().isPersistent());
        try {
            save(getInstance());
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizo Vehiculo" + getInstance().getId() + " con éxito", " ");
            FacesContext.getCurrentInstance().addMessage("", msg);

        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
        }
        return "/paginas/vehiculo/estadoVehiculo/lista.xhtml?faces-redirect=true";
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
        return "/paginas/vehiculo/lista.xhtml?faces-redirect=true";
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
