/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mtop.data;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.mtop.model.EntidadAbstracta_;
import org.mtop.model.Persona;
import org.mtop.model.Persona_;
import org.mtop.model.PlanMantenimiento;
import org.mtop.model.PlanMantenimiento_;
import org.mtop.model.Vehiculo;
import org.mtop.model.Vehiculo_;
import org.mtop.service.ServicioGenerico;

/**
 *
 * @author carlis
 */

@Named
@RequestScoped
public class ListaPlanMantenimiento {
   
  @Inject //utilizar los dtaos de otra clase
    private ServicioGenerico sg;
    private List<PlanMantenimiento> planMantenimiento;    
    
    public List<PlanMantenimiento> getPlanMantenimiento() {
        return planMantenimiento;
    }

    public void setPlanMantenimiento(List<PlanMantenimiento> planMantenimiento) {
          this.planMantenimiento = planMantenimiento;
       
    }
    
    
 @PostConstruct
    public void init(){
        planMantenimiento=sg.buscarTodos(PlanMantenimiento.class, PlanMantenimiento_.estado.getName());
    }
    
}
