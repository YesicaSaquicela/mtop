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
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.jboss.seam.faces.validation.InputElement;
import org.mtop.controlador.ControladorVehiculo;
import org.mtop.modelo.Vehiculo;

/**
 *
 * @author carla
 * @author yesica
 * 
 */

@RequestScoped
@FacesValidator("fechanio")
public class fechaanio implements Validator {

    @Inject
    private EntityManager em;

    @Inject
    private ControladorVehiculo cv;

    @Inject
    private Vehiculo vehiculo;
    private InputElement<Integer> value;

    @Override
    public void validate(final FacesContext context, final UIComponent comp, final Object value)
            throws ValidatorException {
        System.out.println(" entro validadorfa");
        String field = value.toString();
        System.out.println("field.matches(\"[0-9-]*\")" + field.matches("[0-9]*"));
        System.out.println("value" + (String) value);
        if (!value.equals("")) {
            if (field.matches("[0-9]*")) {
                if (value instanceof String && !value.equals(vehiculo.getAnioFabricacion())) {
                    cv.setEntityManager(em);
                    if (!cv.obtenerAñoV((String) value)) {
                        System.out.println("entro a presentar mensaje");

                        throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "El año ingresado se encuentra fuera de rango (1950-añoActual).", null));
                    }
                }
            } else {
                System.out.println("entroa validar letras");
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se admiten letras", null));
            }

        }

    }
}
