import java.util.List;
import java.util.Observable;

public class Jeu extends Observable implements Runnable {
    private Damier damier;
    private boolean tourBlanc = true;
    private boolean partieEnCours = true;
    private boolean dejaSignaleEchec = false;
    private boolean dejaSignaleMat = false;
    private boolean dejaSignalePat = false;

    public Jeu(Damier damier) {
        this.damier = damier;
    }

    public Damier getDamier() {
        return damier;
    }

    @Override
    public void run() {
        while (partieEnCours) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (damier.estFinDePartie(tourBlanc) && !dejaSignaleMat) {
                partieEnCours = false;
                dejaSignaleMat = true;
                String gagnant = !tourBlanc ? "Blancs" : "Noirs";
                setChanged();
                // Nouveau libellé avec "Échec et mat"
                notifyObservers("Échec et mat ! Les " + gagnant + " gagnent !");
            }
            else if (damier instanceof PlateauEchecs ech && ech.estPat(tourBlanc) && !dejaSignalePat) {
                partieEnCours = false;
                dejaSignalePat = true;
                setChanged();
                notifyObservers("Pat ! Égalité générale.");
            }
            else if (damier.estSituationCritique(tourBlanc) && !dejaSignaleEchec) {
                dejaSignaleEchec = true;
                setChanged();
                notifyObservers("Situation critique du joueur " + (tourBlanc ? "blanc" : "noir"));
            }
            else if (!damier.estSituationCritique(tourBlanc)) {
                dejaSignaleEchec = false;
            }
        }
    }

    public synchronized void jouerCoup(int sx, int sy, int ex, int ey) {
        if (!partieEnCours) return;

        Piece piece = damier.getPieceAt(sx, sy);
        if (piece == null) {
            setChanged(); notifyObservers("Aucune pièce à cet endroit."); 
            return;
        }

        // *** Vérification du tour avant toute chose ***
        if (piece.estBlanc() != tourBlanc) {
            setChanged(); 
            notifyObservers("Ce n’est pas votre tour."); 
            return;
        }

        // Détection du roque
        boolean estRoque = piece instanceof Roi && Math.abs(ey - sy) == 2;
        if (estRoque) {
            // coordonnées de la tour concernée
            int tourY = (ey > sy) ? 7 : 0;
            Tour tour = (Tour) damier.getPieceAt(sx, tourY);
            if (tour == null) {
                setChanged(); notifyObservers("Roque impossible : pas de tour."); 
                return;
            }
            // simulation du roque
            int newTourY = (ey > sy) ? ey - 1 : ey + 1;
            // retrait temporaire
            damier.removePieceAt(sx, sy);
            damier.removePieceAt(sx, tourY);
            piece.setPosition(ex, ey);
            tour.setPosition(sx, newTourY);
            damier.setPieceAt(ex, ey, piece);
            damier.setPieceAt(sx, newTourY, tour);

            // interdiction si roi en échec
            if (damier.estSituationCritique(piece.estBlanc())) {
                // restauration
                damier.removePieceAt(ex, ey);
                damier.removePieceAt(sx, newTourY);
                piece.setPosition(sx, sy);
                tour.setPosition(sx, tourY);
                damier.setPieceAt(sx, sy, piece);
                damier.setPieceAt(sx, tourY, tour);

                setChanged();
                notifyObservers("Roque interdit : roi en échec lors du passage.");
                return;
            }

            // tout est OK → valider le roque
            piece.setHasMoved(true);
            tour.setHasMoved(true);
            // bascule de tour
            tourBlanc = !tourBlanc;
            setChanged();
            notifyObservers();  // pour rafraîchir l’UI
            return;
        }

        

        Piece prise = damier.getPieceAt(ex, ey);

        // 1) Simulation du coup
        boolean valid = damier.jouerCoup(sx, sy, ex, ey);
        if (!valid) {
            setChanged();
            notifyObservers("Coup invalide");
            return;
        }

        // 2) Vérification que le roi n’est pas en échec après ce coup
        boolean enEchec = damier.estSituationCritique(piece.estBlanc());
        if (enEchec) {
            // Restauration de l’état initial
            damier.removePieceAt(ex, ey);
            damier.setPieceAt(sx, sy, piece);
            piece.setPosition(sx, sy);
            if (prise != null) {
                damier.setPieceAt(ex, ey, prise);
            }
            setChanged();
            notifyObservers("Coup interdit : mettrait le roi en échec");
            return;
        }

        // 3) Validation définitive
        piece.setHasMoved(true);
        tourBlanc = !tourBlanc;
        setChanged();
        notifyObservers();
        
    }
    


    // Méthodes spécifiques aux Échecs
    public boolean estEchec(boolean blanc) {
        if (!(damier instanceof PlateauEchecs echiquier)) return false;

        Roi roi = echiquier.getRoi(blanc);
        if (roi == null) return false;

        List<Piece> adversaires = echiquier.getToutesLesPieces(!blanc);
        for (Piece p : adversaires) {
            List<Case> coups = p.getCoupsPossibles();
            for (Case c : coups) {
                if (c.getX() == roi.getX() && c.getY() == roi.getY()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean estEchec() {
        return estEchec(tourBlanc);
    }

    public boolean estEchecEtMat(boolean blanc) {
        if (!(damier instanceof PlateauEchecs echiquier)) return false;

        Roi roi = echiquier.getRoi(blanc);
        if (roi == null) return true;

        if (!estEchec(blanc)) return false;

        List<Piece> pieces = echiquier.getToutesLesPieces(blanc);
        for (Piece piece : pieces) {
            List<Case> coups = piece.getCoupsPossibles();
            for (Case cible : coups) {
                int oldX = piece.getX(), oldY = piece.getY();
                Piece temp = damier.getPieceAt(cible.getX(), cible.getY());

                damier.removePieceAt(oldX, oldY);
                damier.setPieceAt(cible.getX(), cible.getY(), piece);
                piece.setPosition(cible.getX(), cible.getY());

                boolean encoreEchec = estEchec(blanc);

                damier.setPieceAt(oldX, oldY, piece);
                piece.setPosition(oldX, oldY);
                damier.setPieceAt(cible.getX(), cible.getY(), temp);

                if (!encoreEchec) return false;
            }
        }
        return true;
    }



    public boolean getTourActuel() {
        return tourBlanc;
    }

    public void stop() {
        partieEnCours = false;
    }

    public boolean isPartieEnCours() {
        return partieEnCours;
    }
}
