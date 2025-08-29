package Pallets;

import com.codeforall.simplegraphics.graphics.Color;
import com.codeforall.simplegraphics.graphics.Ellipse;
import java.util.Random;

public class Pallets {

    // Raio da pallet
    private int radius;
    // Posição da pallet no ecrã
    private int x, y;
    // Estado: true se a pallet foi comida
    private boolean isEaten;
    // Cor da pallet
    private Color palletColor;
    // Último desenho da pallet para poder apagar
    private Ellipse lastShape;

    // Controle de respawn
    private long respawnTime = 0; // momento em ms em que vai reaparecer
    private static final int MIN_RESPAWN = 3000;  // 3 segundos mínimo
    private static final int MAX_RESPAWN = 20000; // 20 segundos máximo

    // Construtor
    public Pallets(int radius, int x, int y, boolean isEaten){
        this.radius = radius;     // define o tamanho
        this.x = x;               // posição X
        this.y = y;               // posição Y
        this.isEaten = isEaten;   // define se já foi comida
        this.palletColor = getRandomColor(); // cor aleatória
    }

    // Gera uma cor aleatória para a pallet
    private Color getRandomColor(){
        Random rand = new Random();
        int r = rand.nextInt(256); // componente vermelha
        int g = rand.nextInt(256); // componente verde
        int b = rand.nextInt(256); // componente azul
        return new Color(r, g, b);
    }

    // Desenha a pallet
    public void draw() {
        // Apaga desenho anterior, se existir
        if (lastShape != null) lastShape.delete();
        int d = radius * 2; // largura/altura do círculo
        lastShape = new Ellipse(x - radius, y - radius, d, d); // cria novo círculo
        lastShape.setColor(palletColor); // define cor
        lastShape.fill(); // preenche
    }

    // Apaga a pallet do ecrã
    public void delete() {
        if (lastShape != null) lastShape.delete();
    }

    // Agenda respawn aleatório após ser comida
    public void scheduleRespawn() {
        Random rand = new Random();
        int interval = MIN_RESPAWN + rand.nextInt(MAX_RESPAWN - MIN_RESPAWN + 1); // intervalo 3-20s
        respawnTime = System.currentTimeMillis() + interval; // calcula momento de respawn
        isEaten = true; // marca como comida
    }

    // Verifica se já passou o tempo para reaparecer
    public boolean readyToRespawn() {
        return isEaten && System.currentTimeMillis() >= respawnTime;
    }

    // Reposiciona e redesenha a pallet
    public void respawn(int newX, int newY) {
        x = newX;      // nova posição X
        y = newY;      // nova posição Y
        isEaten = false; // marca como ativa
        draw();        // desenha no ecrã
    }

    // Getters / Setters
    public boolean isEaten() { return isEaten; }
    public void setEaten(boolean eaten) { this.isEaten = eaten; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getRadius() { return radius; }
}

