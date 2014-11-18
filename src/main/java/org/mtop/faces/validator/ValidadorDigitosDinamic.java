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


import org.jboss.seam.faces.validation.InputElement;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;

import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.mtop.controlador.dinamico.BussinesEntityTypeHome;

/**
 *
 * @author carla
 * @author yesica
 * 
 */

@RequestScoped
@FacesValidator("validadorDigitosDinamic")
public class ValidadorDigitosDinamic implements Validator{

    @Inject
    private EntityManager em;

    @Inject
    private BussinesEntityTypeHome beth;
    private InputElement<Integer> value;

    @Override
    public void validate(final FacesContext context, final UIComponent component, final Object value)
            throws ValidatorException {
        beth.setEntityManager(em);
     
        System.out.println("\n\n\n\n entro al validador del digitos dinamico \n\n\n\n" + value);
        String field = value.toString();
        System.out.println("\n\n\n\n entro al validador del digitos dinamico\n\n\n\n" + field);
        if (!field.matches("[0-9-]*")) {
            beth.setMensaje2("No se admiten letras");
            System.out.println("\n\n\n si valida\n\n\n\n");
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se admiten letras ", "No se admiten letras");

            throw new ValidatorException(msg);
        }else{
             beth.setMensaje2(null);
        }
        

    }
}
