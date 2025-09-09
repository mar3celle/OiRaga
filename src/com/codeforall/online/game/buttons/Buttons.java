package com.codeforall.online.game.buttons;

import com.codeforall.online.game.Game;
import com.codeforall.online.game.inputs.MouseMenus;
import com.codeforall.simplegraphics.pictures.Picture;

public  class Buttons {
    private double x;
    private double xMax;
    private double y;
    private double yMax;
    private Picture picture;

    public Buttons(double xValue, double yValue, String src) {
        this.picture = new Picture(xValue, yValue, src);
        this.picture.draw();
        this.x = xValue;
        this.y = yValue;
        this.xMax = picture.getMaxX();
        this.yMax = picture.getMaxY();
    }

    public double getX() {
        return x;
    }

    public double getxMax() {
        return xMax;
    }

    public double getY() {
        return y;
    }

    public double getyMax() {
        return yMax;
    }

    public void delete() {
        this.picture.delete();
    }

    public void load(String s){
        this.picture.load(s);
    }
}
