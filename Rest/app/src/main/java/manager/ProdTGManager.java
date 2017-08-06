package manager;

import java.sql.Connection;

import DataBaseConnection.DataBase;
import order.dao.ProdTGDao;

/**
 * Created by Anna on 07/11/17.
 */
public class ProdTGManager {
    protected Connection conn = null;
    private DataBase db;
    protected ProdTGDao prodtgD = null;

    public ProdTGManager(DataBase data) throws ClassNotFoundException{
        this.db = data;
        this.conn = db.getConnection();
    }

    public ProdTGDao getProdTGDao(){
        if(this.prodtgD == null){
            this.prodtgD = new ProdTGDao(this.conn);
        }
        return this.prodtgD;
    }
}
