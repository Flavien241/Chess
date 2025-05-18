public abstract class Damier {
    protected Case[][] cases = new Case[8][8];

    public abstract void initialiser();
    public abstract boolean jouerCoup(int startX, int startY, int endX, int endY);

    // Ces deux méthodes doivent être déclarées ici comme abstraites
    public abstract boolean estSituationCritique(boolean blanc);
    public abstract boolean estFinDePartie(boolean blanc);

    public Case getCase(int x, int y) {
        if (x >= 0 && x < 8 && y >= 0 && y < 8)
            return cases[x][y];
        return null;
    }

    public Piece getPieceAt(int x, int y) {
        Case c = getCase(x, y);
        return c != null ? c.getPiece() : null;
    }

    public void setPieceAt(int x, int y, Piece piece) {
        Case c = getCase(x, y);
        if (c != null) c.setPiece(piece);
    }

    public Piece removePieceAt(int x, int y) {
        Case c = getCase(x, y);
        if (c != null) {
            Piece p = c.getPiece();
            c.setPiece(null);
            return p;
        }
        return null;
    }
}
