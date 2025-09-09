package com.codeforall.online.game.inputs;

import com.codeforall.simplegraphics.keyboard.Keyboard;
import com.codeforall.simplegraphics.keyboard.KeyboardEvent;
import com.codeforall.simplegraphics.keyboard.KeyboardEventType;
import com.codeforall.simplegraphics.keyboard.KeyboardHandler;

public class MyKeyboard implements KeyboardHandler {

    private final Keyboard keyboard;

    // flags one-shot (consumidas pelo poll)
    private boolean cheatInvPressed = false;   // tecla I
    private boolean pausePressed = false;   // tecla P
    private boolean exitPressed = false;   // tecla ESC

    public MyKeyboard() {
        keyboard = new Keyboard(this);

        // I -> invencibilidade cheat “easy mode”
        KeyboardEvent iPress = new KeyboardEvent();
        iPress.setKey(KeyboardEvent.KEY_I);
        iPress.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(iPress);

        //  P -> pause, ESC -> exit
        KeyboardEvent pPress = new KeyboardEvent();
        pPress.setKey(KeyboardEvent.KEY_P);
        pPress.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(pPress);

        KeyboardEvent escPress = new KeyboardEvent();
        escPress.setKey(KeyboardEvent.KEY_ESC);
        escPress.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(escPress);
    }

    @Override
    public void keyPressed(KeyboardEvent e) {
        int k = e.getKey();
        if (k == KeyboardEvent.KEY_I) {
            cheatInvPressed = true;
        } else if (k == KeyboardEvent.KEY_P) {
            pausePressed = true;
        } else if (k == KeyboardEvent.KEY_ESC) {
            exitPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyboardEvent e) {
    }

    // devolve true uma única vez quando I é pressionado
    public boolean pollCheatInvincible() {
        if (!cheatInvPressed) return false;
        cheatInvPressed = false;
        return true;
    }

    public boolean pollPause() {
        if (!pausePressed) return false;
        pausePressed = false;
        return true;
    }

    public boolean pollExit() {
        if (!exitPressed) return false;
        exitPressed = false;
        return true;
    }
}
