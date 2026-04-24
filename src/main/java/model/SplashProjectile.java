package model;

import javafx.scene.paint.Color;
import model.entity.enemy.Enemy;

import java.util.List;

public class SplashProjectile extends Projectile {

    private final int splashRadius;
    private final List<Enemy> allEnemies;

    public SplashProjectile(
            double startX,
            double startY,
            Enemy target,
            int damage,
            int splashRadius,
            List<Enemy> allEnemies
    ) {
        super(
                startX,
                startY,
                target,
                damage,
                200,
                Color.ORANGERED,
                7
        );
        this.splashRadius = splashRadius;
        this.allEnemies = allEnemies;
    }

    @Override
    public void update(double deltaTime) {
        boolean wasHit = isHit();
        super.update(deltaTime);

        // deal splash damage (deal damage to all enemies) when hit
        if (!wasHit && isHit()) {
            for (Enemy e : allEnemies) {
                double dx = e.getPixelX() - getX();
                double dy = e.getPixelY() - getY();
                double dist = Math.sqrt(dx * dx + dy * dy);
                if (dist <= splashRadius) {
                    e.takeDamage(getDamage());
                }
            }
        }
    }
}