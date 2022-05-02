import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Main extends Canvas implements Runnable {
    enum Direction {
        up, down, left, right
    }

    private static int[][] grid = {
            {
                -1, -1, -1, -1
            },
            {
                -1, -1, -1, -1
            }
    };

    private static final Dimension frameSize = new Dimension(768, 768);
    private static Random random = new Random(System.nanoTime());

    private ArrayList<Point> snakePoints = new ArrayList<>();

    private Direction direction = Direction.up;

    private long lastMoveTime = System.currentTimeMillis();

    private Point fruitPoint;

    private boolean dead = false;

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
        snakePoints.add(new Point(5, 5));
        generateFruitPoint();

        while (!dead) {
            update();
            render();
        }
    }

    public void update() {
        if (Input.getKeyDown(KeyEvent.VK_UP) && direction != Direction.down)
            direction = Direction.up;
        else if (Input.getKeyDown(KeyEvent.VK_DOWN) && direction != Direction.up)
            direction = Direction.down;
        else if (Input.getKeyDown(KeyEvent.VK_RIGHT) && direction != Direction.right)
            direction = Direction.right;
        else if (Input.getKeyDown(KeyEvent.VK_LEFT) && direction != Direction.left)
            direction = Direction.left;

        if (System.currentTimeMillis() - lastMoveTime < 125 + 375 / Math.sqrt(snakePoints.size()))
            return;

        lastMoveTime = System.currentTimeMillis();

        Point newPoint = (Point) snakePoints.get(snakePoints.size() - 1).clone();

        switch (direction) {
            case up:
                newPoint.y = (newPoint.y - 1) % 16;
                break;
            case down:
                newPoint.y = (newPoint.y + 1) % 16;
                break;
            case left:
                newPoint.x = (newPoint.x - 1) % 16;
                break;
            case right:
                newPoint.x = (newPoint.x + 1) % 16;
                break;
        }

        if (newPoint.x < 0)
            newPoint.x += 16;
        if (newPoint.y < 0)
            newPoint.y += 16;

        if (newPoint.x == fruitPoint.x && newPoint.y == fruitPoint.y) {
            generateFruitPoint();
        }
        else snakePoints.remove(0);

        if (snakeHas(newPoint))
            dead = true;

        snakePoints.add(newPoint);
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
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                g.drawRect(x * 48, y * 48, 48, 48);
            }
        }

        g.setColor(Color.GREEN);
        snakeRender(g);

        g.setColor(Color.RED);
        g.fillRect(48 * fruitPoint.x + 1, 48 * fruitPoint.y + 1, 47, 47);

        bs.show();
        g.dispose();
    }

    private void snakeRender(Graphics g) {
        for (Point p : snakePoints) {
            g.fillRect(48 * p.x + 1, 48 * p.y + 1, 47, 47);
        }
    }

    private void generateFruitPoint() {
        do {
            fruitPoint = new Point(random.nextInt(16), random.nextInt(16));
        } while (snakePoints.contains(fruitPoint));
    }

    private boolean snakeHas(Point p) {
        for (Point point : snakePoints) {
            if (p.x == point.x && point.y == p.y)
                return true;
        }

        return false;
    }

    private boolean equals(Point p1, Point p2) {
        return p1.x == p2.x && p1.y == p2.y;
    }
}
