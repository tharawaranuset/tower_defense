package model.entity.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.TilePoint;
import util.GameConfig;

import java.util.List;

/**
 * FastEnemy.java
 * enemy ที่วิ่งเร็วที่สุด hp น้อย ต้องวาง tower ให้ครอบคลุมตลอดเส้นทาง
 */
public class FastEnemy extends Enemy {

    public FastEnemy(List<TilePoint> path) {
        super(
                path.getFirst().getCol(),
                path.getFirst().getRow(),
                50,
                5.0,
                15,
                path
        );
    }

    @Override
    protected Color getBodyColor() {
        return Color.YELLOW;
    }

    // less than others
    @Override
    public void render(GraphicsContext gc) {
        double r = GameConfig.TILE_SIZE / 2.0 - 8;
        gc.setFill(getBodyColor());
        gc.fillOval(getPixelX() - r, getPixelY() - r, r * 2, r * 2);

        if (isSlowed()) {
            gc.setStroke(Color.CYAN);
            gc.setLineWidth(2);
            gc.strokeOval(getPixelX() - r, getPixelY() - r, r * 2, r * 2);
        }

        drawHpBar(gc, r);
    }
}