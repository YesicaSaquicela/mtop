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
import org.mtop.controlador.dinamico.BussinesEntityHome;
import org.mtop.modelo.dinamico.BussinesEntityType;
import org.mtop.modelo.EstadoVehiculo;

import org.mtop.servicios.ServicioGenerico;

/**
 *
 * @author carlis
 */
@Named
@ViewScoped
public class ControladorEstadoVehiculo extends BussinesEntityHome<EstadoVehiculo> implements Serializable {

    @Inject
    @Web
    private EntityManager em;
    @Inject
    private ServicioGenerico servgen;
    List<EstadoVehiculo> listaEstadoVehiculo = new ArrayList<EstadoVehiculo>();
    private String palabrab = "";
    private Date fechaFinal;
   

    public Date getFechaFinal() {
        fechaFinal=listaEstadoVehiculo.get(listaEstadoVehiculo.size()-1).getFechaEntrada();
        System.out.println("fecha final"+fechaFinal);
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
              this.fechaFinal = fechaFinal;
    }
    
    
 
    public String getPalabrab() {
        return palabrab;
    }

    public void setPalabrab(String palabrab) {
        this.palabrab = palabrab;
    }

    
    public Long getEstadoVehiculoId() {
        return (Long) getId();
    }

    public void setEstadoVehiculoId(Long estadoVehiculoId) {
        setId(estadoVehiculoId);
    }

    @TransactionAttribute   
    public EstadoVehiculo load() {
        if (isIdDefined()) {
            wire();
        }

        return getInstance();
    }

    @TransactionAttribute
    public void wire() {
        getInstance();
    }

    public List<EstadoVehiculo> getListaEstadoVehiculo() {
        return listaEstadoVehiculo;
    }

    public void setListaEstadoVehiculo(List<EstadoVehiculo> listaEstadoVehiculo) {
        this.listaEstadoVehiculo = listaEstadoVehiculo;
    }

    @PostConstruct
    public void init() {
        setEntityManager(em);
        /*el bussinesEntityService.setEntityManager(em) solo va si la Entidad en este caso (ConsultaMedia)
         *hereda de la Entidad BussinesEntity...  caso contrario no se lo agrega
         */
        bussinesEntityService.setEntityManager(em);
        servgen.setEm(em);
        listaEstadoVehiculo = findAll(EstadoVehiculo.class);
//        List<EstadoVehiculo> le= servgen.buscarTodos(EstadoVehiculo.class);
//        listaEstadoVehiculo.clear();
////
//        for (EstadoVehiculo estado : le) {
//            if (estado.isEstado()) {
//                listaEstadoVehiculo.add(estado);
//                System.out.println("Entro a remover>>>>");
//                System.out.println("a;iadia" + listaEstadoVehiculo);
//
//            }
//
//        }
    }

    @Override
    protected EstadoVehiculo createInstance() {
        //prellenado estable para cualquier clase 
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(EstadoVehiculo.class.getName());
        Date now = Calendar.getInstance().getTime();
        EstadoVehiculo estadoVehiculo = new EstadoVehiculo();
        estadoVehiculo.setCreatedOn(now);
        estadoVehiculo.setLastUpdate(now);
        estadoVehiculo.setActivationTime(now);
        estadoVehiculo.setType(_type);
        estadoVehiculo.buildAttributes(bussinesEntityService);  //
        return estadoVehiculo;
    }

    @Override
    public Class<EstadoVehiculo> getEntityClass() {
        return EstadoVehiculo.class;
    }

    @TransactionAttribute
    public String guardar() {

        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);

        System.out.println("IIIIDEPERSISTEN  >>>>>>" + getInstance());

        try {
            if (getInstance().isPersistent()) {
                System.out.println("Entro a Editar>>>>>>>>");
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizó Partida contabilidad" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            } else {
                System.out.println("Entro a crear>>>>>>>>");
                getInstance().setEstado(true);
                create(getInstance());
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se creó una nueva Partida contabilidad" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
        }
        return "/paginas/admin/vehiculo/estadodeubicacion/lista.xhtml?faces-redirect=true";
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
        return "/paginas/admin/vehiculo/estadoUbicacion/lista.xhtml?faces-redirect=true";
    }
}
