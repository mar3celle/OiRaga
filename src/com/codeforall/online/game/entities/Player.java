package com.codeforall.online.game.entities;

import com.codeforall.online.game.entities.BasePlayer;

public class Player extends BasePlayer {

    public Player(double x, double y, double radius) {
        super(x, y, radius);
    }

    @Override
    public void update(double dt, int worldW, int worldH) {
        // direção já definida pelo input externo (mouse, teclado, etc.)
        updatePosition(dt, worldW, worldH);
    }
}