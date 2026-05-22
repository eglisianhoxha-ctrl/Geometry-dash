package geometrydash;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 480;
    private static final int GROUND_Y = 420;
    private static final int PLAYER_SIZE = 50;

    private Timer timer;
    private Rectangle player;
    private double velocityY;
    private boolean jumping;
    private boolean gameOver;
    private int score;
    private int scrollSpeed;

    private ArrayList<Platform> platforms;
    private ArrayList<Obstacle> obstacles;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(17, 24, 39));
        setFocusable(true);
        addKeyListener(this);
        initGame();
    }

    private void initGame() {
        player = new Rectangle(100, GROUND_Y - PLAYER_SIZE, PLAYER_SIZE, PLAYER_SIZE);
        velocityY = 0;
        jumping = false;
        gameOver = false;
        score = 0;
        scrollSpeed = 6;

        platforms = new ArrayList<>();
        obstacles = new ArrayList<>();

        platforms.add(new Platform(0, GROUND_Y, WIDTH, 60));
        platforms.add(new Platform(400, GROUND_Y - 20, 120, 20));
        platforms.add(new Platform(700, GROUND_Y - 50, 100, 20));

        obstacles.add(new Obstacle(600, GROUND_Y - 30, 20, 30));

        timer = new Timer(16, this);
    }

    public void startGame() {
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            updateGame();
        }
        repaint();
    }

    private void updateGame() {
        score++;
        velocityY += 0.8;
        player.y += (int) velocityY;

        if (player.y + player.height >= GROUND_Y) {
            player.y = GROUND_Y - player.height;
            velocityY = 0;
            jumping = false;
        }

        for (Platform platform : platforms) {
            platform.move(scrollSpeed);
        }

        for (Obstacle obstacle : obstacles) {
            obstacle.move(scrollSpeed);
        }

        if (!platforms.isEmpty() && platforms.get(0).getRight() < 0) {
            platforms.remove(0);
        }

        if (!obstacles.isEmpty() && obstacles.get(0).getRight() < 0) {
            obstacles.remove(0);
        }

        if (platforms.isEmpty() || platforms.get(platforms.size() - 1).getRight() < WIDTH) {
            addPlatform();
        }

        if (obstacles.isEmpty() || obstacles.get(obstacles.size() - 1).getRight() < WIDTH - 240) {
            addObstacle();
        }

        if (player.y + player.height > HEIGHT) {
            gameOver = true;
            timer.stop();
        }

        if (checkPlatformCollision()) {
            gameOver = true;
            timer.stop();
        }

        if (checkObstacleCollision()) {
            gameOver = true;
            timer.stop();
        }
    }

    private boolean checkPlatformCollision() {
        for (int i = 1; i < platforms.size(); i++) {
            Platform platform = platforms.get(i);
            Rectangle bounds = platform.getBounds();
            if (player.intersects(bounds)) {
                if (player.y + player.height - 6 <= platform.y) {
                    player.y = platform.y - player.height;
                    velocityY = 0;
                    jumping = false;
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    private boolean checkObstacleCollision() {
        for (Obstacle obstacle : obstacles) {
            if (player.intersects(obstacle.getBounds())) {
                return true;
            }
        }
        return false;
    }

    private void addPlatform() {
        int gap = 120 + (int) (Math.random() * 140);
        int width = 80 + (int) (Math.random() * 80);
        int height = 20;
        int y = GROUND_Y - 20 - (int) (Math.random() * 160);
        if (y < GROUND_Y - 220) {
            y = GROUND_Y - 220;
        }

        int lastX = platforms.get(platforms.size() - 1).x + platforms.get(platforms.size() - 1).width;
        platforms.add(new Platform(WIDTH + gap, y, width, height));
    }

    private void addObstacle() {
        int gap = 200 + (int) (Math.random() * 120);
        int x = WIDTH + gap;
        int y = GROUND_Y - 30;
        obstacles.add(new Obstacle(x, y, 20, 30));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(30, 41, 59));
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        g2.setColor(new Color(148, 163, 184));
        for (int x = 0; x < WIDTH; x += 40) {
            g2.drawLine(x, GROUND_Y, x + 20, GROUND_Y);
        }

        g2.setColor(new Color(59, 130, 246));
        g2.fillRect(player.x, player.y, player.width, player.height);

        g2.setColor(new Color(16, 185, 129));
        for (Platform platform : platforms) {
            g2.fillRect(platform.x, platform.y, platform.width, platform.height);
        }

        g2.setColor(new Color(239, 68, 68));
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(g2);
        }

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("Score: " + score / 10, 20, 30);

        if (gameOver) {
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            g2.drawString("GAME OVER", WIDTH / 2 - 140, HEIGHT / 2 - 20);
            g2.setFont(new Font("Arial", Font.PLAIN, 18));
            g2.drawString("Press R to restart", WIDTH / 2 - 100, HEIGHT / 2 + 20);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if ((e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) && !jumping && !gameOver) {
            jumping = true;
            velocityY = -14;
        }

        if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            initGame();
            timer.start();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
