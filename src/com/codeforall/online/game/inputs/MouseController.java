package com.codeforall.online.game.inputs;

import com.codeforall.simplegraphics.mouse.Mouse;
import com.codeforall.simplegraphics.mouse.MouseEvent;
import com.codeforall.simplegraphics.mouse.MouseEventType;
import com.codeforall.simplegraphics.mouse.MouseHandler;

public class MouseController implements MouseHandler {

    private Mouse mouse;
    private volatile double mouseX;
    private volatile double mouseY;
    private volatile boolean active= false;

    public MouseController(){
        this.mouse= new Mouse(this);
        mouse.addEventListener(MouseEventType.MOUSE_MOVED);
    }


@Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        mouseX= mouseEvent.getX();
        mouseY= mouseEvent.getY();
        active=true;
        //System.out.println("mouseMoved: " + mouseX + ", " + mouseY);
        //sout é pra testar se o metodo ta a ser chamado

    }

    public boolean isActive() {
        return active;
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }
}
