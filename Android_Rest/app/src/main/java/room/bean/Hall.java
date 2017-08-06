package room.bean;


public class Hall {
    private int idDarbazi, idProek, idDarbaziT, momsProc, lenX, lenY;
    private String darbazi, shen;
    private boolean hasOwnMenu;

    public void setIdDarbazi(int id) {
        this.idDarbazi = id;
    }

    public void setIdProek(int id) { this.idProek = id; }

    public void setIdDarbaziT(int id) { this.idDarbaziT = id; }

    public void setMomsProc(int p) { this.momsProc = p; }

    public void setLenX(int x) { this.lenX = x; }

    public void setLenY(int y) { this.lenY = y; }

    public void setDarbazi(String darb) { this.darbazi = darb; }

    public void setShen(String shen) { this.shen = shen; }

    public void setHasOwnMenu(boolean b) { this.hasOwnMenu = b; }

    public int getIdDarbazi() {
        return idDarbazi;
    }

    public int getIdProek() {
        return idProek;
    }

    public int getLenX() {
        return lenX;
    }

    public int getLenY() {
        return lenY;
    }

    public boolean isHasOwnMenu() {
        return hasOwnMenu;
    }

    public int getIdDarbaziT() {
        return idDarbaziT;
    }

    public int getMomsProc() {
        return momsProc;
    }

    public String getDarbazi() {
        return darbazi;
    }

    public String getShen() {
        return shen;
    }
}
