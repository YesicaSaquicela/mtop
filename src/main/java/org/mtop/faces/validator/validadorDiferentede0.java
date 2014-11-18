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

/**
 *
 * @author carla
 * @author yesica
 * 
 */

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.jboss.seam.faces.validation.InputElement;
import org.mtop.util.StringValidations;
import org.mtop.util.UI;

@FacesValidator("validadorDiferentede0")
@RequestScoped
public class validadorDiferentede0 implements Validator {

   
    @Override
    public void validate(final FacesContext context, final UIComponent component, final Object value)
            throws ValidatorException {
        System.out.println("\n\n\n\n entro al validador diferente de 0\n\n\n\n" + value);
        Integer field = value.hashCode();
        System.out.println("\n\n\n\n entro al validador diferente de 0\n\n\n\n" + field);
        if (field.equals(0)) {
            System.out.println("\n\n\n si valida\n\n\n\n");
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR! Valor no puede ser 0", "");

            throw new ValidatorException(msg);
        }

    }
}
