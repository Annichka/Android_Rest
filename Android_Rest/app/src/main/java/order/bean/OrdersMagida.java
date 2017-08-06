package order.bean;

/**
 * Created by Anna on 07/11/17.
 */
public class OrdersMagida {
    private int OrdersMagida, OrdersId, MagidaId;

    public void setOrdersMagida(int id) {
        this.OrdersMagida = id;
    }

    public void setOrdersId(int id) {
        this.OrdersId = id;
    }

    public void setMagidaId(int id) {
        this.MagidaId = id;
    }

    public int getOrdersMagida() {
        return this.OrdersMagida;
    }

    public int getOrdersId() {
        return this.OrdersId;
    }

    public int getMagidaId() {
        return this.MagidaId;
    }
}
