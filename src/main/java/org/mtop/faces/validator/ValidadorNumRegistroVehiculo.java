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
@FacesValidator("validadorNumRegistro")
public class ValidadorNumRegistroVehiculo implements Validator {

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
        System.out.println("validador numregistro");
        //
        String field = value.toString();
        System.out.println("field>>>" + field.matches("[0-9]+(-[0-9]+)?"));
        if (!field.matches("[0-9]+(-[0-9]+)?")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se admiten letras ni guión al inicio o al final ", " ");

            throw new ValidatorException(msg);
        } else {
            if (value instanceof String && !value.equals(vehiculo.getNumRegistro()) && field.matches("[0-9-]*")) {
                cv.setEntityManager(em);
                System.out.println("entro>> validador");
                if (!cv.numRegistroUnico((String) value)) {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "El número de registro ingresado no está disponible. ¡Pertenece a otro vehículo! ", " ");

                    throw new ValidatorException(msg);
                }
            }
        }

    }
}
