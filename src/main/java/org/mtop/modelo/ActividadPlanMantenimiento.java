/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author carlis
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
