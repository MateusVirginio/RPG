package editor;

import java.util.ArrayList;
import java.util.List;

public class MonsterData {
    private String monsterToPlaceName;
    private int monsterToPlaceMaxLife;
    private int monsterToPlaceSpeed;

    private final List<MonsterPlacement> placedMonsters = new ArrayList<>();

    public void addMonsterToPlace(String name, int maxLife, int speed) {
        this.monsterToPlaceName = name;
        this.monsterToPlaceMaxLife = maxLife;
        this.monsterToPlaceSpeed = speed;
    }

    public boolean isMonsterToPlace() {
        return monsterToPlaceName != null;
    }

    public void placeMonster(int col, int row) {
        if (isMonsterToPlace()) {
            placedMonsters.add(new MonsterPlacement(monsterToPlaceName, monsterToPlaceMaxLife, monsterToPlaceSpeed, col, row));
            monsterToPlaceName = null;
        }
    }

    public List<MonsterPlacement> getPlacedMonsters() {
        return placedMonsters;
    }

    public void clearMonsters() {
        placedMonsters.clear();
    }

    public static class MonsterPlacement {
        public String name;
        public int maxLife;
        public int speed;
        public int col;
        public int row;

        public MonsterPlacement(String name, int maxLife, int speed, int col, int row) {
            this.name = name;
            this.maxLife = maxLife;
            this.speed = speed;
            this.col = col;
            this.row = row;
        }
    }
}