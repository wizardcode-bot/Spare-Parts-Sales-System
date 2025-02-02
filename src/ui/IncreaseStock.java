package ui;

import common.Validations;
import dao.ConnectionProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author HOME
 */
public class IncreaseStock extends javax.swing.JFrame {
    
    private final String typeOfAdjustment = "Aumento de stock";

    /**
     * Creates new form InventoryAdjustments
     */
    public IncreaseStock() {
        initComponents();
        setSize(1200, 610);
        setLocationRelativeTo(null);
    }

    private void productName(String nameOrUniqueId) {
        //mostrar código y descripción de cada producto en la tabla
        DefaultTableModel model = (DefaultTableModel) productsTable.getModel();
        model.setRowCount(0);

        String query = "SELECT * FROM products WHERE description LIKE ? OR product_pk LIKE ?";

        try (Connection con = ConnectionProvider.getCon(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, nameOrUniqueId + "%");
            pst.setString(2, nameOrUniqueId + "%");

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{rs.getString("product_pk") + "   -   " + rs.getString("description")});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar productos: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        productsTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtProductID = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtDescription = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtProductBrand = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCurrentStock = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtProductLocation = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtSellingPrice = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtStockToIncrease = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtMotive = new javax.swing.JTextArea();
        jLabel13 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Incrementar stock");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(411, 6, -1, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 56, 1200, 10));

        productsTable.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        productsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Productos Registrados"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        productsTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        productsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                productsTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(productsTable);
        if (productsTable.getColumnModel().getColumnCount() > 0) {
            productsTable.getColumnModel().getColumn(0).setResizable(false);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(38, 148, 370, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Buscar producto por ID o descripción");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 84, -1, -1));

        txtSearch.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });
        getContentPane().add(txtSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(38, 107, 370, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("ID del producto");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 148, -1, -1));

        txtProductID.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtProductID.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtProductID, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 169, 300, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Descripción");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 211, -1, -1));

        txtDescription.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtDescription.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 232, 300, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Marca");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 274, -1, -1));

        txtProductBrand.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtProductBrand.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtProductBrand, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 295, 300, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Cantidad en stock");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 400, -1, -1));

        txtCurrentStock.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtCurrentStock.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtCurrentStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 421, 200, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Ubicación en almacén");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 337, -1, -1));

        txtProductLocation.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtProductLocation.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtProductLocation, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 358, 300, -1));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Precio de venta");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 463, -1, -1));

        txtSellingPrice.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtSellingPrice.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtSellingPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 484, 200, -1));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Información del producto");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 105, -1, -1));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Detalles de ajuste");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 105, -1, -1));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Cantidad de unidades a incrementar *");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 145, -1, -1));

        txtStockToIncrease.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtStockToIncrease.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtStockToIncrease, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 166, 233, -1));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("Motivo de ajuste *");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 211, -1, -1));

        txtMotive.setColumns(20);
        txtMotive.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtMotive.setForeground(new java.awt.Color(0, 0, 0));
        txtMotive.setRows(5);
        jScrollPane2.setViewportView(txtMotive);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 232, 300, -1));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("(*) Indica campo obligatorio");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 554, -1, -1));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        jButton1.setText("Guardar");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton1.setIconTextGap(10);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 330, 110, -1));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jLabel14.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel14MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(1154, 6, -1, -1));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/whiteSmoke1200x612.jpg"))); // NOI18N
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseClicked
        dispose();
    }//GEN-LAST:event_jLabel14MouseClicked

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        productName("");
        txtProductID.setEditable(false);
        txtDescription.setEditable(false);
        txtProductBrand.setEditable(false);
        txtProductLocation.setEditable(false);
        txtCurrentStock.setEditable(false);
        txtSellingPrice.setEditable(false);
    }//GEN-LAST:event_formComponentShown

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        String search = txtSearch.getText().trim();
        productName(search);
    }//GEN-LAST:event_txtSearchKeyReleased

    private void productsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productsTableMouseClicked
        // mostrar los datos del producto al seleccionarlo en la tabla
        int index = productsTable.getSelectedRow();
        TableModel model = productsTable.getModel();
        String nameOrUniqueId = model.getValueAt(index, 0).toString();

        String uniqueId[] = nameOrUniqueId.split("-", 0);

        String query = "SELECT * FROM products WHERE product_pk = ?";
        try (Connection con = ConnectionProvider.getCon(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, uniqueId[0]);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    txtProductID.setText(uniqueId[0]);
                    txtDescription.setText(rs.getString("description"));
                    txtProductBrand.setText(rs.getString("productBrand"));
                    txtProductLocation.setText(rs.getString("productLocation"));
                    txtCurrentStock.setText(rs.getString("quantity"));
                    txtSellingPrice.setText(rs.getString("sellingPrice"));
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_productsTableMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // botón para guardar 
        String stockToIncrease = txtStockToIncrease.getText().trim();
        String motive = txtMotive.getText().trim();
        String productID = txtProductID.getText().trim();
        String currentStock = txtCurrentStock.getText().trim();

        if (Validations.isNullOrBlank(productID)) {
            JOptionPane.showMessageDialog(null, "No has seleccionado ningún producto a modificar", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (Validations.isNullOrBlank(stockToIncrease) || Validations.isNullOrBlank(motive)) {
            JOptionPane.showMessageDialog(null, "Debes de completar todos los campos obligatorios (*)", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            long currentStockValue = Long.parseLong(currentStock);
            long stockToIncreaseValue = Long.parseLong(stockToIncrease);
            long newStockValue = currentStockValue + stockToIncreaseValue;

            String insertAdjustmentQuery = "INSERT INTO inventory_adjustments "
                    + "(typeOfAdjustment, previousQuantity, newQuantity, adjustmentMotive, product_pk) "
                    + "VALUES (?, ?, ?, ?, ?)";

            String updateProductQuery = "UPDATE products SET quantity = ? WHERE product_pk = ?";

            try (Connection con = ConnectionProvider.getCon(); PreparedStatement pstInsert = con.prepareStatement(insertAdjustmentQuery); PreparedStatement pstUpdate = con.prepareStatement(updateProductQuery)) {

                // Insertar en inventory_adjustments
                pstInsert.setString(1, typeOfAdjustment); // previousQuantity
                pstInsert.setLong(2, currentStockValue); // previousQuantity
                pstInsert.setLong(3, newStockValue);    // newQuantity
                pstInsert.setString(4, motive);         // adjustmentMotive
                pstInsert.setString(5, productID);      // product_pk

                int rowsAffectedInsert = pstInsert.executeUpdate();

                if (rowsAffectedInsert > 0) {
                    // Actualizar el stock en la tabla products
                    pstUpdate.setLong(1, newStockValue); // nueva cantidad
                    pstUpdate.setString(2, productID);   // producto a actualizar

                    int rowsAffectedUpdate = pstUpdate.executeUpdate();

                    if (rowsAffectedUpdate > 0) {
                        JOptionPane.showMessageDialog(null, "Ajuste de inventario registrado y stock actualizado con éxito.");
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo actualizar el stock del producto.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo registrar el ajuste de inventario.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al procesar los valores numéricos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar en la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        new IncreaseStock().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(IncreaseStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IncreaseStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IncreaseStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IncreaseStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IncreaseStock().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable productsTable;
    private javax.swing.JTextField txtCurrentStock;
    private javax.swing.JTextField txtDescription;
    private javax.swing.JTextArea txtMotive;
    private javax.swing.JTextField txtProductBrand;
    private javax.swing.JTextField txtProductID;
    private javax.swing.JTextField txtProductLocation;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSellingPrice;
    private javax.swing.JTextField txtStockToIncrease;
    // End of variables declaration//GEN-END:variables
}
