package ui;

import dao.ConnectionProvider;
import javax.swing.JOptionPane;
import java.sql.*;
import common.Validations;
import ui.help.UpdateProductHelp;

public class UpdateProduct extends javax.swing.JFrame {

    /**
     * Creates new form ActualizarProducto
     */
    public UpdateProduct() {
        initComponents();
        loadCategories();
        setSize(850,500);
        setLocationRelativeTo(null);
    }

    private void loadCategories() {
        //cargar las categorías en el jcomboBox
        String query = "SELECT categoryName FROM productCategories";
        try (Connection con = ConnectionProvider.getCon(); PreparedStatement pst = con.prepareStatement(query); ResultSet rs = pst.executeQuery()) {

            comboCategory.removeAllItems();
            comboCategory.addItem("Seleccionar");

            while (rs.next()) {
                comboCategory.addItem(rs.getString("categoryName"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar las categorías: " + e.getMessage());
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        txtProductId = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtDescription = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtProductBrand = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtQuantity = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        txtSellingPrice = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        comboCategory = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtNewCategory = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        txtAcquiredPrice = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtProductLocation = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtAddQuantity = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Actualizar Producto");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(239, 6, -1, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 56, 850, 10));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Código del Producto");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 103, -1, -1));

        txtProductId.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtProductId, new org.netbeans.lib.awtextra.AbsoluteConstraints(222, 94, 260, -1));

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 0, 0));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search.png"))); // NOI18N
        jButton2.setText("Buscar");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(488, 94, 100, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Descripción");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 315, -1, -1));

        txtDescription.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtDescription.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 335, 300, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Marca");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 375, -1, -1));

        txtProductBrand.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtProductBrand.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtProductBrand, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 395, 300, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Cantidad en inventario");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 182, -1, -1));

        txtQuantity.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtQuantity.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 200, 144, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Precio de venta (Unidad)");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 315, -1, -1));

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 0, 0));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        jButton3.setText("Actualizar");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton3.setIconTextGap(8);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 425, 120, -1));

        txtSellingPrice.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtSellingPrice.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtSellingPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 335, 300, -1));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Seleccione una categoría");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 182, -1, -1));

        comboCategory.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        comboCategory.setForeground(new java.awt.Color(0, 0, 0));
        comboCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(comboCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 200, 300, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Añadir nueva categoría");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 240, -1, -1));

        txtNewCategory.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtNewCategory.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtNewCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 266, 200, -1));

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(0, 0, 0));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/addIcon.png"))); // NOI18N
        jButton4.setText("Añadir");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(296, 263, 100, -1));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Precio Adquirido");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 237, -1, 20));

        txtAcquiredPrice.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtAcquiredPrice.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtAcquiredPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 263, 300, -1));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Ubicación en almacén");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 375, -1, -1));

        txtProductLocation.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtProductLocation.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtProductLocation, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 395, 300, -1));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Añadir cantidad");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 182, -1, -1));

        txtAddQuantity.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtAddQuantity.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtAddQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(616, 200, 140, -1));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jLabel14.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel14MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(804, 6, -1, -1));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/copy.png"))); // NOI18N
        jButton1.setText("Copiar código");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(594, 96, -1, -1));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/help.png"))); // NOI18N
        jLabel13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel13MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(768, 14, -1, -1));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/whiteSmoke.jpg"))); // NOI18N
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //botón para buscar datos del producto
        String uniqueId = txtProductId.getText().trim();

        if (Validations.isNullOrBlank(uniqueId)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el ID del producto!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            String query = "SELECT p.*, c.categoryName FROM products p "
                    + "INNER JOIN productCategories c ON p.category_pk = c.category_pk WHERE p.product_pk = ?";
            try (Connection con = ConnectionProvider.getCon(); PreparedStatement ps = con.prepareStatement(query)) {

                ps.setString(1, uniqueId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        txtProductId.setEditable(false);
                        txtDescription.setText(rs.getString("description"));
                        txtProductBrand.setText(rs.getString("productBrand"));
                        txtQuantity.setText(rs.getString("quantity"));
                        txtAcquiredPrice.setText(rs.getString("acquiredPrice"));
                        txtSellingPrice.setText(rs.getString("sellingPrice"));
                        txtProductLocation.setText(rs.getString("productLocation"));
                        txtQuantity.setEditable(false);

                        // Cargar la categoría en el JComboBox
                        String categoryName = rs.getString("categoryName");
                        comboCategory.setSelectedItem(categoryName); // Selecciona el nombre en el JComboBox

                    } else {
                        JOptionPane.showMessageDialog(null, "¡El ID de producto ingresado no existe!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // botón para guardar
        String uniqueId = txtProductId.getText().trim();
        String description = txtDescription.getText().trim();
        String productBrand = txtProductBrand.getText().trim();
        String quantity = txtQuantity.getText().trim();
        String acquiredPrice = txtAcquiredPrice.getText().trim();
        String sellingPrice = txtSellingPrice.getText().trim();
        String addQuantity = txtAddQuantity.getText().trim();
        String productLocation = txtProductLocation.getText().trim();
        String selectedCategory = comboCategory.getSelectedItem().toString().trim();

        try (Connection con = ConnectionProvider.getCon()) {
            // Verificar si el uniqueId existe en la tabla products
            String checkUniqueIdQuery = "SELECT COUNT(*) FROM products WHERE product_pk = ?";
            try (PreparedStatement checkStmt = con.prepareStatement(checkUniqueIdQuery)) {
                checkStmt.setString(1, uniqueId);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next() && rs.getInt(1) == 0) {
                    // Si no existe, mostrar mensaje y salir
                    JOptionPane.showMessageDialog(null, "El código de producto no existe. Verifica el ID ingresado.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (Validations.isNullOrBlank(uniqueId)) {
                JOptionPane.showMessageDialog(null, "¡Debes ingresar el ID del producto!", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (Validations.isNullOrBlank(description)) {
                JOptionPane.showMessageDialog(null, "¡Debes ingresar la descripción del producto!", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!addQuantity.matches(Validations.NUMBER_PATTERN)) {
                JOptionPane.showMessageDialog(null, "¡Debes ingresar la cantidad del producto en números!", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (Validations.isNullOrBlank(acquiredPrice)) {
                JOptionPane.showMessageDialog(null, "¡Debes ingresar el precio de adquisición del producto!", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!acquiredPrice.matches(Validations.NUMBER_PATTERN)) {
                JOptionPane.showMessageDialog(null, "¡Debes ingresar el precio de adquisición del producto en números!", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (Validations.isNullOrBlank(sellingPrice)) {
                JOptionPane.showMessageDialog(null, "¡Debes ingresar el precio de venta del producto!", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!sellingPrice.matches(Validations.NUMBER_PATTERN)) {
                JOptionPane.showMessageDialog(null, "¡Debes ingresar el precio de venta del producto en números!", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (Validations.isNullOrBlank(productLocation)) {
                JOptionPane.showMessageDialog(null, "¡Debes ingresar la ubicación del producto en el almacén!", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (Validations.isNullOrBlank(selectedCategory) || selectedCategory.equals("Seleccionar")) {
                JOptionPane.showMessageDialog(null, "¡Debes elegir una categoría para el producto!", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (Validations.isNullOrBlank(productBrand)) {
                productBrand = "No registrado";
            }

            // SE SUMA LA CANTIDAD INGRESADA CON LA YA EXISTENTE
            int totalQuantity = 0;
            if (Validations.isNullOrBlank(addQuantity)) {
                totalQuantity = Integer.parseInt(quantity);
            } else {
                totalQuantity = Integer.parseInt(quantity) + Integer.parseInt(addQuantity);
            }

            if (Validations.isNullOrBlank(productBrand)) {
                productBrand = "No registrado";
            }

            // Consultar la llave primaria de la categoría seleccionada
            String getCategoryPKQuery = "SELECT category_pk FROM productCategories WHERE categoryName = ?";
            try (PreparedStatement getCategoryPKStmt = con.prepareStatement(getCategoryPKQuery)) {
                getCategoryPKStmt.setString(1, selectedCategory);
                ResultSet rs = getCategoryPKStmt.executeQuery();

                if (rs.next()) {
                    int categoryPK = rs.getInt("category_pk");

                    // Actualizar los datos del producto en la tabla products
                    String updateQuery = "UPDATE products SET category_pk = ?, description = ?, productBrand = ?, quantity = ?, acquiredPrice = ?, "
                            + "sellingPrice = ?, productLocation = ? WHERE product_pk = ?";
                    try (PreparedStatement ps = con.prepareStatement(updateQuery)) {
                        ps.setInt(1, categoryPK); // Usar la llave primaria de la categoría
                        ps.setString(2, description);
                        ps.setString(3, productBrand);
                        ps.setInt(4, totalQuantity);
                        ps.setString(5, acquiredPrice);
                        ps.setString(6, sellingPrice);
                        ps.setString(7, productLocation);
                        ps.setString(8, uniqueId);

                        ps.executeUpdate();
                        JOptionPane.showMessageDialog(null, "¡Producto actualizado exitosamente!",
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        new UpdateProduct().setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "¡Categoría no encontrada en la base de datos!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el producto: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        //botón para añadir nueva categoría de productos
        String newCategory = txtNewCategory.getText();

        if (newCategory != null && !newCategory.isEmpty()) {

            String checkQuery = "SELECT COUNT(*) FROM productCategories WHERE categoryName = ?";
            String insertQuery = "INSERT INTO productCategories(categoryName) VALUES (?)";

            try (Connection con = ConnectionProvider.getCon(); PreparedStatement checkStmt = con.prepareStatement(checkQuery); PreparedStatement insertStmt = con.prepareStatement(insertQuery)) {

                // Verificar si la categoría ya existe
                checkStmt.setString(1, newCategory);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(null, "La categoría ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Salir si ya existe
                }

                // Insertar la nueva categoría
                insertStmt.setString(1, newCategory);
                insertStmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "¡Nueva categoría añadida!",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                // Actualizar la lista de categorías y limpiar el campo de texto
                loadCategories();
                txtNewCategory.setText("");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al añadir la nueva categoría: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jLabel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseClicked
        dispose();
    }//GEN-LAST:event_jLabel14MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // interfaz de ID de productos
        new ProductIDS().setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jLabel13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel13MouseClicked
        new UpdateProductHelp().setVisible(true);
    }//GEN-LAST:event_jLabel13MouseClicked

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
            java.util.logging.Logger.getLogger(UpdateProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UpdateProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UpdateProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UpdateProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UpdateProduct().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox<String> comboCategory;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField txtAcquiredPrice;
    private javax.swing.JTextField txtAddQuantity;
    private javax.swing.JTextField txtDescription;
    private javax.swing.JTextField txtNewCategory;
    private javax.swing.JTextField txtProductBrand;
    private javax.swing.JTextField txtProductId;
    private javax.swing.JTextField txtProductLocation;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtSellingPrice;
    // End of variables declaration//GEN-END:variables
}
