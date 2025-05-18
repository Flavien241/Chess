import java.util.ArrayList;
import java.util.List;

public class PlateauDames extends Damier {

    public PlateauDames() {
        super();
        initialiser();
    }

    @Override
    public void initialiser() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                cases[i][j] = new Case(i, j, null);

        placerPions(true, 0, 3);   // Blancs
        placerPions(false, 5, 8);  // Noirs
    }

    private void placerPions(boolean blanc, int debut, int fin) {
        for (int i = debut; i < fin; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 != 0)
                    cases[i][j].setPiece(new PionDame(i, j, blanc, this));
            }
        }
    }

    @Override
    public boolean jouerCoup(int startX, int startY, int endX, int endY) {
        Piece piece = getPieceAt(startX, startY);
        if (piece == null) return false;

        int maxCaptures = getMaxCaptures(piece.estBlanc());

        for (Case cible : piece.getCoupsPossibles()) {
            if (cible.getX() == endX && cible.getY() == endY) {
                int dx = Math.abs(endX - startX);
                int thisCaptureCount = dx > 1 ? dx / 2 : 0;

                if (maxCaptures > 0 && thisCaptureCount < maxCaptures) return false;

                appliquerCoup(piece, startX, startY, endX, endY);
                return true;
            }
        }

        return false;
    }

    private void appliquerCoup(Piece piece, int startX, int startY, int endX, int endY) {
        int dx = Math.abs(endX - startX);
        int dy = Math.abs(endY - startY);

        removePieceAt(startX, startY);
        setPieceAt(endX, endY, piece);
        piece.setPosition(endX, endY);

        // Si capture, supprimer la ou les pièces au milieu
        if (dx > 1 && dy > 1) {
            int stepX = (endX - startX) / dx;
            int stepY = (endY - startY) / dy;

            for (int i = 1; i < dx; i++) {
                int midX = startX + i * stepX;
                int midY = startY + i * stepY;
                Piece prise = getPieceAt(midX, midY);

                if (prise != null && prise.estBlanc() != piece.estBlanc()) {
                    removePieceAt(midX, midY);
                }
            }
        }

        // Promotion
        if (piece instanceof PionDame pion && pion.doitEtrePromu()) {
            setPieceAt(endX, endY, new Dame(endX, endY, piece.estBlanc(), this));
        }
    }

    public List<Piece> getToutesLesPieces(boolean blanc) {
        List<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                Piece p = getPieceAt(i, j);
                if (p != null && p.estBlanc() == blanc)
                    pieces.add(p);
            }
        return pieces;
    }

    private int getMaxCaptures(boolean blanc) {
        int max = 0;
        for (Piece p : getToutesLesPieces(blanc)) {
            for (Case cible : p.getCoupsPossibles()) {
                int dx = Math.abs(cible.getX() - p.getX());
                if (dx > 1) {
                    max = Math.max(max, dx / 2); // 2 cases = 1 capture, 4 = 2 captures, etc.
                }
            }
        }
        return max;
    }
    
    @Override
    public boolean estSituationCritique(boolean blanc) {
        return false; // Pas d’échec aux dames
    }

    @Override
    public boolean estFinDePartie(boolean blanc) {
        return getToutesLesPieces(blanc).isEmpty();
    }

}
