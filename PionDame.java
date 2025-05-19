import java.util.ArrayList;
import java.util.List;

public class PionDame extends Piece {

    public PionDame(int x, int y, boolean estBlanc, Damier plateau) {
        super(x, y, estBlanc, plateau);
    }

    @Override
    public List<Case> getCoupsPossibles() {
        List<Case> captures = new ArrayList<>();
        List<Case> deplacements = new ArrayList<>();
        int direction = estBlanc ? 1 : -1;

        for (int dy = -1; dy <= 1; dy += 2) {
            int nx = x + direction;
            int ny = y + dy;
            Case simple = plateau.getCase(nx, ny);

            if (simple != null && simple.getPiece() == null) {
                deplacements.add(simple);
            }

            Case diagonale = plateau.getCase(nx, ny);
            if (diagonale != null && diagonale.getPiece() != null &&
                diagonale.getPiece().estBlanc() != estBlanc) {

                int cx = x + 2 * direction;
                int cy = y + 2 * dy;
                Case saut = plateau.getCase(cx, cy);

                if (saut != null && saut.getPiece() == null) {
                    captures.add(saut);
                }
            }
        }

        return captures.isEmpty() ? deplacements : captures;
    }

    @Override
    public String getNom() {
        return "Pion";
    }

    @Override
    public String getIconPath() {
        return estBlanc ? "ChessPieces/white-pawn.png"
                        : "ChessPieces/black-pawn.png";
    }

    public boolean doitEtrePromu() {
        return (estBlanc && x == 7) || (!estBlanc && x == 0);
    }
}
