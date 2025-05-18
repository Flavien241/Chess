import java.util.Observable;
import java.util.Observer;

public class MaVueConsole implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("[Vue Console] Coup joué ou mise à jour du plateau.");
        if (arg instanceof String) {
            System.out.println("➡️ " + arg);
        }
    }
}
