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
import java.util.Date;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.mtop.modelo.dinamico.BussinesEntityType;
import org.mtop.service.ProfileListService;

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
    private Map<String, String> tipos = new HashMap<String, String>();
    private List<Profile> listausuarios;
    private String estado;
    private List<Profile> listausuariosInactivos;
    private String mensajei = "";
    private String tipopersona= "";
 

    public String getTipopersona() {
        return tipopersona;
    }

    public void setTipopersona(String tipopersona) {
        this.tipopersona = tipopersona;
    }
    
      

    public Boolean requerido() {
        Boolean ban = false;
        System.out.println("tipo de personas>>>>>"+tipopersona);
            if (tipopersona.equals("Natural")) {
                ban = true;
            }
        

        return ban;
    }

    public String getMensajei() {
        return mensajei;
    }

    public void setMensajei(String mensajei) {
        this.mensajei = mensajei;
    }

    public List<Profile> getListausuariosInactivos() {
        return listausuariosInactivos;

    }

    public void setListausuariosInactivos(List<Profile> listausuariosInactivos) {
        this.listausuariosInactivos = listausuariosInactivos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {

        this.estado = estado;
        if ("INACTIVO".equals(estado)) {
            listausuarios = ps.findAllA(true);
            List<Profile> lu = new ArrayList<Profile>();
            for (Profile profile : listausuarios) {
                if (profile.getUsername() != null) {
                    lu.add(profile);
                }

            }
            listausuarios = lu;
        }

    }

    public void inavilitarC(Profile inavilitar) {
        inavilitar.setEstado(false);
    }

    @TransactionAttribute
    public void inavilitarCuenta(Profile selectedProfile) {

        try {
            PersistenceManager identityManager = security.getPersistenceManager();
            AttributesManager attributesManager = security.getAttributesManager();
            User user = identityManager.findUser(selectedProfile.getUsername());
            if (!identity.getUser().getKey().equals(user.getKey())) {
                IdentityObjectAttribute ida = ps.getAttributos(selectedProfile.getUsername(), "estado").get(0);
                // IdentityObjectAttribute ida = ps.getAttributos(getInstance().getUsername(), "estado").get(0);
                //securityRol.disassociate(getInstance().getUsername());
                ida.setValue("INACTIVO");
                em.merge(ida);
                em.flush();
                selectedProfile.setDeleted(true);
                save(selectedProfile);

                System.out.println("PROFILE_________0lista antes" + listausuarios);

                List<Profile> lui = new ArrayList<Profile>();
                for (Profile profile : listausuarios) {
                    if (!selectedProfile.getId().equals(profile.getId())) {
                        lui.add(profile);
                    }
                }
                listausuarios = lui;
                FacesMessage msg = new FacesMessage("EL Usuario: " + selectedProfile.getFullName(), "ha sido deshabilitado");
                FacesContext.getCurrentInstance().addMessage("", msg);
                mensajei = "EL Usuario: " + selectedProfile.getFullName() + " ha sido deshabilitado";

            } else {
                FacesMessage msg = new FacesMessage("No se puede deshabilitar este usuario", "el usuario esta logeado");
                FacesContext.getCurrentInstance().addMessage("", msg);
                mensajei = "No se puede deshabilitar este usuario porque se encuentra logeado";
            }

        } catch (IdentityException ex) {
            ex.printStackTrace();
            System.out.println("PROFILE_________3");
            java.util.logging.Logger.getLogger(ProfileListService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<Profile> getListausuarios() {
        return listausuarios;
    }

    public void setListausuarios(List<Profile> listausuarios) {
        this.listausuarios = listausuarios;
    }

    public Map<String, String> getTipos() {
        return tipos;
    }

    public void setTipos(Map<String, String> tipos) {
        this.tipos = tipos;
    }

    public Long getProfileId() {
        return (Long) getId();

    }

    public void setProfileId(Long profileId) {
        setId(profileId);
        tipopersona=getInstance().getTipo();
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
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(Profile.class.getName());
        Date now = Calendar.getInstance().getTime();
        Profile profile = new Profile();
        profile.setCreatedOn(now);
        profile.setLastUpdate(now);
        profile.setActivationTime(now);
        profile.setExpirationTime(Dates.addDays(now, 364));
        profile.setResponsable(null); //Establecer al usuario actual
        profile.setType(_type);
        profile.buildAttributes(bussinesEntityService);
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
        System.out.println("entri init" + getInstance());

        tipos = new HashMap<String, String>();
        tipos.put("Conductor", "Conductor");
        tipos.put("Secretario", "Secetario");
        tipos.put("Mecánico", "Mecánico");
        List<Profile> lu = new ArrayList<Profile>();
        listausuarios = ps.findAllA(false);
        for (Profile profile : listausuarios) {
            if (profile.getUsername() != null) {
                lu.add(profile);
            }

        }
        listausuarios = lu;

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

 



    @TransactionAttribute
    private void createUser() throws IdentityException {

        PersistenceManager identityManager = security.getPersistenceManager();
        System.out.println("entro a create user" + getInstance().getUsername());
        Set<String> ik = getInstance().getIdentityKeys();
        System.out.println("size " + ik.size());
        User user = identityManager.findUser(getInstance().getUsername());
        for (String oik : ik) {
            user = identityManager.findUser(oik);
        }

        if (user == null) {
            user = identityManager.createUser(getInstance().getUsername());
        }

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
        // save(getInstance()); //
        setProfileId(getInstance().getId());
        wire();
        getInstance().setName(getInstance().getUsername()); //Para referencia
//        getInstance().setType(_type);
//        
//        getInstance().buildAttributes(bussinesEntityService);
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
    public String saveUsuario() {
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        String salida = "/paginas/admin/listProfile.xhtml?faces-redirect=true";

        getInstance().setEstado(true);
        getInstance().setEstado1(true);

        if (getInstance().isPersistent()) {
            try {
                register();
            } catch (IdentityException ex) {
                ex.printStackTrace();
                Logger.getLogger(ProfileHome.class
                        .getName()).log(Level.SEVERE, null, ex);
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
            return "/paginas/admin/listProfile.xhtml?faces-redirect=true&username=" + getInstance().getUsername() + "&backView=" + getBackView();
        } else {
            return salida;
        }

    }

    @TransactionAttribute
    public String saveProfile() {
        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        System.out.println("getinstance" + getInstance());
        getInstance().setTipo(tipopersona);
         System.out.println("tipo de persona" + getInstance().getTipo()); 
        try {
            if (getInstance().isPersistent()) {

                save(getInstance());
            } else {

                create();
                save(getInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar: " + getInstance().getId(), " ");
            FacesContext.getCurrentInstance().addMessage("", msg);
        }
        return "/paginas/admin/listProfile.xhtml?faces-redirect=true";

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
    public void activarCuenta(Profile habilitar) {
        try {
            IdentityObjectAttribute ida = ps.getAttributos(habilitar.getUsername(), "estado").get(0);

            ida.setValue("ACTIVO");
            habilitar.setEstado(true);
            habilitar.getLastUpdate();
            save(habilitar);
            em.merge(ida);
            em.flush();
            habilitar.setDeleted(false);
            List<Profile> lui = new ArrayList<Profile>();
            for (Profile profile : listausuarios) {
                if (!habilitar.getId().equals(profile.getId())) {
                    lui.add(profile);
                }
            }
            listausuarios = lui;
            FacesMessage msg = new FacesMessage("EL Usuario: " + habilitar.getFullName(), "ha sido habilitado");
            FacesContext.getCurrentInstance().addMessage("", msg);
            mensajei = "EL Usuario: " + habilitar.getFullName() + " ha sido habilitado";
            System.out.println("PROFILE_________2");
        } catch (IdentityException ex) {

        }

    }

}
