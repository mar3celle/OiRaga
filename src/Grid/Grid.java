package Grid;

import com.codeforall.simplegraphics.graphics.Rectangle;
import com.codeforall.simplegraphics.graphics.Color;

public class Grid {

    public static final int PADDING = 10;
    private int cellSize = 30;
    private int cols;
    private int rows;

    public Grid(int cols, int rows){
        this.cols = cols;
        this.rows = rows;
    }

    public void init(){
        Rectangle gameField = new Rectangle(PADDING, PADDING, cols * cellSize, rows * cellSize);
        gameField.setColor(Color.BLACK);
        gameField.draw();

        // Desenha linhas horizontais
        for(int r = 1; r < rows; r++){
            Rectangle line = new Rectangle(PADDING, PADDING + r * cellSize, cols * cellSize, 1);
            line.setColor(Color.LIGHT_GRAY);
            line.fill();
        }

        // Desenha linhas verticais
        for(int c = 1; c < cols; c++){
            Rectangle line = new Rectangle(PADDING + c * cellSize, PADDING, 1, rows * cellSize);
            line.setColor(Color.LIGHT_GRAY);
            line.fill();
        }
    }

    public int getCellSize() { return cellSize; }
    public int getCols() { return cols; }
    public int getRows() { return rows; }

    public int rowToY(int row) { return PADDING + cellSize * row; }
    public int columnToX(int column) { return PADDING + cellSize * column; }

    public int[] getRandomCell() {
        int row = (int)(Math.random() * rows);
        int col = (int)(Math.random() * cols);
        return new int[]{row, col};
    }
}
