/*
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.io.Serializable;
import java.util.Calendar;
import static java.util.Calendar.getInstance;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.mail.*;

import java.util.List;
import java.util.Map;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jboss.seam.international.status.Messages;
import org.jboss.seam.security.Attribute;
import org.jboss.seam.security.Authenticator;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.external.openid.OpenIdAuthenticator;
import org.jboss.seam.security.management.IdmAuthenticator;
import org.jboss.seam.transaction.Transactional;
import org.mtop.cdi.Current;
import org.mtop.cdi.Web;
import org.mtop.controlador.dinamico.BussinesEntityHome;
import org.mtop.modelo.dinamico.BussinesEntity;
import org.mtop.modelo.dinamico.Group;
import org.mtop.modelo.profile.Profile;
import org.mtop.modelo.security.IdentityObjectAttribute;
import org.mtop.profile.ProfileService;
import org.mtop.security.authorization.SecurityRules;
import org.mtop.util.Dates;
import org.mtop.web.ParamsBean;
import org.picketlink.idm.api.AttributesManager;
import org.picketlink.idm.api.IdentitySession;
import org.picketlink.idm.api.IdentitySessionFactory;
import org.picketlink.idm.api.PersistenceManager;
import org.picketlink.idm.api.User;
import org.picketlink.idm.common.exception.IdentityException;
import org.picketlink.idm.impl.api.PasswordCredential;

//import org.picketlink.idm.impl.api.model.SimpleUser;
import org.primefaces.component.commandbutton.CommandButton;

/**
 *
 * @author jlgranda
 */
@Named
@ViewScoped
public class ProfileHome extends BussinesEntityHome<Profile> implements Serializable {

    private static final long serialVersionUID = 7632987414391869389L;
    private static org.jboss.solder.logging.Logger log = org.jboss.solder.logging.Logger.getLogger(ProfileHome.class);
    @Inject
    @Web
    private EntityManager em;
    private Messages msg;
    @Inject
    private Identity identity;
    @Inject
    private Credentials credentials;
    @Inject
    private IdentitySession security;
    @Inject
    private OpenIdAuthenticator oidAuth;
    @Inject
    private IdmAuthenticator idmAuth;
    @Inject
    private SecurityGroupService securityRol;
    private String password;
    private String passwordConfirm;
    private String structureName;
    @Inject
    private ParamsBean params;
    @Inject
    private ProfileService ps;
    @Inject
    private IdentitySessionFactory identitySessionFactory;

    public Long getProfileId() {
        System.out.println("obtiene objeto::::::::::::: "+getInstance().getFirstname());
        return (Long) getId();
         
    }

    public void setProfileId(Long profileId) {
        setId(profileId);
        System.out.println("obtiene objeto::::::::::::: "+getInstance().getFirstname());
    }

    public String getStructureName() {
        return structureName;
    }

    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    @Override
    protected Profile createInstance() {
        Date now = Calendar.getInstance().getTime();
        Profile profile = new Profile();
        profile.setCreatedOn(now);
        profile.setLastUpdate(now);
        profile.setActivationTime(now);
        profile.setExpirationTime(Dates.addDays(now, 364));
        profile.setResponsable(null); //Establecer al usuario actual
        //profile.buildAttributes(bussinesEntityService);
        return profile;
    }

    @Produces
    @Named("profile")
    @Current
    @TransactionAttribute
    public Profile load() {
        if (isIdDefined()) {
            wire();
        } else if (this.instance == null) {

            if (identity.isLoggedIn() && !(identity.inGroup(SecurityRules.ADMIN, "GROUP") || "admin".contains(SecurityRules.getUsername(identity)))) {
                setInstance(ps.getProfileByUsername(identity.getUser().getKey()));
            } else if (identity.isLoggedIn() && (identity.inGroup(SecurityRules.ADMIN, "GROUP") || "admin".contains(SecurityRules.getUsername(identity)))) {
                setInstance(createInstance());
            }
        }
        return getInstance();
    }

    @TransactionAttribute
    public void wire() {
        getInstance();
    }

    public boolean isWired() {
        return true;
    }

    public Profile getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

