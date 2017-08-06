package order.bean;

/**
 * Created by Anna on 07/11/17.
 */
public class SalT {
    private int idSal;
    private String sal;

    public void setIdSal(int id) { this.idSal = id; }

    public void setSal(String sal) { this.sal = sal; }

    public int getIdSal() { return this.idSal; }

    public String getSal() { return this.sal; }

    @Override
    public String toString() {
        return this.sal;
    }
}
