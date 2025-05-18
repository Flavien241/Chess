import java.util.List;
import java.util.Observable;

public class Jeu extends Observable implements Runnable {
    private Damier damier;
    private boolean tourBlanc = true;
    private boolean partieEnCours = true;
    private boolean dejaSignaleEchec = false;
    private boolean dejaSignaleMat = false;

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
                Thread.sleep(200); // évite saturation CPU
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (damier.estFinDePartie(tourBlanc) && !dejaSignaleMat) {
                partieEnCours = false;
                dejaSignaleMat = true;
                String gagnant = !tourBlanc ? "Blancs" : "Noirs";
                setChanged();
                notifyObservers("Fin de partie. " + gagnant + " gagnent !");
            } else if (damier.estSituationCritique(tourBlanc) && !dejaSignaleEchec) {
                dejaSignaleEchec = true;
                setChanged();
                notifyObservers("Situation critique du joueur " + (tourBlanc ? "blanc" : "noir"));
            } else if (!damier.estSituationCritique(tourBlanc)) {
                dejaSignaleEchec = false;
            }
            
        }
    }

    public synchronized void jouerCoup(int startX, int startY, int endX, int endY) {
        if (!partieEnCours) return;
    
        Piece piece = damier.getPieceAt(startX, startY);
    
        if (piece == null) {
            setChanged();
            notifyObservers("Aucune pièce à cet endroit.");
            return;
        }
    
        if (piece.estBlanc() != tourBlanc) {
            setChanged();
            notifyObservers("Ce n’est pas votre tour.");
            return;
        }
    
        // ✅ On délègue au Damier (PlateauEchecs ou PlateauDames)
        boolean coupValide = damier.jouerCoup(startX, startY, endX, endY);
    
        if (coupValide) {
            tourBlanc = !tourBlanc;
            setChanged();
            notifyObservers(); // rafraîchir l’affichage
        } else {
            setChanged();
            notifyObservers("Coup invalide");
        }
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
