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
    //click on mouse to exit or restart
    private volatile boolean clicked = false;
    private volatile double clickX;
    private volatile double clickY;

    public MouseController(){
        this.mouse= new Mouse(this);
        mouse.addEventListener(MouseEventType.MOUSE_MOVED);
        mouse.addEventListener(MouseEventType.MOUSE_CLICKED);
    }


@Override
    public void mouseClicked(MouseEvent mouseEvent) {
    clickX= mouseEvent.getX();
    clickY= mouseEvent.getY();
    clicked= true;
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        mouseX= mouseEvent.getX();
        mouseY= mouseEvent.getY();
        active=true;
        //System.out.println("mouseMoved: " + mouseX + ", " + mouseY);

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
    //devolve o click /para de clicar
    public boolean pollClick(double[] xyOut){
        if (!clicked) return false;
        xyOut[0] = clickX;
        xyOut[1] = clickY;
        clicked = false;
        return true;
    }
}
