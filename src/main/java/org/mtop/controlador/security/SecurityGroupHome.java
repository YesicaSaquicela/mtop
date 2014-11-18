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

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.seam.security.Identity;
import org.mtop.cdi.Web;
import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.IdentitySession;
import org.picketlink.idm.common.exception.IdentityException;
import org.picketlink.idm.impl.api.model.SimpleGroup;

/**
 *
 * @author carla
 * @author yesica
 * 
 */
@Named
@ViewScoped
public class SecurityGroupHome implements Serializable {

    private static final long serialVersionUID = 7632987414391869389L;
    private static org.jboss.solder.logging.Logger log = org.jboss.solder.logging.Logger.getLogger(SecurityGroupHome.class);
    @Inject
    @Web
    private EntityManager entityManager;
    @Inject
    private Identity identity;
    @Inject
    private IdentitySession security;
    private Group instance;
    @Inject
    private SecurityGroupService securityGroupService;
    private String groupKey;
    private String groupName;

    @PostConstruct
    public void init() {
        securityGroupService.setSecurity(security);
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public String getGroupName() {
        if (isManaged()) {
            setGroupName(getInstance().getName());
        }
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setInstance(Group instance) {
        this.instance = instance;
    }

    public Group getInstance() {
        if (instance == null) {
            initInstance();
        }
        return instance;
    }

    @TransactionAttribute
    public String saveGroup() {
        log.info("Save instance for " + getInstance().getKey() + " with name " + getGroupName());
        if (isManaged()) {
            //TODO implementar actualización de nombre de grupo, evaluar si es necesario
        } else {
            try {
                security.getPersistenceManager().createGroup(getGroupName(), "GROUP");
            } catch (IdentityException ex) {
                Logger.getLogger(SecurityGroupHome.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "/paginas/admin/security/list?faces-redirect=true";
    }

    protected Group createInstance() {
        Group u = new SimpleGroup("New Group", "GROUP");                
        return u;
    }

    @TransactionAttribute
    public void load() {
        if (isIdDefined()) {
            wire();
        }
        log.info("eqaula --> Loaded instance " + getInstance());
    }
  
    @TransactionAttribute
    public void wire() {
        getInstance();
    }
  
    public boolean isPersistent() {
        return getInstance().getKey() != null;
    }

    public boolean isIdDefined() {
        return getGroupKey() != null && !"".equals(getGroupKey());
    }

    public Group find() throws IdentityException {
        if (securityGroupService.getSecurity() != null) {
            Group result = securityGroupService.findByKey(getGroupKey());
            if (result == null) {
                result = handleNotFound();
            }
            return result;
        } else {
            return null;
        }
    }

    public void clearInstance() {
        setInstance(null);
        setGroupKey(null);
    }

    protected void initInstance() {
        if (isIdDefined()) {
            if (true /*!isTransactionMarkedRollback()*/) {
                try {
                    //we cache the instance so that it does not "disappear"
                    //after remove() is called on the instance
                    //is this really a Good Idea??
                    setInstance(find());
                } catch (IdentityException ex) {
                    Logger.getLogger(SecurityGroupHome.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            setInstance(createInstance());
        }
    }

    private Group handleNotFound() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public boolean isManaged() {
        return getInstance() != null
                && getGroupKey() != null;
    }
}
