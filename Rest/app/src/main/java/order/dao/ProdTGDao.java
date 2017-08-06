package order.dao;

import android.annotation.SuppressLint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import order.bean.ProdTG;

/**
 * Created by Anna on 07/11/17.
 */
public class ProdTGDao {
    private final Connection conn;

    public ProdTGDao(Connection conn) {
        this.conn = conn;
    }

    @SuppressLint("NewApi")
    public ArrayList<ProdTG> getMainMenuTG() {
        ArrayList<ProdTG> tgs = new ArrayList<>();
        try(PreparedStatement stmt = conn.prepareStatement("Select * from prodTG where idProdtg in(SELECT ProdTG.IdProdTG FROM prod, prodT, prodTg " +
                        "WHERE IsInMainMenu = 1 and prod.IdProdT = prodT.IdProdT and " +
                "prodT.IdProdTG = prodTG.IdProdTG GROUP BY prodTg.IdProdTG)")) {
            try(ResultSet rslt = stmt.executeQuery()) {
                while(rslt.next()) {
                    ProdTG tgp = new ProdTG();
                    tgp.setIdprodtg(rslt.getInt("IdProdTG"));
                    tgp.setProdtg(rslt.getString("ProdTG"));
                    tgs.add(tgp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tgs;
    }
}
