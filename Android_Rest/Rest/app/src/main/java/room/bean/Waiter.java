package room.bean;


public class Waiter {
    private int idGvari,idGank,idGvariT;
    private String gvari;
    private double tveXelp,dgeXelp;
    private int magidaProc,darbaziProc,sulProc,cardnumber;

    public void setIdGvari(int id){ this.idGvari=id; }

    public void setIdGank(int id){ this.idGank=id; }

    public void setIdGvariT(int id){ this.idGvariT=id; }

    public void setGvari(String gv) { this.gvari = gv; }

    public void setTveXelp(double x) { this.tveXelp=x; }

    public void setDgeXelp (double x) { this.dgeXelp = x; }

    public void setMagidaProc(int p) { this.magidaProc = p; }

    public void setDarbaziProc(int p) { this.darbaziProc = p; }

    public void setSulProc(int p) { this.sulProc = p; }

    public void setCardnumber(int num) { this.cardnumber = num; }

    public double getDgeXelp() {
        return dgeXelp;
    }

    public double getTveXelp() {
        return tveXelp;
    }

    public String getGvari() {
        return gvari;
    }

    public int getCardnumber() {
        return cardnumber;
    }

    public int getDarbaziProc() {
        return darbaziProc;
    }

    public int getIdGank() {
        return idGank;
    }

    public int getIdGvari() {
        return idGvari;
    }

    public int getIdGvariT() {
        return idGvariT;
    }

    public int getMagidaProc() {
        return magidaProc;
    }

    public int getSulProc() {
        return sulProc;
    }

    @Override
    public String toString() {
        return gvari;
    }
}
