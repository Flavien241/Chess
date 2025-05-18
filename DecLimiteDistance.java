import java.util.ArrayList;

public class DecLimiteDistance extends DecCasesAccessibles {
    private int maxDistance;

    public DecLimiteDistance(DecCasesAccessibles base, int maxDistance) {
        super(base);
        this.maxDistance = maxDistance;
    }

    @Override
    protected ArrayList<Case> getCasesAccessibles(Piece piece) {
        ArrayList<Case> toutes = base.getMesCasesAccessibles(piece);
        ArrayList<Case> limitees = new ArrayList<>();

        int x0 = piece.getX(), y0 = piece.getY();

        for (Case c : toutes) {
            int dx = Math.abs(c.getX() - x0);
            int dy = Math.abs(c.getY() - y0);
            if (Math.max(dx, dy) <= maxDistance) {
                limitees.add(c);
            }
        }

        return limitees;
    }
}
