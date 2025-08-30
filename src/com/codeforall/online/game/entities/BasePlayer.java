package com.codeforall.online.game.entities;

import com.codeforall.online.game.gameobjects.Pallets;
import com.codeforall.simplegraphics.graphics.Color;
import com.codeforall.simplegraphics.graphics.Ellipse;

import java.util.Iterator;
import java.util.Random;

public abstract class BasePlayer {

    protected double x, y;
    protected double dx, dy;
    protected double radius;
    protected boolean alive = true;
    protected Ellipse lastShape;
    protected final Color playerColor;

    private static final double BASE_SPEED = 1800.0;
    private static final double SPEED_RADIUS_FLOOR = 12.0;

    public BasePlayer(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.playerColor = getRandomColor();
    }

    private Color getRandomColor() {
        Random rand = new Random();
        return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }

    public void setDirection(double dx, double dy) {
        double len = Math.hypot(dx, dy);
        this.dx = (len < 1e-6) ? 0 : dx / len;
        this.dy = (len < 1e-6) ? 0 : dy / len;
    }

    protected double getSpeed() {
        return BASE_SPEED / Math.max(SPEED_RADIUS_FLOOR, radius);
    }

    public void updatePosition(double dt, int worldW, int worldH) {
        double v = getSpeed();
        x += dx * v * dt;
        y += dy * v * dt;

        x = Math.max(radius, Math.min(worldW - radius, x));
        y = Math.max(radius, Math.min(worldH - radius, y));
    }

    public void draw() {
        if (lastShape != null) lastShape.delete();

        int d = (int) Math.round(radius * 2);
        int sx = (int) Math.round(x - radius);
        int sy = (int) Math.round(y - radius);

        Ellipse circle = new Ellipse(sx, sy, d, d);
        circle.setColor(playerColor);
        circle.fill();
        lastShape = circle;
    }

    public void growByArea(double areaToAdd) {
        double myArea = Math.PI * radius * radius;
        myArea += areaToAdd;
        radius = Math.sqrt(myArea / Math.PI);
    }

    public void delete() {
        alive = false;
        if (lastShape != null) lastShape.delete();
    }

    public boolean isAlive() { return alive; }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getRadius() { return radius; }

    public abstract void update(double dt, int worldW, int worldH);

    // Pallet Colision

    public void checkPalletCollision(Pallets pallets) {
        Iterator<Ellipse> it = pallets.getPallets().iterator();

        while (it.hasNext()) {
            Ellipse ellipse = it.next();

            if (checkCollision(this, ellipse)) {
                double palletRadius = ellipse.getWidth() / 2.0;
                double palletArea = Math.PI * palletRadius * palletRadius;

                growByArea(palletArea); // aumenta o tamanho do jogador
                ellipse.delete();       // remove do ecrã
                it.remove();            // remove da lista
            }
        }
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
}