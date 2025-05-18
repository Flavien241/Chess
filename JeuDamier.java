public interface JeuDamier {
    PlateauEchecs getPlateau();
    boolean estTermine();
    void jouerCoup(int x1, int y1, int x2, int y2);
}
