package com.codeforall.online.game.entities;

import com.codeforall.online.game.gameobjects.Pallets;
import com.codeforall.simplegraphics.graphics.Color;
import com.codeforall.simplegraphics.graphics.Ellipse;
import com.codeforall.online.game.Grid.Grid;

import java.util.Iterator;
import java.util.Random;

public abstract class BasePlayer {

    // Position and movement
    protected double x;
    protected double y;       // current position
    protected double dx, dy;     // movement direction (normalized)

    protected double radius;
    protected boolean alive = true;
    protected Ellipse lastShape; // reference to last drawn shape (for deletion)
    protected final Color playerColor;

    // Movement constants
    private static final double BASE_SPEED = 1800.0;
    private static final double SPEED_RADIUS_FLOOR = 12.0;

    // Constructor (position x, position y, size) and sets color
    public BasePlayer(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.playerColor = getRandomColor();
    }

    // Random color generator
    private Color getRandomColor() {
        Random rand = new Random();
        return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }

    // Sets movement direction (normalized vector)
    public void setDirection(double dx, double dy) {
        double len = Math.hypot(dx, dy);
        this.dx = (len < 1e-6) ? 0 : dx / len;
        this.dy = (len < 1e-6) ? 0 : dy / len;
    }

    // Decrease Speed when growing
    protected double getSpeed() {
        return BASE_SPEED / Math.max(SPEED_RADIUS_FLOOR, radius);
    }

    // Updates position based on direction and speed, keeping inside game boundaries
    public void updatePosition(double dt, int worldW, int worldH) {
        double v = getSpeed();
        x += dx * v * dt;
        y += dy * v * dt;

        int pad = Grid.PADDING;
        x = Math.max(pad + radius, Math.min(worldW - radius, x));
        y = Math.max(pad + radius, Math.min(worldH - radius, y));
    }

    // Draws the player constantly
    public void draw() {
        if (lastShape != null) lastShape.delete(); // remove previous shape

        int d = (int) Math.round(radius * 2);
        int sx = (int) Math.round(x - radius);
        int sy = (int) Math.round(y - radius);

        Ellipse circle = new Ellipse(sx, sy, d, d);
        circle.setColor(playerColor);
        circle.fill();
        lastShape = circle;
    }

    // Increases player size
    public void growByArea(double areaToAdd) {
        double myArea = Math.PI * radius * radius;
        myArea += areaToAdd;
        radius = Math.sqrt(myArea / Math.PI);
    }

    // Removes player from screen when dead
    public void delete() {
        alive = false;
        if (lastShape != null) lastShape.delete();
    }


    public boolean isAlive() {
        return alive;
    }

    // getters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }

    // Abstract method to be implemented by subclasses (Player, BotPlayer)
    public abstract void update(double dt, int worldW, int worldH);

    // Player - Pallets collision
    public int checkPalletCollision(Pallets pallets) {
        int points = 0;
        Iterator<Ellipse> it = pallets.getPallets().iterator();

        while (it.hasNext()) {
            Ellipse ellipse = it.next();

            if (checkCollision(this, ellipse)) {
                double palletRadius = ellipse.getWidth() / 2.0;
                double palletArea = Math.PI * palletRadius * palletRadius;

                growByArea(palletArea); // increase size
                ellipse.delete();       // remove from screen
                it.remove();            // remove from list
                if (this instanceof com.codeforall.online.game.entities.Player) {
                    points += 1;            //da pontos se quem comeu foi o Player e nao o bot
                }
            }
        }
        return points;
    }

    private boolean checkCollision(BasePlayer a, Ellipse b) {
        double ax = a.getX();
        double ay = a.getY();
        double bx = b.getX() + b.getWidth() / 2.0;
        double by = b.getY() + b.getWidth() / 2.0;

        double dx = ax - bx;
        double dy = ay - by;
        double distance = Math.sqrt(dx * dx + dy * dy);

        double radiusA = a.getRadius();
        double radiusB = b.getWidth() / 2.0;

        return distance < (radiusA + radiusB);
    }

    // Player - player collision
    // The larger player "eats" the smaller one, converting its area into pallets
    public int checkPlayerCollision(BasePlayer other, Pallets pallets) {
        if (!this.isAlive() || !other.isAlive()) return 0;

        double dx = this.getX() - other.getX();
        double dy = this.getY() - other.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        double radiusSum = this.getRadius() + other.getRadius();

        if (distance < radiusSum) {
            // Determine winner and loser based on size
            BasePlayer winner = (this.getRadius() >= other.getRadius()) ? this : other;
            BasePlayer loser = (winner == this) ? other : this;

            double loserArea = Math.PI * loser.getRadius() * loser.getRadius();
            loser.delete(); // remove loser from game

            // Convert loser’s area into multiple pallets around its position
            double palletRadius = 5.0;
            double palletArea = Math.PI * palletRadius * palletRadius;
            int numPallets = (int) (loserArea / palletArea);

            double centerX = loser.getX();
            double centerY = loser.getY();

            for (int i = 0; i < numPallets; i++) {
                double angle = Math.random() * 2 * Math.PI;
                double spread = Math.random() * 30; // spread within 30px radius
                double px = centerX + Math.cos(angle) * spread;
                double py = centerY + Math.sin(angle) * spread;

                pallets.addPallet(px, py, palletRadius);
            }

            //if winner is a Player and looser is a bot, 50pts
            if (winner instanceof com.codeforall.online.game.entities.Player
                    && loser instanceof com.codeforall.online.game.entities.BotPlayer) {
                return 50; // matar bot = 50 pts
            }
        }
        return 0;
    }
}
