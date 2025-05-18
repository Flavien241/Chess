import java.util.ArrayList;
import java.util.List;

public class Roi extends Piece {

    public Roi(int x, int y, boolean estBlanc, PlateauEchecs plateau) {
        super(x, y, estBlanc, plateau);
        // Ici on pourrait utiliser un décorateur DecLimite si tu le crées plus tard
    }

    @Override
    public List<Case> getCoupsPossibles() {
        List<Case> coups = new ArrayList<>();
        int[] directions = {-1, 0, 1};

        for (int dx : directions) {
            for (int dy : directions) {
                if (dx != 0 || dy != 0) {
                    int newX = x + dx;
                    int newY = y + dy;
                    if (estDansLimites(newX, newY)) {
                        Case cible = plateau.getCase(newX, newY);
                        if (!cible.estOccupee() || cible.contientPieceAdverse(estBlanc)) {
                            coups.add(cible);
                        }
                    }
                }
            }
        }

        if (!hasMoved()) {
            // petit roque (tour à y=7)
            Piece pTourD = plateau.getPieceAt(x, 7);
            if (pTourD instanceof Tour tourD && !tourD.hasMoved()
                && plateau.getCase(x,5).getPiece() == null
                && plateau.getCase(x,6).getPiece() == null) {
                coups.add(plateau.getCase(x, 6));
            }

            // grand roque (tour à y=0)
            Piece pTourG = plateau.getPieceAt(x, 0);
            if (pTourG instanceof Tour tourG && !tourG.hasMoved()
                && plateau.getCase(x,1).getPiece() == null
                && plateau.getCase(x,2).getPiece() == null
                && plateau.getCase(x,3).getPiece() == null) {
                coups.add(plateau.getCase(x, 2));
            }
        }

        return coups;
    }

    private boolean estDansLimites(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    @Override
    public String getNom() {
        return "Roi";
    }

    @Override
    public String getIconPath() {
        return estBlanc ? "C:\\Users\\etulyon1\\Documents\\Chess\\ChessPieces\\white-king.png"
                        : "C:\\Users\\etulyon1\\Documents\\Chess\\ChessPieces\\black-king.png";
    }
}
