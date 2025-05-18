public class Case {
    private int x, y;
    private Piece piece;

    public Case(int x, int y, Piece piece) {
        this.x = x;
        this.y = y;
        this.piece = piece;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public boolean estOccupee() {
        return piece != null;
    }

    public boolean contientPieceAdverse(boolean estBlanc) {
        return piece != null && piece.estBlanc() != estBlanc;
    }
}
