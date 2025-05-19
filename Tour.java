import java.util.List;

public class Tour extends Piece {

    public Tour(int x, int y, boolean estBlanc, PlateauEchecs plateau) {
        super(x, y, estBlanc, plateau);
        this.dca = new DecLigne(); // Le décorateur pour les déplacements en ligne
    }

    @Override
    public List<Case> getCoupsPossibles() {
        return dca.getMesCasesAccessibles(this); // Délègue au décorateur
    }

    @Override
    public String getNom() {
        return "Tour";
    }

    @Override
    public String getIconPath() {
        return estBlanc ? "ChessPieces/white-rook.png"
                        : "ChessPieces/black-rook.png";
    }
}
