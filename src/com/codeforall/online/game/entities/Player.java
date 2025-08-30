package com.codeforall.online.game.entities;

public class Player extends BasePlayer {

    public Player(double x, double y, double radius) {
        super(x, y, radius);
    }

    @Override
    public void update(double dt, int worldW, int worldH) {
        updatePosition(dt, worldW, worldH);
    }
}