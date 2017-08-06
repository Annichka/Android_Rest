package order.bean;

/**
 * Prod list for one order.
 */
public class Prod {
    private String prodId, prodName;
    private String prodShort, prodEng, prodShortEng, prodRus, prodShortRus;
    private double prodPrice;
    private int prodCatId, cookingTime;

    public Prod() {
        prodId = "";
        prodName = "";
        prodEng = "";
    }

    public void setProdId(String id) {
        this.prodId = id;
    }

    public void setProdName(String name) {
        this.prodName = name;
    }

    public void setProdPrice(double price) {
        this.prodPrice = price;
    }

    public void setProdCatId(int id) {
        this.prodCatId = id;
    }

    public void setProdShort(String n) {
        this.prodShort = n;
    }

    public void setProdEng(String n) {
        this.prodEng = n;
    }

    public void setProdShortEng(String n) {
        this.prodShortEng = n;
    }

    public void setProdRus(String n) {
        this.prodRus = n;
    }

    public void setProdShortRus(String n) {
        this.prodShortRus = n;
    }

    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getProdId() {
        return this.prodId;
    }

    public String getProdName() {
        return this.prodName;
    }

    public double getProdPrice() {
        return this.prodPrice;
    }

    public int getProdCatId() {
        return this.prodCatId;
    }

    public String getProdShort() {
        return this.prodShort;
    }

    public String getProdEng() {
        return this.prodEng;
    }

    public String getProdShortEng() {
        return this.prodShortEng;
    }

    public String getProdRus() {
        return this.prodRus;
    }

    public String getProdShortRus() {
        return this.prodShortRus;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    @Override
    public String toString() {
        return prodEng;
    }

}
