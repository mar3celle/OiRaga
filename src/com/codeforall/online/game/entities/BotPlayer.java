package com.codeforall.online.game.entities;

import com.codeforall.online.game.Game;
import com.codeforall.online.game.entities.Player;
import java.util.Random;

public class BotPlayer extends BasePlayer {

    private double timeSinceLastChange = 0;
    private final Random rand = new Random();

    // fields for hunt ai
    private double targetX, targetY;
    private final Random rng = new Random();


    public BotPlayer(double x, double y, double radius) {
        super(x, y, radius);
        setRandomDirection();
    }

    private void setRandomDirection() {
        double angle = rand.nextDouble() * 2 * Math.PI;
        setDirection(Math.cos(angle), Math.sin(angle));
    }

    // Hunt ai
    private void pickNewTarget(int worldW, int worldH) {
        double margin = 30;
        targetX = margin + rng.nextDouble() * (worldW - 2 * margin);
        targetY = margin + rng.nextDouble() * (worldH - 2 * margin);
    }

    @Override
    public void update(double dt, int worldW, int worldH) {
        timeSinceLastChange += dt;

        // update target
        double distToTarget = Math.hypot(targetX - getX(), targetY - getY());
        if (distToTarget < 20 || rng.nextDouble() < 0.005 || timeSinceLastChange > 5.0) {
            pickNewTarget(worldW, worldH);
            timeSinceLastChange = 0;
        }

        // AI logic: analyse other players
        BasePlayer closest = null;
        double closestDist = Double.MAX_VALUE;

        for (BasePlayer other : Game.players) {
            if (other == this || !other.isAlive()) continue;

            double dx = other.getX() - getX();
            double dy = other.getY() - getY();
            double dist = Math.hypot(dx, dy);

            if (dist < closestDist) {
                closestDist = dist;
                closest = other;
            }
        }

        if (closest != null && closestDist < 600) {
            boolean otherMuchBigger = closest.getRadius() >= this.getRadius() * 1.15;
            boolean botMuchBigger = this.getRadius() >= closest.getRadius() * 1.15;

            if (otherMuchBigger) {
                setDirection(getX() - closest.getX(), getY() - closest.getY()); // fugir
            } else if (botMuchBigger) {
                setDirection(closest.getX() - getX(), closest.getY() - getY()); // perseguir
            } else {
                setDirection(targetX - getX(), targetY - getY()); // vaguear
            }
        } else {
            setDirection(targetX - getX(), targetY - getY()); // vaguear
        }

        updatePosition(dt, worldW, worldH);
    }


}