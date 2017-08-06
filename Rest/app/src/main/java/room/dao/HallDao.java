package room.dao;

import android.annotation.SuppressLint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import room.bean.Hall;


public class HallDao {
    private final Connection conn;

    public HallDao(Connection conn) {
        this.conn = conn;
    }

    @SuppressLint("NewApi")
    public List<Hall> getHalls() {
        List<Hall> hall = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Darbazi")) {
            try (ResultSet rslt = stmt.executeQuery()) {
                while (rslt.next()) {
                    Hall h = new Hall();
                    h.setIdDarbazi(rslt.getInt("IdDarbazi"));
                    h.setDarbazi(rslt.getString("Darbazi"));
                    h.setIdProek(rslt.getInt("IdProek"));
                    h.setIdDarbazi(rslt.getInt("IdDarbaziT"));
                    h.setHasOwnMenu(rslt.getBoolean("HasOwnMenu"));
                    h.setMomsProc(rslt.getInt("Momsproc"));
                    h.setShen(rslt.getString("Shen"));
                    h.setLenX(rslt.getInt("lenX"));
                    h.setLenY(rslt.getInt("lenY"));
                    hall.add(h);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hall;
    }
}
