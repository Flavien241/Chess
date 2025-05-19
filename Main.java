import javax.swing.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Choisissez un jeu :");
        System.out.println("1. Échecs");
        System.out.println("2. Dames");
        System.out.print("Votre choix : ");

        Scanner scanner = new Scanner(System.in);
        int choix = scanner.nextInt();
        Damier damier;

        if (choix == 1) {
            damier = new PlateauEchecs();
        } else if (choix == 2) {
            damier = new PlateauDames();
        } else {
            System.out.println("Choix invalide.");
            return;
        }

    

        Jeu jeu = new Jeu(damier);
        new Thread(jeu).start();

        SwingUtilities.invokeLater(() -> new MaFenetre(jeu));
    }
}
