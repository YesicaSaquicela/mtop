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
import javax.persistence.EntityManager;
import org.jboss.seam.faces.validation.InputElement;
import org.mtop.controlador.ControladorVehiculo;
import org.mtop.modelo.Vehiculo;
import org.mtop.util.UI;

/**
 *
 * @author yesica
 */
@RequestScoped
@FacesValidator("validadorFechasSoat")
public class validadorFechaSoat implements Validator {

    private static org.jboss.solder.logging.Logger log = org.jboss.solder.logging.Logger.getLogger(validadorFechaSolicitud.class);

    @Inject
    private InputElement<Date> fechaSalida;
    @Inject
    private InputElement<Date> fechaEntrada;
    @Inject
    private EntityManager em;
    @Inject
    private ControladorVehiculo cv;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        log.info("Ingreso a validar fechas");
        cv.setEntityManager(em);
        Date fFinal = fechaSalida.getValue();
        Date fEntrada = fechaEntrada.getValue();
        if (fFinal != null || fEntrada != null) {
            log.info(" fechaFinal:" + fFinal
                    + "fecha entrada : " + fEntrada);
            if (!fEntrada.before(fFinal)) {
                cv.setMensaje1("La Fecha Final debe ser mayor a la Fecha de Inicio ");
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_WARN, UI.getMessages("ADVERTENCIA: "), "La Fecha Final debe ser mayor a la Fecha de Inicio "));

            }else
            {
                 cv.setMensaje1(null);
            }
        }

    }
}
