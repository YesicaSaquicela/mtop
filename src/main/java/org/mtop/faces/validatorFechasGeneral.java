/**
 */

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.Calendar;
import java.util.Date;
import javax.inject.Inject;
import org.jboss.seam.faces.validation.InputElement;
import org.mtop.util.UI;

@FacesValidator("validadorFecha")
@RequestScoped
public class validatorFechasGeneral implements Validator {
    
    @Inject
    private InputElement<Date> fechaI;
    
    @Override
    public void validate(final FacesContext context, final UIComponent component, final Object value)
            throws ValidatorException {
        String field = value.toString();
        Date now = new Date();
        Date fecha = fechaI.getValue();
        System.out.println("FECHAS  " + fecha + " _______" + now);
        if (fecha.before(now)) {
            FacesMessage msg = new FacesMessage(UI.getMessages("validation.date"), "La fecha debe ser posterior a la fecha actual");
            throw new ValidatorException(msg);
        }
    }
}
