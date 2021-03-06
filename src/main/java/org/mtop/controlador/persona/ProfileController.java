
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.solder.logging.Logger;
import org.mtop.modelo.dinamico.Property;
import org.mtop.modelo.profile.Profile;
import org.mtop.profile.ProfileService;
import org.mtop.service.BussinesEntityService;
import org.mtop.util.Dates;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
@Model
public class ProfileController {

    private static Logger log = Logger.getLogger(ProfileController.class);
    Long profileId;
    @Inject
    private EntityManager em;
    @Inject
    private FacesContext facesContext;
    @Inject
    private ProfileService profileService;
    @Inject
    private BussinesEntityService bussinesEntityService;
    private Profile profile;
    private Map<Property, Object> attributes;

    @Produces
    @Named("newProfile")
    public Profile getProfile() {
        return profile;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    @TransactionAttribute
    public String register() throws Exception {
        System.out.println("--------> starting register!");
        Date now = Calendar.getInstance().getTime();
        profile.setCreatedOn(now);
        profile.setActivationTime(now);
        profile.setExpirationTime(Dates.addDays(now, 364));
        profile.setResponsable(null); //Establecer al usuario actual

        profileService.create(profile);
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration profile successful"));
        em.flush();
        System.out.println("--------> ending register!");
        return "persisted";
    }
    
    @TransactionAttribute
    public String save() throws Exception {
        Date now = Calendar.getInstance().getTime();
        profile.setLastUpdate(now);
        profileService.save(profile);
        
        bussinesEntityService.saveAttributes(profile, attributes);
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration profile successful"));
        
        return "saved";
    }

    @PostConstruct
    public void init() {
        profileService.setEntityManager(em);
        profile = new Profile();
    }
}
