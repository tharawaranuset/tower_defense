package model.entity.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.TilePoint;
import main.Main;
import util.GameConfig;
import util.SpriteSheet;
import java.util.List;


public class FlyingEnemy extends Enemy {
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
        // วาด shadow บนพื้น
        gc.setFill(Color.color(0, 0, 0, 0.25));
        gc.fillOval(getPixelX() - sizeRender * 0.8, getPixelY() - sizeRender * 0.4, sizeRender * 1.6, sizeRender * 0.8);

        // เลื่อนขึ้นก่อนวาดตัว
        gc.save();
        gc.translate(0, -HOVER_OFFSET);

        super.render(gc);
        gc.restore();


    }
}

