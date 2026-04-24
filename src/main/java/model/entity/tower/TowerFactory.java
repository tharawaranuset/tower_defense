package model.entity.tower;

public class TowerFactory {

    public static Tower create(String type, int col, int row) {
        return switch (type.toLowerCase()) {
            case "arrow" -> new ArrowTower(col, row);
            case "cannon" -> new CannonTower(col, row);
            case "ice" -> new IceTower(col, row);
            default -> null;
        };
    }
}