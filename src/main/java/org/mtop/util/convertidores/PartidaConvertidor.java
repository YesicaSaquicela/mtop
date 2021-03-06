package org.mtop.util.convertidores;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.mtop.modelo.PartidaContabilidad;
import org.mtop.servicios.ServicioGenerico;


/**
 *
 * @author jesica
 * 
 */
@FacesConverter("partidaConverter")
@RequestScoped
public class PartidaConvertidor implements Converter{
    ServicioGenerico servGen;
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String valor) {
        if(valor!=null && !valor.isEmpty()){
            try {
                System.out.println("Ide Partida valor>>>>>>"+valor);
                System.out.println("Ide Partida key>>>>>>"+getKey(valor));
                return servGen.buscarPorId(PartidaContabilidad.class, getKey(valor));
            } catch (Exception e) {
                return new PartidaContabilidad();
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
