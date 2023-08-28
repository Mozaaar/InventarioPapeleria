/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package inventario.papeleria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Funciones extends BD {

    BD cc = new BD();
    Connection con = cc.conexion();

    public void registrar(Papeleria pal) throws Exception {

        try {
            this.conexion();
            PreparedStatement registro = this.conectar.prepareStatement("insert into articulos(Codigo, Producto,ExistenciInicial,Stock,ValorUnitario) values(?,?,?,?,?)");
            registro.setInt(1, pal.getCodigo());
            registro.setString(2, pal.getProducto());
            registro.setInt(3, pal.getExistenciInicial());
            registro.setInt(4, pal.getStock());
            registro.setInt(5, pal.getValorUnitario());

            registro.executeUpdate();
            registro.close();
            JOptionPane.showMessageDialog(null, "Registro exitoso");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Registro fallido " + e.getMessage());

        }
    }

    public void registrarsalida(Papeleria pal) throws Exception {
        try {

            this.conexion();

            // Obtener el valor actual de salidas desde la base de datos
            PreparedStatement getSalidas = this.conectar.prepareStatement("SELECT Salidas FROM articulos WHERE Codigo = ?");
            getSalidas.setInt(1, pal.getCodigo());
            
            ResultSet salidasResult = getSalidas.executeQuery();

            int salidasActuales = 0;
            if (salidasResult.next()) {
                salidasActuales = salidasResult.getInt("Salidas");
            }

            // Sumar las nuevas salidas a las anteriores
            int nuevasSalidas = salidasActuales + pal.getSalida();

            // Verificar si hay suficiente stock para realizar la operación
            PreparedStatement stock = this.conectar.prepareStatement("SELECT Stock FROM articulos WHERE Codigo = ?");
            stock.setInt(1, pal.getCodigo());
            
            ResultSet stockResult = stock.executeQuery();

            if (stockResult.next()) {
                int stockActual = stockResult.getInt("Stock");
                int salidaSolicitada = pal.getSalida();

                if (salidaSolicitada <= stockActual) {
                    // Actualizar el campo Salidas en la base de datos
                    PreparedStatement updateSalidas = this.conectar.prepareStatement("UPDATE articulos SET Salidas = ? WHERE Codigo = ?");
                    updateSalidas.setInt(1, nuevasSalidas);
                    updateSalidas.setInt(2, pal.getCodigo());
                    updateSalidas.executeUpdate();

                    // Actualizar el stock del producto
                    int nuevoStock = stockActual - salidaSolicitada;
                    pal.setStock(nuevoStock);
                    actualizarStock(pal);

                    JOptionPane.showMessageDialog(null, "Registro Salida exitoso");
                } else {
                    JOptionPane.showMessageDialog(null, "No hay suficiente stock para realizar la operación.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Producto no encontrado.");
            }

            stockResult.close();
            stock.close();
            salidasResult.close();
            getSalidas.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "RegistroSalida fallido " + ex.getMessage());

        }
    }

    public void actualizarStock(Papeleria pal) throws Exception {
        try {
            this.conexion();
            PreparedStatement pst = this.conectar.prepareStatement("Update articulos set Stock =? where codigo = ?");
            pst.setInt(1, pal.getStock());
            pst.setInt(2, pal.getCodigo());

            pst.executeUpdate();
            pst.close();

        } catch (Exception e) {

        }
    }

    public void eliminar(Papeleria pal) throws Exception {

        try {
            int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar este registro?", "Confirmación de Eliminación", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                this.conexion();
                PreparedStatement pst = this.conectar.prepareStatement("SELECT * FROM articulos WHERE codigo = ?");
                pst.setInt(1, pal.getCodigo());

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {                                 
                    
                    PreparedStatement deletePst = this.conectar.prepareStatement("DELETE FROM articulos WHERE codigo = ?");
                    deletePst.setInt(1, pal.getCodigo());
                    deletePst.executeUpdate();
                    deletePst.close();

                    JOptionPane.showMessageDialog(null, "Eliminación exitosa");
                } else {
                    JOptionPane.showMessageDialog(null, "El registro no existe.");
                }

                rs.close();
                pst.close();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Eliminación fallida " + e.getMessage());

        }
    }

    public List<Papeleria> listar() throws Exception {
        List<Papeleria> lista = null;
        try {
            this.conexion();
            PreparedStatement pst = this.conectar.prepareStatement("select * from articulos");

            lista = new ArrayList();
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Papeleria pal = new Papeleria();
                pal.setCodigo(rs.getInt("Codigo"));
                pal.setProducto(rs.getString("Producto"));
                pal.setExistenciInicial(rs.getInt("ExistenciInicial"));
                pal.setSalida(rs.getInt("Salidas"));
                pal.setStock(rs.getInt("Stock"));
                pal.setValorUnitario(rs.getInt("ValorUnitario"));
                lista.add(pal);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Registro fallido " + e.getMessage());

        }
        return lista;
    }

}
