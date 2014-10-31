/*
 * Copyright 2014 yesica.
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
