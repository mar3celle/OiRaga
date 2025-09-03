package com.codeforall.online.game.Grid;

import com.codeforall.simplegraphics.graphics.Rectangle;
import com.codeforall.simplegraphics.graphics.Color;

public class Grid {

    // Padding (border) around the playable area
    public static final int PADDING = 10;

    // Each cell size in pixels
    private int cellSize = 15;

    // Number of columns and rows in the grid
    private int cols;
    private int rows;

    // Constructor defines the grid size in cells
    public Grid(int cols, int rows){
        this.cols = cols;
        this.rows = rows;
    }

    // Initialize and draw the game field (grid background + grid lines)
    public void init(){
        // Outer border (black rectangle)
        Rectangle gameField = new Rectangle(PADDING, PADDING, cols * cellSize, rows * cellSize);
        gameField.setColor(Color.BLACK);
        gameField.draw();

        // Draw horizontal lines
        for(int r = 1; r < rows; r++){
            Rectangle line = new Rectangle(PADDING, PADDING + r * cellSize, cols * cellSize, 1);
            line.setColor(Color.LIGHT_GRAY);
            line.fill();
        }

        // Draw vertical lines
        for(int c = 1; c < cols; c++){
            Rectangle line = new Rectangle(PADDING + c * cellSize, PADDING, 1, rows * cellSize);
            line.setColor(Color.LIGHT_GRAY);
            line.fill();
        }
    }

    // Getters
    public int getCellSize() { return cellSize; }
    public int getCols() { return cols; }
    public int getRows() { return rows; }

    public int getGameFieldMaxX(){
        System.out.println(this.cols * cellSize);
        return this.cols * cellSize;
    }

    public int getGameFieldMaxY(){
        System.out.println(this.rows * cellSize);
        return this.rows * cellSize;
    }

    // Convert a row index to Y coordinate (top-left origin with padding)
    public int rowToY(int row) { return PADDING + cellSize * row; }

    // Convert a column index to X coordinate
    public int columnToX(int column) { return PADDING + cellSize * column; }

    // Pick a random cell inside the grid
    public int[] getRandomCell() {
        int row = (int)(Math.random() * rows);
        int col = (int)(Math.random() * cols);
        return new int[]{row, col};
    }
}
