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

package org.mtop.modelo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 *
 * @author carla
 * @author yesica
 * 
 */

@Entity
@Table(name = "AuxiliarParReq")
public class AuxiliarParReq implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "requisicionId")
    private Requisicion requisicionId;
    @ManyToOne
    @JoinColumn(name = "partidaId")
    private PartidaContabilidad partidaId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Requisicion getRequisicionId() {
        return requisicionId;
    }

    public void setRequisicionId(Requisicion requisicionId) {
        this.requisicionId = requisicionId;
    }

    public PartidaContabilidad getPartidaId() {
        return partidaId;
    }

    public void setPartidaId(PartidaContabilidad partidaId) {
        this.partidaId = partidaId;
    }
    
    
    
}
