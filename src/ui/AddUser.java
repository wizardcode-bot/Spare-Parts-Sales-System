package ui;

import dao.ConnectionProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.util.Date;
import org.mindrot.jbcrypt.BCrypt;
import common.Validations;

public class AddUser extends javax.swing.JFrame {

    private boolean checkUsername = false;
    private boolean checkID = false;
    private String hashedPassword = "";

    /**
     * Creates new form AddUser
     */
    public AddUser() {
        initComponents();
        userIconLabel.setVisible(false);
        IDiconLabel.setVisible(false);
        setSize(850, 500);
        setLocationRelativeTo(null);
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
        jLabel2 = new javax.swing.JLabel();
        comboUserRole = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        dateDOB = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        txtMobileNumber = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtIDcard = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        userIconLabel = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        IDiconLabel = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Añadir Usuario");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(295, 9, -1, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 59, 850, 10));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Rol de usuario *");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 120, -1, -1));

        comboUserRole.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        comboUserRole.setForeground(new java.awt.Color(255, 255, 255));
        comboUserRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administrador", "Vendedor" }));
        getContentPane().add(comboUserRole, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 142, 300, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Nombre *");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 186, -1, -1));

        txtName.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 214, 300, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Fecha de Nacimiento *");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 256, -1, -1));

        dateDOB.setForeground(new java.awt.Color(255, 255, 255));
        dateDOB.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(dateDOB, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 284, 300, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Número de teléfono *");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 326, -1, -1));

        txtMobileNumber.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtMobileNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 354, 300, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Número de cédula (sin puntos ni espacios) *");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 120, -1, -1));

        txtIDcard.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtIDcard.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtIDcardKeyReleased(evt);
            }
        });
        getContentPane().add(txtIDcard, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 143, 300, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Nombre de usuario *");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 186, -1, -1));

        txtUsername.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUsernameKeyReleased(evt);
            }
        });
        getContentPane().add(txtUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 214, 300, -1));

        userIconLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        userIconLabel.setForeground(new java.awt.Color(0, 0, 0));
        userIconLabel.setText("---");
        getContentPane().add(userIconLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(778, 218, -1, -1));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Contraseña *");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 256, -1, -1));

        txtPassword.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPasswordActionPerformed(evt);
            }
        });
        getContentPane().add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 284, 300, -1));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Dirección *");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 326, -1, -1));

        txtAddress.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 354, 300, -1));

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
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 383, 110, -1));
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        IDiconLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        IDiconLabel.setForeground(new java.awt.Color(0, 0, 0));
        IDiconLabel.setText("---");
        getContentPane().add(IDiconLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(778, 147, -1, -1));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("(*) Indica campo obligatorio");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 450, -1, -1));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel14MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(804, 6, -1, -1));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/all_pages_background.png"))); // NOI18N
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPasswordActionPerformed

    private void txtUsernameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsernameKeyReleased
        // Verificar si el nombre de usuario ya existe
        String username = txtUsername.getText().trim();

        if (Validations.isNullOrBlank(username)) {
            userIconLabel.setVisible(false);
            checkUsername = false;
            return;
        }

        userIconLabel.setVisible(true);
        userIconLabel.setIcon(new ImageIcon("src\\images\\yes.png"));
        userIconLabel.setText("");
        checkUsername = false;

        String query = "SELECT 1 FROM appuser WHERE username = ?";
        try (Connection con = ConnectionProvider.getCon(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, username);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    checkUsername = true;
                    userIconLabel.setIcon(new ImageIcon("src\\images\\no.png"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al verificar el nombre de usuario: " + e.getMessage());
        }
    }//GEN-LAST:event_txtUsernameKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // método para guardar los datos
        String userRole = (String) comboUserRole.getSelectedItem();
        String name = txtName.getText().trim();
        SimpleDateFormat dFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = dateDOB.getDate();
        String dob = (date != null) ? dFormat.format(date) : "";
        String mobileNumber = txtMobileNumber.getText().trim();
        String IDcard = txtIDcard.getText().trim();
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String address = txtAddress.getText().trim();

        if (Validations.isNullOrBlank(name)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el nombre del usuario!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!name.matches(Validations.justLetters)) {
            JOptionPane.showMessageDialog(null, "¡El nombre debe contener solamente letras!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (Validations.isNullOrBlank(dob)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar la fecha de nacimiento!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (Validations.isNullOrBlank(mobileNumber)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el número de teléfono!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!mobileNumber.matches(Validations.numberPattern) || mobileNumber.length() != 10) {
            JOptionPane.showMessageDialog(null, "¡El número de teléfono no es válido, debe contener 10 dígitos!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (Validations.isNullOrBlank(IDcard)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el número de cédula!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!IDcard.matches(Validations.numberPattern) || IDcard.length() < 6 || IDcard.length() > 10) {
            JOptionPane.showMessageDialog(null, "¡El número de cédula no es válido, debe contener entre 6 a 10 digitos!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (checkID) {
            JOptionPane.showMessageDialog(null, "¡El número de cédula ya existe!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (Validations.isNullOrBlank(username)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el nombre de usuario!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (Validations.isNullOrBlank(password)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar la contraseña!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (Validations.isNullOrBlank(address)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar la dirección!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (checkUsername) {
            JOptionPane.showMessageDialog(null, "¡El nombre de usuario ya existe!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "INSERT INTO appuser (userRole, name, dob, mobileNumber, IDcard, username, password, address) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection con = ConnectionProvider.getCon(); PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, userRole);
            ps.setString(2, name);
            ps.setString(3, dob);
            ps.setString(4, mobileNumber);
            ps.setString(5, IDcard);
            ps.setString(6, username);
            ps.setString(7, hashedPassword);
            ps.setString(8, address);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "¡Usuario agregado exitosamente!",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            setVisible(false);
            new AddUser().setVisible(true);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar el usuario: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtIDcardKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIDcardKeyReleased
        String idCard = txtIDcard.getText().trim();

        if (Validations.isNullOrBlank(idCard) || !idCard.matches(Validations.numberPattern)
                || idCard.length() < 6 || idCard.length() > 10) {
            IDiconLabel.setVisible(false);
            return;
        }

        String query = "SELECT 1 FROM appuser WHERE IDcard = ?";
        try (Connection con = ConnectionProvider.getCon(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, idCard);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    checkID = true;
                    IDiconLabel.setVisible(true);
                    IDiconLabel.setIcon(new ImageIcon("src\\images\\no.png"));
                    IDiconLabel.setText("");
                } else {
                    checkID = false;
                    IDiconLabel.setVisible(true);
                    IDiconLabel.setIcon(new ImageIcon("src\\images\\yes.png"));
                    IDiconLabel.setText("");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en la base de datos: " + e.getMessage());
        }
    }//GEN-LAST:event_txtIDcardKeyReleased

    private void jLabel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseClicked
        setVisible(false);
    }//GEN-LAST:event_jLabel14MouseClicked

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
            java.util.logging.Logger.getLogger(AddUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddUser().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel IDiconLabel;
    private javax.swing.JComboBox<String> comboUserRole;
    private com.toedter.calendar.JDateChooser dateDOB;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtIDcard;
    private javax.swing.JTextField txtMobileNumber;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtUsername;
    private javax.swing.JLabel userIconLabel;
    // End of variables declaration//GEN-END:variables
}
