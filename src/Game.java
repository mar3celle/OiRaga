import Input.MouseController;
import Pallets.Pallets;
import Player.Player;
import Grid.Grid;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class Game {

    public static void main(String[] args) throws InterruptedException {

        // Cria e inicializa a grid do jogo (50 colunas x 30 linhas)
        Grid grid = new Grid(50, 30);
        grid.init();

        // Lista para guardar todas as pallets
        List<Pallets> pallets = new ArrayList<>();
        // Conjunto para controlar células ocupadas e evitar sobreposição
        Set<String> occupiedCells = new HashSet<>();
        int maxPallets = 100;  // número máximo de pallets
        int palletRadius = 8;   // raio de cada pallet

        // Criar pallets iniciais sem sobreposição
        while (pallets.size() < maxPallets) {
            int[] cell = grid.getRandomCell();  // obtém uma célula aleatória
            int row = cell[0];
            int col = cell[1];
            String key = row + "," + col;

            // Se a célula já estiver ocupada, tenta outra
            if (occupiedCells.contains(key)) continue;

            // Calcula o centro da célula para posicionar a pallet
            int centerX = grid.columnToX(col) + grid.getCellSize() / 2;
            int centerY = grid.rowToY(row) + grid.getCellSize() / 2;

            // Cria a pallet, adiciona à lista e marca a célula como ocupada
            Pallets pallet = new Pallets(palletRadius, centerX, centerY, false);
            pallets.add(pallet);
            occupiedCells.add(key);
            pallet.draw();  // desenha a pallet na tela
        }

        // Spawn do player em uma célula aleatória
        int[] cell = grid.getRandomCell();
        int row = cell[0];
        int col = cell[1];

        double playerX = grid.columnToX(col) + grid.getCellSize() / 2.0;
        double playerY = grid.rowToY(row) + grid.getCellSize() / 2.0;

        // Cria o player com raio inicial 12 e desenha
        Player player = new Player(playerX, playerY, 12.0);
        player.draw();

        // Inicializa o controlador do mouse
        MouseController mouse = new MouseController();
        double dt = 0.016; // intervalo por frame (~60 FPS)

        Random rand = new Random(); // gerador de números aleatórios

        // Loop principal do jogo
        while (player.isAlive()) {

            // Captura input do mouse e define direção do player
            if (mouse.isActive()) {
                double dx = mouse.getMouseX() - player.getX();
                double dy = mouse.getMouseY() - player.getY();
                player.setDirection(dx, dy);
            }

            // Atualiza posição do player e redesenha
            player.updatePosition(dt,
                    grid.getCols() * grid.getCellSize() + Grid.PADDING,
                    grid.getRows() * grid.getCellSize() + Grid.PADDING);
            player.draw();

            // Verifica colisões com pallets e controla respawn
            for (Iterator<Pallets> it = pallets.iterator(); it.hasNext();) {
                Pallets pallet = it.next();

                if (!pallet.isEaten()) {  // se a pallet estiver ativa
                    double dx = pallet.getX() - player.getX();
                    double dy = pallet.getY() - player.getY();
                    double dist = Math.hypot(dx, dy); // distância player → pallet

                    // Se houver colisão
                    if (dist < player.getRadius() + pallet.getRadius()) {
                        pallet.delete(); // remove a pallet da tela
                        // Cresce o player com 30% da área do pallet
                        double area = Math.PI * Math.pow(pallet.getRadius(), 2) * 0.3;
                        player.growByArea(area);

                        pallet.scheduleRespawn(); // agenda respawn aleatório
                    }

                } else if (pallet.readyToRespawn()) { // se estiver pronta para reaparecer
                    // encontra nova célula livre
                    int[] newCell;
                    String key;
                    do {
                        newCell = grid.getRandomCell();
                        key = newCell[0] + "," + newCell[1];
                    } while (occupiedCells.contains(key));

                    // Calcula centro da nova célula e reposiciona a pallet
                    int centerX = grid.columnToX(newCell[1]) + grid.getCellSize() / 2;
                    int centerY = grid.rowToY(newCell[0]) + grid.getCellSize() / 2;
                    occupiedCells.add(key);

                    pallet.respawn(centerX, centerY); // redesenha a pallet
                }
            }

            // Pausa o loop para manter ~60 FPS
            Thread.sleep((int) (dt * 1000));
        }
    }
}
