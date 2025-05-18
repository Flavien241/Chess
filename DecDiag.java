import java.util.ArrayList;

public class DecDiag extends DecCasesAccessibles {

    public DecDiag() {}
    public DecDiag(DecCasesAccessibles base) {
        super(base);
    }

    @Override
    protected ArrayList<Case> getCasesAccessibles(Piece piece) {
        ArrayList<Case> resultats = new ArrayList<>();
        Damier damier = piece.getPlateau(); 
        int x = piece.getX();
        int y = piece.getY();

        // Haut-gauche
        for (int dx = -1, dy = -1; x + dx >= 0 && y + dy >= 0; dx--, dy--) {
            Case c = damier.getCase(x + dx, y + dy);
            if (!ajouterSiLibreOuAdverse(c, piece, resultats)) break;
        }

        // Haut-droite
        for (int dx = 1, dy = -1; x + dx < 8 && y + dy >= 0; dx++, dy--) {
            Case c = damier.getCase(x + dx, y + dy);
            if (!ajouterSiLibreOuAdverse(c, piece, resultats)) break;
        }

        // Bas-gauche
        for (int dx = -1, dy = 1; x + dx >= 0 && y + dy < 8; dx--, dy++) {
            Case c = damier.getCase(x + dx, y + dy);
            if (!ajouterSiLibreOuAdverse(c, piece, resultats)) break;
        }

        // Bas-droite
        for (int dx = 1, dy = 1; x + dx < 8 && y + dy < 8; dx++, dy++) {
            Case c = damier.getCase(x + dx, y + dy);
            if (!ajouterSiLibreOuAdverse(c, piece, resultats)) break;
        }

        return resultats;
    }

    private boolean ajouterSiLibreOuAdverse(Case c, Piece piece, ArrayList<Case> liste) {
        if (c == null) return false;
        if (c.getPiece() == null) {
            liste.add(c);
            return true;
        } else if (c.getPiece().estBlanc() != piece.estBlanc()) {
            liste.add(c);
            return false;
        } else {
            return false;
        }
    }
}
