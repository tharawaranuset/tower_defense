package model.entity.tower;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.Main;
import model.Projectile;
import model.entity.enemy.Enemy;

public class ArrowTower extends Tower {

    public ArrowTower(int tileX, int tileY) {
        super(
                tileX,
                tileY,
                100,
                3,
                15,
                50,
                800
        );
    }

    @Override
    public Projectile attack(Enemy target) {
        if (isNotReady()) return null;
        resetCooldown();
        double cx = (getTileX() * Main.TILE_SIZE) + (Main.TILE_SIZE / 2.0);
        double cy = (getTileY() * Main.TILE_SIZE) + (Main.TILE_SIZE / 2.0);
        return new Projectile(
                cx,
                cy,
                target,
                getDamage(),
                300,
                Color.PERU,
                4
        );
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
        double x = getTileX() * Main.TILE_SIZE;
        double y = getTileY() * Main.TILE_SIZE;
        double s = Main.TILE_SIZE;

        // upside arrow
        gc.setFill(Color.PERU);
        double cx = x + s / 2.0;
        double cy = y + s / 2.0;
        gc.fillPolygon(
                new double[]{cx, cx - 8, cx + 8},
                new double[]{cy - 12, cy + 8, cy + 8},
                3
        );
    }
}