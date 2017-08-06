package room.dao;

import android.annotation.SuppressLint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import room.bean.Waiter;


public class WaiterDao {

    private final Connection conn;

    public WaiterDao(Connection conn) {
        this.conn = conn;
    }

    @SuppressLint("NewApi")
    public Waiter getWaiterByCard(int card) throws SQLException {
        try(PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Gvari WHERE CardNumber = " + card)) {
            try(ResultSet rslt = stmt.executeQuery()) {
                if(rslt.next()) {
                    Waiter w = new Waiter();
                    w.setIdGvari(rslt.getInt("IdGvari"));
                    w.setGvari(rslt.getString("Gvari"));
                    return w;
                }
            }
        }
        return null;
    }

    @SuppressLint("NewApi")
    public Waiter getWaiterById(int id) throws SQLException {
        try(PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Gvari WHERE IdGvari = " + id)) {
            try(ResultSet rslt = stmt.executeQuery()) {
                if(rslt.next()) {
                    Waiter w = new Waiter();
                    w.setIdGvari(rslt.getInt("IdGvari"));
                    w.setGvari(rslt.getString("Gvari"));
                    return w;
                }
            }
        }
        return null;
    }
}
