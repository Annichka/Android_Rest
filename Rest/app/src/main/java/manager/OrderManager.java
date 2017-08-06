package manager;

import java.sql.Connection;

import DataBaseConnection.DataBase;
import order.dao.OrderDao;

/**
 * Created by Anna on 07/11/17.
 */
public class OrderManager  {
    protected Connection conn = null;
    private DataBase db;
    protected OrderDao orderD = null;

    public OrderManager(DataBase data) throws ClassNotFoundException {
        this.db = data;
        this.conn = db.getConnection();
    }

    public OrderDao getOrderDao() {
        if(this.orderD == null){
            this.orderD = new OrderDao(this.conn);
        }
        return this.orderD;
    }
}
