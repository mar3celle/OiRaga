package com.codeforall.online.game.players;

import com.codeforall.online.game.input.PlayerKeyboard;
import com.codeforall.simplegraphics.graphics.Ellipse;

import java.util.Iterator;

public class PlayerMain {

    private double size;
    private double startingPositionX;
    private double startingPositionY;
    private final Ellipse player;
    private int movementSpeed = 10;
    private String color = "red";


    public PlayerMain(double startingPositionX, double startingPositionY, double size){
        Ellipse player = new Ellipse(startingPositionX, startingPositionY, size, size);
        this.size = size;

        this.player = player;
        player.fill();

        PlayerKeyboard keyboard = new PlayerKeyboard(this);
    }

    public void moveRight(){
        player.translate(movementSpeed,0);
    }

    public void moveLeft(){
        player.translate(-movementSpeed,0);
    }

    public void moveUp(){
        player.translate(0,-movementSpeed);
    }

    public void moveDown(){
        player.translate(0,movementSpeed);
    }

//    colisao entre as apllets


    public void checkPalletCollision(Pallets pallets){
        Iterator<Ellipse> it = pallets.getPallets().iterator();

        while(it.hasNext()){ // percorre todas as pallets ate haver uma colisao
            Ellipse ellipse = it.next();

            if (checkPalletCollision(player, pallets)){
                grow(2);  // aumenta o tamanho
                pallets.delete(); // vai remover a cell "apanhada" / "comida" do ecra
                it.remove();  // remove da lista
            }
        }
    }

    public boolean checkCollision(Ellipse a, Ellipse b) {  // retorna "true" se houver alguma colisao, vai calculando a distancia para detetar as colisoes

        double ax = a.getX() +  a.getWidth() / 2;
        double ay = a.getY() + a.getHeight() / 2;
        double bx = b.getX() +  b.getWidth() / 2;
        double by = b.getY() + b.getHeight() / 2;

        double dx = ax - bx;
        double dy = ay - by;
        double distance = Math.sqrt(dx * dx + dy * dy);

        double radiusA = a.getWidth() / 2;
        double radiusB = b.getWidth() / 2;

        return distance < (radiusA + radiusB);

    }

    private void grow(double increase){  // faz a nossa esfera "player1" aumentar o tamanho
        size += increase;
        player.grow(increase, increase);
    }



}