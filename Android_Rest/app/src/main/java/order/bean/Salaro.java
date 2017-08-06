package order.bean;


public class Salaro {

    private int idSalaro, idSalaroT, idFiscalPrinter;
    private String salaro, fiscalPrinter;
    private boolean isFiskaluri, withoutCash, showInClientApp;

    public void setIdSalaro(int id) {
        this.idSalaro = id;
    }

    public void setIdSalaroT(int id) {
        this.idSalaroT = id;
    }

    public void setIdFiscalPrinter(int id) {
        this.idFiscalPrinter = id;
    }

    public void setSalaro(String sal) {
        this.salaro = sal;
    }

    public void setFiscalPrinter(String fisc) {
        this.fiscalPrinter = fisc;
    }

    public void setIsFiskaluri(boolean b) {
        this.isFiskaluri = b;
    }

    public void setWithoutCash(boolean b){
        this.withoutCash = b;
    }

    public void setShowInClientApp(boolean b) {
        this.showInClientApp = b;
    }

    public int getIdSalaro() { return this.idSalaro; }
    public int getIdSalaroT() { return this.idSalaroT; }
    public int getIdFiscalPrinter() { return this.idFiscalPrinter; }
    public String getSalaro() { return this.salaro; }
    public String getFiscalPrinter() { return fiscalPrinter; }
    public boolean getIsFiscaluri() { return this.isFiskaluri; }
    public boolean getWithoutCash() { return this.withoutCash; }

    public boolean isShowInClientApp() {
        return showInClientApp;
    }

    @Override
    public String toString() {
        return salaro;
    }
}
