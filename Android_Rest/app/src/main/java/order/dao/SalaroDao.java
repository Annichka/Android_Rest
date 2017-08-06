package order.dao;

import android.annotation.SuppressLint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import order.bean.SalT;
import order.bean.Salaro;

/**
 * Created by Anna on 07/11/17.
 */
public class SalaroDao {

    private final Connection conn;

    public SalaroDao(Connection conn) {
        this.conn = conn;
    }

    @SuppressLint("NewApi")
    public ArrayList<SalT> getPaymentTypes() {
        String table = "SalT";
        ArrayList<SalT> data = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + table)) {
            try (ResultSet rslt = stmt.executeQuery()) {
                while (rslt.next()) {
                    SalT s = new SalT();
                    s.setIdSal(rslt.getInt("IdSalT"));
                    s.setSal(rslt.getString("SalT"));
                    data.add(s);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    @SuppressLint("NewApi")
    public ArrayList<Salaro> getSalaroTypes() {
        String table = "Salaro";
        ArrayList<Salaro> data = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + table)) {
            try (ResultSet rslt = stmt.executeQuery()) {
                while (rslt.next()) {
                    Salaro s = new Salaro();
                    s.setIdSalaro(rslt.getInt("IdSalaro"));
                    s.setSalaro(rslt.getString("Salaro"));
                    s.setIsFiskaluri(rslt.getBoolean("IsFiskaluri"));
                    s.setIdSalaroT(rslt.getInt("IdSalaroT"));
                    s.setShowInClientApp(rslt.getBoolean("ShowInClientApp"));
                    s.setIdFiscalPrinter(rslt.getInt("IdFiscalPrinter"));
                    s.setFiscalPrinter(rslt.getString("FiscalPrinterCOM"));
                    data.add(s);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
