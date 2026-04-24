package model.interfaces;

import model.Projectile;
import model.entity.enemy.Enemy;

public interface Attackable {
    Projectile attack(Enemy e);

    int getRange();

    int getDamage();
}
