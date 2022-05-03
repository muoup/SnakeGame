import java.awt.*;
import java.awt.event.KeyEvent;

public class Menu {
    private Main main;

    private boolean renderScores = false;

    private final String[] buttons = {"Start", "High Scores", "Quit"};

    private int selectedIndex = 0;

    public Menu(Main main) {
        this.main = main;
    }

    public void render(Graphics g) {
        // Render the menu
        g.setColor(Color.BLACK);
        g.fillRect(100, 100, main.getWidth() - 200, main.getHeight() - 200);

        g.setColor(new Color(133, 104, 53, 255));
        g.fillRect(105, 105, main.getWidth() - 210, main.getHeight() - 210);

        // Draw the title
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 50));

        String header = (renderScores) ? "High Scores" : "Snake";
        g.drawString(header, (main.getWidth() - g.getFontMetrics().stringWidth(header)) / 2, 200);

        // Draw the buttons
        g.setFont(new Font("Arial", Font.BOLD, 30));

        if (renderScores) {
            int[] scores = main.getScores();
            String[] names = main.getNames();

            for (int i = 0; i < scores.length; i++) {
                String text = (i + 1) + ". " + names[i] + ": " + scores[i];

                if (names[i].equals("N/A") || names[i].equals("")) {
                    text = (i + 1) + ".";
                }

                g.setColor(Color.BLACK);
                g.drawString(text, (main.getWidth() - g.getFontMetrics().stringWidth(text)) / 2, 275 + (i * 50));
            }

            String exitText = "Press Escape or Enter to return";
            g.drawString(exitText, (main.getWidth() - g.getFontMetrics().stringWidth(exitText)) / 2, main.getHeight() - 150);
        } else {
            for (int i = 0; i < buttons.length; i++) {
                String button = buttons[i];

                if (i == selectedIndex) {
                    button = "> " + button + " <";
                }

                g.drawString(button, (main.getWidth() - g.getFontMetrics().stringWidth(button)) / 2, 400 + 50 * i);
            }
        }
    }

    public void update() {
        if (renderScores) {
            if (Input.getKeyDown(KeyEvent.VK_ESCAPE) || Input.getKeyDown(KeyEvent.VK_ENTER)) {
                renderScores = false;
            }

            return;
        }

        if (Input.getKeyDown(KeyEvent.VK_DOWN)) {
            selectedIndex++;
        } else if (Input.getKeyDown(KeyEvent.VK_UP)) {
            selectedIndex--;
        }

        if (selectedIndex < 0) {
            selectedIndex = buttons.length - 1;
        } else if (selectedIndex >= buttons.length) {
            selectedIndex = 0;
        }

        if (Input.getKeyDown(KeyEvent.VK_ENTER)) {
            onAction(buttons[selectedIndex]);
        }
    }

    public void onAction(String button) {
        switch (button) {
            case "Start":
                main.startGame();
                break;
            case "High Scores":
                renderScores = true;
                break;
            case "Quit":
                main.quit();
                break;
        }
    }
}
