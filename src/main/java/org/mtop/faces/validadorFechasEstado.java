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

package org.mtop.faces;

import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import org.jboss.seam.faces.validation.InputElement;
import org.mtop.util.UI;


/**
 *
 * @author carlis
 */
@RequestScoped
@FacesValidator("validadorFechasE")
public class validadorFechasEstado implements Validator {

    private static org.jboss.solder.logging.Logger log = org.jboss.solder.logging.Logger.getLogger(validadorFechasEstado.class);
    @Inject
    private InputElement<Date> fechainicio;
    @Inject
    private InputElement<Date> fechasalida;

    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        log.info("Ingreso a validar fechas");
        
        Date fInicio = fechainicio.getValue();
              
        log.info("fechaInicio:"+fInicio+"fecha salidasinnn "+fechasalida
        );
        Date fFinal = fechasalida.getValue();
            
        log.info("fechaInicio:"+fInicio+" fechaFinal:"+fFinal);
        if (fInicio != null && fFinal!= null) {
            log.info("Ingreso a condiciones");
            if (!fInicio.before(fFinal)) {  //metodo que compara si una fecha es anterior a la otra
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_WARN, UI.getMessages("La Fecha Final debe ser mayor a la Fecha de Inicio"), ""));
            }

        }

    }
}
