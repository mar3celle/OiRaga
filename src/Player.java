import com.codeforall.simplegraphics.graphics.Color;
import com.codeforall.simplegraphics.graphics.Ellipse;

public class Player {

    private double x, y;        //posição atual
    private double dx , dy;     //direção atual
    private double radius;  //raio da celula
    private boolean alive = true;   //verifica se esta vivo ou perdeu

    private static final Color PLAYER_COLOR = Color.BLUE;
    private static final Color BOT_COLOR = Color.RED;

    private final boolean isPlayer;     //identificar se é p ou bot

    private static final double BASE_SPEED= 240.0; //volocidade

    private Player(double x, double y, double radius, boolean isPlayer){
        this.x=x;
        this.y=y;
        this.radius= radius;
        this.isPlayer= isPlayer;
                        //uso um construtor privado p regrar futuros players criados
    }

    public static Player createPlayer( double x, double y){
        return new Player(x, y, 12.0, true);
    }   //cria um player de 12px vivo

    public static Player createBot(double x, double y){
    return new Player(x,y, 10.0 + Math.random() *8, false);
    //cria um bot de tamanho aleatorio entre 10 e 18 px
    }

    //movimento ____________________________________

    public void setDirection(double dx, double dy){

    }
    public void updatePosition(){

    }

    //DESENHO_____________________________________
    public void draw(){
        int d = (int) Math.round(radius * 2);         // diâmetro
        int sx = (int) Math.round(x - radius);        // canto superior-esquerdo
        int sy = (int) Math.round(y - radius);

        Ellipse circle = new Ellipse(sx, sy, d, d);
        circle.setColor(isPlayer? PLAYER_COLOR : BOT_COLOR);
        circle.fill();
    }

    //crescimento
    public void growByArea(double areaToAdd){
        double myArea= Utils.areaFromRadius(radius);
        myArea += areaToAdd;            //soma a area do que comeu
        radius = Utils.radiusFromArea(myArea);              //devolve raio atualizado
    }

        //delete mata o player
    public void delete(){
        alive = false;
    }
    public boolean isAlive(){
        return alive;
    }
    // getters ______________________
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getRadius() {
        return radius;
    }
    public boolean isHuman() {
        return isPlayer;
    }





}
