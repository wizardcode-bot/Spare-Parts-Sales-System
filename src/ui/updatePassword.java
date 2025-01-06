package ui;


import dao.ConnectionProvider;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;
import common.Validations;

public class updatePassword extends javax.swing.JFrame {

    private String username = "";

    /**
     * Creates new form updatePassword
     */
    public updatePassword() {
        initComponents();
    }

    public updatePassword(String tempUsername) {
        initComponents();
        username = tempUsername;
        setSize(480, 300);
        setLocationRelativeTo(null);
    }

    public void updatePassword(String username, String currentPassword, String newPassword) {

        String verifyQuery = "SELECT password FROM appusers WHERE username = ?";
        String updateQuery = "UPDATE appusers SET password = ? WHERE username = ?";

        try (Connection con = ConnectionProvider.getCon(); PreparedStatement verifyStmt = con.prepareStatement(verifyQuery); 
                PreparedStatement updateStmt = con.prepareStatement(updateQuery)) {

            verifyStmt.setString(1, username);
            try (ResultSet rs = verifyStmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");

                    if (BCrypt.checkpw(currentPassword, storedPassword)) {
                        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

                        updateStmt.setString(1, hashedPassword);
                        updateStmt.setString(2, username);

                        int rowsUpdated = updateStmt.executeUpdate();
                        if (rowsUpdated > 0) {
                            JOptionPane.showMessageDialog(null, "¡Contraseña actualizada exitosamente!",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "Error al actualizar la contraseña. Inténtalo nuevamente.", "Error", 
                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "¡La contraseña actual es incorrecta!", "Error", 
                    JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "¡El usuario no existe!", "Error", 
                    JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtCurrentPassword = new javax.swing.JTextField();
        txtNewPassword = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Actualizar Contraseña");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 6, -1, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Ingrese la contraseña actual");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(89, 104, -1, -1));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Ingrese la nueva contraseña");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(89, 174, -1, -1));

        txtCurrentPassword.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtCurrentPassword.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtCurrentPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(89, 124, 300, -1));

        txtNewPassword.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtNewPassword.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtNewPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(89, 194, 300, -1));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        jButton1.setText("Actualizar");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton1.setIconTextGap(8);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(89, 224, 120, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 51, 480, 10));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/smallClose.png"))); // NOI18N
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(442, 6, -1, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/background.jpg"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 480, 300));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //botón para guardar
        String currentPassword = txtCurrentPassword.getText().trim();
        String newPassword = txtNewPassword.getText().trim();

        if (username != null && !currentPassword.isBlank() && !Validations.isNullOrBlank(newPassword)) {
            updatePassword(username, currentPassword, newPassword);
        } else {
            JOptionPane.showMessageDialog(null, "¡Todos los campos son obligatorios!", "Advertencia", 
                    JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        dispose();
    }//GEN-LAST:event_jLabel4MouseClicked

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
            java.util.logging.Logger.getLogger(updatePassword.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(updatePassword.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(updatePassword.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(updatePassword.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new updatePassword().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField txtCurrentPassword;
    private javax.swing.JTextField txtNewPassword;
    // End of variables declaration//GEN-END:variables
}
