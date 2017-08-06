package order.dao;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import order.bean.Order;
import order.bean.OrdersMagida;
import order.bean.OrdersProd;
import order.bean.Prod;


public class OrderDao {
    private final Connection conn;

    public OrderDao(Connection conn) {
        this.conn = conn;
    }

    public int makeNewOrder(int tableId, int waiterId) {
        /* ჯერ ვაკეთებ Orders  და მერე მივუერთებ OrdersMagida და OrdersProd-ებს.  */
        int newOrdId = makeOrder(waiterId);

        /* Make new Record in OrdersMagida */
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "INSERT INTO OrdersMagida(IdOrders, IdMagida) VALUES("
                    + newOrdId + ", " + tableId + ")";
            stmt.executeUpdate(sql);

            setOrdersMimtani(newOrdId, waiterId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newOrdId;
    }

    /*
    * ახალი შეკვეთის შექმნა Order მაგიდაში.
    * */
    @SuppressLint("NewApi")
    private int makeOrder(int IdGvari) {
        Order newOrd = new Order();
        int newOrdId = -1;

        String newZedd = generateZedd();

        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO Orders(MomsProc, Zedd," +
                "IdGvari, IdOrg, IdOrdersStatus, IdDeleteT, IdGonisdziebaT, IdOrdersT, " +
                "Shen, Active, Discount, Time) " +
                "VALUES(10, '" + newZedd + "', " + IdGvari  +", 1, -1, 1, 1, 1, 'me', 1, 0, 'date')")) {
            stmt.executeUpdate();

            Statement stmt2 = conn.createStatement();
            ResultSet rs = stmt2.executeQuery("SELECT IDENT_CURRENT('Orders')");
            if (rs.next())
                newOrdId = rs.getInt(1);
            Log.d("Scope Identity", "" + newOrdId);
            rs.close();
            stmt2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newOrdId;
    }

    @SuppressLint("NewApi")
    private String generateZedd() {
        String newZedd = "";
        try (PreparedStatement stmt = conn.prepareStatement("SELECT Num FROM ZeddAuto WHERE TableName LIKE 'Orders'")) {
            ResultSet rslt = stmt.executeQuery();
            if(rslt.next()) {
                int nZedd = rslt.getInt("Num") + 1;
                newZedd += nZedd;
                Statement stmt2 = conn.createStatement();
                String sql = "UPDATE ZeddAuto SET Num = " + nZedd + " WHERE TableName LIKE 'Orders'";
                stmt2.executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newZedd;
    }

    @SuppressLint("NewApi")
    public List<OrdersMagida> getOrdersMagidaByTableId(int tableId) {
        List<OrdersMagida> tabList = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM OrdersMagida WHERE IdMagida = ?")) {
            stmt.setInt(1, tableId);
            try (ResultSet rslt = stmt.executeQuery()) {
                while (rslt.next()) {
                    OrdersMagida tabl = new OrdersMagida();
                    tabl.setOrdersMagida(rslt.getInt("IdOrdersMagida"));
                    tabl.setOrdersId(rslt.getInt("IdOrders"));
                    tabl.setMagidaId(rslt.getInt("IdMagida"));
                    tabList.add(tabl);
                }
                return tabList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tabList;
    }

    @SuppressLint("NewApi")
    public int getActiveOrder(List<OrdersMagida> ordTabl) {
        int activeOrd = -1;
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Orders" +
                " WHERE IdOrders  = ?")) {
            for (int i = 0; i < ordTabl.size(); i++) {
                stmt.setInt(1, ordTabl.get(i).getOrdersId());
                try (ResultSet rslt = stmt.executeQuery()) {
                    int b = -111;
                    if (rslt.next())
                        b = rslt.getInt("Active");
                    if (b == 1) {
                        activeOrd = rslt.getInt("IdOrders");
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activeOrd;
    }

    @SuppressLint("NewApi")
    public ArrayList<OrdersProd> getOrdersProdByOrderId(int orderId) {
        Log.d("orderis ID ", "" + orderId);

        String table = "OrdersProd";
        ArrayList<OrdersProd> ordProd = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + table +
                " WHERE IdOrders  = ?")) {
            stmt.setInt(1, orderId);
            try (ResultSet rslt = stmt.executeQuery()) {
                while (rslt.next()) {
                    OrdersProd ord = new OrdersProd();
                    ord.setOrdersId(rslt.getInt("IdOrders"));
                    ord.setQuantity(rslt.getInt("Raod"));
                    ord.setProdId(rslt.getString("IdProd"));
                    ord.setDateCreate(rslt.getString("DataCreate"));
                    ord.setDateModified(rslt.getString("DataModified"));
                    ord.setOrdersProdId(rslt.getInt("IdOrdersProd"));
                    ord.setOrdersProdStatusId(rslt.getInt("IdOrdersProdStatus"));
                    ord.setMomzadebaT(rslt.getString("MomzadebaT"));
                    ord.setPrice(rslt.getDouble("Fasi"));
                    ord.setSum(rslt.getDouble("Tanxa"));
                    ord.setMomsCost(rslt.getDouble("MomsTanxa"));
                    ordProd.add(0, ord);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ordProd;
    }

    /**
     * Methods for OrdersProd
     *
     * @param foodList
     * @throws SQLException
     */
    /* Insert into OrdersProd */
    public void addAllProd(List<OrdersProd> foodList, int ordId) {
        for (int i = 0; i < foodList.size(); i++) {
            OrdersProd curr = foodList.get(i);
           // Log.d("StatusPRod    " + i,curr.getOrdersProdStatusId() + "");
            Statement stmt = null;
            String sql = "INSERT INTO OrdersProd" +
                    "(IdOrders, DataCreate, DataModified, IdProd, IdORdersProdStatus, " +
                    "IdDeleteT, Raod, Fasi, SubOrderNum, Printed, " +
                    "MomzadebaT, UN, CD, MomsTanxa, FasiStandart) " +
                    "VALUES (" +
                    ordId + ", 'asd', 'asd1', '" + curr.getProdId() + "', " + curr.getOrdersProdStatusId() + //TODO:
                    ", 1, " + curr.getQuantity() + ", " + curr.getPrice() + ", 0, 0, " +
                    "'" + curr.getMomzadebaT() + "', 'sa', 'asd2', 2.93, " + curr.getPrice() + ")";
            try {
                stmt = conn.createStatement();
                stmt.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void finishOrder(int ordId, double sum) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String table = "Orders";
            String sql = "UPDATE " + table +" SET Active = 0 WHERE IdOrders = " + ordId;
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Add new Product to OrdersProd. */
    public void addProdToOrder(Prod p, int orderId) {
        OrdersProd isAdded = findProdInOrder(p.getProdId(), orderId);
        if(isAdded == null) {
            String sql = "INSERT INTO OrdersProd(IdOrders, DataCreate, DataModified, IdProd, " +
                    "IdOrdersProdStatus, IdDeleteT, Raod, Fasi, SubOrderNum, Printed, " +
                    "MomzadebaT, UN, MomsTanxa, FasiStandart) VALUES (" + orderId + ", GETDATE(), GETDATE(), '" + p.getProdId() + "',"
                    + " 1, 1, 1, 2.40, 0, 0, '', 'sa', 2.93, 1);";
            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                stmt.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("NewApi")
    public int getMomsProc(int orderId) {
        int num = -1;
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Orders" +
                " WHERE IdOrders  = " + orderId)) {
            try (ResultSet rslt = stmt.executeQuery()) {
                if (rslt.next()) {
                    num = rslt.getInt("MomsProc");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num;
    }

    @SuppressLint("NewApi")
    public String getLatestDate(int orderid) {
        String date = "";
        try (PreparedStatement stmt = conn.prepareStatement("SELECT TOP 1 * FROM OrdersProd WHERE IdOrders = " + orderid +
                        " ORDER BY dataCreate DESC;")) {
            try (ResultSet rslt = stmt.executeQuery()) {
                if (rslt.next()) {
                    date = rslt.getString("DataCreate");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return date;
    }

    private void setOrdersMimtani(int orderId, int waiterId) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "INSERT INTO OrdersMimtani(IdOrders, IdGvari) VALUES (" + orderId + ", " + waiterId +")";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    private OrdersProd findProdInOrder(String IdProd, int ordId) {
        OrdersProd cr = new OrdersProd();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM OrdersProd" +
                " WHERE IdOrders  = " + ordId + "AND IdProd LIKE '" + IdProd + "';")) {
            try (ResultSet rslt = stmt.executeQuery()) {
                if (rslt.next()) {
                    cr.setProdId(rslt.getString("IdProd"));
                    return cr;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}