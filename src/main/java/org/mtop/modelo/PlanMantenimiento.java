/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mtop.modelo;

import java.io.Serializable;
import java.util.ArrayList;
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
import org.mtop.modelo.dinamico.BussinesEntity;
import org.mtop.modelo.Vehiculo;

/**
 *
 * @author jesica
 */

@Entity
@Table(name = "PlanMantenimiento")
@DiscriminatorValue(value = "pmk")//valor que se discrimina por cada clase
@PrimaryKeyJoinColumn(name = "id")//representa el id de la superclase
public class PlanMantenimiento extends BussinesEntity implements Serializable {

    private long registro;

//    @ManyToOne
//    @JoinColumn(name = "vehiculoId")
//    private Vehiculo vehiculo;
    @OneToMany(mappedBy = "planMantenimiento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ActividadPlanMantenimiento> listaActividadpm=new ArrayList<ActividadPlanMantenimiento>();


    public List<ActividadPlanMantenimiento> getListaActividadpm() {
        return listaActividadpm;
    }

    public void setListaActividadpm(List<ActividadPlanMantenimiento> listaActividadpm) {
        
         for (ActividadPlanMantenimiento actividadpm : listaActividadpm) {
            actividadpm.setPlanMantenimiento(this);
        }
      
        this.listaActividadpm = listaActividadpm;
    }

    public long getRegistro() {
        return registro;
    }

    public void setRegistro(long registro) {
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
