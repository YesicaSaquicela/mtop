/*
 * Copyright 2013 cesar.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;


import org.jboss.seam.international.status.Messages;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import org.jboss.seam.transaction.Transactional;
import org.mtop.cdi.Web;
import org.mtop.profile.ProfileService;
import org.mtop.util.UI;
import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.IdentitySession;
import org.picketlink.idm.api.User;
import org.picketlink.idm.common.exception.IdentityException;
import org.picketlink.idm.impl.api.model.SimpleUser;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author cesar
 */
@Named(value = "securityHome")
@ViewScoped
public class SecurityHome implements Serializable {

    private static final long serialVersionUID = 7632987414391869389L;
    private static org.jboss.solder.logging.Logger log = org.jboss.solder.logging.Logger.getLogger(SecurityHome.class);
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
    private ProfileService ps;
    @Inject
    private SecurityGroupService securityGroupService;
    private Messages msg;
    private String username;
    private String groupname;
    private Group group;
    private Group groupSelected;
    private User user;

    public SecurityHome() {
    }

    @PostConstruct
    public void init() {        
        ps.setEntityManager(em);
        securityGroupService.setEntityManager(em);
        securityGroupService.setSecurity(security);

    }

    public Group getGroup() throws IdentityException {
        if (this.group == null) {
            if (getGroupname() != null && !getGroupname().isEmpty()) {
                group = securityGroupService.findByName(getGroupname());
            }
        }        
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;        
    }

    private User getUser() throws IdentityException {
        System.out.println("entro get user"+this.user);
        
        if (this.user == null) {
            System.out.println(" ento al if"+getUsername());
            user = new SimpleUser(getUsername());
            if (getUsername() != null && !getUsername().isEmpty()) {
                System.out.println("fijando user>>>>");
                user = securityGroupService.findUser(getUsername());
                
            }
        }       
        System.out.println("retornando>>>"+user);
        return user;
    }

    public Group getGroupSelected() {
        return groupSelected;
    }

    public void setGroupSelected(Group groupSelected) {
        System.out.println("legaaaa grupo selecc"+groupSelected);
        this.groupSelected = groupSelected;
    } 

    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        System.out.println("usio llega en aotorizaci'on>>>>"+username);
        this.username = username;
    }

    public String getGroupname() {
        System.out.println("grupo nombre>>>>>"+groupname);
        return groupname;
    }

    public void setGroupname(String groupname) {        
        this.groupname = groupname;
    }
    
    @Transactional
    public void associateTo() {
        System.out.println("entro a associate");
        System.out.println("grup"+group);
        System.out.println("user"+user);
        try {
            if (getGroup() != null && getUser() != null) {                
                if (!securityGroupService.isAssociated(group, user)) {                 
                    security.getRelationshipManager().associateUser(group, user);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Autorización realizada con exito! ",""));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Autorización exitosa! usuario " + getUser().getKey() + " fue asignado en " + getGroup().getName(),"" ));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se puede realizar la autorización para el"+ getGroup().getName() + " y " + getUser().getKey(),""));
            }

        } catch (IdentityException ex) {
            Logger.getLogger(SecurityHome.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void asignargrupo(Group gs){
        System.out.println("entrooooooooooooooooooooooooooooo");
        System.out.println("gssss"+gs);
        this.groupSelected=gs;
               System.out.println("fijndo grupo>>>>>>"+groupSelected);
    }
    
    @Transactional
    public void disassociate() {
        Group g = groupSelected;
            System.out.println("entro>>>>>");
          
              System.out.println("leego grupo222"+groupname);
              System.out.println("leego grupo333"+groupSelected);
            System.out.println("llego user"+username);
             System.out.println("llego user1"+user);
        try {
            log.info("Usuario: "+user+" Grupo: "+g);
            if (g != null && getUser() != null) {
                if (securityGroupService.isAssociated(g, user)) {
                    securityGroupService.disassociate(g, getUser());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ya no pertene al grupo!",   "" + getUser().getKey() + " se ha retirado de " + g.getName()));
                    RequestContext.getCurrentInstance().execute("deletedDlg.hide();");
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No puede retirarse  !",  "la acción realiza es incorrecta " + g.getName() + ", " + getUser().getKey() + " no existe"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No puede cancelar la asignación de la autorización!", "Por favor ingrese el grupo y el usuario"));
            }

        } catch (IdentityException ex) {
            Logger.getLogger(SecurityHome.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Retorna la lista de Grupos de autorización como una lista de SelectedItem
     *
     * @return
     */
    public List<SelectItem> getGroupsAsSelectItem() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        SelectItem item = null;
        item = new SelectItem(null, UI.getMessages("common.choice"));
        items.add(item);
        try {
            for (org.picketlink.idm.api.Group g : security.getPersistenceManager().findGroup("GROUP")) {
                item = new SelectItem(g, g.getName());
                items.add(item);
                log.info("eqaula --> Items add grupo " + item.getValue());
            }
        } catch (IdentityException ex) {
            Logger.getLogger(SecurityHome.class.getName()).log(Level.SEVERE, null, ex);
        }
        return items;
    }

    public List<Group> findAllGroups() {
        try {
            return new ArrayList<Group>(security.getPersistenceManager().findGroup("GROUP"));
        } catch (IdentityException ex) {
            Logger.getLogger(SecurityHome.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Group>();
    }

    public List<Group> findGroups() {
        List<Group> groups = new ArrayList<Group>();
        System.out.println("presenta antes");
        System.out.println("");
        try {
           
            groups = new ArrayList<Group>(securityGroupService.find(getUser()));
        } catch (IdentityException ex) {
            Logger.getLogger(SecurityHome.class.getName()).log(Level.SEVERE, null, ex);
        }
        return groups;
    }
    
    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage(UI.getMessages("Grupo") + " " + UI.getMessages("common.selected"), "" + ((Group) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage("", msg);
    }

    public void onRowUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage(UI.getMessages("Grupo") + " " + UI.getMessages("common.unselected"), ((Group) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage("", msg);
        this.setGroupSelected(null);
    }
    
}
