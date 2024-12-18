
import dao.ConnectionProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class AddVehicle extends javax.swing.JFrame {

    public boolean checkPlateExists = false;

    /**
     * Creates new form AddVehicle
     */
    public AddVehicle() {
        initComponents();
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
        txtPlate = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtBrandName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtModel = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtCylinder = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtColor = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtFilterClient = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        plateIcon = new javax.swing.JLabel();
        txtRelateClient = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setIconImages(null);
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
        jLabel1.setText("Registrar vehículo");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(256, 6, -1, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 56, 850, 10));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Número de placa *");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(69, 131, -1, -1));

        txtPlate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtPlate.setForeground(new java.awt.Color(0, 0, 0));
        txtPlate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPlateKeyReleased(evt);
            }
        });
        getContentPane().add(txtPlate, new org.netbeans.lib.awtextra.AbsoluteConstraints(69, 152, 300, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Marca *");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(69, 206, -1, -1));

        txtBrandName.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtBrandName.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtBrandName, new org.netbeans.lib.awtextra.AbsoluteConstraints(69, 226, 300, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Modelo");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(69, 281, -1, -1));

        txtModel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtModel.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtModel, new org.netbeans.lib.awtextra.AbsoluteConstraints(69, 299, 300, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Cilindraje");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(69, 357, -1, -1));

        txtCylinder.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtCylinder.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtCylinder, new org.netbeans.lib.awtextra.AbsoluteConstraints(69, 378, 300, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Color *");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(471, 131, -1, -1));

        txtColor.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtColor.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtColor, new org.netbeans.lib.awtextra.AbsoluteConstraints(471, 146, 300, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Relacionar propietario *");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(471, 206, -1, -1));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Filtrar por número de cédula");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(471, 281, -1, -1));

        txtFilterClient.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtFilterClient.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtFilterClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(471, 302, 200, -1));

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 0, 0));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        jButton2.setText("Guardar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(471, 331, 100, -1));

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 0, 0));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/filter.png"))); // NOI18N
        jButton3.setText("Filtrar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(677, 299, 94, -1));

        plateIcon.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        plateIcon.setForeground(new java.awt.Color(0, 0, 0));
        plateIcon.setText("---");
        getContentPane().add(plateIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 150, -1, -1));

        txtRelateClient.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtRelateClient.setForeground(new java.awt.Color(0, 0, 0));
        getContentPane().add(txtRelateClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(471, 226, 300, -1));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel9MouseReleased(evt);
            }
        });
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(804, 6, -1, -1));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("(*) Indica campo obligatorio");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(69, 441, -1, -1));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/all_pages_background.png"))); // NOI18N
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getAccessibleContext().setAccessibleName("registrar");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // filtrar clientes por número de cédula
        boolean checkClientExist = false;
        String filterClient = txtFilterClient.getText();
        if (filterClient.equals("")) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el número de cédula del cliente!", "No hay clientes seleccionados", JOptionPane.INFORMATION_MESSAGE);
        } else {

            try {
                Connection con = ConnectionProvider.getCon();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select name from clients where idCard = '" + filterClient + "'");
                if (rs.next()) {
                    checkClientExist = true;
                    txtRelateClient.setText(rs.getString("name"));
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
            if (!checkClientExist) {
                JOptionPane.showMessageDialog(null, "¡Este cliente no está registrado!", "Error", JOptionPane.ERROR_MESSAGE);
                //txtRelateClient.setText("");
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        txtRelateClient.setEditable(false);
    }//GEN-LAST:event_formComponentShown

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //botón de guardar
        String plate = txtPlate.getText();
        String brandName = txtBrandName.getText();
        String model = txtModel.getText();
        String cylinder = txtCylinder.getText();
        String color = txtColor.getText();
        String selectedClient = txtRelateClient.getText();
        String idCard = txtFilterClient.getText();
        
        if (plate.equals("")) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el número de placa!");
        } else if (brandName.equals("")) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar la marca del vehículo!");
        } else if (color.equals("")) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el color del vehículo!");
        } else if (selectedClient.equals("Seleccione un cliente")) {
            JOptionPane.showMessageDialog(null, "¡Debes relacionar el propietario del vehículo!");
        } else if(checkPlateExists) {
            JOptionPane.showMessageDialog(null, "¡El número de placa ya está registrado!");
        } else {
            String queryGetClientPk = "SELECT client_pk FROM clients WHERE name = ? and idCard=?";
            String queryInsertMotorbike = "INSERT INTO motorbikes (plate, brandName, model, cylinderCapacity, color, client_pk) VALUES (?,?,?,?,?,?)";

            try (Connection con = ConnectionProvider.getCon(); 
                PreparedStatement psGetClientPk = con.prepareStatement(queryGetClientPk); 
                PreparedStatement psInsertMotorbike = con.prepareStatement(queryInsertMotorbike)) {

                // Buscar client_pk del cliente seleccionado
                psGetClientPk.setString(1, selectedClient);
                psGetClientPk.setString(2, idCard);
                ResultSet rs = psGetClientPk.executeQuery();

                if (rs.next()) {
                    int clientPk = rs.getInt("client_pk");

                    // Reemplazar valores por defecto si están vacíos
                    if (model.equals("")) {
                        model = "No registrado";
                    }

                    if (cylinder.equals("")) {
                        cylinder = "No registrado";
                    }

                    // Preparar el INSERT en motorbikes
                    psInsertMotorbike.setString(1, plate);
                    psInsertMotorbike.setString(2, brandName);
                    psInsertMotorbike.setString(3, model);
                    psInsertMotorbike.setString(4, cylinder);
                    psInsertMotorbike.setString(5, color);
                    psInsertMotorbike.setInt(6, clientPk);

                    psInsertMotorbike.executeUpdate();

                    JOptionPane.showMessageDialog(null, "¡Vehículo agregado exitosamente!");
                    setVisible(false);
                    new AddVehicle().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "¡Cliente no encontrado en la base de datos!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtPlateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlateKeyReleased
        String plate = txtPlate.getText();

        if (!plate.equals("")) {
            plateIcon.setVisible(true);
            plateIcon.setIcon(new ImageIcon("src\\images\\yes.png"));
            plateIcon.setText("");
            checkPlateExists = false;

            String query = "SELECT plate FROM motorbikes WHERE plate = ?";
            try (Connection con = ConnectionProvider.getCon(); PreparedStatement pst = con.prepareStatement(query)) {

                pst.setString(1, plate);

                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        checkPlateExists = true;
                        plateIcon.setIcon(new ImageIcon("src\\images\\no.png"));
                        plateIcon.setText("");
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } else {
            plateIcon.setVisible(false);
        }
    }//GEN-LAST:event_txtPlateKeyReleased

    private void jLabel9MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseReleased
        setVisible(false);
    }//GEN-LAST:event_jLabel9MouseReleased

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
            java.util.logging.Logger.getLogger(AddVehicle.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddVehicle.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddVehicle.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddVehicle.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddVehicle().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel plateIcon;
    private javax.swing.JTextField txtBrandName;
    private javax.swing.JTextField txtColor;
    private javax.swing.JTextField txtCylinder;
    private javax.swing.JTextField txtFilterClient;
    private javax.swing.JTextField txtModel;
    private javax.swing.JTextField txtPlate;
    private javax.swing.JTextField txtRelateClient;
    // End of variables declaration//GEN-END:variables
}
