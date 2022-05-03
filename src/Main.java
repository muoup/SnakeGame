import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class Main extends Canvas implements Runnable {
    enum GameState {
        MENU,
        GAME,
        END
    }

    private static final Dimension frameSize = new Dimension(Settings.TILE_SIZE * Settings.GRID_SIZE, Settings.TILE_SIZE * Settings.GRID_SIZE);
    private static Random random = new Random(System.nanoTime());

    private ArrayList<Point> snakePoints = new ArrayList<>();

    private Point fruitPoint;

    private Snake snake;
    private Menu menu;

    private GameState gameState = GameState.MENU;

    private File saveFile;

    private int[] scores = new int[5];
    private String[] names = new String[5];

    public Main() {
        setPreferredSize(frameSize);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Main main = new Main();

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(main);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        main.addKeyListener(new Input());

        Thread thread = new Thread(main);
        thread.start();
    }

    public void run() {
        saveFile = new File("src/data/save.txt");

        readData();

        snake = new Snake(this);
        menu = new Menu(this);

        snake.reset();

        generateFruitPoint();

        while (!snake.isDead()) {
            render();
            update();
        }
    }

    public void update() {
        switch (gameState) {
            case END:
            case MENU:
                menu.update();
                break;
            case GAME:
                snake.update();
                break;
        }
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();

        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        // Render Background
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, (int) frameSize.getWidth(), (int) frameSize.getHeight());

        // Render Grid
        g.setColor(Color.BLACK);
        for (int x = 0; x < Settings.GRID_SIZE; x++) {
            for (int y = 0; y < Settings.GRID_SIZE; y++) {
                g.drawRect(x * Settings.TILE_SIZE, y * Settings.TILE_SIZE,
                        Settings.TILE_SIZE, Settings.TILE_SIZE);
            }
        }

        renderTile(g, Color.RED, fruitPoint);

        switch (gameState) {
            case END:
            case MENU:
                menu.render(g);
                break;
            case GAME:
                snake.render(g);
                break;
        }

        bs.show();
        g.dispose();
    }

    public void renderTile(Graphics g, Color color, Point point) {
        g.setColor(color);
        g.fillRect(Settings.TILE_SIZE * point.x + 1, Settings.TILE_SIZE * point.y + 1, 47, 47);
    }

    public void generateFruitPoint() {
        do {
            fruitPoint = new Point(random.nextInt(Settings.GRID_SIZE), random.nextInt(Settings.GRID_SIZE));
        } while (snake.snakeHas(fruitPoint));
    }

    public void startGame() {
        gameState = GameState.GAME;

        snake.reset();
    }

    public void quit() {
        saveData();
        System.exit(0);
    }

    public void goToMenu() {
        gameState = GameState.MENU;
    }

    public boolean isFruitAt(Point newPoint) {
        return newPoint.equals(fruitPoint);
    }

    public void readData() {
        if (saveFile.isFile()) {
            saveData();

            for (int i = 0; i < 5; i++) {
                names[i] = "N/A";
            }
        } else {
            Scanner scanner = null;

            try {
                scanner = new Scanner(saveFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            int index = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(" ");

                scores[index] = Integer.parseInt(data[0]);
                names[index] = data[1];

                index++;
            }
        }
    }

    public void saveData() {
        try {
            PrintWriter writer = new PrintWriter(saveFile);
            for (int i = 0; i < scores.length; i++) {
                String name = names[i];

                if (name == null) {
                    name = "N/A";
                }

                writer.println(name + " " + scores[i]);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int[] getScores() {
        return scores;
    }

    public String[] getNames() {
        return names;
    }

    public void submitScore(int score) {
        if (score < scores[4])
            return;

        String name = "";

        do {
            if (name.length() > 8) {
                JOptionPane.showMessageDialog(null, "Name is too long! Please keep it under 8 characters.");
            }
            name = JOptionPane.showInputDialog(null, "New High Score!\nEnter your name:", "Score", JOptionPane.PLAIN_MESSAGE);

        } while (name.contains("N/A") || name.length() > 8);

        for (int i = 0; i < scores.length; i++) {
            if (score > scores[i]) {
                for (int j = scores.length - 1; j > i; j--) {
                    scores[j] = scores[j - 1];
                    names[j] = names[j - 1];
                }
                scores[i] = score;
                names[i] = name;

                return;
            }
        }

        saveData();
    }
}
