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
 * @author yesica
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
