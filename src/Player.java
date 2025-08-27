import com.codeforall.simplegraphics.graphics.Color;
import com.codeforall.simplegraphics.graphics.Ellipse;

public class Player {

    protected double x, y;        // posição
    protected double dx, dy;      // direção
    protected double radius;      // raio atual
    protected boolean alive = true;


    private static final double BASE_SPEED = 600.0;         // px/s quando pequeno
    private static final double SPEED_RADIUS_FLOOR = 12.0;  // nao fica muito rapido quando pequeno


    // manter a última forma para limpar no próximo frame
    private Ellipse lastShape;

    public Player(double x, double y, double radius){
        this.x = x;
        this.y = y;
        this.radius = radius;
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

    // Velocidade atual (px/s) — desacelera com o tamanho ______________
    protected double getSpeed(){
        return BASE_SPEED / Math.max(SPEED_RADIUS_FLOOR, radius);
    }

    // Atualiza posição com dt em segundos e faz nao passa limites do world. __________
    public void updatePosition(double dt, int worldW, int worldH){
        double v = getSpeed();
        x += dx * v * dt;
        y += dy * v * dt;

        x = Math.max(radius, Math.min(worldW - radius, x)); //limites
        y = Math.max(radius, Math.min(worldH - radius, y));
    }

    // Desenha o círculo; apaga o anterior ____________
    public void draw(Color color){
        if (lastShape != null) lastShape.delete();

        int d  = (int) Math.round(radius * 2);
        int sx = (int) Math.round(x - radius);
        int sy = (int) Math.round(y - radius);

        Ellipse circle = new Ellipse(sx, sy, d, d);
        circle.setColor(color);
        circle.fill();

        lastShape = circle;
    }
    public void draw(){
        draw(Color.BLUE);
    }

    // Cresce por área _____________________________
    public void growByArea(double areaToAdd){
        double myArea = Utils.areaFromRadius(radius);
        myArea += areaToAdd;
        radius = Utils.radiusFromArea(myArea);
    }

    public void delete(){ alive = false; if (lastShape != null) lastShape.delete(); }
    public boolean isAlive(){ return alive; }

    // getters
    public double getX(){ return x; }
    public double getY(){ return y; }
    public double getRadius(){ return radius; }


}

