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


package org.mtop.faces.validator;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;


/**
 *
 * @author carla
 * @author yesica
 * 
 */

@RequestScoped
@FacesValidator("validarLista")
public class ValidarLista implements Validator {

    

    @Override
    public void validate(final FacesContext context, final UIComponent comp, final Object value)
            throws ValidatorException {
        System.out.println("validador validadLista");
        //
        String field = value.toString();
       
        System.out.println("field>>>" + field.matches("^,()*"));
        if (!field.matches("([a-zA-Z]+|[0-9]+)+(,([a-zA-Z]+|[0-9]+)+)*")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No puede tener comas seguidas, ni iniciar y terminar en coma ", " ");

            throw new ValidatorException(msg);
        } 

    }
}
