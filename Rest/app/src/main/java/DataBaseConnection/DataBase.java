package DataBaseConnection;

import android.util.Log;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DataBase implements Serializable {

    private Connection conn;
    private Statement statement;
    public static DataBase db;

    public DataBase() {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            this.conn = (Connection) DriverManager.getConnection(
                   MyDBInfo.MSSQL_DATABASE_SERVER, MyDBInfo.MSSQL_USERNAME, MyDBInfo.MSSQL_PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return this.conn;
    }

    public static synchronized DataBase getDBConnection() {
        if(db == null)
            db = new DataBase();
        return db;
    }

    public ResultSet runSql(String sql) throws SQLException {
        statement = (Statement) db.conn.createStatement();
        ResultSet res = statement.executeQuery(sql);
        return res;
    }
}
