package geometrydash;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Color;

public class Obstacle {
    int x;
    int y;
    int width;
    int height;

    public Obstacle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void move(int dx) {
        x -= dx;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getRight() {
        return x + width;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(new Color(239, 68, 68));
        Polygon spike = new Polygon();
        spike.addPoint(x, y + height);
        spike.addPoint(x + width / 2, y);
        spike.addPoint(x + width, y + height);
        g2.fill(spike);
        g2.setColor(Color.WHITE);
        g2.draw(spike);
    }
}
