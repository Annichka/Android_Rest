package manager;

import java.sql.Connection;

import DataBaseConnection.DataBase;
import room.dao.TableDao;

/**
 * Created by Anna on 07/11/17.
 */
public class TableManager {

    protected Connection conn = null;
    private DataBase db;
    protected TableDao tableD = null;

    public TableManager(DataBase data) throws ClassNotFoundException {
        this.db = data;
        this.conn = db.getConnection();
    }

    public TableDao getTableDao() {
        if(this.tableD == null) {
            this.tableD = new TableDao(this.conn);
        }
        return this.tableD;
    }
}
