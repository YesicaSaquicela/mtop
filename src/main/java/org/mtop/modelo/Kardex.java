/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mtop.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.mtop.model.BussinesEntity;
import org.mtop.modelo.Vehiculo;

/**
 *
 * @author jesica
 */

@Entity
@Table(name = "Kardex")
@DiscriminatorValue(value = "kk")//valor que se discrimina por cada clase
@PrimaryKeyJoinColumn(name = "id")//representa el id de la superclase
public class Kardex extends BussinesEntity implements Serializable {
  
    private String observaciones;
//    @OneToMany(mappedBy = "kardex", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<SolicitudReparacionMantenimiento> listaSolicitudReparacion = new ArrayList<SolicitudReparacionMantenimiento>();
//    @OneToMany(mappedBy = "kardex",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    private List<Requisicion> requisicion=new ArrayList<Requisicion>();
//    private String numero;
//    
//    @ManyToOne
//    @JoinColumn(name = "vehiculoId")
//    private Vehiculo vehiculo;

//    public List<Requisicion> getRequisicion() {
//        return requisicion;
//    }
//
//    public void setRequisicion(List<Requisicion> requisicion) {
//        this.requisicion = requisicion;
//    }
//
//     
//    public Vehiculo getVehiculo() {
//        return vehiculo;
//    }
//
//    public void setVehiculo(Vehiculo vehiculo) {
//        this.vehiculo = vehiculo;
//    }
//    
//    
//
//    public Date getFechaEntradaR() {
//        return fechaEntradaR;
//    }
//
//    public void setFechaEntradaR(Date fechaEntradaR) {
//        this.fechaEntradaR = fechaEntradaR;
//    }
//
//    public Date getFechaSalidaR() {
//        return fechaSalidaR;
//    }
//
//    public void setFechaSalidaR(Date fechaSalidaR) {
//        this.fechaSalidaR = fechaSalidaR;
//    }
//
//    public String getNumero() {
//        return numero;
//    }
//
//    public void setNumero(String numero) {
//        this.numero = numero;
//    }
//    
//    
//  
//  
//    public Date getFechaEntrada() {
//        return fechaEntradaR;
//    }
//
//    public void setFechaEntrada(Date fechaEntrada) {
//        this.fechaEntradaR = fechaEntrada;
//    }
//
//    public Date getFechaSalida() {
//        return fechaSalidaR;
//    }
//
//    public void setFechaSalida(Date fechaSalida) {
//        this.fechaSalidaR = fechaSalida;
//    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

//    public List<SolicitudReparacionMantenimiento> getListaSolicitudReparacion() {
//        return listaSolicitudReparacion;
//    }
//
//    public void setListaSolicitudReparacion(List<SolicitudReparacionMantenimiento> listaSolicitudReparacion) {
//        for (SolicitudReparacionMantenimiento solicitudReparacion : listaSolicitudReparacion) {
//            solicitudReparacion.setKardex(this);
//        }
//        this.listaSolicitudReparacion = listaSolicitudReparacion;
//    }


    @Override
    public String toString() {
        return "org.mtop.model.Kardex[ id=" + getId()+ " ]";
    }

}
