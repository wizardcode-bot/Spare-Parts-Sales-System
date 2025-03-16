package ui;

import dao.ConnectionProvider;
import java.sql.*;
import ui.help.CashFlowHelp;
import common.Validations;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author HOME
 */
public class CashFlow extends javax.swing.JFrame {

    /**
     * Creates new form CashFlow
     */
    public CashFlow() {
        initComponents();
        setSize(850, 500);
        setLocationRelativeTo(null);
        updateCashFlowData();
        
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

    private void updateCashFlowData() {
        Timestamp start = startDate.getDate() != null
                ? new Timestamp(startDate.getDate().getTime())
                : null;
        Timestamp end = endDate.getDate() != null
                ? new Timestamp(endDate.getDate().getTime() + 86399999L) // Sumar casi un día para incluir toda la fecha
                : null;

        String whereClause = (start != null && end != null)
                ? "WHERE date_column >= ? AND date_column <= ?" // Excluye el día siguiente
                : "WHERE DATE(date_column) = CURRENT_DATE";

        try (Connection con = ConnectionProvider.getCon()) {
            // 1. Sumar cashPaid en bills
            String sqlCashPaid = "SELECT COALESCE(SUM(cashPaid), 0) FROM bills " + whereClause.replace("date_column", "billDate");
            try (PreparedStatement ps = con.prepareStatement(sqlCashPaid)) {
                if (start != null && end != null) {
                    ps.setTimestamp(1, start);
                    ps.setTimestamp(2, end);
                }
                ResultSet rs = ps.executeQuery();
                lblCashPaid.setText("$ " + (rs.next() ? rs.getLong(1) : 0));
            }

            // 2. Sumar transferPaid en bills
            String sqlTransferPaid = "SELECT COALESCE(SUM(transferPaid), 0) FROM bills " + whereClause.replace("date_column", "billDate");
            try (PreparedStatement ps = con.prepareStatement(sqlTransferPaid)) {
                if (start != null && end != null) {
                    ps.setTimestamp(1, start);
                    ps.setTimestamp(2, end);
                }
                ResultSet rs = ps.executeQuery();
                lblTransferPaid.setText("$ " + (rs.next() ? rs.getLong(1) : 0));
            }

            // 3. Sumar moneyReceived en inventory_adjustments
            String sqlInventoryIncome = "SELECT COALESCE(SUM(moneyReceived), 0) FROM inventory_adjustments "
                    + whereClause.replace("date_column", "lastModified");
            try (PreparedStatement ps = con.prepareStatement(sqlInventoryIncome)) {
                if (start != null && end != null) {
                    ps.setTimestamp(1, start);
                    ps.setTimestamp(2, end);
                }
                ResultSet rs = ps.executeQuery();
                lblInventoryIncome.setText("$ " + (rs.next() ? rs.getLong(1) : 0));
            }

            // 4. Sumar expenseValue en expenses
            String sqlExpenses = "SELECT COALESCE(SUM(expenseValue), 0) FROM expenses " + whereClause.replace("date_column", "expenseDate");
            try (PreparedStatement ps = con.prepareStatement(sqlExpenses)) {
                if (start != null && end != null) {
                    ps.setTimestamp(1, start);
                    ps.setTimestamp(2, end);
                }
                ResultSet rs = ps.executeQuery();
                lblExpenses.setText("$ " + (rs.next() ? rs.getLong(1) : 0));
            }

            // 5. Sumar moneyPaid en inventory_adjustments
            String sqlInventoryExpenses = "SELECT COALESCE(SUM(moneyPaid), 0) FROM inventory_adjustments " + whereClause.replace("date_column", "lastModified");
            try (PreparedStatement ps = con.prepareStatement(sqlInventoryExpenses)) {
                if (start != null && end != null) {
                    ps.setTimestamp(1, start);
                    ps.setTimestamp(2, end);
                }
                ResultSet rs = ps.executeQuery();
                lblInventoryExpenses.setText("$ " + (rs.next() ? rs.getLong(1) : 0));
            }

            // 6. Sumar paymentAmount en credit_payments
            String sqlCreditPayments = (start != null && end != null)
                    ? "SELECT COALESCE(SUM(paymentAmount), 0) FROM credit_payments WHERE DATE(paymentDate) >= ? AND DATE(paymentDate) < ?"
                    : "SELECT COALESCE(SUM(paymentAmount), 0) FROM credit_payments WHERE DATE(paymentDate) = CURRENT_DATE";

            try (PreparedStatement ps = con.prepareStatement(sqlCreditPayments)) {
                if (start != null && end != null) {
                    ps.setDate(1, new java.sql.Date(start.getTime()));
                    ps.setDate(2, new java.sql.Date(end.getTime()));
                }
                ResultSet rs = ps.executeQuery();
                lblCreditPayments.setText("$ " + (rs.next() ? rs.getLong(1) : 0));
            }

        } catch (SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al cargar los datos: " + ex.getMessage());
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
        lblCashPaid = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblTransferPaid = new javax.swing.JLabel();
        lblInventoryIncome = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblExpenses = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        startDate = new com.toedter.calendar.JDateChooser();
        jLabel13 = new javax.swing.JLabel();
        endDate = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        lblInventoryExpenses = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        lblCreditPayments = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Flujo de dinero");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(281, 6, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Ingresos por ventas");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 155, -1, -1));

