package com.codeforall.online.game.players;

import com.codeforall.simplegraphics.graphics.Color;
import com.codeforall.simplegraphics.graphics.Ellipse;

public class Pallets {

    private final int size = 5;
    private int increaseValue;
    private int amount;
    private double minX = 0; // substituir por getCanvaMinX
    private double maxX = 400; // substituir por getCanvaMaxY
    private double minY = 0; // substituir por getCanvaMinX
    private double maxY = 500; // substituir por getCanvaMaxY
    private boolean colision;

    public Pallets(int amount){
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


}
