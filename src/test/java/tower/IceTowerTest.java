package tower;

import model.Projectile;
import model.TilePoint;
import model.entity.enemy.BasicEnemy;
import model.entity.enemy.Enemy;
import model.entity.tower.ArrowTower;
import model.entity.tower.IceTower;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IceTowerTest {

    private IceTower iceTower;
    private ArrowTower arrowTower;
    private Enemy enemy;

    @BeforeEach
    void setup() {
        List<TilePoint> path = List.of(new TilePoint(5, 5));
        iceTower = new IceTower(3, 3);
        enemy = new BasicEnemy(path);
    }

    @Test
    void testAttackReturnProjectile() {
        Projectile p = iceTower.attack(enemy);
        assertNotNull(p);
    }

    @Test
    void testDamageLowerThanArrowTower() {
        assertTrue(iceTower.getDamage() < arrowTower.getDamage());
    }

    @Test
    void testWhenProjectileHitShouldSlowEnemy() {
        List<TilePoint> sameTile = List.of(new TilePoint(3, 3));
        Enemy closeEnemy = new BasicEnemy(sameTile);

        Projectile p = iceTower.attack(closeEnemy);
        assertNotNull(p);

        for (int i = 0; i < 1000; i++) {
            p.update(0.1);
            if (p.isHit()) break;
        }

        assertTrue(closeEnemy.isSlowed());
    }

    @Test
    void testApplySlow() {
        enemy.applySlow(0.4, 3000);
        assertTrue(enemy.isSlowed());
    }

    @Test
    void testAttackDuringCooldown() {
        iceTower.attack(enemy);
        Projectile p = iceTower.attack(enemy);
        assertNull(p);
    }
}