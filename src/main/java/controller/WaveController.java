package controller;

import model.GameMap;
import model.entity.enemy.BasicEnemy;
import model.entity.enemy.BossEnemy;
import model.entity.enemy.Enemy;
import model.entity.enemy.FastEnemy;

import java.util.LinkedList;
import java.util.Queue;

public class WaveController {

    private int currentWave = 0;
    private final Queue<Enemy> spawnQueue;
    private final GameMap map;
    private boolean waveInProgress = false;

    public WaveController(GameMap map) {
        this.map = map;
        this.spawnQueue = new LinkedList<>();
    }

    public void nextWave() {
        if (waveInProgress) return;   // can't re-pressed when running
        currentWave++;
        buildQueue(currentWave);
        waveInProgress = true;
    }

    // define type of enemy, each wave
    private void buildQueue(int wave) {
        spawnQueue.clear();

        int basicCount = 5 + wave * 2;
        int fastCount = wave >= 2 ? wave * 2 : 0;
        int bossCount = wave >= 3 ? wave / 3 : 0;

        for (int i = 0; i < basicCount; i++) {
            spawnQueue.add(new BasicEnemy(map.getPath()));
        }
        for (int i = 0; i < fastCount; i++) {
            spawnQueue.add(new FastEnemy(map.getPath()));
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