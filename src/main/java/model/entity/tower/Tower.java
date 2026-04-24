package model.entity.tower;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.Main;
import model.Projectile;
import model.entity.Entity;
import model.entity.enemy.Enemy;
import model.interfaces.Attackable;

public abstract class Tower extends Entity implements Attackable {
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
    }

    @Override
    public void render(GraphicsContext gc) {
        double x = tileX * Main.TILE_SIZE;
        double y = tileY * Main.TILE_SIZE;
        double s = Main.TILE_SIZE;

        // base platform
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(x + 4, y + 4, s - 8, s - 8);

        // hp bar
        double barW = s - 8;
        double filled = barW * ((double) hp / maxHp);
        gc.setFill(Color.DARKRED);
        gc.fillRect(x + 4, y + s - 8, barW, 4);
        gc.setFill(Color.LIMEGREEN);
        gc.fillRect(x + 4, y + s - 8, filled, 4);

        // tower range
        renderRange(gc);
    }

    // draw tower's range circle
    public void renderRange(GraphicsContext gc) {
        double cx = (tileX * Main.TILE_SIZE) + (Main.TILE_SIZE / 2.0);
        double cy = (tileY * Main.TILE_SIZE) + (Main.TILE_SIZE / 2.0);
        double r = range * Main.TILE_SIZE;
        gc.setFill(Color.color(1, 1, 0, 0.06));
        gc.fillOval(cx - r, cy - r, r * 2, r * 2);
        gc.setStroke(Color.color(1, 1, 0, 0.3));
        gc.setLineWidth(1);
        gc.strokeOval(cx - r, cy - r, r * 2, r * 2);
    }

    @Override
    public abstract void attack(Enemy target);

    public abstract Projectile createProjectile(Enemy target);

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
