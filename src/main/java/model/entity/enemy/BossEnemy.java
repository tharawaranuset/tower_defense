package model.entity.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.TilePoint;

import java.util.List;

/**
 * BossEnemy.java
 * enemy ที่แข็งที่สุด มี armor ลด damage ที่รับ hp สูงมาก reward มากที่สุด
 */
public class BossEnemy extends Enemy {

    private int armor;

    public BossEnemy(List<TilePoint> path) {
        super(
                path.getFirst().getCol(),
                path.getFirst().getRow(),
                500,
                1.5,
                50,
                path
        );
        setArmor(10);
        getSpriteSheet().setFrameDelayMs(200);
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    @Override
    public void takeDamage(int dmg) {
        int reduced = Math.max(1, dmg - armor);
        super.takeDamage(reduced);
    }

    @Override
    protected Color getBodyColor() {
        return Color.DARKVIOLET;
    }

    // bigger than others
    @Override
    public void render(GraphicsContext gc) {
        // circle around boss
        gc.setStroke(Color.GOLD);
        gc.setLineWidth(2);
        gc.strokeOval(
                getPixelX() - sizeRender,
                getPixelY() - sizeRender,
                sizeRender * 2,
                sizeRender * 2
        );

        super.render(gc);
    }
}