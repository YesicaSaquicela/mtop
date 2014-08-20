/**
 */


import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.mtop.util.StringValidations;
import org.mtop.util.UI;


@FacesValidator("stringValidator")
@RequestScoped
public class StringValidator implements Validator {
    @Override
    public void validate(final FacesContext context, final UIComponent component, final Object value)
            throws ValidatorException {
        String field = value.toString();        
        if (!StringValidations.isPunctuatedTextUTF8(field) ) {
            FacesMessage msg = new FacesMessage(UI.getMessages("Este texto no anda bien"), UI.getMessages("Evite usar caráteres extraños en nombres y otra información corta"));
            throw new ValidatorException(msg);
        }else if(StringValidations.containsDecimal(field)){
            FacesMessage msg = new FacesMessage(UI.getMessages("No se admiten números"));
            throw new ValidatorException(msg);
        }
    }
}
