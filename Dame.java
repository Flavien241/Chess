import java.util.ArrayList;
import java.util.List;

public class Dame extends Piece {

    public Dame(int x, int y, boolean estBlanc, Damier plateau) {
        super(x, y, estBlanc, plateau);
    }

    @Override
    public List<Case> getCoupsPossibles() {
        List<Case> captures    = new ArrayList<>();
        List<Case> deplacements = new ArrayList<>();
        int[] dirs = { -1, +1 };

        // Pour chaque diagonale
        for (int dx : dirs) {
            for (int dy : dirs) {
                int nx = x + dx, ny = y + dy;
                // 1) déplacement simple
                if (estDansLimites(nx, ny)) {
                    Case c = plateau.getCase(nx, ny);
                    if (!c.estOccupee()) {
                        deplacements.add(c);
                    }
                    // 2) capture par saut
                    else if (c.contientPieceAdverse(estBlanc)) {
                        int cx = x + 2*dx, cy = y + 2*dy;
                        if (estDansLimites(cx, cy) 
                            && !plateau.getCase(cx, cy).estOccupee()) {
                            captures.add(plateau.getCase(cx, cy));
                        }
                    }
                }
            }
        }

        // si capture possible, seules les captures sont autorisées
        return captures.isEmpty() ? deplacements : captures;
    }

    private boolean estDansLimites(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    @Override
    public String getNom() {
        return "Dame";
    }

    @Override
    public String getIconPath() {
        return estBlanc ? "ChessPieces/white-queen.png"
                        : "ChessPieces/black-queen.png";
    }
}
