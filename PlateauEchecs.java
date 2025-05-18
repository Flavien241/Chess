import java.util.ArrayList;
import java.util.List;

public class PlateauEchecs extends Damier {
    private Roi roiBlanc;
    private Roi roiNoir;

    public PlateauEchecs() {
        super(); // Initialise les cases
        initialiser(); // 👈 Appelle la méthode requise par l'abstraction
    }

    @Override
    public void initialiser() {
        // Étape 1 : Initialisation des cases
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cases[i][j] = new Case(i, j, null);
            }
        }

        // Étape 2 : Placement des pièces noires (en haut, lignes 0 et 1)
        roiNoir = new Roi(0, 4, false, this);
        cases[0][4].setPiece(roiNoir);
        cases[0][0].setPiece(new Tour(0, 0, false, this));
        cases[0][1].setPiece(new Cavalier(0, 1, false, this));
        cases[0][2].setPiece(new Fou(0, 2, false, this));
        cases[0][3].setPiece(new Reine(0, 3, false, this));
        cases[0][5].setPiece(new Fou(0, 5, false, this));
        cases[0][6].setPiece(new Cavalier(0, 6, false, this));
        cases[0][7].setPiece(new Tour(0, 7, false, this));
        for (int j = 0; j < 8; j++) {
            cases[1][j].setPiece(new Pion(1, j, false, this));
        }

        // Étape 3 : Placement des pièces blanches (en bas, lignes 6 et 7)
        roiBlanc = new Roi(7, 4, true, this);
        cases[7][4].setPiece(roiBlanc);
        cases[7][0].setPiece(new Tour(7, 0, true, this));
        cases[7][1].setPiece(new Cavalier(7, 1, true, this));
        cases[7][2].setPiece(new Fou(7, 2, true, this));
        cases[7][3].setPiece(new Reine(7, 3, true, this));
        cases[7][5].setPiece(new Fou(7, 5, true, this));
        cases[7][6].setPiece(new Cavalier(7, 6, true, this));
        cases[7][7].setPiece(new Tour(7, 7, true, this));
        for (int j = 0; j < 8; j++) {
            cases[6][j].setPiece(new Pion(6, j, true, this));
        }
    }



    public Roi getRoi(boolean blanc) {
        return blanc ? roiBlanc : roiNoir;
    }

    public List<Piece> getToutesLesPieces(boolean blanc) {
        List<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece p = getPieceAt(i, j);
                if (p != null && p.estBlanc() == blanc) {
                    pieces.add(p);
                }
            }
        }
        return pieces;
    }

    @Override
public boolean estSituationCritique(boolean blanc) {
    Roi roi = getRoi(blanc);
    if (roi == null) return false;
    for (Piece p : getToutesLesPieces(!blanc)) {
        for (Case c : p.getCoupsPossibles()) {
            if (c.getX() == roi.getX() && c.getY() == roi.getY()) {
                return true;
            }
        }
    }
    return false;
}

@Override
public boolean estFinDePartie(boolean blanc) {
    if (!estSituationCritique(blanc)) return false;

    for (Piece p : getToutesLesPieces(blanc)) {
        for (Case c : p.getCoupsPossibles()) {
            int oldX = p.getX(), oldY = p.getY();
            Piece temp = getPieceAt(c.getX(), c.getY());

            removePieceAt(oldX, oldY);
            setPieceAt(c.getX(), c.getY(), p);
            p.setPosition(c.getX(), c.getY());

            boolean encoreEchec = estSituationCritique(blanc);

            // annulation
            setPieceAt(oldX, oldY, p);
            p.setPosition(oldX, oldY);
            setPieceAt(c.getX(), c.getY(), temp);

            if (!encoreEchec) return false;
        }
    }
    return true;
}



    @Override
public boolean jouerCoup(int startX, int startY, int endX, int endY) {
    Piece piece = getPieceAt(startX, startY);
    if (piece == null) return false;

    for (Case cible : piece.getCoupsPossibles()) {
        if (cible.getX() == endX && cible.getY() == endY) {
            removePieceAt(startX, startY);
            setPieceAt(endX, endY, piece);
            piece.setPosition(endX, endY);
            return true;
        }
    }
    return false;
}

}
