package com.codeforall.online.game.input;

import com.codeforall.online.game.players.PlayerMain;
import com.codeforall.simplegraphics.keyboard.Keyboard;
import com.codeforall.simplegraphics.keyboard.KeyboardEvent;
import com.codeforall.simplegraphics.keyboard.KeyboardEventType;
import com.codeforall.simplegraphics.keyboard.KeyboardHandler;

public class PlayerKeyboard implements KeyboardHandler {

    private Keyboard keyboard;
    private PlayerMain player;

    public PlayerKeyboard(PlayerMain player){
        this.keyboard = new Keyboard(this);

        initKeys();
        this.player = player;

    }

    public void initKeys(){
        KeyboardEvent right = new KeyboardEvent();
        right.setKey(KeyboardEvent.KEY_RIGHT);
        right.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(right);

        KeyboardEvent left = new KeyboardEvent();
        left.setKey(KeyboardEvent.KEY_LEFT);
        left.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(left);

        KeyboardEvent up = new KeyboardEvent();
        up.setKey(KeyboardEvent.KEY_UP);
        up.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(up);

        KeyboardEvent down = new KeyboardEvent();
        down.setKey(KeyboardEvent.KEY_DOWN);
        down.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(down);
    }

    @Override
    public void keyPressed(KeyboardEvent keyboardEvent) {
        switch (keyboardEvent.getKey()){
            case KeyboardEvent.KEY_RIGHT -> player.moveRight();
            case KeyboardEvent.KEY_LEFT -> player.moveLeft();
            case KeyboardEvent.KEY_UP -> player.moveUp();
            case KeyboardEvent.KEY_DOWN -> player.moveDown();
        }
    }

    @Override
    public void keyReleased(KeyboardEvent keyboardEvent) {
    }


}
