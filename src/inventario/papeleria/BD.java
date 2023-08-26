/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package inventario.papeleria;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author cf2065la
 */
public class BD {
     Connection conectar = null;

    public Connection conexion() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conectar = DriverManager.getConnection("jdbc:mysql://localhost:3306/papeleria", "root", "");
            

            //JOptionPane.showMessageDialog(null, "Conexion exitosa");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Conexion fallida");

        }
        return conectar;
    }

    public static void main(String[] arg) {

    }

}
