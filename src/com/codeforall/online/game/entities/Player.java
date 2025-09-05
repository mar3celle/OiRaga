package com.codeforall.online.game.entities;

public class Player extends BasePlayer {
    private long invincibleUntilMs = 0;       // CHEAT invencivel até..

    public Player(double x, double y, double radius) {
        super(x, y, radius);
    }
    //CHEATS
    public void activateInvincibilityMillis(long millis) {
        invincibleUntilMs = System.currentTimeMillis() + millis;
    }
    public boolean isInvincible() {
        return System.currentTimeMillis() < invincibleUntilMs;
    }


    @Override
    public void update(double dt, int worldW, int worldH) {
        updatePosition(dt, worldW, worldH);
    }
}