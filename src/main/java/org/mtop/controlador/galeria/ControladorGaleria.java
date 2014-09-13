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

package org.mtop.controlador.galeria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author carlis
 */

@Named
@RequestScoped
public class ControladorGaleria implements Serializable {

    private List<String> images;
 
    private Set<String> cuadrante = new HashSet<String>();

    @PostConstruct
    public void init() {
        images = new ArrayList<String>();

        for (int i = 0; i <= 5; i++) {
            images.add("img" + i + ".jpg");
        }

        cuadrante.add("c1");
        cuadrante.add("c2");
        cuadrante.add("c3");
        cuadrante.add("c4");
        
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    
    public Set<String> getCuadrante() {
        return cuadrante;
    }

    public void setCuadrante(Set<String> cuadrante) {
        this.cuadrante = cuadrante;
    }

    public List<String> cuandrantes() {
        return new ArrayList<String>(getCuadrante());
    }

 

}
