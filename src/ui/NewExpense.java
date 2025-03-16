package ui;

import dao.ConnectionProvider;
import javax.swing.JOptionPane;
import java.sql.*;
import common.Validations;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import ui.help.NewExpenseHelp;

/**
 *
 * @author HOME
 */
public class NewExpense extends javax.swing.JFrame {

    /**
     * Creates new form NewExpense
     */
    public NewExpense() {
        initComponents();
        setSize(850,500);
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
        txtExpenseValue = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtExpenseMotive = new javax.swing.JTextArea();
        btnSave = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtExpenseResponsible = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Registrar Nuevo Gasto");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(212, 6, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Valor total del gasto *");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 181, -1, -1));

        txtExpenseValue.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtExpenseValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 201, 280, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Detalle o motivo del gasto *");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(456, 181, -1, -1));

        txtExpenseMotive.setColumns(20);
        txtExpenseMotive.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtExpenseMotive.setRows(5);
        jScrollPane1.setViewportView(txtExpenseMotive);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(456, 202, 300, -1));

        btnSave.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSave.setForeground(new java.awt.Color(0, 0, 0));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        btnSave.setText("Guardar");
        btnSave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSave.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSave.setIconTextGap(10);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        getContentPane().add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 300, 110, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("(*) Indica campo obligatorio");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 431, -1, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Responsable del gasto *");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 275, -1, -1));

        txtExpenseResponsible.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtExpenseResponsible, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 295, 280, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jLabel7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(804, 6, -1, -1));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/help.png"))); // NOI18N
        jLabel8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(768, 14, -1, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 56, 850, 10));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/whiteSmoke.jpg"))); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // botón para guardar
        
        String description = txtExpenseMotive.getText().trim();
        String valueText = txtExpenseValue.getText().trim();
        String expenseResponsible = txtExpenseResponsible.getText().trim();

        // Validar que los campos no estén vacíos
        if (Validations.isNullOrBlank(description) || Validations.isNullOrBlank(valueText) || Validations.isNullOrBlank(expenseResponsible)) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios.", "Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            long expenseValue = Long.parseLong(valueText); // Convertir valor a número


            String sql = "INSERT INTO expenses (description, expenseValue, expenseResponsible) VALUES (?, ?, ?)";
            try (Connection conn = ConnectionProvider.getCon(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                // Configurar parámetros
                stmt.setString(1, description);
                stmt.setLong(2, expenseValue);
                stmt.setString(3, expenseResponsible);

                // Ejecutar la inserción
                int rowsInserted = stmt.executeUpdate();

                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Gasto registrado con éxito.", "Éxito", 
                            JOptionPane.INFORMATION_MESSAGE);
                    txtExpenseMotive.setText(""); // Limpiar campos
                    txtExpenseValue.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo registrar el gasto. Intente nuevamente.", "Error", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El valor del gasto debe ser un número válido.", "Error", 
                    JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos: " + ex.getMessage(), "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        dispose();
    }//GEN-LAST:event_jLabel7MouseClicked

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        new NewExpenseHelp().setVisible(true);
    }//GEN-LAST:event_jLabel8MouseClicked

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
            java.util.logging.Logger.getLogger(NewExpense.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewExpense.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewExpense.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewExpense.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewExpense().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea txtExpenseMotive;
    private javax.swing.JTextField txtExpenseResponsible;
    private javax.swing.JTextField txtExpenseValue;
    // End of variables declaration//GEN-END:variables
}
