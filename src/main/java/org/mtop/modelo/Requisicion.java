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
@Table(name = "Requisicion")
@DiscriminatorValue(value = "rk")//valor que se discrimina por cada clase
@PrimaryKeyJoinColumn(name = "id")//representa el id de la superclase
public class Requisicion extends BussinesEntity implements Serializable {

    private boolean aprobado;
    private String numRequisicion;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaRequisicion;
    private String tipoAdquisicion;
    private String observaciones;
    private String tipoRequisicion;

    @ManyToOne
    @JoinColumn(name = "vehiculoId1")
    private Vehiculo vehiculo;

    @OneToMany(mappedBy = "requisicion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemRequisicion> listaItems = new ArrayList<ItemRequisicion>();
  
    
    @OneToOne(fetch = FetchType.LAZY)
    private SolicitudReparacionMantenimiento solicitudReparacionId;

    @ManyToOne
    @JoinColumn(name = "kardexId")
    private Kardex kardex;


    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Kardex getKardex() {
        return kardex;
    }

    public void setKardex(Kardex kardex) {
        this.kardex = kardex;
    }

    public SolicitudReparacionMantenimiento getSolicitudReparacionId() {
        return solicitudReparacionId;
    }

    public void setSolicitudReparacionId(SolicitudReparacionMantenimiento solicitudReparacionId) {
        this.solicitudReparacionId = solicitudReparacionId;
    }

    public List<ItemRequisicion> getListaItems() {
        return listaItems;
    }
   

    public void setListaItems(List<ItemRequisicion> listaItems) {
        for (ItemRequisicion itemRequisicion : listaItems) {
            itemRequisicion.setRequisicion(this);
        }
        this.listaItems = listaItems;
    }


    public String getNumRequisicion() {
        return numRequisicion;
    }

    public void setNumRequisicion(String numRequisicion) {
        this.numRequisicion = numRequisicion;
    }

    public String getTipoAdquisicion() {
        return tipoAdquisicion;
    }

    public void setTipoAdquisicion(String tipoAdquisicion) {
        this.tipoAdquisicion = tipoAdquisicion;
    }

    public boolean getAprobado() {
        return aprobado;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFechaRequisicion() {
        return fechaRequisicion;
    }

    public void setFechaRequisicion(Date fechaRequisicion) {
        this.fechaRequisicion = fechaRequisicion;
    }

    public String getTipoRequisicion() {
        return tipoRequisicion;
    }

    public void setTipoRequisicion(String tipoRequisicion) {
        this.tipoRequisicion = tipoRequisicion;
    }

    @Override
    public String toString() {
        return "org.mtop.modelo.Requisicion[ id=" + getId() + " ]";
    }

}
