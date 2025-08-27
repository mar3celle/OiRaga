import com.codeforall.simplegraphics.graphics.Color;

import java.util.Random;

public class Bot extends Player {
    private static final Color BOT_COLOR = Color.RED;
    private double targetX, targetY;
    private static final double SPEED_MULT = 0.85; //85% da velocidade do P

    public Bot(double x, double y, double radius) {
        super(x, y, radius);  // alvo
        this.targetX = x;
        this.targetY = y;
    }

    public void pickNewTarget(int worldW, int worldH, Random rng) {
        double margin = 30;
        //escolher um alvo aleatorio
        targetX = margin + rng.nextDouble() * (worldW - 2 * margin);
        targetY = margin + rng.nextDouble() * (worldH - 2 * margin);

    }

    public void updateAI(double dt, int worldW, int worldH, Random rng, Player player) {
        // renovar alvo de vez em quando / quando chega perto
        double distToTarget = Math.hypot(targetX - x, targetY - y);
        if (distToTarget < 20 || rng.nextDouble() < 0.005) {
            pickNewTarget(worldW, worldH, rng);
        }

        double dxp = player.getX() - x;
        double dyp = player.getY() - y;
        double distP = Math.hypot(dxp, dyp);

        boolean playerMuchBigger = player.getRadius() >= this.radius * 1.15;
        boolean botMuchBigger = this.radius >= player.getRadius() * 1.15;

        if (distP < 600) {
            if (playerMuchBigger) {
                setDirection(-dxp, -dyp);           // fugir
            } else if (botMuchBigger) {
                setDirection(dxp, dyp);             // perseguir
            } else {
                setDirection(targetX - x, targetY - y); // vaguear
            }
        } else {
            setDirection(targetX - x, targetY - y);
        }
        updatePosition(dt, worldW, worldH);
    }
    @Override
    public void draw(Color color){
        super.draw(Color.RED);
    }
    // colisão com o payer_____________________________
        //o bot é maior que o player?(come)
    public boolean canEatPlayer(Player player){
        if (this.radius < player.getRadius() * 1.15) return false;
        double dist = Math.hypot(player.getX() - x, player.getY() - y);
        return dist < (this.radius - player.getRadius() * 0.2);
    }

    //o bot é menor q o player? (é comido) ________________________
    public boolean isEatenByPlayer(Player player){
        if (player.getRadius() < this.radius * 1.15) return false;
        double dist = Math.hypot(player.getX() - x, player.getY() - y);
        return dist < (player.getRadius() - this.radius * 0.2);
    }
    @Override
    protected double getSpeed() {
        return super.getSpeed() * SPEED_MULT;
    }
}
