package tower;

import model.Projectile;
import model.SplashProjectile;
import model.TilePoint;
import model.entity.enemy.BasicEnemy;
import model.entity.enemy.Enemy;
import model.entity.tower.CannonTower;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// test CannonTower - splash damage, enemiesRef, cooldown ยาวกว่า
class CannonTowerTest {

    private CannonTower tower;
    private Enemy primaryTarget;
    private Enemy nearbyEnemy;
    private List<Enemy> enemies;
    private List<TilePoint> path;

    @BeforeEach
    void setup() {
        path = List.of(new TilePoint(5, 5));
        tower = new CannonTower(3, 3);

        primaryTarget = new BasicEnemy(path);
        nearbyEnemy = new BasicEnemy(path);

        enemies = new ArrayList<>();
        enemies.add(primaryTarget);
        enemies.add(nearbyEnemy);

        tower.setEnemiesRef(enemies);
    }

    // attack โดยไม่มี enemiesRef ต้อง return null
    @Test
    void attack_withoutEnemiesRef_returnsNull() {
        CannonTower noRef = new CannonTower(1, 1);
        Projectile p = noRef.attack(primaryTarget);
        assertNull(p);
    }

    // attack เมื่อมี enemiesRef ครบต้อง return SplashProjectile
    @Test
    void attack_withEnemiesRef_returnsSplashProjectile() {
        Projectile p = tower.attack(primaryTarget);
        assertNotNull(p);
        assertInstanceOf(SplashProjectile.class, p);
    }

    // damage ต้องสูงกว่า ArrowTower เพราะเป็น splash
    @Test
    void getDamage_higherThanArrow() {
        assertTrue(tower.getDamage() >= 20);
    }

    // cooldown ต้องนานกว่า ArrowTower เพราะ trade-off กับ splash
    @Test
    void attack_duringCooldown_returnsNull() {
        tower.attack(primaryTarget);
        Projectile p = tower.attack(primaryTarget);
        assertNull(p);
    }

    // splash ต้อง deal damage กับ enemy ที่อยู่ใกล้ target ด้วย
    @Test
    void splashProjectile_damagesNearbyEnemies() {
        int hpBefore = nearbyEnemy.getHp();
        Projectile p = tower.attack(primaryTarget);
        assertNotNull(p);

        // simulate projectile ถึง target
        for (int i = 0; i < 1000; i++) {
            p.update(0.1);
            if (p.isHit()) break;
        }

        assertTrue(nearbyEnemy.getHp() < hpBefore);
    }
}