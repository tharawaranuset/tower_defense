package model.entity;

import javafx.scene.canvas.GraphicsContext;
import interfaces.Renderable;

/**
 * Entity.java
 * abstract class หลักของทุก object ในเกม เก็บ position และ hp ที่ทุก entity ต้องมี
 */
public abstract class Entity implements Renderable {
    protected int tileX;
    protected int tileY;
    protected int hp;
    protected final int maxHp;

    public Entity(int tileX, int tileY, int hp) {
        this.tileX = tileX;
        this.tileY = tileY;
        this.hp = hp;
        this.maxHp = hp;
    }

    @Override
    public abstract void render(GraphicsContext gc);

    // ----------------------------------------------------------------
    // Getter and Setter
    // ----------------------------------------------------------------

    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }


}
