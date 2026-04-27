package system;

import controller.GameController;
import model.GameMap;
import model.entity.enemy.BasicEnemy;
import model.entity.enemy.Enemy;
import model.entity.tower.ArrowTower;
import model.entity.tower.Tower;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    private GameController controller;

    @BeforeEach
    void setup() {
        controller = new GameController();
    }

    @Test
    void testInitialGold() {
        assertEquals(150, controller.getGold());
    }

    // lives เริ่มต้นถูกต้อง
    @Test
    void testInitialLives() {
        assertEquals(10, controller.getLives());
    }

    @Test
    void testInitialIsGameOver() {
        assertFalse(controller.isGameOver());
    }

    @Test
    void testBuyTowerOnGrassTile() {
        assertTrue(controller.buyTower("arrow", 0, 1));
    }

    @Test
    void testGoldAfterBuyTower() {
        int before = controller.getGold();
        controller.buyTower("arrow", 0, 1);
        Tower t = new ArrowTower(0, 1);
        assertEquals(before - t.getCost(), controller.getGold());
    }

    @Test
    void testBuyTowerWhenNotEnoughGold() {
        controller.setGold(10);
        int before = controller.getGold();
        boolean result = controller.buyTower("arrow", 0, 1);
        assertFalse(result);
        assertEquals(before, controller.getGold());
    }

    @Test
    void testBuyTowerOnPathTiles() {
        assertFalse(controller.buyTower("arrow", 6, 7));
    }

    @Test
    void testBuyTowerOnTowerTile() {
        controller.buyTower("arrow", 0, 1);
        assertFalse(controller.buyTower("arrow", 0, 1));
    }

    @Test
    void testBuyUnknownTowerType() {
        assertFalse(controller.buyTower("unknown", 0, 1));
    }

    @Test
    void testSetLives() {
        controller.setLives(5);
        assertEquals(5, controller.getLives());
    }

    @Test
    void testIsGameOver() {
        controller.setLives(0);
        controller.update();
        assertTrue(controller.isGameOver());
    }

    @Test
    void testUpdateWithNoEntities() {
        assertDoesNotThrow(() -> controller.update());
    }

    @Test
    void testUpdateAfterGameOver() {
        controller.setLives(0);
        controller.update();
        int goldAfterGameOver = controller.getGold();
        controller.update();
        assertEquals(goldAfterGameOver, controller.getGold());
    }

    @Test
    void testCollectGold() {
        GameController controller = new GameController();
        GameMap map = controller.getMap();

        Enemy enemy = new BasicEnemy(map.getPath());

        enemy.takeDamage(9999);

        map.getEnemies().add(enemy);

        int beforeGold = controller.getGold();

        controller.update();

        assertEquals(beforeGold + enemy.getReward(), controller.getGold());
    }
}