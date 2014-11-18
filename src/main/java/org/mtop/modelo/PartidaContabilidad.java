/*
    SOFTWARE PARA LA GESTIÓN DE INFORMACIÓN DEL ESTADO  MECÁNICO DE LOS 
    VEHÍCULOS DEL MINISTERIO DE TRANSPORTE Y OBRAS PÚBLICAS
    Copyright (C) 2014  Romero Carla, Saquicela Yesica

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
 * @author carla
 * @author yesica
 * 
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
    private String tipo;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    


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
        String resultado = "250-" + getNumeroProvincia() + "-0000-" + getNumeroPrograma() + "-00-" + getNumeroProyecto() + "-001-" + getNumeroItem() + "-1100-"+ getNumeroFuenteFinanciera()+"-0000"+"-0000";
        return resultado;
    }

    @Override
    public String toString() {
        return "org.mtop.modelo.PartidaContabilidad[ id=" + getId() + " ]";
    }

}
