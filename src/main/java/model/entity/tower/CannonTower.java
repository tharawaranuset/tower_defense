package model.entity.tower;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.Main;
import model.Projectile;
import model.SplashProjectile;
import model.entity.enemy.Enemy;

import java.util.List;

public class CannonTower extends Tower {

    private int splashRadius;

    // enemiesRef is for splash
    private List<Enemy> enemiesRef;

    public CannonTower(int tileX, int tileY) {
        super(
                tileX,
                tileY,
                120,
                2,
                40,
                80,
                1500
        );
        setSplashRadius(60);
    }

    public void setSplashRadius(int splashRadius) {
        this.splashRadius = splashRadius;
    }

    public void setEnemiesRef(List<Enemy> enemies) {
        this.enemiesRef = enemies;
    }

    @Override
    public Projectile attack(Enemy target) {
        if (isNotReady() || enemiesRef == null) return null;
        resetCooldown();
        double cx = (getTileX() * Main.TILE_SIZE) + (Main.TILE_SIZE / 2.0);
        double cy = (getTileY() * Main.TILE_SIZE) + (Main.TILE_SIZE / 2.0);
        return new SplashProjectile(
                cx,
                cy,
                target,
                getDamage(),
                splashRadius,
                enemiesRef
        );
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
        double x = getTileX() * Main.TILE_SIZE;
        double y = getTileY() * Main.TILE_SIZE;
        double s = Main.TILE_SIZE;

        // cannon
        gc.setFill(Color.DIMGRAY);
        double cx = x + s / 2.0;
        double cy = y + s / 2.0;
        gc.fillRoundRect(cx - 6, cy - 14, 12, 20, 4, 4);

        // base, circle
        gc.setFill(Color.SLATEGRAY);
        gc.fillOval(cx - 10, cy - 6, 20, 20);
    }
}