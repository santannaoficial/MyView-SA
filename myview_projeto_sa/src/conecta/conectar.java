package conecta;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Matheus Amorim da Cruz
 */

/**
 * Classe criada para conectar o Netbeans ao MySql.
 */

public class conectar {
    public static Connection conector(){
        java.sql.Connection conectar = null;
        
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/db_sa_myview_pedrosmatheusc";
        
        String user = "root";
        String password = "root";
        
        try{
            Class.forName(driver);
            conectar = DriverManager.getConnection(url,user,password);
            return conectar;
            
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }   
        
    
}
