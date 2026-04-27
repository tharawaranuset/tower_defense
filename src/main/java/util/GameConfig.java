package util;

/**
 * GameConfig.java
 * เก็บ constant ที่ใช้ร่วมกันทั้งโปรเจค เช่น ขนาด tile และจำนวน grid
 */
public class GameConfig {

    public static final int TILE_SIZE = 48;
    public static final int COLS = 16;
    public static final int ROWS = 10;

    private GameConfig() {
    }  // prevent new GameConfig()
}
