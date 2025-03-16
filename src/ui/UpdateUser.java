package ui;

import ui.help.UpdateUserHelp;
import dao.ConnectionProvider;
import javax.swing.JOptionPane;
import java.sql.*;
import common.Validations;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class UpdateUser extends javax.swing.JFrame {

    /**
     * Creates new form UpdateUser
     */
    public UpdateUser() {
        initComponents();
        setSize(850, 500);
        setLocationRelativeTo(null);
        
        //establecer icono
        setImage();
    }
    
    //icono de la aplicación
    public void setImage() {
        try {
            InputStream imgStream = getClass().getResourceAsStream("/images/icono.png");
            if (imgStream != null) {
                setIconImage(ImageIO.read(imgStream));
            } else {
                System.out.println("Icono no encontrado");
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        jLabel2 = new javax.swing.JLabel();
        txtIDcard = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        comboUserRole = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtMobileNumber = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        txtUsername = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Actualizar Usuario");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(254, 6, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Número de cédula");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(133, 107, -1, -1));

        txtIDcard.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtIDcard, new org.netbeans.lib.awtextra.AbsoluteConstraints(254, 103, 300, -1));

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
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(566, 101, 100, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Rol del Usuario");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(96, 215, -1, -1));

        comboUserRole.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        comboUserRole.setForeground(new java.awt.Color(255, 255, 255));
        comboUserRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administrador", "Vendedor" }));
        comboUserRole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboUserRoleActionPerformed(evt);
            }
        });
        getContentPane().add(comboUserRole, new org.netbeans.lib.awtextra.AbsoluteConstraints(96, 237, 300, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Nombre");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(96, 288, -1, -1));

        txtName.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(96, 309, 300, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Nombre de usuario");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(96, 360, -1, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Número de teléfono");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 215, -1, -1));

        txtMobileNumber.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtMobileNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 238, 300, -1));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Dirección");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 288, -1, -1));

        txtAddress.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 309, 300, -1));

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
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(647, 347, 120, -1));

        txtUsername.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtUsername.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(96, 387, 300, -1));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(804, 6, -1, -1));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/help.png"))); // NOI18N
        jLabel9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(768, 14, -1, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 56, 850, 10));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/whiteSmoke.jpg"))); // NOI18N
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //buscar usuario por número de cédula
        String idCard = txtIDcard.getText().trim();

        // Validar campo vacío
        if (Validations.isNullOrBlank(idCard)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el número de cédula!",
                    "No hay usuarios seleccionados", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String query = "SELECT name, username, mobileNumber, address, userRole FROM appusers WHERE appuser_pk = ?";
        try (Connection con = ConnectionProvider.getCon(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, idCard);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    txtIDcard.setEditable(false);
                    txtName.setText(rs.getString("name"));
                    txtUsername.setText(rs.getString("username"));
                    txtMobileNumber.setText(rs.getString("mobileNumber"));
                    txtAddress.setText(rs.getString("address"));

                    // Configurar comboBox de roles
                    comboUserRole.removeAllItems();
                    String userRole = rs.getString("userRole");
                    if ("Administrador".equals(userRole)) {
                        comboUserRole.addItem("Administrador");
                        comboUserRole.addItem("Vendedor");
                    } else {
                        comboUserRole.addItem("Vendedor");
                        comboUserRole.addItem("Administrador");
                    }
                } else {
                    // Usuario no encontrado
                    JOptionPane.showMessageDialog(null, "¡El usuario no existe!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al buscar el usuario: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Botón para guardar
        String userRole = (String) comboUserRole.getSelectedItem();
        String name = txtName.getText().trim();
        String username = txtUsername.getText().trim();
        String mobileNumber = txtMobileNumber.getText().trim();
        String IDcard = txtIDcard.getText().trim();
        String address = txtAddress.getText().trim();

        if (Validations.isNullOrBlank(IDcard)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el número de cédula!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (userRole.equals("Vendedor")) {

            if (Validations.SUPPORT_ID.equals(IDcard) || Validations.ADMIN_ID.equals(IDcard)) {
                JOptionPane.showMessageDialog(null, "No se puede cambiar el rol de esta cuenta.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (!IDcard.matches(Validations.NUMBER_PATTERN) || IDcard.length() < 6 || IDcard.length() > 10) {
            JOptionPane.showMessageDialog(null, "¡El número de cédula no es válido, debe contener entre 6 a 10 digitos!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (Validations.isNullOrBlank(name)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el nombre!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!name.matches(Validations.JUST_LETTERS)) {
            JOptionPane.showMessageDialog(null, "¡El nombre solo puede contener letras y espacios!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (Validations.isNullOrBlank(username)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el nombre de usuario!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (username.length() > 40) {
            JOptionPane.showMessageDialog(null, "¡Se recomienda que el nombre de usuario no contenga más de 40 caracteres!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (Validations.isNullOrBlank(mobileNumber)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el número de teléfono!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!mobileNumber.matches(Validations.NUMBER_PATTERN) || mobileNumber.length() != 10) {
            JOptionPane.showMessageDialog(null, "¡El número de teléfono no es válido, debe contener 10 dígitos!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (Validations.isNullOrBlank(address)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar la dirección!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (address.length() > 60) {
            JOptionPane.showMessageDialog(null, "¡Se recomienda que la dirección no contenga más de 60 caracteres!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verificar si el nombre de usuario ya existe
        String checkUsernameQuery = "SELECT COUNT(*) FROM appusers WHERE username = ? AND appuser_pk != ?";
        try (Connection con = ConnectionProvider.getCon(); PreparedStatement psCheck = con.prepareStatement(checkUsernameQuery)) {

            psCheck.setString(1, username);  // Verificar el nombre de usuario ingresado
            psCheck.setString(2, IDcard);    // Excluir el usuario actual (con IDcard específico)

            try (ResultSet rsCheck = psCheck.executeQuery()) {
                rsCheck.next();
                int usernameCount = rsCheck.getInt(1);

                if (usernameCount > 0) {
                    JOptionPane.showMessageDialog(null, "¡El nombre de usuario ya está en uso!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;  // Salir si el nombre de usuario ya existe
                }
            }

            // Si el nombre de usuario es único, proceder con la actualización
            String query = "UPDATE appusers SET userRole=?, name=?, username=?, mobileNumber=?, address=? WHERE appuser_pk=?";
            try (PreparedStatement psUpdate = con.prepareStatement(query)) {
                psUpdate.setString(1, userRole);
                psUpdate.setString(2, name);
                psUpdate.setString(3, username);
                psUpdate.setString(4, mobileNumber);
                psUpdate.setString(5, address);
                psUpdate.setString(6, IDcard);

                int rowsUpdated = psUpdate.executeUpdate();

                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(null, "¡Usuario actualizado exitosamente!", "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new UpdateUser().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "¡No se encontró ningún usuario con la cédula especificada!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al actualizar: " + e.getMessage());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al verificar el nombre de usuario: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void comboUserRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboUserRoleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboUserRoleActionPerformed

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        dispose();
    }//GEN-LAST:event_jLabel10MouseClicked

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        new UpdateUserHelp().setVisible(true);
    }//GEN-LAST:event_jLabel9MouseClicked

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
            java.util.logging.Logger.getLogger(UpdateUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UpdateUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UpdateUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UpdateUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UpdateUser().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> comboUserRole;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtIDcard;
    private javax.swing.JTextField txtMobileNumber;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
