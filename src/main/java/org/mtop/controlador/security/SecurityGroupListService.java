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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.seam.security.management.picketlink.IdentitySessionProducer;
import org.jboss.seam.transaction.Transactional;
import org.mtop.cdi.Web;
import org.mtop.util.QuerySortOrder;
import org.mtop.util.UI;
import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.IdentitySessionFactory;
import org.picketlink.idm.api.UnsupportedCriterium;
import org.picketlink.idm.common.exception.IdentityException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author carla
 * @author yesica
 * 
 */
@Named
@ViewScoped
public class SecurityGroupListService extends LazyDataModel<Group> {

    private static final long serialVersionUID = 4819808125494695197L;
    private static final int MAX_RESULTS = 5;
    private static org.jboss.solder.logging.Logger log = org.jboss.solder.logging.Logger.getLogger(SecurityGroupListService.class);
    @Inject
    @Web
    private EntityManager entityManager;
    @Inject
    private IdentitySessionFactory identitySessionFactory;
    @Inject
    private SecurityGroupService securityGroupService;
    private int firstResult = 0;
    private Group[] selectedGroups;
    private Group selectedGroup;
    private String groupName;
    private List<Group> listGroups;

    public SecurityGroupListService() {
        setPageSize(MAX_RESULTS);
        listGroups = new ArrayList<Group>();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getNextFirstResult() {
        return firstResult + this.getPageSize();
    }

    public int getPreviousFirstResult() {
        return this.getPageSize() >= firstResult ? 0 : firstResult - this.getPageSize();
    }

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    public Group getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(Group selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public Group[] getSelectedGroups() {
        return selectedGroups;
    }

    public List<Group> getListGroups() {
        return listGroups;
    }

    public void setListGroups(List<Group> listGroups) {
        this.listGroups = listGroups;
    }

    public void setSelectedGroups(Group[] selectedGroups) {
        this.selectedGroups = selectedGroups;
    }

    @Transactional
    public void deleteGroup() {
        if (this.getSelectedGroup() != null) {
            try {
                if (securityGroupService.isAssociatedUser(selectedGroup)) {
                    securityGroupService.removeGroup(selectedGroup);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Se borró exitosamente:  " + getSelectedGroup().getName(), ""));
                    RequestContext.getCurrentInstance().execute("deletedDlg.hide();"); //cerrar el popup si se grabo correctamente
                    this.setSelectedGroup(null);
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "No se pudo borrar! \nEste gropu tiene usuarios asociados:", ""));
                }
            } catch (IdentityException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "No se pudo borrar :  " + getSelectedGroup().getName(), ""));
                Logger.getLogger(SecurityGroupListService.class.getName()).log(Level.SEVERE, "Error al eliminar grupo", ex);

            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cann't delete group!", "Please provide a group"));
        }

    }

    @PostConstruct
    public void init() {
        securityGroupService.setEntityManager(entityManager);
        Map<String, Object> sessionOptions = new HashMap<String, Object>();
        sessionOptions.put(IdentitySessionProducer.SESSION_OPTION_ENTITY_MANAGER, entityManager);
        try {
            securityGroupService.setSecurity(identitySessionFactory.createIdentitySession("default", sessionOptions));
        } catch (IdentityException ex) {
            Logger.getLogger(SecurityGroupListService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage(UI.getMessages("Group") + " " + UI.getMessages("common.selected"), ((Group) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        this.setSelectedGroup((Group) event.getObject());
    }

    public void onRowUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage(UI.getMessages("Group") + " " + UI.getMessages("common.unselected"), ((Group) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        this.setSelectedGroup(null);
    }

    @Override
    public Group getRowData(String rowKey) {
        try {
            return securityGroupService.findByName(rowKey);
        } catch (IdentityException ex) {
            FacesMessage msg = new FacesMessage(UI.getMessages("common.error"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            Logger.getLogger(SecurityGroupListService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Object getRowKey(Group entity) {
        return entity.getName();
    }

    
    
    @Override
    public List<Group> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
        List<Group> result = new ArrayList<Group>();
        try {
            int end = first + pageSize;
            QuerySortOrder order = QuerySortOrder.ASC;
            if (sortOrder == SortOrder.DESCENDING) {
                order = QuerySortOrder.DESC;
            }
            Map<String, Object> _filters = new HashMap<String, Object>();
            result = securityGroupService.find(first, end, sortField, order, _filters);
            this.setRowCount(result.size());  //importante para cargar datos 
        } catch (UnsupportedCriterium ex) {
            Logger.getLogger(SecurityGroupListService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IdentityException ex) {
            Logger.getLogger(SecurityGroupListService.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.listGroups = result;
        return result;
    }
    

    public void assignGroup(Group g) {
        this.selectedGroup = g;
    }
}
