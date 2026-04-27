package model.entity.tower;

/**
 * TowerFactory.java
 * สร้าง Tower object ตาม type ที่กำหนด ใช้ Factory pattern
 * เพื่อให้ GameController ไม่ต้องรู้จัก Tower subclass โดยตรง
 */
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