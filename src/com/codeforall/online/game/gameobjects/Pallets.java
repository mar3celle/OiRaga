package com.codeforall.online.game.gameobjects;

import com.codeforall.online.game.Grid.Grid;
import com.codeforall.simplegraphics.graphics.Color;
import com.codeforall.simplegraphics.graphics.Ellipse;

import java.util.ArrayList;
import java.util.List;

public class Pallets {

    private final int size = 5;
    private int increaseValue;
    private int amount;
    private double minX = Grid.PADDING;
    private double minY = Grid.PADDING;
    private double radius;
    private boolean colision;

    private List<Ellipse> pallets = new ArrayList<>(); // ao criar cells este array vai guarda las para verificarmos as colisoes

    public Pallets(int amount, int maxX, int maxY){
        this.amount = amount;

        for(int i = 0; i < amount; i++){
            double randomX = minX + Math.random() * (maxX - minX);
            double randomY = minY + Math.random() * (maxY - minY);

            int randomColor = (int) Math.floor(Math.random() * 10);

            Ellipse size = new Ellipse(randomX, randomY, this.size,this.size);
            size.fill();
            size.setColor(switch (randomColor) {
                case 0 -> Color.BLUE;
                case 1 -> Color.CYAN;
                case 2 -> Color.DARK_GRAY;
                case 3 -> Color.GREEN;
                case 4 -> Color.LIGHT_GRAY;
                case 5 -> Color.MAGENTA;
                case 6 -> Color.ORANGE;
                case 7 -> Color.PINK;
                case 8 -> Color.RED;
                case 9 -> Color.YELLOW;
                default -> Color.BLACK;
            });
        }
    }


    public List<Ellipse> getPallets(){ // getter para invocar na classe playermain
        return pallets;
    }


    public void delete() {

    }
}
