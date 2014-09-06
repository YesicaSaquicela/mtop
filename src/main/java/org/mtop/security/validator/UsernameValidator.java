
package org.mtop.security.validator;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.mtop.util.StringValidations;

@RequestScoped
@FacesValidator("usernameValidator")
public class UsernameValidator implements Validator
{
   @Override
   public void validate(final FacesContext context, final UIComponent comp, final Object value)
            throws ValidatorException
   {
      if (value instanceof String) {
         String username = (String) value;
          System.out.println("obtine user>>>>"+username);
         if (username.length() > 30)
            throw new ValidatorException(
                     new FacesMessage(FacesMessage.SEVERITY_ERROR,
                              "Demasido largo (máximo 30 caracteres), sólo estan permitidos " +
                                       "caracteres alfanúmericos.", null));

         if (!StringValidations.isAlphanumericDash(username))
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                     "Soló estan permitidos caracteres alfanuméricos", null));

         if (username.startsWith("-"))
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "No puede empezar con un guión",
                     null));

      }
   }
}