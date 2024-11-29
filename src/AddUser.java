
import dao.ConnectionProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.util.Date;
import org.mindrot.jbcrypt.BCrypt;

public class AddUser extends javax.swing.JFrame {

    //public String emailPattern = "^[a-zA-Z0-9]+[@]+[a-zA-Z0-9]+[.]+[a-zA-Z0-9]+$";
    public String mobileNumberPattern = "^[0-9]*$";
    public int checkUsername = 0;
    public String hashedPassword  = "";

    /**
     * Creates new form AddUser
     */
    public AddUser() {
        initComponents();
        iconLabel.setVisible(false);
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
        iconLabel = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Añadir Usuario");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 10, -1, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 59, 850, 10));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Rol de usuario");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 120, -1, -1));

        comboUserRole.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        comboUserRole.setForeground(new java.awt.Color(255, 255, 255));
        comboUserRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administrador", "Vendedor", " " }));
        getContentPane().add(comboUserRole, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 142, 300, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Nombre");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 186, -1, -1));

        txtName.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 214, 300, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Fecha de Nacimiento");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 256, -1, -1));

        dateDOB.setForeground(new java.awt.Color(255, 255, 255));
        dateDOB.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(dateDOB, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 284, 300, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Número de teléfono");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 326, -1, -1));

        txtMobileNumber.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtMobileNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 354, 300, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Número de cédula (sin puntos ni espacios)");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 120, -1, -1));

        txtIDcard.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtIDcard, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 143, 300, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Nombre de usuario");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 186, -1, -1));

        txtUsername.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUsernameKeyReleased(evt);
            }
        });
        getContentPane().add(txtUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 214, 300, -1));

        iconLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        iconLabel.setForeground(new java.awt.Color(0, 0, 0));
        iconLabel.setText("---");
        getContentPane().add(iconLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(778, 218, -1, -1));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Contraseña");
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
        jLabel10.setText("Dirección");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 326, -1, -1));

        txtAddress.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 354, 300, -1));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        jButton1.setText("Guardar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 396, 100, -1));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/help.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 10, 40, 30));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(802, 6, 40, 40));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/all_pages_background.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 850, 500));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPasswordActionPerformed

    private void txtUsernameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsernameKeyReleased
        // TODO add your handling code here:
        String username = txtUsername.getText();
        if (!username.equals("")) {
            iconLabel.setVisible(true);
            iconLabel.setIcon(new ImageIcon("src\\images\\yes.png"));
            iconLabel.setText("");
            checkUsername = 0;

            try {
                Connection con = ConnectionProvider.getCon();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select * from appuser where username = '" + username + "'");
                while (rs.next()) {
                    checkUsername = 1;
                    iconLabel.setIcon(new ImageIcon("src\\images\\no.png"));
                    iconLabel.setText("");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            iconLabel.setVisible(false);
        }
    }//GEN-LAST:event_txtUsernameKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String userRole = (String) comboUserRole.getSelectedItem();
        String name = txtName.getText();
        SimpleDateFormat dFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = dateDOB.getDate();
        String dob = "";

        if (date != null) {
            dob = dFormat.format(dateDOB.getDate());
        }
        String mobileNumber = txtMobileNumber.getText();
        String IDcard = txtIDcard.getText();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String address = txtAddress.getText();

        if (name.equals("")) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el nombre!");
        } else if (dob.equals("")) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar la fecha de nacimiento!");
        } else if (mobileNumber.equals("")) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el número de teléfono!");
        } else if (!mobileNumber.matches(mobileNumberPattern) || mobileNumber.length() != 10) {
            JOptionPane.showMessageDialog(null, "¡El número de teléfono no es válido!");
        } else if (IDcard.equals("")) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el número de cédula!");
        } else if (!IDcard.matches(mobileNumberPattern) || IDcard.length() > 10 || IDcard.length() < 6) {
            JOptionPane.showMessageDialog(null, "¡El número de cédula no es válido!");
        } else if (username.equals("")) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el nombre de usuario!");
        } else if (checkUsername == 1) {
            JOptionPane.showMessageDialog(null, "¡El nombre de usuario ya existe!");
        } else if (password.equals("")) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar la contraseña!");
        } else if (address.equals("")) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar la dirección!");
        }else{
            try{
                Connection con = ConnectionProvider.getCon();
                PreparedStatement ps = con.prepareStatement("insert into appuser (userRole, name, dob, mobileNumber, IDcard, username, password, address) values (?,?,?,?,?,?,?,?)");
                ps.setString(1, userRole);
                ps.setString(2, name);
                ps.setString(3, dob);
                ps.setString(4, mobileNumber);
                ps.setString(5, IDcard);
                ps.setString(6, username);
                ps.setString(7, hashedPassword);
                ps.setString(8, address);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "¡Usuario agregado exitosamente!");
                setVisible(false);
                new AddUser().setVisible(true);
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:

        new AddUserHelp().setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

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
    private javax.swing.JComboBox<String> comboUserRole;
    private com.toedter.calendar.JDateChooser dateDOB;
    private javax.swing.JLabel iconLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtIDcard;
    private javax.swing.JTextField txtMobileNumber;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
