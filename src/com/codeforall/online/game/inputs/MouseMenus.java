package com.codeforall.online.game.inputs;

import com.codeforall.online.game.buttons.Buttons;

public class MouseMenus {

    private Buttons button;
    private Runnable action;

    public MouseMenus(Buttons button, Runnable action) {
        this.button = button;
        this.action = action;
    }

    public boolean checkClick(int x, int y) {
        if (x >= button.getX() && x <= button.getxMax() &&
                y >= button.getY() && y <= button.getyMax()) {
            action.run();
            return true;
        }
        return false;
    }
}