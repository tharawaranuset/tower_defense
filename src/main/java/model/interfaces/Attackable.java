package model.interfaces;

import model.Projectile;
import model.entity.enemy.Enemy;

/**
 * Attackable.java
 * interface สำหรับ object ที่สามารถโจมตีได้ - Tower ทุกประเภท implement
 */
public interface Attackable {
    Projectile attack(Enemy e);

    int getRange();

    int getDamage();
}
