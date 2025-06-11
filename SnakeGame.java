import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;

public class SnakeGame extends JPanel implements ActionListener {

    private final int TILE_SIZE = 25;
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private final int ALL_TILES = (WIDTH * HEIGHT) / (TILE_SIZE * TILE_SIZE);
    private final int[] x = new int[ALL_TILES];
    private final int[] y = new int[ALL_TILES];

    private int snakeLength;
    private ArrayList<Point> foodList = new ArrayList<>();
    private final int MIN_FOOD = 3;
    private final int MAX_FOOD = 7;
    private final int MAX_TOTAL_FOOD = 20;

    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;

    private Timer timer;
    private Timer foodTimer;
    private final int FOOD_SPAWN_INTERVAL = 5000; // 3 seconds

    private Random random;
    private int score = 0;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        initGame();
    }

    public void initGame() {
        snakeLength = 3;
        score = 0;

        for (int i = 0; i < snakeLength; i++) {
            x[i] = 100 - i * TILE_SIZE;
            y[i] = 100;
        }

        random = new Random();
        placeInitialFood();

        timer = new Timer(200, this);
        timer.start();

        foodTimer = new Timer(FOOD_SPAWN_INTERVAL, e -> spawnTimedFood());
        foodTimer.start();
    }

    public void placeInitialFood() {
        foodList.clear();
        int foodCount = MIN_FOOD + random.nextInt(MAX_FOOD - MIN_FOOD + 1);
        for (int i = 0; i < foodCount; i++) {
            spawnSingleFood();
        }
    }

    public void spawnSingleFood() {
        if (foodList.size() >= MAX_TOTAL_FOOD) return;

        int fx, fy;
        Point food;
        do {
            fx = random.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE;
            fy = random.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE;
            food = new Point(fx, fy);
        } while (foodList.contains(food) || isOnSnake(food));

        foodList.add(food);
    }

    public void spawnTimedFood() {
        spawnSingleFood();
        repaint();
    }

    public boolean isOnSnake(Point p) {
        for (int i = 0; i < snakeLength; i++) {
            if (x[i] == p.x && y[i] == p.y) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (inGame) {
            g.setColor(Color.RED);
            for (Point food : foodList) {
                g.fillRect(food.x, food.y, TILE_SIZE, TILE_SIZE);
            }

            for (int i = 0; i < snakeLength; i++) {
                g.setColor(i == 0 ? Color.GREEN : Color.YELLOW);
                g.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);
            }

            // 🧮 Show score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Helvetica", Font.PLAIN, 18));
            g.drawString("Score: " + score, 10, 20);

            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    public void gameOver(Graphics g) {
        String msg = "Game Over!";
        Font font = new Font("Helvetica", Font.BOLD, 30);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(msg, (WIDTH - metrics.stringWidth(msg)) / 2, HEIGHT / 2);

        timer.stop();
        foodTimer.stop();

        int choice = JOptionPane.showOptionDialog(
                this,
                "Game Over!\nYour Score: " + score + "\nWould you like to try again?",
                "Snake Game",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Try Again", "Exit"},
                "Try Again"
        );

        if (choice == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            System.exit(0);
        }
    }

    public void restartGame() {
        inGame = true;
        left = false;
        right = true;
        up = false;
        down = false;
        snakeLength = 3;
        score = 0;

        for (int i = 0; i < snakeLength; i++) {
            x[i] = 100 - i * TILE_SIZE;
            y[i] = 100;
        }

        placeInitialFood();
        timer.restart();
        foodTimer.restart();
        repaint();
    }

    public void checkFoodCollision() {
        for (int i = 0; i < foodList.size(); i++) {
            Point food = foodList.get(i);
            if (x[0] == food.x && y[0] == food.y) {
                snakeLength++;
                score++;
                foodList.remove(i);
                break;
            }
        }
    }

    public void checkCollision() {
        for (int i = snakeLength; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
                return;
            }
        }

        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
            foodTimer.stop();
        }
    }

    public void move() {
        for (int i = snakeLength; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (left) x[0] -= TILE_SIZE;
        else if (right) x[0] += TILE_SIZE;
        else if (up) y[0] -= TILE_SIZE;
        else if (down) y[0] += TILE_SIZE;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            move();
            checkFoodCollision();
            checkCollision();
        }
        repaint();
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && !right) {
                left = true; up = false; down = false;
            } else if ((key == KeyEvent.VK_RIGHT) && !left) {
                right = true; up = false; down = false;
            } else if ((key == KeyEvent.VK_UP) && !down) {
                up = true; left = false; right = false;
            } else if ((key == KeyEvent.VK_DOWN) && !up) {
                down = true; left = false; right = false;
            }
        }
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame gamePanel = new SnakeGame();
        frame.add(gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}