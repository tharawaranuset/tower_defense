package model.entity;

import model.interfaces.Renderable;

public abstract class Entity implements Renderable {
    protected int tileX;
    protected int tileY;
    protected int hp;
    // TODO: turn maxHp to final
    protected int maxHp;

    public Entity(int tileX, int tileY, int hp) {
        setTileX(tileX);
        setTileY(tileY);
        setHp(hp);
        setMaxHp(hp);
    }

    // TODO: define range of setter
    // --- Getter and Setter ---

    public int getTileX() {
        return tileX;
    }

    public void setTileX(int tileX) {
        this.tileX = tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public void setTileY(int tileY) {
        this.tileY = tileY;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }
}
