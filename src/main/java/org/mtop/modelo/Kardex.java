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
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.mtop.modelo.dinamico.BussinesEntity;
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
//    private List<Requisicion> listaRequisicion=new ArrayList<Requisicion>();
    private String numero;
    
    @OneToOne
    @JoinColumn(name = "vehiculoId")
    private Vehiculo vehiculo;

//    public List<Requisicion> getListaRequisicion() {
//        return listaRequisicion;
//    }
//
//    public void setListaRequisicion(List<Requisicion> listaRequisicion) {
//         for (Requisicion requisicion : listaRequisicion) {
//           requisicion.setKardex(this);
//        }
//        this.listaRequisicion = listaRequisicion;
//    }
     
    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }
    

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
    

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