        lblCashPaid.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblCashPaid.setForeground(new java.awt.Color(0, 0, 0));
        lblCashPaid.setText("--:--");
        getContentPane().add(lblCashPaid, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 216, -1, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Intercambio de inventario");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 378, -1, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Efectivo");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 216, -1, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Transferencia");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 268, -1, -1));

        lblTransferPaid.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTransferPaid.setForeground(new java.awt.Color(0, 0, 0));
        lblTransferPaid.setText("--:--");
        getContentPane().add(lblTransferPaid, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 268, -1, -1));

        lblInventoryIncome.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblInventoryIncome.setForeground(new java.awt.Color(0, 0, 0));
        lblInventoryIncome.setText("--:--");
        getContentPane().add(lblInventoryIncome, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 418, -1, -1));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Gastos");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(522, 155, -1, -1));

        lblExpenses.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblExpenses.setForeground(new java.awt.Color(0, 0, 0));
        lblExpenses.setText("--:--");
        getContentPane().add(lblExpenses, new org.netbeans.lib.awtextra.AbsoluteConstraints(533, 216, -1, -1));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jLabel11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(804, 6, -1, -1));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("Desde");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 93, -1, -1));
        getContentPane().add(startDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 84, 160, 32));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("Hasta");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(311, 92, -1, -1));
        getContentPane().add(endDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(352, 84, 160, 32));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/filter.png"))); // NOI18N
        jButton1.setText("Filtrar");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(524, 88, 110, -1));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setText("Intercambio de inventario");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(522, 378, -1, -1));

        lblInventoryExpenses.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblInventoryExpenses.setForeground(new java.awt.Color(0, 0, 0));
        lblInventoryExpenses.setText("--:--");
        getContentPane().add(lblInventoryExpenses, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 418, -1, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/help.png"))); // NOI18N
        jLabel7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(768, 14, -1, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 56, 850, 10));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Pago de créditos");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 319, -1, -1));

        lblCreditPayments.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblCreditPayments.setForeground(new java.awt.Color(0, 0, 0));
        lblCreditPayments.setText("--:--");
        getContentPane().add(lblCreditPayments, new org.netbeans.lib.awtextra.AbsoluteConstraints(249, 319, -1, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/whiteSmoke.jpg"))); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
        dispose();
    }//GEN-LAST:event_jLabel11MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        updateCashFlowData();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        new CashFlowHelp().setVisible(true);
    }//GEN-LAST:event_jLabel7MouseClicked

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
            java.util.logging.Logger.getLogger(CashFlow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CashFlow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CashFlow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CashFlow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CashFlow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser endDate;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JLabel lblCashPaid;
    private javax.swing.JLabel lblCreditPayments;
    private javax.swing.JLabel lblExpenses;
    private javax.swing.JLabel lblInventoryExpenses;
    private javax.swing.JLabel lblInventoryIncome;
    private javax.swing.JLabel lblTransferPaid;
    private com.toedter.calendar.JDateChooser startDate;
    // End of variables declaration//GEN-END:variables
}
