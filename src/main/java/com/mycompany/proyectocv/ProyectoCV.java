/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyectocv;

import com.mycompany.proyectocv.daos.ConexionBD;

/**
 *
 * @author Lenovo
 */
public class ProyectoCV {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        
        ConexionBD cc= new ConexionBD();
        cc.conectarBD();
        System.out.println(cc.conectarBD());
    }
}
