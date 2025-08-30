package com.codeforall.online.game.entities;

import com.codeforall.online.game.entities.BasePlayer;

import java.util.Random;

public class BotPlayer extends BasePlayer {

    private double timeSinceLastChange = 0;
    private final Random rand = new Random();

    public BotPlayer(double x, double y, double radius) {
        super(x, y, radius);
        setRandomDirection();
    }

    private void setRandomDirection() {
        double angle = rand.nextDouble() * 2 * Math.PI;
        setDirection(Math.cos(angle), Math.sin(angle));
    }

    @Override
    public void update(double dt, int worldW, int worldH) {
        timeSinceLastChange += dt;
        if (timeSinceLastChange > 2.0) {
            setRandomDirection();
            timeSinceLastChange = 0;
        }
        updatePosition(dt, worldW, worldH);
    }
}