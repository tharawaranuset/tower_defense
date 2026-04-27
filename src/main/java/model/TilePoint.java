package model;

import util.GameConfig;

/**
 * TilePoint.java
 * แทน coordinate ของ tile บน map โดยใช้ col และ row แทน x และ y
 */
public class TilePoint {

    private final int col;
    private final int row;

    public TilePoint(int col, int row) {
        col = Math.max(0, col);
        col = Math.min(col, GameConfig.COLS - 1);

        row = Math.max(0, row);
        row = Math.min(row, GameConfig.ROWS - 1);

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