/*
 * Copyright 2013 tania.
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
package edu.sgssalud.model.farmacia;

import edu.sgssalud.model.BussinesEntity;
import edu.sgssalud.model.paciente.Paciente;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import org.jboss.solder.logging.Logger;
/**
 *
 * @author tania
 */

@Entity
public class Receta implements Serializable{
    @Id
    private Long id;
    
    private static Logger log = Logger.getLogger(Receta.class);
    private static final long serialVersionUID = 3L;
   
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fecha;
    private String medicacion;
    private String indicaciones;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}