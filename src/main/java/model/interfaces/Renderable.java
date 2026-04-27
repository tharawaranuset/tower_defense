package model.interfaces;

import javafx.scene.canvas.GraphicsContext;

/**
 * Renderable.java
 * interface สำหรับ object ที่วาดตัวเองบน canvas ได้ - Tower และ Enemy implement
 */
public interface Renderable {
    void render(GraphicsContext gc);
}
