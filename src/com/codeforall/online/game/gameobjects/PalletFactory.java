package com.codeforall.online.game.gameobjects;

import com.codeforall.online.game.Grid.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PalletFactory {

    private static final Random rand = new Random();
    private final List<Pallets> pallets = new ArrayList<>();

    public void spawnPallets(int amount, int worldW, int worldH, int radius) {
        for (int i = 0; i < amount; i++) {
            int x = Grid.PADDING + rand.nextInt(worldW - radius * 2) + radius;
            int y = Grid.PADDING + rand.nextInt(worldH - radius * 2) + radius;
            Pallets p = new Pallets(radius, x, y);
            p.draw();
            pallets.add(p);
        }
    }

    public void update(int worldW, int worldH) {
        for (Pallets p : pallets) {
            if (p.readyToRespawn()) {
                int x = Grid.PADDING + rand.nextInt(worldW - p.getRadius() * 2) + p.getRadius();
                int y = Grid.PADDING + rand.nextInt(worldH - p.getRadius() * 2) + p.getRadius();
                p.respawn(x, y);
            }
        }
    }

    public List<Pallets> getPallets() {
        return pallets;
    }

    public void addPallet(int x, int y, int radius, boolean eaten) {
        // garantir que não sai da grid
        int safeX = Math.max(Grid.PADDING + radius, Math.min(Grid.PADDING + worldW - radius, x));
        int safeY = Math.max(Grid.PADDING + radius, Math.min(Grid.PADDING + worldH - radius, y));

        Pallets p = new Pallets(radius, safeX, safeY);
        p.setEaten(eaten);
        if (!eaten) p.draw();
        pallets.add(p);
    }

    // precisamos do tamanho do mundo para addPallet (quando dropa do player)
    private int worldW, worldH;

    public void setWorldSize(int w, int h) {
        this.worldW = w;
        this.worldH = h;
    }
}
