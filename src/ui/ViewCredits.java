package ui;

import dao.ConnectionProvider;
import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import ui.help.ViewCreditsHelp;

/**
 *
 * @author HOME
 */
public class ViewCredits extends javax.swing.JFrame {

    /**
     * Creates new form ViewCredits
     */
    public ViewCredits() {
        initComponents();
        setSize(950, 500);
        setLocationRelativeTo(null);
        loadCredits("", comboCreditState.getSelectedItem().toString());

        //establecer icono
        setImage();

        txtClientName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                loadCredits(txtClientName.getText(), comboCreditState.getSelectedItem().toString());
            }
        });

        comboCreditState.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadCredits(txtClientName.getText(), comboCreditState.getSelectedItem().toString());
            }
        });

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

    private void loadCredits(String clientName, String creditState) {
        DefaultTableModel model = (DefaultTableModel) tblCredits.getModel();
        model.setRowCount(0); // Limpiar la tabla

        String query = "SELECT cc.clientsCredits_pk, c.name, cc.totalCredit, cc.pendingBalance, "
                + "cc.creditDate, cc.paymentDeadline, cc.creditState "
                + "FROM clients_credits cc "
                + "INNER JOIN clients c ON cc.client_pk = c.client_pk "
                + "WHERE (? = '' OR c.name LIKE ?) ";

        // Si no es "Todas", agregamos el filtro por estado
        if (!"Todas".equalsIgnoreCase(creditState)) {
            query += "AND cc.creditState = ? ";
        }

        try (Connection conn = ConnectionProvider.getCon(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, clientName);
            ps.setString(2, "%" + clientName + "%");

            // Si no es "Todas", asignamos el valor de estado
            if (!"Todas".equalsIgnoreCase(creditState)) {
                ps.setString(3, creditState);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getLong("clientsCredits_pk"),
                    rs.getString("name"),
                    rs.getLong("totalCredit"),
                    rs.getLong("pendingBalance"),
                    rs.getTimestamp("creditDate").toLocalDateTime().toLocalDate(),
                    rs.getTimestamp("paymentDeadline").toLocalDateTime().toLocalDate(),
                    rs.getString("creditState")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tblCredits.getColumnModel().getColumn(5).setCellRenderer(new DeadlineCellRenderer());
    }

    public class DeadlineCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value instanceof LocalDate) {
                LocalDate deadlineDate = (LocalDate) value;
                LocalDate today = LocalDate.now();

                // Obtener el estado del crédito (columna 6 en la tabla)
                String creditState = table.getValueAt(row, 6).toString();

                // Si el estado es "Pendiente" y la fecha límite ya pasó o es hoy, se pinta en rojo
                if ("Pendiente".equalsIgnoreCase(creditState) && !deadlineDate.isAfter(today)) {
                    cell.setForeground(Color.RED);
                } else {
                    cell.setForeground(Color.BLACK);
                }
            }
            return cell;
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
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        txtClientName = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCredits = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        comboCreditState = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Créditos Registrados");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(285, 6, -1, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel2MouseReleased(evt);
            }
        });
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(904, 6, -1, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 56, 950, 10));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Filtrar por nombre de cliente");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 77, -1, -1));

        txtClientName.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtClientName, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 70, 200, -1));

        tblCredits.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblCredits.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Cliente", "Valor del crédito", "Valor pendiente", "Fecha del crédito", "Fecha límite de pago", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCredits.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblCredits.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblCreditsMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblCredits);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 109, 938, 352));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Selecciona el crédito a modificar");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(374, 467, -1, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Filtrar por estado");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(445, 77, -1, -1));

        comboCreditState.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        comboCreditState.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "Pendiente", "Pagado" }));
        comboCreditState.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getContentPane().add(comboCreditState, new org.netbeans.lib.awtextra.AbsoluteConstraints(557, 72, 205, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/help.png"))); // NOI18N
        jLabel7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel7MouseReleased(evt);
            }
        });
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(868, 14, -1, -1));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/whiteSmoke950.jpg"))); // NOI18N
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseReleased
        dispose();
    }//GEN-LAST:event_jLabel2MouseReleased

    private void tblCreditsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCreditsMouseReleased
        // acción a realizar al seleccionar fila de la tabla

        int selectedRow = tblCredits.getSelectedRow();
        if (selectedRow >= 0) {
            // Obtener datos de la fila seleccionada
            String creditID = tblCredits.getValueAt(selectedRow, 0).toString();
            String clientName = tblCredits.getValueAt(selectedRow, 1).toString();
            String totalCreditValue = tblCredits.getValueAt(selectedRow, 2).toString();
            String pendingBalance = tblCredits.getValueAt(selectedRow, 3).toString();
            String creditDate = tblCredits.getValueAt(selectedRow, 4).toString();
            String creditDeadline = tblCredits.getValueAt(selectedRow, 5).toString();
            String creditState = tblCredits.getValueAt(selectedRow, 6).toString();

            // Abrir ventana UpdateCredit con los datos
            UpdateCredit updateCreditForm = new UpdateCredit();
            updateCreditForm.setCreditData(creditID, clientName, totalCreditValue,
                    pendingBalance, creditDate, creditDeadline, creditState);
            updateCreditForm.setVisible(true);
            dispose();
        }
    }//GEN-LAST:event_tblCreditsMouseReleased

    private void jLabel7MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseReleased
        new ViewCreditsHelp().setVisible(true);
    }//GEN-LAST:event_jLabel7MouseReleased

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
            java.util.logging.Logger.getLogger(ViewCredits.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewCredits.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewCredits.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewCredits.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewCredits().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> comboCreditState;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable tblCredits;
    private javax.swing.JTextField txtClientName;
    // End of variables declaration//GEN-END:variables
}
