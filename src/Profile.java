
import dao.ConnectionProvider;
import javax.swing.JOptionPane;
import java.sql.*;


public class Profile extends javax.swing.JFrame {
    
    public String emailPattern = "^[a-zA-Z0-9]+[@]+[a-zA-Z0-9]+[.]+[a-zA-Z0-9]+$";
    public String mobileNumberPattern = "^[0-9]*$";
    private String username="";

    /**
     * Creates new form Profile
     */
    public Profile() {
        initComponents();
    }
    
    public Profile(String tempUsername) {
        initComponents();
        username = tempUsername;
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
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtMobileNumber = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtDoB = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();

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
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 6, -1, -1));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(804, 6, 40, 40));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 59, 850, 10));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Viewprofile.png"))); // NOI18N
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 149, 190, 130));

        lblUsername.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblUsername.setForeground(new java.awt.Color(0, 0, 0));
        lblUsername.setText("Nombre de Usuario");
        getContentPane().add(lblUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 306, -1, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Nombre");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 140, -1, -1));

        txtName.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 161, 300, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Número de teléfono");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 264, -1, -1));

        txtMobileNumber.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtMobileNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 285, 300, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Dirección");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 326, -1, -1));

        txtAddress.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 347, 300, -1));

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 0, 0));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        jButton2.setText("Actualizar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 382, 120, -1));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/help.png"))); // NOI18N
        jButton4.setBorder(null);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 10, 40, 30));

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 0, 0));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/updatePassword.png"))); // NOI18N
        jButton3.setText("Actualizar contraseña");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 378, 190, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Fecha de nacimiento");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 202, -1, -1));

        txtDoB.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtDoB.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtDoB, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 223, 300, -1));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/all_pages_background.png"))); // NOI18N
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:
        try{
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from appuser where username='"+username+"'");
            
            while(rs.next()){
                txtName.setText(rs.getString("name"));
                txtDoB.setText(rs.getString("dob"));
                txtMobileNumber.setText(rs.getString("mobileNumber"));
                txtAddress.setText(rs.getString("address"));
                lblUsername.setText(username);
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_formComponentShown

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        String name = txtName.getText();
        String mobileNumber = txtMobileNumber.getText();
        String address = txtAddress.getText();
        
        if(name.equals("")){
             JOptionPane.showMessageDialog(null, "¡Debes ingresar el nombre!");
        } else if (mobileNumber.equals("")) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el número de teléfono!");
        } else if (!mobileNumber.matches(mobileNumberPattern) || mobileNumber.length() != 10) {
            JOptionPane.showMessageDialog(null, "¡El número de teléfono es inválido!");
        } else if (address.equals("")) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar la dirección!");
        } else {
            try{
                Connection con = ConnectionProvider.getCon();
                PreparedStatement ps = con.prepareStatement("update appuser set name=?, mobileNumber=?, address=? where username=?");
                ps.setString(1, name);
                ps.setString(2, mobileNumber);
                ps.setString(3, address);
                ps.setString(4, username);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "¡Perfil actualizado exitosamente!");
                setVisible(false);
                new Profile(username).setVisible(true);
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        new ProfileHelp().setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        new updatePassword(username).setVisible(true);
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
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtDoB;
    private javax.swing.JTextField txtMobileNumber;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
}
