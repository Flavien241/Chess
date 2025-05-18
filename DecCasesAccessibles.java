import java.util.ArrayList;

public abstract class DecCasesAccessibles {
    protected DecCasesAccessibles base;

    public DecCasesAccessibles() {
        this.base = null;
    }

    public DecCasesAccessibles(DecCasesAccessibles base) {
        this.base = base;
    }

    public ArrayList<Case> getMesCasesAccessibles(Piece piece) {
        ArrayList<Case> retour = getCasesAccessibles(piece);
        if (base != null) {
            retour.addAll(base.getMesCasesAccessibles(piece));
        }
        return retour;
    }

    protected abstract ArrayList<Case> getCasesAccessibles(Piece piece);
}
