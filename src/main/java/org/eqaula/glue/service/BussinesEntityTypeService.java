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
package org.eqaula.glue.service;

import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.eqaula.glue.model.BussinesEntity;
import org.eqaula.glue.model.BussinesEntityAttribute;
import org.eqaula.glue.model.BussinesEntityType;
import org.eqaula.glue.model.BussinesEntityType_; 
import org.eqaula.glue.util.PersistenceUtil; 

/**
 *
 * @author cesar
 */
public class BussinesEntityTypeService extends PersistenceUtil<BussinesEntity> {

    private static final long serialVersionUID = 6569835981443699931L;
    @Inject
    private Logger log;

    public BussinesEntityTypeService() {
        super(BussinesEntity.class);
    }
    
    @Override
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
    
    //metodo buscar por nombre
    public BussinesEntityType findByName(final String name) {

        log.info("find BussinesEntityType with name " + name);

        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<BussinesEntityType> query = builder.createQuery(BussinesEntityType.class);

        Root<BussinesEntityType> bussinesEntityType = query.from(BussinesEntityType.class);

        query.where(builder.equal(bussinesEntityType.get(BussinesEntityType_.name), name));

        return getSingleResult(query);
    }
    
    //metodo buscar  lista de BussinesEntityType
     public List<BussinesEntityType> find(int maxresults, int firstresult) {
        log.info("find BussinesEntityType, max results " + maxresults + " next result " + firstresult);

        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<BussinesEntityType> query = builder.createQuery(BussinesEntityType.class);

        Root<BussinesEntityType> from = query.from(BussinesEntityType.class);
        query.select(from).orderBy(builder.desc(from.get(BussinesEntityType_.name)));
        return getResultList(query, maxresults, firstresult);
    }
    
    //metodo count
    public Long count() {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<BussinesEntityType> from = query.from(BussinesEntityType.class);

        query.select(builder.count(from)); 
        return this.<Long>getTypedSingleResult(query); 
    }
    
    //metodo saveAtributes 
 /*   public void saveAttributes(BussinesEntity bussinesEntity, Map<Property, Object> attributes) {
        BussinesEntityAttribute attribute = null;
        for (Map.Entry<Property, Object> e : attributes.entrySet()) {
            attribute = new BussinesEntityAttribute();
            attribute.setName(e.getKey().getName());
            attribute.setValue((Serializable) e.getValue());
            attribute.setType(e.getKey().getType());
            bussinesEntity.addBussinesEntityAttribute(attribute);
        }

        super.save(bussinesEntity);
        em.flush();
    }*/
    
    public List<BussinesEntityType> findAll() {        
        Query query = em.createNamedQuery("BussinesEntityType.findAllBussinesEntityTypes");         
        return query.getResultList();
    }        
}
