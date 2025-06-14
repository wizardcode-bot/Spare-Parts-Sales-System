package ui;

import java.sql.*;
import dao.ConnectionProvider;
import javax.swing.JOptionPane;
import common.Validations;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import ui.help.UpdateClientHelp;

public class UpdateClient extends javax.swing.JFrame {

    /**
     * Creates new form UpdateClient
     */
    public UpdateClient() {
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
        txtIDCard = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtMobileNumber = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Actualizar Cliente");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(279, 6, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Número de cédula o NITdel cliente");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 123, -1, -1));

        txtIDCard.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtIDCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(309, 120, 300, -1));

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
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(617, 117, 100, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Nombre o razón social*");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 222, -1, -1));

        txtName.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 243, 300, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Teléfono");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 316, -1, -1));

        txtMobileNumber.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtMobileNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMobileNumberActionPerformed(evt);
            }
        });
        getContentPane().add(txtMobileNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 337, 300, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Dirección");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(447, 316, -1, -1));

        txtAddress.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(447, 337, 300, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Correo");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(447, 221, -1, -1));

        txtEmail.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(447, 242, 300, -1));

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
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(447, 372, 120, -1));
        getContentPane().add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 56, 850, 10));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jLabel9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(804, 6, -1, -1));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/help.png"))); // NOI18N
        jLabel8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel8MouseReleased(evt);
            }
        });
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(768, 14, -1, -1));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("(*) Indica campo obligatorio");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 460, -1, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/whiteSmoke.jpg"))); // NOI18N
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtMobileNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMobileNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMobileNumberActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Botón para buscar cliente
        String idCard = txtIDCard.getText().trim();

        if (Validations.isNullOrBlank(idCard)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar la cédula del cliente!",
                    "No hay clientes seleccionados", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String query = "SELECT name, mobileNumber, address, email FROM clients WHERE client_pk = ?";
        try (Connection con = ConnectionProvider.getCon(); PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, idCard);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    txtIDCard.setEditable(false);
                    txtName.setText(rs.getString("name"));
                    txtMobileNumber.setText(rs.getString("mobileNumber"));
                    txtAddress.setText(rs.getString("address"));
                    txtEmail.setText(rs.getString("email"));
                } else {
                    JOptionPane.showMessageDialog(null, "¡Este cliente no está registrado!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar el cliente: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Botón para guardar
        String name = txtName.getText().trim();
        String mobileNumber = txtMobileNumber.getText().trim();
        String address = txtAddress.getText().trim();
        String email = txtEmail.getText().trim();
        String idCard = txtIDCard.getText().trim();

        // Validaciones de campos
        if (Validations.isNullOrBlank(name)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el nombre o razón social del cliente!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!mobileNumber.equals("No registrado") && !Validations.isNullOrBlank(mobileNumber)) {
            if (!mobileNumber.matches(Validations.NUMBER_PATTERN) || mobileNumber.length() != 10) {
                JOptionPane.showMessageDialog(null, "¡El número de teléfono no es válido, debe contener 10 dígitos!", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        if (address.length() > 60) {
            JOptionPane.showMessageDialog(null, "¡Se recomienda que la dirección no contenga más de 60 caracteres!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.matches(Validations.EMAIL_PATTERN) && !email.equalsIgnoreCase("No registrado") && !Validations.isNullOrBlank(email)) {
            JOptionPane.showMessageDialog(null, "¡El correo electrónico no es válido!", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Consulta de actualización
        String query = "UPDATE clients SET name=?, mobileNumber=?, address=?, email=? WHERE client_pk=?";
        try (Connection con = ConnectionProvider.getCon(); PreparedStatement ps = con.prepareStatement(query)) {

            mobileNumber = Validations.isNullOrBlank(mobileNumber) ? "No registrado" : mobileNumber;
            address = Validations.isNullOrBlank(address) ? "No registrado" : address;
            email = Validations.isNullOrBlank(email) ? "No registrado" : email;

            ps.setString(1, name);
            ps.setString(2, mobileNumber);
            ps.setString(3, address);
            ps.setString(4, email);
            ps.setString(5, idCard);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "¡Cliente actualizado exitosamente!",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "¡No se encontró un cliente con esa cédula!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            dispose();
            new UpdateClient().setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        dispose();
    }//GEN-LAST:event_jLabel9MouseClicked

    private void jLabel8MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseReleased
        new UpdateClientHelp().setVisible(true);
    }//GEN-LAST:event_jLabel8MouseReleased

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
            java.util.logging.Logger.getLogger(UpdateClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UpdateClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UpdateClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UpdateClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UpdateClient().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtIDCard;
    private javax.swing.JTextField txtMobileNumber;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
}
