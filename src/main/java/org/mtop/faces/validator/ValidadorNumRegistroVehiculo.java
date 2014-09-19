/*
 * Copyright 2014 carlis.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import org.mtop.util.UI;

/**
 *
 * @author carlis
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
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "El número de registro ingresado no esta disponible. ¡Pertenece a otro vehículo! ", " ");

                    throw new ValidatorException(msg);
                }
            }
        }

    }
}
