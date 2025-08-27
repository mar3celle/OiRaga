package com.codeforall.online.game;

import com.codeforall.online.game.players.Pallets;
import com.codeforall.online.game.players.PlayerMain;
import com.codeforall.simplegraphics.graphics.Canvas;
import com.codeforall.simplegraphics.graphics.Ellipse;

public class Main {
    public static void main(String[] args) {

        Canvas.setMaxX(1000);
        Canvas.setMaxY(500);

        PlayerMain player = new PlayerMain(30,30,50);

        Pallets pallets = new Pallets(100);

    }
}

