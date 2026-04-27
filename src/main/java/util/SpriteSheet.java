package util;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * SpriteSheet.java
 * จัดการการตัดและแสดง frame จาก sprite sheet แนวนอน
 */
public class SpriteSheet {

    private static final long DEFAULT_FRAME_DELAY_MS = 150;

    private final Image image;
    private final int totalFrames;
    private final int frameWidth;
    private final int frameHeight;
    private int currentFrame = 0;
    private long lastFrameTime = 0;
    private long frameDelayMs = DEFAULT_FRAME_DELAY_MS;

    /**
     * สร้าง SpriteSheet จากไฟล์รูปและจำนวน frame
     *
     * @param path        path ของไฟล์ใน resources เช่น "/images/BasicEnemy.png"
     * @param totalFrames จำนวน frame ทั้งหมดใน sheet
     */
    public SpriteSheet(String path, int totalFrames) {
        Image image1;
        try {
            var stream = getClass().getResourceAsStream(path);
            if (stream == null) {
                System.out.println("Sprite not found: " + path);
                image1 = null;
            } else {
                image1 = new Image(stream);
            }
        } catch (Exception e) {
            System.out.println("Failed to load sprite: " + path);
            image1 = null;
        }
        this.image = image1;
        this.totalFrames = totalFrames;
        this.frameWidth  = image != null ? (int)(image.getWidth() / totalFrames) : GameConfig.TILE_SIZE;
        this.frameHeight = image != null ? (int)(image.getHeight()) : GameConfig.TILE_SIZE;
    }

    /**
     * วาด frame ปัจจุบันลงบน canvas ที่ตำแหน่งและขนาดที่กำหนด
     *
     * @param gc    GraphicsContext ที่ใช้วาด
     * @param destX pixel x ที่จะวาด (จุดกลาง)
     * @param destY pixel y ที่จะวาด (จุดกลาง)
     * @param size  ขนาดที่ต้องการวาด (กว้าง = สูง)
     */
    public void draw(GraphicsContext gc, double destX, double destY, double size) {
        updateFrame();
        double srcX = currentFrame * frameWidth;
        gc.drawImage(
                image,
                srcX, 0, frameWidth, frameHeight,
                destX - size / 2, destY - size / 2, size, size
        );
    }

    private void updateFrame() {
        long now = System.currentTimeMillis();
        if (now - lastFrameTime >= frameDelayMs) {
            currentFrame = (currentFrame + 1) % totalFrames;
            lastFrameTime = now;
        }
    }

    public void setFrameDelayMs(long frameDelayMs) {
        this.frameDelayMs = frameDelayMs;
    }
}