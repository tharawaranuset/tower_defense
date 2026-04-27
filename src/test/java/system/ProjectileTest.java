package system;

import javafx.scene.paint.Color;
import model.Projectile;
import model.TilePoint;
import model.entity.enemy.BasicEnemy;
import model.entity.enemy.Enemy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectileTest {

    private Enemy target;
    private Projectile projectile;

    @BeforeEach
    void setup() {
        List<TilePoint> path = List.of(new TilePoint(3, 3));
        // place Enemy on the close Tile with Projectile (easy to hit)
        target = new BasicEnemy(path);
        projectile = new Projectile(
                0,
                0,
                target,
                20,
                500,
                Color.WHITE,
                4
        );
    }

    @Test
    void testInitialIsHit() {
        assertFalse(projectile.isHit());
    }

    @Test
    void testUpdateOnSmallDeltaTime() {
        projectile.update(0.001);
        assertFalse(projectile.isHit());
    }

    @Test
    void testUpdate() {
        for (int i = 0; i < 1000; i++) {
            projectile.update(0.1);
            if (projectile.isHit()) break;
        }
        assertTrue(projectile.isHit());
    }

    @Test
    void testDealDamage() {
        int hpBefore = target.getHp();
        for (int i = 0; i < 1000; i++) {
            projectile.update(0.1);
            if (projectile.isHit()) break;
        }
        assertTrue(target.getHp() < hpBefore);
    }

    @Test
    void testDamageValue() {
        int hpBefore = target.getHp();
        for (int i = 0; i < 1000; i++) {
            projectile.update(0.1);
            if (projectile.isHit()) break;
        }
        assertEquals(hpBefore - 20, target.getHp());
    }

    @Test
    void testTargetDeadBeforeHit() {
        target.takeDamage(9999);
        projectile.update(0.1);
        assertTrue(projectile.isHit());
    }

    @Test
    void testHitOnlyOnce() {
        for (int i = 0; i < 1000; i++) {
            projectile.update(0.1);
            if (projectile.isHit()) break;
        }
        int hpAfterHit = target.getHp();
        projectile.update(1.0);
        assertEquals(hpAfterHit, target.getHp());
    }
}