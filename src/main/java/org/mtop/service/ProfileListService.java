/*
 * Copyright 2012 cesar.
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
package org.mtop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import org.mtop.cdi.Web;

import org.mtop.modelo.profile.Profile;

import org.mtop.modelo.security.IdentityObjectAttribute;
import org.mtop.profile.ProfileService;
import org.mtop.util.QueryData;
import org.mtop.util.QuerySortOrder;
import org.mtop.util.UI;
import java.util.logging.Level;
import javax.ejb.TransactionAttribute;
import org.jboss.seam.transaction.Transactional;
import org.jboss.solder.logging.Logger;
import org.picketlink.idm.api.IdentitySession;
import org.picketlink.idm.api.PersistenceManager;
import org.picketlink.idm.api.User;
import org.picketlink.idm.common.exception.IdentityException;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author cesar
 */
@Named
@RequestScoped
public class ProfileListService extends LazyDataModel<Profile> {

    private static Logger log = Logger.getLogger(ProfileListService.class);
    private static final int MAX_RESULTS = 5;
    @Inject
    @Web
    private EntityManager entityManager;
    @Inject
    private ProfileService profileService;
//    @Inject
//    private SecurityGroupService securityRol;
    private List<Profile> resultList = new ArrayList<Profile>();
    private int firstResult = 0;
    private String typeName;
    private Profile[] selectedProfiles;
    private Profile selectedProfile;
    private String estado;
    private String listarUsuarios;

    @Inject
    private IdentitySession security;

    public String getListarUsuarios() {
        return listarUsuarios;
    }

    public void setListarUsuarios(String listarUsuarios) {
        List<Profile> lc = profileService.findAll();
        List<Profile> lp = new ArrayList<Profile>();
        System.out.println("ento all meetodo>>>>>>>>>>.>>>>>>>>");
        if (listarUsuarios.equals("registrados")) {
            System.out.println("ento all ifff>>>>>>>>>>.>>>>>>>>");
            for (Profile profile : lc) {
                System.out.println("ento all for>>>>>>>>>>.>>>>>>>>");
                if (profile.getUsername() != null) {
                    System.out.println("ento all >>>>>>>>>>.>>>>>>>>");
                    lp.add(profile);
                }
            }

        }
        this.listarUsuarios = listarUsuarios;
    }

    public ProfileListService() {
        setPageSize(MAX_RESULTS);
        //resultList = new ArrayList<Profile>();
    }

    @PostConstruct
    public void init() {
        profileService.setEntityManager(entityManager);
//        securityRol.setSecurity(security);
        if ("inactivo".equals(estado)) {
            resultList = profileService.findAllA(true);
        } else {
            resultList = resultList = profileService.findAllA(false);
        }
//
//        List<Profile> lc = profileService.findAll();
//        List<Profile> lp = new ArrayList<Profile>();
//        System.out.println("ento all meetodo>>>>>>>>>>.>>>>>>>>");
//
//        System.out.println("ento all ifff>>>>>>>>>>.>>>>>>>>");
//        for (Profile profile : lc) {
//            System.out.println("ento all for>>>>>>>>>>.>>>>>>>>");
//            if (profile.getUsername() != null) {
//                System.out.println("ento all >>>>>>>>>>.>>>>>>>>");
//                lp.add(profile);
//            }
//        }
//
//        resultList = lp;

    }

    @Override
    public List<Profile> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        int end = first + pageSize;

        QuerySortOrder order = QuerySortOrder.ASC;
        if (sortOrder == SortOrder.DESCENDING) {
            order = QuerySortOrder.DESC;
        }
        Map<String, Object> _filters = new HashMap<String, Object>();
        /*filters.put(Profile_.deleted.getName(), estado);
         _filters.putAll(filters);*/

        QueryData<Profile> qData = profileService.find(first, end, sortField, order, _filters);
        this.setRowCount(qData.getTotalResultCount().intValue());
        this.setResultList(qData.getResult());

        return qData.getResult();
    }

    @Override
    public Profile getRowData(String rowKey) {
        //return profileService.find(Long.parseLong(rowKey));
        return profileService.findByName(rowKey);
    }

    @Override
    public Object getRowKey(Profile entity) {
        return entity.getId();
    }

    public void inactivos() {
        resultList = profileService.findAllA(true);
    }

    public List<Profile> getResultList() {
//        if (resultList.isEmpty() /*&& getSelectedBussinesEntityType() != null*/) {
//            resultList = profileService.getProfiles(this.getPageSize(), firstResult);
//        }
        return resultList;
    }

    public void setResultList(List<Profile> resultList) {
        this.resultList = resultList;
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
        this.resultList = null;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Profile[] getSelectedProfiles() {
        return selectedProfiles;
    }

    public void setSelectedProfiles(Profile[] selectedProfiles) {
        this.selectedProfiles = selectedProfiles;
    }

    public Profile getSelectedProfile() {
        return selectedProfile;
    }

    public void setSelectedProfile(Profile selectedProfile) {
        this.selectedProfile = selectedProfile;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        if ("inactivo".equals(estado)) {
            setResultList(profileService.findAllA(true));
        } else {
            resultList = profileService.findAllA(false);
        }
        this.estado = estado;
    }

    public void onRowSelect(SelectEvent event) {
        //this.setSelectedProfile((Profile) event.getObject());

        FacesMessage msg = new FacesMessage(UI.getMessages("profile") + " " + UI.getMessages("common.selected"), ((Profile) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage("", msg);
    }

    public void onRowUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage(UI.getMessages("profile") + " " + UI.getMessages("common.unselected"), ((Profile) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage("", msg);
        this.setSelectedProfile(null);
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @TransactionAttribute
    public void inavilitarCuenta() {
        System.out.println("PROFILE_________init");

        try {
            //PersistenceManager identityManager = security.getPersistenceManager();
            //AttributesManager attributesManager = security.getAttributesManager();
            //User user = identityManager.findUser(selectedProfile.getUsername());            
            IdentityObjectAttribute ida = profileService.getAttributos(selectedProfile.getUsername(), "estado").get(0);
            //securityRol.disassociate(selectedProfile.getUsername());
            System.out.println("PROFILE_________0");
            ida.setValue("INACTIVO");
            entityManager.merge(ida);
            entityManager.flush();
            FacesMessage msg = new FacesMessage("EL Usuario: " + selectedProfile.getFullName(), "ha sido deshabilitado");
            FacesContext.getCurrentInstance().addMessage("", msg);
            System.out.println("PROFILE_________2");
        } catch (IdentityException ex) {
            ex.printStackTrace();
            System.out.println("PROFILE_________3");
            java.util.logging.Logger.getLogger(ProfileListService.class.getName()).log(Level.SEVERE, null, ex);
        }
//        } else {
//            System.out.println("PROFILE_________x");
//        }

    }
}
