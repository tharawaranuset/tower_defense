package tower;

import model.Projectile;
import model.TilePoint;
import model.entity.enemy.BasicEnemy;
import model.entity.enemy.Enemy;
import model.entity.tower.ArrowTower;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArrowTowerTest {

    private ArrowTower tower;
    private Enemy enemy;

    @BeforeEach
    void setup() {
        List<TilePoint> path = List.of(new TilePoint(5, 5));
        tower = new ArrowTower(3, 3);
        enemy = new BasicEnemy(path);
    }

    @Test
    void testAttack() {
        Projectile p = tower.attack(enemy);
        assertNotNull(p);
    }

    @Test
    void testAttackDuringCooldown() {
        tower.attack(enemy);
        Projectile p = tower.attack(enemy);
        assertNull(p);
    }

    @Test
    void testDamageValue() {
        assertEquals(15, tower.getDamage());
    }

    @Test
    void testRangeValue() {
        assertEquals(3, tower.getRange());
    }

    @Test
    void testCostValue() {
        assertEquals(50, tower.getCost());
    }

    @Test
    void testProjectileTarget() {
        Projectile p = tower.attack(enemy);
        assertNotNull(p);
        p.update(0.001);
        assertFalse(p.isHit());
    }

    @Test
    void arrowTower_canHitFlying_isTrue() {
        ArrowTower arrow = new ArrowTower(0, 0);
        assertTrue(arrow.canHitFlying());
    }
}