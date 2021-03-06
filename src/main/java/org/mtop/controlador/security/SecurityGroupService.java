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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.mtop.util.QuerySortOrder;

import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.IdentitySearchCriteriumType;
import org.picketlink.idm.api.IdentitySession;
import org.picketlink.idm.api.SortOrder;
import org.picketlink.idm.api.UnsupportedCriterium;
import org.picketlink.idm.api.User;
import org.picketlink.idm.common.exception.IdentityException;
import org.picketlink.idm.impl.api.IdentitySearchCriteriaImpl;
import org.picketlink.idm.impl.api.model.SimpleGroup;

/**
 *
 * @author carla
 * @author yesica
 * 
 */
public class SecurityGroupService implements Serializable {

    private static org.jboss.solder.logging.Logger log = org.jboss.solder.logging.Logger.getLogger(SecurityGroupService.class);
    private static final long serialVersionUID = -8856264241192917839L;
    private EntityManager entityManager;
    private IdentitySession security;

    public SecurityGroupService() {
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public IdentitySession getSecurity() {
        return security;
    }

    public void setSecurity(IdentitySession security) {
        this.security = security;
    }

    //metodo count
    public long count() {
        try {
            return (long) security.getPersistenceManager().getGroupTypeCount("GROUP");
        } catch (IdentityException ex) {
            Logger.getLogger(SecurityGroupService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public Group getGroupById(final Long id) throws IdentityException {
        Group g = security.getPersistenceManager().findGroupByKey(String.valueOf(id));        
        return g;

    }

    public Group findByName(final String name) throws IdentityException {        
        return security.getPersistenceManager().findGroup(name, "GROUP");
    }

    public Group findByKey(final String key) throws IdentityException {        
        return security.getPersistenceManager().findGroupByKey(key);
    }

    public List<Group> find(int first, int end, String sortField, QuerySortOrder order, Map<String, Object> _filters) throws UnsupportedCriterium, IdentityException {

        IdentitySearchCriteriaImpl identitySearchCriteria = new IdentitySearchCriteriaImpl();
        identitySearchCriteria.sort(SortOrder.ASCENDING);
        if (QuerySortOrder.DESC.equals(order)) {
            identitySearchCriteria.sort(SortOrder.DESCENDING);
        }
        identitySearchCriteria.sortAttributeName(sortField);
        identitySearchCriteria.setPaged(true);
        identitySearchCriteria.page(first, end);
        String[] values = new String[1];
        for (Map.Entry entry : _filters.entrySet()) {
            values[1] = (String) entry.getValue();
            identitySearchCriteria.attributeValuesFilter((String) entry.getKey(), values);
        }
        log.info("retrieve from" + first + " to " + end);
        List<Group> tem = new ArrayList<Group>(security.getPersistenceManager().findGroup("GROUP", identitySearchCriteria));
        return tem;

    }
    public void removeGroup(Group g) throws IdentityException {    
        security.getPersistenceManager().removeGroup(g, true);
    }
    
    public void associate(Group g, User u) throws IdentityException {
        security.getRelationshipManager().associateUser(g, u);
    }

    public void disassociate(Group g, User u) throws IdentityException {
        Collection<User> listUser= new ArrayList<User>();
        listUser.add(u);
        security.getRelationshipManager().disassociateUsers(g, listUser);
    }
    
    public void disassociate(String nameUser) throws IdentityException {
        security.getRelationshipManager().disassociateGroups(nameUser);
    }

    public User findUser(String usr) throws IdentityException {
        return security.getPersistenceManager().findUser(usr);
    }

    Collection<Group> find(User user) throws IdentityException {
        return security.getRelationshipManager().findAssociatedGroups(user);
    }

    boolean isAssociated(Group group, User user) throws IdentityException {        
        return security.getRelationshipManager().isAssociated(group, user);
    }
    
    boolean isAssociatedUser(Group group) throws IdentityException {
        boolean b = security.getRelationshipManager().findAssociatedUsers(group, true).isEmpty();        
        return b;
    }

   
}
