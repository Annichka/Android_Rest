package room.dao;


import android.annotation.SuppressLint;
import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import room.bean.Table;

public class TableDao {

    private final Connection conn;

    public TableDao(Connection conn) {
        this.conn = conn;
    }

    @SuppressLint("NewApi")
    public List<Table> getTablesByHall(int hallId, int waiterId) {
        List<Table> tables = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM GvariMagida, Magida  WHERE IdDarbazi = " + hallId
                        +" AND IdGvari = " + waiterId +" AND GvariMagida.IdMagida = Magida.IdMagida")) {
            try (ResultSet rslt = stmt.executeQuery()) {
                while (rslt.next()) {
                    Table tb = new Table();
                    tb.setTableId(rslt.getInt("IdMagida"));
                    tb.setTable(rslt.getString("Magida"));
                    tb.setRoom(rslt.getInt("IdDarbazi"));
                    tables.add(tb);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }
}