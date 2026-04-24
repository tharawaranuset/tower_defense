package model.interfaces;

import model.entity.enemy.Enemy;

public interface Attackable {
    void attack(Enemy e);
    int getRange();
    int getDamage();
}
