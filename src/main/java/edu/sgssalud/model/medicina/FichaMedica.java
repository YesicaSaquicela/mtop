/*
 * Copyright 2013 cesar.
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
package edu.sgssalud.model.medicina;

import edu.sgssalud.model.BussinesEntity;
import edu.sgssalud.model.profile.Profile;
import edu.sgssalud.model.paciente.Paciente;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author cesar
 */
@Entity
@Table(name = "FichaMedica")
@DiscriminatorValue(value = "FM")
@PrimaryKeyJoinColumn(name = "id")
public class FichaMedica extends BussinesEntity implements Serializable {    

    private static final long serialVersionUID = 1L;   
    
    private Long numeroFicha;
// la fecha de apertura de la historiaClinica se guarda en (createOn)
// registrar Tipos de entidad de Negocio antecedentes familiares y personales    
    
    private String observacionAntecedentesPersonales;
    private String observacionAntecedentesFamiliares;
    
    @OneToOne
    @JoinColumn(name = "paciente_id")  //nombre de la columna en la BD
    private Paciente paciente;

    public Long getNumeroFicha() {
        return numeroFicha;
    }

    public void setNumeroFicha(Long numeroFicha) {
        this.numeroFicha = numeroFicha;
    }      

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }       

    public String getObservacionAntecedentesPersonales() {
        return observacionAntecedentesPersonales;
    }

    public void setObservacionAntecedentesPersonales(String observacionAntecedentesPersonales) {
        this.observacionAntecedentesPersonales = observacionAntecedentesPersonales;
    }

    public String getObservacionAntecedentesFamiliares() {
        return observacionAntecedentesFamiliares;
    }

    public void setObservacionAntecedentesFamiliares(String observacionAntecedentesFamiliares) {
        this.observacionAntecedentesFamiliares = observacionAntecedentesFamiliares;
    } 
        
}
