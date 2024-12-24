package common;

/**
 *
 * @author HOME
 */
public class Validations {

    public static final String emailPattern = "^[a-zA-Z0-9]+[@]+[a-zA-Z0-9]+[.]+[a-zA-Z0-9]+$";
    public static final String numberPattern = "^[0-9]*$";
    public static final String justLetters = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$";

    //método para comprobar si un campo es null o está vacío
    public static boolean isNullOrBlank(String str) {
        return str == null || str.isBlank();
    }

}
