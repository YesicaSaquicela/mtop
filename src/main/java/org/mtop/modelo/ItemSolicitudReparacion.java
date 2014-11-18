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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "ItemSolicitudReparacion")
@DiscriminatorValue(value = "isk")//valor que se discrimina por cada clase
@PrimaryKeyJoinColumn(name = "id")//representa el id de la superclase
public class ItemSolicitudReparacion extends BussinesEntity implements Serializable{
    private String descripcionElementoRevisar;
    private String descripcionFalla;
    @ManyToOne()
    @JoinColumn(name = "solicitudReparacionId")
    private SolicitudReparacionMantenimiento solicitudReparacion;


    public String getDescripcionElementoRevisar() {
        return descripcionElementoRevisar;
    }

    public void setDescripcionElementoRevisar(String descripcionElementoRevisar) {
        this.descripcionElementoRevisar = descripcionElementoRevisar;
    }

    public String getDescripcionFalla() {
        return descripcionFalla;
    }

    public void setDescripcionFalla(String descripcionFalla) {
        this.descripcionFalla = descripcionFalla;
    }

    public SolicitudReparacionMantenimiento getSolicitudReparacion() {
        return solicitudReparacion;
    }

    public void setSolicitudReparacion(SolicitudReparacionMantenimiento solicitudReparacion) {
        this.solicitudReparacion = solicitudReparacion;
    }
    


    @Override
    public String toString() {
        return "org.mtop.modelo.ItemSolicicitudReparacion[ id=" + getId() + " ]";
    }
    
}
