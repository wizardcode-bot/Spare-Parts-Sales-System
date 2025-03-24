package ui;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import ui.help.UpdateServiceHelp;

public class UpdateService extends javax.swing.JFrame {

    private String billId = "";
    private String username = "";
    private String serviceState = "";
    private long finalTotalPrice = 0;
    private long cashPaidInt = 0;
    private long transferPaidInt = 0;
    private long totalPriceLong = 0;
    private long servicePKLong = 0;
    private boolean selectedService = false;

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
        setSize(1366, 768);

        //establecer icono
        setImage();

        deadlineDate.setEnabled(false);
        deadlineLabel.setEnabled(false);

        comboPayment.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String paymentMethod = comboPayment.getSelectedItem().toString();

                if ("Contado".equals(paymentMethod)) {
                    deadlineDate.setEnabled(false);
                    deadlineLabel.setEnabled(false);

                    txtCashPaid.setEnabled(true);
                    txtTransferPaid.setEnabled(true);
                    transferTextLabel.setEnabled(true);
                    cashTextLabel.setEnabled(true);

                    txtBillPaid.setEnabled(true);
                    billPaidLabel.setEnabled(true);
                    calculateSurplusBtn.setEnabled(true);

                    surplusTextLabel.setEnabled(true);
                    surplusMoneyLabel.setEnabled(true);

                } else if ("Crédito".equals(paymentMethod)) {
                    deadlineDate.setEnabled(true);
                    deadlineLabel.setEnabled(true);

                    txtCashPaid.setEnabled(false);
                    txtTransferPaid.setEnabled(false);
                    transferTextLabel.setEnabled(false);
                    cashTextLabel.setEnabled(false);

                    txtBillPaid.setEnabled(false);
                    calculateSurplusBtn.setEnabled(false);
                    billPaidLabel.setEnabled(false);

                    surplusTextLabel.setEnabled(false);
                    surplusMoneyLabel.setEnabled(false);

                }
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

    // Verifica si hay productos en el carrito
    private boolean isCartEmpty() {
        DefaultTableModel dtm = (DefaultTableModel) cartTable.getModel();
        return dtm.getRowCount() == 0; // Devuelve true si está vacío, false si contiene filas
    }

