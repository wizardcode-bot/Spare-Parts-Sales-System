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
import javax.imageio.ImageIO;
import ui.help.SellProductHelp;

public class SellProduct extends javax.swing.JFrame {

    private long finalTotalPrice = 0;
    private String billId = "";
    private String username = "";
    private long cashPaidInt = 0;
    private long transferPaidInt = 0;
    private long servicePriceInt = 0;
    private boolean servicePriceAdded = false;

    /**
     * Creates new form SellProduct
     */
    public SellProduct() {
        initComponents();
    }

    public SellProduct(String tempUsername) {
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

                    txtTransferPaid.setEnabled(true);
                    txtCashPaid.setEnabled(true);
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

                    txtTransferPaid.setEnabled(false);
                    txtCashPaid.setEnabled(false);
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

    private void productName(String nameOrUniqueId) {
        //MOSTRAR CÓDIGO Y DESCRIPCIÓN DE CADA PRODUCTO EN LA TABLA 
        DefaultTableModel model = (DefaultTableModel) productsTable.getModel();
        model.setRowCount(0);
        String query = "SELECT * FROM products WHERE description LIKE ? OR product_pk LIKE ?";
        try (Connection con = ConnectionProvider.getCon(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, "%" + nameOrUniqueId + "%");
            pst.setString(2, "%" + nameOrUniqueId + "%");
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
        txtProductLocation.setText("");
        txtStockQuantity.setText("");
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

    private void generatePDF(Timestamp paymentDate) {
        // Configurar el tamaño de la factura para 58mm
        com.itextpdf.text.Document doc = new com.itextpdf.text.Document(new Rectangle(226, PageSize.A4.getHeight()));
        doc.setMargins(10, 5, 10, 10); // Márgenes 

        String selectedClient = comboRelateClient.getSelectedItem().toString();
        String idCard = getClientIdCard(selectedClient);
        String paymentTerm = comboPayment.getSelectedItem().toString();
        final String NIT = "119887284-4";
        final String ADDRESS = "Cra 18 # 11 - 62 Centro - Cumaral";
        String vendorName = Validations.getNameByUsername(username);

        try {
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
                    + "\nCliente: " + selectedClient
                    + "\nNIT/CC: " + (idCard != null ? idCard : "No disponible") + "\n", normalFont);
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

            // Sección "DETALLE DE PAGO"
            Paragraph paymentTitle = new Paragraph("DETALLE DE PAGO\n", boldFont);
            paymentTitle.setAlignment(Element.ALIGN_CENTER);
            doc.add(paymentTitle);

            Paragraph total = new Paragraph("Total: " + (finalTotalPrice) + "\n", boldFont);
            doc.add(total);

            Paragraph paymentDetails = new Paragraph("Servicio: " + servicePriceInt
                    + "\nForma de pago: " + paymentTerm
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

            // Preguntar si se desea imprimir
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
        comboRelateClient = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        txtFilterClient = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtUniqueId = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        cashTextLabel = new javax.swing.JLabel();
        transferTextLabel = new javax.swing.JLabel();
        txtTransferPaid = new javax.swing.JTextField();
        txtCashPaid = new javax.swing.JTextField();
        txtDescription = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        deadlineLabel = new javax.swing.JLabel();
        deadlineDate = new com.toedter.calendar.JDateChooser();
        comboPayment = new javax.swing.JComboBox<>();
        billPaidLabel = new javax.swing.JLabel();
        txtBillPaid = new javax.swing.JTextField();
        surplusTextLabel = new javax.swing.JLabel();
        surplusMoneyLabel = new javax.swing.JLabel();
        calculateSurplusBtn = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        txtProductLocation = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtStockQuantity = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtServicePrice = new javax.swing.JTextField();
        btnServicePrice = new javax.swing.JButton();
        btnResetSP = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Vender Producto");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(546, 6, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Buscar producto por código o descripción");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 87, -1, -1));

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
        getContentPane().add(txtSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 107, 370, -1));

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

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 139, 370, 454));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Código del Producto");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 91, -1, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Descripción");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 150, -1, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Marca");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 206, -1, -1));

        txtProductBrand.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtProductBrand, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 228, 306, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Precio por Unidad");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(813, 208, -1, -1));

        txtPricePerUnit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtPricePerUnit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPricePerUnitKeyReleased(evt);
            }
        });
        getContentPane().add(txtPricePerUnit, new org.netbeans.lib.awtextra.AbsoluteConstraints(813, 229, 220, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Número de Unidades a vender *");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(813, 264, -1, -1));

        txtNoOfUnits.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtNoOfUnits.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNoOfUnitsKeyReleased(evt);
            }
        });
        getContentPane().add(txtNoOfUnits, new org.netbeans.lib.awtextra.AbsoluteConstraints(813, 285, 220, -1));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Precio Total");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(813, 320, -1, -1));

        txtTotalPrice.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtTotalPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(813, 341, 220, -1));

        btnAddToCart.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAddToCart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/addToCart.png"))); // NOI18N
        btnAddToCart.setText("Añadir al carrito");
        btnAddToCart.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddToCart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddToCartActionPerformed(evt);
            }
        });
        getContentPane().add(btnAddToCart, new org.netbeans.lib.awtextra.AbsoluteConstraints(894, 378, -1, -1));

        cartTable.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cartTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código del Producto", "Descripción", "Marca", "Precio Por Unidad", "N° de Unidades", "Precio Total"
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

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 484, 845, 233));

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Precio total:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(134, 605, -1, -1));

        lblFinalTotalPrice.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblFinalTotalPrice.setForeground(new java.awt.Color(255, 255, 255));
        lblFinalTotalPrice.setText("0");
        getContentPane().add(lblFinalTotalPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(294, 605, -1, -1));

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print.png"))); // NOI18N
        jButton3.setText("Finalizar venta e imprimir");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(122, 649, -1, -1));

        comboRelateClient.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        comboRelateClient.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione un cliente" }));
        comboRelateClient.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getContentPane().add(comboRelateClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 284, 306, -1));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Filtrar por número de cédula");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 321, -1, -1));

        txtFilterClient.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtFilterClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 344, 200, -1));

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/filter.png"))); // NOI18N
        jButton2.setText("Filtrar");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 341, 100, -1));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Relacionar Cliente *");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 263, -1, -1));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Seleccione en la tabla el producto a eliminar ");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(753, 729, -1, -1));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jLabel14.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel14MouseReleased(evt);
            }
        });
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(1320, 6, -1, -1));

        txtUniqueId.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtUniqueId, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 112, 306, -1));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/addIcon.png"))); // NOI18N
        jButton1.setText("Añadir cliente");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 383, -1, -1));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Forma y medio de pago *");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(1073, 91, -1, -1));

        cashTextLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cashTextLabel.setForeground(new java.awt.Color(255, 255, 255));
        cashTextLabel.setText("Efectivo ($) *");
        getContentPane().add(cashTextLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1073, 149, -1, -1));

        transferTextLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        transferTextLabel.setForeground(new java.awt.Color(255, 255, 255));
        transferTextLabel.setText("Transferencia ($) *");
        getContentPane().add(transferTextLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1073, 205, -1, -1));

        txtTransferPaid.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtTransferPaid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTransferPaidKeyReleased(evt);
            }
        });
        getContentPane().add(txtTransferPaid, new org.netbeans.lib.awtextra.AbsoluteConstraints(1073, 227, 236, -1));

        txtCashPaid.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtCashPaid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCashPaidKeyReleased(evt);
            }
        });
        getContentPane().add(txtCashPaid, new org.netbeans.lib.awtextra.AbsoluteConstraints(1073, 170, 236, -1));

        txtDescription.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 171, 306, -1));

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/help.png"))); // NOI18N
        jLabel18.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel18MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(1284, 14, -1, -1));
        getContentPane().add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 59, 1366, 10));

        deadlineLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        deadlineLabel.setForeground(new java.awt.Color(255, 255, 255));
        deadlineLabel.setText("Fecha límite de pago *");
        getContentPane().add(deadlineLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1073, 262, -1, -1));
        getContentPane().add(deadlineDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(1073, 283, 236, -1));

        comboPayment.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        comboPayment.setForeground(new java.awt.Color(255, 255, 255));
        comboPayment.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Contado", "Crédito" }));
        comboPayment.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getContentPane().add(comboPayment, new org.netbeans.lib.awtextra.AbsoluteConstraints(1073, 112, 180, -1));

        billPaidLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        billPaidLabel.setForeground(new java.awt.Color(255, 255, 255));
        billPaidLabel.setText("Paga con ($)");
        getContentPane().add(billPaidLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1073, 319, -1, -1));

        txtBillPaid.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtBillPaid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBillPaidKeyReleased(evt);
            }
        });
        getContentPane().add(txtBillPaid, new org.netbeans.lib.awtextra.AbsoluteConstraints(1073, 341, 160, -1));

        surplusTextLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        surplusTextLabel.setForeground(new java.awt.Color(255, 255, 255));
        surplusTextLabel.setText("Excedente");
        getContentPane().add(surplusTextLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1073, 378, -1, -1));

        surplusMoneyLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        surplusMoneyLabel.setForeground(new java.awt.Color(51, 204, 255));
        surplusMoneyLabel.setText("$ 0");
        getContentPane().add(surplusMoneyLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1073, 399, -1, -1));

        calculateSurplusBtn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        calculateSurplusBtn.setText("Calcular");
        calculateSurplusBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        calculateSurplusBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateSurplusBtnActionPerformed(evt);
            }
        });
        getContentPane().add(calculateSurplusBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(1239, 341, 80, -1));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Ubicación en almacén");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(813, 87, -1, -1));

        txtProductLocation.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtProductLocation, new org.netbeans.lib.awtextra.AbsoluteConstraints(813, 112, 220, -1));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Cantidad en stock");
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(813, 151, -1, -1));

        txtStockQuantity.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        getContentPane().add(txtStockQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(813, 171, 220, -1));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Servicio ($)");
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 419, -1, -1));

        txtServicePrice.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtServicePrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtServicePriceKeyReleased(evt);
            }
        });
        getContentPane().add(txtServicePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 442, 152, -1));

        btnServicePrice.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnServicePrice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/addIcon.png"))); // NOI18N
        btnServicePrice.setText("Añadir");
        btnServicePrice.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnServicePrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnServicePriceActionPerformed(evt);
            }
        });
        getContentPane().add(btnServicePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 439, 100, -1));

        btnResetSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/reset.png"))); // NOI18N
        btnResetSP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnResetSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetSPActionPerformed(evt);
            }
        });
        getContentPane().add(btnResetSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(622, 439, 42, -1));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/adminDashboardBackground.png"))); // NOI18N
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        productName("");
        cargarClientes();
        txtUniqueId.setEditable(false);
        txtDescription.setEditable(false);
        txtProductBrand.setEditable(false);
        txtPricePerUnit.setEditable(false);
        txtTotalPrice.setEditable(false);
        txtProductLocation.setEditable(false);
        txtStockQuantity.setEditable(false);
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
        // MOSTRAR LOS DATOS DEL PRODUCTO AL SELECCIONARLO EN LA TABLA
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
                    txtProductLocation.setText(rs.getString("productLocation"));
                    txtStockQuantity.setText(rs.getString("quantity"));
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
                String query = "SELECT * FROM products WHERE product_pk = ?";
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

        String selectedClient = comboRelateClient.getSelectedItem().toString();
        String clientID = getClientIdCard(selectedClient);
        String paymentTerm = comboPayment.getSelectedItem().toString(); // Forma de pago
        String cashPaid = null;
        String transferPaid = null;
        String servicePrice = txtServicePrice.getText().trim();

        if (cartTable.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "El carrito está vacío. Agrega productos antes de continuar.",
                    "Carrito vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if ("Contado".equals(paymentTerm)) {
            cashPaid = txtCashPaid.getText().trim();
            transferPaid = txtTransferPaid.getText().trim();

            if (Validations.isNullOrBlank(cashPaid) || Validations.isNullOrBlank(transferPaid)) {
                JOptionPane.showMessageDialog(null, "Debes completar el medio de pago (Efectivo y Transferencia)",
                        "Datos incompletos", JOptionPane.WARNING_MESSAGE);
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
                JOptionPane.showMessageDialog(null, "Debes ingresar los valores en números",
                        "Datos incorrectos", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else if ("Crédito".equals(paymentTerm)) {
            cashPaid = "0";
            transferPaid = "0";

            try {
                cashPaidInt = Integer.parseInt(cashPaid);
                transferPaidInt = Integer.parseInt(transferPaid);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Valor de transferencia y efectivo no válidos.");
            }
        }

        if (Validations.isNullOrBlank(servicePrice)) {
            servicePriceInt = 0;
        } else {
            try {
                servicePriceInt = Integer.parseInt(servicePrice);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Debes ingresar el valor del servicio en numeros, sin puntos, comas o algún otro caracter",
                        "Datos incorrectos", JOptionPane.WARNING_MESSAGE);
                return;
            }
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
                            billPk = generatedKeys.getInt(1);
                        } else {
                            throw new Exception("Error al obtener el ID de la factura.");
                        }
                    }
                }

                // Variable para almacenar la fecha de pago
                Timestamp paymentDate;

                // Insertar crédito si el pago es "Crédito"
                if ("Crédito".equals(paymentTerm)) {
                    java.util.Date utilDate = deadlineDate.getDate(); // Obtener fecha del JDateChooser
                    if (utilDate != null) {
                        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                        paymentDate = new Timestamp(sqlDate.getTime()); // Fecha límite de pago
                    } else {
                        throw new Exception("Debes seleccionar una fecha de vencimiento para el crédito.");
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

                // Insertar productos en SoldProducts y asociarlos con la factura en products_bills
                String insertSoldProductQuery = "INSERT INTO SoldProducts (quantity, salePrice, saleDate, product_pk) VALUES (?, ?, ?, ?)";
                String insertProductsBillsQuery = "INSERT INTO products_bills (bill_pk, soldProduct_pk) VALUES (?, ?)";

                DefaultTableModel dtm = (DefaultTableModel) cartTable.getModel();
                for (int i = 0; i < dtm.getRowCount(); i++) {
                    String productPK = dtm.getValueAt(i, 0).toString();
                    int quantity = Integer.parseInt(dtm.getValueAt(i, 4).toString());
                    long salePrice = Long.parseLong(dtm.getValueAt(i, 3).toString());

                    long soldProductPk;
                    try (PreparedStatement psSoldProduct = con.prepareStatement(insertSoldProductQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        psSoldProduct.setInt(1, quantity);
                        psSoldProduct.setLong(2, salePrice);
                        psSoldProduct.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                        psSoldProduct.setString(4, productPK);
                        psSoldProduct.executeUpdate();

                        try (ResultSet generatedKeys = psSoldProduct.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                soldProductPk = generatedKeys.getLong(1);
                            } else {
                                throw new Exception("Error al obtener el ID del producto vendido.");
                            }
                        }
                    }

                    try (PreparedStatement psProductsBills = con.prepareStatement(insertProductsBillsQuery)) {
                        psProductsBills.setLong(1, billPk);
                        psProductsBills.setLong(2, soldProductPk);
                        psProductsBills.executeUpdate();
                    }
                }

                updateProductQuantity(); // Actualizar inventario

                // Llamar a generatePDF con la fecha de pago calculada
                generatePDF(paymentDate);
                dispose();
                new SellProduct(username).setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor agrega algún producto al carrito.", "No hay productos seleccionados",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // filtrar clientes por número de cédula
        boolean checkClientExist = false;
        String filterClient = txtFilterClient.getText().trim();
        if (Validations.isNullOrBlank(filterClient)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el número de cédula del cliente!", "No hay clientes seleccionados",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            String query = "SELECT name FROM clients WHERE client_pk = ?";
            try (Connection con = ConnectionProvider.getCon(); PreparedStatement pst = con.prepareStatement(query)) {

                pst.setString(1, filterClient); // Parámetro seguro para evitar inyección SQL
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        checkClientExist = true;
                        comboRelateClient.setSelectedItem(rs.getString("name"));
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            if (!checkClientExist) {
                JOptionPane.showMessageDialog(null, "¡Este cliente no está registrado!", "Error", JOptionPane.ERROR_MESSAGE);
                comboRelateClient.setSelectedItem("Seleccione un cliente");
            }
        }
        txtFilterClient.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jLabel14MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseReleased
        dispose();
    }//GEN-LAST:event_jLabel14MouseReleased

    private void txtPricePerUnitKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPricePerUnitKeyReleased
        String pricePerUnit = txtPricePerUnit.getText();

        if (!pricePerUnit.matches(Validations.NUMBER_PATTERN)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el precio del producto en números!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtPricePerUnit.setText("");
            return;
        }
    }//GEN-LAST:event_txtPricePerUnitKeyReleased

    private void txtTransferPaidKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTransferPaidKeyReleased
        String cashPaid = txtTransferPaid.getText();

        if (!cashPaid.matches(Validations.NUMBER_PATTERN)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el valor en números!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtTransferPaid.setText("");
            return;
        }
    }//GEN-LAST:event_txtTransferPaidKeyReleased

    private void txtCashPaidKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCashPaidKeyReleased
        String transferPaid = txtCashPaid.getText();

        if (!transferPaid.matches(Validations.NUMBER_PATTERN)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el valor en números!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtCashPaid.setText("");
            return;
        }
    }//GEN-LAST:event_txtCashPaidKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        AddClient addClientWindow = new AddClient();
        addClientWindow.setVisible(true);

        // Agregar un WindowListener para detectar cuándo se cierra la ventana
        addClientWindow.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                cargarClientes(); // Recargar los clientes al cerrar la ventana AddClient
            }
        });
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jLabel18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseClicked
        new SellProductHelp().setVisible(true);
    }//GEN-LAST:event_jLabel18MouseClicked

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
        String cashPaid = txtCashPaid.getText().trim();

        if (Validations.isNullOrBlank(cashPaid)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar primero el valor pagado en efectivo!",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            txtBillPaid.setText("");
            return;
        }

        // Calcular el excedente a entregar
        try {
            int cashPaidInt = Integer.parseInt(cashPaid);
            int billPaidInt = Integer.parseInt(billPaid);

            int surplusMoney = (billPaidInt - cashPaidInt);

            // Mostrar el resultado en el label
            surplusMoneyLabel.setText(String.format("%,d", surplusMoney));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_calculateSurplusBtnActionPerformed

    private void txtServicePriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtServicePriceKeyReleased
        // Ingresar precio de servicio en números
        String servicePrice = txtServicePrice.getText().trim();

        if (!servicePrice.matches(Validations.NUMBER_PATTERN)) {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar el valor en números!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtServicePrice.setText("");
            return;
        }
    }//GEN-LAST:event_txtServicePriceKeyReleased

    private void btnServicePriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnServicePriceActionPerformed
        // Botón añadir precio de servicio
        String servicePrice = txtServicePrice.getText().trim();

        if (Validations.isNullOrBlank(servicePrice)) {
            JOptionPane.showMessageDialog(null, "¡Debe ingresar primero el precio del servicio!", "Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int servicePaidInt = 0;

        try {
            servicePaidInt = Integer.parseInt(servicePrice);

            finalTotalPrice += servicePaidInt;

            lblFinalTotalPrice.setText(String.valueOf(finalTotalPrice));
            // Desactivar botón y campo de texto al sumar el precio del servicio, activar bandera
            btnServicePrice.setEnabled(false);
            txtServicePrice.setEnabled(false);
            servicePriceAdded = true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnServicePriceActionPerformed

    private void btnResetSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetSPActionPerformed
        // Botón para restar el precio del servicio y activar botones
        String servicePrice = txtServicePrice.getText().trim();
        
        if (Validations.isNullOrBlank(servicePrice) || !servicePriceAdded) {
            JOptionPane.showMessageDialog(null, "¡Debe añadir primero el precio del servicio!", "Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int servicePaidInt = 0;

        try {
            servicePaidInt = Integer.parseInt(servicePrice);

            finalTotalPrice -= servicePaidInt;

            lblFinalTotalPrice.setText(String.valueOf(finalTotalPrice));
            // Activar botón y campo de texto al restar el precio del servicio, desactivar bandera
            btnServicePrice.setEnabled(true);
            txtServicePrice.setEnabled(true);
            txtServicePrice.setText("");
            servicePriceAdded = false;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnResetSPActionPerformed

    /* Método para cargar los clientes en el jComboBox */
    private void cargarClientes() {
        String query = "SELECT * FROM clients";
        String targetIdCard = "222222222222";

        try (Connection con = ConnectionProvider.getCon(); PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            comboRelateClient.removeAllItems(); // Limpiar el comboBox

            int indexToSelect = 0; // Índice del cliente por defecto
            int currentIndex = 0; // Índice actual al recorrer los resultados

            while (rs.next()) {
                String clientName = rs.getString("name");
                String idCard = rs.getString("client_pk");
                comboRelateClient.addItem(clientName);

                // Si el idCard coincide, guardar su índice
                if (targetIdCard.equals(idCard)) {
                    indexToSelect = currentIndex;
                }
                currentIndex++;
            }

            // Establecer como seleccionado el cliente deseado
            comboRelateClient.setSelectedIndex(indexToSelect);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar clientes: " + e.getMessage());
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
            java.util.logging.Logger.getLogger(SellProduct.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SellProduct.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SellProduct.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SellProduct.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SellProduct().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel billPaidLabel;
    private javax.swing.JButton btnAddToCart;
    private javax.swing.JButton btnResetSP;
    private javax.swing.JButton btnServicePrice;
    private javax.swing.JButton calculateSurplusBtn;
    private javax.swing.JTable cartTable;
    private javax.swing.JLabel cashTextLabel;
    private javax.swing.JComboBox<String> comboPayment;
    private javax.swing.JComboBox<String> comboRelateClient;
    private com.toedter.calendar.JDateChooser deadlineDate;
    private javax.swing.JLabel deadlineLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
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
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblFinalTotalPrice;
    private javax.swing.JTable productsTable;
    private javax.swing.JLabel surplusMoneyLabel;
    private javax.swing.JLabel surplusTextLabel;
    private javax.swing.JLabel transferTextLabel;
    private javax.swing.JTextField txtBillPaid;
    private javax.swing.JTextField txtCashPaid;
    private javax.swing.JTextField txtDescription;
    private javax.swing.JTextField txtFilterClient;
    private javax.swing.JTextField txtNoOfUnits;
    private javax.swing.JTextField txtPricePerUnit;
    private javax.swing.JTextField txtProductBrand;
    private javax.swing.JTextField txtProductLocation;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtServicePrice;
    private javax.swing.JTextField txtStockQuantity;
    private javax.swing.JTextField txtTotalPrice;
    private javax.swing.JTextField txtTransferPaid;
    private javax.swing.JTextField txtUniqueId;
    // End of variables declaration//GEN-END:variables
}
