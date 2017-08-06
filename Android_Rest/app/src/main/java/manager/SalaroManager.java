package manager;

import java.sql.Connection;

import DataBaseConnection.DataBase;
import order.dao.SalaroDao;

/**
 * Created by Anna on 07/11/17.
 */
public class SalaroManager {
    protected Connection conn = null;
    private DataBase db;
    protected SalaroDao salD = null;

    public SalaroManager(DataBase data) throws ClassNotFoundException {
        this.db = data;
        this.conn = db.getConnection();
    }

    public SalaroDao getSalaroDao() {
        if(this.salD == null) {
            this.salD = new SalaroDao(this.conn);
        }
        return this.salD;
    }
}
