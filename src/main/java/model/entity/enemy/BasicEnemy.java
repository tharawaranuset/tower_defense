package model.entity.enemy;

import javafx.scene.paint.Color;
import model.TilePoint;

import java.util.List;

public class BasicEnemy extends Enemy {

    public BasicEnemy(List<TilePoint> path) {
        super(
                path.getFirst().getCol(),
                path.getFirst().getRow(),
                100,
                2.5,
                10,
                path
        );
    }

    @Override
    protected Color getBodyColor() {
        return Color.TOMATO;
    }
}