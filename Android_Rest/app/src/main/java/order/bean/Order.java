package order.bean;


public class Order {

    private int orderId;
    private String date,  dateCreate, dateEnd;
    private String Zedd;

    private boolean status;
    private double orderPrice;   // შეკვეთის ფასი
    private double servicePrice; // მომსახურება

    private static final int servicePercent = 10;

    public void setOrderId(int orderid) {
        this.orderId = orderid;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDateCreate(String date) {
        this.dateCreate = date;
    }

    public void setDateEnd(String date) {
        this.dateEnd = date;
    }

    public void setZedd(String Zedd) {
        this.Zedd = Zedd;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setOrderPrice(double pr) {
        this.orderPrice = pr;
    }

    public void setServicePrice(double pr) {
        this.servicePrice = pr;
    }

    public int getOrderId() {
        return this.orderId;
    }

    public String getDate() {
        return this.date;
    }

    public String getDateCreate() {
        return this.dateCreate;
    }

    public String getDateEnd() {
        return this.dateEnd;
    }

    public String getZedd() {
        return this.Zedd;
    }

    public boolean getStatus() {
        return this.status;
    }

    public double getOrderPrice() {
        return this.orderPrice;
    }

    public double getServicePrice() {
        return this.servicePrice;
    }

}
