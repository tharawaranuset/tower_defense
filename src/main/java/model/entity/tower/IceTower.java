package model.entity.tower;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.Projectile;
import model.entity.enemy.Enemy;
import util.GameConfig;

/**
 * IceTower.java
 * tower ที่ทำให้ enemy ช้าลงชั่วคราว damage ต่ำแต่ช่วย tower อื่นได้มาก
 */
public class IceTower extends Tower {

    private double slowMultiplier;
    private long slowDurationMs;

    public IceTower(int tileX, int tileY) {
        super(
                tileX,
                tileY,
                80,
                3,
                5,
                70,
                1200
        );
        setSlowMultiplier(0.4);
        setSlowDurationMs(2000);
        setCanHitFlying(false);
    }

    public void setSlowMultiplier(double slowMultiplier) {
        this.slowMultiplier = slowMultiplier;
    }

    public void setSlowDurationMs(long slowDurationMs) {
        this.slowDurationMs = slowDurationMs;
    }

    @Override
    public Projectile attack(Enemy target) {
        if (isNotReady()) return null;
        resetCooldown();
        double cx = (getTileX() * GameConfig.TILE_SIZE) + (GameConfig.TILE_SIZE / 2.0);
        double cy = (getTileY() * GameConfig.TILE_SIZE) + (GameConfig.TILE_SIZE / 2.0);
        return new Projectile(
                cx,
                cy,
                target,
                getDamage(),
                250,
                Color.CYAN,
                5
        ) {
            @Override
            public void update(double deltaTime) {
                boolean wasHit = isHit();
                super.update(deltaTime);
                if (!wasHit && isHit()) {
                    target.applySlow(slowMultiplier, slowDurationMs);
                }
            }
        };
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
        double x = getTileX() * GameConfig.TILE_SIZE;
        double y = getTileY() * GameConfig.TILE_SIZE;
        double s = GameConfig.TILE_SIZE;
        double cx = x + s / 2.0;
        double cy = y + s / 2.0;

        // ice, diamond
        gc.setFill(Color.LIGHTCYAN);
        gc.fillPolygon(
                new double[]{cx, cx + 10, cx, cx - 10},
                new double[]{cy - 12, cy, cy + 12, cy},
                4
        );
    }
}