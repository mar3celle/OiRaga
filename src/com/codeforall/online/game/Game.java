package com.codeforall.online.game;

import com.codeforall.online.game.entities.Player;
import com.codeforall.online.game.Grid.Grid;
import com.codeforall.online.game.gameobjects.Pallets;
import com.codeforall.online.game.inputs.MouseController;

public class Game {

    public static void main(String[] args) throws InterruptedException {

        Grid grid = new Grid(100, 50);
        grid.init();

        // draw pallets
        Pallets allPallets = new Pallets(1000, grid.getGameFieldMaxX(), grid.getGameFieldMaxY());

        int[] cell = grid.getRandomCell();
        int row = cell[0];
        int col = cell[1];

        double playerX = grid.columnToX(col) + grid.getCellSize() / 2.0;
        double playerY = grid.rowToY(row) + grid.getCellSize() / 2.0;

        Player player = new Player(playerX, playerY, 12.0);
        player.draw();

        MouseController mouse = new MouseController();

        double dt = 0.016; // ~60 FPS
        while (player.isAlive()) {

            if (mouse.isActive()) {
                double dx = mouse.getMouseX() - player.getX();
                double dy = mouse.getMouseY() - player.getY();
                player.setDirection(dx, dy);
            }

            player.updatePosition(dt,
                    grid.getCols() * grid.getCellSize() + Grid.PADDING,
                    grid.getRows() * grid.getCellSize() + Grid.PADDING);

            player.draw();


            Thread.sleep((int)(dt * 1000));
        }
    }
}
