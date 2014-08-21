/**
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

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.jboss.seam.faces.validation.InputElement;
import org.mtop.util.StringValidations;
import org.mtop.util.UI;

@FacesValidator("realValidator")
@RequestScoped
public class RealValidador implements Validator {

    private InputElement<Integer> value;

    @Override
    public void validate(final FacesContext context, final UIComponent component, final Object value)
            throws ValidatorException {
        System.out.println("\n\n\n\n entro al validador del real\n\n\n\n" + value);
        String field = value.toString();
        System.out.println("\n\n\n\n entro al validador del real\n\n\n\n" + field);
        if (!field.matches("[0-9]*\\.[0-9]+")) {
            System.out.println("\n\n\n si valida\n\n\n\n");
            FacesMessage msg = new FacesMessage(UI.getMessages("No se admiten letras"));
            throw new ValidatorException(msg);
        } else {
        }

    }
}
