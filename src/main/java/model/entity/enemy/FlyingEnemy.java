package model.entity.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.TilePoint;
import main.Main;
import util.GameConfig;
import util.SpriteSheet;
import java.util.List;


public class FlyingEnemy extends Enemy{
    private static final double HOVER_OFFSET = 12.0;

    public FlyingEnemy(List<TilePoint> path) {
        super(
                path.getFirst().getCol(),
                path.getFirst().getRow(),
                80,    // hp - น้อยกว่า Basic เพราะบินหลบได้
                3.5,   // speed - เร็วกว่า Basic นิดหน่อย
                20,    // reward - มากกว่า Basic เพราะจับยากกว่า
                path
        );
        setSpriteSheet(new SpriteSheet("/images/enemy/FlyingEnemy.png", 4));
    }


    @Override
    protected Color getBodyColor() {
        return Color.DEEPSKYBLUE;  // fallback ถ้าไม่มีรูป
    }

    @Override
    public void render(GraphicsContext gc) {
        double size = GameConfig.TILE_SIZE;
        double r    = size / 2.0 - 4;

        // วาด shadow บนพื้น
        gc.setFill(Color.color(0, 0, 0, 0.25));
        gc.fillOval(getPixelX() - r * 0.8, getPixelY() - r * 0.4, r * 1.6, r * 0.8);

        // เลื่อนขึ้นก่อนวาดตัว
        gc.save();
        gc.translate(0, -HOVER_OFFSET);

        if (getSpriteSheet() != null) {
            getSpriteSheet().draw(gc, getPixelX(), getPixelY(), size);
        } else {
            gc.setFill(getBodyColor());
            gc.fillOval(getPixelX() - r, getPixelY() - r, r * 2, r * 2);

            // วาดปีก
            gc.setFill(Color.color(0.5, 0.8, 1.0, 0.7));
            gc.fillOval(getPixelX() - r * 2, getPixelY() - r * 0.5, r * 1.2, r * 0.8);
            gc.fillOval(getPixelX() + r * 0.8, getPixelY() - r * 0.5, r * 1.2, r * 0.8);
        }

        gc.restore();

        // hp bar วาดเหนือตัวที่ลอยขึ้นด้วย
        drawHpBar(gc, r);
    }
}

