package manager;

import java.sql.Connection;

import DataBaseConnection.DataBase;
import order.dao.ProdDao;

/**
 * Created by Anna on 07/11/17.
 */
public class ProdManager {
    protected Connection conn = null;
    private DataBase db;
    protected ProdDao prodD = null;

    public ProdManager(DataBase data) throws ClassNotFoundException {
        this.db = data;
        this.conn = db.getConnection();
    }

    public ProdDao getProdDao(){
        if(this.prodD == null){
            this.prodD = new ProdDao(this.conn);
        }
        return this.prodD;
    }
}
