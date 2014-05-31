/*
 * Copyright 2014 carlis.
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

package org.mtop.servicios;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.mtop.modelo.Requisicion;
import org.mtop.modelo.Vehiculo;
import org.mtop.modelo.Vehiculo_;
import org.mtop.util.PersistenceUtil;

/**
 *
 * @author carlis
 */
public class ServicioRequisicion extends PersistenceUtil<Requisicion> {

    public ServicioRequisicion() {
        super(Requisicion.class);
    }

    @Override
    public void setEntityManager(EntityManager em) {
       this.em=em;
    }
    
    public List<Requisicion> getRequisicion(){
    List listR=this.findAll(Requisicion.class);
    return listR;
    }
    
    public Requisicion getRequisicionId(final Long id){
    return (Requisicion) findById(Requisicion.class, id);
    }
    
    public List<Vehiculo> getVehiculo(final Requisicion r){
    CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<Vehiculo> query = builder.createQuery(Vehiculo.class);
        Root<Vehiculo> entity = query.from(Vehiculo.class);
    //    query.where(builder.equal(entity.get(Vehiculo_.), r));
        return getResultList(query);
    }
}
