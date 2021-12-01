package tc.web.evaluaciones.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDB{
    public ConnectionDB(){
        
    }

    public static Connection createConnection(){
        Connection connection = null;
        try{
            connection = DriverManager.getConnection("jdbc:mariadb://localhost/evaluaciones", "root", null);
        }catch(Exception exception){

        }
        return connection;
    }
}
