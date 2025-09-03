package com.codeforall.online.game;

import com.codeforall.online.game.entities.BasePlayer;
import com.codeforall.online.game.entities.BotPlayer;
import com.codeforall.online.game.entities.Player;
import com.codeforall.online.game.Grid.Grid;
import com.codeforall.online.game.gameobjects.PalletFactory;
import com.codeforall.online.game.inputs.MouseController;

public class Game {

    // Global list of all players (both human and bots)
    public static java.util.List<BasePlayer> players = new java.util.ArrayList<>();

    public static void main(String[] args) throws InterruptedException {

        // Initialize game grid with 100 columns and 50 rows
        Grid grid = new Grid(100, 50);
        grid.init();

        // Compute world size in pixels
        int worldW = grid.getCols() * grid.getCellSize();
        int worldH = grid.getRows() * grid.getCellSize();

        // Create pallet factory and spawn initial pallets
        PalletFactory palletFactory = new PalletFactory();
        int palletAmount = 80;     // number of pallets to spawn
        int palletRadius = 10;     // radius of each pallet
        palletFactory.spawnPallets(palletAmount, worldW, worldH, palletRadius);

        // Create the human player at a random grid cell
        int[] cell = grid.getRandomCell();
        double playerX = grid.columnToX(cell[1]) + grid.getCellSize()/2.0; // center X of the chosen cell
        double playerY = grid.rowToY(cell[0]) + grid.getCellSize()/2.0;    // center Y of the chosen cell
        Player player = new Player(playerX, playerY, 12.0); // start with radius 12
        players.add(player);

        // Spawn bots
        int numberOfBots = 0; // currently no bots are added
        for (int i = 0; i < numberOfBots; i++) {
            double margin = 30; // keep bots away from edges
            double botX = margin + Math.random() * (worldW - 2*margin);
            double botY = margin + Math.random() * (worldH - 2*margin);
            BotPlayer bot = new BotPlayer(botX, botY, 12.0); // bots also start with radius 12
            players.add(bot);
        }

        // Initialize mouse controller for player movement
        MouseController mouse = new MouseController();

        double dt = 0.016; // Simulation time step (~60 FPS)

        // Main game loop
        while (player.isAlive()) {

            // Update human player direction based on mouse
            if (mouse.isActive()) {
                double dx = mouse.getMouseX() - player.getX();
                double dy = mouse.getMouseY() - player.getY();
                player.setDirection(dx, dy);
            }

            // Update and draw all players
            for (BasePlayer p : players) {
                if (p.isAlive()) {
                    // update position and logic
                    p.update(dt, worldW, worldH);

                    // check collisions with pallets
                    p.checkPalletCollision(palletFactory.getPallets());

                    // redraw player
                    p.draw();
                }
            }

            // Check collisions between players
            for (int i = 0; i < players.size(); i++) {
                BasePlayer p1 = players.get(i);
                for (int j = i+1; j < players.size(); j++) {
                    BasePlayer p2 = players.get(j);
                    p1.checkPlayerCollision(p2, palletFactory);
                }
            }

            // Update pallets (respawn logic for eaten ones)
            palletFactory.update(worldW, worldH);

            // Sleep to maintain frame rate
            Thread.sleep((int)(dt*1000));
        }
    }
}
