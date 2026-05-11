package system;

import controller.WaveController;
import model.GameMap;
import model.entity.enemy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WaveControllerTest {

    private WaveController wc;
    private GameMap map;

    @BeforeEach
    void setup() {
        map = new GameMap();
        wc = new WaveController(map);
    }

    @Test
    void testInitialWave() {
        assertEquals(0, wc.getCurrentWave());
    }

    @Test
    void testNextWave() {
        wc.nextWave();
        assertEquals(1, wc.getCurrentWave());
    }

    @Test
    void testQueueEnemyAfterCalledNextWave() {
        wc.nextWave();
        assertFalse(wc.isQueueEmpty());
    }

    @Test
    void testSpawnNextThenQueueSizeIncreased() {
        wc.nextWave();
        int before = wc.getQueueSize();
        wc.spawnNext();
        assertEquals(before - 1, wc.getQueueSize());
    }

    @Test
    void testSpawnNextThenEnemyInMap() {
        wc.nextWave();
        int before = map.getEnemies().size();
        wc.spawnNext();
        assertEquals(before + 1, map.getEnemies().size());
    }

    @Test
    void testSpawnNextWhenQueueEmpty() {
        int before = map.getEnemies().size();
        wc.spawnNext();
        assertEquals(before, map.getEnemies().size());
    }

    @Test
    void testNextWaveHaveEnemyMoreThanLastWave() {
        int wave0Size = wc.getQueueSize();
        wc.nextWave();
        int wave1Size = wc.getQueueSize();
        assertTrue(wave1Size > wave0Size);
    }

    @Test
    void testIsWaveCompleteAfterNextWave() {
        wc.nextWave();
        assertFalse(wc.isWaveComplete());
    }

    @Test
    void testIsWaveComplete() {
        assertTrue(wc.isWaveComplete());
    }

    @Test
    void testClickOnNextWaveTwice() {
        wc.nextWave();
        int wave = wc.getCurrentWave();
        wc.nextWave();  // should ignore
        assertEquals(wave, wc.getCurrentWave());
    }

    @Test
    void testNumberOfEnemyInWave1() {
        wc.nextWave();

        while (!wc.isQueueEmpty()) {
            wc.spawnNext();
        }

        List<Enemy> enemies = map.getEnemies();

        Map<Class<? extends Enemy>, Integer> countMap = new HashMap<>();

        for (Enemy enemy : enemies) {
            Class<? extends Enemy> type = enemy.getClass();
            countMap.put(type, countMap.getOrDefault(type, 0) + 1);
        }

        assertEquals(7, countMap.getOrDefault(BasicEnemy.class, 0));
        assertEquals(0, countMap.getOrDefault(FastEnemy.class, 0));
        assertEquals(0, countMap.getOrDefault(BossEnemy.class, 0));
    }

    @Test
    void testNumberOfEnemyInWave2() {
        wc.setCurrentWave(1);
        wc.nextWave();

        while (!wc.isQueueEmpty()) {
            wc.spawnNext();
        }

        List<Enemy> enemies = map.getEnemies();

        Map<Class<? extends Enemy>, Integer> countMap = new HashMap<>();

        for (Enemy enemy : enemies) {
            Class<? extends Enemy> type = enemy.getClass();
            countMap.put(type, countMap.getOrDefault(type, 0) + 1);
        }

        assertEquals(9, countMap.getOrDefault(BasicEnemy.class, 0));
        assertEquals(4, countMap.getOrDefault(FastEnemy.class, 0));
        assertEquals(0, countMap.getOrDefault(BossEnemy.class, 0));
    }

    @Test
    void testNumberOfEnemyInWave3() {
        wc.setCurrentWave(2);
        wc.nextWave();

        while (!wc.isQueueEmpty()) {
            wc.spawnNext();
        }

        List<Enemy> enemies = map.getEnemies();

        Map<Class<? extends Enemy>, Integer> countMap = new HashMap<>();

        for (Enemy enemy : enemies) {
            Class<? extends Enemy> type = enemy.getClass();
            countMap.put(type, countMap.getOrDefault(type, 0) + 1);
        }

        assertEquals(11, countMap.getOrDefault(BasicEnemy.class, 0));
        assertEquals(6, countMap.getOrDefault(FastEnemy.class, 0));
        assertEquals(6, countMap.getOrDefault(FlyingEnemy.class, 0));
        assertEquals(3, countMap.getOrDefault(BossEnemy.class, 0));
    }

    @Test
    void nextWave_wave2HasFlyingEnemies() {
        wc.setCurrentWave(2);
        wc.nextWave();

        boolean hasFlyingEnemy = false;
        while (!wc.isQueueEmpty()) {
            wc.spawnNext();
        }
        for (Enemy e : map.getEnemies()) {
            if (e instanceof FlyingEnemy) {
                hasFlyingEnemy = true;
                break;
            }
        }
        assertTrue(hasFlyingEnemy);
    }
}