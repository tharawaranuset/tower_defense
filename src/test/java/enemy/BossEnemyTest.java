package enemy;

import model.TilePoint;
import model.entity.enemy.BasicEnemy;
import model.entity.enemy.BossEnemy;
import model.entity.enemy.FastEnemy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BossEnemyTest {

    private BossEnemy boss;
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
        boss = new BossEnemy(path);
        basic = new BasicEnemy(path);
    }

    @Test
    void testHpIsHigherThanBasicEnemy() {
        assertTrue(boss.getHp() > basic.getHp());
    }

    @Test
    void testRewardIsHigherThanBasicEnemy() {
        assertTrue(boss.getReward() > basic.getReward());
    }

    @Test
    void testArmor() {
        int hpBefore = boss.getHp();
        int rawDamage = 20;
        boss.takeDamage(rawDamage);

        int actualReduction = hpBefore - boss.getHp();
        assertTrue(actualReduction < rawDamage);
    }

    @Test
    void testTakeMinValueDamage() {
        int hpBefore = boss.getHp();
        boss.takeDamage(1);
        assertTrue(boss.getHp() < hpBefore);
    }

    @Test
    void testInitialArmor() {
        assertEquals(10, boss.getArmor());
    }

    @Test
    void testSetArmor() {
        boss.setArmor(20);
        assertEquals(20, boss.getArmor());
    }

    @Test
    void testIsDead() {
        boss.takeDamage(9999);
        assertTrue(boss.isDead());
    }

    @Test
    void testSlow() {
        boss.applySlow(0.5, 2000);
        assertTrue(boss.isSlowed());
    }

    @Test
    void testSpeedIsSlowerThanBasicEnemy() {
        double bossStartX = boss.getPixelX();
        double basicStartX = basic.getPixelX();

        boss.move(0.3);
        basic.move(0.3);

        double bossDist = Math.abs(boss.getPixelX() - bossStartX);
        double fastDist = Math.abs(basic.getPixelX() - basicStartX);

        assertTrue(bossDist < fastDist);
    }
}