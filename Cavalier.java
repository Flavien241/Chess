import java.util.ArrayList;
import java.util.List;

public class Cavalier extends Piece {

    public Cavalier(int x, int y, boolean estBlanc, PlateauEchecs plateau) {
        super(x, y, estBlanc, plateau);
        this.dca = null; // pas de décorateur ici, car le cavalier a une logique spéciale
    }

    @Override
    public List<Case> getCoupsPossibles() {
        List<Case> coups = new ArrayList<>();
        int[][] mouvements = {
            {-2, -1}, {-2, +1},
            {-1, -2}, {-1, +2},
            {+1, -2}, {+1, +2},
            {+2, -1}, {+2, +1}
        };

        for (int[] move : mouvements) {
            int newX = x + move[0];
            int newY = y + move[1];

            if (estDansLimites(newX, newY)) {
                Case cible = plateau.getCase(newX, newY);
                if (!cible.estOccupee() || cible.contientPieceAdverse(estBlanc)) {
                    coups.add(cible);
                }
            }
        }

        return coups;
    }

    private boolean estDansLimites(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    @Override
    public String getNom() {
        return "Cavalier";
    }

    @Override
    public String getIconPath() {
        return estBlanc ? "ChessPieces/white-knight.png"
                        : "ChessPieces/black-knight.png";
    }
}
