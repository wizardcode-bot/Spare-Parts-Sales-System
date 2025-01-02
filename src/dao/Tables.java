package dao;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.Statement;

public class Tables {

    public static void main(String[] args) {
        try (Connection con = ConnectionProvider.getCon(); Statement st = con.createStatement()) {

            String createAppUserTable = "CREATE TABLE IF NOT EXISTS appuser ("
                    + "appuser_pk INT AUTO_INCREMENT PRIMARY KEY, "
                    + "userRole VARCHAR(50), "
                    + "name VARCHAR(200), "
                    + "dob VARCHAR(50), "
                    + "mobileNumber VARCHAR(100), "
                    + "IDcard VARCHAR(100), "
                    + "username VARCHAR(200), "
                    + "password VARCHAR(255), "
                    + "address VARCHAR(200))";

            String insertAdmin = "INSERT INTO appuser (userRole, name, dob, mobileNumber, IDcard, username, password, address) "
                    + "VALUES ('Admin', 'Admin', '16-12-1992', '0000111122', '1006465848', 'admin', 'admin', 'Colombia')";

            String createProductsTable = "CREATE TABLE IF NOT EXISTS products ("
                    + "product_pk BIGINT AUTO_INCREMENT PRIMARY KEY, "
                    + "uniqueId VARCHAR(255) NOT NULL, "
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
                    + "paymentTerm varchar(50), " //contado o crédito
                    + "client_pk BIGINT," //llave primaria de el cliente que hizo la compra
                    + "appuser_pk INT, " //llave primaria de el usuario(username) que hizo la venta
                    + "FOREIGN KEY (appuser_pk) REFERENCES appuser(appuser_pk))";

            String createClientsTable = "CREATE TABLE IF NOT EXISTS clients ("
                    + "client_pk BIGINT AUTO_INCREMENT PRIMARY KEY, "
                    + "name VARCHAR(200) NOT NULL, "
                    + "mobileNumber VARCHAR(100), "
                    + "address VARCHAR(200), "
                    + "email VARCHAR(200), "
                    + "idCard VARCHAR(100) NOT NULL)";

            String createMotorbikesTable = "CREATE TABLE IF NOT EXISTS motorbikes ("
                    + "motorbike_pk BIGINT AUTO_INCREMENT PRIMARY KEY, " 
                    + "plate VARCHAR(50) NOT NULL, "
                    + "brandName VARCHAR(100) NOT NULL, "
                    + "model VARCHAR(100), "
                    + "cylinderCapacity VARCHAR(100), "
                    + "color VARCHAR(50) NOT NULL, "
                    + "client_pk BIGINT, "
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
                    + "product_pk BIGINT,"
                    + "FOREIGN KEY (product_pk) REFERENCES products(product_pk))";

            String createProductsBills = "CREATE TABLE IF NOT EXISTS products_bills("
                    + "productsBills_pk BIGINT AUTO_INCREMENT PRIMARY KEY,"
                    + "bill_pk BIGINT,"
                    + "soldProduct_pk BIGINT,"
                    + "FOREIGN KEY (bill_pk) REFERENCES bills(bill_pk),"
                    + "FOREIGN KEY (soldProduct_pk) REFERENCES SoldProducts(soldProduct_pk))";

            // Ejecutar las consultas
            //st.executeUpdate(createAppUserTable);
            //st.executeUpdate(insertAdmin);
            //st.executeUpdate(createProductsTable);
            //st.executeUpdate(createBillsTable);
            //st.executeUpdate(createClientsTable);
            //st.executeUpdate(createMotorbikesTable);
            //st.executeUpdate(createCategories);
            //st.executeUpdate(createSoldProducts);
            //st.executeUpdate(createProductsBills);
            JOptionPane.showMessageDialog(null, "Table created successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }
}
