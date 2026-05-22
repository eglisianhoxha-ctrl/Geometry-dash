package geometrydash;

import java.awt.Rectangle;

public class Platform {
    int x;
    int y;
    int width;
    int height;

    public Platform(int x, int y, int width, int height) {
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
}
