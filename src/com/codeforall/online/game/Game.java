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

        // Initialize the game grid
        Grid grid = new Grid(100, 50);
        grid.init();

        // Changeable values
        int palletAmount = 300; // number of pallets to spawn
        int palletDistributionX = grid.getCols() * grid.getCellSize() - grid.PADDING; // Spawn inside the game rectangle
        int palletDistributionY = grid.getRows() * grid.getCellSize() - grid.PADDING; // Spawn inside the game rectangle
        int numberOfBots = 7; // number of bots to spawn

        // Create pallets
        Pallets allPallets = new Pallets(palletAmount, palletDistributionX, palletDistributionY);

        // Get a random cell to place the human player
        int[] cell = grid.getRandomCell();
        int row = cell[0];
        int col = cell[1];
        double playerX = grid.columnToX(col) + grid.getCellSize() / 2.0; // coordinate x
        double playerY = grid.rowToY(row) + grid.getCellSize() / 2.0; // coordinate y

        // Create player and add to the global list
        Player player = new Player(playerX, playerY, 12.0);
        players.add(player);

        // Calculate game grid dimensions
        int worldW = grid.getCols() * grid.getCellSize() - Grid.PADDING;
        int worldH = grid.getRows() * grid.getCellSize() - Grid.PADDING;

        // Bot spawn loop, to spawn in different places
        for (int i = 0; i < numberOfBots; i++) {
            double margin = 30; // spawn away from edges (they can get stuck if near edge spawn)
            double botX = margin + Math.random() * (worldW - 2 * margin);
            double botY = margin + Math.random() * (worldH - 2 * margin);

            BotPlayer bot = new BotPlayer(botX, botY, 12.0);
            players.add(bot);
        }

        // Initialize mouse controller for player movement
        MouseController mouse = new MouseController();

        // Time step for each frame (~60 FPS)
        double dt = 0.016;

        // Main game loop
        while (player.isAlive()) {

            // Update player direction based on mouse input
            if (mouse.isActive()) {
                double dx = mouse.getMouseX() - player.getX();
                double dy = mouse.getMouseY() - player.getY();
                player.setDirection(dx, dy);
            }

            // Update all players
            for (BasePlayer p : players) {
                if (p.isAlive()) {
                    p.update(dt, worldW, worldH);              // move player or bot
                    p.checkPalletCollision(allPallets);        // eat pallets if collided
                    p.draw();                                  // render on screen
                }
            }

            // Check collisions between players
            for (int i = 0; i < players.size(); i++) {
                BasePlayer p1 = players.get(i);
                for (int j = i + 1; j < players.size(); j++) {
                    BasePlayer p2 = players.get(j);
                    p1.checkPlayerCollision(p2, allPallets);   // handle fight logic
                }
            }

            // control frame rate
            Thread.sleep((int)(dt * 1000));
        }
    }

    // Global list of all players
    public static List<BasePlayer> players = new ArrayList<>();
}