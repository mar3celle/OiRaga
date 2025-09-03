package com.codeforall.online.game.gameobjects;

import com.codeforall.simplegraphics.graphics.Color;
import com.codeforall.simplegraphics.graphics.Ellipse;
import java.util.Random;

public class Pallets {

    // --- FIELDS ---
    private int radius;             // Radius of the pallet (size)
    private int x, y;               // Position of the pallet (center)
    private boolean isEaten;        // Whether the pallet has been eaten by a player
    private Color palletColor;      // Pallet's color
    private Ellipse lastShape;      // Reference to last drawn shape (so it can be deleted/redrawn)

    // Respawn system
    private long respawnTime = 0;   // Timestamp for when the pallet should respawn
    private static final int MIN_RESPAWN = 3000;   // Minimum respawn delay (ms)
    private static final int MAX_RESPAWN = 20000;  // Maximum respawn delay (ms)

    // --- CONSTRUCTOR ---
    public Pallets(int radius, int x, int y) {
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.isEaten = false;
        this.palletColor = getRandomColor();
    }

    // Generate a random color for this pallet
    private Color getRandomColor() {
        Random rand = new Random();   // ⚠ Creates a new Random every time → could reuse a static instance
        return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }

    // Draw the pallet as a circle
    public void draw() {
        if (lastShape != null) lastShape.delete(); // Remove previous drawing
        int d = radius * 2;
        lastShape = new Ellipse(x - radius, y - radius, d, d); // Draw centered
        lastShape.setColor(palletColor);
        lastShape.fill();
    }

    // Remove the pallet from the screen
    public void delete() {
        if (lastShape != null) lastShape.delete();
    }

    // --- RESPAWN SYSTEM ---

    // Called when pallet is eaten → schedules a respawn after random interval
    public void scheduleRespawn() {
        Random rand = new Random(); // ⚠ Again creates new Random instance → could reuse
        int interval = MIN_RESPAWN + rand.nextInt(MAX_RESPAWN - MIN_RESPAWN + 1);
        respawnTime = System.currentTimeMillis() + interval;
        isEaten = true;
        delete();
    }

    // Check if it's time for the pallet to respawn
    public boolean readyToRespawn() {
        return isEaten && System.currentTimeMillis() >= respawnTime;
    }

    // Respawn at a new position
    public void respawn(int newX, int newY) {
        this.x = newX;
        this.y = newY;
        this.isEaten = false;
        draw();
    }

    // --- GETTERS / SETTERS ---
    public int getX() { return x; }
    public int getY() { return y; }
    public int getRadius() { return radius; }
    public boolean isEaten() { return isEaten; }
    public void setEaten(boolean eaten) { isEaten = eaten; }
}