    @PostConstruct
    public void init() {
        setEntityManager(em);
        bussinesEntityService.setEntityManager(em);
        ps.setEntityManager(em);
        securityRol.setSecurity(security);

    }

    @Override
    public Class<Profile> getEntityClass() {
        return Profile.class;
    }

    @TransactionAttribute
    public String register() throws IdentityException {
        createUser();
        //BasicPasswordEncryptor().encryptPassword(password)
        credentials.setUsername(getInstance().getUsername());
        //credentials.getCredential().
        credentials.setCredential(new PasswordCredential(getPassword()));
        oidAuth.setStatus(Authenticator.AuthenticationStatus.FAILURE);
        identity.setAuthenticatorClass(IdmAuthenticator.class);

        /*
         * Try twice to work around some state bug in Seam Security
         * TODO file issue in seam security
         */
        String result = identity.login();
        if (Identity.RESPONSE_LOGIN_EXCEPTION.equals(result)) {
            result = identity.login();
        }

        return result;
    }

    //Enviar mensajes al correo
    public String sendEmail() {
        try {
            setInstance(ps.getProfileByEmail(getInstance().getEmail()));
            //return "/pages/reset?faces-redirect=true&profileId=" + getInstance().getId();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "La dirección de correo electrónico introducida no está asociada a ningún usuario. ", ""));
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            //return "/pages/recover";
        }
        String mailTO = "ynsaquicelac@unl.edu.ec";
        String mailFrom = getInstance().getEmail();
        String host = "localhost";
        Properties props = new Properties();
        //props = System.getProperties();
        props.setProperty("mail.smtp.host", "587");
        /*props.put("mail.smtp.auth", "true");
         props.put("mail.smtp.starttls.enable", "true");
         props.put("mail.smtp.host", "smtp.gmail.com");
         props.put("mail.smtp.port", "587");*/
        Session session = Session.getDefaultInstance(props);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailFrom));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTO));
            message.setSubject("Mensaje de recuperación de contraseña");
            message.setContent("Este mensaje se envio para verificar el cambio de contraseña"
                    + "<br/> acceda desde este link"
                    + "<br/>  http://localhost:8080/mtop/paginas/reset?faces-redirect=true&profileId=" + getInstance().getId(),
                    "text/html");
            Transport.send(message);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Se envio un mensaje de verificación a su correo electronico",
                            "¡revise por favor!"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al solicitar información de su correo electronico");
        }

        return null;
    }

