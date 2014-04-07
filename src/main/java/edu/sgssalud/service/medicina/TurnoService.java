/*
 * Copyright 2014 cesar.
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

package edu.sgssalud.service.medicina;

import edu.sgssalud.model.medicina.SignosVitales;
import edu.sgssalud.model.servicios.Turno;
import edu.sgssalud.util.PersistenceUtil;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author cesar
 */
public class TurnoService extends PersistenceUtil<Turno> implements Serializable{
    
    private static final long serialVersionUID = 234L;  
        
    public TurnoService(){
        super(Turno.class);
    }
    
    @Override
    public void setEntityManager(EntityManager em) {
        this.em = em;        
    }
    
    public List<Turno> getTurnos(final int limit, final int offset) {
        return findAll(entityClass);
    }
    
    public List<Turno> getTurnos() {
        return findAll(Turno.class);
    }
    
    public Turno getSTurnoPorId(final Long id) {
        return (Turno) findById(Turno.class, id);
    }
}
