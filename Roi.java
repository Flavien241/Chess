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
        return estBlanc ? "C:\\Users\\flavi\\Downloads\\ChessPieces\\white-king.png"
                        : "C:\\Users\\flavi\\Downloads\\ChessPieces\\black-king.png";
    }
}
