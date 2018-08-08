package App;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connect {
    public Connection getConnection() {

        String dbaseName = "gantt-chart";
        String user = "root";
        String pass = "root";

        Connection connect = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

            connect = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/" + dbaseName, user, pass);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return connect;
    }
}
