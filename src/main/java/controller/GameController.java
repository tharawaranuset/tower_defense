package controller;

import model.GameMap;
import model.Projectile;
import model.entity.enemy.Enemy;
import model.entity.tower.CannonTower;
import model.entity.tower.Tower;
import model.entity.tower.TowerFactory;

import java.util.List;

public class GameController {

    private final GameMap map;
    private final WaveController waveController;
    private int gold = 150;
    private int lives = 10;
    private boolean gameOver = false;

    // deltaTime, to compute enemy movement
    private long lastUpdateTime;

    public GameController() {
        map = new GameMap();
        waveController = new WaveController(map);
        lastUpdateTime = System.currentTimeMillis();
    }

    // GameLoopThread called this every 16 ms (60FPS)
    public void update() {
        if (gameOver) return;

        long now = System.currentTimeMillis();
        double deltaTime = (now - lastUpdateTime) / 1000.0;
        lastUpdateTime = now;

        moveEnemies(deltaTime);
        towerAttack();
        updateProjectiles(deltaTime);
        collectGold();
        checkLives();
        map.removeDead();
        map.removeHitProjectiles();
        checkGameOver();
    }

    private void updateProjectiles(double deltaTime) {
        for (Projectile p : map.getProjectiles()) {
            p.update(deltaTime);
        }
    }

    private void moveEnemies(double deltaTime) {
        for (Enemy e : map.getEnemies()) {
            e.move(deltaTime);
        }
    }

    private void towerAttack() {
        for (Tower t : map.getTowers()) {
            List<Enemy> inRange = map.getEnemiesInRange(t);
            if (inRange.isEmpty()) continue;

            if (t instanceof CannonTower ct) {
                ct.setEnemiesRef(map.getEnemies());
            }

            Projectile p = t.attack(inRange.getFirst());
            if (p != null) map.addProjectile(p);
        }
    }

    private void collectGold() {
        for (Enemy e : map.getEnemies()) {
            if (e.isDead() && !e.hasReachedEnd()) {
                gold += e.getReward();
            }
        }
    }

    private void checkLives() {
        for (Enemy e : map.getEnemies()) {
            if (e.hasReachedEnd()) {
                lives--;
            }
        }
    }

    private void checkGameOver() {
        if (lives <= 0) {
            lives = 0;
            gameOver = true;
        }
    }

    // called in Main, when Button is clicked
    public boolean buyTower(String type, int col, int row) {
        Tower t = TowerFactory.create(type, col, row);
        if (t == null) return false;
        if (gold < t.getCost()) return false;
        if (!map.canPlace(col, row)) return false;

        gold -= t.getCost();
        map.addTower(t);
        return true;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public GameMap getMap() {
        return map;
    }

    public WaveController getWaveController() {
        return waveController;
    }
}