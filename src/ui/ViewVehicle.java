package ui;

import dao.ConnectionProvider;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import ui.help.ViewVehicleHelp;

public class ViewVehicle extends javax.swing.JFrame {
    
    private String username = null;

    /**
     * Creates new form ViewVehicle
     */
    public ViewVehicle() {
        initComponents();
    }
    public ViewVehicle(String tempUsername) {
        initComponents();
        username = tempUsername;
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

    private void loadAllData() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Limpia la tabla antes de cargar los datos
        String query = "SELECT m.motorbike_pk, m.brandName, m.model, m.cylinderCapacity, m.color, c.name AS clientName "
                + "FROM motorbikes m "
                + "JOIN clients c ON m.client_pk = c.client_pk";

        try (Connection con = ConnectionProvider.getCon(); PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("motorbike_pk"),
                    rs.getString("brandName"),
                    rs.getString("model"),
                    rs.getString("cylinderCapacity"),
                    rs.getString("color"),
                    rs.getString("clientName") // Obténer el nombre del cliente
                });
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

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtFilterPlate = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();

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
        jLabel1.setText("Vehículos registrados");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 6, -1, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Placa", "Marca", "Modelo", "Cilindraje", "Color", "Propietario"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 123, 838, 336));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Seleccione la fila del vehículo a eliminar");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(297, 471, -1, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Filtrar por número de placa");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 90, -1, -1));

        txtFilterPlate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtFilterPlate.setForeground(new java.awt.Color(0, 0, 0));
        txtFilterPlate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFilterPlateKeyReleased(evt);
            }
        });
        getContentPane().add(txtFilterPlate, new org.netbeans.lib.awtextra.AbsoluteConstraints(189, 86, 160, -1));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(804, 6, -1, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/help.png"))); // NOI18N
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(768, 14, -1, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 56, 850, 10));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/whiteSmoke.jpg"))); // NOI18N
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        loadAllData();
    }//GEN-LAST:event_formComponentShown

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // Eliminar vehículo seleccionado con validación de rol

        int index = jTable1.getSelectedRow();
        TableModel model = jTable1.getModel();
        String plate = model.getValueAt(index, 0).toString();

        // Consultar el rol del usuario actual
        String userRole = "";
        String roleQuery = "SELECT userRole FROM appusers WHERE username = ?";

        try (Connection con = ConnectionProvider.getCon(); PreparedStatement ps = con.prepareStatement(roleQuery)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userRole = rs.getString("userRole");
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró información para el usuario actual.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al consultar el rol del usuario: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar el rol
        if (!"Administrador".equalsIgnoreCase(userRole)) {
            JOptionPane.showMessageDialog(null, "No tienes permiso para eliminar vehículos.",
                    "Permiso denegado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirmar y eliminar vehículo
        int a = JOptionPane.showOptionDialog(null, "¿Quieres eliminar este vehículo?", "Selecciona una opción",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Sí", "No"}, "Sí");

        if (a == 0) {
            String query = "DELETE FROM motorbikes WHERE motorbike_pk = ?";
            try (Connection con = ConnectionProvider.getCon(); PreparedStatement ps = con.prepareStatement(query)) {

                ps.setString(1, plate); // Configura el ID en la consulta
                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "¡Vehículo eliminado exitosamente!");
                    // Refrescar la vista
                    ((DefaultTableModel) jTable1.getModel()).removeRow(index);
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el vehículo con la placa seleccionada.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al eliminar el vehículo: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void txtFilterPlateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFilterPlateKeyReleased
        //filtrar por número de placa

        String filterText = txtFilterPlate.getText().trim();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        String query;
        if (!filterText.isEmpty()) {
            query = "SELECT m.motorbike_pk, m.brandName, m.model, "
                    + "m.cylinderCapacity, m.color, c.name AS clientName "
                    + "FROM motorbikes m "
                    + "INNER JOIN clients c ON m.client_pk = c.client_pk "
                    + "WHERE m.motorbike_pk LIKE ?";
        } else {
            loadAllData();
            return;
        }

        try (Connection con = ConnectionProvider.getCon(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, "%" + filterText + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("motorbike_pk"),
                        rs.getString("brandName"),
                        rs.getString("model"),
                        rs.getString("cylinderCapacity"),
                        rs.getString("color"),
                        rs.getString("clientName") // obtener el nombre del propietario
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_txtFilterPlateKeyReleased

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        dispose();
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        new ViewVehicleHelp().setVisible(true);
    }//GEN-LAST:event_jLabel5MouseClicked

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
            java.util.logging.Logger.getLogger(ViewVehicle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewVehicle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewVehicle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewVehicle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewVehicle().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtFilterPlate;
    // End of variables declaration//GEN-END:variables
}
