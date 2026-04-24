package model;

import main.Main;

public class TilePoint {

    private final int col;
    private final int row;

    public TilePoint(int col, int row) {
        col = Math.max(0, col);
        col = Math.min(col, Main.COLS - 1);

        row = Math.max(0, row);
        row = Math.min(row, Main.ROWS - 1);

        this.col = col;
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

}