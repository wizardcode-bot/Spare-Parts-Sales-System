package ui;

import dao.ConnectionProvider;
import javax.swing.JOptionPane;
import java.sql.*;
import common.Validations;
import static common.Validations.getIDByUsername;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Profile extends javax.swing.JFrame {

    private String username = "";
    private ImageIcon image;
    private Icon icon;
    private String ruta;
    private String ID;

    /**
     * Creates new form Profile
     */
    public Profile() {
        initComponents();
    }

    public Profile(String tempUsername) {
        initComponents();
        setSize(850, 500);
        username = tempUsername;
        setLocationRelativeTo(null);
    }

    private void setImage(JLabel lbl, String ruta) {
        //método para establecer icono en el label

        this.image = new ImageIcon(ruta);
        this.icon = new ImageIcon(
                this.image.getImage().getScaledInstance(
                        lbl.getWidth(),
                        lbl.getHeight(),
                        Image.SCALE_DEFAULT
                )
        );
        lbl.setIcon(this.icon);
        this.repaint();
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
        lblProfile = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtMobileNumber = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtDoB = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Mi Perfil");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(358, 6, -1, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 56, 850, 10));

        lblProfile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Viewprofile.png"))); // NOI18N
        lblProfile.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(lblProfile, new org.netbeans.lib.awtextra.AbsoluteConstraints(96, 134, 264, 219));

        lblUsername.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblUsername.setForeground(new java.awt.Color(0, 0, 0));
        lblUsername.setText("Nombre de Usuario");
        getContentPane().add(lblUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 378, -1, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Nombre");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 128, -1, -1));

        txtName.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 149, 300, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Número de teléfono");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 252, -1, -1));

        txtMobileNumber.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtMobileNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 273, 300, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Dirección");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 314, -1, -1));

        txtAddress.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 335, 300, -1));

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 0, 0));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        jButton2.setText("Actualizar");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 370, 120, -1));

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 0, 0));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/updatePassword.png"))); // NOI18N
        jButton3.setText("Actualizar contraseña");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 414, 190, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Fecha de nacimiento");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 190, -1, -1));

        txtDoB.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtDoB.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtDoB, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 211, 300, -1));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jLabel9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(804, 6, -1, -1));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/smallCamera.png"))); // NOI18N
        jButton1.setText("Establecer foto de perfil");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(127, 414, -1, -1));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/help.png"))); // NOI18N
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(768, 14, -1, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/whiteSmoke.jpg"))); // NOI18N
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        //mostrar datos en las casillas
        txtDoB.setEditable(false);

        String query = "SELECT name, dateOfBirth, mobileNumber, address FROM appusers WHERE username = ?";

        try (Connection con = ConnectionProvider.getCon(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, username);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    txtName.setText(rs.getString("name"));
                    txtDoB.setText(rs.getString("dateOfBirth"));
                    txtMobileNumber.setText(rs.getString("mobileNumber"));
                    txtAddress.setText(rs.getString("address"));
                    lblUsername.setText(username);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        // Mostrar imagen de perfil
        this.ID = getIDByUsername(username);
        ruta = "C:\\Users\\HOME\\Documents\\NetBeansProjects\\Inventory Management System\\src\\images\\profile\\" + this.ID + ".jpg";

        java.io.File file = new java.io.File(ruta);
        if (file.exists() && !file.isDirectory()) {
            this.setImage(this.lblProfile, ruta);
        } else {
            // Imagen por defecto
            String defaultRuta = "C:\\Users\\HOME\\Documents\\NetBeansProjects\\Inventory Management System\\src\\images\\Viewprofile.png";
            this.setImage(this.lblProfile, defaultRuta);
        }
    }//GEN-LAST:event_formComponentShown

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //botón actualizar

        String name = txtName.getText().trim();
        String mobileNumber = txtMobileNumber.getText().trim();
        String address = txtAddress.getText().trim();

        if (Validations.isNullOrBlank(name)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el nombre!", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        } else if (!name.matches(Validations.justLetters)) {
            JOptionPane.showMessageDialog(null, "¡El nombre debe contener solamente letras!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        } else if (Validations.isNullOrBlank(mobileNumber)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el número de teléfono!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        } else if (!mobileNumber.matches(Validations.numberPattern) || mobileNumber.length() != 10) {
            JOptionPane.showMessageDialog(null, "¡El número de teléfono no es válido, debe contener 10 dígitos!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        } else if (Validations.isNullOrBlank(address)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar la dirección!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        } else {
            String updateQuery = "UPDATE appusers SET name = ?, mobileNumber = ?, address = ? WHERE username = ?";

            try (Connection con = ConnectionProvider.getCon(); PreparedStatement ps = con.prepareStatement(updateQuery)) {

                ps.setString(1, name);
                ps.setString(2, mobileNumber);
                ps.setString(3, address);
                ps.setString(4, username);

                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "¡Perfil actualizado exitosamente!",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new Profile(username).setVisible(true);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al actualizar el perfil: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        new updatePassword(username).setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        dispose();
    }//GEN-LAST:event_jLabel9MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // botón para llamar la interfaz de la cámara
        new Camera(username).setVisible(true);
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
            java.util.logging.Logger.getLogger(Profile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Profile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Profile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Profile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Profile().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblProfile;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtDoB;
    private javax.swing.JTextField txtMobileNumber;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
}
