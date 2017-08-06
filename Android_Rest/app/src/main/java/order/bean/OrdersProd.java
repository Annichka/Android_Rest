package order.bean;


public class OrdersProd {

    private int OrdersProdId, OrdersId, OrdersProdStatusId;
    private String ProdId, MomzadebaT;
    private String DateCreate, DateModified;
    private int quantity;
    private double price; //as Fasi
    private double sum; //as Tanxa
    private double MomsCost;

    private boolean isEnable;

    public void setOrdersProdId(int id) {
        this.OrdersProdId = id;
    }

    public void setOrdersId(int id) {
        this.OrdersId = id;
    }

    public void setProdId(String id) {
        this.ProdId = id;
    }

    public void setMomzadebaT(String momz) { this.MomzadebaT = momz; }

    public void setOrdersProdStatusId(int id) {
        this.OrdersProdStatusId = id;
    }

    public void setDateCreate(String date) {
        this.DateCreate = date;
    }

    public void setDateModified(String date) {
        this.DateModified = date;
    }

    public void setQuantity(int q) {
        this.quantity = q;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public void setMomsCost(double cost) {
        this.MomsCost = cost;
    }

    public void setEnable(Boolean b) { this.isEnable = b; }

    public int getOrdersProdId() { return this.OrdersProdId; }

    public int getOrdersId() { return this.OrdersId; }

    public String getProdId() { return this.ProdId; }

    public String getMomzadebaT() { return this.MomzadebaT; }

    public int getOrdersProdStatusId() { return this.OrdersProdStatusId; }

    public String getDateCreate() { return this.DateCreate; }

    public String getDateModified() { return this.DateModified; }

    public int getQuantity() {
        return this.quantity;
    }

    public double getPrice() { return this.price; }

    public double getSum() {
        sum = quantity*price;
        return this.sum;
    }

    public double getMomsCost() { return this.MomsCost; }

    public boolean isEnable() { return this.isEnable; }
}
