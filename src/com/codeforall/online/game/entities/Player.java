package com.codeforall.online.game.entities;

// Represents the human-controlled player (inherits all movement and collision logic from BasePlayer)
public class Player extends BasePlayer {

    // Constructor: sets initial position (x, y) and radius (size of the player)
    public Player(double x, double y, double radius) {
        super(x, y, radius); // Calls BasePlayer constructor
    }

    // Update method (called every frame)
    @Override
    public void update(double dt, int worldW, int worldH) {
        // Human player does not have AI, only moves based on input
        updatePosition(dt, worldW, worldH);
    }
}
