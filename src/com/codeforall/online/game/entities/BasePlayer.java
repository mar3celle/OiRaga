package com.codeforall.online.game.entities;

import com.codeforall.online.game.Grid.Grid;
import com.codeforall.online.game.gameobjects.Pallets;
import com.codeforall.online.game.gameobjects.PalletFactory;
import com.codeforall.simplegraphics.graphics.Color;
import com.codeforall.simplegraphics.graphics.Ellipse;

import java.util.List;
import java.util.Random;

// Abstract base class for all players (human and bot).
// Handles movement, growth, drawing, pallet collisions, and player-vs-player collisions.
public abstract class BasePlayer {

    // Position and movement
    protected double x, y;        // current position
    protected double dx, dy;      // normalized movement direction

    // Size and state
    protected double radius;      // player radius (controls size & speed)
    protected boolean alive = true;

    // Graphics
    protected Ellipse lastShape;  // the last drawn circle
    protected final Color playerColor;

    // Constants for speed calculation
    private static final double BASE_SPEED = 1800.0;   // base movement factor
    private static final double SPEED_RADIUS_FLOOR = 12.0; // prevents tiny players from being too fast
    private static final Random rand = new Random();

    // Constructor: initializes position and size
    public BasePlayer(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.playerColor = getRandomColor();
    }

    // Assigns a random color to each player
    private Color getRandomColor() {
        return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }

    // Set movement direction (dx, dy normalized to unit vector)
    public void setDirection(double dx, double dy) {
        double len = Math.hypot(dx, dy);
        this.dx = (len < 1e-6) ? 0 : dx / len;
        this.dy = (len < 1e-6) ? 0 : dy / len;
    }

    // Speed decreases as radius grows (area growth makes you slower)
    protected double getSpeed() {
        return BASE_SPEED / Math.max(SPEED_RADIUS_FLOOR, radius);
    }

    // Updates position and clamps inside game boundaries
    public void updatePosition(double dt, int worldW, int worldH) {
        x += dx * getSpeed() * dt;
        y += dy * getSpeed() * dt;

        // Clamp so player stays inside the grid
        x = clamp(x, Grid.PADDING + radius, Grid.PADDING + worldW - radius);
        y = clamp(y, Grid.PADDING + radius, Grid.PADDING + worldH - radius);
    }

    // Utility function for clamping values to [min, max]
    private double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    // Draws the player as a circle
    public void draw() {
        if (lastShape != null) lastShape.delete();
        int d = (int) Math.round(radius * 2);
        lastShape = new Ellipse((int)(x-radius), (int)(y-radius), d, d);
        lastShape.setColor(playerColor);
        lastShape.fill();
    }

    // Increase radius proportionally to the added area
    public void growByArea(double areaToAdd) {
        double myArea = Math.PI * radius * radius + areaToAdd;
        radius = Math.sqrt(myArea / Math.PI);
    }

    // Deletes the player (dies)
    public void delete() {
        alive = false;
        if (lastShape != null) lastShape.delete();
    }

    // Basic getters
    public boolean isAlive() { return alive; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getRadius() { return radius; }

    // Abstract: must be implemented by Player and BotPlayer
    public abstract void update(double dt, int worldW, int worldH);

    // Check collision with pallets
    public void checkPalletCollision(List<Pallets> pallets) {
        for (Pallets p : pallets) {
            if (!p.isEaten() && checkCollision(this, p)) {
                // Grow by pallet area
                double area = Math.PI * p.getRadius() * p.getRadius();
                growByArea(area);

                // Remove pallet and schedule respawn
                p.scheduleRespawn();
            }
        }
    }

    // Helper: check collision between player and a single pallet
    private boolean checkCollision(BasePlayer player, Pallets pallet) {
        double dx = player.getX() - pallet.getX();
        double dy = player.getY() - pallet.getY();
        return Math.hypot(dx, dy) < (player.getRadius() + pallet.getRadius());
    }

    // Handles player-vs-player collisions
    public void checkPlayerCollision(BasePlayer other, PalletFactory palletFactory) {
        if (!this.isAlive() || !other.isAlive()) return;

        double dx = this.getX() - other.getX();
        double dy = this.getY() - other.getY();
        double distance = Math.hypot(dx, dy);

        if (distance < this.getRadius() + other.getRadius()) {
            // Decide winner/loser based on radius
            BasePlayer winner = (this.radius >= other.radius) ? this : other;
            BasePlayer loser = (winner == this) ? other : this;

            // Calculate loser area, then delete loser
            double loserArea = Math.PI * loser.radius * loser.radius;
            loser.delete();

            // Drop pallets with fixed radius (same as normal pallets)
            int fixedPalletRadius = 10;
            int numPallets = (int)(loserArea / (Math.PI * fixedPalletRadius * fixedPalletRadius));

            for (int i = 0; i < numPallets; i++) {
                double angle = Math.random() * 2 * Math.PI;
                double spread = Math.random() * 30;

                int px = (int)(loser.getX() + Math.cos(angle) * spread);
                int py = (int)(loser.getY() + Math.sin(angle) * spread);

                palletFactory.addPallet(px, py, fixedPalletRadius, false);
            }
        }
    }
}
