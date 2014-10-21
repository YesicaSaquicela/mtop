/*
 * Copyright 2014 yesica.
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

package org.mtop.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.mtop.modelo.profile.Profile;

/**
 *
 * @author yesica
 */
@Entity
@Table(name = "Auxiliar")
public class Auxiliar {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "requisicionId")
    private Requisicion requisicionId;
    @ManyToOne
    @JoinColumn(name = "soliciudId")
    private SolicitudReparacionMantenimiento soliciudId;
    @ManyToOne
    @JoinColumn(name = "personalId")
    private Profile personalId;
    private String tipoRelacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Requisicion getRequisicionId() {
        return requisicionId;
    }

    public void setRequisicionId(Requisicion requisicionId) {
        this.requisicionId = requisicionId;
    }

    public SolicitudReparacionMantenimiento getSoliciudId() {
        return soliciudId;
    }

    public void setSoliciudId(SolicitudReparacionMantenimiento soliciudId) {
        this.soliciudId = soliciudId;
    }

    public Profile getPersonalId() {
        return personalId;
    }

    public void setPersonalId(Profile personalId) {
        this.personalId = personalId;
    }

    
    public String getTipoRelacion() {
        return tipoRelacion;
    }

    public void setTipoRelacion(String tipoRelacion) {
        this.tipoRelacion = tipoRelacion;
    }
    
    
}
