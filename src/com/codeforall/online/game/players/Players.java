package com.codeforall.online.game.players;
import com.codeforall.online.game.input.PlayerKeyboard;
import com.codeforall.simplegraphics.graphics.Ellipse;

public abstract class Players {

    // Main arguments
    private double size;
    private double startingPositionX;
    private double startingPositionY;
    private Ellipse player;
    private int movementSpeed = 10;
    private String color = "red"; //para depois alterar o interior de cada player


    // Constructor
    public Players(double startingPositionX, double startingPositionY, double size){
        this.size = size;
        Ellipse player = new Ellipse(startingPositionX, startingPositionY, size, size);

        this.player = player;
        player.fill();
    }

    //  Main Movement
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
}
