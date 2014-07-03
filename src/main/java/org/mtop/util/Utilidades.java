/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mtop.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author carlis
 */
public class Utilidades {

    public static List<String> cadenaALista(String s) {
        if (s == null) {
            return new ArrayList<String>();
        }
        if (s.isEmpty()) {
            return new ArrayList<String>();
        }
        return Arrays.asList(s.split(","));
    }
    //polimorfismo dos metodos de igual nombre pero diferente contenido
    public static String buscarValorDefecto(String values) {
        return buscarValorDefecto(values, "*");
    }

    public static String buscarValorDefecto(String values, String mnemotecnico) {
        List<String> options = Utilidades.cadenaALista(values);
        String defaultValue = null;
        for (String s : options) {
            
            if (s.contains(mnemotecnico)) {
                defaultValue = s.substring(0, s.length() - 1);
            }
        }
        return defaultValue;
    }
}
