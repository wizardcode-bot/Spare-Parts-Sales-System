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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UpdateService extends javax.swing.JFrame {

    private String billId = "";
    private String username = "";
    private String serviceState = "";
    private long finalTotalPrice = 0;
    private long cashPaidInt = 0;
    private long transferPaidInt = 0;
    private long totalPriceLong = 0;
    private long servicePKLong = 0;

    /**
     * Creates new form SellProduct
     */
    public UpdateService() {
        initComponents();
    }

    public UpdateService(String tempUsername) {
        initComponents();
        username = tempUsername;
        setLocationRelativeTo(null);
        setSize(1366, 778);
    }

    private long getProductPk(Connection con, String uniqueId) throws SQLException {
        //método para obtener la llave primaria de cada producto
        String query = "SELECT product_pk FROM products WHERE uniqueId = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, uniqueId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("product_pk");
                } else {
                    throw new SQLException("No se encontró el producto con unique_id: " + uniqueId);
                }
            }
        }
    }

    private boolean isProductAlreadyInService(Connection con, long servicePk, long productPk) throws SQLException {
        //método para consultar si han sido agregados nuevos productos al carrito
        String query = "SELECT 1 FROM soldProducts_services sps "
                + "INNER JOIN soldProducts sp ON sps.soldProduct_pk = sp.soldProduct_pk "
                + "WHERE sps.service_pk = ? AND sp.product_pk = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setLong(1, servicePk);
            ps.setLong(2, productPk);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Retorna true si el producto ya está asociado
            }
        }
    }

    private long insertSoldProduct(Connection con, int quantity, long salePrice, long productPk) throws SQLException {
        //método para insertar productos en la tabla soldProducts
        String query = "INSERT INTO soldProducts (quantity, salePrice, product_pk) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, quantity);
            ps.setLong(2, salePrice);
            ps.setLong(3, productPk);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1); // Devuelve la clave primaria generada.
                } else {
                    throw new SQLException("No se pudo obtener la clave primaria del producto vendido.");
                }
            }
        }
    }

    private void associateProductWithBill(Connection con, long billPk, long soldProductPk) throws SQLException {
        //asocia productos vendidos en cada factura
        String query = "INSERT INTO products_bills (bill_pk, soldProduct_pk) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setLong(1, billPk);
            ps.setLong(2, soldProductPk);
            ps.executeUpdate();
        }
    }

    private void updateProductQuantity(Map<Long, Integer> productsToUpdateQuantity) throws SQLException {
        //modifica el inventario según lo ingresado o descontado del carrito
        String query = "UPDATE products SET quantity = quantity - ? WHERE product_pk = ?";
        try (Connection con = ConnectionProvider.getCon(); PreparedStatement ps = con.prepareStatement(query)) {
            for (Map.Entry<Long, Integer> entry : productsToUpdateQuantity.entrySet()) {
                ps.setInt(1, entry.getValue()); // Cantidad a descontar
                ps.setLong(2, entry.getKey());  // product_pk
                ps.addBatch();
            }
            ps.executeBatch(); // Ejecuta todas las actualizaciones en una sola operación
        }
    }

    private long insertBill(Connection con, String billId, long totalPrice, String paymentTerm,
            long cashPaid, long transferPaid, String clientPk, long appuserPk) throws SQLException {
        //insertar en tabla bills
        String query = "INSERT INTO bills (billId, totalPaid, paymentTerm, cashPaid, transferPaid, client_pk, appuser_pk) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, billId);
            ps.setLong(2, totalPrice);
            ps.setString(3, paymentTerm);
            ps.setLong(4, cashPaid);
            ps.setLong(5, transferPaid);
            ps.setString(6, clientPk);
            ps.setLong(7, appuserPk);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1); // Devuelve la clave primaria generada.
                } else {
                    throw new SQLException("No se pudo obtener la clave primaria de la factura.");
                }
            }
        }
    }

    private void updateService(Connection con, String vehiclePlate, String serviceState, long totalPrice) throws SQLException {
        //actualizar tabla services

        String query = "UPDATE services SET motorbike_pk = ?, state = ?, totalPrice = ? WHERE service_pk = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, vehiclePlate);
            ps.setString(2, serviceState);
            ps.setLong(3, totalPrice);
            ps.setLong(4, servicePKLong);
            ps.executeUpdate();
        }

    }

    private void productName(String nameOrUniqueId) {
        //mostrar código y descripción de cada producto en la tabla
        DefaultTableModel model = (DefaultTableModel) productsTable.getModel();
        model.setRowCount(0);

        String query = "SELECT * FROM products WHERE description LIKE ? OR uniqueId LIKE ?";

        try (Connection con = ConnectionProvider.getCon(); PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, nameOrUniqueId + "%");
            pst.setString(2, nameOrUniqueId + "%");

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{rs.getString("uniqueId") + "   -   " + rs.getString("description")});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar productos: " + e.getMessage());
        }
    }

    private void clearProductFields() {
        txtUniqueId.setText("");
        txtDescription.setText("");
        txtProductBrand.setText("");
        txtPricePerUnit.setText("");
        txtNoOfUnits.setText("");
        txtTotalPrice.setText("");
    }

    private void generatePDF(String clientID, Long lblTotalPrice, Long cashPaidInt, Long transferPaidInt) {
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
            Paragraph total = new Paragraph("\nPrecio Total Pagado: " + lblTotalPrice, boldFont);
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
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtUniqueId = new javax.swing.JTextField();
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
        txtServiceID = new javax.swing.JTextField();
        btnSearchService = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        txtPlate = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();

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
        jLabel1.setText("Actualizar Servicio");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(526, 6, -1, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 62, 1366, 10));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Buscar producto por ID o descripción");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(81, 106, -1, -1));

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
        getContentPane().add(txtSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 134, 370, -1));

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

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 167, 370, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("ID del Producto");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 231, -1, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Descripción");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 295, -1, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Marca");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 350, -1, -1));

        txtProductBrand.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtProductBrand, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 371, 300, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Precio por Unidad");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(994, 106, -1, -1));

        txtPricePerUnit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtPricePerUnit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPricePerUnitKeyReleased(evt);
            }
        });
        getContentPane().add(txtPricePerUnit, new org.netbeans.lib.awtextra.AbsoluteConstraints(994, 127, 300, -1));

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
        getContentPane().add(btnAddToCart, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 447, -1, -1));

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
        cartTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cartTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cartTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(cartTable);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 494, 802, 233));

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Precio total:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 448, -1, -1));

        lblFinalTotalPrice.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblFinalTotalPrice.setForeground(new java.awt.Color(255, 255, 255));
        lblFinalTotalPrice.setText("---");
        getContentPane().add(lblFinalTotalPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(652, 448, -1, -1));

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
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(137, 664, -1, -1));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Seleccione en la tabla el producto a eliminar ");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(788, 733, -1, -1));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jLabel14.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel14MouseReleased(evt);
            }
        });
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(1320, 6, -1, -1));

        txtUniqueId.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtUniqueId, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 252, 300, -1));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Forma de pago *");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(994, 293, -1, -1));

        comboPayment.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        comboPayment.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Contado", "Crédito" }));
        getContentPane().add(comboPayment, new org.netbeans.lib.awtextra.AbsoluteConstraints(994, 314, 300, -1));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Medio de pago *");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(1094, 350, -1, -1));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Efectivo ($)");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(994, 388, -1, -1));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Transferencia ($)");
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(994, 418, -1, -1));

        txtCashPaid.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtCashPaid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCashPaidKeyReleased(evt);
            }
        });
        getContentPane().add(txtCashPaid, new org.netbeans.lib.awtextra.AbsoluteConstraints(1094, 383, 200, -1));

        txtTransferPaid.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtTransferPaid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTransferPaidKeyReleased(evt);
            }
        });
        getContentPane().add(txtTransferPaid, new org.netbeans.lib.awtextra.AbsoluteConstraints(1104, 413, 190, -1));

        txtDescription.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 316, 300, -1));

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
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(162, 600, -1, -1));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Ingrese el ID del servicio a actualizar");
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 106, -1, -1));

        txtServiceID.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtServiceID, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 134, 217, -1));

        btnSearchService.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSearchService.setForeground(new java.awt.Color(0, 0, 0));
        btnSearchService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search.png"))); // NOI18N
        btnSearchService.setText("Buscar");
        btnSearchService.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearchService.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSearchService.setIconTextGap(8);
        btnSearchService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchServiceActionPerformed(evt);
            }
        });
        getContentPane().add(btnSearchService, new org.netbeans.lib.awtextra.AbsoluteConstraints(715, 129, 102, -1));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Placa del vehículo");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 175, -1, -1));

        txtPlate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtPlate, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 196, 222, -1));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/adminDashboardBackground.png"))); // NOI18N
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        productName("");
        txtUniqueId.setEditable(false);
        txtDescription.setEditable(false);
        txtProductBrand.setEditable(false);
        txtPricePerUnit.setEditable(false);
        txtTotalPrice.setEditable(false);
        txtPlate.setEditable(false);
        txtPlate.setEditable(false);
        btnSearchService.setEnabled(true);
        txtServiceID.setEditable(true);
    }//GEN-LAST:event_formComponentShown

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        String search = txtSearch.getText().trim();
        productName(search);
    }//GEN-LAST:event_txtSearchKeyReleased

    private void txtNoOfUnitsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoOfUnitsKeyReleased
        //calcular el precio total de acuerdo a la cantidad de unidades a vender

        String noOfUnits = txtNoOfUnits.getText().trim();
        String price = txtPricePerUnit.getText().trim();

        try {
            if (!Validations.isNullOrBlank(noOfUnits) && !Validations.isNullOrBlank(price)) {
                if (!noOfUnits.matches(Validations.numberPattern)) {
                    JOptionPane.showMessageDialog(null, "Debes ingresar la cantidad de unidades a vender en números.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    txtNoOfUnits.setText("");
                    txtTotalPrice.setText("");
                    return;
                }

                int units = Integer.parseInt(noOfUnits);
                int unitPrice = Integer.parseInt(price);

                if (units <= 0) {
                    JOptionPane.showMessageDialog(null, "La cantidad de unidades debe ser mayor a 0.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    txtNoOfUnits.setText("");
                    txtTotalPrice.setText("");
                    return;
                }

                int totalPrice = units * unitPrice;
                txtTotalPrice.setText(String.valueOf(totalPrice));
            } else {
                txtTotalPrice.setText("");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al calcular el precio total. Verifica los valores ingresados.",
                    "Error", JOptionPane.ERROR_MESSAGE);
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

        String query = "SELECT * FROM products WHERE uniqueId = ?";
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
        // Sincronizar finalTotalPrice con lblFinalTotalPrice
        try {
            finalTotalPrice = Integer.parseInt(lblFinalTotalPrice.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al sincronizar el precio total. Verifica los valores existentes.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        txtPricePerUnit.setEditable(false);
        DefaultTableModel dtm = (DefaultTableModel) cartTable.getModel();

        String noOfUnits = txtNoOfUnits.getText().trim();
        String uniqueId = txtUniqueId.getText().trim();
        String pricePerUnit = txtPricePerUnit.getText().trim();

        if (!Validations.isNullOrBlank(noOfUnits) && !Validations.isNullOrBlank(uniqueId) && !Validations.isNullOrBlank(pricePerUnit)) {

            if (!pricePerUnit.matches(Validations.numberPattern) || !noOfUnits.matches(Validations.numberPattern)) {
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

            if (rowIndex == -1) {
                // Si el producto no está en el carrito, buscar en la base de datos
                String query = "SELECT * FROM products WHERE uniqueId = ?";
                try (
                        Connection con = ConnectionProvider.getCon(); PreparedStatement pst = con.prepareStatement(query)) {
                    pst.setString(1, uniqueId);
                    try (ResultSet rs = pst.executeQuery()) {
                        if (rs.next()) {
                            int availableQuantity = rs.getInt("quantity");

                            if (availableQuantity >= nUnits) {
                                String description = txtDescription.getText();
                                String productBrand = txtProductBrand.getText();
                                String totalPrice = txtTotalPrice.getText();

                                dtm.addRow(new Object[]{uniqueId, description, productBrand, pricePerUnit, noOfUnits, totalPrice});

                                Long totalPriceInt = Long.parseLong(totalPrice);
                                finalTotalPrice += totalPriceInt; // Sumar al total sincronizado
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
                // Si el producto ya está en el carrito, mostrar un mensaje de error
                JOptionPane.showMessageDialog(null, "El producto ya está en el carrito. No puedes modificar la cantidad.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el ID del producto y el número de unidades!",
                    "No hay productos seleccionados", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAddToCartActionPerformed

    private void cartTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cartTableMouseClicked
        // eliminar producto del carrito
        int index = cartTable.getSelectedRow();
        if (index >= 0) {
            int a = JOptionPane.showOptionDialog(null, "¿Quieres eliminar este producto?", "Selecciona una opción",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Sí", "No"}, "Sí");
            if (a == 0) {
                TableModel model = cartTable.getModel();

                // Sincronizar finalTotalPrice con lblFinalTotalPrice
                try {
                    finalTotalPrice = Integer.parseInt(lblFinalTotalPrice.getText().trim());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Error al sincronizar el precio total. Verifica los valores existentes.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try (Connection con = ConnectionProvider.getCon()) {
                    // Obtener la cantidad a devolver y el uniqueId del producto seleccionado
                    int quantityToReturn = Integer.parseInt(model.getValueAt(index, 4).toString());
                    String uniqueId = model.getValueAt(index, 0).toString();

                    // Actualizar la cantidad en inventario (tabla "products")
                    String updateQuery = "UPDATE products SET quantity = quantity + ? WHERE uniqueId = ?";
                    try (PreparedStatement ps = con.prepareStatement(updateQuery)) {
                        ps.setInt(1, quantityToReturn);
                        ps.setString(2, uniqueId);
                        ps.executeUpdate();
                    }

                    // Actualizar el precio total
                    String total = model.getValueAt(index, 5).toString();
                    int totalPriceToRemove = Integer.parseInt(total);

                    if (finalTotalPrice >= totalPriceToRemove) {
                        finalTotalPrice -= totalPriceToRemove;
                        lblFinalTotalPrice.setText(String.valueOf(finalTotalPrice));

                        // Eliminar el producto del carrito
                        ((DefaultTableModel) cartTable.getModel()).removeRow(index);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error: El precio total no puede ser negativo.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Error al procesar el precio del producto.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al actualizar la cantidad del producto en inventario: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, selecciona un producto para eliminar.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_cartTableMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // BOTÓN GENERAR VENTA E IMPRIMIR

        String selectedVehicle = txtPlate.getText().trim();
        String paymentTerm = comboPayment.getSelectedItem().toString();
        String cashPaid = txtCashPaid.getText();
        String transferPaid = txtTransferPaid.getText();
        String servicePK = txtServiceID.getText().trim();
        serviceState = "Terminado";

        if (Validations.isNullOrBlank(servicePK)) {
            JOptionPane.showMessageDialog(null, "Debes ingresar el ID del servicio que quieres actualizar",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

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
            // Obtener el total final de lblFinalTotalPrice
            long lblTotalPrice;
            try {
                lblTotalPrice = Long.parseLong(lblFinalTotalPrice.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "El precio total debe ser un número válido.",
                        "Error de formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                servicePKLong = Long.parseLong(servicePK);
            } catch (NumberFormatException e) {
                // Manejo de la excepción en caso de que el texto no sea un número válido
                JOptionPane.showMessageDialog(null, "El ID del servicio debe ser un número válido.",
                        "Error de conversión", JOptionPane.ERROR_MESSAGE);
            }

            // Validar medios de pago
            long cashPaidInt = Long.parseLong(cashPaid);
            long transferPaidInt = Long.parseLong(transferPaid);

            if (cashPaidInt + transferPaidInt != lblTotalPrice) {
                JOptionPane.showMessageDialog(null, "La suma de efectivo y transferencia debe ser igual al total pagado.");
                return;
            }

            String clientID = "";
            long appuserPk = -1;

            try (Connection con = ConnectionProvider.getCon()) {
                // Obtener client_pk basado en la placa del vehículo
                String getClientIdQuery = "SELECT client_pk FROM motorbikes WHERE motorbike_pk = ?";
                try (PreparedStatement ps = con.prepareStatement(getClientIdQuery)) {
                    ps.setString(1, selectedVehicle);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            clientID = rs.getString("client_pk");
                        } else {
                            JOptionPane.showMessageDialog(null, "No se encontró un cliente relacionado con el vehículo seleccionado.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

                // Obtener appuser_pk basado en el usuario activo
                String getAppUserIdQuery = "SELECT appuser_pk FROM appusers WHERE username = ?";
                try (PreparedStatement ps = con.prepareStatement(getAppUserIdQuery)) {
                    ps.setString(1, username);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            appuserPk = rs.getLong("appuser_pk");
                        } else {
                            JOptionPane.showMessageDialog(null, "No se encontró un usuario activo.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

                billId = getUniqueId("FACT-");

                // Insertar en la tabla bills
                long billPk = insertBill(con, billId, lblTotalPrice, paymentTerm, cashPaidInt, transferPaidInt, clientID, appuserPk);

                // Actualizar la tabla services
                updateService(con, selectedVehicle, serviceState, lblTotalPrice);

                // Procesar productos en el carrito
                Map<Long, Integer> productsToUpdateQuantity = new HashMap<>();
                DefaultTableModel dtm = (DefaultTableModel) cartTable.getModel();

                for (int i = 0; i < dtm.getRowCount(); i++) {
                    String uniqueId = dtm.getValueAt(i, 0).toString();
                    int quantity = Integer.parseInt(dtm.getValueAt(i, 4).toString());
                    long salePrice = Long.parseLong(dtm.getValueAt(i, 3).toString());

                    long productPk = getProductPk(con, uniqueId);

                    // Verificar si el producto ya está asociado al servicio
                    if (isProductAlreadyInService(con, servicePKLong, productPk)) {
                        continue; // No realizar acciones adicionales si ya está asociado
                    }

                    long soldProductPk = insertSoldProduct(con, quantity, salePrice, productPk);

                    associateProductWithBill(con, billPk, soldProductPk);

                    productsToUpdateQuantity.put(productPk, quantity);
                }

                // Actualizar inventario solo para los productos nuevos
                updateProductQuantity(productsToUpdateQuantity);

                // Generar PDF de la venta
                generatePDF(clientID, lblTotalPrice, cashPaidInt, cashPaidInt);

                JOptionPane.showMessageDialog(null, "Factura generada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            dispose();
            new UpdateService(username).setVisible(true);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese un número válido.");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jLabel14MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseReleased
        dispose();
    }//GEN-LAST:event_jLabel14MouseReleased

    private void txtPricePerUnitKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPricePerUnitKeyReleased
        String pricePerUnit = txtPricePerUnit.getText();

        if (!pricePerUnit.matches(Validations.numberPattern)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el precio del producto en números!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtPricePerUnit.setText("");
            return;
        }
    }//GEN-LAST:event_txtPricePerUnitKeyReleased

    private void txtCashPaidKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCashPaidKeyReleased
        String cashPaid = txtCashPaid.getText();

        if (!cashPaid.matches(Validations.numberPattern)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el valor en números!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtCashPaid.setText("");
            return;
        }
    }//GEN-LAST:event_txtCashPaidKeyReleased

    private void txtTransferPaidKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTransferPaidKeyReleased
        String transferPaid = txtTransferPaid.getText();

        if (!transferPaid.matches(Validations.numberPattern)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el valor en números!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtTransferPaid.setText("");
            return;
        }
    }//GEN-LAST:event_txtTransferPaidKeyReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // BOTÓN DE GUARDAR PROCESO

        String selectedVehicle = txtPlate.getText().trim();
        String serviceId = txtServiceID.getText().trim();
        serviceState = "En proceso";

        if (serviceId.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El ID del servicio no puede estar vacío.",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cartTable.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "El carrito está vacío. Agrega productos antes de continuar.",
                    "Carrito vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            totalPriceLong = Long.parseLong(lblFinalTotalPrice.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El precio total debe ser un número válido.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = ConnectionProvider.getCon()) {
            // Actualizar datos del servicio en la tabla "services"
            String updateServiceQuery = "UPDATE services SET motorbike_pk = ?, state = ?, totalPrice = ? WHERE service_pk = ?";
            try (PreparedStatement psService = con.prepareStatement(updateServiceQuery)) {
                psService.setString(1, selectedVehicle);
                psService.setString(2, serviceState);
                psService.setLong(3, totalPriceLong);
                psService.setLong(4, Long.parseLong(serviceId));
                psService.executeUpdate();
            }

            // Obtener productos actuales del servicio desde "soldProducts_services" con un JOIN a "SoldProducts"
            String getCurrentProductsQuery = """
            SELECT sps.soldProduct_pk, sp.product_pk
            FROM soldProducts_services sps
            JOIN SoldProducts sp ON sps.soldProduct_pk = sp.soldProduct_pk
            WHERE sps.service_pk = ?
        """;
            Map<Long, Long> currentProducts = new HashMap<>();
            try (PreparedStatement psGetProducts = con.prepareStatement(getCurrentProductsQuery)) {
                psGetProducts.setLong(1, Long.parseLong(serviceId));
                try (ResultSet rs = psGetProducts.executeQuery()) {
                    while (rs.next()) {
                        currentProducts.put(rs.getLong("soldProduct_pk"), rs.getLong("product_pk"));
                    }
                }
            }

            // Preparar consultas para actualizar/incluir/eliminar productos
            String insertSoldProductQuery = "INSERT INTO SoldProducts (quantity, salePrice, saleDate, product_pk) VALUES (?, ?, ?, ?)";
            String updateSoldProductQuery = "UPDATE SoldProducts SET quantity = ?, salePrice = ? WHERE soldProduct_pk = ?";
            String deleteSoldProductQuery = "DELETE FROM SoldProducts WHERE soldProduct_pk = ?";
            String insertSoldProductsServicesQuery = "INSERT INTO soldProducts_services (service_pk, soldProduct_pk) VALUES (?, ?)";
            String deleteSoldProductsServicesQuery = "DELETE FROM soldProducts_services WHERE soldProduct_pk = ?";

            // Verificar productos en el carrito
            DefaultTableModel dtm = (DefaultTableModel) cartTable.getModel();
            Set<Long> processedSoldProducts = new HashSet<>();
            Map<Long, Integer> productsToUpdateQuantity = new HashMap<>();

            for (int i = 0; i < dtm.getRowCount(); i++) {
                String uniqueId = dtm.getValueAt(i, 0).toString();
                int quantity = Integer.parseInt(dtm.getValueAt(i, 4).toString());
                long salePrice = Long.parseLong(dtm.getValueAt(i, 3).toString());

                // Obtener product_pk a partir del uniqueId
                String productQuery = "SELECT product_pk FROM products WHERE uniqueId = ?";
                long productPk = -1;
                try (PreparedStatement psProduct = con.prepareStatement(productQuery)) {
                    psProduct.setString(1, uniqueId);
                    try (ResultSet rs = psProduct.executeQuery()) {
                        if (rs.next()) {
                            productPk = rs.getLong("product_pk");
                        }
                    }
                }

                // Verificar si el producto ya está en "soldProducts_services"
                Long existingSoldProductPk = null;
                for (Map.Entry<Long, Long> entry : currentProducts.entrySet()) {
                    if (entry.getValue().equals(productPk)) {
                        existingSoldProductPk = entry.getKey();
                        break;
                    }
                }

                if (existingSoldProductPk != null) {
                    // Actualizar producto en "SoldProducts"
                    try (PreparedStatement psUpdate = con.prepareStatement(updateSoldProductQuery)) {
                        psUpdate.setInt(1, quantity);
                        psUpdate.setLong(2, salePrice);
                        psUpdate.setLong(3, existingSoldProductPk);
                        psUpdate.executeUpdate();
                    }
                    processedSoldProducts.add(existingSoldProductPk);
                } else {
                    // Insertar producto nuevo en "SoldProducts" y asociarlo
                    long newSoldProductPk;
                    try (PreparedStatement psInsert = con.prepareStatement(insertSoldProductQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        psInsert.setInt(1, quantity);
                        psInsert.setLong(2, salePrice);
                        psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                        psInsert.setLong(4, productPk);
                        psInsert.executeUpdate();
                        try (ResultSet generatedKeys = psInsert.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                newSoldProductPk = generatedKeys.getLong(1);
                            } else {
                                throw new SQLException("No se pudo obtener el ID del producto vendido.");
                            }
                        }
                    }

                    try (PreparedStatement psAssociate = con.prepareStatement(insertSoldProductsServicesQuery)) {
                        psAssociate.setLong(1, Long.parseLong(serviceId));
                        psAssociate.setLong(2, newSoldProductPk);
                        psAssociate.executeUpdate();
                    }

                    // Agregar producto al mapa de actualización de inventario
                    productsToUpdateQuantity.put(productPk, quantity);
                }
            }

            // Eliminar productos que ya no están en el carrito
            for (Long soldProductPk : currentProducts.keySet()) {
                if (!processedSoldProducts.contains(soldProductPk)) {
                    try (PreparedStatement psDelete = con.prepareStatement(deleteSoldProductsServicesQuery)) {
                        psDelete.setLong(1, soldProductPk);
                        psDelete.executeUpdate();
                    }
                    try (PreparedStatement psDeleteProduct = con.prepareStatement(deleteSoldProductQuery)) {
                        psDeleteProduct.setLong(1, soldProductPk);
                        psDeleteProduct.executeUpdate();
                    }
                }
            }

            // Actualizar el inventario solo para los productos nuevos
            updateProductQuantity(productsToUpdateQuantity);

            JOptionPane.showMessageDialog(null, "Servicio actualizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        dispose();
        new UpdateService(username).setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnSearchServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchServiceActionPerformed
        // botón para cargar los datos del servicio ingresado
        String serviceId = txtServiceID.getText().trim();
        btnSearchService.setEnabled(false);
        txtServiceID.setEditable(false);

        if (serviceId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID de servicio.", "Error", JOptionPane.ERROR_MESSAGE);
            btnSearchService.setEnabled(true);
            txtServiceID.setEditable(true);
            return;
        }

        String checkServiceStateQuery = "SELECT state FROM services WHERE service_pk = ?";
        String getServiceQuery = "SELECT motorbike_pk, totalPrice FROM services WHERE service_pk = ?";
        String getProductsQuery = "SELECT sp.quantity, sp.salePrice, p.uniqueId, p.description, p.productBrand "
                + "FROM soldProducts_services sps "
                + "INNER JOIN soldProducts sp ON sps.soldProduct_pk = sp.soldProduct_pk "
                + "INNER JOIN products p ON sp.product_pk = p.product_pk "
                + "WHERE sps.service_pk = ?";

        try (Connection con = ConnectionProvider.getCon()) {
            // Verificar el estado del servicio
            try (PreparedStatement psCheckState = con.prepareStatement(checkServiceStateQuery)) {
                psCheckState.setString(1, serviceId);
                try (ResultSet rsState = psCheckState.executeQuery()) {
                    if (rsState.next()) {
                        String state = rsState.getString("state");
                        if ("Terminado".equalsIgnoreCase(state)) {
                            JOptionPane.showMessageDialog(this, "El servicio ya está terminado y no puede ser editado.", "Servicio terminado", JOptionPane.WARNING_MESSAGE);
                            btnSearchService.setEnabled(true);
                            txtServiceID.setEditable(true);
                            return;
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "No se encontró el servicio con el ID proporcionado.", "Error", JOptionPane.ERROR_MESSAGE);
                        btnSearchService.setEnabled(true);
                        txtServiceID.setEditable(true);
                        return;
                    }
                }
            }

            // Obtener información del servicio
            try (PreparedStatement psService = con.prepareStatement(getServiceQuery)) {
                psService.setString(1, serviceId);
                try (ResultSet rsService = psService.executeQuery()) {
                    if (rsService.next()) {
                        String motorbikePk = rsService.getString("motorbike_pk");
                        long totalPrice = rsService.getLong("totalPrice");

                        txtPlate.setText(motorbikePk);
                        lblFinalTotalPrice.setText(String.valueOf(totalPrice));
                    } else {
                        JOptionPane.showMessageDialog(this, "No se encontró el servicio con el ID proporcionado.", "Error", JOptionPane.ERROR_MESSAGE);
                        btnSearchService.setEnabled(true);
                        txtServiceID.setEditable(true);
                        return;
                    }
                }
            }

            // Limpiar la tabla
            DefaultTableModel model = (DefaultTableModel) cartTable.getModel();
            model.setRowCount(0);

            // Obtener productos asociados al servicio
            try (PreparedStatement psProducts = con.prepareStatement(getProductsQuery)) {
                psProducts.setString(1, serviceId);
                try (ResultSet rsProducts = psProducts.executeQuery()) {
                    while (rsProducts.next()) {
                        String uniqueId = rsProducts.getString("uniqueId");
                        String description = rsProducts.getString("description");
                        String productBrand = rsProducts.getString("productBrand");
                        int quantity = rsProducts.getInt("quantity");
                        long salePrice = rsProducts.getLong("salePrice");
                        long totalProductPrice = quantity * salePrice;

                        model.addRow(new Object[]{uniqueId, description, productBrand, salePrice, quantity, totalProductPrice});
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar el servicio: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSearchServiceActionPerformed

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
            java.util.logging.Logger.getLogger(UpdateService.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UpdateService.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UpdateService.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UpdateService.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UpdateService().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddToCart;
    private javax.swing.JButton btnSearchService;
    private javax.swing.JTable cartTable;
    private javax.swing.JComboBox<String> comboPayment;
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
    private javax.swing.JTextField txtNoOfUnits;
    private javax.swing.JTextField txtPlate;
    private javax.swing.JTextField txtPricePerUnit;
    private javax.swing.JTextField txtProductBrand;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtServiceID;
    private javax.swing.JTextField txtTotalPrice;
    private javax.swing.JTextField txtTransferPaid;
    private javax.swing.JTextField txtUniqueId;
    // End of variables declaration//GEN-END:variables
}
