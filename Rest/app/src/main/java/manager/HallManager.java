package manager;

import java.sql.Connection;

import DataBaseConnection.DataBase;
import room.dao.HallDao;

/**
 * Created by Anna on 07/11/17.
 */
public class HallManager {
    protected Connection conn = null;
    private DataBase db;
    protected HallDao roomD = null;

    public HallManager(DataBase data) throws ClassNotFoundException{
        this.db = data;
        this.conn = db.getConnection();
    }

    public HallDao getHallDao(){
        if(this.roomD == null){
            this.roomD = new HallDao(this.conn);
        }
        return this.roomD;
    }

}
