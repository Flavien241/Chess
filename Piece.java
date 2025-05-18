import java.util.List;

public abstract class Piece {
    protected int x, y;
    protected boolean estBlanc;
    protected Damier plateau; // ici, type plus générique
    protected DecCasesAccessibles dca;

    protected boolean hasMoved = false;

    public Piece(int x, int y, boolean estBlanc, Damier plateau) {
        this.x = x;
        this.y = y;
        this.estBlanc = estBlanc;
        this.plateau = plateau;
    }

    public boolean hasMoved() { return hasMoved; }
    public void setHasMoved(boolean moved) { this.hasMoved = moved; }

    public Damier getPlateau() {
        return plateau;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean estBlanc() { return estBlanc; }
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract List<Case> getCoupsPossibles();
    public abstract String getNom();
    public abstract String getIconPath();
}
