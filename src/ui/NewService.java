package ui;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import common.OpenPdf;
import dao.ConnectionProvider;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import javax.swing.table.TableModel;
import dao.ProductsUtils;
import java.io.FileOutputStream;
import common.Validations;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import ui.help.NewServiceHelp;

public class NewService extends javax.swing.JFrame {

    private String billId = "";
    private String username = "";
    private String serviceState = "";
    private long finalTotalPrice = 0;
    private long cashPaidInt = 0;
    private long transferPaidInt = 0;
    private long totalPriceLong = 0;

    /**
     * Creates new form SellProduct
     */
    public NewService() {
        initComponents();
    }

    public NewService(String tempUsername) {
        initComponents();
        username = tempUsername;
        setLocationRelativeTo(null);
        setSize(1366, 768);
    }

    private void productName(String nameOrUniqueId) {
        //mostrar código y descripción de cada producto en la tabla
        DefaultTableModel model = (DefaultTableModel) productsTable.getModel();
        model.setRowCount(0);

        String query = "SELECT * FROM products WHERE description LIKE ? OR product_pk LIKE ?";

        try (Connection con = ConnectionProvider.getCon(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, nameOrUniqueId + "%");
            pst.setString(2, nameOrUniqueId + "%");

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{rs.getString("product_pk") + "   -   " + rs.getString("description")});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar productos: " + e.getMessage());
        }
    }

    // Verifica si hay productos en el carrito
    private boolean isCartEmpty() {
        DefaultTableModel dtm = (DefaultTableModel) cartTable.getModel();
        return dtm.getRowCount() == 0; // Devuelve true si está vacío, false si contiene filas
    }

    private void clearProductFields() {
        txtUniqueId.setText("");
        txtDescription.setText("");
        txtProductBrand.setText("");
        txtPricePerUnit.setText("");
        txtNoOfUnits.setText("");
        txtTotalPrice.setText("");
    }

    private void updateProductQuantity() {
        // método para actualizar la cantidad del producto en inventario
        DefaultTableModel dtm = (DefaultTableModel) cartTable.getModel();
        if (cartTable.getRowCount() != 0) {
            try (Connection con = ConnectionProvider.getCon()) {
                for (int i = 0; i < cartTable.getRowCount(); i++) {
                    String productIdString = dtm.getValueAt(i, 0).toString().trim();
                    int quantityToReduce = Integer.parseInt(dtm.getValueAt(i, 4).toString());

                    String updateQuery = "UPDATE products SET quantity = quantity - ? WHERE product_pk = ?";
                    try (PreparedStatement ps = con.prepareStatement(updateQuery)) {
                        ps.setInt(1, quantityToReduce);
                        ps.setString(2, productIdString);
                        ps.executeUpdate();
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    //POR ACTUALIZAR
    private String getClientIdCard(String selectedClient) {
        String idCard = null; // Para almacenar el idCard consultado
        String query = "SELECT client_pk FROM clients WHERE name = ?";

        try (Connection con = ConnectionProvider.getCon(); PreparedStatement pst = con.prepareStatement(query)) {
            // Configura el parámetro de la consulta
            pst.setString(1, selectedClient);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    idCard = rs.getString("client_pk");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al consultar el ID del cliente: " + e.getMessage());
        }
        return idCard;
    }

    private void generatePDF(String clientID) {
        // Crear factura
        com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
        String client_pk = clientID;
        String clientName = null;
        String paymentTerm = comboPayment.getSelectedItem().toString();
        final String NIT = "119887284-4";
        final String ADDRESS = "Cra 18 # 11 - 62 Centro - Cumaral";
        String vendorName = Validations.getNameByUsername(username);

        try (Connection con = ConnectionProvider.getCon()) {
            // Consulta para obtener el nombre del cliente
            String query = "SELECT name FROM clients WHERE client_pk = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, client_pk);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        clientName = rs.getString("name");
                    } else {
                        throw new Exception("No se encontró un cliente con el ID proporcionado.");
                    }
                }
            }

            // Obtener la fecha actual
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String dateString = now.format(formatter);

            // Crear el PDF
            PdfWriter.getInstance(doc, new FileOutputStream(ProductsUtils.billPath + "" + billId + ".pdf"));
            doc.open();

            // Título
            Paragraph title = new Paragraph("Factura de venta\nMOTOREPUESTOS GOOFY\nNIT: " + NIT + "\nDirección: " + ADDRESS
                    + "\nNo Responsable De IVA");
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);

            // Detalles del cliente
            Paragraph details = new Paragraph("\nID de factura: " + billId
                    + "\nFecha y hora de expedición: " + dateString
                    + "\nVendedor: " + vendorName
                    + "\nCliente: " + (clientName != null ? clientName : "Desconocido")
                    + "\nNIT/CC: " + client_pk);
            doc.add(details);

            // Línea divisoria
            Paragraph hyphenLine = new Paragraph("-".repeat(130) + "\n\n");
            hyphenLine.setAlignment(Element.ALIGN_CENTER);
            doc.add(hyphenLine);

            // Tabla de productos
            PdfPTable tbl = new PdfPTable(6);
            tbl.addCell("ID del producto");
            tbl.addCell("Descripción");
            tbl.addCell("Marca");
            tbl.addCell("Precio por unidad");
            tbl.addCell("Cantidad");
            tbl.addCell("Sub Total");

            for (int i = 0; i < cartTable.getRowCount(); i++) {
                tbl.addCell(cartTable.getValueAt(i, 0).toString());
                tbl.addCell(cartTable.getValueAt(i, 1).toString());
                tbl.addCell(cartTable.getValueAt(i, 2).toString());
                tbl.addCell(cartTable.getValueAt(i, 3).toString());
                tbl.addCell(cartTable.getValueAt(i, 4).toString());
                tbl.addCell(cartTable.getValueAt(i, 5).toString());
            }
            doc.add(tbl);

            // Detalle de pago
            Paragraph paymentTitle = new Paragraph("-".repeat(40) + "Detalle De Pago" + "-".repeat(40) + "\n");
            paymentTitle.setAlignment(Element.ALIGN_CENTER);
            doc.add(paymentTitle);

            Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Paragraph total = new Paragraph("\nPrecio Total Pagado: " + finalTotalPrice, boldFont);
            doc.add(total);

            Paragraph paymentDetails = new Paragraph("Forma de pago: " + paymentTerm
                    + "\nEfectivo: " + cashPaidInt
                    + "\nTransferencia: " + transferPaidInt);
            doc.add(paymentDetails);

            doc.add(hyphenLine);

            // Mensaje de agradecimiento
            Paragraph thanksMsg = new Paragraph("¡Gracias por tu compra. Te esperamos de nuevo!");
            doc.add(thanksMsg);

            // Abrir PDF
            OpenPdf.openById(String.valueOf(billId));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            doc.close();
        }
    }

    public String getUniqueId(String prefix) {
        return prefix + System.nanoTime();
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
        txtSearch = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        productsTable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtProductBrand = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPricePerUnit = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtNoOfUnits = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtTotalPrice = new javax.swing.JTextField();
        btnAddToCart = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        cartTable = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        lblFinalTotalPrice = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        comboRelateVehicle = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        txtFilterPlate = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtUniqueId = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        comboPayment = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtCashPaid = new javax.swing.JTextField();
        txtTransferPaid = new javax.swing.JTextField();
        txtDescription = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Nuevo Servicio");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(576, 6, -1, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 59, 1366, 10));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Buscar producto por ID o descripción");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(81, 113, -1, -1));

        txtSearch.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSearchMouseClicked(evt);
            }
        });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });
        getContentPane().add(txtSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 137, 370, -1));

        productsTable.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        productsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Productos Registrados"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        productsTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        productsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                productsTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(productsTable);
        if (productsTable.getColumnModel().getColumnCount() > 0) {
            productsTable.getColumnModel().getColumn(0).setResizable(false);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 178, 370, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("ID del Producto");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 113, -1, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Descripción");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 172, -1, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Marca");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 231, -1, -1));

        txtProductBrand.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtProductBrand, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 252, 300, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Precio por Unidad");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(994, 113, -1, -1));

        txtPricePerUnit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtPricePerUnit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPricePerUnitKeyReleased(evt);
            }
        });
        getContentPane().add(txtPricePerUnit, new org.netbeans.lib.awtextra.AbsoluteConstraints(994, 134, 300, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Número de Unidades *");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(994, 175, -1, -1));

        txtNoOfUnits.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtNoOfUnits.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNoOfUnitsKeyReleased(evt);
            }
        });
        getContentPane().add(txtNoOfUnits, new org.netbeans.lib.awtextra.AbsoluteConstraints(994, 196, 300, -1));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Precio Total");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(994, 231, -1, -1));

        txtTotalPrice.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtTotalPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(994, 252, 300, -1));

        btnAddToCart.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAddToCart.setForeground(new java.awt.Color(0, 0, 0));
        btnAddToCart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/addToCart.png"))); // NOI18N
        btnAddToCart.setText("Añadir al carrito");
        btnAddToCart.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddToCart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddToCartActionPerformed(evt);
            }
        });
        getContentPane().add(btnAddToCart, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 446, -1, -1));

        cartTable.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cartTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID del Producto", "Descripción", "Marca", "Precio Por Unidad", "N° de Unidades", "Precio Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        cartTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cartTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(cartTable);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 493, 802, 233));

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Precio total:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 447, -1, -1));

        lblFinalTotalPrice.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblFinalTotalPrice.setForeground(new java.awt.Color(255, 255, 255));
        lblFinalTotalPrice.setText("---");
        getContentPane().add(lblFinalTotalPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(652, 447, -1, -1));

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 0, 0));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print.png"))); // NOI18N
        jButton3.setText("Finalizar servicio e Imprimir");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(137, 675, -1, -1));

        comboRelateVehicle.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        comboRelateVehicle.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione un vehículo" }));
        getContentPane().add(comboRelateVehicle, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 313, 300, -1));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Filtrar por placa");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 349, -1, -1));

        txtFilterPlate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtFilterPlate, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 373, 200, -1));

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 0, 0));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/filter.png"))); // NOI18N
        jButton2.setText("Filtrar");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(698, 370, 100, -1));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Relacionar vehículo *");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 293, -1, -1));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Seleccione en la tabla el producto a eliminar ");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(788, 732, -1, -1));

        txtUniqueId.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtUniqueId, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 134, 300, -1));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/addIcon.png"))); // NOI18N
        jButton1.setText("Añadir vehículo");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 404, -1, -1));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Forma de pago *");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(994, 293, -1, -1));

        comboPayment.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        comboPayment.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Contado", "Crédito" }));
        getContentPane().add(comboPayment, new org.netbeans.lib.awtextra.AbsoluteConstraints(994, 313, 300, -1));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Medio de pago *");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(1094, 349, -1, -1));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Efectivo ($)");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(994, 387, -1, -1));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Transferencia ($)");
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(994, 417, -1, -1));

        txtCashPaid.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtCashPaid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCashPaidKeyReleased(evt);
            }
        });
        getContentPane().add(txtCashPaid, new org.netbeans.lib.awtextra.AbsoluteConstraints(1094, 382, 200, -1));

        txtTransferPaid.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtTransferPaid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTransferPaidKeyReleased(evt);
            }
        });
        getContentPane().add(txtTransferPaid, new org.netbeans.lib.awtextra.AbsoluteConstraints(1104, 412, 190, -1));

        txtDescription.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 196, 300, -1));

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(0, 0, 0));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/saveProgress.png"))); // NOI18N
        jButton4.setText("Guardar proceso");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(162, 611, -1, -1));

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jLabel18.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel18MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(1315, 6, -1, -1));

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/help.png"))); // NOI18N
        jLabel19.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel19MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(1279, 17, -1, -1));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/adminDashboardBackground.png"))); // NOI18N
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        productName("");
        loadVehicles();
        txtUniqueId.setEditable(false);
        txtDescription.setEditable(false);
        txtProductBrand.setEditable(false);
        txtPricePerUnit.setEditable(false);
        txtTotalPrice.setEditable(false);
    }//GEN-LAST:event_formComponentShown

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        String search = txtSearch.getText().trim();
        productName(search);
    }//GEN-LAST:event_txtSearchKeyReleased

    private void txtNoOfUnitsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoOfUnitsKeyReleased
        //calcular el precio total de acuerdo a la cantidad de unidades a vender
        String noOfUnits = txtNoOfUnits.getText().trim();

        if (!Validations.isNullOrBlank(noOfUnits)) {
            String price = txtPricePerUnit.getText().trim();

            if (!noOfUnits.matches(Validations.NUMBER_PATTERN)) {
                JOptionPane.showMessageDialog(null, "Debes ingresar la cantidad de unidades a vender en números.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                txtNoOfUnits.setText("");
                return;
            }

            int totalPrice = Integer.parseInt(noOfUnits) * Integer.parseInt(price);
            txtTotalPrice.setText(String.valueOf(totalPrice));
        } else {
            txtTotalPrice.setText("");
        }

    }//GEN-LAST:event_txtNoOfUnitsKeyReleased

    private void txtSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSearchMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchMouseClicked

    private void productsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productsTableMouseClicked
        // mostrar los datos del producto al seleccionarlo en la tabla
        txtPricePerUnit.setEditable(true);
        int index = productsTable.getSelectedRow();
        TableModel model = productsTable.getModel();
        String nameOrUniqueId = model.getValueAt(index, 0).toString();

        String uniqueId[] = nameOrUniqueId.split("-", 0);

        String query = "SELECT * FROM products WHERE product_pk = ?";
        try (Connection con = ConnectionProvider.getCon(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, uniqueId[0]);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    txtUniqueId.setText(uniqueId[0]);
                    txtDescription.setText(rs.getString("description"));
                    txtProductBrand.setText(rs.getString("productBrand"));
                    txtPricePerUnit.setText(rs.getString("sellingPrice"));
                    txtNoOfUnits.setText("");
                    txtTotalPrice.setText("");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_productsTableMouseClicked

    private void btnAddToCartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddToCartActionPerformed
        // Añadir al carrito
        txtPricePerUnit.setEditable(false);
        DefaultTableModel dtm = (DefaultTableModel) cartTable.getModel();

        String noOfUnits = txtNoOfUnits.getText().trim();
        String uniqueId = txtUniqueId.getText().trim();
        String pricePerUnit = txtPricePerUnit.getText().trim();

        if (!Validations.isNullOrBlank(noOfUnits) && !Validations.isNullOrBlank(uniqueId) && !Validations.isNullOrBlank(pricePerUnit)) {

            if (!pricePerUnit.matches(Validations.NUMBER_PATTERN) || !noOfUnits.matches(Validations.NUMBER_PATTERN)) {
                JOptionPane.showMessageDialog(null, "El número de unidades y precio del producto deben ser ingresados en números", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int nUnits = Integer.parseInt(noOfUnits);
            if (nUnits <= 0) {
                JOptionPane.showMessageDialog(null, "El número de unidades a vender debe ser mayor a cero", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar si el producto ya está en el carrito
            int rowIndex = -1;

            for (int i = 0; i < dtm.getRowCount(); i++) {
                if (dtm.getValueAt(i, 0).toString().equals(uniqueId)) {
                    rowIndex = i;
                    break;
                }
            }

            String query = "SELECT * FROM products WHERE product_pk = ?";
            try (
                    Connection con = ConnectionProvider.getCon(); PreparedStatement pst = con.prepareStatement(query)) {
                pst.setString(1, uniqueId); // Sustituir el parámetro único ID
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        int availableQuantity = rs.getInt("quantity");

                        if (availableQuantity >= nUnits) {
                            String description = txtDescription.getText();
                            String productBrand = txtProductBrand.getText();
                            int unitPrice = Integer.parseInt(pricePerUnit);

                            if (rowIndex == -1) {
                                // Si el producto no está en el carrito, añadirlo como nuevo
                                int totalPrice = unitPrice * nUnits;
                                dtm.addRow(new Object[]{uniqueId, description, productBrand, pricePerUnit, noOfUnits, totalPrice});
                            } else {
                                // Si el producto ya está en el carrito, actualizar su cantidad y total
                                int currentUnits = Integer.parseInt(dtm.getValueAt(rowIndex, 4).toString());
                                int newUnits = currentUnits + nUnits;

                                // Actualizar cantidad y precio total en el carrito
                                dtm.setValueAt(newUnits, rowIndex, 4);
                                int newTotalPrice = unitPrice * newUnits;
                                dtm.setValueAt(newTotalPrice, rowIndex, 5);
                            }

                            // Actualizar inventario en la base de datos
                            int updatedQuantity = availableQuantity - nUnits;
                            try (PreparedStatement updatePst = con.prepareStatement(
                                    "UPDATE products SET quantity = ? WHERE product_pk = ?"
                            )) {
                                updatePst.setInt(1, updatedQuantity);
                                updatePst.setString(2, uniqueId);
                                updatePst.executeUpdate();
                            }

                            // Actualizar precio final total
                            finalTotalPrice += unitPrice * nUnits;
                            lblFinalTotalPrice.setText(String.valueOf(finalTotalPrice));

                            JOptionPane.showMessageDialog(null, "¡Producto añadido exitosamente!");
                            clearProductFields();
                        } else {
                            JOptionPane.showMessageDialog(null, "No hay suficiente stock para este producto. Solo quedan "
                                    + availableQuantity + " unidades.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Producto no encontrado en la base de datos.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al añadir el producto: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el ID del producto y el número de unidades!",
                    "No hay productos seleccionados", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAddToCartActionPerformed

    private void cartTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cartTableMouseClicked
        // eliminar producto del carrito
        int index = cartTable.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(null, "No se ha seleccionado ningún producto para eliminar.", "Error", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int a = JOptionPane.showOptionDialog(null, "¿Quieres eliminar este producto?", "Selecciona una opción",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Sí", "No"}, "Sí");
        if (a == 0) {
            DefaultTableModel model = (DefaultTableModel) cartTable.getModel();
            String uniqueId = model.getValueAt(index, 0).toString();
            int quantity = Integer.parseInt(model.getValueAt(index, 4).toString());
            int totalPrice = Integer.parseInt(model.getValueAt(index, 5).toString());

            // Restar el precio del producto eliminado al precio final total
            finalTotalPrice -= totalPrice;
            lblFinalTotalPrice.setText(String.valueOf(finalTotalPrice));

            // Actualizar el stock en la base de datos
            String updateStockQuery = "UPDATE products SET quantity = quantity + ? WHERE product_pk = ?";
            try (Connection con = ConnectionProvider.getCon(); PreparedStatement pst = con.prepareStatement(updateStockQuery)) {
                pst.setInt(1, quantity);
                pst.setString(2, uniqueId);
                pst.executeUpdate();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al actualizar el stock: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Eliminar la fila seleccionada del carrito
            model.removeRow(index);
            JOptionPane.showMessageDialog(null, "Producto eliminado del carrito y stock actualizado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_cartTableMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // BOTÓN GENERAR VENTA E IMPRIMIR

        String selectedVehicle = comboRelateVehicle.getSelectedItem().toString();
        String paymentTerm = comboPayment.getSelectedItem().toString();
        String cashPaid = txtCashPaid.getText();
        String transferPaid = txtTransferPaid.getText();
        serviceState = "Terminado";

        if (cartTable.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "El carrito está vacío. Agrega productos antes de continuar.",
                    "Carrito vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (Validations.isNullOrBlank(cashPaid) && Validations.isNullOrBlank(transferPaid)) {
            JOptionPane.showMessageDialog(null, "Debes completar el medio de pago",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if ("Seleccione un vehículo".equals(selectedVehicle)) {
            JOptionPane.showMessageDialog(null, "¡Debes relacionar un vehículo al servicio!.",
                    "No hay vehículo relacionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            cashPaidInt = Integer.parseInt(cashPaid);
            transferPaidInt = Integer.parseInt(transferPaid);

            if (cashPaidInt + transferPaidInt != finalTotalPrice) {
                JOptionPane.showMessageDialog(null, "La suma de efectivo y transferencia debe ser igual al total pagado.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese un número válido.");
            return;
        }

        if (finalTotalPrice != 0) {
            billId = getUniqueId("FACT-"); // Genera el código de la factura            

            try (Connection con = ConnectionProvider.getCon()) {
                // Obtener appuser_pk
                String userQuery = "SELECT appuser_pk FROM appusers WHERE username = ?";
                int appuserPk = -1;
                try (PreparedStatement psUser = con.prepareStatement(userQuery)) {
                    psUser.setString(1, username);
                    try (ResultSet rs = psUser.executeQuery()) {
                        if (rs.next()) {
                            appuserPk = rs.getInt("appuser_pk");
                        } else {
                            throw new Exception("No se encontró el usuario: " + username);
                        }
                    }
                }

                // Obtener client_pk relacionado con motorbike_pk
                String clientQuery = "SELECT client_pk FROM motorbikes WHERE motorbike_pk = ?";
                String clientID = null;
                try (PreparedStatement psClient = con.prepareStatement(clientQuery)) {
                    psClient.setString(1, selectedVehicle);
                    try (ResultSet rs = psClient.executeQuery()) {
                        if (rs.next()) {
                            clientID = rs.getString("client_pk");
                        } else {
                            throw new Exception("No se encontró un cliente asociado al vehículo seleccionado.");
                        }
                    }
                }

                // Insertar en la tabla bills
                String insertBillQuery = "INSERT INTO bills (billId, billDate, totalPaid, paymentTerm, cashPaid, transferPaid, client_pk, appuser_pk) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                long billPk;
                try (PreparedStatement psBill = con.prepareStatement(insertBillQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    psBill.setString(1, billId);
                    psBill.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                    psBill.setLong(3, finalTotalPrice);
                    psBill.setString(4, paymentTerm);
                    psBill.setLong(5, cashPaidInt);
                    psBill.setLong(6, transferPaidInt);
                    psBill.setString(7, clientID);
                    psBill.setInt(8, appuserPk);
                    psBill.executeUpdate();

                    try (ResultSet generatedKeys = psBill.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            billPk = generatedKeys.getLong(1);
                        } else {
                            throw new Exception("Error al obtener el ID de la factura.");
                        }
                    }
                }

                // Insertar en la tabla services
                String insertServiceQuery = "INSERT INTO services (motorbike_pk, state, totalPrice) VALUES (?, ?, ?)";
                try (PreparedStatement psService = con.prepareStatement(insertServiceQuery)) {
                    psService.setString(1, selectedVehicle);
                    psService.setString(2, serviceState);
                    psService.setLong(3, finalTotalPrice);
                    psService.executeUpdate();
                }

                // Insertar productos en SoldProducts y asociarlos con la factura
                String insertSoldProductQuery = "INSERT INTO SoldProducts (quantity, salePrice, saleDate, product_pk) VALUES (?, ?, ?, ?)";
                String insertProductsBillsQuery = "INSERT INTO products_bills (bill_pk, soldProduct_pk) VALUES (?, ?)";

                DefaultTableModel dtm = (DefaultTableModel) cartTable.getModel();
                for (int i = 0; i < dtm.getRowCount(); i++) {
                    String uniqueId = dtm.getValueAt(i, 0).toString(); // uniqueId del producto
                    int quantity = Integer.parseInt(dtm.getValueAt(i, 4).toString());
                    long salePrice = Long.parseLong(dtm.getValueAt(i, 3).toString());

                    // Obtener product_pk a partir del uniqueId
                    String productQuery = "SELECT product_pk FROM products WHERE product_pk = ?";
                    long productPk = -1;
                    try (PreparedStatement psProduct = con.prepareStatement(productQuery)) {
                        psProduct.setString(1, uniqueId);
                        try (ResultSet rs = psProduct.executeQuery()) {
                            if (rs.next()) {
                                productPk = rs.getLong("product_pk");
                            } else {
                                throw new Exception("No se encontró el producto con ID único: " + uniqueId);
                            }
                        }
                    }

                    // Insertar en SoldProducts
                    long soldProductPk;
                    try (PreparedStatement psSoldProduct = con.prepareStatement(insertSoldProductQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        psSoldProduct.setInt(1, quantity);
                        psSoldProduct.setLong(2, salePrice);
                        psSoldProduct.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                        psSoldProduct.setLong(4, productPk);
                        psSoldProduct.executeUpdate();

                        try (ResultSet generatedKeys = psSoldProduct.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                soldProductPk = generatedKeys.getLong(1);
                            } else {
                                throw new Exception("Error al obtener el ID del producto vendido.");
                            }
                        }
                    }

                    // Asociar el producto vendido con la factura en products_bills
                    try (PreparedStatement psProductsBills = con.prepareStatement(insertProductsBillsQuery)) {
                        psProductsBills.setLong(1, billPk);
                        psProductsBills.setLong(2, soldProductPk);
                        psProductsBills.executeUpdate();
                    }
                }
                generatePDF(clientID); // Crear PDF de la venta
                JOptionPane.showMessageDialog(null, "Factura generada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            dispose();
            new NewService(username).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Por favor agrega algún producto al carrito.", "No hay productos seleccionados",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // filtrar clientes por número de cédula
        boolean checkPlateExist = false;
        String filterPlate = txtFilterPlate.getText().trim();
        if (Validations.isNullOrBlank(filterPlate)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar la placa del vehículo!", "No hay vehículos seleccionados",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            String query = "SELECT motorbike_pk FROM motorbikes WHERE motorbike_pk = ?";
            try (Connection con = ConnectionProvider.getCon(); PreparedStatement pst = con.prepareStatement(query)) {

                pst.setString(1, filterPlate);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        checkPlateExist = true;
                        comboRelateVehicle.setSelectedItem(rs.getString("motorbike_pk"));
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            if (!checkPlateExist) {
                JOptionPane.showMessageDialog(null, "¡Este vehículo no está registrado!", "Error", JOptionPane.ERROR_MESSAGE);
                comboRelateVehicle.setSelectedItem("Seleccione un vehículo");
            }
        }
        txtFilterPlate.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtPricePerUnitKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPricePerUnitKeyReleased
        String pricePerUnit = txtPricePerUnit.getText();

        if (!pricePerUnit.matches(Validations.NUMBER_PATTERN)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el precio del producto en números!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtPricePerUnit.setText("");
            return;
        }
    }//GEN-LAST:event_txtPricePerUnitKeyReleased

    private void txtCashPaidKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCashPaidKeyReleased
        String cashPaid = txtCashPaid.getText();

        if (!cashPaid.matches(Validations.NUMBER_PATTERN)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el valor en números!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtCashPaid.setText("");
            return;
        }
    }//GEN-LAST:event_txtCashPaidKeyReleased

    private void txtTransferPaidKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTransferPaidKeyReleased
        String transferPaid = txtTransferPaid.getText();

        if (!transferPaid.matches(Validations.NUMBER_PATTERN)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el valor en números!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtTransferPaid.setText("");
            return;
        }
    }//GEN-LAST:event_txtTransferPaidKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // botón para agregar vehículo
        ManageClients addVehicleWindow = new ManageClients();
        addVehicleWindow.setVisible(true);

        // Agregar un WindowListener para detectar cuándo se cierra la ventana
        addVehicleWindow.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                loadVehicles(); // Recargar los vehículos al cerrar la ventana AddVehicle
            }
        });
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // BOTÓN DE GUARDAR PROCESO

        String selectedVehicle = comboRelateVehicle.getSelectedItem().toString().trim();
        serviceState = "En proceso";

        if ("Seleccione un vehículo".equals(selectedVehicle)) {
            JOptionPane.showMessageDialog(null, "¡Debes relacionar un vehículo al servicio!.",
                    "No hay vehículo relacionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (cartTable.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "El carrito está vacío. Agrega productos antes de continuar.",
                    "Carrito vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String totalPrice = lblFinalTotalPrice.getText();
            totalPriceLong = Long.parseLong(totalPrice);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El precio total debe ser un número válido.", "Error de formato",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (finalTotalPrice != 0) {
            try (Connection con = ConnectionProvider.getCon()) {
                // Verificar si existe un servicio con el mismo vehículo y estado "En proceso"
                String checkQuery = "SELECT COUNT(*) AS count FROM services WHERE motorbike_pk = ? AND state = ?";
                try (PreparedStatement psCheck = con.prepareStatement(checkQuery)) {
                    psCheck.setString(1, selectedVehicle); // motorbike_pk
                    psCheck.setString(2, serviceState); // Estado "En proceso"
                    try (ResultSet rs = psCheck.executeQuery()) {
                        if (rs.next() && rs.getInt("count") > 0) {
                            JOptionPane.showMessageDialog(null,
                                    "Ya existe un servicio en proceso para este vehículo.",
                                    "Servicio duplicado",
                                    JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }
                }

                // Insertar en services
                String insertServiceQuery = "INSERT INTO services (motorbike_pk, state, totalPrice) VALUES (?, ?, ?)";
                long servicePk;
                try (PreparedStatement psService = con.prepareStatement(insertServiceQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    psService.setString(1, selectedVehicle); // motorbike_pk
                    psService.setString(2, serviceState); // Estado del servicio
                    psService.setLong(3, totalPriceLong); // Precio actual del servicio
                    psService.executeUpdate();

                    try (ResultSet generatedKeys = psService.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            servicePk = generatedKeys.getLong(1);
                        } else {
                            throw new Exception("Error al obtener el ID del servicio.");
                        }
                    }
                }

                // Insertar productos en SoldProducts y asociarlos con el servicio en soldProducts_services
                String insertSoldProductQuery = "INSERT INTO SoldProducts (quantity, salePrice, saleDate, product_pk) VALUES (?, ?, ?, ?)";
                String insertSoldProductsServicesQuery = "INSERT INTO soldProducts_services (service_pk, soldProduct_pk) VALUES (?, ?)";

                DefaultTableModel dtm = (DefaultTableModel) cartTable.getModel();
                for (int i = 0; i < dtm.getRowCount(); i++) {
                    String uniqueId = dtm.getValueAt(i, 0).toString(); // uniqueId del producto
                    int quantity = Integer.parseInt(dtm.getValueAt(i, 4).toString());
                    long salePrice = Long.parseLong(dtm.getValueAt(i, 3).toString());

                    // Insertar en SoldProducts
                    long soldProductPk;
                    try (PreparedStatement psSoldProduct = con.prepareStatement(insertSoldProductQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        psSoldProduct.setInt(1, quantity);
                        psSoldProduct.setLong(2, salePrice);
                        psSoldProduct.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                        psSoldProduct.setString(4, uniqueId);
                        psSoldProduct.executeUpdate();

                        try (ResultSet generatedKeys = psSoldProduct.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                soldProductPk = generatedKeys.getLong(1);
                            } else {
                                throw new Exception("Error al obtener el ID del producto vendido.");
                            }
                        }
                    }

                    // Asociar service_pk con soldProduct_pk en soldProducts_services
                    try (PreparedStatement psSoldProductsServices = con.prepareStatement(insertSoldProductsServicesQuery)) {
                        psSoldProductsServices.setLong(1, servicePk); // service_pk
                        psSoldProductsServices.setLong(2, soldProductPk); // soldProduct_pk
                        psSoldProductsServices.executeUpdate();
                    }
                }
                JOptionPane.showMessageDialog(null, "Servicio guardado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            dispose();
            new NewService(username).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Por favor agrega algún producto al carrito.", "No hay productos seleccionados",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jLabel18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseClicked
        //botón para cerrar ventana
        if (!isCartEmpty()) {
            JOptionPane.showMessageDialog(null, "Debes guardar el proceso del servicio antes de salir.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        } else {
            dispose();
        }
    }//GEN-LAST:event_jLabel18MouseClicked

    private void jLabel19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19MouseClicked
        new NewServiceHelp().setVisible(true);
    }//GEN-LAST:event_jLabel19MouseClicked

    /* Método para cargar las placas en el jComboBox */
    private void loadVehicles() {
        String query = "SELECT motorbike_pk FROM motorbikes";

        try (Connection con = ConnectionProvider.getCon(); PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            comboRelateVehicle.removeAllItems();
            comboRelateVehicle.addItem("Seleccione un vehículo");

            while (rs.next()) {
                String plateNumber = rs.getString("motorbike_pk");
                comboRelateVehicle.addItem(plateNumber);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar las placas de motos: " + e.getMessage());
        }
    }

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
            java.util.logging.Logger.getLogger(NewService.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewService.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewService.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewService.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewService().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddToCart;
    private javax.swing.JTable cartTable;
    private javax.swing.JComboBox<String> comboPayment;
    private javax.swing.JComboBox<String> comboRelateVehicle;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblFinalTotalPrice;
    private javax.swing.JTable productsTable;
    private javax.swing.JTextField txtCashPaid;
    private javax.swing.JTextField txtDescription;
    private javax.swing.JTextField txtFilterPlate;
    private javax.swing.JTextField txtNoOfUnits;
    private javax.swing.JTextField txtPricePerUnit;
    private javax.swing.JTextField txtProductBrand;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTotalPrice;
    private javax.swing.JTextField txtTransferPaid;
    private javax.swing.JTextField txtUniqueId;
    // End of variables declaration//GEN-END:variables
}
