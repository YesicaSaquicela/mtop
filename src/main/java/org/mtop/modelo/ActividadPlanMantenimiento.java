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
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.mtop.modelo.dinamico.BussinesEntity;

/**
 *
 * @author carla
 * @author yesica
 * 
 */

@SuppressWarnings("serial") 
@Entity
@Table(name = "actividadPlanMantenimiento")
@DiscriminatorValue(value = "iplank")//valor que se discrimina por cada clase
@PrimaryKeyJoinColumn(name = "id")//representa el id de la superclase
public class ActividadPlanMantenimiento extends BussinesEntity implements Serializable {
    
    @NotNull
    private Integer kilometraje;
    @NotNull
    @Column(length = 1000)
    private String actividad;
    @ManyToOne
    @JoinColumn(name = "planMId")
    public PlanMantenimiento planMantenimiento=new PlanMantenimiento();
    
    public Integer getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(Integer kilometraje) {
        this.kilometraje = kilometraje;
    }

   
 

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public PlanMantenimiento getPlanMantenimiento() {
        return planMantenimiento;
    }

    public void setPlanMantenimiento(PlanMantenimiento planMantenimiento) {
        this.planMantenimiento = planMantenimiento;
    }
   


    @Override
    public String toString() {
        return "org.mtop.modelo.Actividades[ id=" + getId() + " ]";
    }
    
}
