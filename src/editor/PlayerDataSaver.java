package editor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PlayerDataSaver {

    public static void savePlayerData(String filePath, PlayerData playerData) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            String line = String.format("%d,%d,%d,%d", playerData.maxLife, playerData.speed, playerData.startX, playerData.startY);
            bw.write(line);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados do jogador: " + e.getMessage());
        }
    }

    public static PlayerData loadPlayerData(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            if (line != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int maxLife = Integer.parseInt(parts[0]);
                    int speed = Integer.parseInt(parts[1]);
                    int startX = Integer.parseInt(parts[2]);
                    int startY = Integer.parseInt(parts[3]);
                    return new PlayerData(maxLife, speed, startX, startY);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao carregar dados do jogador: " + e.getMessage());
        }
        return null;
    }
}