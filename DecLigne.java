import java.util.ArrayList;

public class DecLigne extends DecCasesAccessibles {

    public DecLigne() {}
    public DecLigne(DecCasesAccessibles base) {
        super(base);
    }

    @Override
    protected ArrayList<Case> getCasesAccessibles(Piece piece) {
        ArrayList<Case> resultats = new ArrayList<>();
        Damier damier = piece.getPlateau(); // ✅ Rester générique
        int x = piece.getX();
        int y = piece.getY();

        // Droite
        for (int i = x + 1; i < 8; i++) {
            Case c = damier.getCase(i, y);
            if (!ajouterSiLibreOuAdverse(c, piece, resultats)) break;
        }

        // Gauche
        for (int i = x - 1; i >= 0; i--) {
            Case c = damier.getCase(i, y);
            if (!ajouterSiLibreOuAdverse(c, piece, resultats)) break;
        }

        // Haut
        for (int j = y - 1; j >= 0; j--) {
            Case c = damier.getCase(x, j);
            if (!ajouterSiLibreOuAdverse(c, piece, resultats)) break;
        }

        // Bas
        for (int j = y + 1; j < 8; j++) {
            Case c = damier.getCase(x, j);
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
