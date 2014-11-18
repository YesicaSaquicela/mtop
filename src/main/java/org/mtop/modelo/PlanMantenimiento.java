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
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.mtop.modelo.dinamico.BussinesEntity;

/**
 *
 * @author carla
 * @author yesica
 * 
 */

@Entity
@Table(name = "PlanMantenimiento")
@DiscriminatorValue(value = "pmk")//valor que se discrimina por cada clase
@PrimaryKeyJoinColumn(name = "id")//representa el id de la superclase
public class PlanMantenimiento extends BussinesEntity implements Serializable {

    private String registro;
    private Boolean activado ;
    @Transient
    private String alerta;
     @OneToMany(mappedBy = "planMantenimiento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ActividadPlanMantenimiento> listaActividadpm=new ArrayList<ActividadPlanMantenimiento>();


     
     
     public Boolean getActivado() {
        return activado;
    }

    public void setActivado(Boolean activado) {
        this.activado = activado;
    }

  


    public List<ActividadPlanMantenimiento> getListaActividadpm() {
        return listaActividadpm;
    }

    public void setListaActividadpm(List<ActividadPlanMantenimiento> listaActividadpm) {
        
         for (ActividadPlanMantenimiento actividadpm : listaActividadpm) {
            actividadpm.setPlanMantenimiento(this);
        }
      
        this.listaActividadpm = listaActividadpm;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

  
    
//    public Vehiculo getVehiculo() {
//        return vehiculo;
//    }
//
//    public void setVehiculo(Vehiculo vehiculo) {
//        this.vehiculo = vehiculo;
//    }
//    

    @Override
    public String toString() {
        return "org.mtop.modelo.PlanMantenimiento[ id=" + getId() + " ]";
    }
    
}
