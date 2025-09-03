package com.codeforall.online.game;

import com.codeforall.simplegraphics.graphics.Text;
import com.codeforall.simplegraphics.graphics.Color;
import com.codeforall.online.game.entities.BasePlayer;
import com.codeforall.online.game.entities.BotPlayer;
import com.codeforall.online.game.entities.Player;
import com.codeforall.online.game.Grid.Grid;
import com.codeforall.online.game.gameobjects.Pallets;
import com.codeforall.online.game.inputs.MouseController;

import java.util.ArrayList;
import java.util.List;

public class Game {
    // Pontuação
    private static final int PELLET_POINTS = 1;
    private static final int BOT_POINTS = 50; // precisa coincidir com BasePlayer

    // CHANGED: tornados estáticos para poderem ser usados de main (estático)
    private static int score = 0;                 // CHANGED
    private static int highScore = 0;             // CHANGED
    private static final HighScoreManager highMan = new HighScoreManager(); // CHANGED
    private static Text hudScore, hudHigh;        // CHANGED

    // Global list of all players
    public static List<BasePlayer> players = new ArrayList<>();

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

        //create highscore and draw
        highScore = highMan.loadHighScore();
        updateHud();   //  "Score: 0  High: <valor>"

        // Calculate game grid dimensions
        int worldW = Grid.PADDING + grid.getCols() * grid.getCellSize();   // CHANGED: respeita padding à esquerda
        int worldH = Grid.PADDING + grid.getRows() * grid.getCellSize();   // CHANGED: respeita padding no topo

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
                if (!p.isAlive()) continue;

                p.update(dt, worldW, worldH);

                // pallets -> pontos (só contam para o humano; BasePlayer retorna pts só do humano)
                int gained = p.checkPalletCollision(allPallets);
                if (gained > 0) {
                    score += gained * PELLET_POINTS;
                    if (score > highScore) { highScore = score; }
                    updateHud();
                }

                p.draw();
            }

            // Check collisions between players
            for (int i = 0; i < players.size(); i++) {
                BasePlayer p1 = players.get(i);
                for (int j = i + 1; j < players.size(); j++) {
                    BasePlayer p2 = players.get(j);
                    // handle fight logic
                    //somar pontos vindos da colisão (quando o player elimina um bot)
                    int gainedVs = p1.checkPlayerCollision(p2, allPallets);
                    if (gainedVs > 0) {
                        score += gainedVs;                  //(BOT_POINTS já aplicados no BasePlayer)
                        if (score > highScore) { highScore = score; }
                        updateHud();
                    }
                }
            }

            // control frame rate
            Thread.sleep((int)(dt * 1000));
        }

        //  ao terminar, persistir o high score
        highScore = highMan.updateIfBest(score, highScore);  // CHANGED
    }

    //método estático para atualizar o HUD (score/high)
    private static void updateHud() {
        // apaga textos anteriores
        if (hudScore != null) hudScore.delete();
        if (hudHigh  != null) hudHigh.delete();

        int x = Grid.PADDING + 16;
        int y = Grid.PADDING - 1;

        hudScore = new Text(x, y, "Score: " + score);
        hudScore.setColor(Color.DARK_GRAY);
        hudScore.draw();

        hudHigh  = new Text(x + 140, y, "High: " + highScore);
        hudHigh.setColor(Color.DARK_GRAY);
        hudHigh.draw();
    }
}
