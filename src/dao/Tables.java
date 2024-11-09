
package dao;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.Statement;

public class Tables {
    public static void main(String[] args) {
        try{
            Connection con = ConnectionProvider.getCon();   
            Statement st = con.createStatement();
            //st.executeUpdate("create table appuser(appuser_pk int AUTO_INCREMENT primary key, userRole varchar (200),name varchar(200), dob varchar(50),mobileNumber varchar(200), email varchar(200), username varchar(200), password varchar(50), address varchar(200))");
            //st.executeUpdate("insert into appuser(userRole, name, dob, mobileNumber, email, username, password, address) values('Admin','Admin','16-12-1992','0000111122','admin@gmail.com','admin','admin','Colombia')"); 
            //st.executeUpdate("create table products(product_pk int AUTO_INCREMENT primary key, uniqueId varchar(200), name varchar(200), companyName varchar(200), quantity bigint, price bigint)");
            //st.executeUpdate("create table bills(bill_pk int AUTO_INCREMENT primary key, billId varchar(200), billDate varchar(50), totalPaid bigint, generatedBy varchar(50), relatedClient varchar(200))");
            //st.executeUpdate("create table clients(client_pk int AUTO_INCREMENT primary key, name varchar(200), mobileNumber varchar(200), address varchar(200), email varchar(200), nickname varchar(200), vehicleInfo varchar(200))");
            JOptionPane.showMessageDialog(null, "Table created succesfully");        
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
}
