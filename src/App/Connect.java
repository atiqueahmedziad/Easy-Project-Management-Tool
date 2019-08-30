package App;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connect {

    public Connection getConnection() {

        // Database name, username and password
        String dbaseName = "epmtdb";
        String user = "sa";
        String pass = "ziad07rock";
        Connection connect = null;

        try {
            // Java Driver for connecting MySQL Database
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            // port number is 3306
            connect = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;" +
                    "databaseName="+dbaseName+";user="+user+";password="+pass+";");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connect;
    }
}
