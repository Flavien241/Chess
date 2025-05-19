import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Observer;
import java.util.Observable;

public class MaFenetre extends JFrame implements Observer {
    private JLabel[][] labels = new JLabel[8][8];
    private Jeu jeu;
    private Case caseSelectionnee = null;

    public MaFenetre(Jeu jeu) {
        this.jeu = jeu;
        this.jeu.addObserver(this);

        buildInterface();
        setTitle("Jeu de damier - Vue");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void buildInterface() {
        JPanel panel = new JPanel(new GridLayout(8, 8));
        setContentPane(panel);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JLabel label = new JLabel("", SwingConstants.CENTER);
                label.setOpaque(true);
                label.setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.DARK_GRAY);
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                labels[i][j] = label;

                final int x = i, y = j;
                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        gererClic(x, y);
                    }
                });

                panel.add(label);
            }
        }

        rafraichirAffichage();
    }

    private void gererClic(int x, int y) {
        if (!jeu.isPartieEnCours()) return;

        Damier damier = jeu.getDamier();

        if (caseSelectionnee == null) {
            caseSelectionnee = damier.getCase(x, y);
            afficherDeplacementsPossibles(caseSelectionnee);
        } else {
            jeu.jouerCoup(caseSelectionnee.getX(), caseSelectionnee.getY(), x, y);
            caseSelectionnee = null;
            rafraichirAffichage();
        }
    }

    private boolean coupLegal(Piece piece, Case cible) {
        // En mode Échecs : on simule pour interdire les coups qui laissent le roi en échec
        if (jeu.getDamier() instanceof PlateauEchecs pe) {
            int oldX = piece.getX(), oldY = piece.getY();
            Piece prise = pe.getPieceAt(cible.getX(), cible.getY());

            // Simulation du coup
            pe.removePieceAt(oldX, oldY);
            pe.setPieceAt(cible.getX(), cible.getY(), piece);
            piece.setPosition(cible.getX(), cible.getY());

            // Test : roi toujours attaqué ?
            boolean enEchec = pe.estSituationCritique(piece.estBlanc());

            // Reprise de la situation initiale
            pe.removePieceAt(cible.getX(), cible.getY());
            pe.setPieceAt(oldX, oldY, piece);
            piece.setPosition(oldX, oldY);
            if (prise != null) {
                pe.setPieceAt(cible.getX(), cible.getY(), prise);
            }

            return !enEchec;
        }

        // En mode Dames (ou tout autre Damier) : on autorise tous les coups légaux listés
        return true;
    }

    private void afficherDeplacementsPossibles(Case c) {
        resetCouleurs();
        if (c.getPiece() != null) {
            for (Case cible : c.getPiece().getCoupsPossibles()) {
                if (coupLegal(c.getPiece(), cible)) {
                    labels[cible.getX()][cible.getY()].setBackground(Color.GREEN);
                }
            }
        }
    }

    private void resetCouleurs() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                labels[i][j].setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.DARK_GRAY);
    }

    private void rafraichirAffichage() {
        resetCouleurs();
        Damier damier = jeu.getDamier();
    
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = damier.getPieceAt(x, y);
                JLabel label = labels[x][y];
    
                if (piece != null) {
                    ImageIcon icon = new ImageIcon(piece.getIconPath());
                    Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    label.setIcon(new ImageIcon(img));
                } else {
                    label.setIcon(null); // ← OBLIGATOIRE pour faire disparaître une pièce capturée
                }
            }
        }
    
        if (damier instanceof PlateauEchecs && jeu.estEchec()) {
            Roi roi = trouverRoiAttaque();
            if (roi != null) {
                labels[roi.getX()][roi.getY()].setBackground(Color.RED);
            }
        }
    }
    

    private Roi trouverRoiAttaque() {
        Damier damier = jeu.getDamier();
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                Piece p = damier.getPieceAt(i, j);
                if (p instanceof Roi && p.estBlanc() == jeu.getTourActuel()) {
                    return (Roi) p;
                }
            }
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        rafraichirAffichage();

        if (arg instanceof String message) {
            if (message.contains("Échec et mat")) {
                JOptionPane.showMessageDialog(this, message, "🎉 Fin de la partie", JOptionPane.INFORMATION_MESSAGE);
                jeu.stop();
                dispose();
            } else if (message.contains("Pat")) {
                JOptionPane.showMessageDialog(this, message, "♟ Pat - Égalité", JOptionPane.INFORMATION_MESSAGE);
                jeu.stop();
                dispose();
            } else if (message.contains("Échec")) {
                setTitle("⚠️ " + message);
            } else if (message.contains("Coup invalide")) {
                JOptionPane.showMessageDialog(this, "Ce coup n'est pas autorisé.", "Coup invalide", JOptionPane.WARNING_MESSAGE);
            } else if (message.contains("Ce n’est pas votre tour")) {
                JOptionPane.showMessageDialog(this, message, "Tour du joueur adverse", JOptionPane.WARNING_MESSAGE);
            } else if (message.contains("Aucune pièce")) {
                JOptionPane.showMessageDialog(this, message, "Sélection invalide", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
