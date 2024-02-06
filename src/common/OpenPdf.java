
package common;

import javax.swing.JOptionPane;
import java.io.File;
import dao.ProductsUtils;

public class OpenPdf {
    public static void openById(String id){
        try{
            if((new File(ProductsUtils.billPath+id+".pdf")).exists()){
                Process p = Runtime
                        .getRuntime()
                        .exec("rundll32 url.dll,FileProtocolHandler "+ProductsUtils.billPath+""+id+".pdf");
            }
            else{
                JOptionPane.showMessageDialog(null, "El archivo no existe.");
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
}
