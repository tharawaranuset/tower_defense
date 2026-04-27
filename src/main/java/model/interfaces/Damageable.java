package model.interfaces;

/**
 * Damageable.java
 * interface สำหรับ object ที่รับ damage ได้ - Enemy ทุกประเภท implement
 */
public interface Damageable {
    void takeDamage(int damage);

    boolean isDead();
}
