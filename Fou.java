import java.util.List;

public class Fou extends Piece {

    public Fou(int x, int y, boolean estBlanc, PlateauEchecs plateau) {
        super(x, y, estBlanc, plateau);
        // Le fou se déplace uniquement en diagonale, donc base = DecDiag
        this.dca = new DecDiag();
    }

    @Override
    public List<Case> getCoupsPossibles() {
        // On applique la logique décorateur pour les mouvements autorisés
        return dca.getMesCasesAccessibles(this);
    }

    @Override
    public String getNom() {
        return "Fou";
    }

    @Override
    public String getIconPath() {
        return estBlanc ? "ChessPieces/white-bishop.png"
                        : "ChessPieces/black-bishop.png";
    }
}
