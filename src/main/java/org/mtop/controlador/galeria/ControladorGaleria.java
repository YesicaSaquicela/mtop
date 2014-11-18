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
 * @author carla
 * @author yesica
 * 
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
