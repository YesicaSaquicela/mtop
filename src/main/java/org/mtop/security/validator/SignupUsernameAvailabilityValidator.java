
package org.mtop.security.validator;

import org.mtop.controlador.persona.*;

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
import org.mtop.cdi.Current;
import org.mtop.cdi.LoggedIn;
import org.mtop.controlador.ControladorPersona;

import org.mtop.modelo.profile.Profile;
import org.mtop.profile.ProfileService;

@RequestScoped
@FacesValidator("signupUsernameAvailabilityValidator")
public class SignupUsernameAvailabilityValidator implements Validator {

    @Inject
    private ControladorPersona cp;

    @Inject
    private EntityManager em;

    @Inject
    private ProfileService ps;

    @Inject
    @Current
    private Profile profile;

    @Inject
    private InputElement<String> username;

    @Override
    public void validate(final FacesContext context, final UIComponent comp, final Object value)
            throws ValidatorException {
        System.out.println("username"+username);
        if (username != null) {
            String valor = username.getValue();

            ps.setEntityManager(em);
            String usuario = "";
            System.out.println("presentando valor" + valor);
            System.out.println("usuaio antes de entrar" + profile.getUsername());
            System.out.println("passwordddd" + cp.getPassword());
            if (profile.isPersistent() && cp.getPassword() != null) {

                System.out.println("entro a persisten");
                usuario = ps.find(profile.getId()).getUsername();
            }
            System.out.println("usuario validadr" + usuario);
            System.out.println("valuuueeee despues" + valor);

            if (!usuario.equals(valor)) {
                if (valor instanceof String) {
                    System.out.println("entro 2do if");
                    System.out.println("!ps.isUsernameAvailable(valor)"+!ps.isUsernameAvailable(valor));
                    System.out.println("valor.equals(profile.getUsername())"+valor.equals(profile.getUsername()));
                    if (!ps.isUsernameAvailable(valor) && !(valor.equals(profile.getUsername()))) {
                        System.out.println("no debe entrar mensjae");
                       context.addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "El nombre de usuario no esta disponible. ¡Pertenece a otro usuario!", null));
                    }

                }
            }
        }

    }
}
