/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mtop.util;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.mtop.model.Vehiculo;
import org.mtop.service.ServicioGenerico;

/**
 *
 * @author jesica
 * 
 */
@FacesConverter("vehiculoConverter")
@RequestScoped
public class VehiculoConvertidor implements Converter{
    ServicioGenerico servGen;
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String valor) {
        if(valor!=null && !valor.isEmpty()){
            try {
                return servGen.buscarPorId(Vehiculo.class, getKey(valor));
            } catch (Exception e) {
                return new Vehiculo();
            }
            
        }
        return null;
    }
    private Long getKey(String v){
        int empieza=v.indexOf("id");
        int termina=v.indexOf(",")== -1 ? v.indexOf("]") : v.indexOf(",");
        return Long.valueOf(v.substring(empieza + 3, termina).trim());
    }
    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object valor) {
        if(valor!=null){
            return valor.toString();
        }else{
            return null;
        }
        
    }
    
}