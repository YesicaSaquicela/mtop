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
import org.mtop.model.BussinesEntityAttribute;
import org.mtop.model.BussinesEntityType;
import org.mtop.model.profile.Profile;
import org.mtop.modelo.Persona;
import org.mtop.modelo.Vehiculo;
import org.mtop.profile.ProfileService;
import org.mtop.servicios.ServicioGenerico;

/**
 *
 * @author carlis
 */
@Named
@ViewScoped
public class ControladorPersona extends BussinesEntityHome<Profile> implements Serializable{
    
    @Inject
    @Web
    private EntityManager em;
    @Inject
    private ServicioGenerico servgen;
    //@Inject
    //private ProfileService pservicio;
    List<Profile> listaPersona= new ArrayList<Profile>();
    
    public Long getPersonaId() {
 
        return (Long) getId();
    }

    public void setPersonaId(Long personaId) {
       
        setId(personaId);

    }

    @TransactionAttribute   //
    public Profile load() {
        if (isIdDefined()) {
            wire();
        }
        //  log.info("sgssalud --> cargar instance " + getInstance());
        return getInstance();
    }

    public List<Profile> getListaPersona() {
        return listaPersona;
    }

    public void setListaPersona(List<Profile> listaPersona) {
        this.listaPersona = listaPersona;
    }
    

    @TransactionAttribute
    public void wire() {
        getInstance();
    }



    @PostConstruct
    public void init() {
        setEntityManager(em);
        /*el bussinesEntityService.setEntityManager(em) solo va si la Entidad en este caso (ConsultaMedia)
         *hereda de la Entidad BussinesEntity...  caso contrario no se lo agrega
         */
        bussinesEntityService.setEntityManager(em);
       // pservicio.setEntityManager(em);
      servgen.setEm(em);
      listaPersona = servgen.buscarTodos(Profile.class);
    }
    
    

    @Override
    protected Profile createInstance() {
        //prellenado estable para cualquier clase 
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(Profile.class.getName());
        Date now = Calendar.getInstance().getTime();
        Profile persona = new Profile();
        persona.setCreatedOn(now);
        persona.setLastUpdate(now);
        persona.setActivationTime(now);

        persona.setType(_type);
        persona.buildAttributes(bussinesEntityService);  //
        return persona;
    }

    @Override
    public Class<Profile> getEntityClass() {
        return Profile.class;
    }
//tiene muchas variaciones
    @TransactionAttribute
    public String guardar() {
Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        
        try {
            if (getInstance().isPersistent()) {
                List<BussinesEntityAttribute> listA = getInstance().getAttributes();
                System.out.println("Attributos "+getInstance().getAttributes().size());
                for (BussinesEntityAttribute a : listA) {
                    System.out.println("ATRIB "+a.getName()+" valor "+a.getValue().toString() +" valor String "+a.getStringValue());
                    //save(a);
                    
                }
                //update();
                save(getInstance());                
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizo Persona" + getInstance().getId() + " con éxito", " ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            } else {
                create(getInstance());
                save(getInstance());
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se creo nueva Vehiculo " + getInstance().getId() + " con éxito"," ");
                FacesContext.getCurrentInstance().addMessage("", msg);
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error al guardar: " + getInstance().getId()," ");
            FacesContext.getCurrentInstance().addMessage("", msg);
        }
         return "/paginas/personal/lista.xhtml?faces-redirect=true";

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
        return "/paginas/personal/lista.xhtml?faces-redirect=true";
    }
}