//    public void sendEmail1() {
//        try {
//            setInstance(ps.getProfileByEmail(getInstance().getEmail()));
//            System.out.println("Encontro usuario______________" + getInstance().toString());
//            if (getInstance().isPersistent()) {
//                HtmlEmail email = new HtmlEmail();
//                String mailTO = "sgssalud@gmail.com";
//                String mailFrom = getInstance().getEmail();
//                email.setHostName("mail.smtp.host");
//                email.addTo(mailTO, "Sgssalud Soporte Técnico");
//                email.setFrom(mailFrom, getInstance().getFullName());
//                email.setSubject("Mensaje de recuperación de contraseña");
//                email.setContent("Este mensaje se envio para verificar el cambio de contraseña"
//                        + "<br/> acceda desde este link"
//                        + "<br/>  http://localhost:8080/Sgssalud/pages/reset?faces-redirect=true&profileId=" + getInstance().getId(),
//                        "text/html");
//                email.send();
//            } else {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "La dirección de correo electrónico introducida no está asociada a ningún usuario. ", ""));
//                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("Error_______ de envio");
//        }
//
//    }

    //TODO- Revisar implementación de envío de mensaje para cambio de contraseña
    public void activateButtonByEmail() {
        FacesContext fc = FacesContext.getCurrentInstance();
        UIViewRoot uiViewRoot = fc.getViewRoot();

        CommandButton commandButton = (CommandButton) uiViewRoot.findComponent("form:save");

        try {
            setInstance(ps.getProfileByEmail(getInstance().getEmail()));
            commandButton.setStyleClass("btn primary");
            commandButton.setDisabled(false);

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "La dirección de correo electrónico introducida no está asociada a ningún usuario. ", ""));
            commandButton.setStyleClass("btn");
            commandButton.setDisabled(true);
        }

    }

    @TransactionAttribute
    public String changePassword() throws IdentityException, InterruptedException {
        PersistenceManager identityManager = security.getPersistenceManager();
        User user = identityManager.findUser(getInstance().getUsername());
        AttributesManager attributesManager = security.getAttributesManager();
        attributesManager.updatePassword(user, getPassword());
        getInstance().setPassword(getPassword());
        save(getInstance());

        em.flush();
        credentials.setUsername(getInstance().getUsername());
        credentials.setCredential(new PasswordCredential(getPassword()));
        oidAuth.setStatus(Authenticator.AuthenticationStatus.FAILURE);
        identity.setAuthenticatorClass(IdmAuthenticator.class);
//        String result = identity.login();
//        if (Identity.RESPONSE_LOGIN_EXCEPTION.equals(result)) {
//            result = identity.login();
//        }
        return "/paginas/inicio.xhtml?faces-redirect=true";
    }

    @TransactionAttribute
    private void createUser() throws IdentityException {
        // TODO validate username, email address, and user existence
        PersistenceManager identityManager = security.getPersistenceManager();
        User user = identityManager.createUser(getInstance().getUsername());

        AttributesManager attributesManager = security.getAttributesManager();
        PasswordCredential p = new PasswordCredential(getPassword());
        attributesManager.updatePassword(user, p.getValue());
        
        attributesManager.addAttribute(user, "email", getInstance().getEmail());  //me permite agregar un atributo de cualquier tipo a un usuario 
        attributesManager.addAttribute(user, "estado", "ACTIVO");
//Attribute att = attributesManager.

        em.flush();

        // TODO figure out a good pattern for this...
        //setInstance(createInstance());
        //getInstance().setEmail(email);
        getInstance().setPassword(getPassword());
        getInstance().getIdentityKeys().add(user.getKey());
        getInstance().setUsernameConfirmed(true);
        getInstance().setShowBootcamp(true);
        create(getInstance()); //
        setProfileId(getInstance().getId());
        wire();
        getInstance().setName(getInstance().getUsername()); //Para referencia
        getInstance().setType(bussinesEntityService.findBussinesEntityTypeByName(Profile.class
                .getName()));
        getInstance()
                .buildAttributes(bussinesEntityService);
        save(getInstance()); //Actualizar estructura de datos

    }

    @TransactionAttribute
    public String saveAjax() {
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        /*for (BussinesEntityAttribute a : getInstance().getAttributes()) {
         if (a.getProperty().getType().equals("java.lang.String") && a.getValue() == null) {
         a.setValue(a.getName());
         a.setStringValue(a.getName());
         }
         }*/
        save(getInstance());
        return "/paginas/admin/profile/view?faces-redirect=true&profileId=" + getProfileId();
    }

    @TransactionAttribute
    public String saveProfile() {
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        String salida = "/paginas" + getBackView() + "?faces-redirect=true";
        if (getInstance().isPersistent()) {
            try {
                changeEmail();
            } catch (IdentityException ex) {
                Logger.getLogger(ProfileHome.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
            save(getInstance());
        } else {
            try {
                register();
            } catch (IdentityException ex) {
                ex.printStackTrace();
                Logger.getLogger(ProfileHome.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        if ("/admin/listProfile".equalsIgnoreCase(getBackView())) {
            return "/paginas/admin/security/authorization.xhtml?faces-redirect=true&username=" + getInstance().getUsername() + "&backView=" + getBackView();
        } else {
            return salida;
        }
    }

    public void changeEmail() throws IdentityException {

        PersistenceManager identityManager = security.getPersistenceManager();
        //AttributesManager attributesManager = security.getAttributesManager();
        User user = identityManager.findUser(getInstance().getUsername());
        IdentityObjectAttribute ida = ps.getAttributos(user.getKey(), "email").get(0);
        ida.setValue(getInstance().getEmail());
        //a.setValue(getInstance().getEmail());        
//        Attribute[] ats = {a};
//        attributesManager.updateAttributes(user, ats);        
        /*org.picketlink.idm.model.basic.User us = (org.picketlink.idm.model.basic.User) identityManager.findUser(getInstance().getUsername());
         us.setEmail(getInstance().getEmail());                
         identityM = (IdentityManager) new PersistenceManagerImpl(null);
         identityM.update(us);*/
        em.merge(ida);
        em.flush();
        System.out.println("ACTUALIZO CORREO ____________");
    }

    @TransactionAttribute
    public void displayBootcampAjax() {
        getInstance().setShowBootcamp(true);
        update();
    }

    @TransactionAttribute
    public void dismissBootcampAjax() {
        getInstance().setShowBootcamp(false);
        update();
    }

    public void commuteEdition() {
        setEditionEnabled(!isEditionEnabled());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Edición activa " + isEditionEnabled(), null));

    }

    @Transactional
    public void addBussinesEntity(Group group) {
        Date now = Calendar.getInstance().getTime();
        String name = "Nuevo " + (group.getProperty() != null ? group.getProperty().getLabel() : "elemento") + " " + (group.findOtherMembers(this.getInstance()).size() + 1);
        BussinesEntity entity = new BussinesEntity();
        entity.setName(name);
        //TODO implementar generador de códigos para entidad de negocio
        entity.setCode((group.getProperty() != null ? group.getProperty().getLabel() : "elemento") + " " + (group.findOtherMembers(this.getInstance()).size() + 1));
        entity.setCreatedOn(now);
        entity.setLastUpdate(now);
        entity.setActivationTime(now);
        entity.setExpirationTime(Dates.addDays(now, 364));
        entity.setResponsable(null); //Establecer al usuario actual
        entity.buildAttributes(bussinesEntityService);
        //Set default values into dinamycs properties
        //TODO idear un mecanismo generico de inicialización de variables dinamicas
        //entity.getBussinessEntityAttribute("title").setValue(name);

        group.add(entity);

        setBussinesEntity(entity); //Establecer para edición
    }

    @Transactional
    public void saveBussinesEntity() {

        try {
            if (getBussinesEntity() == null) {
                throw new NullPointerException("bussinessEntity is null");
            }

            if (getBussinesEntity().getName().equalsIgnoreCase("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No name", null));
            } else {
                save(getBussinesEntity());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Se actualizó con exito " + bussinesEntity.getName(), ""));
            }

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRORE", e.toString()));
        }
    }

    @TransactionAttribute
    public String inavilitarCuenta() {
        String salida = null;
        try {
            PersistenceManager identityManager = security.getPersistenceManager();
            AttributesManager attributesManager = security.getAttributesManager();
            User user = identityManager.findUser(getInstance().getUsername());
            //System.out.println("PROFILE_________" + getSelectedProfile().getName());
            //SecurityRules s = new SecurityRules();
            System.out.println("USUARIO ");
            if (!identity.getUser().getKey().equals(user.getKey())) {
                IdentityObjectAttribute ida = ps.getAttributos(getInstance().getUsername(), "estado").get(0);
                //securityRol.disassociate(getInstance().getUsername());
                ida.setValue("INACTIVO");
                em.merge(ida);
                em.flush();
                getInstance().setDeleted(true);
                save(getInstance());
                FacesMessage msg = new FacesMessage("EL Usuario: " + getInstance().getFullName(), "ha sido deshabilitado");
                FacesContext.getCurrentInstance().addMessage("", msg);
                salida = "/paginas" + getBackView() + "?faces-redirect=true";
            } else {
                FacesMessage msg = new FacesMessage("No se puede deshabilitar este usuario", "el usuario esta logeado");
                FacesContext.getCurrentInstance().addMessage("", msg);
            }

        } catch (IdentityException ex) {

        }
        return salida;
    }

    @TransactionAttribute
    public String activarCuenta() {
        try {

            IdentityObjectAttribute ida = ps.getAttributos(getInstance().getUsername(), "estado").get(0);
            //securityRol.disassociate(getInstance().getUsername());
            ida.setValue("ACTIVO");
            em.merge(ida);
            em.flush();
            getInstance().setDeleted(false);
            save(getInstance());
            FacesMessage msg = new FacesMessage("EL Usuario: " + getInstance().getFullName(), "ha sido deshabilitado");
            FacesContext.getCurrentInstance().addMessage("", msg);
            System.out.println("PROFILE_________2");
        } catch (IdentityException ex) {

        }
        return "/paginas" + getBackView() + "?faces-redirect=true";
    }
}
