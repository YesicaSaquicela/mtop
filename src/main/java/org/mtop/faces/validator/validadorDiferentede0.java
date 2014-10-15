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

/**
 *
 * @author yesica
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
