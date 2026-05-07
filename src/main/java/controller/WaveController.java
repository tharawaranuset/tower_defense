package controller;

import model.GameMap;
import model.entity.enemy.*;

import java.util.LinkedList;
import java.util.Queue;

/**
 * WaveController.java
 * จัดการ wave ของเกม ควบคุมการ spawn enemy และเช็คว่า wave จบแล้วหรือยัง
 */
public class WaveController {

    private static final int BASE_BASIC_COUNT = 5;
    private static final int BASIC_PER_WAVE = 2;
    private static final int FAST_START_WAVE = 2;
    private static final int FAST_RATIO = 2;
    private static final int BOSS_START_WAVE = 3;
    private static final int BOSS_RATIO = 3;
    private static final int FLY_START_WAVE = 2;
    private static final int FLY_RATIO = 2;

    private int currentWave = 0;
    private final Queue<Enemy> spawnQueue;
    private final GameMap map;
    private boolean waveInProgress = false;

    public WaveController(GameMap map) {
        this.map = map;
        this.spawnQueue = new LinkedList<>();
    }

    public void nextWave() {
        if (isWaveInProgress()) return;   // can't re-pressed when running
        currentWave++;
        buildQueue(currentWave);
        waveInProgress = true;
    }

    // define type of enemy, each wave
    private void buildQueue(int wave) {
        spawnQueue.clear();

        int basicCount = BASE_BASIC_COUNT + wave * BASIC_PER_WAVE;
        int fastCount = wave >= FAST_START_WAVE ? wave * FAST_RATIO : 0;
        int bossCount = wave >= BOSS_START_WAVE ? wave * BOSS_RATIO : 0;
        int flyingCount = wave >= FLY_START_WAVE ? wave * FLY_RATIO : 0;

        for (int i = 0; i < basicCount; i++) {
            spawnQueue.add(new BasicEnemy(map.getPath()));
        }
        for (int i = 0; i < fastCount; i++) {
            spawnQueue.add(new FastEnemy(map.getPath()));
        }
        for (int i = 0; i < flyingCount; i++) {
            spawnQueue.add(new FlyingEnemy(map.getPath()));
        }
        for (int i = 0; i < bossCount; i++) {
            spawnQueue.add(new BossEnemy(map.getPath()));
        }

    }

    // SpawnThread called this method every 2 seconds
    public void spawnNext() {
        if (spawnQueue.isEmpty()) return;
        Enemy e = spawnQueue.poll();
        map.addEnemy(e);
    }

    // called from GameController every update
    public boolean isWaveComplete() {
        boolean complete = spawnQueue.isEmpty() && map.getEnemies().isEmpty();
        if (complete) waveInProgress = false;
        return complete;
    }

    public boolean isWaveInProgress() {
        return waveInProgress;
    }

    public boolean isQueueEmpty() {
        return spawnQueue.isEmpty();
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public void setCurrentWave(int wave) {
        this.currentWave = wave;
    }

    public int getQueueSize() {
        return spawnQueue.size();
    }
}