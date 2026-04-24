package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import model.entity.enemy.Enemy;
import model.entity.tower.Tower;
import model.interfaces.Renderable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.Main;
import static main.Main.TILE_SIZE;

public class GameMap implements Renderable {

    // grass
    public static final int GRASS = 0;
    // enemy path tile
    public static final int PATH = 1;
    // tower tile
    public static final int TOWER = 2;
    private final int[][] grid;
    private final List<Tower> towers;
    private final List<Enemy> enemies;
    // thread safe list, because often updating
    private final List<Projectile> projectiles =
            Collections.synchronizedList(new ArrayList<>());
    // enemy path
    private List<TilePoint> path;

    public GameMap() {
        grid = new int[Main.ROWS][Main.COLS];
        towers = Collections.synchronizedList(new ArrayList<>());
        enemies = Collections.synchronizedList(new ArrayList<>());
        buildMap();
    }

    // กำหนด layout ของ map และ path ที่ enemy เดิน
    private void buildMap() {
        path = new ArrayList<>();

        // column, row
        int[][] enemyPath = {
                {0, 2}, {1, 2}, {2, 2}, {3, 2}, {4, 2},
                {4, 3}, {4, 4}, {4, 5},
                {3, 5}, {2, 5}, {1, 5}, {0, 5},
                {0, 6}, {0, 7},
                {1, 7}, {2, 7}, {3, 7}, {4, 7}, {5, 7}, {6, 7},
                {6, 6}, {6, 5}, {6, 4}, {6, 3}, {6, 2},
                {6, 2}, {7, 2}, {8, 2}, {9, 2}, {10, 2},
                {10, 3}, {10, 4}, {10, 5}, {10, 6}, {10, 7},
                {11, 7}, {12, 7}, {13, 7}, {14, 7}, {15, 7}
        };

        for (int[] p : enemyPath) {
            grid[p[1]][p[0]] = PATH;
            path.add(new TilePoint(p[0], p[1]));
        }
    }

    public boolean canPlace(int col, int row) {
        if (col < 0 || col >= Main.COLS) return false;
        if (row < 0 || row >= Main.ROWS) return false;
        return grid[row][col] == GRASS;
    }

    public void addTower(Tower t) {
        grid[t.getTileY()][t.getTileX()] = TOWER; // tower tile
        towers.add(t);
    }

    public void addEnemy(Enemy e) {
        enemies.add(e);
    }

    public void removeDead() {
        enemies.removeIf(Enemy::isDead);
    }

    public List<Enemy> getEnemiesInRange(Tower t) {
        List<Enemy> result = new ArrayList<>();
        // tower location
        double cx = t.getTileX() * TILE_SIZE + TILE_SIZE / 2.0;
        double cy = t.getTileY() * TILE_SIZE + TILE_SIZE / 2.0;
        double rangePixel = t.getRange() * TILE_SIZE;

        for (Enemy e : enemies) {
            double dx = e.getPixelX() - cx;
            double dy = e.getPixelY() - cy;
            // Euclidean distance
            double dist = Math.sqrt(dx * dx + dy * dy);
            if (dist <= rangePixel) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public void render(GraphicsContext gc) {
        for (int r = 0; r < Main.ROWS; r++) {
            for (int c = 0; c < Main.COLS; c++) {
                switch (grid[r][c]) {
                    case PATH -> gc.setFill(Color.web("#8B7355")); // enemy tile
                    case TOWER -> gc.setFill(Color.web("#555555")); // tower tile
                    default -> gc.setFill(Color.web("#4a7c59")); // grass
                }
                gc.fillRect(c * TILE_SIZE, r * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                gc.setStroke(Color.color(0, 0, 0, 0.15));
                gc.setLineWidth(0.5);
                gc.strokeRect(c * TILE_SIZE, r * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    public int[][] getGrid() {
        return grid;
    }

    public List<TilePoint> getPath() {
        return path;
    }

    public List<Tower> getTowers() {
        return towers;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void addProjectile(Projectile p) {
        projectiles.add(p);
    }

    public void removeHitProjectiles() {
        projectiles.removeIf(Projectile::isHit);
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

}