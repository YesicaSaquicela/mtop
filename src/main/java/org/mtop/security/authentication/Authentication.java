/**
 *
 */
package org.mtop.security.authentication;

import org.mtop.cdi.Web;
import org.mtop.modelo.profile.Profile;
import org.mtop.modelo.security.IdentityObjectAttribute;
import org.mtop.modelo.security.IdentityObjectCredential;
import org.mtop.profile.ProfileService;
import java.io.IOException;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.mtop.util.UI;
import javax.persistence.EntityManager;
import org.jboss.seam.international.status.Messages;
import org.jboss.seam.security.Authenticator.AuthenticationStatus;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.events.DeferredAuthenticationEvent;
import org.jboss.seam.security.events.LoggedInEvent;
import org.jboss.seam.security.events.LoginFailedEvent;
import org.jboss.seam.security.external.openid.OpenIdAuthenticator;
import org.jboss.seam.security.management.IdmAuthenticator;
import org.ocpsoft.logging.Logger;
import org.ocpsoft.rewrite.servlet.http.event.HttpInboundServletRewrite;
import org.ocpsoft.rewrite.servlet.impl.HttpInboundRewriteImpl;
import org.picketlink.idm.api.Credential;
import org.picketlink.idm.api.User;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.faces.application.FacesMessage;
import javax.servlet.ServletContext;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jboss.seam.security.Authenticator;
import org.jboss.seam.security.external.api.ResponseHolder;
import org.jboss.seam.security.external.openid.api.OpenIdPrincipal;
import org.picketlink.idm.api.AttributesManager;
import org.picketlink.idm.api.IdentitySession;
import org.picketlink.idm.api.PersistenceManager;
import org.picketlink.idm.common.exception.IdentityException;
import org.picketlink.idm.impl.api.PasswordCredential;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Named
@RequestScoped
public class Authentication {

    private static org.jboss.solder.logging.Logger log = org.jboss.solder.logging.Logger.getLogger(Authentication.class);
    Logger logger = Logger.getLogger(Authentication.class);
    @Inject
    private HttpSession session;
    @Inject
    private ServletContext servletContext;

    @Inject
    private FacesContext context;
    @Inject
    private Credentials credencials;

    @Inject
    private Identity identity;
    @Inject
    private IdentitySession security;
    @Inject
    private Messages messages;
    @Inject
    @Web
    private EntityManager em;
    @Inject
    private ProfileService profileService;

    private IdmAuthenticator idmAuth;
    @Inject
    private OpenIdAuthenticator openAuth;
    @Inject
    Event<DeferredAuthenticationEvent> deferredAuthentication;

    @PostConstruct
    public void init() {

        profileService.setEntityManager(em);

    }

    public void loginSuccess(@Observes final LoggedInEvent event, final NavigationHandler navigation,
            final FacesContext context,
            final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        User user = event.getUser();
        //logger.info("User logged in [{}, {}]", user.getId(), user.getKey());

        String viewId = context.getViewRoot().getViewId();
        //logger.info("viewId [{}]", viewId);

        if (!"/paginas/signup.xhtml".equals(viewId) && !"/paginas/login.xhtml".equals(viewId) && !"/paginas/reset.xhtml".equals(viewId)) {
            // TODO need a better way to navigate: this doesn't work with AJAX requests
            HttpInboundServletRewrite rewrite = new HttpInboundRewriteImpl(request, response);

            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", rewrite.getContextPath() + rewrite.getURL());
            response.flushBuffer();
            //return;
        } else {
            //Configurar si es usuario de perfil o usuario de paciente...  
            //log.info("Nombre usuario: " + credencials.getUsername());
//            pacienteServic.setEntityManager(em);
//            if (pacienteServic.getPacientePorIdentityKey(identity.getUser().getKey()) != null) {
//                String result = "/pages/home.xhtml";
//                navigation.handleNavigation(context, null, result + "?faces-redirect=true");
//            } else {
//            }
            String result = "/paginas/inicio.xhtml";
            navigation.handleNavigation(context, null, result + "?faces-redirect=true");
        }
    }

    /*
     * This is called outside of the JSF lifecycle.
     */
    public void openLoginSuccess(@Observes final DeferredAuthenticationEvent event, final NavigationHandler navigation) {
        if (event.isSuccess()) {
            logger.info("User logged in with OpenID");
        } else {
            logger.info("User failed to login via OpenID, potentially due to cancellation");
        }
    }

    public void loginSucceeded(OpenIdPrincipal principal, ResponseHolder responseHolder) {

        try {
            openAuth.success(principal);
            deferredAuthentication.fire(new DeferredAuthenticationEvent(true));
            responseHolder.getResponse().sendRedirect(servletContext.getContextPath() + "/pages/home.xhtml");
        } catch (IOException e) {

            throw new RuntimeException(e);

        }

    }