    // Obtiene los productos actuales de un servicio desde la base de datos.
    private Map<String, Integer> getCurrentProducts(Connection con, long servicePK) throws SQLException {
        Map<String, Integer> currentProducts = new HashMap<>();
        String query = "SELECT sp.product_pk, sp.quantity "
                + "FROM soldProducts sp "
                + "JOIN soldProducts_services sps ON sp.soldProduct_pk = sps.soldProduct_pk "
                + "WHERE sps.service_pk = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setLong(1, servicePK);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String productPk = rs.getString("product_pk"); // Ahora es String
                    int quantity = rs.getInt("quantity");
                    currentProducts.put(productPk, quantity);
                }
            }
        }
        return currentProducts;
    }

    private void updateSoldProduct(Connection con, long servicePK, String productPk, int newQuantity, long salePrice) throws SQLException {
        String updateQuery = "UPDATE soldProducts sp "
                + "JOIN soldProducts_services sps ON sp.soldProduct_pk = sps.soldProduct_pk "
                + "SET sp.quantity = ?, sp.salePrice = ? "
                + "WHERE sps.service_pk = ? AND sp.product_pk = ?";
        try (PreparedStatement ps = con.prepareStatement(updateQuery)) {
            ps.setInt(1, newQuantity);
            ps.setLong(2, salePrice);
            ps.setLong(3, servicePK);
            ps.setString(4, productPk);
            ps.executeUpdate();
        }
    }

    private String getClientPk(Connection con, String vehiclePlate) throws SQLException {
        String query = "SELECT client_pk FROM motorbikes WHERE motorbike_pk = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, vehiclePlate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("client_pk");
                } else {
                    throw new SQLException("No se encontró un cliente asociado con el vehículo.");
                }
            }
        }
    }

    private long getAppUserPk(Connection con, String username) throws SQLException {
        String query = "SELECT appuser_pk FROM appusers WHERE username = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("appuser_pk");
                } else {
                    throw new SQLException("Usuario no encontrado.");
                }
            }
        }
    }

    private long insertSoldProduct(Connection con, int quantity, long salePrice, String productPk) throws SQLException {
        //método para insertar productos en la tabla soldProducts
        String query = "INSERT INTO soldProducts (quantity, salePrice, product_pk) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, quantity);
            ps.setLong(2, salePrice);
            ps.setString(3, productPk);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1); // Devuelve la clave primaria generada.
                } else {
                    throw new SQLException("No se pudo obtener el ID del producto vendido.");
                }
            }
        }
    }

    private void removeProductFromService(Connection con, String soldProductPk) throws SQLException {
        String query = "DELETE FROM soldProducts WHERE soldProduct_pk = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, soldProductPk);
            ps.executeUpdate();
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

    private long insertBill(Connection con, String billId, long totalPrice, String paymentTerm, long cashPaid, long transferPaid,
            String clientPk, long appuserPk) throws SQLException {
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

    private void updateService(Connection con, String vehiclePlate, String serviceState, long totalPrice, long servicePKLong) throws SQLException {
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

    private void clearProductFields() {
        txtUniqueId.setText("");
        txtDescription.setText("");
        txtProductBrand.setText("");
        txtPricePerUnit.setText("");
        txtNoOfUnits.setText("");
        txtTotalPrice.setText("");
    }

    private void generatePDF(String clientID, Long totalPrice, Long cashPaidInt, Long transferPaidInt, String billId, Timestamp paymentDate) {
        // Ajustar ancho a 58mm y dejar la altura dinámica
        com.itextpdf.text.Document doc = new com.itextpdf.text.Document(new Rectangle(226, PageSize.A4.getHeight()));
        //medida del documento
        doc.setMargins(10, 5, 10, 10); // Márgenes 
        
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

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String dateString = now.format(formatter);
            
            // Formatear la fecha de pago
            DateTimeFormatter paymentFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedPaymentDate = paymentDate.toLocalDateTime().format(paymentFormatter);

            PdfWriter.getInstance(doc, new FileOutputStream(ProductsUtils.billPath + "" + billId + ".pdf"));
            doc.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);

            Paragraph title = new Paragraph("Factura de venta\nMOTOREPUESTOS GOOFY\nNIT: " + NIT + "\nDirección: "
                    + ADDRESS + "\nNo Responsable De IVA\n", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);

            Paragraph details = new Paragraph("\nID de factura: " + billId
                    + "\nFecha: " + dateString
                    + "\nVendedor: " + vendorName
                    + "\nCliente: " + clientName
                    + "\nNIT/CC: " + (client_pk != null ? client_pk : "No disponible") + "\n", normalFont);
            doc.add(details);

            Paragraph separator = new Paragraph("-".repeat(57) + "\n\n", normalFont);
            separator.setAlignment(Element.ALIGN_CENTER);
            doc.add(separator);

            PdfPTable tbl = new PdfPTable(4);
            tbl.setWidthPercentage(100);
            tbl.setWidths(new float[]{4, 3, 3, 3});

            PdfPCell cell;

            cell = new PdfPCell(new Phrase("Producto", boldFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tbl.addCell(cell);

            cell = new PdfPCell(new Phrase("P. Unidad", boldFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tbl.addCell(cell);

            cell = new PdfPCell(new Phrase("Cant.", boldFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tbl.addCell(cell);

            cell = new PdfPCell(new Phrase("Total", boldFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tbl.addCell(cell);

            for (int i = 0; i < cartTable.getRowCount(); i++) {
                cell = new PdfPCell(new Phrase(cartTable.getValueAt(i, 1).toString(), normalFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tbl.addCell(cell);

                cell = new PdfPCell(new Phrase(cartTable.getValueAt(i, 3).toString(), normalFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tbl.addCell(cell);

                cell = new PdfPCell(new Phrase(cartTable.getValueAt(i, 4).toString(), normalFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tbl.addCell(cell);

                cell = new PdfPCell(new Phrase(cartTable.getValueAt(i, 5).toString(), normalFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tbl.addCell(cell);
            }

            doc.add(tbl);
            doc.add(separator);
            
            Paragraph paymentTitle = new Paragraph("DETALLE DE PAGO\n", boldFont);
            paymentTitle.setAlignment(Element.ALIGN_CENTER);
            doc.add(paymentTitle);

            Paragraph total = new Paragraph("Total: " + totalPrice + "\n", boldFont);
            doc.add(total);

            Paragraph paymentDetails = new Paragraph("Forma de pago: " + paymentTerm
                    + "\nEfectivo: " + cashPaidInt
                    + "\nTransferencia: " + transferPaidInt
                    + "\nFecha de pago: " + formattedPaymentDate + "\n", normalFont);
            doc.add(paymentDetails);

            doc.add(separator);
            Paragraph disclaimerMsg = new Paragraph(
                    "NO SE HACE DEVOLUCIÓN DE DINERO\n"
                    + "Después de ocho (8) días no se aceptan devoluciones.\n"
                    + "No hay devoluciones en partes eléctricas.\n"
                    + "Indispensable presentar esta factura.\n"
                    + "¡Gracias por tu compra!", normalFont);
            disclaimerMsg.setAlignment(Element.ALIGN_CENTER);
            doc.add(disclaimerMsg);

            JOptionPane.showMessageDialog(null, "Factura generada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            //preguntar si se desea imprimir
            int a = JOptionPane.showOptionDialog(null, "¿Quieres imprimir la factura?", "Selecciona una opción",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Sí", "No"}, "Sí");
            if (a == 0) {
                OpenPdf.openById(String.valueOf(billId));
            }

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
        txtUniqueId = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        comboPayment = new javax.swing.JComboBox<>();
        cashTextLabel = new javax.swing.JLabel();
        transferTextLabel = new javax.swing.JLabel();
        txtCashPaid = new javax.swing.JTextField();
        txtTransferPaid = new javax.swing.JTextField();
        txtDescription = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        txtServiceID = new javax.swing.JTextField();
        btnSearchService = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        txtPlate = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        deadlineLabel = new javax.swing.JLabel();
        deadlineDate = new com.toedter.calendar.JDateChooser();
        billPaidLabel = new javax.swing.JLabel();
        txtBillPaid = new javax.swing.JTextField();
        calculateSurplusBtn = new javax.swing.JButton();
        surplusTextLabel = new javax.swing.JLabel();
        surplusMoneyLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
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
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(532, 6, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Buscar producto por ID o descripción");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(64, 126, -1, -1));

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
        getContentPane().add(txtSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(51, 147, 370, -1));

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

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(51, 176, 370, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("ID del Producto");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 253, -1, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Descripción");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 309, -1, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Marca");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 365, -1, -1));

        txtProductBrand.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtProductBrand, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 386, 300, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Precio por Unidad");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(821, 127, -1, -1));

        txtPricePerUnit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtPricePerUnit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPricePerUnitKeyReleased(evt);
            }
        });
        getContentPane().add(txtPricePerUnit, new org.netbeans.lib.awtextra.AbsoluteConstraints(821, 148, 220, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Número de Unidades *");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(821, 197, -1, -1));

        txtNoOfUnits.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtNoOfUnits.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNoOfUnitsKeyReleased(evt);
            }
        });
        getContentPane().add(txtNoOfUnits, new org.netbeans.lib.awtextra.AbsoluteConstraints(821, 218, 220, -1));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Precio Total");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(821, 253, -1, -1));

        txtTotalPrice.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtTotalPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(821, 274, 220, -1));

        btnAddToCart.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAddToCart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/addToCart.png"))); // NOI18N
        btnAddToCart.setText("Añadir al carrito");
        btnAddToCart.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddToCart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddToCartActionPerformed(evt);
            }
        });
        getContentPane().add(btnAddToCart, new org.netbeans.lib.awtextra.AbsoluteConstraints(819, 309, -1, -1));

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

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 485, 859, 233));

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Precio total:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 441, -1, -1));

        lblFinalTotalPrice.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblFinalTotalPrice.setForeground(new java.awt.Color(255, 255, 255));
        lblFinalTotalPrice.setText("---");
        getContentPane().add(lblFinalTotalPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 441, -1, -1));

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print.png"))); // NOI18N
        jButton3.setText("Finalizar servicio e Imprimir");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(108, 672, -1, -1));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Seleccione en la tabla el producto a eliminar ");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 730, -1, -1));

        txtUniqueId.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtUniqueId, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 274, 300, -1));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Forma y medio de pago *");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(1077, 127, -1, -1));

        comboPayment.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        comboPayment.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Contado", "Crédito" }));
        comboPayment.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getContentPane().add(comboPayment, new org.netbeans.lib.awtextra.AbsoluteConstraints(1077, 148, 180, -1));

        cashTextLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cashTextLabel.setForeground(new java.awt.Color(255, 255, 255));
        cashTextLabel.setText("Efectivo ($)");
        getContentPane().add(cashTextLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1077, 195, -1, -1));

        transferTextLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        transferTextLabel.setForeground(new java.awt.Color(255, 255, 255));
        transferTextLabel.setText("Transferencia ($)");
        getContentPane().add(transferTextLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1077, 251, -1, -1));

        txtCashPaid.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtCashPaid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCashPaidKeyReleased(evt);
            }
        });
        getContentPane().add(txtCashPaid, new org.netbeans.lib.awtextra.AbsoluteConstraints(1077, 216, 210, -1));

        txtTransferPaid.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtTransferPaid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTransferPaidKeyReleased(evt);
            }
        });
        getContentPane().add(txtTransferPaid, new org.netbeans.lib.awtextra.AbsoluteConstraints(1077, 272, 210, -1));

        txtDescription.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 330, 300, -1));

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/saveProgress.png"))); // NOI18N
        jButton4.setText("Guardar proceso");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(146, 609, -1, -1));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Ingrese el ID del servicio a actualizar *");
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 126, -1, -1));

        txtServiceID.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtServiceID, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 152, 217, -1));

        btnSearchService.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
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
        getContentPane().add(btnSearchService, new org.netbeans.lib.awtextra.AbsoluteConstraints(687, 147, 102, -1));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Placa del vehículo");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 197, -1, -1));

        txtPlate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtPlate, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 218, 222, -1));

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jLabel19.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel19MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(1320, 6, -1, -1));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/help.png"))); // NOI18N
        jLabel14.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel14MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(1284, 14, -1, -1));

        deadlineLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        deadlineLabel.setForeground(new java.awt.Color(255, 255, 255));
        deadlineLabel.setText("Fecha límite de pago *");
        getContentPane().add(deadlineLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1077, 307, -1, -1));
        getContentPane().add(deadlineDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(1077, 328, 236, -1));

        billPaidLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        billPaidLabel.setForeground(new java.awt.Color(255, 255, 255));
        billPaidLabel.setText("Paga con ($)");
        getContentPane().add(billPaidLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1077, 364, -1, -1));

        txtBillPaid.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtBillPaid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBillPaidKeyReleased(evt);
            }
        });
        getContentPane().add(txtBillPaid, new org.netbeans.lib.awtextra.AbsoluteConstraints(1077, 385, 160, -1));

        calculateSurplusBtn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        calculateSurplusBtn.setText("Calcular");
        calculateSurplusBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        calculateSurplusBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateSurplusBtnActionPerformed(evt);
            }
        });
        getContentPane().add(calculateSurplusBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(1243, 385, 80, -1));

        surplusTextLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        surplusTextLabel.setForeground(new java.awt.Color(255, 255, 255));
        surplusTextLabel.setText("Excedente");
        getContentPane().add(surplusTextLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1077, 420, -1, -1));

        surplusMoneyLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        surplusMoneyLabel.setForeground(new java.awt.Color(51, 204, 255));
        surplusMoneyLabel.setText("$ 0");
        getContentPane().add(surplusMoneyLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1077, 441, -1, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 59, 1366, 10));

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
                if (!noOfUnits.matches(Validations.NUMBER_PATTERN)) {
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
        // Añadir al carrito, actualizar el stock del producto ingresado y actualizar el precio total 
        if (!selectedService) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar el servicio que quieres actualizar",
                    "No hay servicios seleccionados", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            finalTotalPrice = Long.parseLong(lblFinalTotalPrice.getText().trim());
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

            int rowIndex = -1;
            for (int i = 0; i < dtm.getRowCount(); i++) {
                if (dtm.getValueAt(i, 0).toString().equals(uniqueId)) {
                    rowIndex = i;
                    break;
                }
            }

            try (Connection con = ConnectionProvider.getCon()) {
                if (rowIndex == -1) {
                    // Producto nuevo en el carrito
                    try (PreparedStatement pst = con.prepareStatement("SELECT * FROM products WHERE product_pk = ?")) {
                        pst.setString(1, uniqueId);
                        try (ResultSet rs = pst.executeQuery()) {
                            if (rs.next()) {
                                int availableQuantity = rs.getInt("quantity");
                                if (availableQuantity >= nUnits) {
                                    String description = txtDescription.getText();
                                    String productBrand = txtProductBrand.getText();
                                    long totalPrice = nUnits * Long.parseLong(pricePerUnit);

                                    // Añadir al carrito
                                    dtm.addRow(new Object[]{uniqueId, description, productBrand, pricePerUnit, nUnits, totalPrice});
                                    finalTotalPrice += totalPrice;
                                    lblFinalTotalPrice.setText(String.valueOf(finalTotalPrice));

                                    // Actualizar el inventario
                                    try (PreparedStatement updatePst = con.prepareStatement(
                                            "UPDATE products SET quantity = quantity - ? WHERE product_pk = ?")) {
                                        updatePst.setInt(1, nUnits);
                                        updatePst.setString(2, uniqueId);
                                        updatePst.executeUpdate();
                                    }

                                    JOptionPane.showMessageDialog(null, "¡Producto añadido exitosamente!");
                                    clearProductFields();

                                    //verificar si el stock es menor a 5 unidades
                                    long updatedStock = availableQuantity - Integer.parseInt(noOfUnits);
                                    if (updatedStock < 5) {
                                        JOptionPane.showMessageDialog(null, "¡Te quedan menos de 5 unidades de este producto en stock!", "Advertencia",
                                                JOptionPane.WARNING_MESSAGE);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "No hay suficiente stock para este producto. Solo quedan "
                                            + availableQuantity + " unidades.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Producto no encontrado en la base de datos.", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } else {
                    // Actualizar producto existente en el carrito
                    try (PreparedStatement ps = con.prepareStatement("SELECT quantity FROM products WHERE product_pk = ?")) {
                        ps.setString(1, uniqueId);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                int availableStock = rs.getInt("quantity");
                                int currentQuantity = Integer.parseInt(dtm.getValueAt(rowIndex, 4).toString());
                                int updatedQuantity = currentQuantity + nUnits;

                                if (availableStock + currentQuantity >= updatedQuantity) {
                                    dtm.setValueAt(updatedQuantity, rowIndex, 4);

                                    long currentTotalPrice = Long.parseLong(dtm.getValueAt(rowIndex, 5).toString());
                                    long additionalPrice = nUnits * Long.parseLong(pricePerUnit);
                                    long newTotalPrice = currentTotalPrice + additionalPrice;

                                    finalTotalPrice = (finalTotalPrice - currentTotalPrice) + newTotalPrice;

                                    dtm.setValueAt(newTotalPrice, rowIndex, 5);
                                    lblFinalTotalPrice.setText(String.valueOf(finalTotalPrice));

                                    // Actualizar el inventario
                                    try (PreparedStatement updatePst = con.prepareStatement(
                                            "UPDATE products SET quantity = quantity - ? WHERE product_pk = ?")) {
                                        updatePst.setInt(1, nUnits);
                                        updatePst.setString(2, uniqueId);
                                        updatePst.executeUpdate();
                                    }

                                    JOptionPane.showMessageDialog(null, "Cantidad actualizada exitosamente.");
                                    clearProductFields();
                                } else {
                                    JOptionPane.showMessageDialog(null, "No hay suficiente stock para completar esta operación.",
                                            "Error de stock", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al procesar la operación: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el ID del producto y el número de unidades!",
                    "No hay productos seleccionados", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAddToCartActionPerformed

    private void cartTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cartTableMouseClicked
        // Eliminar producto del carrito, actualizar la cantidad en stock y el precio total
        int index = cartTable.getSelectedRow();
        if (index >= 0) {
            int a = JOptionPane.showOptionDialog(null, "¿Quieres eliminar este producto?", "Selecciona una opción",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Sí", "No"}, "Sí");
            if (a == 0) {
                TableModel model = cartTable.getModel();

                try {
                    finalTotalPrice = Integer.parseInt(lblFinalTotalPrice.getText().trim());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Error al sincronizar el precio total. Verifica los valores existentes.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String uniqueId = model.getValueAt(index, 0).toString(); // ID único del producto
                int currentCartQuantity = Integer.parseInt(model.getValueAt(index, 4).toString()); // Cantidad en el carrito

                try (Connection con = ConnectionProvider.getCon()) {
                    // Recuperar el inventario actual del producto
                    String query = "SELECT quantity FROM products WHERE product_pk = ?";
                    int availableStock = 0;
                    try (PreparedStatement ps = con.prepareStatement(query)) {
                        ps.setString(1, uniqueId);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                availableStock = rs.getInt("quantity");
                            }
                        }
                    }

                    // Actualizar el inventario añadiendo la cantidad eliminada del carrito
                    String updateQuery = "UPDATE products SET quantity = ? WHERE product_pk = ?";
                    try (PreparedStatement ps = con.prepareStatement(updateQuery)) {
                        ps.setInt(1, availableStock + currentCartQuantity);
                        ps.setString(2, uniqueId);
                        ps.executeUpdate();
                    }

                    // Actualizar el precio total y eliminar el producto del carrito
                    String total = model.getValueAt(index, 5).toString(); // Precio total del producto
                    finalTotalPrice -= Integer.parseInt(total);
                    lblFinalTotalPrice.setText(String.valueOf(finalTotalPrice));

                    // Eliminar la fila correspondiente del carrito
                    ((DefaultTableModel) cartTable.getModel()).removeRow(index);

                    JOptionPane.showMessageDialog(null, "Stock actualizado correctamente.");
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al actualizar el inventario: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, selecciona un producto para eliminar.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_cartTableMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // BOTÓN FINALIZAR PROCESO Y GENERAR FACTURA DE VENTA

        String selectedVehicle = txtPlate.getText().trim();
        String paymentTerm = comboPayment.getSelectedItem().toString();
        String cashPaid = null;
        String transferPaid = null;
        String servicePK = txtServiceID.getText().trim();
        serviceState = "Terminado";
        long totalPrice = 0;

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

        if ("Seleccione un vehículo".equals(selectedVehicle)) {
            JOptionPane.showMessageDialog(null, "¡Debes relacionar un vehículo al servicio!",
                    "No hay vehículo relacionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if ("Contado".equals(paymentTerm)) {
            cashPaid = txtCashPaid.getText();
            transferPaid = txtTransferPaid.getText();

            if (Validations.isNullOrBlank(cashPaid) || Validations.isNullOrBlank(transferPaid)) {
                JOptionPane.showMessageDialog(null, "Debes completar el medio de pago (Efectivo y Transferencia)",
                        "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                totalPrice = Long.parseLong(lblFinalTotalPrice.getText().trim());
                cashPaidInt = Integer.parseInt(cashPaid);
                transferPaidInt = Integer.parseInt(transferPaid);

                if (cashPaidInt + transferPaidInt != totalPrice) {
                    JOptionPane.showMessageDialog(null, "La suma de efectivo y transferencia debe ser igual al total pagado.");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Debes ingresar los valores en números",
                        "Datos incorrectos", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else if ("Crédito".equals(paymentTerm)) {
            cashPaid = "0";
            transferPaid = "0";

            try {
                totalPrice = Long.parseLong(lblFinalTotalPrice.getText().trim());
                cashPaidInt = Integer.parseInt(cashPaid);
                transferPaidInt = Integer.parseInt(transferPaid);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Valor de transferencia y efectivo no válidos.");
            }
        }

        try {
            long servicePKLong = Long.parseLong(servicePK);

            try (Connection con = ConnectionProvider.getCon()) {
                // Obtener client_pk basado en la placa del vehículo
                String clientID = getClientPk(con, selectedVehicle);

                // Obtener appuser_pk basado en el usuario activo
                long appuserPk = getAppUserPk(con, username);

                // Generar un ID único para la factura
                String billId = getUniqueId("FACT-");

                long billPk = insertBill(con, billId, totalPrice, paymentTerm, cashPaidInt, transferPaidInt, clientID, appuserPk);
                
                // Variable para almacenar la fecha de pago
                Timestamp paymentDate;

                // Insertar crédito si el pago es "Crédito"
                if ("Crédito".equals(paymentTerm)) {
                    java.util.Date utilDate = deadlineDate.getDate(); // Obtener fecha del JDateChooser
                    if (utilDate != null) {
                        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                        paymentDate = new Timestamp(sqlDate.getTime()); // Fecha límite de pago
                    } else {
                        JOptionPane.showMessageDialog(null, "Debes seleccionar una fecha de vencimiento para el crédito.");
                        return;
                    }

                    String insertCreditQuery = "INSERT INTO clients_credits (client_pk, totalCredit, pendingBalance, creditDate, paymentDeadline,"
                            + " creditState, lastModified) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

                    try (PreparedStatement psCredit = con.prepareStatement(insertCreditQuery)) {
                        psCredit.setString(1, clientID);
                        psCredit.setLong(2, finalTotalPrice);
                        psCredit.setLong(3, finalTotalPrice);
                        psCredit.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now())); // Fecha del crédito
                        psCredit.setTimestamp(5, paymentDate); // Fecha límite de pago
                        psCredit.setString(6, "Pendiente");
                        psCredit.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now())); // Guardar la fecha en lastModified
                        psCredit.executeUpdate();
                    }
                } else {
                    paymentDate = Timestamp.valueOf(LocalDateTime.now()); // Fecha actual si es contado
                }

                // Actualizar la tabla services
                updateService(con, selectedVehicle, serviceState, totalPrice, servicePKLong);

                // Obtener productos actuales del servicio desde la base de datos
                Map<String, Integer> currentProducts = getCurrentProducts(con, servicePKLong);

                // Procesar productos en el carrito
                DefaultTableModel dtm = (DefaultTableModel) cartTable.getModel();
                for (int i = 0; i < dtm.getRowCount(); i++) {
                    String uniqueId = dtm.getValueAt(i, 0).toString(); // Asegurar que es un String
                    int newQuantity = Integer.parseInt(dtm.getValueAt(i, 4).toString());
                    long salePrice = Long.parseLong(dtm.getValueAt(i, 3).toString());

                    if (currentProducts.containsKey(uniqueId)) {
                        // Actualizar producto existente
                        updateSoldProduct(con, servicePKLong, uniqueId, newQuantity, salePrice);
                        currentProducts.remove(uniqueId);
                    } else {
                        // Insertar nuevo producto vendido
                        long soldProductPk = insertSoldProduct(con, newQuantity, salePrice, uniqueId);
                        associateProductWithBill(con, billPk, soldProductPk);
                    }
                }

                // Eliminar productos que ya no están en el carrito
                for (Map.Entry<String, Integer> entry : currentProducts.entrySet()) {
                    String soldProductPk = entry.getKey(); // Ahora es String
                    removeProductFromService(con, soldProductPk);
                }

                // Generar PDF de la venta
                generatePDF(clientID, totalPrice, cashPaidInt, transferPaidInt, billId, paymentDate);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            dispose();
            new UpdateService(username).setVisible(true);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese un número válido.");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

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

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // BOTÓN DE GUARDAR PROCESO
        String selectedVehicle = txtPlate.getText().trim();
        String serviceId = txtServiceID.getText().trim();
        String serviceState = "En proceso";

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

        long totalPriceLong;
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

            // Obtener productos actuales del servicio desde la base de datos
            String getCurrentProductsQuery = """
        SELECT sps.soldProduct_pk, sp.product_pk, sp.quantity
        FROM soldProducts_services sps
        JOIN SoldProducts sp ON sps.soldProduct_pk = sp.soldProduct_pk
        WHERE sps.service_pk = ?
        """;
            Map<String, Long> currentProducts = new HashMap<>(); // product_pk -> soldProduct_pk
            try (PreparedStatement psGetProducts = con.prepareStatement(getCurrentProductsQuery)) {
                psGetProducts.setLong(1, Long.parseLong(serviceId));
                try (ResultSet rs = psGetProducts.executeQuery()) {
                    while (rs.next()) {
                        currentProducts.put(rs.getString("product_pk"), rs.getLong("soldProduct_pk"));
                    }
                }
            }

            // Preparar consultas
            String insertSoldProductQuery = "INSERT INTO SoldProducts (quantity, salePrice, saleDate, product_pk) VALUES (?, ?, ?, ?)";
            String insertSoldProductsServicesQuery = "INSERT INTO soldProducts_services (service_pk, soldProduct_pk) VALUES (?, ?)";
            String updateSoldProductQuery = "UPDATE SoldProducts SET quantity = ? WHERE soldProduct_pk = ?";
            String deleteSoldProductsServicesQuery = "DELETE FROM soldProducts_services WHERE soldProduct_pk = ?";
            String deleteSoldProductQuery = "DELETE FROM SoldProducts WHERE soldProduct_pk = ?";

            // Procesar productos en el carrito
            DefaultTableModel dtm = (DefaultTableModel) cartTable.getModel();
            Set<String> productsInCart = new HashSet<>(); // Para identificar productos que permanecen en el carrito.

            for (int i = 0; i < dtm.getRowCount(); i++) {
                String uniqueId = dtm.getValueAt(i, 0).toString();
                int quantity = Integer.parseInt(dtm.getValueAt(i, 4).toString());
                long salePrice = Long.parseLong(dtm.getValueAt(i, 3).toString());

                productsInCart.add(uniqueId);

                if (currentProducts.containsKey(uniqueId)) {
                    // El producto ya existe, actualizar la cantidad sin sumarla
                    long soldProductPk = currentProducts.get(uniqueId);
                    String getCurrentQuantityQuery = "SELECT quantity FROM SoldProducts WHERE soldProduct_pk = ?";
                    long currentQuantity = 0;
                    try (PreparedStatement psGetQuantity = con.prepareStatement(getCurrentQuantityQuery)) {
                        psGetQuantity.setLong(1, soldProductPk);
                        try (ResultSet rs = psGetQuantity.executeQuery()) {
                            if (rs.next()) {
                                currentQuantity = rs.getLong("quantity");
                            }
                        }
                    }

                    // Actualizar la cantidad directamente con el valor del carrito
                    try (PreparedStatement psUpdateSoldProduct = con.prepareStatement(updateSoldProductQuery)) {
                        psUpdateSoldProduct.setInt(1, quantity); // Actualizar con la nueva cantidad
                        psUpdateSoldProduct.setLong(2, soldProductPk);
                        psUpdateSoldProduct.executeUpdate();
                    }
                    currentProducts.remove(uniqueId); // Eliminar el producto de la lista de productos a eliminar
                } else {
                    // Producto nuevo, insertar en SoldProducts y asociar con el servicio
                    long soldProductPk;
                    try (PreparedStatement psInsertSoldProduct = con.prepareStatement(insertSoldProductQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        psInsertSoldProduct.setInt(1, quantity);
                        psInsertSoldProduct.setLong(2, salePrice);
                        psInsertSoldProduct.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                        psInsertSoldProduct.setString(4, uniqueId);
                        psInsertSoldProduct.executeUpdate();

                        try (ResultSet generatedKeys = psInsertSoldProduct.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                soldProductPk = generatedKeys.getLong(1);
                            } else {
                                throw new SQLException("No se pudo obtener el ID del producto vendido.");
                            }
                        }
                    }

                    try (PreparedStatement psAssociate = con.prepareStatement(insertSoldProductsServicesQuery)) {
                        psAssociate.setLong(1, Long.parseLong(serviceId));
                        psAssociate.setLong(2, soldProductPk);
                        psAssociate.executeUpdate();
                    }
                }
            }

            // Eliminar productos que ya no están en el carrito
            for (Map.Entry<String, Long> entry : currentProducts.entrySet()) {
                long soldProductPk = entry.getValue();
                try (PreparedStatement psDeleteServiceAssociation = con.prepareStatement(deleteSoldProductsServicesQuery)) {
                    psDeleteServiceAssociation.setLong(1, soldProductPk);
                    psDeleteServiceAssociation.executeUpdate();
                }
                try (PreparedStatement psDeleteSoldProduct = con.prepareStatement(deleteSoldProductQuery)) {
                    psDeleteSoldProduct.setLong(1, soldProductPk);
                    psDeleteSoldProduct.executeUpdate();
                }
            }

            JOptionPane.showMessageDialog(null, "Servicio actualizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el servicio: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
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

        if (!serviceId.matches(Validations.NUMBER_PATTERN)) {
            JOptionPane.showMessageDialog(this, "El ID del servicio debe ser ingresado en números", "Advertencia",
                    JOptionPane.ERROR_MESSAGE);
        }

        String checkServiceStateQuery = "SELECT state FROM services WHERE service_pk = ?";
        String getServiceQuery = "SELECT motorbike_pk, totalPrice FROM services WHERE service_pk = ?";
        String getProductsQuery = "SELECT sp.quantity, sp.salePrice, p.product_pk, p.description, p.productBrand "
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
                            JOptionPane.showMessageDialog(this, "El servicio ya está terminado y no puede ser editado.",
                                    "Servicio terminado", JOptionPane.WARNING_MESSAGE);
                            btnSearchService.setEnabled(true);
                            txtServiceID.setEditable(true);
                            return;
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "No se encontró el servicio con el ID proporcionado.",
                                "Error", JOptionPane.ERROR_MESSAGE);
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
                        String uniqueId = rsProducts.getString("product_pk");
                        String description = rsProducts.getString("description");
                        String productBrand = rsProducts.getString("productBrand");
                        int quantity = rsProducts.getInt("quantity");
                        long salePrice = rsProducts.getLong("salePrice");
                        long totalProductPrice = quantity * salePrice;

                        model.addRow(new Object[]{uniqueId, description, productBrand, salePrice, quantity, totalProductPrice});
                        selectedService = true;
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar el servicio: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSearchServiceActionPerformed

    private void jLabel19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19MouseClicked
        //botón para cerrar ventana
        if (!isCartEmpty()) {
            JOptionPane.showMessageDialog(null, "Debes guardar el proceso del servicio antes de salir.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        } else {
            dispose();
        }
    }//GEN-LAST:event_jLabel19MouseClicked

    private void jLabel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseClicked
        new UpdateServiceHelp().setVisible(true);
    }//GEN-LAST:event_jLabel14MouseClicked

    private void txtBillPaidKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBillPaidKeyReleased
        String billPaid = txtBillPaid.getText().trim();

        if (!billPaid.matches(Validations.NUMBER_PATTERN)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el valor en números!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtBillPaid.setText("");
            return;
        }
    }//GEN-LAST:event_txtBillPaidKeyReleased

    private void calculateSurplusBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculateSurplusBtnActionPerformed
        // botón para calcular excedente
        String billPaid = txtBillPaid.getText().trim();
        String transferPaid = txtTransferPaid.getText().trim();
        String cashPaid = txtCashPaid.getText().trim();

        if (Validations.isNullOrBlank(transferPaid) || Validations.isNullOrBlank(cashPaid)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar primero el valor pagado por efectivo y por tranferencia!",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            txtBillPaid.setText("");
            return;
        }

        // Calcular el excedente a entregar
        try {
            int transferPaidInt = Integer.parseInt(transferPaid);
            int cashPaidInt = Integer.parseInt(cashPaid);
            int billPaidInt = Integer.parseInt(billPaid);

            int totalPaid = (transferPaidInt + cashPaidInt);
            int surplusMoney = (billPaidInt - totalPaid);

            // Mostrar el resultado en el label
            surplusMoneyLabel.setText(String.format("%,d", surplusMoney));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_calculateSurplusBtnActionPerformed

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
    private javax.swing.JLabel billPaidLabel;
    private javax.swing.JButton btnAddToCart;
    private javax.swing.JButton btnSearchService;
    private javax.swing.JButton calculateSurplusBtn;
    private javax.swing.JTable cartTable;
    private javax.swing.JLabel cashTextLabel;
    private javax.swing.JComboBox<String> comboPayment;
    private com.toedter.calendar.JDateChooser deadlineDate;
    private javax.swing.JLabel deadlineLabel;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
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
    private javax.swing.JLabel surplusMoneyLabel;
    private javax.swing.JLabel surplusTextLabel;
    private javax.swing.JLabel transferTextLabel;
    private javax.swing.JTextField txtBillPaid;
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
