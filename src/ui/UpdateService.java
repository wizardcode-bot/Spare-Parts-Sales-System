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

public class UpdateService extends javax.swing.JFrame {

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
    public UpdateService() {
        initComponents();
    }

    public UpdateService(String tempUsername) {
        initComponents();
        username = tempUsername;
        setLocationRelativeTo(null);
        setSize(1366, 778);
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

    private void updateProductQuantity() {
        // método para actualizar la cantidad del producto en inventario
        DefaultTableModel dtm = (DefaultTableModel) cartTable.getModel();
        if (cartTable.getRowCount() != 0) {
            try (Connection con = ConnectionProvider.getCon()) {
                for (int i = 0; i < cartTable.getRowCount(); i++) {
                    String productIdString = dtm.getValueAt(i, 0).toString().trim();
                    int quantityToReduce = Integer.parseInt(dtm.getValueAt(i, 4).toString());

                    String updateQuery = "UPDATE products SET quantity = quantity - ? WHERE uniqueId = ?";
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Actualizar Servicio");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Buscar producto por ID o descripción");

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

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("ID del Producto");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Descripción");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Marca");

        txtProductBrand.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Precio por Unidad");

        txtPricePerUnit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtPricePerUnit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPricePerUnitKeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Número de Unidades *");

        txtNoOfUnits.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtNoOfUnits.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNoOfUnitsKeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Precio Total");

        txtTotalPrice.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

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

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Precio total:");

        lblFinalTotalPrice.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblFinalTotalPrice.setForeground(new java.awt.Color(255, 255, 255));
        lblFinalTotalPrice.setText("---");

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

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Seleccione en la tabla el producto a eliminar ");

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jLabel14.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel14MouseReleased(evt);
            }
        });

        txtUniqueId.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Forma de pago *");

