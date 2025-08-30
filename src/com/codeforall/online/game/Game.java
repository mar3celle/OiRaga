package com.codeforall.online.game;

import com.codeforall.online.game.entities.BasePlayer;
import com.codeforall.online.game.entities.BotPlayer;
import com.codeforall.online.game.entities.Player;
import com.codeforall.online.game.Grid.Grid;
import com.codeforall.online.game.gameobjects.Pallets;
import com.codeforall.online.game.inputs.MouseController;

import java.util.ArrayList;
import java.util.List;

public class Game {

    public static void main(String[] args) throws InterruptedException {

        Grid grid = new Grid(100, 50);
        grid.init();

        // Pallets
        //Pallets allPallets = new Pallets(100, grid.getGameFieldMaxX(),
        Pallets allPallets = new Pallets(100, 1500, 750);

        // Posição inicial aleatória
        int[] cell = grid.getRandomCell();
        int row = cell[0];
        int col = cell[1];

        double playerX = grid.columnToX(col) + grid.getCellSize() / 2.0;
        double playerY = grid.rowToY(row) + grid.getCellSize() / 2.0;

        // Jogadores
        Player player = new Player(playerX, playerY, 12.0);
        BotPlayer bot = new BotPlayer(playerX + 100, playerY + 100, 12.0); // posição deslocada

        List<BasePlayer> players = new ArrayList<>();
        players.add(player);
        players.add(bot);

        MouseController mouse = new MouseController();

        double dt = 0.016; // ~60 FPS
        int worldW = grid.getCols() * grid.getCellSize() + Grid.PADDING;
        int worldH = grid.getRows() * grid.getCellSize() + Grid.PADDING;

        while (player.isAlive()) {

            // Atualiza direção do jogador controlado
            if (mouse.isActive()) {
                double dx = mouse.getMouseX() - player.getX();
                double dy = mouse.getMouseY() - player.getY();
                player.setDirection(dx, dy);
            }

            // Atualiza todos os jogadores
            for (BasePlayer p : players) {
                p.update(dt, worldW, worldH);
                p.checkPalletCollision(allPallets);
                p.draw();
            }

            Thread.sleep((int)(dt * 1000));
        }
    }
}