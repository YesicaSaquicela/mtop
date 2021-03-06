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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "SolicitudReparacion")
@DiscriminatorValue(value = "srk")//valor que se discrimina por cada clase
@PrimaryKeyJoinColumn(name = "id")//representa el id de la superclase

public class SolicitudReparacionMantenimiento extends BussinesEntity implements Serializable {

    @Enumerated(EnumType.STRING)//anotacion de tipos de datos
    private SolicitudReparacionMantenimiento.datosEnumerador listaEnum;

    private String observacion;
    private boolean aprobado;
    private String numSolicitud;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaSolicitud;
  
    @ManyToOne
    @JoinColumn(name = "kardexId")
    private Kardex kardex;
    @ManyToOne
    @JoinColumn(name = "vehiculoId")
    private Vehiculo vehiculo;
    @OneToMany(mappedBy = "solicitudReparacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemSolicitudReparacion> listaItemSR = new ArrayList<ItemSolicitudReparacion>();
    
    @OneToOne(fetch = FetchType.LAZY,mappedBy = "solicitudReparacionId")
    private Requisicion requisicionId;

    

    public Requisicion getRequisicionId() {
        return requisicionId;
    }

    public void setRequisicionId(Requisicion requisicionId) {
        this.requisicionId = requisicionId;
        
    }

   


    public String getNumSolicitud() {
        return numSolicitud;
    }

    public void setNumSolicitud(String numSolicitud) {
        this.numSolicitud = numSolicitud;
    }

    

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public boolean getAprobado() {
        return aprobado;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }


    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public List<ItemSolicitudReparacion> getListaItemSR() {
       return listaItemSR;
   }

    public void setListaItemSR(List<ItemSolicitudReparacion> listaItemSR) {
        for (ItemSolicitudReparacion itemSolicicitudReparacion : listaItemSR) {
            itemSolicicitudReparacion.setSolicitudReparacion(this);

        }
        this.listaItemSR = listaItemSR;
    }

    public Kardex getKardex() {
        return kardex;
    }

    public void setKardex(Kardex kardex) {
        this.kardex = kardex;
    }
    public datosEnumerador getListaEnum() {
        return listaEnum;
    }

    public void setListaEnum(datosEnumerador listaEnum) {
        this.listaEnum = listaEnum;
    }

    @Override
    public String toString() {
        return "org.mtop.modelo.Solicitud[ id=" + getId() + " ]";
    }

    //enum tclase k representa tipos de datos enumerados
    public enum datosEnumerador {
        
//        vehiculo1,vehiculo2;
      
        VEHICULO1("Hola"),
        VEHICULO("Hola2");
        
//
        private final String tipo;//para recorre datos 
        //constructor para inicializar y llamar

        private datosEnumerador(String tipo) {
            this.tipo = tipo;
        }

        //devolver la llave del tipo
        public String getTipo() {
            return tipo;
        }
        
        public String metodo(String nombre){
            return null;
        
        }
      
    }

}
