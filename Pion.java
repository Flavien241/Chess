import java.util.ArrayList;
import java.util.List;

public class Pion extends Piece {
    private boolean premierCoup = true;

    public Pion(int x, int y, boolean estBlanc, PlateauEchecs plateau) {
        super(x, y, estBlanc, plateau);
    }

    @Override
    public List<Case> getCoupsPossibles() {
        List<Case> coups = new ArrayList<>();
        int direction = estBlanc ? -1 : 1; // Les blancs montent (vers le haut), noirs descendent

        int nextX = x + direction;

        // Avancer tout droit
        if (estDansLimites(nextX, y) && !plateau.getCase(nextX, y).estOccupee()) {
            coups.add(plateau.getCase(nextX, y));

            // Si c'est le premier coup, possibilité d'avancer de deux cases
            int twoStepsX = x + 2 * direction;
            if (premierCoup && estDansLimites(twoStepsX, y) && !plateau.getCase(twoStepsX, y).estOccupee()) {
                coups.add(plateau.getCase(twoStepsX, y));
            }
        }

        // Captures diagonales
        int[] dy = {-1, 1};
        for (int d : dy) {
            int diagY = y + d;
            if (estDansLimites(nextX, diagY)) {
                Case caseCible = plateau.getCase(nextX, diagY);
                if (caseCible.estOccupee() && caseCible.contientPieceAdverse(estBlanc)) {
                    coups.add(caseCible);
                }
            }
        }

        return coups;
    }

    @Override
    public void setHasMoved(boolean moved) {
        super.setHasMoved(moved);
        if (moved) {
            premierCoup = false;
        }
    }

    private boolean estDansLimites(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    @Override
    public String getNom() {
        return "Pion";
    }

    @Override
    public String getIconPath() {
        return estBlanc ? "C:\\Users\\etulyon1\\Documents\\Chess\\ChessPieces\\white-pawn.png"
                        : "C:\\Users\\etulyon1\\Documents\\Chess\\ChessPieces\\black-pawn.png";
    }


}