        comboPayment.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        comboPayment.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Contado", "Crédito" }));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Medio de pago *");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Efectivo ($)");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Transferencia ($)");

        txtCashPaid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCashPaidKeyReleased(evt);
            }
        });

        txtTransferPaid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTransferPaidKeyReleased(evt);
            }
        });

        txtDescription.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

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

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Ingrese el ID del servicio a actualizar");

        txtServiceID.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

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

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Placa del vehículo");

        txtPlate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(477, 477, 477)
                        .addComponent(jLabel14))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(68, 68, 68)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(94, 94, 94)
                                        .addComponent(jButton4))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(69, 69, 69)
                                        .addComponent(jButton3))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(81, 81, 81)
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(68, 68, 68)
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(54, 54, 54)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(22, 22, 22)
                                .addComponent(lblFinalTotalPrice)
                                .addGap(464, 464, 464)
                                .addComponent(btnAddToCart))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 802, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(296, 296, 296)
                                .addComponent(jLabel13))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(txtUniqueId, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4)
                                    .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtProductBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel11)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtPlate, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtServiceID, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnSearchService, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(177, 177, 177)
                                        .addComponent(jLabel8))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(177, 177, 177)
                                        .addComponent(txtTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(177, 177, 177)
                                        .addComponent(jLabel10))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(177, 177, 177)
                                        .addComponent(comboPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(277, 277, 277)
                                        .addComponent(jLabel15))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(177, 177, 177)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel16)
                                                .addGap(29, 29, 29)
                                                .addComponent(txtCashPaid, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel17)
                                                .addGap(7, 7, 7)
                                                .addComponent(txtTransferPaid, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(177, 177, 177)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtNoOfUnits, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel7)
                                            .addComponent(txtPricePerUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6))))))
                        .addGap(0, 66, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel14))
                .addGap(9, 9, 9)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel2)
                        .addGap(6, 6, 6))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtServiceID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSearchService)))
                    .addComponent(txtPricePerUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jButton4)
                        .addGap(11, 11, 11)
                        .addComponent(jButton3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtNoOfUnits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPlate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(6, 6, 6)
                                .addComponent(txtTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(6, 6, 6)
                                .addComponent(txtUniqueId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(comboPayment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(6, 6, 6)
                                .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(5, 5, 5)
                                        .addComponent(jLabel16))
                                    .addComponent(txtCashPaid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtProductBrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(jLabel17))
                            .addComponent(txtTransferPaid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAddToCart)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(lblFinalTotalPrice))))
                        .addGap(14, 14, 14)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jLabel13))))
        );

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

            if (!noOfUnits.matches(Validations.numberPattern)) {
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

            int nUnits = Integer.parseInt(noOfUnits); //se convierte el string #units a entero para la comparación
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
                    pst.setString(1, uniqueId); // Sustituir el parámetro único ID
                    try (ResultSet rs = pst.executeQuery()) {
                        if (rs.next()) {
                            int availableQuantity = rs.getInt("quantity");

                            if (availableQuantity >= Integer.parseInt(noOfUnits)) {
                                String description = txtDescription.getText();
                                String productBrand = txtProductBrand.getText();
                                //String pricePerUnit = txtPricePerUnit.getText();
                                String totalPrice = txtTotalPrice.getText();

                                dtm.addRow(new Object[]{uniqueId, description, productBrand, pricePerUnit, noOfUnits, totalPrice});

                                int totalPriceInt = Integer.parseInt(totalPrice);
                                finalTotalPrice += totalPriceInt;
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
        int a = JOptionPane.showOptionDialog(null, "¿Quieres eliminar este producto?", "Selecciona una opción",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Sí", "No"}, "Sí");
        if (a == 0) {
            TableModel model = cartTable.getModel();
            String total = model.getValueAt(index, 5).toString();
            finalTotalPrice = finalTotalPrice - Integer.parseInt(total);
            lblFinalTotalPrice.setText(String.valueOf(finalTotalPrice));
            ((DefaultTableModel) cartTable.getModel()).removeRow(index);
        }
    }//GEN-LAST:event_cartTableMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // BOTÓN GENERAR VENTA E IMPRIMIR

        String selectedVehicle = txtPlate.getText().trim();
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
                    String productQuery = "SELECT product_pk FROM products WHERE uniqueId = ?";
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

                updateProductQuantity(); // Actualizar la cantidad del producto en inventario
                generatePDF(clientID); // Crear PDF de la venta
                JOptionPane.showMessageDialog(null, "Factura generada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            dispose();
            new UpdateService(username).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Por favor agrega algún producto al carrito.", "No hay productos seleccionados",
                    JOptionPane.INFORMATION_MESSAGE);
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

                    // Obtener product_pk a partir del uniqueId
                    String productQuery = "SELECT product_pk FROM products WHERE uniqueId = ?";
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

                    // Asociar service_pk con soldProduct_pk en soldProducts_services
                    try (PreparedStatement psSoldProductsServices = con.prepareStatement(insertSoldProductsServicesQuery)) {
                        psSoldProductsServices.setLong(1, servicePk); // service_pk
                        psSoldProductsServices.setLong(2, soldProductPk); // soldProduct_pk
                        psSoldProductsServices.executeUpdate();
                    }
                }

                updateProductQuantity(); // Actualizar la cantidad del producto en inventario
                JOptionPane.showMessageDialog(null, "Servicio guardado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            dispose();
            new UpdateService(username).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Por favor agrega algún producto al carrito.", "No hay productos seleccionados",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnSearchServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchServiceActionPerformed
        //botón para cargar los datos del servicio ingresado
        String serviceId = txtServiceID.getText().trim();

        if (serviceId.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese un ID de servicio.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String queryService = "SELECT motorbike_pk, totalPrice FROM services WHERE service_pk = ?";
        String querySoldProducts = "SELECT p.product_pk, p.description, p.productBrand, sp.salePrice, sp.quantity "
                + "FROM soldProducts_services sps "
                + "JOIN soldProducts sp ON sps.soldProduct_pk = sp.soldProduct_pk "
                + "JOIN products p ON sp.product_pk = p.product_pk "
                + "WHERE sps.service_pk = ?";

        try (Connection con = ConnectionProvider.getCon()) {
            // Consultar datos del servicio
            try (PreparedStatement psService = con.prepareStatement(queryService)) {
                psService.setString(1, serviceId);

                try (ResultSet rsService = psService.executeQuery()) {
                    if (rsService.next()) {
                        String motorbikePk = rsService.getString("motorbike_pk");
                        long totalPrice = rsService.getLong("totalPrice");

                        // Cargar datos en los campos
                        txtPlate.setText(motorbikePk);
                        lblFinalTotalPrice.setText(String.valueOf(totalPrice));
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontró el servicio con el ID ingresado.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            }

            // Consultar productos asociados al servicio
            try (PreparedStatement psProducts = con.prepareStatement(querySoldProducts)) {
                psProducts.setString(1, serviceId);

                try (ResultSet rsProducts = psProducts.executeQuery()) {
                    DefaultTableModel model = (DefaultTableModel) cartTable.getModel();
                    model.setRowCount(0); // Limpiar la tabla antes de cargar los datos

                    while (rsProducts.next()) {
                        long productPk = rsProducts.getLong("product_pk");
                        String description = rsProducts.getString("description");
                        String productBrand = rsProducts.getString("productBrand");
                        long salePrice = rsProducts.getLong("salePrice");
                        int quantity = rsProducts.getInt("quantity");
                        long totalPrice = salePrice * quantity; // Calcular precio total por producto

                        // Agregar filas a la tabla
                        model.addRow(new Object[]{productPk, description, productBrand, salePrice, quantity, totalPrice});
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al buscar el servicio: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
