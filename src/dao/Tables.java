package dao;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.Statement;

public class Tables {

    public static void main(String[] args) {
        try (Connection con = ConnectionProvider.getCon(); Statement st = con.createStatement()) {

            String createAppUserTable = "CREATE TABLE IF NOT EXISTS appusers ("
                    + "appuser_pk BIGINT PRIMARY KEY, " //cédula
                    + "userRole VARCHAR(50) NOT NULL, "
                    + "name VARCHAR(200) NOT NULL, "
                    + "dateOfBirth VARCHAR(50) NOT NULL, "
                    + "mobileNumber VARCHAR(15) NOT NULL, "
                    + "username VARCHAR(50) NOT NULL UNIQUE, "
                    + "password VARCHAR(255) NOT NULL, "
                    + "address VARCHAR(100) NOT NULL)";

            String insertAdmin = "INSERT INTO appuser (userRole, name, dob, mobileNumber, IDcard, username, password, address) "
                    + "VALUES ('Admin', 'Admin', '16-12-1992', '0000111122', '1006465848', 'admin', 'admin', 'Colombia')";

            String createProductsTable = "CREATE TABLE IF NOT EXISTS products ("
                    + "product_pk VARCHAR(255) PRIMARY KEY, "
                    + "category_pk INT NOT NULL, "
                    + "description VARCHAR(200) NOT NULL, "
                    + "productBrand VARCHAR(200), "
                    + "quantity BIGINT NOT NULL, "
                    + "acquiredPrice BIGINT NOT NULL, "
                    + "sellingPrice BIGINT NOT NULL, "
                    + "productLocation VARCHAR(200) NOT NULL, "
                    + "lastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
                    + "FOREIGN KEY (category_pk) REFERENCES productCategories(category_pk))";

            String createBillsTable = "CREATE TABLE IF NOT EXISTS bills ("
                    + "bill_pk BIGINT AUTO_INCREMENT PRIMARY KEY, "
                    + "billId VARCHAR(200) NOT NULL, "
                    + "billDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "totalPaid BIGINT NOT NULL, "
                    + "paymentTerm varchar(50) NOT NULL, " //contado o crédito
                    + "cashPaid BIGINT DEFAULT 0, " //pago en efectivo
                    + "transferPaid BIGINT DEFAULT 0," //pago por transferencia
                    + "client_pk VARCHAR(20)," //llave primaria de el cliente que hizo la compra
                    + "appuser_pk BIGINT, " //llave primaria de el usuario(vendedor) que hizo la venta
                    + "FOREIGN KEY (client_pk) REFERENCES clients(client_pk) ON DELETE CASCADE ON UPDATE RESTRICT,"
                    + "FOREIGN KEY (appuser_pk) REFERENCES appusers(appuser_pk) ON DELETE CASCADE ON UPDATE RESTRICT)";

            String createClientsTable = "CREATE TABLE IF NOT EXISTS clients ("
                    + "client_pk VARCHAR(20) PRIMARY KEY, " //Cédula o NIT
                    + "name VARCHAR(200) NOT NULL, "
                    + "mobileNumber VARCHAR(15) NOT NULL, "
                    + "address VARCHAR(100) NOT NULL, "
                    + "email VARCHAR(200) NOT NULL)";

            String createMotorbikesTable = "CREATE TABLE IF NOT EXISTS motorbikes ("
                    + "motorbike_pk VARCHAR(10) PRIMARY KEY, " //placa
                    + "brandName VARCHAR(100) NOT NULL, "
                    + "model VARCHAR(100) NOT NULL, "
                    + "cylinderCapacity VARCHAR(100) NOT NULL, "
                    + "color VARCHAR(50) NOT NULL, "
                    + "client_pk varchar(20), "
                    + "FOREIGN KEY (client_pk) REFERENCES clients(client_pk) "
                    + "ON DELETE SET NULL ON UPDATE RESTRICT)";

            String createCategories = "CREATE TABLE IF NOT EXISTS productCategories("
                    + "category_pk INT AUTO_INCREMENT PRIMARY KEY,"
                    + "categoryName VARCHAR(100) NOT NULL)";

            String createSoldProducts = "CREATE TABLE IF NOT EXISTS SoldProducts("
                    + "soldProduct_pk BIGINT AUTO_INCREMENT PRIMARY KEY,"
                    + "quantity int NOT NULL,"
                    + "salePrice BIGINT NOT NULL,"
                    + "saleDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                    + "product_pk VARCHAR(255),"
                    + "FOREIGN KEY (product_pk) REFERENCES products(product_pk) ON DELETE CASCADE ON UPDATE RESTRICT)";

            String createProductsBills = "CREATE TABLE IF NOT EXISTS products_bills("
                    + "productsBills_pk BIGINT AUTO_INCREMENT PRIMARY KEY,"
                    + "bill_pk BIGINT,"
                    + "soldProduct_pk BIGINT,"
                    + "FOREIGN KEY (bill_pk) REFERENCES bills(bill_pk) ON DELETE CASCADE ON UPDATE RESTRICT,"
                    + "FOREIGN KEY (soldProduct_pk) REFERENCES SoldProducts(soldProduct_pk) ON DELETE CASCADE ON UPDATE RESTRICT)";
            
            String createServices = "CREATE TABLE IF NOT EXISTS services("
                    + "service_pk BIGINT AUTO_INCREMENT PRIMARY KEY,"
                    + "motorbike_pk VARCHAR(10),"
                    + "state VARCHAR(50) NOT NULL,"
                    + "totalPrice BIGINT NOT NULL,"
                    + "lastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
                    + "FOREIGN KEY (motorbike_pk) REFERENCES motorbikes(motorbike_pk) ON DELETE CASCADE ON UPDATE RESTRICT)";
            
            String createSoldProductsServices = "CREATE TABLE IF NOT EXISTS soldProducts_services("
                    + "soldProductsServices_pk BIGINT AUTO_INCREMENT PRIMARY KEY,"
                    + "service_pk BIGINT,"
                    + "soldProduct_pk BIGINT,"
                    + "FOREIGN KEY (service_pk) REFERENCES services(service_pk) ON DELETE CASCADE,"
                    + "FOREIGN KEY (soldProduct_pk) REFERENCES SoldProducts(soldProduct_pk) ON DELETE CASCADE)";
            
            String createInventoryAdjustments = "CREATE TABLE IF NOT EXISTS inventory_adjustments("
                    + "inventory_pk BIGINT AUTO_INCREMENT PRIMARY KEY,"
                    + "typeOfAdjustment varchar(50) NOT NULL,"
                    + "previousQuantity BIGINT NOT NULL,"
                    + "newQuantity BIGINT NOT NULL,"
                    + "adjustmentMotive VARCHAR(255),"
                    + "moneyReceived BIGINT DEFAULT 0,"
                    + "moneyPaid BIGINT DEFAULT 0,"
                    + "lastModified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
                    + "product_pk VARCHAR(255),"
                    + "FOREIGN KEY (product_pk) REFERENCES products(product_pk) ON DELETE CASCADE)";
            
            String createExpenses = "CREATE TABLE IF NOT EXISTS expenses("
                    + "expense_pk BIGINT AUTO_INCREMENT PRIMARY KEY,"
                    + "description VARCHAR(255) NOT NULL,"
                    + "expenseValue BIGINT NOT NULL,"
                    + "expenseDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                    + "expenseResponsible VARCHAR(200) NOT NULL)";

            // EJECUTAR LAS CONSULTAS
            
            //st.executeUpdate(createAppUserTable);
            //st.executeUpdate(insertAdmin);
            //st.executeUpdate(createProductsTable);
            //st.executeUpdate(createBillsTable);
            //st.executeUpdate(createClientsTable);
            //st.executeUpdate(createMotorbikesTable);
            //st.executeUpdate(createCategories);
            //st.executeUpdate(createSoldProducts);
            //st.executeUpdate(createProductsBills);
            //st.executeUpdate(createServices);
            //st.executeUpdate(createSoldProductsServices);
            //st.executeUpdate(createInventoryAdjustments);
            //st.executeUpdate(createExpenses);
            
            JOptionPane.showMessageDialog(null, "Table created successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }
}
