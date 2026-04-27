package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.entity.enemy.Enemy;
import model.interfaces.Renderable;

/**
 * Projectile.java
 * กระสุนที่ tower ยิงออกไป เคลื่อนที่ตาม target และ deal damage เมื่อชน
 */
public class Projectile implements Renderable {

    private final Enemy target;
    private final int damage;
    private final double speed; // pixel per second
    private final Color color;
    private final double radius;
    private double x;
    private double y;
    private boolean hit = false;

    public Projectile(double startX, double startY, Enemy target, int damage, double speed, Color color, double radius) {
        this.x = startX;
        this.y = startY;
        this.target = target;
        this.damage = damage;
        this.speed = speed;
        this.color = color;
        this.radius = radius;
    }

    public void update(double deltaTime) {
        if (hit || target.isDead()) {
            hit = true;
            return;
        }

        double dx = target.getPixelX() - x;
        double dy = target.getPixelY() - y;
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist <= speed * deltaTime) {
            // reach target
            x = target.getPixelX();
            y = target.getPixelY();
            target.takeDamage(damage);
            hit = true;
        } else {
            x += (dx / dist) * speed * deltaTime;
            y += (dy / dist) * speed * deltaTime;
        }
    }

    public void render(GraphicsContext gc) {
        if (hit) return;
        gc.setFill(color);
        gc.fillOval(
                x - radius,
                y - radius,
                radius * 2,
                radius * 2
        );
    }

    public boolean isHit() {
        return hit;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getDamage() {
        return damage;
    }
}