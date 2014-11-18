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

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.mtop.controlador.dinamico.PropertyHome;
import org.mtop.modelo.dinamico.Property;

/**
 *
 * @author carla
 * @author yesica
 * 
 */

@FacesValidator("validarNomPropiedad")
public class ValidarNomPropiedad implements Validator {

    @Inject
    private EntityManager em;

    @Inject
    private PropertyHome ph;

    @Inject
    private Property propiedad;

    @Override
    public void validate(final FacesContext context, final UIComponent comp, final Object value)
            throws ValidatorException {
        System.out.println("validador nombrePropiedad"+value);
        //

        if (value instanceof String && !value.equals(propiedad.getName())) {
            ph.setEntityManager(em);
            System.out.println("entro>> validador"+propiedad.getName());
            if (!ph.nombreUnico((String) value)) {
                System.out.println("entroa presentar mensjae");
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "El nombre de la propiedad no está disponible. ¡Pertenece a otra propiedad! ", " ");

                throw new ValidatorException(msg);
            }
        }

    }
}
