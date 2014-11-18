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
import java.util.Date;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.mtop.modelo.dinamico.BussinesEntity;


/**
 *
 * @author carla
 * @author yesica
 * 
 */
@Entity
@Table(name = "EstadoVehiculo")
@DiscriminatorValue(value = "evk")//valor que se discrimina por cada clase
@PrimaryKeyJoinColumn(name = "id")//representa el id de la superclase
public class EstadoVehiculo extends BussinesEntity implements Serializable {

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaEntrada;

    private String ubicacion;
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "vehiculoId")
    private Vehiculo vehiculo;

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        
        this.vehiculo = vehiculo;
    }
   
    public Date getFechaEntrada() {
        
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
       this.fechaEntrada = fechaEntrada;
        
        System.out.println("fija fecha"+this.fechaEntrada);
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "org.mtop.modelo.EstadoVehiculo[ id=" + getId() + " ]";
    }

}
