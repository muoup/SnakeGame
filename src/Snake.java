import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Snake {
    enum Direction {
        up, down, left, right
    }

    private Main main; // Instance of the main class

    private Direction direction = Direction.up; // Where will the snake move in the next move cycle?
    public ArrayList<Point> snakePoints;
    private long lastMoveTime = System.currentTimeMillis();
    private boolean dead = false;

    public Snake(Main main) {
        this.snakePoints = new ArrayList<>();
        this.main = main;
    }

    public void render(Graphics g) {
        g.setColor(Color.GREEN);

        for (Point p : snakePoints) {
            g.fillRect(Settings.TILE_SIZE * p.x + 1, Settings.TILE_SIZE * p.y + 1,
                    Settings.TILE_SIZE - 1, Settings.TILE_SIZE - 1);
        }
    }

    public void update() {
        detectDirectionChange();

        if (System.currentTimeMillis() - lastMoveTime < Settings.moveTime(snakePoints.size()))
            return;

        lastMoveTime = System.currentTimeMillis();

        Point newPoint = (Point) snakePoints.get(snakePoints.size() - 1).clone();

        switch (direction) {
            case up:
                newPoint.y = (newPoint.y - 1);
                break;
            case down:
                newPoint.y = (newPoint.y + 1);
                break;
            case left:
                newPoint.x = (newPoint.x - 1);
                break;
            case right:
                newPoint.x = (newPoint.x + 1);
                break;
        }

        newPoint.y %= Settings.GRID_SIZE;
        newPoint.x %= Settings.GRID_SIZE;

        if (newPoint.x < 0)
            newPoint.x += Settings.GRID_SIZE;
        if (newPoint.y < 0)
            newPoint.y += Settings.GRID_SIZE;

        if (main.isFruitAt(newPoint)) {
            main.generateFruitPoint();
        }
        else snakePoints.remove(0);

        if (snakeHas(newPoint))
            die();

        snakePoints.add(newPoint);
    }

    public void detectDirectionChange() {
        if (Input.getKeyDown(KeyEvent.VK_UP) && (direction != Direction.down || snakePoints.size() == 1))
            direction = Direction.up;
        else if (Input.getKeyDown(KeyEvent.VK_DOWN) && (direction != Direction.up || snakePoints.size() == 1))
            direction = Direction.down;
        else if (Input.getKeyDown(KeyEvent.VK_RIGHT) && (direction != Direction.right || snakePoints.size() == 1))
            direction = Direction.right;
        else if (Input.getKeyDown(KeyEvent.VK_LEFT) && (direction != Direction.left || snakePoints.size() == 1))
            direction = Direction.left;
    }

    public void reset() {
        snakePoints.clear();
        snakePoints.add(new Point(5, 5));

        main.generateFruitPoint();
    }

    public void die() {
        JOptionPane.showMessageDialog(null, "You died!");
        main.submitScore(snakePoints.size());
        main.goToMenu();
    }

    // -- Getters and Setters -- //

    public boolean snakeHas(Point p) {
        for (Point point : snakePoints) {
            if (p.equals(point))
                return true;
        }

        return false;
    }

    public boolean isDead() {
        return dead;
    }
}
