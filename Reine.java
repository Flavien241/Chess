import java.util.List;

public class Reine extends Piece {

    public Reine(int x, int y, boolean estBlanc, PlateauEchecs plateau) {
        super(x, y, estBlanc, plateau);
        // La reine se déplace en lignes et en diagonales
        this.dca = new DecLigne(new DecDiag());
    }

    @Override
    public List<Case> getCoupsPossibles() {
        // On délègue la logique de déplacement au décorateur
        return dca.getMesCasesAccessibles(this);
    }

    @Override
    public String getNom() {
        return "Reine";
    }

    @Override
    public String getIconPath() {
        return estBlanc ? "C:\\Users\\etulyon1\\Documents\\Chess\\ChessPieces\\white-queen.png"
                        : "C:\\Users\\etulyon1\\Documents\\Chess\\ChessPieces\\black-queen.png";
    }
}
