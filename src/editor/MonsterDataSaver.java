package editor;

import editor.MonsterData.MonsterPlacement;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MonsterDataSaver {

    public static void saveMonstersToFile(String filePath, List<MonsterPlacement> monsters) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (MonsterPlacement monster : monsters) {
                String line = String.format("%s:%d:%d:%d,%d", monster.name, monster.maxLife, monster.speed, monster.col, monster.row);
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar monstros: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void loadMonstersFromFile(String filePath, MonsterData monsterData) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 4) {
                    String name = parts[0];
                    int maxLife = Integer.parseInt(parts[1]);
                    int speed = Integer.parseInt(parts[2]);
                    String[] coords = parts[3].split(",");
                    if (coords.length == 2) {
                        int col = Integer.parseInt(coords[0].trim());
                        int row = Integer.parseInt(coords[1].trim());
                        monsterData.getPlacedMonsters().add(new MonsterPlacement(name, maxLife, speed, col, row));
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao carregar monstros: " + e.getMessage());
            e.printStackTrace();
        }
    }
}