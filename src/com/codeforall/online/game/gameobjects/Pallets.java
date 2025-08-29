package com.codeforall.online.game.gameobjects;

import com.codeforall.online.game.Grid.Grid;
import com.codeforall.simplegraphics.graphics.Color;
import com.codeforall.simplegraphics.graphics.Ellipse;

import java.util.ArrayList;
import java.util.List;

public class Pallets {

    private final int size = 10;

    public Pallets(int amount, double maxX, double maxY){
        for(int i = 0; i < amount; i++){
            // get random (x,y) location
            double randomX = Grid.PADDING + Math.random() * (maxX - Grid.PADDING);
            double randomY = Grid.PADDING + Math.random() * (maxY - Grid.PADDING);

            // creates the pallet
            Ellipse size = new Ellipse(randomX, randomY, this.size,this.size);

            // adds to list for colisions
            pallets.add(size);

            // styling
            size.fill();
            int randomColor = (int) Math.floor(Math.random() * 10);
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


    private List<Ellipse> pallets = new ArrayList<>(); // ao criar cells este array vai guarda las para verificarmos as colisoes

    public List<Ellipse> getPallets(){
        return pallets;
    }

}
