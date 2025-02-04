package common;

import dao.ConnectionProvider;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author HOME
 */
public class Validations {

    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9]+[@]+[a-zA-Z0-9]+[.]+[a-zA-Z0-9]+$";
    public static final String NUMBER_PATTERN = "^[0-9]*$";
    public static final String JUST_LETTERS = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$";
    public static final String ADMIN_ID = "1006798617";
    public static final String SUPPORT_ID = "1006798616";

    //método para comprobar si un campo es null o está vacío
    public static boolean isNullOrBlank(String str) {
        return str == null || str.isBlank();
    }

    //método para consultar el nombre según username
    public static String getNameByUsername(String username) {
        String name = "";
        String query = "SELECT name FROM appusers WHERE username = ?";

        try (Connection con = ConnectionProvider.getCon(); PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                name = rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return name;
    }

    //método para consultar el appuser_pk según username
    public static String getIDByUsername(String username) {
        String ID = "";
        String query = "SELECT appuser_pk FROM appusers WHERE username = ?";

        try (Connection con = ConnectionProvider.getCon(); PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ID = rs.getString("appuser_pk");
            } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el usuario " + username, "Error", 
                            JOptionPane.ERROR_MESSAGE);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ID;
    }

}
