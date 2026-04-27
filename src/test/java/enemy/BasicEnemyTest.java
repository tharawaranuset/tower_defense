package enemy;

import model.TilePoint;
import model.entity.enemy.BasicEnemy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BasicEnemyTest {

    private BasicEnemy enemy;
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
        enemy = new BasicEnemy(path);
    }

    @Test
    void testInitialHp() {
        assertEquals(100, enemy.getHp());
    }

    @Test
    void testTakeDamage() {
        enemy.takeDamage(30);
        assertEquals(70, enemy.getHp());
    }

    @Test
    void testTakeDamageMultipleTimes() {
        enemy.takeDamage(30);
        enemy.takeDamage(20);
        assertEquals(50, enemy.getHp());
    }

    @Test
    void testHpIsNotBelowZero() {
        enemy.takeDamage(999);
        assertEquals(0, enemy.getHp());
    }

    @Test
    void testInitialIsDead() {
        assertFalse(enemy.isDead());
    }

    @Test
    void testIsDead() {
        enemy.takeDamage(100);
        assertTrue(enemy.isDead());
    }

    @Test
    void testReturnReward() {
        assertEquals(10, enemy.getReward());
    }

    @Test
    void testInitialHasReachedEnd() {
        assertFalse(enemy.hasReachedEnd());
    }

    @Test
    void testMove() {
        double startX = enemy.getPixelX();
        double startY = enemy.getPixelY();

        enemy.move(0.5);

        boolean moved = enemy.getPixelX() != startX || enemy.getPixelY() != startY;
        assertTrue(moved);
    }

    @Test
    void testMoveWhenDead() {
        enemy.takeDamage(999);
        double x = enemy.getPixelX();
        double y = enemy.getPixelY();
        enemy.move(1.0);
        assertEquals(x, enemy.getPixelX());
        assertEquals(y, enemy.getPixelY());
    }

    @Test
    void testSlow() {
        enemy.applySlow(0.5, 2000);
        assertTrue(enemy.isSlowed());
    }
}