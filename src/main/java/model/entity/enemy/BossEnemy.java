package model.entity.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.Main;
import model.TilePoint;

import java.util.List;

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
        double r = Main.TILE_SIZE / 2.0;
        gc.setFill(getBodyColor());
        gc.fillOval(getPixelX() - r, getPixelY() - r, r * 2, r * 2);

        // circle around boss
        gc.setStroke(Color.GOLD);
        gc.setLineWidth(2);
        gc.strokeOval(getPixelX() - r, getPixelY() - r, r * 2, r * 2);

        if (isSlowed()) {
            gc.setStroke(Color.CYAN);
            gc.setLineWidth(2);
            gc.strokeOval(getPixelX() - r - 3, getPixelY() - r - 3, r * 2 + 6, r * 2 + 6);
        }

        drawHpBar(gc, r);
    }
}