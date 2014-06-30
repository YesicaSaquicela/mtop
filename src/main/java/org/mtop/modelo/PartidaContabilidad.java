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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
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

    private Long numeroProvincia;
    private Long numeroPrograma;
    private Long numeroProyecto;
    private Long numeroItem;
    private Long numeroFuenteFinanciera;
    private String descripcion;

//    @OneToOne(mappedBy = "partidaC")
//    private PartidaContabilidad pContabilidad;
//    public PartidaContabilidad getpContabilidad() {
//        return pContabilidad;
//    }
//
//    public void setpContabilidad(PartidaContabilidad pContabilidad) {
//        this.pContabilidad = pContabilidad;
//    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getNumeroProvincia() {
        return numeroProvincia;
    }

    public void setNumeroProvincia(Long numeroProvincia) {
        this.numeroProvincia = numeroProvincia;
    }

    public Long getNumeroPrograma() {
        return numeroPrograma;
    }

    public void setNumeroPrograma(Long numeroPrograma) {
        this.numeroPrograma = numeroPrograma;
    }

    public Long getNumeroProyecto() {
        return numeroProyecto;
    }

    public void setNumeroProyecto(Long numeroProyecto) {
        this.numeroProyecto = numeroProyecto;
    }

    public Long getNumeroItem() {
        return numeroItem;
    }

    public void setNumeroItem(Long numeroItem) {
        this.numeroItem = numeroItem;
    }

    public Long getNumeroFuenteFinanciera() {
        return numeroFuenteFinanciera;
    }

    public void setNumeroFuenteFinanciera(Long numeroFuenteFinanciera) {
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
