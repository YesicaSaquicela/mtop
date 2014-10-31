/**
 * This file is part of Glue: Adhesive BRMS
 * 
* Copyright (c)2012 José Luis Granda <jlgranda@eqaula.org> (Eqaula Tecnologías
 * Cia Ltda) Copyright (c)2012 Eqaula Tecnologías Cia Ltda (http://eqaula.org)
 * 
* If you are developing and distributing open source applications under the GNU
 * General Public License (GPL), then you are free to re-distribute Glue under
 * the terms of the GPL, as follows:
 * 
* GLue is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
* Glue is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
* You should have received a copy of the GNU General Public License along with
 * Glue. If not, see <http://www.gnu.org/licenses/>.
 * 
* For individuals or entities who wish to use Glue privately, or internally,
 * the following terms do not apply:
 * 
* For OEMs, ISVs, and VARs who wish to distribute Glue with their products, or
 * host their product online, Eqaula provides flexible OEM commercial licenses.
 * 
* Optionally, Customers may choose a Commercial License. For additional
 * details, contact an Eqaula representative (sales@eqaula.org)
 */
package org.mtop.faces.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.mtop.controlador.ControladorPersona;

import org.mtop.util.StringValidations;
import org.mtop.util.UI;

@FacesValidator("passwordValidator")
@RequestScoped
public class PasswordValidator implements Validator {
      @Inject
    private EntityManager em;

    @Inject
    private ControladorPersona cp;

    @Override
    public void validate(final FacesContext context, final UIComponent component, final Object value)
            throws ValidatorException {
        System.out.println("lor en validador " + value);
          cp.setEntityManager(em);
        if (value != null) {
            if (!value.equals("")) {
                String field = value.toString();
                System.out.println("entro a diferentte d enull");
                if (value instanceof String && ((String) value).length() > 0 && !StringValidations.isPassword(field)) {
                   cp.setMensaje1("");
                    System.out.println("tiene que pesenta mensaje");
                    FacesMessage msg = new FacesMessage(UI.getMessages("common.password.validator"));
                    throw new ValidatorException(msg);
                    
                    
                }

                System.out.println("eqaula --> passwordValidator " + field);
            }

        }

    }
}