    public void loginFailed(@Observes final LoginFailedEvent event, final NavigationHandler navigation)
            throws InterruptedException {
        if (!(OpenIdAuthenticator.class.equals(identity.getAuthenticatorClass())
                && AuthenticationStatus.DEFERRED.equals(openAuth.getStatus()))) {
            Exception exception = event.getLoginException();
            if (exception != null) {
                logger.error(
                        "Login failed due to exception" + identity.getAuthenticatorName() + ", "
                        + identity.getAuthenticatorClass()
                        + ", " + identity); // TODO , exception );
                //messages.warn("Whoops! Something went wrong with your login. Care to try again? We'll try to figure out what went wrong.");
                //messages.warn(UI.getMessages("common.login.fail"));

            } else {
                //messages.warn("Whoops! We don't recognize that username or password. Care to try again?");
                messages.warn(UI.getMessages("common.login.bad.usernamepassword"));
            }
            Thread.sleep(50);
            navigation.handleNavigation(context, null, "/paginas/login?faces-redirect=true");
        }
    }

    public void login() throws InterruptedException {
        identity.setAuthenticatorClass(IdmAuthenticator.class);
        try {
            identity.login();
        } catch (Exception e) {
            //identity.tryLogin();
            identity.login();
        }
    }

    public void login1() throws InterruptedException, IdentityException {
        identity.setAuthenticatorClass(IdmAuthenticator.class);
        PersistenceManager identityManager = security.getPersistenceManager();

        User user = identityManager.findUser(credencials.getUsername());
        System.out.println("usuario" + user);
        if (user != null) {
            IdentityObjectAttribute ida = profileService.getAttributos(user.getKey(), "estado").get(0);
            //Profile userP = profileService.getProfileByUsername(credencials.getUsername());
            //Paciente p = pacienteServic.getPacientePorNombreUsuario(credencials.getUsername());
            if (ida != null && "ACTIVO".equals(ida.getValue())) {
                System.out.println("entro a loguear");
                //try {
                this.login();
//                } catch (Exception ex) {
//                    System.out.println("ERROR_");
//                    String pass = ((PasswordCredential) credencials.getCredential()).getValue();
////                    boolean autenticacion = conexionSGA.autenticarUsuariosWSSGA(credencials.getUsername(), pass);
//                    //System.out.println("autenticado sga  " + autenticacion);
////                    if (autenticacion) {
////                        this.actualizarPass(pass, user);
////                        //identity.login();
////                        System.out.println("Ingreso");
////                    }
//                    this.login();
//                }
            } else {
                System.out.println(" no entro a loguear");
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "! El usuario ", "esta inactivo o no existe¡");
                FacesContext.getCurrentInstance().addMessage("", msg);
              //  messages.warn("!El usuario esta inactivo o no existe¡");  //los datos no son correctos                    
            }

        } else {
            System.out.println("entro a us no correcto");
//            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "! Nombre de usuario  ", "o contraseña incorrectos. Intentelo de nuevo¡");
//            FacesContext.getCurrentInstance().addMessage("", msg);
            messages.warn(UI.getMessages("¡Nombre de usuario o contraseña incorrectos. Intentelo de nuevo1!"));
        }
        //buscar   /*
    }

    public String logout() {
        identity.setAuthenticatorClass(IdmAuthenticator.class);
        identity.logout();
        //session.invalidate();
        return "/paginas/loggedOffHome.xhtml?faces-redirect=true";
    }

    public String actualizarPass(String pass, User user) throws InterruptedException {
        try {
            //IdentityObjectCredential credent = profileService.getCredencial(user.getKey()).get(0);
            System.out.println("cambio password  " + user.getKey());
            //credent.setValue(pass);            
            //em.merge(credent);
            AttributesManager attributesManager = security.getAttributesManager();
            attributesManager.updatePassword(user, user.getKey());
            System.out.println("cambio password 1 " + pass);
            credencials.setUsername(user.getKey());
            credencials.setCredential(new PasswordCredential(pass));
            idmAuth.setStatus(Authenticator.AuthenticationStatus.FAILURE);
            identity.setAuthenticatorClass(IdmAuthenticator.class);
            System.out.println("cambio password con exito 1");
            String result = identity.login();
            if (Identity.RESPONSE_LOGIN_EXCEPTION.equals(result)) {
                result = identity.login();
            }
            //regresa a homeee
            return "/paginas/inicio.xhtml?faces-redirect=true";
        } catch (IdentityException ex) {
            System.out.println("Error____");
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(Authentication.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
