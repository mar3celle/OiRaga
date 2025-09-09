package com.codeforall.online.game.gameobjects;

import com.codeforall.online.game.grid.Grid;                     // CHANGED: pacote grid (minúsculo)
import com.codeforall.simplegraphics.graphics.Color;
import com.codeforall.simplegraphics.graphics.Ellipse;

import java.util.ArrayList;                                       // CHANGED: mover imports para topo (organização)
import java.util.List;
import java.util.Random;

public class Pallets {

    private final List<Ellipse> pallets = new ArrayList<>();
    private final Random rng = new Random();

    // “tamanho base” para geração (raio padrão dos pallets criados no construtor)
    private static final double DEFAULT_RADIUS = 5.0;
    private static final Color[] COLORS = new Color[]{
            new Color(80, 170, 80),
            new Color(70, 160, 200),
            new Color(200, 140, 70),
            new Color(170, 90, 160)
    };

    // Construtor
    public Pallets(int amount, double maxX, double maxY) {
        for (int i = 0; i < amount; i++) {
            // get random (x,y) location

            double pad = Grid.PADDING;
            double margin = 6.0; // pequeno afastamento das paredes

            double x = pad + margin + rng.nextDouble() * ((maxX - pad) - 2 * margin);
            double y = pad + margin + rng.nextDouble() * ((maxY - pad) - 2 * margin);

            Ellipse e = new Ellipse(
                    x - DEFAULT_RADIUS,
                    y - DEFAULT_RADIUS,
                    DEFAULT_RADIUS * 2,
                    DEFAULT_RADIUS * 2
            );
            e.setColor(randomColor());
            e.fill();
            pallets.add(e);
        }
    }


    //return the ellipse list to colisions
    public List<Ellipse> getPallets() {
        return pallets;
    }

    // for player collision
    /** Adiciona um pallet em (x,y) com o raio especificado (usado quando um player explode em pallets). */
    public void addPallet(double x, double y, double radius) {
        Ellipse ellipse = new Ellipse(x - radius, y - radius, radius * 2, radius * 2);
        ellipse.setColor(randomColor());                                              // CHANGED: antes era fixo BLUE
        ellipse.fill();
        pallets.add(ellipse);
    }

    // Helpers privados

    private Color randomColor() {                                                     // CHANGED: helper para cor
        return COLORS[rng.nextInt(COLORS.length)];
    }
}
