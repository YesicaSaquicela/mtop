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
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.mtop.modelo.dinamico.BussinesEntity;

/**
 *
 * @author carla
 * @author yesica
 * 
 */


@Entity
@Table(name = "Kardex")
@DiscriminatorValue(value = "kk")//valor que se discrimina por cada clase
@PrimaryKeyJoinColumn(name = "id")//representa el id de la superclase
public class Kardex extends BussinesEntity implements Serializable {
  
    @OneToMany(mappedBy = "kardex", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SolicitudReparacionMantenimiento> listaSolicitudReparacion = new ArrayList<SolicitudReparacionMantenimiento>();
    @OneToMany(mappedBy = "kardex",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Requisicion> listaRequisicion=new ArrayList<Requisicion>();
    private String numero;

    @JoinColumn(name = "vehiculoId")
    @OneToOne
    private Vehiculo vehiculo;

    public List<Requisicion> getListaRequisicion() {
        return listaRequisicion;
    }

    public void setListaRequisicion(List<Requisicion> listaRequisicion) {
         for (Requisicion requisicion : listaRequisicion) {
           requisicion.setKardex(this);
        }
        this.listaRequisicion = listaRequisicion;
    }
     
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
    

    public List<SolicitudReparacionMantenimiento> getListaSolicitudReparacion() {
        return listaSolicitudReparacion;
    }

    public void setListaSolicitudReparacion(List<SolicitudReparacionMantenimiento> listaSolicitudReparacion) {
        for (SolicitudReparacionMantenimiento solicitudReparacion : listaSolicitudReparacion) {
            solicitudReparacion.setKardex(this);
        }
        this.listaSolicitudReparacion = listaSolicitudReparacion;
    }


    @Override
    public String toString() {
        return "org.mtop.modelo.Kardex[ id=" + getId()+ " ]";
    }

}
