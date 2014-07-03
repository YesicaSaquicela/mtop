/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mtop.data;

import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import org.mtop.model.EntidadAbstracta_;

import org.mtop.model.Member;
import org.mtop.model.Persona;
import org.mtop.model.Persona_;
import org.mtop.service.ServicioGenerico;

@Named
@RequestScoped
public class ListaPersonas {

    @Inject //utilizar los dtaos de otra clase
    private ServicioGenerico sg;
    private List<Persona> personas=new ArrayList<Persona>();
    private List<Persona> conductores=new ArrayList<Persona>();;
    private List<Persona> mecanicos=new ArrayList<Persona>();;
    

    // @Named provides access the return value via the EL variable name "members" in the UI (e.g.
    // Facelets or JSP view)
  
 

    public List<Persona> getConductores() {
        return conductores;
    }

    public void setConductores(List<Persona> conductores) {
        conductores=sg.buscarTodos(Persona.class, Persona_.estado.getName(),Persona_.tipo.getName(),"mecanico");
        this.conductores = conductores;
    }
    
  
    public List<Persona> getMecanico() {
        mecanicos=sg.buscarTodos(Persona.class, Persona_.estado.getName(),Persona_.tipo.getName(),"mecanico");
        return mecanicos;
    }

    public void setMecanico(List<Persona> mecanico) {
        this.mecanicos = mecanico;
    }
    
    public List<Persona> getPersona() {
        
        return personas;
    }

    public void setConductor(List<Persona> personas1) {
        this.personas = personas1;
    }
   
    
    @PostConstruct
    public void init(){
        personas=sg.buscarTodos(Persona.class, Persona_.estado.getName());
    }
}