package manager;

import java.sql.Connection;

import DataBaseConnection.DataBase;
import room.dao.WaiterDao;

/**
 * Created by Anna on 07/11/17.
 */
public class WaiterManager {

    protected Connection conn = null;
    private DataBase db;
    protected WaiterDao waiterD = null;

    public WaiterManager(DataBase data) throws ClassNotFoundException{
        this.db = data;
        this.conn = db.getConnection();
    }

    public WaiterDao getWaiterDao(){
        if(this.waiterD == null){
            this.waiterD = new WaiterDao(this.conn);
        }
        return this.waiterD;
    }
}
