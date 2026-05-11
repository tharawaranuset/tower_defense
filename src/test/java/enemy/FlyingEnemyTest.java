package enemy;

import model.TilePoint;
import model.entity.enemy.FlyingEnemy;
import model.entity.enemy.BasicEnemy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlyingEnemyTest {

    private FlyingEnemy flying;
    private BasicEnemy  basic;
    private List<TilePoint> path;

    @BeforeEach
    void setup() {
        path   = List.of(new TilePoint(0, 0), new TilePoint(5, 0));
        flying = new FlyingEnemy(path);
        basic  = new BasicEnemy(path);
    }

    @Test
    void hp_lowerThanBasicEnemy() {
        assertTrue(flying.getHp() < basic.getHp());
    }

    @Test
    void reward_higherThanBasicEnemy() {
        assertTrue(flying.getReward() > basic.getReward());
    }

    @Test
    void isDead_initiallyFalse() {
        assertFalse(flying.isDead());
    }

    @Test
    void takeDamage_reducesHp() {
        int before = flying.getHp();
        flying.takeDamage(20);
        assertEquals(before - 20, flying.getHp());
    }

    @Test
    void isDead_trueWhenHpZero() {
        flying.takeDamage(9999);
        assertTrue(flying.isDead());
    }

    @Test
    void move_changesPixelPosition() {
        double startX = flying.getPixelX();
        flying.move(0.5);
        assertNotEquals(startX, flying.getPixelX());
    }

    @Test
    void applySlow_worksOnFlyingEnemy() {
        flying.applySlow(0.4, 2000);
        assertTrue(flying.isSlowed());
    }
}