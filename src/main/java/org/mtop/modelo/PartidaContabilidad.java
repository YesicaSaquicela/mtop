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
package org.mtop.modelo;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import org.mtop.modelo.dinamico.BussinesEntity;

/**
 *
 * @author carlis
 */
@Entity
@Table(name = "PartidaContabilidad")
@DiscriminatorValue(value = "pck")//valor que se discrimina por cada clase
@PrimaryKeyJoinColumn(name = "id")
public class PartidaContabilidad extends BussinesEntity implements Serializable {

    private static final long serialVersionUID = 1L;
 
    @Pattern(regexp = "[0-9]+", message = "Error: solo puede ingresar números")
    private String numeroProvincia;
    @Pattern(regexp = "[0-9]+", message = "Error: solo puede ingresar números")
    private String numeroPrograma;
    @Pattern(regexp = "[0-9]+", message = "Error: solo puede ingresar números")
    private String numeroProyecto;
    @Pattern(regexp = "[0-9]+", message = "Error: solo puede ingresar números")
    private String numeroItem;
    @Pattern(regexp = "[0-9]+", message = "Error: solo puede ingresar números")
    private String numeroFuenteFinanciera;
    private String descripcion;


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNumeroProvincia() {
        return numeroProvincia;
    }

    public void setNumeroProvincia(String numeroProvincia) {
        this.numeroProvincia = numeroProvincia;
    }

    public String getNumeroPrograma() {
        return numeroPrograma;
    }

    public void setNumeroPrograma(String numeroPrograma) {
        this.numeroPrograma = numeroPrograma;
    }

    public String getNumeroProyecto() {
        return numeroProyecto;
    }

    public void setNumeroProyecto(String numeroProyecto) {
        this.numeroProyecto = numeroProyecto;
    }

    public String getNumeroItem() {
        return numeroItem;
    }

    public void setNumeroItem(String numeroItem) {
        this.numeroItem = numeroItem;
    }

    public String getNumeroFuenteFinanciera() {
        return numeroFuenteFinanciera;
    }

    public void setNumeroFuenteFinanciera(String numeroFuenteFinanciera) {
        this.numeroFuenteFinanciera = numeroFuenteFinanciera;
    }

   
   public String concatenarPartida() {
        String resultado = "250-" + getNumeroProvincia() + "-0000-" + getNumeroPrograma() + "-00-" + getNumeroProyecto() + "-001-" + getNumeroItem() + "-1100-"+ getNumeroFuenteFinanciera();
        return resultado;
    }

    @Override
    public String toString() {
        return "org.mtop.modelo.PartidaContabilidad[ id=" + getId() + " ]";
    }

}
