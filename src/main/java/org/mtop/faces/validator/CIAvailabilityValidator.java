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


package org.mtop.faces.validator;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.mtop.cdi.Current;
import org.mtop.cdi.Web;
import org.mtop.modelo.profile.Profile;
import org.mtop.profile.ProfileService;
import org.mtop.util.UI;


/**
 *
 * @author yesica
 * @author carla
 */
@RequestScoped
@FacesValidator("ciAvailabilityValidator")
public class CIAvailabilityValidator implements Validator {

    @Inject
    @Web
    private EntityManager em;
    @Inject
    private ProfileService ps;
    @Inject
    @Current
    private Profile profile;

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object value)
            throws ValidatorException {
        ps.setEntityManager(em);
        String currentDni = "";
        System.out.println("Perfil de usuario es: " + profile.toString());
        if (profile.isPersistent()) {
            currentDni = ps.find(profile.getId()).getCedula();
            System.out.println("el dni es: " + currentDni);
        }
        if (currentDni != null) {
            if (!currentDni.equals(value)) {

                if (value instanceof String) {
                    ps.setEntityManager(em);
                    System.out.println("Número de cédula ingresada es diferente al valor en la base de datos: ");

                    if (!ps.isDniAviable((String) value)) {
                        throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_WARN, UI.getMessages("validator.dni"), null));
                    }
                }
            }
        }


    }
}
