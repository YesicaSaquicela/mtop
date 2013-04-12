/*
 * Copyright 2013 dianita.
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
package org.eqaula.glue.controller.management;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import org.eqaula.glue.cdi.Current;
import org.eqaula.glue.cdi.Web;
import org.eqaula.glue.controller.BussinesEntityHome;
import org.eqaula.glue.model.BussinesEntityType;
import org.eqaula.glue.model.management.Diagnostic;
import org.eqaula.glue.model.management.Initiative;
import org.eqaula.glue.model.management.Owner;
import org.eqaula.glue.model.profile.Profile;
import org.eqaula.glue.service.BussinesEntityService;
import org.eqaula.glue.service.OwnerService;
import org.eqaula.glue.util.Dates;
import org.jboss.seam.transaction.Transactional;
import org.primefaces.context.RequestContext;

/*
 * @author dianita
 */

@Named
@ViewScoped
public class DiagnosticHome extends BussinesEntityHome<Diagnostic> implements Serializable{
    private static final long serialVersionUID = 1285521981921867012L;
    
    @Inject
    @Web
    private EntityManager em;
    @Inject
    private BussinesEntityService bussinesEntityService;
    @Current
    @Inject
    private Profile profile;
    
    private Owner owner;
    private Long ownerId;
    @Inject
    private OwnerService ownerService;

    public DiagnosticHome() {
    }
    
     public Long getDiagnosticId(){
        return (Long) getId();
    }
    
    public void setDiagnosticId(Long diagnosticId){
        setId(diagnosticId);
    }
    
    public String getStructureName(){
        return getInstance().getName();
    }

    public Owner getOwner() {
        if(owner==null){
            if(ownerId == null){
                owner= null;
            }else{
                owner = ownerService.find(getOwnerId());
            }
        }
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    
    
    @TransactionAttribute
    public void load() {
        if (isIdDefined()) {
            wire();
        }
    }

    @TransactionAttribute
    public void wire() {
        getInstance();
    }

    @PostConstruct
    public void init() {
        setEntityManager(em);
        ownerService.setEntityManager(em);
        bussinesEntityService.setEntityManager(em);
    }
    
    @Override
    protected Diagnostic createInstance() {
        BussinesEntityType _type = bussinesEntityService.findBussinesEntityTypeByName(Diagnostic.class.getName());
        Date now = Calendar.getInstance().getTime();
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setCode(UUID.randomUUID().toString());
        diagnostic.setCreatedOn(now);
        diagnostic.setLastUpdate(now);
        diagnostic.setActivationTime(now);
        diagnostic.setExpirationTime(Dates.addDays(now, 364));
        diagnostic.setType(_type);
        diagnostic.buildAttributes(bussinesEntityService);
        return diagnostic;
    }

    @TransactionAttribute
    public String saveDiagnostic() {

        Date now = Calendar.getInstance().getTime();
        getInstance().setLastUpdate(now);
        if (getInstance().isPersistent()) {
            save(getInstance());
        } else {
            getInstance().setAuthor(this.profile);
            getInstance().setOwner(getOwner());
            create(getInstance());
        }
        if (null!= null) {
            //return getOutcome() + "?balancedScorecardId=" + getTarget().getMeasure().getObjetive().getTheme().getPerspective().getBalancedScorecard().getId() + "&faces-redirect=true&includeViewParams=true";
        }
        return getOutcome() + "?faces-redirect=true&includeViewParams=true";
    }

    public boolean isWired() {
        return true;
    }

    public Diagnostic getDefiniedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

    @Override
    public Class<Diagnostic> getEntityClass() {
        return Diagnostic.class;
    }

    @Transactional
    public String deleteDiagnostic() {
        try {
            if (getInstance() == null) {
                throw new NullPointerException("Diagnostic is Null");
            }
            if (getInstance().isPersistent()) {
                delete(getInstance());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Se borró exitosamente:  " + getInstance().getName(), ""));
                RequestContext.getCurrentInstance().execute("editDlg.hide()");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "¡No existe un diagnostico para ser borrado!", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRORE", e.toString()));
        }
        //if (getTarget()!= null) {
          //  return getOutcome() + "?balancedScorecardId=" + getTarget().getMeasure().getObjetive().getTheme().getPerspective().getBalancedScorecard().getId() + "&faces-redirect=true&includeViewParams=true";
        //}
        return getOutcome() + "?faces-redirect=true&includeViewParams=true";
    }    
}
