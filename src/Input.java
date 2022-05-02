import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

public class Input implements KeyListener {

    public static HashSet<Integer> keysDown = new HashSet<>();

    public static boolean getKeyDown(int keyCode) {
        return keysDown.contains(keyCode);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keysDown.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keysDown.remove(e.getKeyCode());
    }
}
