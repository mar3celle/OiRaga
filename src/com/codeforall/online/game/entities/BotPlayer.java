package com.codeforall.online.game.entities;

import com.codeforall.online.game.Game;
import com.codeforall.online.game.grid.Grid;

import java.util.Random;

public class BotPlayer extends BasePlayer {

    private double timeSinceLastChange = 0;
    private final Random rand = new Random();

    // fields for hunt ai
    private double targetX;
    private double targetY;
    private final Random rng = new Random();

    private static final double AGGRO_DIST   = 600.0;         // distance to react to target
    private static final double SIZE_RATIO   = 1.15;
    private static final double STEER_ALPHA  = 0.08;          // suaviza a direção(sem isso deu ruim)
    private static final double RETARGET_SEC = 5.0;           // change target
    private static final double TARGET_NEAR  = 20.0;

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
        int pad = Grid.PADDING;
        // intervalo: [pad + margin, (worldW - margin)] e idem para Y
        targetX = pad + margin + rng.nextDouble() * ((worldW - pad) - 2 * margin);
        targetY = pad + margin + rng.nextDouble() * ((worldH - pad) - 2 * margin);
        }
    private void ensureTarget(double dt, int worldW, int worldH) {
        timeSinceLastChange += dt;
        double distToTarget = Math.hypot(targetX - getX(), targetY - getY());
        if (distToTarget < TARGET_NEAR || rng.nextDouble() < 0.005 || timeSinceLastChange > RETARGET_SEC) {
            pickNewTarget(worldW, worldH);
            timeSinceLastChange = 0;
        }
    }

    // encontra o player/bot vivo mais próximo
    private BasePlayer findClosestAlive() {
        BasePlayer closest = null;
        double closestDist = Double.MAX_VALUE;

        for (BasePlayer other : Game.players) {                    // mantém tua fonte de verdade
            if (other == this || !other.isAlive()) continue;

            double dx = other.getX() - getX();
            double dy = other.getY() - getY();
            double dist = Math.hypot(dx, dy);

            if (dist < closestDist) {
                closestDist = dist;
                closest = other;
            }
        }
        return closest;
    }

    // CHANGED: decide se foge, persegue ou vagueia
    private void decideBehavior(BasePlayer closest) {
        if (closest == null) {
            steer(targetX - getX(), targetY - getY());            // vaguear
            return;
        }

        double dx = closest.getX() - getX();
        double dy = closest.getY() - getY();
        double dist = Math.hypot(dx, dy);

        if (dist < AGGRO_DIST) {
            boolean otherMuchBigger = closest.getRadius() >= this.getRadius() * SIZE_RATIO;
            boolean botMuchBigger   = this.getRadius()  >= closest.getRadius() * SIZE_RATIO;

            if (otherMuchBigger) {
                steer(-dx, -dy);                                  // fugir
            } else if (botMuchBigger) {
                steer(dx, dy);                                    // perseguir
            } else {
                steer(targetX - getX(), targetY - getY());        // vaguear
            }
        } else {
            steer(targetX - getX(), targetY - getY());            // longe → vaguear
        }
    }

    //CHANGED: suaviza a direção
    private void steer(double desiredX, double desiredY) {
        double len = Math.hypot(desiredX, desiredY);
        if (len < 1e-6) return;
        desiredX /= len;
        desiredY /= len;

        // mistura direção atual com direção desejada
        this.dx = (1 - STEER_ALPHA) * this.dx + STEER_ALPHA * desiredX;
        this.dy = (1 - STEER_ALPHA) * this.dy + STEER_ALPHA * desiredY;

        double l = Math.hypot(this.dx, this.dy);
        if (l > 1e-6) { this.dx /= l; this.dy /= l; }
    }

    // isola o movimento/clamp
    private void move(double dt, int worldW, int worldH) {
        updatePosition(dt, worldW, worldH);
    }

    @Override
    public void update(double dt, int worldW, int worldH) {
        ensureTarget(dt, worldW, worldH);
        BasePlayer closest = findClosestAlive();
        decideBehavior(closest);
        move(dt, worldW, worldH);
    }
}