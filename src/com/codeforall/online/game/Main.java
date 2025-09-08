package com.codeforall.online.game;

import com.codeforall.online.game.Game;
import com.codeforall.online.game.MenuBox;

public class Main {
    public static void main(String[] args) {
        Game mainGame = new Game();
        MenuBox.init(mainGame); // inicia o menu principal
    }
}