package model.entity.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.TilePoint;
import model.entity.Entity;
import model.interfaces.Damageable;
import util.GameConfig;

import java.util.List;

public abstract class Enemy extends Entity implements Damageable {
    protected double speed; // tile per second
    protected int reward; // drop gold when Enemy dies
    protected boolean dead = false;
    protected boolean reachedEnd = false;

    // list of tile coordinates which enemy can walk
    protected List<TilePoint> path;
    protected int pathIdx = 0;

    // pixel position to draw
    protected double pixelX;
    protected double pixelY;

    // slow effect from IceTower
    protected double slowMultiplier = 1.0;
    protected long slowUntil = 0;

    public Enemy(int startCol, int startRow, int hp, double speed, int reward, List<TilePoint> path) {
        super(startCol, startRow, hp);
        this.speed = speed;
        this.reward = reward;
        this.path = path;
        // start at middle pixel of start tile
        this.pixelX = (startCol * GameConfig.TILE_SIZE) + (GameConfig.TILE_SIZE / 2.0);
        this.pixelY = (startRow * GameConfig.TILE_SIZE) + (GameConfig.TILE_SIZE / 2.0);
    }

    @Override
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            dead = true;
        }
    }

    public void move(double deltaTime) {
        if (isDead() || pathIdx >= getPath().size()) return;

        // check if slow expired
        if (System.currentTimeMillis() > slowUntil) {
            setSlowMultiplier(1.0);
        }

        TilePoint target = path.get(pathIdx);
        double targetX = (target.getCol() * GameConfig.TILE_SIZE) + (GameConfig.TILE_SIZE / 2.0);
        double targetY = (target.getRow() * GameConfig.TILE_SIZE) + (GameConfig.TILE_SIZE / 2.0);

        double effectiveSpeed = speed * slowMultiplier * GameConfig.TILE_SIZE * deltaTime;

        double dx = targetX - pixelX;
        double dy = targetY - pixelY;
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist <= effectiveSpeed) {
            // reach target, jump to next target
            pixelX = targetX;
            pixelY = targetY;
            pathIdx++;

            if (pathIdx >= path.size()) {
                reachedEnd = true;
                dead = true;
            }
        } else {
            // gradually move to target
            pixelX += (dx / dist) * effectiveSpeed;
            pixelY += (dy / dist) * effectiveSpeed;
        }
    }

    public void applySlow(double multiplier, long durationMs) {
        slowMultiplier = multiplier;
        slowUntil = System.currentTimeMillis() + durationMs;
    }

    public boolean isSlowed() {
        return slowMultiplier < 1.0 && System.currentTimeMillis() < slowUntil;
    }

    @Override
    public void render(GraphicsContext gc) {
        double r = GameConfig.TILE_SIZE / 2.0 - 4;

        // enemy body
        gc.setFill(getBodyColor());
        gc.fillOval(pixelX - r, pixelY - r, r * 2, r * 2);

        // slow indicator effect
        if (slowMultiplier < 1.0) {
            gc.setStroke(Color.CYAN);
            gc.setLineWidth(2);
            gc.strokeOval(pixelX - r, pixelY - r, r * 2, r * 2);
        }

        // hp bar above
        drawHpBar(gc, r);
    }

    protected void drawHpBar(GraphicsContext gc, double r) {
        double barW = r * 2;
        double filled = barW * ((double) getHp() / getMaxHp());
        gc.setFill(Color.DARKRED);
        gc.fillRect(getPixelX() - r, getPixelY() - r - 8, barW, 4);
        gc.setFill(Color.LIMEGREEN);
        gc.fillRect(getPixelX() - r, getPixelY() - r - 8, filled, 4);
    }

    protected abstract Color getBodyColor();

    // ----------------------------------------------------------------
    // Getter and Setter
    // ----------------------------------------------------------------

    @Override
    public boolean isDead() {
        return dead;
    }

    public int getReward() {
        return reward;
    }

    public boolean hasReachedEnd() {
        return reachedEnd;
    }

    public double getPixelX() {
        return pixelX;
    }

    public double getPixelY() {
        return pixelY;
    }

    public double getSpeed() {
        return speed;
    }

    public boolean isReachedEnd() {
        return reachedEnd;
    }

    public List<TilePoint> getPath() {
        return path;
    }

    public int getPathIdx() {
        return pathIdx;
    }

    public double getSlowMultiplier() {
        return slowMultiplier;
    }

    public void setSlowMultiplier(double slowMultiplier) {
        this.slowMultiplier = slowMultiplier;
    }

    public long getSlowUntil() {
        return slowUntil;
    }

}
