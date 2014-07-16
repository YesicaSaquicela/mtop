/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mtop.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.persistence.Transient;
import org.mtop.modelo.dinamico.BussinesEntity;
import org.mtop.modelo.Vehiculo;
import org.mtop.util.FechasUtil;

/**
 *
 * @author jesica
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

//    public String getAlerta() {
//        Date now = Calendar.getInstance().getTime();
//        int dias = FechasUtil.getFechaLimite(now, fechaCaducidad);
//        if (dias >= 0 && dias < 90) {
//            return "POR CADUCARSE";
//        } else {
//            return "";
//        }
//        
//    }
//
//    public void setAlerta(String alerta) {
//        this.alerta = alerta;
//    }

   
     
     
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
