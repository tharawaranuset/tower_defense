package enemy;

import model.TilePoint;
import model.entity.enemy.BasicEnemy;
import model.entity.enemy.FastEnemy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FastEnemyTest {

    private FastEnemy fast;
    private BasicEnemy basic;
    private List<TilePoint> path;

    @BeforeEach
    void setup() {
        path = List.of(
                new TilePoint(0, 0),
                new TilePoint(1, 0),
                new TilePoint(2, 0),
                new TilePoint(3, 0),
                new TilePoint(4, 0),
                new TilePoint(5, 0)
        );
        fast = new FastEnemy(path);
        basic = new BasicEnemy(path);
    }

    @Test
    void testHpIsLowerThanBasicEnemy() {
        assertTrue(fast.getHp() < basic.getHp());
    }

    @Test
    void testRewardIsHigherThanBasicEnemy() {
        assertTrue(fast.getReward() > basic.getReward());
    }

    @Test
    void testMoveFasterThanBasicEnemy() {
        double fastStartX = fast.getPixelX();
        double basicStartX = basic.getPixelX();

        fast.move(0.3);
        basic.move(0.3);

        double fastDist = Math.abs(fast.getPixelX() - fastStartX);
        double basicDist = Math.abs(basic.getPixelX() - basicStartX);

        assertTrue(fastDist > basicDist);
    }

    @Test
    void testIsDead() {
        fast.takeDamage(fast.getHp());
        assertTrue(fast.isDead());
    }

    @Test
    void testSlow() {
        fast.applySlow(0.4, 2000);
        assertTrue(fast.isSlowed());
    }

    @Test
    void testSlowIsExpired() throws InterruptedException {
        fast.applySlow(0.4, 100);
        Thread.sleep(200);
        fast.move(0.01);
        assertFalse(fast.isSlowed());
    }
}