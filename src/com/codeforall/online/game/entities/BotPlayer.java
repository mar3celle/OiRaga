package com.codeforall.online.game.entities;

import com.codeforall.online.game.Game;
import java.util.Random;

// Represents an AI-controlled player (a bot) that moves, hunts, or flees based on other players
public class BotPlayer extends BasePlayer {

    private double timeSinceLastChange = 0; // Tracks how long since the bot last changed target
    private final Random rand = new Random();

    // Target position the bot is trying to reach (for wandering)
    private double targetX, targetY;
    private final Random rng = new Random();

    // Constructor: creates a bot at (x, y) with a given radius
    public BotPlayer(double x, double y, double radius) {
        super(x, y, radius);
        setRandomDirection(); // Starts moving in a random direction
    }

    // Sets a completely random movement direction (unit vector)
    private void setRandomDirection() {
        double angle = rand.nextDouble() * 2 * Math.PI;
        setDirection(Math.cos(angle), Math.sin(angle));
    }

    // Picks a new random target position inside the world (avoids borders by using margin)
    private void pickNewTarget(int worldW, int worldH) {
        double margin = 30; // Prevents target being too close to edges
        targetX = margin + rng.nextDouble() * (worldW - 2 * margin);
        targetY = margin + rng.nextDouble() * (worldH - 2 * margin);
    }

    @Override
    public void update(double dt, int worldW, int worldH) {
        timeSinceLastChange += dt;

        // Check if bot should choose a new random target:
        // - reached close to current target
        // - random chance (0.5%)
        // - or has been stuck on one target for more than 5 seconds
        double distToTarget = Math.hypot(targetX - getX(), targetY - getY());
        if (distToTarget < 20 || rng.nextDouble() < 0.005 || timeSinceLastChange > 5.0) {
            pickNewTarget(worldW, worldH);
            timeSinceLastChange = 0;
        }

        // Look for the closest other player
        BasePlayer closest = null;
        double closestDist = Double.MAX_VALUE;

        for (BasePlayer other : Game.players) {
            if (other == this || !other.isAlive()) continue; // Skip itself and dead players

            double dx = other.getX() - getX();
            double dy = other.getY() - getY();
            double dist = Math.hypot(dx, dy);

            if (dist < closestDist) {
                closestDist = dist;
                closest = other;
            }
        }

        // Decision-making logic:
        // Only care about players within 600 units
        if (closest != null && closestDist < 600) {
            boolean otherMuchBigger = closest.getRadius() >= this.getRadius() * 1.15;
            boolean botMuchBigger = this.getRadius() >= closest.getRadius() * 1.15;

            if (otherMuchBigger) {
                // Flee if the other is much bigger
                setDirection(getX() - closest.getX(), getY() - closest.getY());
            } else if (botMuchBigger) {
                // Chase if the bot is much bigger
                setDirection(closest.getX() - getX(), closest.getY() - getY());
            } else {
                // Otherwise, just wander
                setDirection(targetX - getX(), targetY - getY());
            }
        } else {
            // No nearby players → wander randomly
            setDirection(targetX - getX(), targetY - getY());
        }

        // Apply movement (respects speed scaling and world boundaries)
        updatePosition(dt, worldW, worldH);
    }
}
