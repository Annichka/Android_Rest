package order.dao;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import order.bean.MomzadebaT;
import order.bean.Prod;
import order.bean.ProdTG;

/**
 * Created by Anna on 07/11/17.
 */
public class ProdDao {

    private final Connection conn;

    public ProdDao(Connection conn) {
        this.conn = conn;
    }

    public void addProd(Prod f) {
        Statement stmt = null;
        try {
            stmt = (Statement) conn.createStatement();
            String table = "";
            String sql = "INSERT INTO " + table + " VALUES('" + f.getProdId() + "', '"
                    + f.getProdName() + "', '" + f.getProdPrice() + "')";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    * აქ რაღაც უცნაური ერრორ ამომიგდო და უნდა გადავამოწმო.
    * */
    @SuppressLint("NewApi")
    public Prod findProdById(String id) {
        String table = "Prod";
        try(PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + table
                + " WHERE IdProd like '" + id + "'")) {
            try(ResultSet rslt = stmt.executeQuery()) {
                if(rslt.next()) {
                    Prod f = new Prod();
                    String prodId = rslt.getString("IdProd");
                    f.setProdId(prodId);
                    f.setProdName(rslt.getString("Prod"));
                    f.setProdShort(rslt.getString("ProdShort"));
                    f.setProdCatId(rslt.getInt("IdProdCat"));
                    f.setProdRus(rslt.getString("ProdRus"));
                    f.setProdShortRus(rslt.getString("ProdShortRus"));
                    f.setProdEng(rslt.getString("ProdEng"));
                    f.setProdShortRus(rslt.getString("ProdShortEng"));
                    f.setCookingTime(rslt.getInt("CookingTime"));
                    f.setProdPrice(rslt.getDouble("Fasi1"));
                    if(!isOutOfStock(prodId))
                        return f;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("NewApi")
    public ArrayList<Prod> findProdByName(String name) {
        ArrayList<Prod> found = new ArrayList<>();
        try(PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Prod"
                + " WHERE ProdEng LIKE '%" + name + "%';")) {
            try(ResultSet rslt = stmt.executeQuery()) {
                while(rslt.next()) {
                    Prod f = new Prod();
                    String prodId = rslt.getString("IdProd");
                    f.setProdId(prodId);
                    f.setProdName(rslt.getString("Prod"));
                    f.setProdShort(rslt.getString("ProdShort"));
                    f.setProdCatId(rslt.getInt("IdProdCat"));
                    f.setProdRus(rslt.getString("ProdRus"));
                    f.setProdShortRus(rslt.getString("ProdShortRus"));
                    f.setProdEng(rslt.getString("ProdEng"));
                    f.setProdShortRus(rslt.getString("ProdShortEng"));
                    f.setProdPrice(rslt.getDouble("Fasi1"));
                    if(!isOutOfStock(prodId))
                        found.add(f);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return found;
    }

    @SuppressLint("NewApi")
    public ArrayList<Prod> getMainMenu() {
        ArrayList<Prod> mainMenu = new ArrayList<>();
        String table = "Prod";
        try(PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + table
                + " WHERE IsInMainMenu = 1")) {
            try (ResultSet rslt = stmt.executeQuery()) {
                while (rslt.next()) {
                    Prod f = new Prod();
                    String prodId = rslt.getString("IdProd");
                    f.setProdId(prodId);
                    f.setProdName(rslt.getString("Prod"));
                    f.setProdShort(rslt.getString("ProdShort"));
                    f.setProdCatId(rslt.getInt("IdProdCat"));
                    f.setProdRus(rslt.getString("ProdRus"));
                    f.setProdShortRus(rslt.getString("ProdShortRus"));
                    f.setProdEng(rslt.getString("ProdEng"));
                    f.setProdShortRus(rslt.getString("ProdShortEng"));
                    f.setProdPrice(rslt.getDouble("Fasi1"));
                    if(!isOutOfStock(prodId))
                        mainMenu.add(f);
                }
            }
        } catch (SQLException e) {
        }
        return mainMenu;
    }

    @SuppressLint("NewApi")
    public ArrayList<Prod> getMainMenuByTG(int tg) {
        ArrayList<Prod> menu = new ArrayList<>();

        try(PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Prod, ProdT, ProdTG"
                + " WHERE IsInMainMenu = 1 AND ProdT.idprodt = prod.idprodt " +
                "AND prodt.idprodtg = prodtg.idprodtg AND ProdTG.IdProdTG = " + tg)) {
            try (ResultSet rslt = stmt.executeQuery()) {
                while (rslt.next()) {
                    Prod f = new Prod();
                    f.setProdId(rslt.getString("IdProd"));
                    f.setProdName(rslt.getString("Prod"));
                    f.setProdShort(rslt.getString("ProdShort"));
                    f.setProdCatId(rslt.getInt("IdProdCat"));
                    f.setProdRus(rslt.getString("ProdRus"));
                    f.setProdShortRus(rslt.getString("ProdShortRus"));
                    f.setProdEng(rslt.getString("ProdEng"));
                    f.setProdShortRus(rslt.getString("ProdShortEng"));
                    f.setProdPrice(rslt.getDouble("Fasi1"));
                    menu.add(f);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menu;
    }

    @SuppressLint("NewApi")
    public ArrayList<MomzadebaT> getMomzadebaListByProd(String prodId) {
        ArrayList<MomzadebaT> momList = new ArrayList<>();
        try(PreparedStatement stmt = conn.prepareStatement("SELECT prodmomzadebat.IdMomzadebaT, MomzadebaT " +
                "FROM momzadebat, prodmomzadebaT " +
                "WHERE momzadebat.idmomzadebat = prodmomzadebat.idmomzadebat and idprod like '" + prodId + "'")) {
            try (ResultSet rslt = stmt.executeQuery()) {
                while (rslt.next()) {
                    MomzadebaT momNew = new MomzadebaT();
                    momNew.setId(rslt.getInt("IdMomzadebaT"));
                    momNew.setMomzadebaT(rslt.getString("MomzadebaT"));
                    momList.add(momNew);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return momList;
    }

    @SuppressLint("NewApi")
    public int getCookingTime(String prodId) {
        int time = -1;

        try(PreparedStatement stmt = conn.prepareStatement("SELECT CookingTime FROM Prod "
                + " WHERE IdProd LIKE '" + prodId + "'")) {
            try (ResultSet rslt = stmt.executeQuery()) {
                if (rslt.next()) {
                    time = rslt.getInt("CookingTime");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return time;
    }

    @SuppressLint("NewApi")
    private boolean isOutOfStock(String prodId) {
        String table = "ProdOutOfStock";
        try(PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + table
                + " WHERE IdProd like '" + prodId + "'")) {
            try (ResultSet rslt = stmt.executeQuery()) {
                if (rslt.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
