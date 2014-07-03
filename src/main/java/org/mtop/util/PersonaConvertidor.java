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
import javax.inject.Inject;
import javax.persistence.NoResultException;
import org.mtop.model.Persona;

import org.mtop.service.ServicioGenerico;


/**
 *
 * @author jesica
 */




@FacesConverter("personaConverter")
@RequestScoped

public class PersonaConvertidor implements Converter {

    @Inject //para poder utilizar los servicios 
    private ServicioGenerico servGen;

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        System.out.println("valor "+value);
        if (value != null && !value.isEmpty()) {
            try {
                return servGen.buscarPorId(Persona.class, getKey(value));
            } catch (NoResultException e) {
                
                return new Persona();
            }
        }
        return null;
    }

    private Long getKey(String value) {
        //get id value from string
        //System.out.println("eqaula--> Account Converter " + value);
        int start = value.indexOf("id=");
        int end = value.indexOf(",") == -1 ? value.indexOf("]") : value.indexOf(",");
        System.out.println("iddepersona"+value.substring(start + 3, end).trim());
        return Long.valueOf(value.substring(start + 3, end).trim());
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object value) {
        if (value != null) {
            return value.toString();
        } else {
            return null;
        }
    }

}
