package room.bean;


public class Table {
    private int tableId;
    private String table;
    private int roomId;
  //  private int romeli oficiantia am magidastan.

    public void setTableId(int tableid) {
        this.tableId = tableid;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public void setRoom(int roomid) {
        this.roomId = roomid;
    }

    public int getTableId() {
        return this.tableId;
    }

    public String getTable() {
        return this.table;
    }

    public int getRoomId() {
        return this.roomId;
    }
}
