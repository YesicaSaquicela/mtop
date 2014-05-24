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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.mtop.model.BussinesEntity;

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

    private long numeroProvincia;
    private long numeroPrograma;
    private long numeroProyecto;
    private long numeroItem;
    private long numeroFuenteFinanciera;
    private String descripcion;

    public long getNumeroProvincia() {
        return numeroProvincia;
    }

    public void setNumeroProvincia(long numeroProvincia) {
        this.numeroProvincia = numeroProvincia;
    }

    public long getNumeroPrograma() {
        return numeroPrograma;
    }

    public void setNumeroPrograma(long numeroPrograma) {
        this.numeroPrograma = numeroPrograma;
    }

    public long getNumeroProyecto() {
        return numeroProyecto;
    }

    public void setNumeroProyecto(long numeroProyecto) {
        this.numeroProyecto = numeroProyecto;
    }

    public long getNumeroItem() {
        return numeroItem;
    }

    public void setNumeroItem(long numeroItem) {
        this.numeroItem = numeroItem;
    }

    public long getNumeroFuenteFinanciera() {
        return numeroFuenteFinanciera;
    }

    public void setNumeroFuenteFinanciera(long numeroFuenteFinanciera) {
        this.numeroFuenteFinanciera = numeroFuenteFinanciera;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "org.mtop.modelo.PartidaContabilidad[ id=" + getId() + " ]";
    }

}
