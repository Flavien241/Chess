import java.util.ArrayList;
import java.util.List;

public class Dame extends Piece {

    public Dame(int x, int y, boolean estBlanc, Damier plateau) {
        super(x, y, estBlanc, plateau);
    }

    @Override
    public List<Case> getCoupsPossibles() {
        List<Case> captures = new ArrayList<>();
        List<Case> deplacements = new ArrayList<>();
        int[] directions = {-1, 1};

        for (int dx : directions) {
            for (int dy : directions) {
                analyserDirection(dx, dy, captures, deplacements);
            }
        }

        return captures.isEmpty() ? deplacements : captures;
    }

    private void analyserDirection(int dx, int dy, List<Case> captures, List<Case> deplacements) {
        int cx = x + dx;
        int cy = y + dy;
        boolean pieceAdverseRencontree = false;

        while (cx >= 0 && cx < 8 && cy >= 0 && cy < 8) {
            Case c = plateau.getCase(cx, cy);
            if (c.getPiece() == null) {
                if (pieceAdverseRencontree) {
                    captures.add(c); // Case après la pièce adverse
                    break; // Une seule capture autorisée par direction
                } else {
                    deplacements.add(c); // Déplacement normal
                }
            } else {
                if (c.getPiece().estBlanc() == estBlanc) break; // allié = bloqué
                if (pieceAdverseRencontree) break; // déjà une pièce adverse
                pieceAdverseRencontree = true;
            }

            cx += dx;
            cy += dy;
        }
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
