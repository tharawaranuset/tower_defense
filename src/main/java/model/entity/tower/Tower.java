package model.entity.tower;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.Projectile;
import model.entity.Entity;
import model.entity.enemy.Enemy;
import interfaces.Attackable;
import util.GameConfig;
import util.SoundManager;

/**
 * Tower.java
 * abstract class ของ tower ทุกประเภท จัดการ cooldown และ range
 * subclass กำหนด attack behavior และ render เอง
 */
public abstract class Tower extends Entity implements Attackable {

    private static final int PADDING = 4;

    protected int range;
    protected int damage;
    protected int cost;

    // prevent a tower fires every frame, using cooldown (ms)
    protected long fireRateMs;
    protected long lastFiredAt = 0;

    public Tower(int tileX, int tileY, int hp, int range, int damage, int cost, long fireRateMs) {
        super(tileX, tileY, hp);
        this.range = range;
        this.damage = damage;
        this.cost = cost;
        this.fireRateMs = fireRateMs;
    }

    public boolean isNotReady() {
        return System.currentTimeMillis() - lastFiredAt < fireRateMs;
    }

    protected void resetCooldown() {
        lastFiredAt = System.currentTimeMillis();
        SoundManager.getInstance().play(this);
    }

    @Override
    public void render(GraphicsContext gc) {
        double x = tileX * GameConfig.TILE_SIZE;
        double y = tileY * GameConfig.TILE_SIZE;
        double s = GameConfig.TILE_SIZE;

        // base platform
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(x + PADDING, y + PADDING, s - PADDING * 2, s - PADDING * 2);

        // hp bar
        double barW   = s - PADDING * 2;
        double filled = barW * ((double) hp / maxHp);
        gc.setFill(Color.DARKRED);
        gc.fillRect(x + PADDING, y + s - PADDING * 2, barW, PADDING);
        gc.setFill(Color.LIMEGREEN);
        gc.fillRect(x + PADDING, y + s - PADDING * 2, filled, PADDING);

        renderRange(gc);
    }

    // draw tower's range circle
    public void renderRange(GraphicsContext gc) {
        double cx = (tileX * GameConfig.TILE_SIZE) + (GameConfig.TILE_SIZE / 2.0);
        double cy = (tileY * GameConfig.TILE_SIZE) + (GameConfig.TILE_SIZE / 2.0);
        double r = range * GameConfig.TILE_SIZE;
        gc.setFill(Color.color(1, 1, 0, 0.06));
        gc.fillOval(cx - r, cy - r, r * 2, r * 2);
        gc.setStroke(Color.color(1, 1, 0, 0.3));
        gc.setLineWidth(1);
        gc.strokeOval(cx - r, cy - r, r * 2, r * 2);
    }

    @Override
    public abstract Projectile attack(Enemy target);

    // ----------------------------------------------------------------
    // Getter and Setter
    // ----------------------------------------------------------------

    @Override
    public int getRange() {
        return range;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    public int getCost() {
        return cost;
    }

}
