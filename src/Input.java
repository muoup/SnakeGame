import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Input implements KeyListener {
    enum KeyState {
        RELEASED, PRESSED, HELD
    }

    private static KeyState[] keys = new KeyState[65536];

    public static boolean getKey(int keyCode) {
        if (keys[keyCode] == KeyState.PRESSED || keys[keyCode] == KeyState.HELD) {
            keys[keyCode] = KeyState.HELD;
            return true;
        }

        return false;
    }

    public static boolean getKeyDown(int keyCode) {
        if (keys[keyCode] == KeyState.PRESSED) {
            keys[keyCode] = KeyState.HELD;
            return true;
        }

        return false;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = KeyState.PRESSED;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = KeyState.RELEASED;
    }
}
