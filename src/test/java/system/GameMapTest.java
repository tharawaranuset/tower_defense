package system;

import model.GameMap;
import model.TilePoint;
import model.entity.enemy.BasicEnemy;
import model.entity.enemy.Enemy;
import model.entity.tower.ArrowTower;
import model.entity.tower.Tower;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameMapTest {

    private GameMap map;

    @BeforeEach
    void setup() {
        map = new GameMap();
    }

    @Test
    void testCanPlaceOnePathTiles() {
        assertFalse(map.canPlace(0, 2));
        assertFalse(map.canPlace(1, 7));
        assertFalse(map.canPlace(6, 7));
        assertFalse(map.canPlace(15, 7));
    }

    @Test
    void testCanPlaceOnGrass() {
        assertTrue(map.canPlace(0, 1));
        assertTrue(map.canPlace(15, 5));
    }

    @Test
    void testCanPlaceOnTower() {
        Tower t = new ArrowTower(0, 1);
        map.addTower(t);
        assertFalse(map.canPlace(0, 1));
    }

    @Test
    void testCanPlaceOnOutOfBoundary() {
        assertFalse(map.canPlace(-1, 0));
        assertFalse(map.canPlace(0, -1));
        assertFalse(map.canPlace(100, 0));
        assertFalse(map.canPlace(0, 100));
    }

    @Test
    void testAddTower() {
        int before = map.getTowers().size();
        map.addTower(new ArrowTower(0, 1));
        assertEquals(before + 1, map.getTowers().size());
    }

    @Test
    void testAddEnemy() {
        int before = map.getEnemies().size();
        map.addEnemy(new BasicEnemy(map.getPath()));
        assertEquals(before + 1, map.getEnemies().size());
    }

    @Test
    void testRemoveDead() {
        Enemy alive = new BasicEnemy(map.getPath());
        Enemy dead = new BasicEnemy(map.getPath());
        dead.takeDamage(9999);

        map.addEnemy(alive);
        map.addEnemy(dead);
        map.removeDead();

        assertTrue(map.getEnemies().contains(alive));
        assertFalse(map.getEnemies().contains(dead));
    }

    @Test
    void testGetEnemiesInRange() {
        Tower tower = new ArrowTower(0, 1);
        map.addTower(tower);

        List<TilePoint> closePath = List.of(new TilePoint(1, 1));
        Enemy close = new BasicEnemy(closePath);
        map.addEnemy(close);

        List<Enemy> inRange = map.getEnemiesInRange(tower);
        assertTrue(inRange.contains(close));
    }

    @Test
    void testGetEnemiesOutOfRange() {
        Tower tower = new ArrowTower(0, 1);
        map.addTower(tower);

        List<TilePoint> farPath = List.of(new TilePoint(15, 9));
        Enemy far = new BasicEnemy(farPath);
        map.addEnemy(far);

        List<Enemy> inRange = map.getEnemiesInRange(tower);
        assertFalse(inRange.contains(far));
    }

    @Test
    void testGetPath() {
        assertFalse(map.getPath().isEmpty());
    }
}