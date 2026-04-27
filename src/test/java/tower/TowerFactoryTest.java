package tower;

import model.entity.tower.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TowerFactoryTest {

    @Test
    void testCreateArrowTower() {
        Tower t = TowerFactory.create("arrow", 0, 0);
        assertInstanceOf(ArrowTower.class, t);
    }

    @Test
    void testCreateCannonTower() {
        Tower t = TowerFactory.create("cannon", 0, 0);
        assertInstanceOf(CannonTower.class, t);
    }

    @Test
    void testCreateIceTower() {
        Tower t = TowerFactory.create("ice", 0, 0);
        assertInstanceOf(IceTower.class, t);
    }

    @Test
    void testCreateUnknownTower() {
        Tower t = TowerFactory.create("unknown", 0, 0);
        assertNull(t);
    }

    @Test
    void testCorrectPosition() {
        Tower t = TowerFactory.create("arrow", 4, 7);
        assertNotNull(t);
        assertEquals(4, t.getTileX());
        assertEquals(7, t.getTileY());
    }

    @Test
    void testCaseInsensitive() {
        Tower t = TowerFactory.create("ARROW", 0, 0);
        assertInstanceOf(ArrowTower.class, t);
    }
}