package model.entity.enemy;

import javafx.scene.paint.Color;
import model.TilePoint;

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
        this.sizeRender -= 4;
        getSpriteSheet().setFrameDelayMs(80);
    }

    @Override
    protected Color getBodyColor() {
        return Color.YELLOW;
    }

}