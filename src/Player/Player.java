package Player;


import com.codeforall.simplegraphics.graphics.Color;
import com.codeforall.simplegraphics.graphics.Ellipse;
import java.util.Random;

public class Player {

    private double x, y;
    private double dx, dy;
    private double radius;
    private boolean alive = true;

    private Color playerColor;
    private static final double BASE_SPEED = 1800.0;
    private static final double SPEED_RADIUS_FLOOR = 12.0;

    // manter a última forma para limpar no próximo frame
    private Ellipse lastShape;

    public Player(double x, double y, double radius){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.playerColor = getRandomColor();
    }

    private Color getRandomColor(){
        Random rand = new Random();
        int r = rand.nextInt(256);
        int g = rand.nextInt(256);
        int b = rand.nextInt(256);
        return new Color(r, g, b);
    }
    // Define A direção (Se dx=dy=0 está parado)
    public void setDirection(double dx, double dy){
        double len = Math.hypot(dx, dy);
        if (len < 1e-6) {
            this.dx = 0; this.dy = 0;
            return;
        }
        this.dx = dx / len;
        this.dy = dy / len;
    }

    // Velocidade atual (px/s) — desacelera com o tamanho
    private double getSpeed(){
        return BASE_SPEED / Math.max(SPEED_RADIUS_FLOOR, radius);
    }
    // Atualiza posição com dt em segundos e faz nao passa limites do world.
    public void updatePosition(double dt, int worldW, int worldH){
        double v = getSpeed();
        x += dx * v * dt;
        y += dy * v * dt;

        x = Math.max(radius, Math.min(worldW - radius, x));
        y = Math.max(radius, Math.min(worldH - radius, y));
    }
    // Desenha o círculo; apaga o anterior
    public void draw(){
        if (lastShape != null) lastShape.delete();

        int d  = (int) Math.round(radius * 2);
        int sx = (int) Math.round(x - radius);
        int sy = (int) Math.round(y - radius);

        Ellipse circle = new Ellipse(sx, sy, d, d);
        circle.setColor(playerColor);
        circle.fill();

        lastShape = circle;
    }
    // Cresce por área
    public void growByArea(double areaToAdd){
        double myArea = Math.PI * radius * radius; // calcula a área atual
        myArea += areaToAdd;                       // adiciona a nova área
        radius = Math.sqrt(myArea / Math.PI);      // converte de volta para raio
    }


    public void delete(){ alive = false; if (lastShape != null) lastShape.delete(); }
    public boolean isAlive(){ return alive; }

    // getters
    public double getX(){ return x; }
    public double getY(){ return y; }
    public double getRadius(){ return radius; }
}

