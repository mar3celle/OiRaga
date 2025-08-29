package Player;

import com.codeforall.simplegraphics.graphics.Ellipse;
import com.codeforall.simplegraphics.graphics.Color;
import java.util.Random;

public class Player {

    // Posição do player
    private double x, y;
    // Direção do movimento (normalizada)
    private double dx, dy;
    // Raio do player
    private double radius;
    // Estado de vida
    private boolean alive = true;
    // Cor do player
    private Color playerColor;
    // Velocidade base
    private static final double BASE_SPEED = 2000.0;
    // Raio mínimo que influencia desaceleração
    private static final double SPEED_RADIUS_FLOOR = 20.0;

    // Último desenho do player para poder apagar no próximo frame
    private Ellipse lastShape;

    // Construtor
    public Player(double x, double y, double radius){
        this.x = x;             // posição X inicial
        this.y = y;             // posição Y inicial
        this.radius = radius;   // tamanho inicial
        this.playerColor = getRandomColor(); // cor aleatória
    }

    // Gera uma cor aleatória
    private Color getRandomColor(){
        Random rand = new Random();
        int r = rand.nextInt(256); // vermelho
        int g = rand.nextInt(256); // verde
        int b = rand.nextInt(256); // azul
        return new Color(r, g, b);
    }

    // Define a direção do player a partir do vetor dx, dy
    // Se dx=dy=0, o player fica parado
    public void setDirection(double dx, double dy){
        double len = Math.hypot(dx, dy); // comprimento do vetor
        if (len < 1e-6) {                // evita divisão por zero
            this.dx = 0; this.dy = 0;
            return;
        }
        this.dx = dx / len;  // normaliza vetor
        this.dy = dy / len;
    }

    // Calcula velocidade atual em px/s
    // Desacelera à medida que o player cresce
    private double getSpeed(){
        return BASE_SPEED / Math.max(SPEED_RADIUS_FLOOR, radius);
    }

    // Atualiza a posição do player com delta time dt
    // Garante que o player não ultrapassa os limites do mundo
    public void updatePosition(double dt, int worldW, int worldH){
        double v = getSpeed();
        x += dx * v * dt;
        y += dy * v * dt;

        // Mantém dentro do mundo
        x = Math.max(radius, Math.min(worldW - radius, x));
        y = Math.max(radius, Math.min(worldH - radius, y));
    }

    // Desenha o player como um círculo
    // Apaga desenho anterior
    public void draw(){
        if (lastShape != null) lastShape.delete();

        int d  = (int) Math.round(radius * 2);       // diâmetro
        int sx = (int) Math.round(x - radius);       // posição X do canto superior esquerdo
        int sy = (int) Math.round(y - radius);       // posição Y do canto superior esquerdo

        Ellipse circle = new Ellipse(sx, sy, d, d);
        circle.setColor(playerColor);
        circle.fill();

        lastShape = circle; // guarda para apagar no próximo frame
    }

    // Cresce pela área adicionada (pallets)
    public void growByArea(double areaToAdd){
        double myArea = Math.PI * radius * radius; // área atual
        myArea += areaToAdd;                       // adiciona nova área
        radius = Math.sqrt(myArea / Math.PI);      // converte de volta para raio
    }

    // Mata o player e apaga o desenho
    public void delete(){
        alive = false;
        if (lastShape != null) lastShape.delete();
    }

    // Verifica se o player está vivo
    public boolean isAlive(){ return alive; }

    // Getters para posição e raio
    public double getX(){ return x; }
    public double getY(){ return y; }
    public double getRadius(){ return radius; }
}


