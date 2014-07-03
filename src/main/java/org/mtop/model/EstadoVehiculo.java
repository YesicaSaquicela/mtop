/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mtop.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author jesica
 */

@Entity
@Table(name = "EstadoVehiculo")
@DiscriminatorValue(value = "evk")//valor que se discrimina por cada clase
@PrimaryKeyJoinColumn(name = "id")//representa el id de la superclase
public class EstadoVehiculo extends EntidadGeneral implements Serializable {
   
   
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaEntrada;
    
    private String ubicacion;
    @ManyToOne
    @JoinColumn(name = "vehiculoId")
    private Vehiculo vehiculo;

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    

  

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

   
   
    @Override
    public String toString() {
        return "org.mtop.model.Estado[ id=" + getId() + " ]";
    }
    
}
