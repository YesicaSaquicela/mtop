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

package org.mtop.faces;

import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import org.jboss.seam.faces.validation.InputElement;
import org.mtop.util.UI;


/**
 *
 * @author carla
 * @author yesica
 * 
 */

@RequestScoped
@FacesValidator("validadorFechasE")
public class validadorFechasEstado implements Validator {

    private static org.jboss.solder.logging.Logger log = org.jboss.solder.logging.Logger.getLogger(validadorFechasEstado.class);
    @Inject
    private InputElement<Date> fechaEntrada;
    @Inject
    private InputElement<Date> fechaSalida;

    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        log.info("Ingreso a validar fechas");
        
        Date fInicio = fechaEntrada.getValue();
              
        Date fFinal = fechaSalida.getValue();
            
        log.info("fechaInicio:"+fInicio+" fechaFinal:"+fFinal);
        if (fInicio != null && fFinal!= null) {
            log.info("Ingreso a condiciones");
            if (!fInicio.before(fFinal)) {  //metodo que compara si una fecha es anterior a la otra
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_WARN, UI.getMessages("La Fecha Final debe ser mayor a la Fecha de Inicio"), ""));
            }

        }

    }
}
