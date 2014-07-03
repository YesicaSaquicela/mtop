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
import org.mtop.model.SolicitudReparacion;
import org.mtop.model.SolicitudReparacion_;
import org.mtop.service.ServicioGenerico;

@Named
@RequestScoped
public class ListaSolicitudesReparacion {

    @Inject //utilizar los dtaos de otra clase
    private ServicioGenerico sg;
    private List<SolicitudReparacion> solicitudesReparacion;

    // @Named provides access the return value via the EL variable name "members" in the UI (e.g.
    // Facelets or JSP view)
    public List<SolicitudReparacion> getSolicitudesReparacion() {
        return solicitudesReparacion;
    }
    public List<SolicitudReparacion> getSolicitudesReparacionAprovadas() {
        for (SolicitudReparacion solicitudReparacion : solicitudesReparacion) {
            if(solicitudReparacion.getAprobado()){
                this.solicitudesReparacion.add(solicitudReparacion);
            }
        }
        return solicitudesReparacion;
    }
    public List<SolicitudReparacion> getSolicitudesReparacionNoAprovadas() {
        for (SolicitudReparacion solicitudReparacion : solicitudesReparacion) {
            if(!solicitudReparacion.getAprobado()){
                this.solicitudesReparacion.add(solicitudReparacion);
            }
        }
        return solicitudesReparacion;
    }
    public void setSolicitudesReparacion(List<SolicitudReparacion> solicitudReparacions) {
        this.solicitudesReparacion = solicitudReparacions;
    }
   
    
    @PostConstruct
    public void init(){
        solicitudesReparacion=sg.buscarTodos(SolicitudReparacion.class, SolicitudReparacion_.estado.getName());
    }

   
}
