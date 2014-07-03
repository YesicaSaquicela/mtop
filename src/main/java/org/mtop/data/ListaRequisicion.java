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
import org.mtop.model.Requisicion;
import org.mtop.model.Requisicion_;
import org.mtop.model.SolicitudReparacion;
import org.mtop.model.SolicitudReparacion_;
import org.mtop.service.ServicioGenerico;

@Named
@RequestScoped
public class ListaRequisicion {

    @Inject //utilizar los dtaos de otra clase
    private ServicioGenerico sg;
    private List<Requisicion> requisicion;

    // @Named provides access the return value via the EL variable name "members" in the UI (e.g.
    // Facelets or JSP view)
    public List<Requisicion> getRequisicion() {
        return requisicion;
    }
    public List<Requisicion> getRequisicionAprovada() {
        for (Requisicion requisicion1 : requisicion) {
            if(requisicion1.getAprobado()){
                this.requisicion.add(requisicion1);
            }
        }
        return requisicion;
    }
    public List<Requisicion> getSolicitudesReparacionNoAprovadas() {
        for (Requisicion requisicion1 : requisicion) {
            if(!requisicion1.getAprobado()){
                this.requisicion.add(requisicion1);
            }
        }
        return requisicion;
    }
    public void setRequisicion(List<Requisicion> requisiciones) {
        this.requisicion = requisiciones;
    }
   
    
    @PostConstruct
    public void init(){
        requisicion=sg.buscarTodos(Requisicion.class, Requisicion_.estado.getName());
    }

   
}