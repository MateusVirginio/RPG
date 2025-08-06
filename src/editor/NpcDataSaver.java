package editor;

import editor.NpcData.NpcPlacement;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class NpcDataSaver {

    public static void saveNpcsToFile(String filePath, List<NpcPlacement> npcs) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (NpcPlacement npc : npcs) {
                StringBuilder line = new StringBuilder();
                line.append(npc.name).append(":");
                for (String dialogue : npc.dialogues) {
                    line.append(dialogue).append("|");
                }
                line.append(":").append(npc.col).append(",").append(npc.row);
                bw.write(line.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar NPCs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void loadNpcsFromFile(String filePath, NpcData npcData) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 3) {
                    String name = parts[0];
                    String[] dialogues = parts[1].split("\\|");
                    String[] coords = parts[2].split(",");
                    if (coords.length == 2) {
                        int col = Integer.parseInt(coords[0].trim());
                        int row = Integer.parseInt(coords[1].trim());
                        npcData.getPlacedNpcs().add(new NpcPlacement(name, dialogues, col, row));
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao carregar NPCs: " + e.getMessage());
            e.printStackTrace();
        }
    }
}