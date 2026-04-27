package model.entity.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.TilePoint;
import model.entity.Entity;
import model.interfaces.Damageable;
import util.GameConfig;
import util.SpriteSheet;

import java.util.List;

/**
 * Enemy.java
 * abstract class ของ enemy ทุกประเภท จัดการ movement ตาม path และ slow effect
 */
public abstract class Enemy extends Entity implements Damageable {
    protected double speed; // tile per second
    protected int reward; // drop gold when Enemy dies
    protected boolean dead = false;
    protected boolean reachedEnd = false;

    // list of tile coordinates which enemy can walk
    protected List<TilePoint> path;
    protected int pathIdx = 1;

    // pixel position to draw
    protected double pixelX;
    protected double pixelY;
    protected double sizeRender = GameConfig.TILE_SIZE / 2.0 - 4;

    // slow effect from IceTower
    protected double slowMultiplier = 1.0;
    protected long slowUntil = 0;

    private SpriteSheet spriteSheet;

    public Enemy(int startCol, int startRow, int hp, double speed, int reward, List<TilePoint> path) {
        super(startCol, startRow, hp);
        this.speed = speed;
        this.reward = reward;
        this.path = path;
        // start at middle pixel of start tile
        this.pixelX = (startCol * GameConfig.TILE_SIZE) + (GameConfig.TILE_SIZE / 2.0);
        this.pixelY = (startRow * GameConfig.TILE_SIZE) + (GameConfig.TILE_SIZE / 2.0);

        setSpriteSheet(new SpriteSheet("/images/enemy/" + this.getClass().getSimpleName() + ".png", 4));
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
        if (isDead() || pathIdx >= path.size()) return;

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
        // enemy body
        if (spriteSheet != null) {
            spriteSheet.draw(gc, pixelX, pixelY, sizeRender * 2.0);
        } else {
            gc.setFill(getBodyColor());
            gc.fillOval(pixelX - sizeRender, pixelY - sizeRender, sizeRender * 2, sizeRender * 2);
        }

        // slow indicator effect
        if (isSlowed()) {
            gc.setStroke(Color.CYAN);
            gc.setLineWidth(2);
            gc.strokeOval(pixelX - sizeRender, pixelY - sizeRender, sizeRender * 2, sizeRender * 2);
        }

        // hp bar above
        drawHpBar(gc, sizeRender);
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

    public List<TilePoint> getPath() {
        return path;
    }

    public int getPathIdx() {
        return pathIdx;
    }

    public void setPathIdx(int pathIdx) {
        this.pathIdx = pathIdx;
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

    public void setSpriteSheet(SpriteSheet sheet) {
        this.spriteSheet = sheet;
    }

    public SpriteSheet getSpriteSheet() {
        return spriteSheet;
    }

}
