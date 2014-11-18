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


import java.util.Date;
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
import org.mtop.util.UI;

/**
 *
 * @author carla
 * @author yesica
 * 
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
