package com.codeforall.online.game;

import com.codeforall.online.game.inputs.MyKeyboard;
import com.codeforall.simplegraphics.graphics.Canvas;
import com.codeforall.simplegraphics.graphics.Text;
import com.codeforall.simplegraphics.graphics.Color;
import com.codeforall.online.game.entities.BasePlayer;
import com.codeforall.online.game.entities.BotPlayer;
import com.codeforall.online.game.entities.Player;
import com.codeforall.online.game.grid.Grid;
import com.codeforall.online.game.gameobjects.Pallets;
import com.codeforall.online.game.inputs.MouseController;
import com.codeforall.simplegraphics.graphics.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private static final int PELLET_POINTS = 1;
    private static final int BOT_POINTS = 50; // precisa coincidir com BasePlayer (pointsForKill)

    private int score = 0;
    private int highScore = 0;
    private final HighScoreManager highMan = new HighScoreManager();
    private Text hudScore;
    private Text hudHigh;
    Text pausedText = null;
    boolean paused = false;

    // campo para win condition
    private Text winText;

    // Global list of all players
    public static final List<BasePlayer> players = new ArrayList<>();

    //instanciar loop do jogo
    public void run() throws InterruptedException {
        Canvas.getCanvas();
        // Initialize the game grid
        Grid grid = new Grid(100, 50);
        grid.init();

        // Changeable values
        int palletAmount = 300; // number of pallets to spawn
        int palletDistributionX = grid.getCols() * grid.getCellSize() - Grid.PADDING; // Spawn inside the game rectangle
        int palletDistributionY = grid.getRows() * grid.getCellSize() - Grid.PADDING; // Spawn inside the game rectangle
        int numberOfBots = 7; // number of bots to spawn

        // Create pallets
        Pallets allPallets = new Pallets(palletAmount, palletDistributionX, palletDistributionY);

        // Get a random cell to place the human player
        int[] cell = grid.getRandomCell();
        int row = cell[0];
        int col = cell[1];
        double playerX = grid.columnToX(col) + grid.getCellSize() / 2.0; // coordinate x
        double playerY = grid.rowToY(row) + grid.getCellSize() / 2.0;    // coordinate y

        // Create player and add to the global list
        Player player = new Player(playerX, playerY, 12.0);
        players.clear();                 // lista limpa ao iniciar
        players.add(player);

        //create beyboard
        MyKeyboard keys = new MyKeyboard();

        // create highscore and draw
        highScore = highMan.loadHighScore();
        updateHud(); //"Score: 0  High: <valor>"

        // Calculate game grid dimensions
        int worldW = Grid.PADDING + grid.getCols() * grid.getCellSize();
        int worldH = Grid.PADDING + grid.getRows() * grid.getCellSize();

        // Bot spawn loop, to spawn in different places
        for (int i = 0; i < numberOfBots; i++) {
            double margin = 30; // spawn away from edges
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

            // ESC → sair
            if (keys.pollExit()) {           // CHANGED
                break;                       // CHANGED
            }

            // P → alterna pausa
            if (keys.pollPause()) {
                paused = !paused;
                if (paused) {
                    if (pausedText != null) pausedText.delete();
                    pausedText = new Text(Grid.PADDING + 260, Grid.PADDING + 20, "PAUSED");
                    pausedText.grow(10,10);
                    pausedText.setColor(Color.DARK_GRAY);
                    pausedText.draw();
                } else {
                    if (pausedText != null) { pausedText.delete(); pausedText = null; }
                }
            }

            if (paused) {                    // CHANGED
                Thread.sleep(16);            // ~60fps enquanto pausado
                continue;                    // CHANGED: não atualiza jogo
            }

            // Update player direction based on mouse input
            if (mouse.isActive()) {
                double dx = mouse.getMouseX() - player.getX();
                double dy = mouse.getMouseY() - player.getY();
                player.setDirection(dx, dy);
            }
            // I → invencibilidade
            if (keys.pollCheatInvincible()) {
                player.activateInvincibilityMillis(8000);
            }

            //CHEAT tecla I deixa invencivel
            if (keys.pollCheatInvincible()) {
                player.activateInvincibilityMillis(8000); // 8000 ms = 8 segundos
            }
            // Update all players
            for (BasePlayer p : players) {
                if (!p.isAlive()) continue;

                p.update(dt, worldW, worldH);

                // pallets -> pontos (só contam para o humano; BasePlayer já retorna pts só do humano)
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
                    // handle fight logic e acrescenta pontos quando o Player elimina Bot
                    int gainedVs = p1.checkPlayerCollision(p2, allPallets);
                    if (gainedVs > 0) {
                        score += gainedVs; // (BOT_POINTS já aplicado no BasePlayer)
                        if (score > highScore) { highScore = score; }
                        updateHud();
                    }
                }
            }

            // win condition: check if all bots are dead
            boolean anyBotAlive = false;
            for (BasePlayer p : players) {
                if (p instanceof BotPlayer && p.isAlive()) {
                    anyBotAlive = true;
                    break;
                }
            }
            if (!anyBotAlive) {
                showWinBanner();
                break;
            }

            // control frame rate
            Thread.sleep((int)(dt * 1000));
        }

        // save highscore
        highScore = highMan.updateIfBest(score, highScore);
        // click to restart
        double[] click = new double[2];
        while (true) {
            if (mouse.pollClick(click)) {
                clearBackground();
                run();   //restart
                return;  // sai desta execução
            }
            Thread.sleep(16);
        }
    }
    private void clearBackground() {
        // desenha um retângulo maior que a área de jogo
        Rectangle bg = new Rectangle(0, 0, 2000, 1200);
        bg.setColor(new Color(240, 244, 248)); // mesma cor do fundo
        bg.fill();
    }

    private void updateHud() {
        // apaga textos anteriores
        if (hudScore != null) hudScore.delete();
        if (hudHigh  != null) hudHigh.delete();

        int x = Grid.PADDING + 6;
        int y = Grid.PADDING - 1;

        hudScore = new Text(x, y, "Score: " + score);
        hudScore.setColor(Color.DARK_GRAY);
        hudScore.draw();

        hudHigh  = new Text(x + 140, y, "High: " + highScore);
        hudHigh.setColor(Color.DARK_GRAY);
        hudHigh.draw();
    }

    private void showWinBanner() {  // CHANGED
        if (winText != null) winText.delete();
        winText = new Text(Grid.PADDING + 250, Grid.PADDING + 20, "YOU WIN!");
        winText.grow(10, 10);
        winText.setColor(Color.BLACK);
        winText.draw();
    }
}
